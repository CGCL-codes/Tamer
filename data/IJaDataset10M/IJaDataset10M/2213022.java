package org.identifylife.taxonomy.store.repository;

import java.util.Properties;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * @author mike
 *
 */
@ContextConfiguration
public abstract class AbstractRepositoryTests extends AbstractTransactionalJUnit4SpringContextTests {

    protected IDatabaseTester databaseTester;

    protected Class<?> testClass;

    public AbstractRepositoryTests(Class<?> testClass) throws Exception {
        this.testClass = testClass;
        Properties props = new Properties();
        props.load(getClass().getResourceAsStream("/hibernate.test.properties"));
        databaseTester = new JdbcDatabaseTester(props.getProperty("jdbc.driverClassName"), props.getProperty("jdbc.url") + "?&sessionVariables=FOREIGN_KEY_CHECKS=0", props.getProperty("jdbc.username"), props.getProperty("jdbc.password"));
    }

    @Before
    public void setUp() throws Exception {
        String filename = "/test-data/dataset/taxon-dataset-16.xml";
        if (logger.isInfoEnabled()) {
            logger.info("loading dataset from: " + filename);
        }
        IDatabaseConnection connection = null;
        try {
            IDataSet dataset = new FlatXmlDataSetBuilder().setColumnSensing(true).build(getClass().getResourceAsStream(filename));
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
    }
}
