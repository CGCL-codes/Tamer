package com.empower.client.controller;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import com.empower.client.tablemodels.PSViewTableModel;
import com.empower.client.tablemodels.UPSViewTableModel;
import com.empower.model.PSCnsgnmntDtlsModel;
import com.empower.utils.ECSConstants;

public class CMSTableCellRenderer extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
        super.getTableCellRendererComponent(table, value, selected, focused, row, column);
        PSCnsgnmntDtlsModel model = null;
        if (PSViewTableModel.class.isInstance(table.getModel())) {
            PSViewTableModel tableModel = (PSViewTableModel) table.getModel();
            model = (PSCnsgnmntDtlsModel) tableModel.getDataRow(row).get(0);
        }
        if (UPSViewTableModel.class.isInstance(table.getModel())) {
            UPSViewTableModel tableModel = (UPSViewTableModel) table.getModel();
            model = (PSCnsgnmntDtlsModel) tableModel.getDataRow(row).get(0);
        }
        if (model.getUnPckngSlipNbr().equals(ECSConstants.EMPSTR)) {
            setBackground(new Color(250, 128, 114));
            setToolTipText("Not unpacked at the destination");
        } else {
            setBackground(new Color(204, 255, 153));
            setToolTipText("Unpacked at the destination");
        }
        if (value instanceof Double | value instanceof Integer | value instanceof Long) {
            setHorizontalAlignment(SwingConstants.TRAILING);
        }
        return this;
    }
}
