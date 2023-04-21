package gui.tabs;

import gui.main.DHTDataTableModel;
import gui.main.DHTNodeTableModel;
import javax.swing.*;
import java.awt.*;

/**
 * todo write javadoc
 */
public final class LocalDataTab {

    public static JComponent createLocalDataTab(DHTDataTableModel dataTableModel) {
        JComponent component = new JComponent() {
        };
        component.setLayout(new BorderLayout());
        JTable dataTable = new JTable(dataTableModel);
        dataTable.getColumnModel().getColumn(2).setCellRenderer(new DHTNodeTableModel.DateRenderer());
        component.add(BorderLayout.CENTER, new JScrollPane(dataTable));
        component.setVisible(true);
        return component;
    }
}
