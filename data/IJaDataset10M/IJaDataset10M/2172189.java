package cc.w3d.jawos2.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import org.junit.Test;
import cc.w3d.jawos2.jinn2.tools.configurationtools.ConfigurationManager;
import cc.w3d.jawos2.jinn2.tools.configurationtools.PropertiesFinder;
import dummyRootPackage.DummyClassRootPackageLocator;
import static org.junit.Assert.*;

public class PropertiesFinderTest {

    @Test
    public void testDummyClassRootPackageLocator() {
        new DummyClassRootPackageLocator();
        new ConfigurationManager();
    }

    @Test
    public void testPropertiesFilesFindingWithoutFolderSpecification() throws FileNotFoundException {
        FoldersCreated folderStates = prepareFolders();
        for (int d = 0; d < 2; d++) for (int s = 0; s < 2; s++) for (int u = 0; u < 2; u++) for (int a = 0; a < 2; a++) {
            TestData testData = new TestData();
            if (d == 1) testData.prepareDefaults();
            if (s == 1) testData.prepareSystem();
            if (u == 1) testData.prepareUser();
            if (a == 1) testData.prepareApp();
            Properties[] properties = d == 1 ? PropertiesFinder.find(testData.propertiesName, testData.defaultValues) : PropertiesFinder.find(testData.propertiesName);
            assertTrue("The Length is '" + properties.length + "' and should be '" + (d + s + u + a) + "'", properties.length == (d + s + u + a));
            int i = 0;
            if (d == 1) testDefaults(properties[i++]);
            if (s == 1) testSystem(properties[i++]);
            if (u == 1) testUser(properties[i++]);
            if (a == 1) testApp(properties[i++]);
            testData.clean();
        }
        restoreFolders(folderStates);
    }

    @Test
    public void testPropertiesFilesFindingWithFolderSpecification() throws FileNotFoundException {
        final String folderName = "sampleEtc";
        FoldersCreated folderStates = prepareFolders(folderName);
        for (int dd = 0; dd < 3; dd++) for (int s = 0; s < 2; s++) for (int u = 0; u < 2; u++) for (int a = 0; a < 2; a++) {
            int d = dd < 2 ? 0 : 1;
            TestData testData = new TestData(folderName);
            if (d == 1) testData.prepareDefaults();
            if (s == 1) testData.prepareSystem();
            if (u == 1) testData.prepareUser();
            if (a == 1) testData.prepareApp();
            Properties[] properties = dd == 2 ? PropertiesFinder.find(folderName + "/" + testData.propertiesName, testData.defaultValues) : dd == 1 ? PropertiesFinder.find(folderName + "/" + testData.propertiesName, "") : PropertiesFinder.find(folderName + "/" + testData.propertiesName);
            assertTrue("The Length is '" + properties.length + "' and should be '" + (d + s + u + a) + "'", properties.length == (d + s + u + a));
            int i = 0;
            if (d == 1) testDefaults(properties[i++]);
            if (s == 1) testSystem(properties[i++]);
            if (u == 1) testUser(properties[i++]);
            if (a == 1) testApp(properties[i++]);
            testData.clean();
        }
        restoreFolders(folderName, folderStates);
    }

    private static void testDefaults(Properties properties) {
        assertTrue(properties.get("file").equals("defaults"));
        assertTrue(!properties.get("file").equals("system"));
        assertTrue(!properties.get("file").equals("user"));
        assertTrue(!properties.get("file").equals("app"));
        assertTrue(properties.get("G").equals("H"));
        assertTrue(!properties.containsKey("A"));
        assertTrue(!properties.containsKey("C"));
        assertTrue(!properties.containsKey("E"));
    }

    private static void testSystem(Properties properties) {
        assertTrue(!properties.get("file").equals("defaults"));
        assertTrue(properties.get("file").equals("system"));
        assertTrue(!properties.get("file").equals("user"));
        assertTrue(!properties.get("file").equals("app"));
        assertTrue(!properties.containsKey("G"));
        assertTrue(properties.get("A").equals("B"));
        assertTrue(!properties.containsKey("C"));
        assertTrue(!properties.containsKey("E"));
    }

    private static void testUser(Properties properties) {
        assertTrue(!properties.get("file").equals("defaults"));
        assertTrue(!properties.get("file").equals("system"));
        assertTrue(properties.get("file").equals("user"));
        assertTrue(!properties.get("file").equals("app"));
        assertTrue(!properties.containsKey("G"));
        assertTrue(!properties.containsKey("A"));
        assertTrue(properties.get("C").equals("D"));
        assertTrue(!properties.containsKey("E"));
    }

    private static void testApp(Properties properties) {
        assertTrue(!properties.get("file").equals("defaults"));
        assertTrue(!properties.get("file").equals("system"));
        assertTrue(!properties.get("file").equals("user"));
        assertTrue(properties.get("file").equals("app"));
        assertTrue(!properties.containsKey("G"));
        assertTrue(!properties.containsKey("A"));
        assertTrue(!properties.containsKey("C"));
        assertTrue(properties.get("E").equals("F"));
    }

    private static class TestData {

        private String folderName = "etc";

        private File defaultsPropertiesFile = null;

        private File systemPropertiesFile = null;

        private File userPropertiesFile = null;

        private File appPropertiesFile = null;

        public final String propertiesName = getRandomPropertiesFileName();

        public TestData() {
        }

        public TestData(String folderName) {
            this.folderName = folderName;
        }

        private void prepareFile(File f, String... vars) throws FileNotFoundException {
            PrintWriter pw = new PrintWriter(f);
            pw.print(genPropertiesFileContent(vars));
            pw.flush();
            pw.close();
        }

        String defaultValues;

        void prepareDefaults() throws FileNotFoundException {
            defaultValues = genPropertiesFileContent("file", "defaults", "G", "H");
        }

        void prepareSystem() throws FileNotFoundException {
            systemPropertiesFile = new File(getSystemFile(folderName), propertiesName);
            prepareFile(systemPropertiesFile, "file", "system", "A", "B");
        }

        void prepareUser() throws FileNotFoundException {
            userPropertiesFile = new File(getUserFile(folderName), propertiesName);
            prepareFile(userPropertiesFile, "file", "user", "C", "D");
        }

        void prepareApp() throws FileNotFoundException {
            appPropertiesFile = new File(getAppFile(folderName), propertiesName);
            prepareFile(appPropertiesFile, "file", "app", "E", "F");
        }

        void clean() {
            if (defaultsPropertiesFile != null) {
                assertTrue(defaultsPropertiesFile.exists());
                assertTrue(!defaultsPropertiesFile.isDirectory());
                assertTrue(defaultsPropertiesFile.canWrite());
                assertTrue(defaultsPropertiesFile.delete());
            }
            if (systemPropertiesFile != null) {
                assertTrue(systemPropertiesFile.exists());
                assertTrue(!systemPropertiesFile.isDirectory());
                assertTrue(systemPropertiesFile.canWrite());
                assertTrue(systemPropertiesFile.delete());
            }
            if (userPropertiesFile != null) {
                assertTrue(userPropertiesFile.exists());
                assertTrue(!userPropertiesFile.isDirectory());
                assertTrue(userPropertiesFile.canWrite());
                assertTrue(userPropertiesFile.delete());
            }
            if (appPropertiesFile != null) {
                assertTrue(appPropertiesFile.exists());
                assertTrue(!appPropertiesFile.isDirectory());
                assertTrue(appPropertiesFile.canWrite());
                assertTrue(appPropertiesFile.delete());
            }
            if (defaultsPropertiesFile != null) assertTrue(!defaultsPropertiesFile.exists());
            if (systemPropertiesFile != null) assertTrue(!systemPropertiesFile.exists());
            if (userPropertiesFile != null) assertTrue(!userPropertiesFile.exists());
            if (appPropertiesFile != null) assertTrue(!appPropertiesFile.exists());
        }
    }

    private static String getRandomPropertiesFileName() {
        return new Random().nextLong() + "." + new Date().getTime() + ".properties";
    }

    private static String genPropertiesFileContent(String... vars) {
        String r = "";
        for (int i = 0; i < vars.length; i += 2) {
            r += vars[i] + " = " + vars[i + 1] + "\n";
        }
        return r;
    }

    private static class FoldersCreated {

        boolean system = false;

        boolean user = false;

        boolean app = false;
    }

    private static FoldersCreated prepareFolders() {
        return prepareFolders("etc");
    }

    private static FoldersCreated prepareFolders(String folderName) {
        FoldersCreated r = new FoldersCreated();
        File system = getSystemFile(folderName);
        File user = getUserFile(folderName);
        File app = getAppFile(folderName);
        r.system = system.exists();
        r.user = user.exists();
        r.app = app.exists();
        system.mkdir();
        user.mkdir();
        app.mkdir();
        return r;
    }

    private static void restoreFolders(FoldersCreated folderStates) {
        restoreFolders("etc", folderStates);
    }

    private static void restoreFolders(String folderName, FoldersCreated folderStates) {
        FoldersCreated r = new FoldersCreated();
        File system = getSystemFile(folderName);
        File user = getUserFile(folderName);
        File app = getAppFile(folderName);
        if (!r.system) system.delete();
        if (!r.user) user.delete();
        if (!r.app) app.delete();
    }

    private static File getSystemFile(String folderName) {
        return new File("/" + folderName);
    }

    private static File getUserFile(String folderName) {
        return new File(fixFolder(System.getProperty("user.home")) + folderName);
    }

    private static File getAppFile(String folderName) {
        return new File(fixFolder(getApplicationFolder()) + folderName);
    }

    private static String fixFolder(String inFolder) {
        String outFolder = inFolder;
        if (!outFolder.endsWith("/") && !outFolder.endsWith("\\")) outFolder += "/";
        return outFolder;
    }

    private static String getApplicationFolder() {
        try {
            return PropertiesFinderTest.class.getResource("../../../..").toURI().toString().replaceAll("/target/test-classes", "/target").replaceFirst("file:/", "").replaceAll("%20", " ");
        } catch (URISyntaxException e) {
            return "";
        }
    }
}
