package net.sourceforge.jwakeup;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import java.io.File;

/**
 * This class manages the access of the database.
 * Machine: Matthias Heidenreich
 */
public class DatabaseAdapterTest extends TestCase {

    private static final Logger LOGGER = Logger.getLogger(DatabaseAdapterTest.class);

    private static File c_DbFile = new File("testdb.obj");

    protected void setUp() throws Exception {
        c_DbFile.delete();
        DatabaseAdapter.changeDbFile(c_DbFile);
    }

    public void testDbAccess() {
        Machine m = new Machine("Matthias");
        DatabaseAdapter.create(m);
        m = new Machine("Andreas");
        DatabaseAdapter.create(m);
        m = new Machine("Hans");
        DatabaseAdapter.create(m);
        m = new Machine("Claudia");
        DatabaseAdapter.create(m);
        m.setUsername("Martha");
        DatabaseAdapter.update(m);
        m = DatabaseAdapter.getMachineByName("Martha");
        assertNotNull(m);
        assertNull(DatabaseAdapter.getMachineByName("Claudia"));
        m = DatabaseAdapter.getMachineByName("Matthias");
        Machine check = DatabaseAdapter.getMachineById(m.getId());
        assertNotNull(check);
        assertEquals(check, m);
        check = DatabaseAdapter.getMachineById(1);
        assertNotNull(check);
        m = DatabaseAdapter.queryById(1);
        assertNotNull(m);
        assertEquals(m, check);
        m.setIpAdress("irgendwas");
        DatabaseAdapter.update(m);
        m = DatabaseAdapter.getMachineByName("Matthias");
        assertNotNull(m);
        assertNotNull(m.getIpAdress());
        assertFalse(m.getIpAdress().equals(check.getIpAdress()));
        ObjectContainer db = Db4o.openFile(c_DbFile.getAbsolutePath());
        assertEquals(4, db.get(new Machine(null)).size());
        assertEquals(1, db.get(new Machine("Andreas")).size());
        db.close();
        m = DatabaseAdapter.getMachineByName("Matthias");
        assertNotNull(m);
        assertEquals(m.getUsername(), "Matthias");
        DatabaseAdapter.deleteById(m.getId());
        m = DatabaseAdapter.getMachineByName("Matthias");
        assertNull(m);
    }

    public void testProfileQueryMethods() {
        long before = System.currentTimeMillis();
        for (int c = 1; c <= 100; c++) {
            Machine m = new Machine("Name" + c);
            m.setIpAdress("" + c);
            DatabaseAdapter.create(m);
        }
        long afterCreation = System.currentTimeMillis();
        for (int c = 0; c < 100; c++) {
            DatabaseAdapter.getMachineByName("Name13");
        }
        long afterByName = System.currentTimeMillis();
        for (int c = 0; c < 100; c++) {
            DatabaseAdapter.getMachineById(34);
        }
        long afterById = System.currentTimeMillis();
        for (int c = 0; c < 100; c++) {
            DatabaseAdapter.queryById(45);
        }
        long afterQuery = System.currentTimeMillis();
        LOGGER.info("Creation: " + (afterCreation - before));
        LOGGER.info("getMachineByName: " + (afterByName - afterCreation));
        LOGGER.info("getMachineById: " + (afterById - afterByName));
        LOGGER.info("queryById: " + (afterQuery - afterById));
        LOGGER.info("Runtime: " + ((afterQuery - before) / 1000) + "s.");
    }

    public void testUnify() {
        for (int c = 1; c <= 10; c++) {
            Machine m = new Machine("Name" + c);
            m.setIpAdress("" + c);
            DatabaseAdapter.create(m);
        }
        for (Machine machine : DatabaseAdapter.getAll()) {
            LOGGER.info("got: " + machine.getUsername() + "(" + machine.getId() + ")");
            assertEquals(machine.getIpAdress(), "" + machine.getId());
        }
    }
}
