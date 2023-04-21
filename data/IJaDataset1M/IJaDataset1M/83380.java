package jgnash.report.compiled;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import javax.swing.table.AbstractTableModel;
import jgnash.engine.commodity.CommodityNode;
import jgnash.report.ReportTableModel;
import jgnash.ui.util.UIResource;

public class PortfolioReportTableModel extends AbstractTableModel implements ReportTableModel {

    private UIResource rb = (UIResource) UIResource.get();

    Set keys;

    Map mapOfCommodities;

    boolean verbose;

    public PortfolioReportTableModel(Map mapOfCommodities) {
        this.mapOfCommodities = mapOfCommodities;
        keys = mapOfCommodities.keySet();
        verbose = true;
    }

    public int getColumnCount() {
        return 10;
    }

    public int getRowCount() {
        return mapOfCommodities.keySet().size();
    }

    public Object getValueAt(int row, int col) {
        Object cns[] = keys.toArray();
        CommodityNode cn = (CommodityNode) cns[row];
        SecurityTransactionHelper sth = (SecurityTransactionHelper) mapOfCommodities.get(cn);
        switch(col) {
            case 0:
                if (verbose) {
                    return cn.getDescription();
                }
                return cn.getSymbol();
            case 1:
                return "" + sth.getCurrentQuantity();
            case 2:
                return cn.getMarketPrice().setScale(2, BigDecimal.ROUND_HALF_EVEN);
            case 3:
                return cn.getMarketPrice().multiply(sth.getCurrentQuantity()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            case 4:
                return sth.getUnRealizedGain(cn.getMarketPrice()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            case 5:
                return sth.getUnrealizedGainsPercentage(cn.getMarketPrice()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            case 6:
                return sth.getRealizedGains().setScale(2, BigDecimal.ROUND_HALF_EVEN);
            case 7:
                return sth.getRealizedGainsPercentage().setScale(2, BigDecimal.ROUND_HALF_EVEN);
            case 8:
                return sth.getTotalGains(cn.getMarketPrice()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            case 9:
                return sth.getTotalGainsPercentage(cn.getMarketPrice()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            default:
                return "ERR";
        }
    }

    public String getColumnName(int col) {
        switch(col) {
            case 0:
                return rb.getString("Column.Security");
            case 1:
                return rb.getString("Column.Short.Quantity");
            case 2:
                return rb.getString("Column.Price");
            case 3:
                return rb.getString("Column.Value");
            case 4:
                return rb.getString("Column.Short.UnrealizedGain");
            case 5:
                return rb.getString("Column.Short.UnrealizedGainPercentage");
            case 6:
                return rb.getString("Column.Short.RealizedGain");
            case 7:
                return rb.getString("Column.Short.RealizedGainPercentage");
            case 8:
                return rb.getString("Column.Short.TotalGain");
            case 9:
                return rb.getString("Column.Short.TotalGainPercentage");
            default:
                return "ERR";
        }
    }

    public boolean isColumnTotalDisplayed(int col) {
        switch(col) {
            case 0:
                return false;
            case 1:
                return false;
            case 2:
                return false;
            case 3:
                return true;
            case 4:
                return true;
            case 5:
                return false;
            case 6:
                return true;
            case 7:
                return false;
            case 8:
                return true;
            case 9:
                return false;
            case 10:
                return false;
            default:
                return false;
        }
    }

    public int[] getPreferredColumnWeightsForTotal() {
        return new int[] { 20, 20, 20, 20, 20 };
    }

    public int[] getPreferredColumnWeights() {
        return new int[] { 14, 6, 10, 12, 11, 8, 12, 8, 12, 7 };
    }

    public Class getColumnClass(int col) {
        if (col >= 4 && col <= 9) {
            return BigDecimal.class;
        }
        return String.class;
    }

    public Object getColumnPrototypeValueAt(int columIndex) {
        return null;
    }

    public int getDataEndColumn() {
        return getColumnCount() - 1;
    }

    public int getDataStartColumn() {
        return 0;
    }

    public int getGroupCount() {
        return 0;
    }

    public int getGroupEndColumn() {
        return 0;
    }

    public String getGroupNameAt(int rowIndex, int depth) {
        return null;
    }

    public int getGroupStartColumn() {
        return 0;
    }
}
