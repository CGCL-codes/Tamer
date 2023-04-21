package gp.utils.swing.filechooser.common;

import gp.utils.swing.filechooser.places.PlacesSeparator;
import java.awt.Component;
import java.io.File;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author gpasquiers
 */
public class FileCellRenderer implements TableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        try {
            if (value instanceof PlacesSeparator) {
                if (table.getRowHeight(row) != 2) table.setRowHeight(row, 2);
                return new FileCellRendererSeparatorView();
            } else if (value instanceof File) {
                FileCellRendererFileView view = new FileCellRendererFileView();
                File file = (File) value;
                view.jLabelText.setText(FormatUtils.formatFileName(file));
                Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);
                view.jLabelIcon.setIcon(icon);
                if (isSelected) {
                    view.setBackground(UIManager.getColor("Table.selectionBackground"));
                } else {
                    view.setBackground(UIManager.getColor("Table.background"));
                }
                if (table.getRowHeight(row) != 22) table.setRowHeight(row, 22);
                return view;
            }
            return new JLabel("unhandled type of value");
        } catch (Exception e) {
            return new JLabel("error");
        }
    }
}
