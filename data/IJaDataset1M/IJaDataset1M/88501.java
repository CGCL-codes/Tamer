package hu.sqooler.jdbc;

import hu.sqooler.exception.SqoolerSQLException;
import hu.sqooler.plugin.SqoolerPlugin;
import hu.sqooler.views.*;
import java.sql.*;
import java.util.*;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public class DatabasePackage extends AbstractConnectionTreeItem {

    private String fPackageName = null;

    public DatabasePackage(String connectionAlias, String schema) {
        super(connectionAlias, schema);
    }

    public Object[] getChildren() {
        ArrayList list = new ArrayList();
        try {
            Connection con = ConnectionsView.getSelectedDatabase().getConnection();
            SortedMap procedures = SqoolerPlugin.getDefault().getDatabase(getActiveDatabase().getDatabaseType()).getProcedures(con.getMetaData(), con.getCatalog(), getSchema(), fPackageName, "%");
            for (Iterator iter = procedures.entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry element = (Map.Entry) iter.next();
                DatabaseStoredProcedure itemStoredProcedure = new DatabaseStoredProcedure(getConnectionAlias(), getSchema(), fPackageName);
                itemStoredProcedure.setProcedureName((String) ((HashMap) element.getValue()).get("PROCEDURE_NAME"));
                if (((Integer) ((HashMap) element.getValue()).get("PROCEDURE_TYPE")).intValue() == DatabaseMetaData.procedureReturnsResult) {
                    itemStoredProcedure.setFunction(true);
                }
                list.add(itemStoredProcedure);
            }
        } catch (SqoolerSQLException e) {
            SqoolerPlugin.log(IStatus.ERROR, "Error reading procedure names.", e, true);
        } catch (SQLException e) {
            SqoolerPlugin.log(IStatus.ERROR, "Error reading procedure names.", e);
            SqlView.errorLog(e.getMessage());
        }
        return list.toArray();
    }

    public Image getImage() {
        ImageDescriptor descriptor = SqoolerPlugin.getImageDescriptor("package.gif");
        Image image = descriptor.createImage();
        return image;
    }

    public String getText() {
        return fPackageName;
    }

    public int getType() {
        return IConnectionTreeItem.PACKAGE;
    }

    public boolean hasChildren() {
        return true;
    }

    /**
    * @param text The text to set.
    */
    public void setText(String text) {
        fPackageName = text;
    }

    public String getPackageName() {
        return fPackageName;
    }

    public void setPackageName(String packageName) {
        fPackageName = packageName;
    }
}
