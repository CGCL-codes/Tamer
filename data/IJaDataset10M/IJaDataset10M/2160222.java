package org.openbroad.client.admin.view.model;

import java.util.ResourceBundle;
import javax.swing.table.DefaultTableModel;
import org.openbroad.client.admin.view.control.DataHolder;
import org.openbroad.shared.Type;

/**
 *
 * @author Openbroad Group
 */
public class TypeModel extends DefaultTableModel {

    public TypeModel() {
        header = new String[] { ResourceBundle.getBundle("org/openbroad/client/languages/openbroad").getString("name") };
        getData();
    }

    public void getData() {
        DataHolder.getInstance().refreshType();
        typeArray = DataHolder.getInstance().getTypeList();
        setRowCount(typeArray.length);
        for (int i = 0; i < typeArray.length; i++) {
            Type type = (Type) typeArray[i];
            if (type != null) {
                setValueAt(type, i, 0);
            }
        }
    }

    public int getColumnCount() {
        return header.length;
    }

    public String getColumnName(int col) {
        return header[col];
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    private String header[];

    private Object[] typeArray;
}
