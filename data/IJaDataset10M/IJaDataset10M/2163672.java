package openfield.persistence.business;

import java.io.File;
import java.util.List;
import openfield.persistence.database.DBLookup;
import openfield.persistence.dbmgr.DBManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author shader
 */
public class UtilesTest {

    private Utiles ut;

    public UtilesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        if (!DBManager.getDBManagerInstance().exists("testdb")) {
            DBManager.getDBManagerInstance().createDB("testdb");
        }
        DBManager.getDBManagerInstance().openDBLookup("testdb");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        DBLookup.clearOpenfieldDB();
    }

    @Before
    public void setUp() {
        BusinessFactory bf = new BusinessFactory();
        ut = bf.getUtiles();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getProvincias method, of class Utiles.
     */
    @Test
    public void testGetProvincias() {
        System.out.println("getProvincias");
        List result = ut.getProvincias();
        assertTrue("Error al recuperar provincias, lista nula", result != null);
    }

    /**
     * Test of getTiposMagnitud method, of class Utiles.
     */
    @Test
    public void testGetTiposMagnitud() {
        System.out.println("getTiposMagnitud");
        List result = ut.getTiposMagnitud();
        assertTrue("Error al recuperar magnitudes, lista nula", result != null);
    }
}
