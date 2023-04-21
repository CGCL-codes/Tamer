package test.net.sf.karatasi;

import java.io.File;
import net.sf.karatasi.Database;
import net.sf.karatasi.DeviceSyncData;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/** Test for {@link DeviceSyncData}
 * @author Mathias Kussinger
 */
public class DeviceSyncDataTest {

    /** Load database driver.
     * @throws ClassNotFoundException if the JDBC driver could not be loaded.
     */
    @BeforeClass
    public static void loadJDBCDriver() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
    }

    /** Tests creation and filling from test database test1.
     * @throws Exception if file doesn't exist or copy fails.
     */
    @Test
    public void testResultsForTest1Db() throws Exception {
        final File testFile = TestHelpers.prepareDataDirectoryAndCopyDatabase("l_test1.db");
        final Database testDatabase = new Database(testFile);
        final DeviceSyncData testling = new DeviceSyncData(testDatabase);
        Assert.assertNotNull("DeviceSyncData object has to exist.", testling);
        Assert.assertEquals("Full name of l_test1.db has to be ok.", "test1", testling.getFullName());
        Assert.assertEquals("Time stamp of l_test1.db has to be ok.", 183296, testling.getFileSize());
        Assert.assertEquals("Size of l_test1.db has to be ok.", 1231605037, (testling.getTimeStamp()).getTime() / 1000);
        TestHelpers.removeDataDirectory();
    }
}
