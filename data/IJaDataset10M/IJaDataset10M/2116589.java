package com.hs.mail.test;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.lang.ArrayUtils;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

public abstract class AbstractTransactionalTestCase extends AbstractTransactionalDataSourceSpringContextTests {

    private String[] testDataFiles;

    protected String[] getConfigLocations() {
        return new String[] { "classpath:conf/testContext.xml" };
    }

    protected void setTestDataFiles(String[] files) {
        this.testDataFiles = files;
    }

    protected DataSource getDataSource() {
        return this.jdbcTemplate.getDataSource();
    }

    @Override
    protected void onSetUpInTransaction() throws Exception {
        setUpDatabase();
    }

    private void setUpDatabase() throws Exception {
        if (!ArrayUtils.isEmpty(testDataFiles)) {
            IDatabaseConnection connection = getConnection();
            try {
                for (String resource : testDataFiles) {
                    DatabaseOperation.INSERT.execute(connection, new FlatXmlDataSet(new ClassPathResource(resource).getInputStream()));
                }
            } finally {
                releaseConnection(connection);
            }
        }
    }

    protected IDatabaseConnection getConnection() throws Exception {
        return new DatabaseConnection(getDataSource().getConnection());
    }

    protected void releaseConnection(IDatabaseConnection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }
    }

    public void dumpTable(String tableName) throws Exception {
        IDatabaseConnection connection = getConnection();
        try {
            IDataSet dataSet = connection.createDataSet(new String[] { tableName });
            if (dataSet != null) {
                FlatXmlDataSet.write(dataSet, System.out);
            }
        } finally {
            releaseConnection(connection);
        }
    }

    @Override
    protected void onTearDownInTransaction() throws Exception {
        tearDownDatabase();
    }

    private void tearDownDatabase() throws Exception {
        if (!ArrayUtils.isEmpty(testDataFiles)) {
            IDatabaseConnection connection = getConnection();
            try {
                for (String resource : testDataFiles) {
                    DatabaseOperation.DELETE.execute(connection, new FlatXmlDataSet(new ClassPathResource(resource).getInputStream()));
                }
            } finally {
                releaseConnection(connection);
            }
        }
    }
}
