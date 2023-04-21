package jgnash.ui.actions;

import java.awt.event.ActionEvent;
import jgnash.ui.budget.BudgetManagerDialog;
import jgnash.ui.util.builder.Action;

/**
 * UI Action to open the budget manager dialog
 *
 * @author Craig Cavanaugh
 * @version $Id: BudgetManagerAction.java 3051 2012-01-02 11:27:23Z ccavanaugh $
 */
@Action("budget-manager-command")
public class BudgetManagerAction extends AbstractEnabledAction {

    private static final long serialVersionUID = 0L;

    @Override
    public void actionPerformed(ActionEvent e) {
        BudgetManagerDialog.showDialog();
    }
}
