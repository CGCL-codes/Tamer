package org.yccheok.jstock.gui.portfolio;

import javax.swing.table.AbstractTableModel;
import org.yccheok.jstock.engine.SimpleDate;
import org.yccheok.jstock.internationalization.GUIBundle;
import org.yccheok.jstock.portfolio.Commentable;
import org.yccheok.jstock.portfolio.Deposit;
import org.yccheok.jstock.portfolio.DepositSummary;

/**
 *
 * @author yccheok
 */
public class DepositSummaryTableModel extends AbstractTableModel implements CommentableContainer {

    public DepositSummaryTableModel(DepositSummary depositSummary) {
        this.depositSummary = depositSummary;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final Deposit deposit = depositSummary.get(rowIndex);
        switch(columnIndex) {
            case 0:
                return deposit.getDate().getCalendar().getTime();
            case 1:
                return deposit.getAmount();
        }
        return null;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return depositSummary.size();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Class getColumnClass(int c) {
        return columnClasses[c];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        switch(col) {
            case 0:
                {
                    Deposit newDeposit = depositSummary.get(row).setDate(new SimpleDate((java.util.Date) value));
                    depositSummary.remove(row);
                    depositSummary.add(row, newDeposit);
                    fireTableCellUpdated(row, col);
                    break;
                }
            case 1:
                {
                    Deposit newDeposit = depositSummary.get(row).setAmount((Double) value);
                    depositSummary.remove(row);
                    depositSummary.add(row, newDeposit);
                    fireTableCellUpdated(row, col);
                    break;
                }
        }
    }

    public void removeRow(int index) {
        depositSummary.remove(index);
        this.fireTableRowsDeleted(index, index);
    }

    public int addNewDeposit() {
        depositSummary.add(new Deposit(0.0, new SimpleDate()));
        final int index = depositSummary.size() - 1;
        this.fireTableRowsInserted(index, index);
        return index;
    }

    public Deposit getDeposit(int index) {
        return depositSummary.get(index);
    }

    @Override
    public Commentable getCommentable(int index) {
        return depositSummary.get(index);
    }

    private static final String[] columnNames;

    private static final Class[] columnClasses = { java.util.Date.class, Double.class };

    private final DepositSummary depositSummary;

    static {
        final String[] tmp = { GUIBundle.getString("PortfolioManagementJPanel_Date"), GUIBundle.getString("PortfolioManagementJPanel_Cash") };
        columnNames = tmp;
    }
}
