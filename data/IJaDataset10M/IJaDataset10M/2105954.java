package org.jfrog.maven.viewer.domain;

import org.jfrog.maven.viewer.common.ArtifactIdentifier;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Dror Bereznitsky
 * Date: Apr 5, 2008
 * Time: 11:34:19 PM
 */
public class MavenArtifactWrapper extends AbstractArtifact {

    private org.apache.maven.artifact.Artifact artifact;

    MavenArtifactWrapper(org.apache.maven.artifact.Artifact artifact) {
        this.artifact = artifact;
    }

    MavenArtifactWrapper(org.apache.maven.artifact.Artifact artifact, ArtifactDependency dependency) {
        this(artifact);
        this.dependency = dependency;
    }

    public ArtifactIdentifier getIdentifier() {
        if (id == null) {
            id = new ArtifactIdentifier(artifact);
        }
        return id;
    }

    public File getFile() {
        return artifact.getFile();
    }

    public String getPackaging() {
        return artifact.getType();
    }

    public boolean resolved() {
        return artifact != null;
    }
}
