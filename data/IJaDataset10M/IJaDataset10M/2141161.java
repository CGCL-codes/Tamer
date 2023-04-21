package com.googlecode.pondskum.gui.swing.tablet;

import com.googlecode.pondskum.client.BigpondMonthlyUsage;
import com.googlecode.pondskum.client.BigpondUsage;
import com.googlecode.pondskum.client.BigpondUsageInformation;
import com.googlecode.pondskum.util.UsageConverter;
import javax.swing.table.AbstractTableModel;

public class BigpondTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 550659947871203828L;

    private static final int DOWNLOAD_USAGE = 1;

    private static final int UPLOAD_USAGE = 2;

    private static final int UMETERED_USAGE = 3;

    private static final int TOTAL_USAGE = 4;

    private final BigpondUsageInformation bigpondUsageInformation;

    private UsageConverter usageConverter;

    private final String[] columnNames;

    public BigpondTableModel(final BigpondUsageInformation bigpondUsageInformation, final UsageConverter usageConverter) {
        this.bigpondUsageInformation = bigpondUsageInformation;
        this.usageConverter = usageConverter;
        columnNames = new String[] { "Month", "Download", "Upload", "Unmetered", "Total" };
    }

    @Override
    public Object getValueAt(final int row, final int column) {
        BigpondMonthlyUsage usage = bigpondUsageInformation.getMonthlyUsageList().get(row);
        return (column == 0) ? usage.getMonth() : getUsageValue(column, usage.getBigpondUsage());
    }

    private Object getUsageValue(final int column, final BigpondUsage usage) {
        String usageValue = "-1";
        switch(column) {
            case DOWNLOAD_USAGE:
                usageValue = usage.getDownloadUsage();
                break;
            case UPLOAD_USAGE:
                usageValue = usage.getUploadUsage();
                break;
            case UMETERED_USAGE:
                usageValue = usage.getUnmeteredUsage();
                break;
            case TOTAL_USAGE:
                usageValue = usage.getTotalUsage();
                break;
            default:
                break;
        }
        return usageConverter.toUsageTableValue(usageValue);
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(final int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        return columnIndex == 0 ? String.class : UsageTableValue.class;
    }

    @Override
    public int getRowCount() {
        return bigpondUsageInformation.getMonthlyUsageList().size();
    }
}
