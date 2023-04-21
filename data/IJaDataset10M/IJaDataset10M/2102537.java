package at.umweltbundesamt.deployer.jobs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import at.umweltbundesamt.deployer.config.Configuration;
import at.umweltbundesamt.deployer.logic.ArtifactLogic;

/**
 * @author baldauf
 * 
 */
public class DeleteOldWarsJob extends BackgroundJob {

    private static Log log = LogFactory.getLog(DeleteOldWarsJob.class);

    @Override
    public void executeJob() {
        deleteOldWars(Configuration.getMavenRepository().getLocalRepository());
        deleteOldWars(Configuration.getMavenRepository().getFinalRepository());
    }

    private void deleteOldWars(String repositoryPath) {
        log.info("Deleting old WARs (keep a maximum of " + Configuration.getDeleteOldWarFiles().getMaxWarsPerArtifact() + " per artifact) in repository: " + repositoryPath);
        ArtifactLogic.deleteOldWars(repositoryPath);
    }
}
