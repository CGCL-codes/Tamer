package de.banh.bibo.model.provider.postgresql;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import de.banh.bibo.model.Manager;
import de.banh.bibo.model.ManagerFactory;
import de.banh.bibo.model.VerlagManager;
import de.banh.bibo.model.Verlag;
import de.banh.bibo.model.provider.db.ConnectionPool;

public class PgVerlagManagerTest {

    public static final String PROPERTIES_FILE = "build.properties";

    public static Manager manager;

    public static VerlagManager verlagmanager;

    public static Verlag verlag;

    private static Properties getProperties() throws Exception {
        File propFile = new File(PROPERTIES_FILE);
        Properties properties = new Properties();
        if (propFile.exists()) {
            InputStream datei_stream = new FileInputStream(PROPERTIES_FILE);
            try {
                properties.load(datei_stream);
            } finally {
                datei_stream.close();
            }
        }
        return properties;
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Properties properties = getProperties();
        System.setProperty(ManagerFactory.PROVIDER_CLASS, "de.banh.bibo.model.provider.postgresql.PgManager");
        System.setProperty(ConnectionPool.PROPERTIES_PATH + ".db.driver", properties.getProperty("db.driver"));
        System.setProperty(ConnectionPool.PROPERTIES_PATH + ".url", properties.getProperty("db.url"));
        System.setProperty(ConnectionPool.PROPERTIES_PATH + ".user", properties.getProperty("db.user"));
        System.setProperty(ConnectionPool.PROPERTIES_PATH + ".password", properties.getProperty("db.password"));
        manager = ManagerFactory.createManager();
        verlagmanager = manager.getVerlagManager();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void anlegen_aendern_loeschen() throws Exception {
        Date datum = new Date();
        verlag = verlagmanager.createVerlag("Test-Verlag " + datum.getTime());
        verlag.setHomepage("http://bibo.sourceforge.net");
        assertTrue("Verlag-ID muss nach einem createVerlag -1 statt " + verlag.getId() + " sein!", verlag.getId() == -1);
        verlagmanager.saveVerlag(verlag);
        assertTrue("Nach Einfügen wurde dem Verlag keine ID übergeben!", verlag.getId() != -1);
        System.out.println("verlag.getId() = " + verlag.getId());
        String s = verlag.getBezeichnung() + " (update)";
        verlag.setBezeichnung(s);
        verlagmanager.saveVerlag(verlag);
        Verlag verlag_check = verlagmanager.getVerlagById(verlag.getId());
        assertTrue("getVerlagById liefert nicht den richtigen Datensatz zurück!", verlag_check.getId() == verlag.getId());
        assertTrue("Bezeichnung wurde nicht geupdatet!", verlag_check.getBezeichnung().equals(verlag.getBezeichnung()));
        verlagmanager.deleteVerlag(verlag);
    }

    @Ignore
    @Test
    public void suchen() throws Exception {
    }
}
