allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

// Redirect build directories
val newBuildDir = rootProject.layout.buildDirectory.dir("../../build").get()
rootProject.layout.buildDirectory.set(newBuildDir)

subprojects {
    val newSubprojectBuildDir = newBuildDir.dir(project.name)
    project.layout.buildDirectory.set(newSubprojectBuildDir)
}

// Force compileSdkVersion and buildToolsVersion on all subprojects
subprojects {
    afterEvaluate {
        if (project.plugins.hasPlugin("com.android.application") ||
            project.plugins.hasPlugin("com.android.library")) {

            // This is the Kotlin DSL way to configure the android block
            project.extensions.configure<com.android.build.gradle.BaseExtension> {
                compileSdkVersion(36)           // or 34 if you prefer
                buildToolsVersion("36.0.0")     // or latest installed
            }
        }
    }
}

// Make sure app is evaluated first
subprojects {
    project.evaluationDependsOn(":app")
}

// Clean task
tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
