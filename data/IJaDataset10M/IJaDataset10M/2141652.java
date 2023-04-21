package org.apache.felix.obrplugin;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

/**
 * Installs bundle details in the local OBR repository (command-line goal)
 * 
 * @requiresProject false
 * @goal install-file
 * @phase install
 * 
 * @author <a href="mailto:dev@felix.apache.org">Felix Project Team</a>
 */
public final class ObrInstallFile extends AbstractFileMojo {

    /**
     * OBR Repository.
     * 
     * @parameter expression="${obrRepository}"
     */
    private String obrRepository;

    /**
     * Project types which this plugin supports.
     *
     * @parameter
     */
    private List supportedProjectTypes = Arrays.asList(new String[] { "jar", "bundle" });

    /**
     * Local Repository.
     * 
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     */
    private ArtifactRepository localRepository;

    public void execute() throws MojoExecutionException {
        MavenProject project = getProject();
        String projectType = project.getPackaging();
        if (!supportedProjectTypes.contains(projectType)) {
            getLog().warn("Ignoring project type " + projectType + " - supportedProjectTypes = " + supportedProjectTypes);
            return;
        } else if ("NONE".equalsIgnoreCase(obrRepository) || "false".equalsIgnoreCase(obrRepository)) {
            getLog().info("Local OBR update disabled (enable with -DobrRepository)");
            return;
        }
        Log log = getLog();
        ObrUpdate update;
        String mavenRepository = localRepository.getBasedir();
        URI repositoryXml = ObrUtils.findRepositoryXml(mavenRepository, obrRepository);
        URI obrXmlFile = ObrUtils.toFileURI(obrXml);
        URI bundleJar;
        if (null == file) {
            bundleJar = ObrUtils.getArtifactURI(localRepository, project.getArtifact());
        } else {
            bundleJar = file.toURI();
        }
        Config userConfig = new Config();
        update = new ObrUpdate(repositoryXml, obrXmlFile, project, mavenRepository, userConfig, log);
        update.parseRepositoryXml();
        update.updateRepository(bundleJar, null, null);
        update.writeRepositoryXml();
    }
}
