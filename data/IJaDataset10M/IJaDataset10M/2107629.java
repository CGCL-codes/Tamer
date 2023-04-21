package tailmaster.gui.listener;

import tailmaster.dao.LogFileDao;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * User: Halil KARAKOSE
 * Date: Jan 16, 2009
 * Time: 1:13:47 PM
 */
public class DeleteLogFileConfigurationListener implements ActionListener {

    private JTable table;

    public DeleteLogFileConfigurationListener(JTable serverTable) {
        this.table = serverTable;
    }

    public void actionPerformed(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        String logFileId = (String) table.getModel().getValueAt(selectedRow, 0);
        LogFileDao dao = LogFileDao.getInstance();
        try {
            dao.delete(Integer.parseInt(logFileId));
            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            tableModel.removeRow(selectedRow);
        } catch (SQLException e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(table.getRootPane(), "Unable to delete log file data", "Error", JOptionPane.ERROR);
        }
    }
}
