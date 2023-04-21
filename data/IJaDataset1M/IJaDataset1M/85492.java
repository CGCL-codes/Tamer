package org.databene.dbsanity.model.identity;

import static org.junit.Assert.*;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import org.databene.commons.ArrayFormat;
import org.databene.commons.version.Versions;
import org.databene.dbsanity.model.CheckContext;
import org.databene.dbsanity.model.DefectIterator;
import org.databene.dbsanity.model.DefectRow;
import org.databene.dbsanity.model.nk.IdentityCheck;
import org.databene.jdbacl.dialect.HSQLUtil;
import org.databene.jdbacl.identity.IdentityModel;
import org.databene.jdbacl.identity.IdentityProvider;
import org.databene.jdbacl.identity.InvalidIdentityDefinitionError;
import org.databene.jdbacl.identity.NkPkQueryIdentity;
import org.databene.jdbacl.model.DBTable;
import org.databene.jdbacl.model.Database;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link IdentityCheck} class.<br/><br/>
 * Created: 06.12.2010 21:04:23
 * @since 0.4
 * @author Volker Bergmann
 */
public class IdentityCheckTest extends AbstractIdentityTest {

    private static final File SOURCE_FILE = new File("target", "source.xml");

    private static final File TEMP_FOLDER = new File("target");

    private static final File TARGET_FOLDER = new File("target");

    private static final File DOC_PAGE = new File("target.index.html");

    Connection connection;

    Database database;

    IdentityProvider identityProvider;

    DBTable countryDbTable;

    IdentityModel countryIdentity;

    @Before
    public void setUp() throws Exception {
        connection = connectDB("db", HSQLUtil.DEFAULT_PORT + 1);
        createTables(connection);
        insertData(connection);
        database = importDatabase(connection);
        identityProvider = createIdentities(database);
        countryIdentity = identityProvider.getIdentity("country");
        countryDbTable = database.getTable("country");
    }

    @After
    public void tearDown() throws SQLException {
        dropTables(connection);
    }

    @Test
    public void testCorrectIdentity() {
        IdentityCheck check = new IdentityCheck("checkName", countryIdentity, Versions.createUnlimited(), "db", SOURCE_FILE, TEMP_FOLDER, TARGET_FOLDER, DOC_PAGE);
        DefectIterator defectIterator = check.perform(new CheckContext(connection, database, 1));
        assertFalse(defectIterator.hasNext());
    }

    @Test
    public void testDuplicateNK() {
        IdentityModel countryIdentity = new NkPkQueryIdentity("country", "select 'X', CODE from COUNTRY");
        IdentityCheck check = new IdentityCheck("checkName", countryIdentity, Versions.createUnlimited(), "db", SOURCE_FILE, TEMP_FOLDER, TARGET_FOLDER, DOC_PAGE);
        DefectIterator defectIterator = check.perform(new CheckContext(connection, database, 1));
        assertNextRow("X", "FR", "Duplicate natural key", defectIterator);
        assertNextRow("X", "DE", "Duplicate natural key", defectIterator);
        assertNextRow("X", "UK", "Duplicate natural key", defectIterator);
        assertFalse(defectIterator.hasNext());
    }

    @Test
    public void testDuplicatePK() {
        IdentityModel countryIdentity = new NkPkQueryIdentity("country", "select CODE, 'X' from COUNTRY");
        IdentityCheck check = new IdentityCheck("checkName", countryIdentity, Versions.createUnlimited(), "db", SOURCE_FILE, TEMP_FOLDER, TARGET_FOLDER, DOC_PAGE);
        DefectIterator defectIterator = check.perform(new CheckContext(connection, database, 1));
        assertTrue(defectIterator.hasNext());
        while (defectIterator.hasNext()) System.out.println("DPK: " + ArrayFormat.format(defectIterator.next().getCells()));
    }

    @Test(expected = InvalidIdentityDefinitionError.class)
    public void testMissingTuples() {
        IdentityModel countryIdentity = new NkPkQueryIdentity("country", "select CODE, CODE from COUNTRY where CODE != 'UK'");
        IdentityCheck check = new IdentityCheck("checkName", countryIdentity, Versions.createUnlimited(), "db", SOURCE_FILE, TEMP_FOLDER, TARGET_FOLDER, DOC_PAGE);
        DefectIterator defectIterator = check.perform(new CheckContext(connection, database, 1));
        while (defectIterator.hasNext()) defectIterator.next();
    }

    private void assertNextRow(String nk, String pk, String message, DefectIterator defectIterator) {
        assertTrue(defectIterator.hasNext());
        DefectRow row = defectIterator.next();
        assertEquals(nk, row.get(0));
        assertEquals(pk, row.get(1));
        assertEquals(message, row.get(2));
    }
}
