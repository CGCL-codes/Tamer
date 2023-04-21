package org.jbudget.gui.budget.wisard;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import org.jbudget.Core.AutomaticTransaction;
import org.jbudget.Core.Budget;
import org.jbudget.Core.ExpenseAllocation;
import org.jbudget.Core.ExpenseCategory;
import org.jbudget.Core.User;
import org.jbudget.gui.trans.editors.AmountTableCellEditor;
import org.jbudget.gui.trans.editors.AutoTransactionTypeEditor;
import org.jbudget.gui.trans.editors.TextTableCellEditor;
import org.jbudget.gui.trans.editors.UserSetTableCellEditor;
import org.jbudget.gui.trans.renderers.AmountRenderer;
import org.jbudget.gui.trans.renderers.SimpleCellRenderer;
import org.jbudget.gui.trans.renderers.UserListRenderer;

/**
 *
 * @author petrov
 */
public class ExpenseCategoriesPage extends WisardPage {

    /** List of income sources shown on the page. */
    private final List<ExpenseCategoryContainer> expense_categories = new ArrayList<ExpenseCategoryContainer>();

    private final JTable categoriesTable = new JTable(new ExpenseCategoriesTableModel());

    /** Constructor. Creates the GUI components and initializes them with the 
     * default values or the values read from the budget (if they exist). */
    public ExpenseCategoriesPage(Budget budget) {
        super(budget, "Expense Categories");
        mainPane.setLayout(new BorderLayout());
        mainPane.add(categoriesTable.getTableHeader(), BorderLayout.PAGE_START);
        mainPane.add(categoriesTable, BorderLayout.CENTER);
        categoriesTable.setRowHeight(21);
        categoriesTable.setDefaultRenderer(String.class, new SimpleCellRenderer());
        categoriesTable.setDefaultRenderer(Long.class, new AmountRenderer());
        categoriesTable.setDefaultRenderer(List.class, new UserListRenderer());
        categoriesTable.setDefaultRenderer(AutomaticTransaction.Type.class, new SimpleCellRenderer());
        categoriesTable.setDefaultEditor(String.class, new TextTableCellEditor());
        categoriesTable.setDefaultEditor(Long.class, new AmountTableCellEditor());
        categoriesTable.setDefaultEditor(AutomaticTransaction.Type.class, new AutoTransactionTypeEditor());
        categoriesTable.setDefaultEditor(List.class, new UserSetTableCellEditor());
        for (ExpenseCategory category : budget.getAllExpenseCategoriesSorted()) {
            ExpenseCategoryContainer container = new ExpenseCategoryContainer(category);
            List<ExpenseAllocation> allocations = budget.getAllocations(category);
            container.allocated_amount = allocations.get(0).getAmount();
            container.allocation_type = allocations.get(0).getType();
            expense_categories.add(container);
        }
    }

    public void createCategoryEntry() {
        ExpenseCategory category = new ExpenseCategory("Expense");
        ExpenseCategoryContainer container = new ExpenseCategoryContainer(category);
        container.allocated_amount = 0;
        container.allocation_type = AutomaticTransaction.Type.MONTHLY;
        expense_categories.add(container);
        ((AbstractTableModel) categoriesTable.getModel()).fireTableRowsInserted(expense_categories.size() - 1, expense_categories.size() - 1);
    }

    @Override
    public void updateBudget() {
    }

    /** Table model that is used to construct a JTable used to edit
     * expense categories in the budget wisard. */
    private class ExpenseCategoriesTableModel extends AbstractTableModel {

        public int getRowCount() {
            return expense_categories.size();
        }

        public int getColumnCount() {
            return 4;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch(columnIndex) {
                case 0:
                    return "Expense Category";
                case 1:
                    return "Holders";
                case 2:
                    return "Allocated Amount";
                case 3:
                    return "Type";
            }
            throw new RuntimeException("Invalid columnIndex");
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch(columnIndex) {
                case 0:
                    return String.class;
                case 1:
                    return List.class;
                case 2:
                    return Long.class;
                case 3:
                    return AutomaticTransaction.Type.class;
            }
            throw new RuntimeException("Invalid columnIndex");
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            ExpenseCategoryContainer container = expense_categories.get(rowIndex);
            switch(columnIndex) {
                case 0:
                    return container.category.getName();
                case 1:
                    return container.category.getHolders();
                case 2:
                    return new Long(container.allocated_amount);
                case 3:
                    return container.allocation_type;
            }
            throw new RuntimeException("Invalid columnIndex");
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            ExpenseCategoryContainer container = expense_categories.get(rowIndex);
            switch(columnIndex) {
                case 0:
                    container.category.setName((String) aValue);
                    return;
                case 1:
                    container.category.setHolders((List<User>) aValue);
                    return;
                case 2:
                    container.allocated_amount = ((Long) aValue).longValue();
                    return;
                case 3:
                    container.allocation_type = (AutomaticTransaction.Type) aValue;
                    return;
            }
            throw new RuntimeException("Invalid columnIndex");
        }
    }

    /** A container class that holds an expence category object, 
     * the amount that is allocated to that category and the type of the 
     * allocation: Yearly, Monthly, Weekly or Daily.
     */
    private class ExpenseCategoryContainer {

        public final ExpenseCategory category;

        public long allocated_amount;

        public AutomaticTransaction.Type allocation_type;

        ExpenseCategoryContainer(ExpenseCategory category) {
            this.category = category;
            allocated_amount = 0;
            allocation_type = AutomaticTransaction.Type.UNKNOWN;
        }
    }
}
