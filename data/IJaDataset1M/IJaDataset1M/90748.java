package com.ideo.sweetdevria.maven.plugin.merge.util;

import java.util.List;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.deployer.ArtifactDeployer;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.installer.ArtifactInstaller;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;

public class MavenHelper {

    protected ArtifactFactory artifactFactory;

    protected MavenProjectBuilder projectBuilder;

    protected List remoteArtifactRepositories;

    protected List remotePluginRepositories;

    protected ArtifactRepository localArtifactRepository;

    protected org.apache.maven.artifact.resolver.ArtifactResolver resolver;

    private ArtifactDeployer deployer;

    private ArtifactInstaller installer;

    private ArtifactRepository deploymentRepository;

    private static MavenHelper singleton = null;

    private MavenHelper() {
    }

    public static MavenHelper getInstance() {
        if (singleton == null) {
            singleton = new MavenHelper();
        }
        return singleton;
    }

    public MavenProject getMavenProject(Artifact artifact) throws ProjectBuildingException {
        return projectBuilder.buildFromRepository(artifact, this.remoteArtifactRepositories, this.localArtifactRepository);
    }

    public MavenProject getMavenProject(String groupId, String artifactId, String version, String type) throws ProjectBuildingException, ArtifactResolutionException, ArtifactNotFoundException {
        Artifact artifact = createArtifact(groupId, artifactId, version, type);
        return getMavenProject(artifact);
    }

    public Artifact createArtifact(String groupId, String artifactId, String version, String type, String scope) throws ArtifactResolutionException, ArtifactNotFoundException {
        return this.artifactFactory.createArtifact(groupId, artifactId, version, scope, type);
    }

    public Artifact createArtifactWithClassifier(String groupId, String artifactId, String version, String type, String classifier) throws ArtifactResolutionException, ArtifactNotFoundException {
        return this.artifactFactory.createArtifactWithClassifier(groupId, artifactId, version, type, classifier);
    }

    public Artifact createArtifact(String groupId, String artifactId, String version, String type) throws ArtifactResolutionException, ArtifactNotFoundException {
        return createArtifact(groupId, artifactId, version, type, "");
    }

    public void resolveArtifact(Artifact artifact) throws ArtifactResolutionException, ArtifactNotFoundException {
        resolver.resolve(artifact, this.remoteArtifactRepositories, this.localArtifactRepository);
    }

    public ArtifactFactory getArtifactFactory() {
        return artifactFactory;
    }

    public void setArtifactFactory(ArtifactFactory artifactFactory) {
        this.artifactFactory = artifactFactory;
    }

    public MavenProjectBuilder getProjectBuilder() {
        return projectBuilder;
    }

    public void setProjectBuilder(MavenProjectBuilder projectBuilder) {
        this.projectBuilder = projectBuilder;
    }

    public List getRemoteArtifactRepositories() {
        return remoteArtifactRepositories;
    }

    public void setRemoteArtifactRepositories(List remoteArtifactRepositories) {
        this.remoteArtifactRepositories = remoteArtifactRepositories;
    }

    public List getRemotePluginRepositories() {
        return remotePluginRepositories;
    }

    public void setRemotePluginRepositories(List remotePluginRepositories) {
        this.remotePluginRepositories = remotePluginRepositories;
    }

    public ArtifactRepository getLocalArtifactRepository() {
        return localArtifactRepository;
    }

    public void setLocalArtifactRepository(ArtifactRepository localArtifactRepository) {
        this.localArtifactRepository = localArtifactRepository;
    }

    public org.apache.maven.artifact.resolver.ArtifactResolver getResolver() {
        return resolver;
    }

    public void setResolver(org.apache.maven.artifact.resolver.ArtifactResolver resolver) {
        this.resolver = resolver;
    }

    public ArtifactDeployer getDeployer() {
        return deployer;
    }

    public void setDeployer(ArtifactDeployer deployer) {
        this.deployer = deployer;
    }

    public ArtifactInstaller getInstaller() {
        return installer;
    }

    public void setInstaller(ArtifactInstaller installer) {
        this.installer = installer;
    }

    public ArtifactRepository getDeploymentRepository() {
        return deploymentRepository;
    }

    public void setDeploymentRepository(ArtifactRepository deploymentRepository) {
        this.deploymentRepository = deploymentRepository;
    }
}
