package pl.rzarajczyk.utils.application;

import java.io.IOException;
import javax.swing.UIManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.rzarajczyk.utils.system.os.OperatingSystem;

/**
 *
 * @author rafalz
 */
public class ConfigurationTest {

    private Configuration configuration;

    @Before
    public void setUp() throws IOException {
        configuration = new Configuration();
    }

    @Test
    public void getTempDirShouldDetectCorrectDirForCurrentOs() throws IOException {
    }

    @Test
    public void getApplicationIdShouldExtractNameFromPomXml() {
        Assert.assertEquals("break-time", configuration.getApplicationId());
    }

    @Test
    public void getApplicationNameShouldExtractNameFromPomXml() {
        Assert.assertEquals("Break Time!", configuration.getApplicationName());
    }

    @Test
    public void getStorageDirShouldDetectCorrectDirForCurrentOs() throws IOException {
        String path = configuration.getStorageDir().getAbsolutePath();
        if (OperatingSystem.isUnix()) Assert.assertTrue("Path: [" + path + "]", path.matches("/home/[a-zA-Z0-9_-]+/\\.[a-zA-Z0-9_-]+")); else {
            System.out.println("Test for this OS is not supported");
        }
    }

    @Test
    public void getLookAndFeelNameShouldPointToSystemLookAndFeel() {
        Assert.assertEquals(UIManager.getSystemLookAndFeelClassName(), configuration.getLookAndFeelName());
    }

    @Test
    public void getBeanConfigurationPathsShouldReturnCurrentJarPaths() throws IOException {
        Assert.assertArrayEquals(new String[] { "spring/context.xml" }, configuration.getBeanConfigurations());
    }
}
