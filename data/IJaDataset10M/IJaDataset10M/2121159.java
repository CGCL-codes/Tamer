package org.jfrog.maven.viewer.dao;

import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.project.MavenProject;
import org.jfrog.maven.viewer.common.ArtifactIdentifier;
import org.jfrog.maven.viewer.dao.exception.ProjectCreationException;
import java.io.File;

public interface MavenProjectDao {

    public MavenProject getMavenProject(ArtifactIdentifier artifactIdentifier) throws ProjectCreationException;

    public MavenProject getMavenProject(File pom) throws ProjectCreationException;

    public ArtifactResolutionResult resolveDependencies(MavenProject mavenProject);
}
