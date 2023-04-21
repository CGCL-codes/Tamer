package org.ops4j.pax.construct.project;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.ops4j.pax.construct.archetype.AbstractPaxArchetypeMojo;
import org.ops4j.pax.construct.util.PomUtils;
import org.ops4j.pax.construct.util.PomUtils.Pom;

/**
 * Updates a Pax-Construct project or (when run in the scripts directory) the installed scripts to the latest version
 * 
 * <code><pre>
 *   mvn pax:update [-Dversion=...]
 * </pre></code>
 * 
 * @goal update
 * @aggregator true
 * 
 * @requiresProject false
 */
public class UpdateMojo extends AbstractMojo {

    /**
     * Component factory for Maven artifacts
     * 
     * @component
     */
    private ArtifactFactory m_factory;

    /**
     * Component for resolving Maven artifacts
     * 
     * @component
     */
    private ArtifactResolver m_resolver;

    /**
     * Component for resolving Maven metadata
     * 
     * @component
     */
    private ArtifactMetadataSource m_source;

    /**
     * List of remote Maven repositories for the containing project.
     * 
     * @parameter expression="${project.remoteArtifactRepositories}"
     * @required
     * @readonly
     */
    private List m_remoteRepos;

    /**
     * The local Maven repository for the containing project.
     * 
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     */
    private ArtifactRepository m_localRepo;

    /**
     * The directory containing the POM to be updated.
     * 
     * @parameter expression="${targetDirectory}" default-value="${project.basedir}"
     */
    private File targetDirectory;

    /**
     * The version of Pax-Construct to update to.
     * 
     * @parameter expression="${version}"
     */
    private String version;

    /**
     * {@inheritDoc}
     */
    public void execute() throws MojoExecutionException {
        String groupId = AbstractPaxArchetypeMojo.PAX_CONSTRUCT_GROUP_ID;
        Artifact scripts = m_factory.createBuildArtifact(groupId, "scripts", version, "zip");
        if (PomUtils.needReleaseVersion(version)) {
            version = PomUtils.getReleaseVersion(scripts, m_source, m_remoteRepos, m_localRepo, null);
            scripts.selectVersion(version);
        }
        if (new File(targetDirectory, "pax-bootstrap-pom.xml").exists()) {
            updatePaxConstructScripts(scripts);
        } else if (new File(targetDirectory, "pom.xml").exists()) {
            updatePaxConstructProject();
        } else {
            getLog().warn("pax-update should be run from the scripts directory, or from a Pax-Construct project");
        }
    }

    /**
     * Update each script in turn from the zipfile stored in the repository
     * 
     * @param scripts maven artifact pointing to the scripts
     * @throws MojoExecutionException
     */
    private void updatePaxConstructScripts(Artifact scripts) throws MojoExecutionException {
        if (!PomUtils.downloadFile(scripts, m_resolver, m_remoteRepos, m_localRepo)) {
            throw new MojoExecutionException("Unable to download scripts " + scripts);
        }
        getLog().info("Updating scripts to version " + version);
        try {
            ZipFile zip = new ZipFile(scripts.getFile());
            for (Enumeration i = zip.entries(); i.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) i.nextElement();
                String name = new File(entry.getName()).getName();
                String path = new File(targetDirectory, name).getPath();
                if (!entry.isDirectory()) {
                    InputStream in = zip.getInputStream(entry);
                    String data = IOUtil.toString(in);
                    FileUtils.fileWrite(path, data);
                    getLog().info(" => " + path);
                }
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Problem updating local scripts", e);
        }
    }

    /**
     * Update existing Pax-Construct project to use the latest release of the plugin
     * 
     * @throws MojoExecutionException
     */
    private void updatePaxConstructProject() throws MojoExecutionException {
        try {
            Pom pom = PomUtils.readPom(targetDirectory);
            boolean foundPlugin = pom.updatePluginVersion("org.ops4j", "maven-pax-plugin", version);
            if (foundPlugin) {
                getLog().info("Updating Pax-Construct project to version " + version);
                pom.write();
                getLog().info(" => " + pom.getFile());
            } else {
                getLog().warn("Unable to find reference to org.ops4j:maven-pax-plugin");
                getLog().warn("you may need to convert this project using 'pax-clone'");
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Problem reading Maven POM: " + targetDirectory);
        }
    }
}
