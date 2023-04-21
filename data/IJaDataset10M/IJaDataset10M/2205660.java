package clouditup.linkbdd;

import java.sql.Connection;
import java.sql.ResultSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mathieu
 */
public class DataSourceTest {

    public DataSourceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testConstructeur() {
        DataSource ds = new DataSource();
        assertNotNull(ds);
    }

    /**
     * Test of fermerConnection method, of class DataSource.
     */
    @Test
    public void testFermerConnection() {
        System.out.println("fermerConnection");
        DataSource instance = new DataSource();
        instance.fermerConnection();
    }

    /**
     * Test of getConnection method, of class DataSource.
     */
    @Test
    public void testGetConnection() {
        System.out.println("getConnection");
        DataSource instance = new DataSource();
        Connection result = instance.getConnection();
        assertNotNull(result);
    }

    /**
     * Test of setConnection method, of class DataSource.
     */
    @Test
    public void testSetConnection() {
        System.out.println("setConnection");
        Connection connection = null;
        DataSource instance = new DataSource();
        instance.setConnection(connection);
    }

    /**
     * Test of recupereVideo method, of class DataSource.
     */
    @Test
    public void testRecupereVideo() throws Exception {
        System.out.println("recupereVideo");
        String id = "3333297870832";
        DataSource instance = new DataSource();
        ResultSet result = instance.recupereVideo(id);
        assertNotNull(result);
    }

    /**
     * Test of recupereUrlQueuePartenaires method, of class DataSource.
     */
    @Test
    public void testRecuperePartenaires() throws Exception {
        System.out.println("RecuperePartenaires");
        DataSource instance = new DataSource();
        ResultSet result = instance.recuperePartenaires();
        assertNotNull(result);
    }

    /**
     * Test of envoiBD method, of class DataSource.
     */
    @Test
    public void testEnvoiBD() throws Exception {
        System.out.println("envoiBD");
        String query = "SELECT * FROM Video";
        DataSource instance = new DataSource();
        ResultSet result = instance.envoiBD(query);
        assertNotNull(result);
    }

    /**
     * Test of updateBDD method, of class DataSource.
     */
    @Test
    public void testUpdateBDD() throws Exception {
        System.out.println("updateBDD");
        String string = "INSERT INTO Video(ID,Titre,Synopsis,URLAffiche,Prix,RentURL) VALUES('3333297870832','ahahah','desc','image',12.0,'renturll')";
        DataSource instance = new DataSource();
        int result = instance.updateBDD(string);
        assertEquals(new Integer(1), new Integer(result));
    }

    @Test
    public void testDeleteInBDD() throws Exception {
        System.out.println("DeleteInBDD");
        String string = "DELETE FROM VIDEO WHERE ID ='3333297870832'";
        DataSource instance = new DataSource();
        int result = instance.updateBDD(string);
        assertEquals(new Integer(1), new Integer(result));
    }
}
