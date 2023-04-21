package uk.ac.roslin.ensembl.dao.database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;
import junit.framework.TestCase;
import uk.ac.roslin.ensembl.config.EnsemblDBType;
import uk.ac.roslin.ensembl.model.core.CollectionSpecies;
import uk.ac.roslin.ensembl.model.database.CollectionDatabase;
import uk.ac.roslin.ensembl.model.database.DatabaseType;

/**
 *
 * @author tpaterso
 */
public class DBCollectionTest extends TestCase {

    DBRegistry reg;

    Class reg_clazz;

    Field f;

    DBCollectionCoreDatabase db;

    public DBCollectionTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        reg = new DBRegistry();
        reg_clazz = Class.forName("uk.ac.roslin.ensembl.dao.database.DBRegistry");
        f = reg_clazz.getDeclaredField("mostRecentEnsemblVersion");
        f.setAccessible(true);
        f.set(reg, "4");
        db = new DBCollectionCoreDatabase("bob_collection_core_4_5", EnsemblDBType.collection_core, reg);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of setPrimaryCoreDatabase method, of class DBCollection.
     */
    public void testSetPrimaryCoreDatabase() {
        System.out.println("setPrimaryCoreDatabase");
        DBCollection instance = new DBCollection();
        assertTrue(instance.getCollectionName().equals(""));
        assertTrue(instance.getDBVersion().equals(""));
        assertTrue(instance.getSchemaVersion().equals(""));
        instance.setPrimaryCoreDatabase(db);
        assertTrue(instance.getCollectionName().equals("bob"));
        assertTrue(instance.getDBVersion().equals("4"));
        assertTrue(instance.getSchemaVersion().equals("5"));
    }

    /**
     * Test of setProperty method, of class DBCollection.
     */
    public void testSetProperty() {
        System.out.println("setProperty");
        String id = "";
        HashMap row = null;
        DBCollection instance = new DBCollection();
        instance.setProperty(id, row);
    }

    /**
     * Test of getDatabases method, of class DBCollection.
     */
    public void testGetDatabases() {
        System.out.println("getDatabases");
        DBCollection instance = new DBCollection();
        assertTrue(instance.getDatabases().isEmpty());
        instance.setPrimaryCoreDatabase(db);
        assertFalse(instance.getDatabases().isEmpty());
        assertTrue(instance.getDatabases().contains(db));
    }

    /**
     * Test of addDatabases method, of class DBCollection.
     */
    public void testAddDatabases() {
        System.out.println("addDatabases");
        TreeSet<CollectionDatabase> databases = new TreeSet<CollectionDatabase>();
        databases.add(db);
        DBCollection instance = new DBCollection();
        assertTrue(instance.getDatabases().isEmpty());
        instance.addDatabases(databases);
        assertFalse(instance.getDatabases().isEmpty());
        assertTrue(instance.getDatabases().contains(db));
    }

    /**
     * Test of getSpecies method, of class DBCollection.
     */
    public void testGetSpecies() {
        System.out.println("getSpecies");
        DBCollection instance = new DBCollection();
        assertTrue(instance.getSpecies().isEmpty());
        DBCollectionSpecies sp = new DBCollectionSpecies();
        instance.addSpecies(sp);
        assertFalse(instance.getSpecies().isEmpty());
        assertTrue(instance.getSpecies().contains(sp));
    }

    /**
     * Test of addSpecies method, of class DBCollection.
     */
    public void testAddSpecies_Collection() {
        System.out.println("addSpecies");
        Collection<CollectionSpecies> spp = new ArrayList<CollectionSpecies>();
        DBCollectionSpecies sp = new DBCollectionSpecies();
        spp.add(sp);
        DBCollection instance = new DBCollection();
        assertTrue(instance.getSpecies().isEmpty());
        instance.addSpecies(spp);
        assertFalse(instance.getSpecies().isEmpty());
        assertTrue(instance.getSpecies().contains(sp));
    }

    /**
     * Test of getCoreDatabase method, of class DBCollection.
     */
    public void testGetCoreDatabase() {
        System.out.println("getCoreDatabase");
        DBCollection instance = new DBCollection();
        assertNull(instance.getCoreDatabase());
        instance.setPrimaryCoreDatabase(db);
        assertEquals(instance.getCoreDatabase(), db);
    }

    /**
     * Test of getDatabasesByType method, of class DBCollection.
     */
    public void testGetDatabasesByType() {
        System.out.println("getDatabasesByType");
        DatabaseType type = EnsemblDBType.collection_core;
        DBCollection instance = new DBCollection();
        assertTrue(instance.getDatabasesByType(type).isEmpty());
        instance.setPrimaryCoreDatabase(db);
        assertFalse(instance.getDatabasesByType(type).isEmpty());
        assertTrue(instance.getDatabasesByType(EnsemblDBType.ancestral).isEmpty());
        assertTrue(instance.getDatabasesByType(type).contains(db));
    }

    /**
     * Test of toString method, of class DBCollection.
     */
    public void testToString() {
        System.out.println("toString");
        DBCollection instance = new DBCollection();
        assertTrue(instance.toString().equals(""));
        instance.setPrimaryCoreDatabase(db);
        assertTrue(instance.toString().equals("bob"));
        assertEquals(instance.toString(), instance.getCollectionName());
    }
}
