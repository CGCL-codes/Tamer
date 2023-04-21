package sample.configure;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * Goal which cretae translator files if needed
 * 
 * @goal configure
 * 
 * @phase process-sources
 */
public class ConfigureEclipsePlugin extends AbstractMojo {

    /**
	 * A boolean to allow or not debug
	 * 
	 * @parameter expression="${translator.debug}" default-value="false"
	 * @required
	 */
    private boolean debug = false;

    /**
	 * Artifact resolver, needed to download source jars for inclusion in classpath.
	 * 
	 * @component role="org.apache.maven.artifact.resolver.ArtifactResolver"
	 * @required
	 * @readonly
	 */
    protected ArtifactResolver artifactResolver;

    /**
	 * The Maven Project.
	 *
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
    protected MavenProject project;

    /**
	 * Location of the local repository.
	 * 
	 * @parameter expression="${localRepository}"
	 * @readonly
	 * @required
	 */
    protected ArtifactRepository localRepo;

    /**
	 * List of Remote Repositories used by the resolver
	 * 
	 * @parameter expression="${project.remoteArtifactRepositories}"
	 * @readonly
	 * @required
	 */
    protected java.util.List remoteRepos;

    /**
	 * @component
	 */
    protected ArtifactMetadataSource artifactMetadataSource;

    /**
	 * 
	 */
    public void execute() throws MojoExecutionException {
        if (debug) getLog().info("Project is " + project.getName());
        String baseDir = project.getBasedir().getAbsolutePath() + File.separator;
        if (debug) getLog().info("Project dir " + baseDir.toString());
        String translatorDir = "src" + File.separator + "main" + File.separator + "translator";
        String configurationDir = translatorDir + File.separator + "configuration" + File.separator;
        File translatorDirFile = new File(baseDir + translatorDir);
        if (debug) getLog().info("Translator dir " + translatorDir);
        if (!translatorDirFile.exists()) {
            if (!translatorDirFile.mkdirs()) {
                getLog().error("Unable to create directory " + translatorDir);
                return;
            }
        }
        File configurationDirFile = new File(baseDir + configurationDir);
        if (debug) getLog().info("Configuration dir " + configurationDir);
        if (!configurationDirFile.exists()) {
            if (!configurationDirFile.mkdirs()) {
                getLog().error("Unable to create directory " + configurationDir);
                return;
            }
        }
        String projectConfiguration = normalize(project.getName()) + ".conf";
        File projectConfigurationFile = new File(baseDir + configurationDir + projectConfiguration);
        if (debug) getLog().info("Configuration file " + projectConfigurationFile.getAbsolutePath());
        if (!projectConfigurationFile.exists()) {
            try {
                projectConfigurationFile.createNewFile();
                if (projectConfigurationFile.canWrite()) {
                    FileWriter writer = new FileWriter(projectConfigurationFile);
                    writer.append("//\n");
                    writer.append("// ILOG Java 2 CSharp translator configuration file for projet " + normalize(project.getName()) + "\n");
                    writer.append("//\n");
                    writer.flush();
                    writer.close();
                } else {
                    if (debug) getLog().info("Can't write configuration file " + projectConfigurationFile.getAbsolutePath() + ".");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (debug) getLog().info("Configuration file " + projectConfigurationFile.getAbsolutePath() + " already exists.");
        }
    }

    private String normalize(String name) {
        return name.replace("/", "_").replace("\\", "_");
    }
}
