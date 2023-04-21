package edu.ucdavis.genomics.metabolomics.binbase;

import org.apache.log4j.Logger;
import edu.ucdavis.genomics.metabolomics.binbase.bci.Configurator;
import edu.ucdavis.genomics.metabolomics.binbase.bci.server.jmx.DatabaseJMXFacade;

/**
 * configures the dabase jmx
 * 
 * @author nase
 */
public class ConfigureDatabaseTask extends AbstractDatabaseTask {

    private Logger logger = Logger.getLogger(getClass());

    private boolean resetDatabases = true;

    @Override
    protected void executeDatabaseTask() throws Exception {
        DatabaseJMXFacade facade = Configurator.getDatabaseService();
        facade.setDatabase(getDatabase());
        facade.setDatabaseServer(getServer());
        facade.setDatabaseServerPassword(getPassword());
        facade.setDatabaseServerType(getType());
        if (isResetDatabases()) {
            logger.info("reset all registered databases");
            Configurator.getImportService().resetDatabases();
        } else {
            logger.info("adding database to list");
        }
        Configurator.getImportService().addDatabase(this.getUsername());
    }

    public boolean isResetDatabases() {
        return resetDatabases;
    }

    public void setResetDatabases(boolean resetDatabases) {
        this.resetDatabases = resetDatabases;
    }
}
