package org.grails.forge.feature.grails

import org.grails.forge.BeanContextSpec
import org.grails.forge.BuildBuilder
import org.grails.forge.application.ApplicationType
import org.grails.forge.fixture.CommandOutputFixture
import org.grails.forge.options.BuildTool
import org.grails.forge.options.JdkVersion
import org.grails.forge.options.Language
import org.grails.forge.options.Options
import org.grails.forge.options.TestFramework

class GrailsGradlePluginSpec extends BeanContextSpec implements CommandOutputFixture {

    void "test build gradle file and gradle properties"() {
        when:
        final def output = generate(ApplicationType.WEB, new Options(Language.GROOVY, TestFramework.SPOCK, BuildTool.GRADLE, JdkVersion.JDK_11))
        final String gradleProps = output["gradle.properties"]

        then:
        gradleProps.contains("grailsGradlePluginVersion=6.0.0-RC1")
        gradleProps.contains("grailsVersion=6.0.0-SNAPSHOT")
    }

    void "test dependencies are present for buildSrc"() {
        when:
        final String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .renderBuildSrc()

        then:
        template.contains('implementation("org.grails:grails-gradle-plugin:6.0.0-RC1")')
    }

    void "test buildSrc is present for buildscript dependencies"() {
        given:
        final def output = generate(ApplicationType.WEB, new Options(Language.GROOVY, TestFramework.SPOCK, BuildTool.GRADLE, JdkVersion.JDK_11))
        final def buildSrcBuildGradle = output["buildSrc/build.gradle"]

        expect:
        buildSrcBuildGradle != null
        buildSrcBuildGradle.contains("implementation(\"org.grails:grails-gradle-plugin:6.0.0-RC1\")")

    }

    void "test dependencies are present for Gradle"() {
        when:
        final String template = new BuildBuilder(beanContext)
                .applicationType(ApplicationType.PLUGIN)
                .render()

        then:
        template.contains("id \"org.grails.grails-plugin\"")
    }

}
