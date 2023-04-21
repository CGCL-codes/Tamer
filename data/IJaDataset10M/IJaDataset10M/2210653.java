package uk.ac.ebi.intact.plugins.dbupdate;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import uk.ac.ebi.intact.plugin.MojoUtils;
import uk.ac.ebi.intact.plugins.dbupdate.mine.MineDatabaseFill;
import uk.ac.ebi.intact.plugins.dbupdate.mine.MineDatabaseFillReport;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Example mojo. This mojo is executed when the goal "mine" is called.
 *
 * @goal mine
 * @phase process-resources
 */
public class UpdateMiNeTablesMojo extends UpdateAbstractMojo {

    /**
     * @parameter default-value="${project.build.directory}/MiNe-excluded-interactors.txt"
     * @required
     */
    private File nullTaxIdFile;

    /**
     * Main execution method, which is called after hibernate has been initialized
     */
    public void executeIntactMojo() throws MojoExecutionException, MojoFailureException, IOException {
        PrintStream ps = new PrintStream(getOutputFile());
        MineDatabaseFillReport report = null;
        try {
            report = MineDatabaseFill.buildDatabase(ps);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MojoExecutionException("SQL error while building the MiNe table. cf. nested Exception !", e);
        }
        MojoUtils.prepareFile(nullTaxIdFile, true);
        MojoUtils.writeStandardHeaderToFile("Null taxID interactors", "Interactors with null taxID", getProject(), nullTaxIdFile);
        FileWriter writer = new FileWriter(nullTaxIdFile, true);
        for (String interactorAc : report.getNullTaxidInteractors()) {
            writer.write(interactorAc + NEW_LINE);
        }
        writer.close();
    }

    public MavenProject getProject() {
        return project;
    }
}
