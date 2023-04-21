package jriaffe.core;

import jriaffe.core.ApplicationInstaller;
import junit.framework.*;
import java.io.File;

/**
 *
 * @author preisler
 */
public class ApplicationInstallerTest extends TestCase {

    public ApplicationInstallerTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of install method, of class jriaffe.core.ApplicationInstaller.
     */
    public void testInstall() throws Exception {
        System.out.println("install");
        ApplicationInstaller instance = ApplicationInstaller.init("http://localhost/~preisler/applications/appWithJarExample/");
        doDownload(instance);
        instance = ApplicationInstaller.init("http://localhost/~preisler/applications/appWithJarExample");
        doDownload(instance);
    }

    private void doDownload(ApplicationInstaller instance) throws Exception {
        instance.install();
        String usersHome = System.getProperty("user.home") + System.getProperty("file.separator");
        String localAppDir = usersHome + "jriaffe/applications/localhost_~preisler_applications_appWithJarExample";
        File file = new File(localAppDir);
        assertTrue(file.exists());
        file = new File(localAppDir + System.getProperty("file.separator") + "demo.jar");
        assertTrue(file.exists());
        file = new File(localAppDir + System.getProperty("file.separator") + "icon.png");
        assertTrue(file.exists());
        file = new File(localAppDir + System.getProperty("file.separator") + "splash.jpg");
        assertTrue(file.exists());
    }

    public void testInstallNapFile() throws Exception {
        System.out.println("install nap file");
        ApplicationInstaller instance = ApplicationInstaller.init("http://localhost/~preisler/applications/appWithNapExample/");
        instance.install();
        String usersHome = System.getProperty("user.home") + System.getProperty("file.separator");
        String localAppDir = usersHome + "jriaffe/applications/localhost_~preisler_applications_appWithNapExample";
        File file = new File(localAppDir);
        assertTrue(file.exists());
        file = new File(localAppDir + System.getProperty("file.separator") + "icon.png");
        assertTrue(file.exists());
        file = new File(localAppDir + System.getProperty("file.separator") + "splash.jpg");
        assertTrue(file.exists());
        file = new File(localAppDir + System.getProperty("file.separator") + "demo.jar");
        assertTrue(file.exists());
        file = new File(localAppDir + System.getProperty("file.separator") + "xalan.jar");
        assertTrue(file.exists());
        file = new File(localAppDir + System.getProperty("file.separator") + "xercesImpl.jar");
        assertTrue(file.exists());
    }
}
