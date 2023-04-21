package org.dicom4jserver.gui.table;

import java.util.ArrayList;
import java.util.List;
import org.dicom4jserver.dao.DaoLayer;
import org.dicom4jserver.dao.beans.config.DicomNodeBean;
import org.dolmen.swing.table.BeanTableModel;

/**
 * 
 *
 * @since 0.0.1
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte 
 *
 */
public class DicomNodesTableModel extends BeanTableModel {

    public List createBeanList() {
        return new ArrayList();
    }

    public int getColumnCount() {
        return 4;
    }

    public String getColumnName(int column) {
        if (column == 0) {
            return "Name";
        }
        if (column == 1) {
            return "Hostname";
        }
        if (column == 2) {
            return "Port";
        }
        if (column == 3) {
            return "AET";
        }
        return "";
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        DicomNodeBean lNode = (DicomNodeBean) getBean(rowIndex);
        if (lNode != null) {
            if (columnIndex == 0) {
                return lNode.getName();
            }
            if (columnIndex == 1) {
                return lNode.getHostName();
            }
            if (columnIndex == 2) {
                return (Integer.toString(lNode.getPort()));
            }
            if (columnIndex == 3) {
                return lNode.getAET();
            }
        }
        return null;
    }
}
