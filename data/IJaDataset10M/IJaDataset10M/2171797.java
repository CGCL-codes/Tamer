package uk.co.whisperingwind.vienna;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.table.TableModel;
import uk.co.whisperingwind.framework.ExceptionDialog;
import uk.co.whisperingwind.framework.Model;
import uk.co.whisperingwind.framework.SwingThread;
import uk.co.whisperingwind.framework.VectorTableModel;

/**
** Foreign key Model for the Schema View. Maintains a list of columns
** comprising the table's foreign key.
*/
class SchemaForeignModel extends Model {

    private Connection connection = null;

    private String schemaName = null;

    private String loadedTable = null;

    private HashMap typeMap = null;

    private ForeignLoader foreignLoader = null;

    private VectorTableModel foreignModel = new VectorTableModel();

    public SchemaForeignModel(Connection theConnection, String theSchemaName) {
        connection = theConnection;
        schemaName = theSchemaName;
        foreignModel.addName("Column", 32);
        foreignModel.addName("References", 32);
        foreignModel.addName("In", 32);
        foreignModel.addName("Sequence", 32);
    }

    public TableModel getTableModel() {
        return foreignModel;
    }

    public void clear() {
        foreignModel.clear();
    }

    public boolean load(String tableName) {
        boolean started = false;
        if (tableName != null && !tableName.equals(loadedTable)) {
            if (foreignLoader != null) foreignLoader.interrupt();
            loadedTable = new String(tableName);
            foreignLoader = new ForeignLoader();
            foreignLoader.start();
            started = true;
        }
        return started;
    }

    private class ForeignLoader extends SwingThread {

        private VectorTableModel newForeignModel = new VectorTableModel();

        public void construct() {
            newForeignModel.addName("Column", 32);
            newForeignModel.addName("References", 32);
            newForeignModel.addName("In", 32);
            newForeignModel.addName("Sequence", 32);
            try {
                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet columnSet = metaData.getImportedKeys(null, schemaName, loadedTable);
                while (columnSet.next() && !stopped) {
                    String column = columnSet.getString("FKCOLUMN_NAME");
                    String references = columnSet.getString("PKCOLUMN_NAME");
                    String table = columnSet.getString("PKTABLE_NAME");
                    String sequence = columnSet.getString("KEY_SEQ");
                    Vector row = newForeignModel.addRow();
                    row.add(column);
                    row.add(references);
                    row.add(table);
                    row.add(sequence);
                }
            } catch (SQLException ex) {
                new ExceptionDialog(ex);
            }
        }

        public void finished() {
            if (!stopped) {
                foreignModel = newForeignModel;
                fireEvent(SchemaForeignModel.this, "foreign", "updated");
            }
            foreignLoader = null;
        }
    }
}
