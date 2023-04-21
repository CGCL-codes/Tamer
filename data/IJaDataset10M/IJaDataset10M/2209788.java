package uk.ac.lkl.migen.system.test;

import static org.junit.Assert.fail;
import java.io.File;
import org.junit.Test;
import uk.ac.lkl.common.util.config.ConfigurationException;
import uk.ac.lkl.common.util.config.MiGenConfiguration;
import uk.ac.lkl.migen.system.ExitStatus;
import uk.ac.lkl.migen.system.ServerLauncher;
import uk.ac.lkl.migen.system.util.MiGenUtilities;

public class BasicServerTest {

    @Test
    public void basicTest() throws ConfigurationException {
        pause(2);
        try {
            String testDbName = "migendb-test";
            resetDb(testDbName);
            MiGenConfiguration.getInstance().factoryReset();
            MiGenConfiguration.setShowingServerUi(false);
            MiGenConfiguration.setDatabaseName(testDbName);
            ServerLauncher launcher;
            launcher = new ServerLauncher();
            launcher.shutdown(ExitStatus.NO_ERROR);
            launcher = new ServerLauncher();
            launcher.shutdown(ExitStatus.NO_ERROR);
            MiGenConfiguration.getInstance().factoryReset();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getLocalizedMessage());
        }
    }

    private static void resetDb(String testDbName) {
        File dbDir = new File(testDbName);
        if (dbDir.exists()) {
            dbDir.renameTo(new File(dbDir + "-" + getDate()));
        }
    }

    private static String getDate() {
        return MiGenUtilities.fromTimestampToString(System.currentTimeMillis(), "yyyy-MM-dd_HH-mm-ss");
    }

    private static void pause(int max) {
        try {
            for (int i = 0; i < max; i++) {
                System.out.print("Sleeping... ");
                Thread.sleep(1000);
                System.out.println(i + " out of " + max + " seconds...");
            }
        } catch (InterruptedException e) {
            System.out.println("Ooops! Interrupted while sleeping! How rude!");
        }
        System.out.println("Oh, well... back to work.");
    }
}
