package hu.sqooler.jdbc;

import hu.sqooler.plugin.SqoolerPlugin;
import hu.sqooler.views.*;
import java.sql.SQLException;
import java.util.HashMap;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public class DatabaseTablePrimaryKey extends AbstractConnectionTreeItem {

    private String fColumnName = null;

    private String fTableName = null;

    private HashMap fColumnProps = null;

    public DatabaseTablePrimaryKey(String connectionAlias, String schema, String tableName) {
        super(connectionAlias, schema);
        fTableName = tableName;
    }

    public Object[] getChildren() {
        return null;
    }

    public Image getImage() {
        ImageDescriptor descriptor = SqoolerPlugin.getImageDescriptor("pk_column2.gif");
        Image image = descriptor.createImage();
        return image;
    }

    public String getText() {
        StringBuffer striBu = new StringBuffer();
        if (fColumnProps != null) {
            striBu.append(fColumnName);
            striBu.append(", ");
            striBu.append(fColumnProps.get("TYPE_NAME"));
            striBu.append("(");
            striBu.append(fColumnProps.get("COLUMN_SIZE"));
            if (fColumnProps.get("DECIMAL_DIGITS") != null) {
                striBu.append(",");
                striBu.append(fColumnProps.get("DECIMAL_DIGITS"));
            }
            striBu.append(")");
            return striBu.toString();
        }
        return fColumnName;
    }

    public String getColumnName() {
        return fColumnName;
    }

    public void setColumnName(String text) {
        fColumnName = text;
    }

    public int getType() {
        return IConnectionTreeItem.PRIMARY_KEY;
    }

    public boolean hasChildren() {
        return false;
    }

    /**
    * @return Returns the tableName.
    */
    public String getTableName() {
        return fTableName;
    }

    public void setColumnProperties(HashMap map) {
        fColumnProps = map;
    }

    public Object getColumnProperty(String propertyName) throws SQLException {
        if (fColumnProps.containsKey(propertyName)) {
            return fColumnProps.get(propertyName);
        }
        throw new SQLException(propertyName + " is not a valid property");
    }
}
