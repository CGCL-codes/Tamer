package org.identifylife.key.engine.core.repository.hibernate;

import java.io.File;
import java.util.List;
import java.util.Properties;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.identifylife.key.engine.core.model.Character;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * @author dbarnier
 *
 */
@ContextConfiguration
public class HibernateCharacterRepositoryTests extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    protected HibernateCharacterRepository repository;

    private IDatabaseTester databaseTester;

    public HibernateCharacterRepositoryTests() throws Exception {
        Properties props = new Properties();
        props.load(getClass().getResourceAsStream("/jdbc.test.properties"));
        databaseTester = new JdbcDatabaseTester(props.getProperty("jdbc.driverClassName"), props.getProperty("jdbc.url") + "&sessionVariables=FOREIGN_KEY_CHECKS=0", props.getProperty("jdbc.username"), props.getProperty("jdbc.password"));
    }

    @Test
    public void testGetByUuid() {
        Character result = repository.getByUuid("urn:identifylife.org:character:1");
        logger.info("result: " + result);
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getStates());
        Assert.assertTrue(result.getStates().size() == 3);
    }

    @Test
    public void testGetByParentUuid() {
        List<Character> results = repository.getByParentUuid("urn:identifylife.org:character:2");
        logger.info("results: " + results);
        Assert.assertNotNull(results);
        Assert.assertTrue(results.size() == 2);
        Assert.assertTrue(results.get(0).getUuid().equals("urn:identifylife.org:character:2.1"));
    }

    @Before
    public void setUp() throws Exception {
        if (getTablesPopulated()) {
            return;
        }
        String filename = "src/test/resources/test-data/characters.xml";
        if (logger.isDebugEnabled()) {
            logger.info("loading dataset from: " + filename);
        }
        IDatabaseConnection connection = null;
        try {
            IDataSet dataset = new FlatXmlDataSetBuilder().setColumnSensing(true).build(new File(filename));
            connection = databaseTester.getConnection();
            DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
        } catch (Exception ex) {
            logger.error("error loading dataset: " + ex.getMessage(), ex);
            throw ex;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        setTablesPopulated(true);
    }

    private static boolean tablesPopulated;

    private static boolean getTablesPopulated() {
        return tablesPopulated;
    }

    private static void setTablesPopulated(boolean populated) {
        tablesPopulated = populated;
    }
}
