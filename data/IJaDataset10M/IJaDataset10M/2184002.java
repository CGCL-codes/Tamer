package org.jfrog.maven.viewer.domain;

import org.apache.maven.project.MavenProject;
import org.jfrog.maven.viewer.common.ArtifactIdentifier;
import java.io.File;

/**
 * User: Dror Bereznitsky
 * Date: 28/11/2006
 * Time: 15:42:58
 */
class MavenProjectWrapper extends AbstractArtifact {

    private MavenProject mavenProject;

    MavenProjectWrapper(MavenProject mavenProject) {
        this.mavenProject = mavenProject;
    }

    MavenProjectWrapper(MavenProject mavenProject, ArtifactDependency dependent) {
        this(mavenProject);
        this.dependency = dependent;
    }

    public File getFile() {
        return mavenProject.getFile();
    }

    public String getPackaging() {
        return mavenProject.getPackaging();
    }

    public boolean resolved() {
        return mavenProject != null;
    }

    public ArtifactIdentifier getIdentifier() {
        if (id == null) {
            id = new ArtifactIdentifier(mavenProject);
        }
        return id;
    }

    public MavenProject getMavenProject() {
        return mavenProject;
    }
}
