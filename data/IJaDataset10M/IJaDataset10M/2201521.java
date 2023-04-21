package br.unifor.finance.persistence.test;

import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.DatabaseUnitException;
import org.dbunit.JdbcBasedDBTestCase;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.operation.TransactionOperation;

/**
 *
 * @author cleilson.oliveira
 */
public abstract class GenericJPADAOBase extends JdbcBasedDBTestCase {

    private final String DATASET_NAME = "FinanceDataSet.xml";

    private final String DATASET_INSERT_NAME = "FinanceInsertDataSet.xml";

    private final String DATASET_UPDATE_NAME = "FinanceUpdateDataSet.xml";

    private final String DATASET_DELETE_NAME = "FinanceDeleteDataSet.xml";

    IDataSet dataSet;

    /**
   * Creates a new instance of GenericJPADAOBase
   */
    public GenericJPADAOBase(String testName) {
        super(testName);
        createDAO();
    }

    protected abstract void createDAO();

    protected abstract String getTableName();

    protected String getDataSetFileName() {
        return DATASET_NAME;
    }

    protected String getDataSetInsertFileName() {
        return DATASET_INSERT_NAME;
    }

    protected String getDataSetUpdateFileName() {
        return DATASET_UPDATE_NAME;
    }

    protected String getDataSetDeleteFileName() {
        return DATASET_DELETE_NAME;
    }

    protected String getConnectionUrl() {
        return TestUtil.getInstance().getConnectionUrl();
    }

    protected IDataSet getDataSet() throws DatabaseUnitException {
        try {
            if (dataSet == null) dataSet = new FlatXmlDataSet(this.getClass().getResource(DATASET_NAME));
            return dataSet;
        } catch (Exception ex) {
            throw new DatabaseUnitException("Erro ao ler o IDataSet do arquivo " + DATASET_NAME + ".", ex);
        }
    }

    protected String getDriverClass() {
        return TestUtil.getInstance().getDriverClass();
    }

    protected String getPassword() {
        return TestUtil.getInstance().getPassword();
    }

    protected DatabaseOperation getSetUpOperation() throws Exception {
        return new TransactionOperation(DatabaseOperation.CLEAN_INSERT);
    }

    protected DatabaseOperation getTearDownOperation() throws Exception {
        return DatabaseOperation.DELETE_ALL;
    }

    protected String getUsername() {
        return TestUtil.getInstance().getUsername();
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    protected ITable getTableFromDataSet(String tableName) throws DataSetException {
        try {
            return getConnection().createDataSet().getTable(tableName);
        } catch (SQLException e) {
            throw new DataSetException("N�o foi poss�vel recuperar a tabela " + tableName + " devido a problemas com o banco de dados.", e);
        } catch (Exception e) {
            throw new DataSetException("N�o foi poss�vel recuperar a tabela " + tableName + ".", e);
        }
    }

    public ITable getTableFromDataSet(IDataSet dataSet, String tableName) throws DatabaseUnitException {
        try {
            return dataSet.getTable(tableName);
        } catch (Exception e) {
            throw new DatabaseUnitException("Erro ao ler a tabela " + tableName + " do DataSet.", e);
        }
    }

    public ITable getTableFromXML(String xmlFileName, String tableName) throws DatabaseUnitException {
        try {
            URL url = this.getClass().getResource(xmlFileName);
            IDataSet dataSet = new FlatXmlDataSet(url);
            return dataSet.getTable(tableName);
        } catch (Exception e) {
            throw new DatabaseUnitException("Erro ao ler a tabela " + tableName + " do arquivo " + xmlFileName + ".", e);
        }
    }

    public void assertTableEquals(ITable expectedTable, ITable actualTable) throws DataSetException, DatabaseUnitException {
        Assertion.assertEquals(actualTable, expectedTable);
    }

    public void assertTableEquals(ITable expectedTable, ITable actualTable, String[] columns) throws DataSetException, DatabaseUnitException {
        ITable actualFilterTable = DefaultColumnFilter.includedColumnsTable(actualTable, columns);
        ITable expectedFilterTable = DefaultColumnFilter.includedColumnsTable(expectedTable, columns);
        assertTableEquals(actualFilterTable, expectedFilterTable);
    }

    public void assertTableRowsEquals(ITable expectedTable, ITable actualTable) throws DataSetException, DatabaseUnitException {
        assertEquals(expectedTable.getRowCount(), actualTable.getRowCount());
    }

    public void assertTableSizeEquals(Collection collection, ITable table) throws DataSetException, DatabaseUnitException {
        int collectionSize = collection.size();
        int tableSize = table.getRowCount();
        assertEquals(collectionSize, tableSize);
    }

    public void assertTableSizeEquals(Collection collection, String tableName, IDataSet dataSet) throws DataSetException, DatabaseUnitException {
        ITable table = getTableFromDataSet(dataSet, tableName);
        assertTableSizeEquals(collection, table);
    }

    public void assertTableSizeEquals(Collection collection, String tableName) throws DataSetException, DatabaseUnitException {
        assertTableSizeEquals(collection, tableName, getDataSet());
    }

    public void assertTableSizeEquals(Map map, ITable table) throws DataSetException, DatabaseUnitException {
        int mapSize = map.size();
        int tableSize = table.getRowCount();
        assertEquals(mapSize, tableSize);
    }

    public void assertTableSizeEquals(Map map, String tableName, IDataSet dataSet) throws DataSetException, DatabaseUnitException {
        ITable table = getTableFromDataSet(dataSet, tableName);
        assertTableSizeEquals(map, table);
    }

    public void assertTableSizeEquals(Map map, String tableName) throws DataSetException, DatabaseUnitException {
        assertTableSizeEquals(map, tableName, getDataSet());
    }
}
