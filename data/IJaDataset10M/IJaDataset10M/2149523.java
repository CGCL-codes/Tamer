package de.uni_leipzig.lots.server.services;

import org.springframework.test.AbstractTransactionalSpringContextTests;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alexander Kiel
 * @version $Id: BackupServiceTest.java,v 1.3 2007/07/14 20:08:34 mai99bxd Exp $
 */
public class BackupServiceTest extends AbstractTransactionalSpringContextTests {

    static {
        Logger.getLogger("").setLevel(Level.WARNING);
        Logger.getLogger("de.uni_leipzig.lots").setLevel(Level.SEVERE);
        Logger.getLogger("org.hibernate.SQL").setLevel(Level.WARNING);
        Logger.getLogger("org.hibernate").setLevel(Level.WARNING);
        Logger.getLogger("org.springframework").setLevel(Level.WARNING);
        Logger.getLogger("de.uni_leipzig.lots.server.services.impl.BackupServiceImpl").setLevel(Level.FINE);
    }

    private BackupService backupService;

    public void setBackupService(BackupService backupService) {
        this.backupService = backupService;
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[] { "testContext.xml", "de/uni_leipzig/lots/server/services/backupServiceTestContext.xml", "de/uni_leipzig/lots/server/persist/repositoryTestData.xml" };
    }

    public void testBackupToFile() throws BackupException, IOException {
        backupService.backupToFile(new File("backup.xml"));
    }
}
