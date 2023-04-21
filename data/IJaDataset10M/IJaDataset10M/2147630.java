package shellkk.qiq.gui.data;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.datamining.JDMException;
import javax.datamining.data.PhysicalDataSet;
import javax.datamining.data.PhysicalDataSetFactory;
import shellkk.qiq.jdm.data.PhysicalDataSetImpl;
import shellkk.qiq.jdm.engine.uriaccess.IURIDataAccessObject;
import shellkk.qiq.jdm.engine.uriaccess.JDBCDataAccessObject;
import shellkk.qiq.jdm.resource.ConnectionImpl;

public class DatabaseDataConfig implements DataConfig {

    protected DataLocationStep step;

    protected String tableName;

    protected List<String> tables = new ArrayList();

    protected JDBCDataAccessObject selectedDataSource;

    protected String selectedTable;

    protected PhysicalDataSetImpl physicalData;

    public DatabaseDataConfig(DataLocationStep step) {
        this.step = step;
    }

    public List<JDBCDataAccessObject> getDataSources() {
        ArrayList<JDBCDataAccessObject> all = new ArrayList();
        for (IURIDataAccessObject ds : step.getUriEngine().getDaos()) {
            if (ds instanceof JDBCDataAccessObject) {
                all.add((JDBCDataAccessObject) ds);
            }
        }
        return all;
    }

    public void loadTables() throws ClassNotFoundException, SQLException {
        tables.clear();
        selectedTable = null;
        Connection conn = null;
        try {
            Class.forName(selectedDataSource.getDriver());
            conn = DriverManager.getConnection(selectedDataSource.getJdbcurl(), selectedDataSource.getUsername(), selectedDataSource.getPassword());
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet results = meta.getTables(null, null, null, null);
            while (results.next()) {
                String table = results.getString("TABLE_NAME");
                if (tableName == null || table.startsWith(tableName)) {
                    tables.add(table);
                }
            }
        } catch (ClassNotFoundException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void loadPhysicalData() throws JDMException {
        String uri = null;
        if (selectedDataSource.getPrefix().endsWith("/")) {
            uri = selectedDataSource.getPrefix() + selectedTable;
        } else {
            uri = selectedDataSource.getPrefix() + "/" + selectedTable;
        }
        ConnectionImpl connection = null;
        try {
            connection = step.getLogin().getConnection();
            PhysicalDataSetFactory dataFactory = (PhysicalDataSetFactory) connection.getFactory(PhysicalDataSet.class.getName());
            physicalData = (PhysicalDataSetImpl) dataFactory.create(uri, true);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public String getBuildDataURI() {
        if (selectedDataSource != null && selectedTable != null) {
            if (selectedDataSource.getPrefix().endsWith("/")) {
                return selectedDataSource.getPrefix() + selectedTable;
            } else {
                return selectedDataSource.getPrefix() + "/" + selectedTable;
            }
        } else {
            return null;
        }
    }

    public String getName() {
        return "Database";
    }

    public String getTitle() {
        return "Database";
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getTables() {
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public JDBCDataAccessObject getSelectedDataSource() {
        return selectedDataSource;
    }

    public void setSelectedDataSource(JDBCDataAccessObject selectedDataSource) {
        this.selectedDataSource = selectedDataSource;
    }

    public String getSelectedTable() {
        return selectedTable;
    }

    public void setSelectedTable(String selectedTable) {
        this.selectedTable = selectedTable;
    }

    public PhysicalDataSetImpl getPhysicalData() {
        return physicalData;
    }

    public void setPhysicalData(PhysicalDataSetImpl physicalData) {
        this.physicalData = physicalData;
    }
}
