package org.hironico.gui.table.renderer;

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import org.jdesktop.swingx.JXFrame;
import org.pushingpixels.substance.api.skin.SubstanceOfficeSilver2007LookAndFeel;

public class BooleanCellRenderer extends JCheckBox implements TableCellRenderer {

    public BooleanCellRenderer() {
        super();
        setText("");
        setHorizontalAlignment(CENTER);
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setSelected(((Boolean) value).booleanValue());
        setFocusPainted(hasFocus);
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
        return this;
    }

    public static void main(String[] args) {
        Runnable myrun = new Runnable() {

            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(new SubstanceOfficeSilver2007LookAndFeel());
                    JXFrame frame = new JXFrame("Test boolean cell renderer...");
                    JTable table = new JTable();
                    JScrollPane scroll = new JScrollPane(table);
                    DefaultTableModel model = new DefaultTableModel();
                    model.setColumnCount(2);
                    Boolean mybool = Boolean.FALSE;
                    for (int cpt = 0; cpt < 15; cpt++) {
                        Object row[] = new Object[2];
                        mybool = !mybool;
                        row[0] = mybool;
                        row[1] = mybool;
                        model.addRow(row);
                    }
                    table.setModel(model);
                    table.getColumnModel().getColumn(0).setCellRenderer(new BooleanCellRenderer());
                    frame.getContentPane().add(scroll);
                    frame.setSize(1024, 768);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        SwingUtilities.invokeLater(myrun);
    }
}
