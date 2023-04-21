package uk.ac.ebi.intact.plugins.dbupdate;

import org.apache.log4j.Priority;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import uk.ac.ebi.intact.business.IntactException;
import uk.ac.ebi.intact.business.IntactTransactionException;
import uk.ac.ebi.intact.context.DataContext;
import uk.ac.ebi.intact.context.IntactContext;
import uk.ac.ebi.intact.plugins.dbupdate.targetspecies.UpdateTargetSpecies;
import java.io.IOException;

/**
 * Example mojo. This mojo is executed when the goal "target-species" is called.
 *
 * @goal target-species
 * @phase process-resources
 */
public class UpdateTargetSpeciesMojo extends UpdateAbstractMojo {

    /**
     * Main execution method, which is called after hibernate has been initialized
     */
    public void executeIntactMojo() throws MojoExecutionException, MojoFailureException, IOException {
        if (IntactContext.getCurrentInstance().getDataContext().isTransactionActive()) {
            commitTransaction();
        }
        UpdateTargetSpecies updateTargetSpecies = new UpdateTargetSpecies();
        updateTargetSpecies.updateAllExperiments();
    }

    protected DataContext getDataContext() {
        return IntactContext.getCurrentInstance().getDataContext();
    }

    protected void beginTransaction() {
        getDataContext().beginTransaction();
    }

    protected void commitTransaction() {
        try {
            getDataContext().commitTransaction();
        } catch (IntactTransactionException e) {
            throw new IntactException(e);
        }
    }

    protected Priority getLogPriority() {
        return Priority.INFO;
    }
}
