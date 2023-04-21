package de.kapsi.net.daap.tests;

import junit.framework.*;
import de.kapsi.net.daap.*;

public class LibraryTest extends TestCase {

    public static TestSuite suite() {
        return new TestSuite(LibraryTest.class);
    }

    private Library library;

    public LibraryTest(String name) {
        super(name);
    }

    public void setUp() {
        library = new Library("TestLibrary");
    }

    public void testChangeLibraryName() {
        int revision = library.getRevision();
        String name = library.getName();
        Transaction txn = library.beginTransaction();
        library.setName(txn, "OK");
        txn.commit();
        assertTrue(library.getRevision() == (revision + 1));
        assertTrue(library.getName().equals("OK"));
    }

    public void testAddDatabase() {
        Database database = new Database("Database");
        int revision = library.getRevision();
        Transaction txn = library.beginTransaction();
        library.addDatabase(txn, database);
        txn.commit();
        assertTrue(library.getRevision() == (revision + 1));
        assertTrue(library.getDatabaseCount() == 1);
        assertTrue(library.containsDatabase(database));
    }

    public void testRemoveDatabase() {
        Database database = new Database("Database");
        Transaction txn = library.beginTransaction();
        library.addDatabase(txn, database);
        txn.commit();
        int revision = library.getRevision();
        txn = library.beginTransaction();
        library.removeDatabase(txn, database);
        txn.commit();
        assertTrue(library.getRevision() == (revision + 1));
        assertTrue(library.getDatabaseCount() == 0);
        assertFalse(library.containsDatabase(database));
    }
}
