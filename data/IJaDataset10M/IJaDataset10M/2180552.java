package vars.all;

import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Collection;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import junit.framework.JUnit4TestAdapter;
import junit.textui.TestRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DatabaseNotFoundException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.MappingException;
import org.junit.Assert;
import org.junit.Test;
import org.mbari.jdo.castor.JDODatabaseFactory;
import org.mbari.vars.annotation.model.dao.VideoArchiveSetDAO;
import org.mbari.vars.knowledgebase.model.Concept;
import org.mbari.vars.knowledgebase.model.dao.ConceptDAO;

/**
 * @author brian
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CastorJDOSetupTest {

    private static final Logger log = LoggerFactory.getLogger(CastorJDOSetupTest.class);

    /**
     * Method description
     *
     *
     * @param args
     */
    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CastorJDOSetupTest.class);
    }

    /**
     * Verify that the castor properties file is on the classpath.
     */
    @Test
    public void testFindCastorProperties() {
        URL url = getClass().getClassLoader().getResource("castor.properties");
        if (url == null) {
            try {
                ResourceBundle.getBundle("castor");
            } catch (Exception e) {
                Assert.fail("Can't find castor.properties on the classpath. Reason: " + e.getMessage());
            }
        } else {
            log.info("Found castor.properties at '" + url + "'");
        }
    }

    /**
     * Locate the vars properties bundle.
     */
    @Test
    public void testFindVarsProperties() {
        String file = "vars.properties";
        log.debug("Looking for '" + file + "'");
        URL url = getClass().getClassLoader().getResource(file);
        if (url == null) {
            Assert.fail("Couldn't find '" + file + "' on the classpath");
        } else {
            log.debug("Found '" + file + "' at '" + url.toExternalForm() + "'");
        }
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("vars");
            url = getClass().getClassLoader().getResource(bundle.getString("castor.database"));
            if (url == null) {
                Assert.fail("Unable to locate " + bundle.getString("castor.database") + " on the classpath");
            } else {
                log.info("Configuring castor using the mapping file '" + url.toExternalForm() + "'");
            }
        } catch (MissingResourceException e) {
            Assert.fail("Unable to find vars.properties on the class path");
        }
    }

    /**
     * Verify that we can get a VARS database object via castor JDO
     */
    @Test
    public void testGetDatabaseVars() {
        try {
            Database db = JDODatabaseFactory.getDatabase("vars");
            db.close();
        } catch (DatabaseNotFoundException e) {
            String message = "Database was not found. Reason:" + e.getMessage();
            log.info(message, e);
            Assert.fail(message);
        } catch (PersistenceException e) {
            String message = "A PersistenceException was thrown. Reason: " + e.getMessage();
            log.info(message, e);
            Assert.fail(message);
        } catch (NullPointerException e) {
            String message = "JDODatabaseFactory.getDatabase('vars') returned null. The most" + " likely cause is that the mapping files and the" + " database are not in sync";
            log.info(message, e);
            Assert.fail();
        } catch (MappingException e) {
            String message = "A Mapping Exception was thrown. Reason: " + e.getMessage();
            log.info(message, e);
            Assert.fail(message);
        }
    }

    /**
     * Tests the Castor database connection by retriving the connections metadata 50 times
     * This assures that we are able to grab and release connections back to the connection
     * pool
     */
    @Test(timeout = 1000 * 60)
    public void testJDBCConnection() {
        for (int i = 0; i < 50; i++) {
            String server = null;
            try {
                Database database = JDODatabaseFactory.getDatabase("vars");
                database.begin();
                Connection connection = database.getJdbcConnection();
                DatabaseMetaData metadata = connection.getMetaData();
                server = metadata.getURL();
                database.commit();
                database.close();
            } catch (Exception e) {
                String message = "JDBC usage caused an error: " + e.getClass() + "->" + e.getMessage();
                log.info(message, e);
                Assert.fail(message);
            }
            Assert.assertNotNull("Failed to get db url", server);
        }
    }

    /**
     * Attempts to read the root concept. Be aware that when this test is run on a new database the
     * root concept may be null.
     */
    @Test
    public void testReadAccessToConcepts() {
        try {
            Concept c = ConceptDAO.getInstance().findRoot();
            if (c == null) {
                log.warn("Allthough a database error did not seem to occur, no root concept was found in the database");
            }
        } catch (Exception e) {
            Assert.fail("Failed to retrive root concept. Reason: " + e.getMessage());
        }
    }

    /**
     * Attempts to read any videoArchvieSets in the database. Be aware that when this test is run on a new database,
     * no VideoArchiveSets may be available.
     */
    @Test
    public void testReadAccessToVideoSets() {
    }
}
