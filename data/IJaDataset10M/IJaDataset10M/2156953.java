package ca.uhn.hl7v2.mvnplugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.database.NormativeDatabase;
import ca.uhn.hl7v2.sourcegen.EventMapGenerator;
import ca.uhn.hl7v2.sourcegen.SourceGenerator;

/**
 * Maven Plugin Mojo for generating HAPI HL7 message/segment/etc source files
 * 
 * @author <a href="mailto:jamesagnew@sourceforge.net">James Agnew</a>
 * @goal sourcegen
 * @phase generate-sources
 * @requiresDependencyResolution runtime
 * @requiresProject
 * @inheritedByDefault false
 */
public class SourceGenMojo extends AbstractMojo {

    private static final Set<String> alreadyMade = new HashSet<String>();

    /**
     * The maven project.
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * The target directory for the generated source
     * 
     * @parameter
     * @required
     */
    private String targetDirectory;

    /**
     * The target directory for the generated resources
     * 
     * @parameter
     * @required
     */
    private String targetResourceDirectory;

    /**
     * The version for the generated source
     * 
     * @parameter
     */
    private String version;

    /**
     * The JDBC URL for the HL7 database
     * 
     * @parameter
     */
    private String jdbcUrl;

    /**
     * The JDBC User for the HL7 database
     * 
     * @parameter
     */
    private String jdbcUser;

    /**
     * The JDBC Password for the HL7 database
     * 
     * @parameter
     */
    private String jdbcPassword;

    /**
     * Should build be skipped
     *
     * @parameter
     */
    private boolean skip;

    /**
     * The package from which to load the templates
     * 
     * @parameter default="ca.uhn.hl7v2.sourcegen.templates"
     */
    private String templatePackage = "ca.uhn.hl7v2.sourcegen.templates";

    /**
     * Should structures be treated as resources
     *
     * @parameter default="false"
     */
    private boolean structuresAsResources;

    /**
     * {@inheritDoc}
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().warn("Configured to skip");
        }
        if (new File(targetDirectory).exists()) {
            getLog().warn("Already exists version " + version + ", skipping!");
        } else if (!alreadyMade.contains(version)) {
            alreadyMade.add(version);
            if (jdbcUser == null) {
                jdbcUser = "";
            }
            if (jdbcPassword == null) {
                jdbcPassword = "";
            }
            System.setProperty(NormativeDatabase.PROP_DATABASE_USER, jdbcUser);
            System.setProperty(NormativeDatabase.PROP_DATABASE_PASSWORD, jdbcPassword);
            System.setProperty(NormativeDatabase.PROP_DATABASE_URL, jdbcUrl);
            try {
                EventMapGenerator.generateEventMap(targetResourceDirectory, version);
                String targetDir = structuresAsResources ? targetResourceDirectory : targetDirectory;
                SourceGenerator.makeAll(targetDir, version, false, templatePackage, "java");
            } catch (HL7Exception e) {
                throw new MojoExecutionException("Failed to build source ", e);
            } catch (SQLException e) {
                throw new MojoExecutionException("Failed to build source ", e);
            } catch (IOException e) {
                throw new MojoExecutionException("Failed to build source ", e);
            }
        } else {
            getLog().warn("Already made version " + version + ", skipping!");
        }
        if (!structuresAsResources) {
            getLog().info("Adding " + targetDirectory + " to compile source root");
            project.addCompileSourceRoot(targetDirectory);
        }
    }
}
