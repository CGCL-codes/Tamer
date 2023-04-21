package org.jbudget.gui.budget;

import java.util.List;
import javax.swing.AbstractListModel;
import org.jbudget.Core.Budget;
import org.jbudget.io.DataManager;
import org.jbudget.util.Pair;

/**
 * A list model for selecting budget.
 *
 * @author petrov
 */
public class BudgetSelectorModel extends AbstractListModel {

    /** if true only unused budgets are shown */
    private final boolean showUnused;

    private List<Pair<Integer, String>> availableBudgets;

    private DataManager.BudgetListener listener = new DataManager.BudgetListener() {

        public void budgetAdded(Budget budget) {
            initialize();
            int budgetID = budget.getID();
            int i = 0;
            for (Pair<Integer, String> budgetInfo : availableBudgets) {
                if (budgetInfo.first().intValue() == budgetID) {
                    fireIntervalAdded(this, i, i);
                    break;
                }
                i++;
            }
        }

        public void budgetRemoved(int budgetID) {
            int i = 0;
            for (Pair<Integer, String> budgetInfo : availableBudgets) {
                if (budgetInfo.first().intValue() == budgetID) break;
                i++;
            }
            if (i == availableBudgets.size()) return;
            initialize();
            fireIntervalRemoved(this, i, i);
        }
    };

    /** Guard variable */
    private boolean wasDisposed = false;

    /** Creates a new instance of BudgetSelectorModel */
    public BudgetSelectorModel(boolean showUnused) {
        DataManager.getInstance().addBudgetListener(listener);
        this.showUnused = showUnused;
        initialize();
    }

    /** Frees resources */
    public void dispose() {
        DataManager.getInstance().removeBudgetListener(listener);
        wasDisposed = true;
    }

    public void finalize() {
        if (!wasDisposed) {
            System.err.println("Dispose was not called for BudgetSelectorModel");
            dispose();
        }
    }

    /** Gets a list of available budgets from the DataManager */
    private void initialize() {
        if (showUnused) availableBudgets = DataManager.getInstance().getUnusedBudgets(); else availableBudgets = DataManager.getInstance().getAllBudgetIDs();
    }

    /** Returns the element at the appropriate location */
    public Object getElementAt(int i) {
        return availableBudgets.get(i);
    }

    /** Returns the number of available budgets */
    public int getSize() {
        return availableBudgets.size();
    }
}
