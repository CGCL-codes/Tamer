package com.ideo.sweetforge.plugin.merge;

import java.io.File;
import java.util.List;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.deployer.ArtifactDeployer;
import org.apache.maven.artifact.installer.ArtifactInstaller;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import com.ideo.sweetforge.plugin.merge.model.Configuration;
import com.ideo.sweetforge.plugin.merge.service.MavenHelper;

/**
 *
 * @author <a href="mailto:o.chaumont@ideotechnologies.com">Olivier Chaumont</a>
 */
public abstract class AbstractMergeMojo extends AbstractMojo {

    protected MavenHelper mavenHelper = MavenHelper.getInstance();

    /**
     * The Maven Project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     * @since 1.0-alpha-1
     */
    protected MavenProject mavenProject;

    /**
     * @component
     * @since 1.0-alpha-1
     */
    protected org.apache.maven.artifact.factory.ArtifactFactory artifactFactory;

    /**
     * @component allo
     * @since 1.0-alpha-1
     */
    protected MavenProjectBuilder projectBuilder;

    /**
     * @parameter expression="${project.remoteArtifactRepositories}"
     * @readonly
     * @since 1.0-alpha-3
     */
    protected List remoteArtifactRepositories;

    /**
     * @parameter expression="${project.pluginArtifactRepositories}"
     * @readonly
     * @since 1.0-alpha-3
     */
    protected List remotePluginRepositories;

    /**
     * @parameter expression="${localRepository}"
     * @readonly
     * @since 1.0-alpha-1
     */
    protected ArtifactRepository localArtifactRepository;

    /**
     * @component
     * @since 1.0-alpha-1
     */
    protected org.apache.maven.artifact.resolver.ArtifactResolver resolver;

    /**
     * To analyse version compatibility between dependencies or not. Accepted values are : on,off.
     * 
     * @since 1.1
     * @parameter default-value="on"
     * @required
     */
    protected String analyse;

    /**
     * The project output directory.
     * 
     * @parameter default-value="${project.build.directory}/merge"
     * @required
     */
    protected File outputDir;

    /**
     * The project output directory.
     * 
     * @parameter default-value="${project.build.directory}"
     * @required
     */
    protected File buildDir;

    /**
     * To keep or not the work directory.
     * 
     * @since 1.1
     * @parameter default-value="false"
     * @required
     */
    protected boolean keepWorkDir;

    /** @component */
    protected ArtifactDeployer deployer;

    /** @component */
    protected ArtifactInstaller installer;

    /** @parameter default-value="${project.distributionManagementArtifactRepository}" */
    protected ArtifactRepository deploymentRepository;

    protected Configuration configuration;

    protected File finalPomFile;

    protected File finalJarFile;

    protected File finalSrcFile;

    protected Artifact artifact_POM;

    protected Artifact artifact_JAR;

    protected Artifact artifact_SRC;

    protected void init() {
        getLog().info("maven-merge-plugin: AbstractlMergeMojo");
        MavenHelper mavenHelper = MavenHelper.getInstance();
        mavenHelper.setArtifactFactory(artifactFactory);
        mavenHelper.setProjectBuilder(projectBuilder);
        mavenHelper.setLocalArtifactRepository(localArtifactRepository);
        mavenHelper.setRemoteArtifactRepositories(remoteArtifactRepositories);
        mavenHelper.setRemotePluginRepositories(remotePluginRepositories);
        mavenHelper.setResolver(resolver);
        mavenHelper.setDeployer(deployer);
        mavenHelper.setInstaller(installer);
        mavenHelper.setDeploymentRepository(deploymentRepository);
        configuration = new Configuration();
        configuration.setOutputDir(outputDir);
        configuration.setBuildDir(buildDir);
        configuration.setAnalyse(analyse);
    }

    protected void prepareArtifacts(String groupId, String artifactId) throws ArtifactResolutionException, ArtifactNotFoundException {
        finalPomFile = new File(configuration.getOutputDir(), getPomArtifactFileName(artifactId, mavenProject.getVersion()));
        finalJarFile = new File(configuration.getOutputDir(), getJarArtifactFileName(artifactId, mavenProject.getVersion()));
        finalSrcFile = new File(configuration.getOutputDir(), getSrcArtifactFileName(artifactId, mavenProject.getVersion()));
        artifact_POM = mavenHelper.createArtifact(groupId, artifactId, mavenProject.getVersion(), "pom");
        artifact_JAR = mavenHelper.createArtifact(groupId, artifactId, mavenProject.getVersion(), "jar");
        artifact_SRC = mavenHelper.createArtifactWithClassifier(groupId, artifactId, mavenProject.getVersion(), "jar", "sources");
    }

    protected String getJarArtifactFileName(String artifactId, String version) {
        return artifactId + "-" + version + ".jar";
    }

    protected String getPomArtifactFileName(String artifactId, String version) {
        return artifactId + "-" + version + ".pom";
    }

    protected String getSrcArtifactFileName(String artifactId, String version) {
        return artifactId + "-" + version + "-sources.jar";
    }

    protected File getArtifactRepoMavenPath(File repoMaven, String groupId, String artifactId, String version) {
        String path = repoMaven.getAbsolutePath() + "/" + groupId.replaceAll("[.]", "/") + "/" + artifactId + "/" + version;
        return new File(path);
    }
}
