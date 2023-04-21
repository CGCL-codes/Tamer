package com.google.code.maven_substitute_plugin.substituters;

import org.apache.maven.project.MavenProject;

public class SubstituterProjectVersion implements Substituter {

    public MavenProject mavenProject;

    public SubstituterProjectVersion(MavenProject mavenProject) {
        this.mavenProject = mavenProject;
    }

    public String getReplacerString() {
        return mavenProject.getVersion();
    }
}
