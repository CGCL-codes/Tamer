package br.org.skenp.metadata;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Davy Diegues Duran
 * 
 */
public class ExportedColumn implements Column {

    private static final long serialVersionUID = 3100012319721981770L;

    private String name;

    private String tableName;

    private String fkTableName;

    private String fkColumnName;

    public ExportedColumn() {
    }

    /**
	 * 
	 * @param name
	 * @param tableName
	 */
    public ExportedColumn(String name, String tableName) {
        this.name = name;
        this.tableName = tableName;
    }

    public ColumnType getColumnType() {
        return ColumnType.EXPORTED;
    }

    public String getName() {
        return name;
    }

    public String getTableName() {
        return tableName;
    }

    /**
	 * @param name
	 *            the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the fkTableName
	 */
    public String getFkTableName() {
        return fkTableName;
    }

    /**
	 * @param fkTableName
	 *            the fkTableName to set
	 */
    public void setFkTableName(String fkTableName) {
        this.fkTableName = fkTableName;
    }

    /**
	 * @return the fkColumnName
	 */
    public String getFkColumnName() {
        return fkColumnName;
    }

    /**
	 * @param fkColumnName
	 *            the fkColumnName to set
	 */
    public void setFkColumnName(String fkColumnName) {
        this.fkColumnName = fkColumnName;
    }

    /**
	 * @param tableName
	 *            the tableName to set
	 */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public static List<ExportedColumn> loadColumnMetaDatas(DatabaseMetaData metaData, Table table) throws SQLException {
        LinkedList<ExportedColumn> cmds = new LinkedList<ExportedColumn>();
        ResultSet columns = metaData.getExportedKeys(table.getCatalog(), table.getSchema(), table.getName());
        while (columns.next()) {
            ExportedColumn ec = new ExportedColumn();
            ec.setName(columns.getString("PKCOLUMN_NAME"));
            ec.setTableName(columns.getString("PKTABLE_NAME"));
            ec.setFkColumnName(columns.getString("FKCOLUMN_NAME"));
            ec.setFkTableName(columns.getString("FKTABLE_NAME"));
            cmds.add(ec);
        }
        return cmds;
    }

    @Override
    public boolean equals(Object obj) {
        Column c = null;
        try {
            c = (Column) obj;
        } catch (ClassCastException e) {
            return false;
        }
        if (c == this) return true;
        if (c == null || c.getName() == null || this.getName() == null) return false;
        return (this.getName().equals(c.getName()));
    }

    @Override
    public String toString() {
        return getColumnType() + " " + tableName + "." + name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
