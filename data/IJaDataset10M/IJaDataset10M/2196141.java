package uk.ac.warwick.dcs.cokefolk.server.backend.sql;

import java.util.*;
import uk.ac.warwick.dcs.cokefolk.server.ServerInteraction;
import uk.ac.warwick.dcs.cokefolk.server.backend.DatabaseException;
import uk.ac.warwick.dcs.cokefolk.server.operations.Identifier;
import uk.ac.warwick.dcs.cokefolk.server.operations.types.*;
import uk.ac.warwick.dcs.cokefolk.server.typechecker.*;
import uk.ac.warwick.dcs.cokefolk.server.users.SystemUser;
import uk.ac.warwick.dcs.cokefolk.server.operations.types.TestDatabase;
import uk.ac.warwick.dcs.cokefolk.util.console.ConsoleInteraction;
import junit.framework.TestCase;

public class TestSQLPersistence extends TestCase {

    SQLPersistence pp;

    private static final ServerInteraction IO = new ConsoleInteraction();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SystemUser user = new SystemUser();
        user.setUsername("postgres");
        user.setDefaultSchema(SQLPersistence.DEFAULT_DATABASE_NAME);
        user.setDatabaseUsername("postgres");
        user.setDatabasePassword("password");
        pp = SQLPersistence.getInstance(user, IO);
    }

    public final void testDKindToSQLType() {
    }

    public final void testGetInstance() {
        try {
            SQLPersistence pp1 = SQLPersistence.getInstance(new SystemUser(), IO);
            assertTrue(pp1 != null);
            SQLPersistence pp2 = SQLPersistence.getInstance(new SystemUser(SystemUser.DEFAULT_USERNAME, SystemUser.DEFAULT_PASSWORD, SystemUser.USER_PRIVILEGE), IO);
            assertTrue(pp2 != null);
            SQLPersistence pp3 = SQLPersistence.getInstance(new SystemUser(SystemUser.DEFAULT_USERNAME, SystemUser.DEFAULT_PASSWORD, SystemUser.ADMINISTRATOR_PRIVILEGE), IO);
            assertTrue(pp3 != null);
        } catch (DatabaseException e) {
            fail(e.toString());
        }
    }

    public final void testSQLAttribToDType() throws Exception {
    }

    public final void testSQLTypeToDKind() throws Exception {
        int sqlType;
        sqlType = java.sql.Types.BIGINT;
        DKind dki = SQLPersistence.SQLTypeToDKind(sqlType);
        assertEquals(dki, DKindPrimitive.INTEGER);
    }

    public final void testAssign() {
    }

    public final void testCreateDatabase() {
        try {
            String name = RandomUtils.getRandomString(10);
            boolean exist = pp.doesDatabaseExist(name);
            while (exist) {
                name = RandomUtils.getRandomString(10);
                exist = pp.doesDatabaseExist(name);
            }
            pp.createDatabase(name);
            assertTrue(pp.doesDatabaseExist(name));
            pp.dropDatabase(name);
            assertFalse(pp.doesDatabaseExist(name));
        } catch (DatabaseException dbe) {
            fail(dbe.toString());
        }
    }

    public final void testCreateTestDatabase() {
        System.out.println("beginning testCreateTestDatabase");
        try {
            createTestDatabase();
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    public final void testCreateSampleDatabase() {
        try {
            pp.createSampleDatabase();
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    public final void testDeclareRelvar() {
        try {
            String name = RandomUtils.getRandomString(10);
            boolean exist = pp.doesTableExist(name);
            while (exist) {
                name = RandomUtils.getRandomString(10);
                exist = pp.doesTableExist(name);
            }
            DKindTuple sig = new DKindTuple(new String[] { "a", "b" }, new DKind[] { DKindPrimitive.INTEGER, DKindPrimitive.INTEGER });
            DKindRelation signature = new DKindRelation(sig);
            pp.declareRelvar(name, signature);
            assertTrue(pp.doesTableExist(name));
            pp.dropRelvar(name);
            assertFalse(pp.doesTableExist(name));
        } catch (DatabaseException dbe) {
            fail(dbe.toString());
        } catch (UnboundVariableException uve) {
            fail(uve.toString());
        } catch (TypeMismatchException tme) {
            fail(tme.toString());
        }
    }

    public final void testfetchRelvar() {
        try {
            TestDatabase td = new TestDatabase();
            String[] relNames = td.getDRelationNames();
            DRelation[] rels = td.getDRelations();
            TupleSet retrievedTup;
            DRelation retrieved;
            for (int i = 0; i < relNames.length; i++) {
                retrievedTup = pp.fetchRelvar(relNames[i]);
                retrieved = new DRelation(retrievedTup, retrievedTup.getSignature());
                assertTrue(retrieved.equalTo(rels[i]).theBoolean());
            }
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    public final void testGetRelvarCatalog() {
        try {
            Map<String, Identifier> cat = pp.getRelvarCatalog();
            Set<String> keys = cat.keySet();
            for (String key : keys) {
                assertTrue(pp.doesTableExist(key));
            }
        } catch (DatabaseException dbe) {
            fail(dbe.toString());
        }
    }

    public final void testGetRelvarSignature() {
        try {
            TestDatabase td = new TestDatabase();
            String[] relNames = td.getDRelationNames();
            DRelation[] rels = td.getDRelations();
            DKindTuple signature;
            for (int i = 0; i < relNames.length; i++) {
                signature = pp.getRelvarSignature(relNames[i]);
                assertEquals(new DKindRelation(signature), rels[i].getKind());
            }
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    public final void testGetRelvarSize() {
        try {
            TestDatabase td = new TestDatabase();
            String[] relNames = td.getDRelationNames();
            DRelation[] rels = td.getDRelations();
            int size;
            for (int i = 0; i < relNames.length; i++) {
                size = pp.getRelvarSize(relNames[i]);
                assertEquals(size, rels[i].tuples().size());
            }
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    public final void testMergeIntervalKinds() {
    }

    public final void testMergeIntervalLabels() {
    }

    public final void testRegisterUser() {
        String name = RandomUtils.getRandomString(4);
        SystemUser user = new SystemUser(name, SystemUser.DEFAULT_PASSWORD);
        boolean exists = pp.isUserRegistered(user);
        while (exists) {
            name = RandomUtils.getRandomString(4);
            user.setUsername(name);
            exists = pp.isUserRegistered(user);
        }
        pp.registerUser(user);
        assertTrue(pp.isUserRegistered(user));
        try {
            pp.removeUser(user);
            assertFalse(pp.isUserRegistered(user));
        } catch (DatabaseException e) {
            fail();
        }
    }

    public final void testRelvarContains() {
    }

    public final void testResultSetToDTuple() {
    }

    public final void testResultSetToTupleSet() {
    }

    public final void testDoesDatabaseExist() {
        try {
            String n = RandomUtils.getRandomString(15);
            assertFalse(pp.doesDatabaseExist(n));
            pp.createDatabase(n);
            assertTrue(pp.doesDatabaseExist(n));
            pp.dropDatabase(n);
            assertFalse(pp.doesDatabaseExist(n));
        } catch (DatabaseException dbe) {
            fail(dbe.toString());
        }
    }

    public final void testDoesTableExist() {
        testCreateTestDatabase();
        TestDatabase td = new TestDatabase();
        String[] relNames = td.getDRelationNames();
        for (int i = 0; i < relNames.length; i++) {
            System.out.println("checking if " + relNames[i] + " exists");
            assertTrue(pp.doesTableExist(relNames[i]));
        }
        assertFalse(pp.doesTableExist("this_table_does_not_exist"));
        assertFalse(pp.doesTableExist(""));
    }

    /**
	 * Cheeky little method to output TestDatabase.websiteString()
	 */
    public final void testSampleDatabaseWebsiteString() {
        SampleDatabase sd = new SampleDatabase();
        System.out.println("-----");
        System.out.println(sd.websiteString());
        System.out.println("-----");
    }

    /**
	 * Creates a test database. The database to create is obtained from the
	 * DRelation objects in the TestDatabase class.
	 * 
	 * @throws DatabaseException
	 */
    public void createTestDatabase() throws DatabaseException {
        try {
            Class testDatabase = TestDatabase.class;
            TestDatabase td = new TestDatabase();
            String[] dRelations = td.getDRelationNames();
            for (int i = 0; i < dRelations.length; i++) {
                String relName = dRelations[i];
                System.out.println("Creating " + relName);
                DRelation drel = (DRelation) testDatabase.getField(dRelations[i]).get(td);
                DKindRelation sig = drel.getKind();
                pp.declareRelvar(relName, sig);
                System.out.println("Declared " + relName);
                pp.assign(relName, drel);
                System.out.println("Assigned " + relName);
            }
        } catch (Exception e) {
            throw new DatabaseException(e.toString());
        }
    }
}
