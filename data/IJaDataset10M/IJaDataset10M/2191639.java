package com.umc.gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import com.umc.gui.tablemodels.ScanTableModel;

/**
 *
 * @author Martin Rutschmann
 */
public class ScanTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        ScanTableModel scantablemodel = (ScanTableModel) table.getModel();
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.umc.gui.GUI.class).getContext().getResourceMap(UMCView.class);
        if (isSelected) {
            label.setBackground(Color.BLUE);
            label.setForeground(Color.WHITE);
        } else {
            label.setForeground(Color.BLACK);
            switch((int) scantablemodel.getDir(row).getID_Scan_Type()) {
                case 0:
                    label.setBackground(resourceMap.getColor("scanLabelLegendDirTyp0.background"));
                    break;
                case 1:
                    label.setBackground(resourceMap.getColor("scanLabelLegendDirTyp1.background"));
                    break;
                case 2:
                    label.setBackground(resourceMap.getColor("scanLabelLegendDirTyp2.background"));
                    break;
                case 3:
                    label.setBackground(resourceMap.getColor("scanLabelLegendDirTyp3.background"));
                    break;
                case 4:
                    label.setBackground(resourceMap.getColor("scanLabelLegendDirTyp4.background"));
                    break;
                default:
                    label.setBackground(resourceMap.getColor("scanLabelLegendDirTyp5.background"));
                    break;
            }
        }
        return label;
    }
}
