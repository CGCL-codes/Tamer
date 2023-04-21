package org.moonwave.dconfig.ui;

import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import org.moonwave.dconfig.dao.springfw.DConfigDataTypeDao;
import org.moonwave.dconfig.model.DConfigDataType;

/**
 *
 * Custom Data Type column cell renderer
 *
 * @author Jonathan Luo
 */
public class DataTypeRenderer extends JComboBox {

    public DataTypeRenderer(JTable table, TableColumn dataTypeColumn) {
        super();
        List list = DConfigDataTypeDao.getDataTypeList();
        for (int i = 0; i < list.size(); i++) {
            DConfigDataType dataType = (DConfigDataType) list.get(i);
            addItem(dataType.getDataTypeName());
        }
        dataTypeColumn.setCellEditor(new DefaultCellEditor(this));
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        dataTypeColumn.setCellRenderer(renderer);
    }
}
