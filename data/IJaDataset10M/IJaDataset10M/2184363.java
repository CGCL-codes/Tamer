package hu.sqooler.jdbc;

import hu.sqooler.exception.SqoolerSQLException;
import hu.sqooler.plugin.SqoolerPlugin;
import hu.sqooler.views.*;
import java.sql.*;
import java.util.*;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public class DatabaseStoredProcedure extends AbstractConnectionTreeItem {

    private String fPackageName = null;

    private String fProcedureName = null;

    private boolean fFunction = false;

    public DatabaseStoredProcedure(String connectionAlias, String schema, String packageName) {
        super(connectionAlias, schema);
        fPackageName = packageName;
    }

    public Object[] getChildren() {
        ArrayList list = new ArrayList();
        try {
            Connection con = ConnectionsView.getSelectedDatabase().getConnection();
            Map columns = SqoolerPlugin.getDefault().getDatabase(getActiveDatabase().getDatabaseType()).getProcedureColumns(con.getMetaData(), con.getCatalog(), getSchema(), fPackageName, fProcedureName, "%");
            DatabaseStoredProcedureColumn retColumn = null;
            for (Iterator iter = columns.entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry element = (Map.Entry) iter.next();
                DatabaseStoredProcedureColumn itemStoredProcedureColumn = new DatabaseStoredProcedureColumn(getConnectionAlias(), getSchema(), fPackageName, fProcedureName);
                itemStoredProcedureColumn.setColumnName((String) ((Map) element.getValue()).get("COLUMN_NAME"));
                itemStoredProcedureColumn.setFunction(isFunction());
                itemStoredProcedureColumn.setColumnType(((Integer) ((Map) element.getValue()).get("COLUMN_TYPE")).intValue());
                itemStoredProcedureColumn.setTypeName((String) ((Map) element.getValue()).get("TYPE_NAME"));
                if (itemStoredProcedureColumn.getColumnType() == DatabaseMetaData.procedureColumnReturn) {
                    retColumn = itemStoredProcedureColumn;
                } else {
                    list.add(itemStoredProcedureColumn);
                }
            }
            if (retColumn != null) {
                list.add(retColumn);
            }
        } catch (SqoolerSQLException e) {
            SqoolerPlugin.log(IStatus.ERROR, "Error reading procedure names.", e, true);
        } catch (SQLException e) {
            SqoolerPlugin.log(IStatus.ERROR, "Error reading procedure names.", e);
            SqlView.errorLog(e.getMessage());
        } catch (NullPointerException e) {
            SqoolerPlugin.log(IStatus.ERROR, "Error reading procedure names.", e, true);
        }
        return list.toArray();
    }

    public Image getImage() {
        if (fFunction) {
            ImageDescriptor descriptor = SqoolerPlugin.getImageDescriptor("function.gif");
            Image image = descriptor.createImage();
            return image;
        }
        ImageDescriptor descriptor = SqoolerPlugin.getImageDescriptor("procedure.gif");
        Image image = descriptor.createImage();
        return image;
    }

    public String getText() {
        return this.fProcedureName;
    }

    public int getType() {
        return IConnectionTreeItem.STORED_PROCEDURE;
    }

    public boolean hasChildren() {
        return true;
    }

    /**
    * @return Returns the tableName.
    */
    public String getPackageName() {
        return fPackageName;
    }

    /**
    * @return Returns the procedureName.
    */
    public String getProcedureName() {
        return fProcedureName;
    }

    /**
    * @param procedureName The procedureName to set.
    */
    public void setProcedureName(String procedureName) {
        fProcedureName = procedureName;
    }

    /**
    * @return Returns the function.
    */
    public boolean isFunction() {
        return fFunction;
    }

    /**
    * @param function The function to set.
    */
    public void setFunction(boolean function) {
        fFunction = function;
    }
}
