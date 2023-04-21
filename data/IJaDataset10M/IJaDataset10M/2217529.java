package be.vds.jtbtaskplanner.client.view.core;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToolBar;
import be.smd.i18n.swing.I18nButton;
import be.vds.jtbtaskplanner.client.actions.ActionType;
import be.vds.jtbtaskplanner.client.actions.PlanningApplActionsContoller;

public class LogBookToolBar extends JToolBar {

    private static final long serialVersionUID = -2813336239605362535L;

    private PlanningApplActionsContoller logbookActions;

    public LogBookToolBar(PlanningApplActionsContoller logbookActions) {
        super("Drag me back...");
        this.logbookActions = logbookActions;
        init();
    }

    private void init() {
    }

    private JButton createToolBarButton(Action action, String toolTipKey) {
        I18nButton b = new I18nButton(action);
        b.setTooltipTextBundleKey(toolTipKey);
        b.setToolTipText(null);
        b.setText(null);
        b.setTextBundleKey(null);
        b.setFocusable(false);
        return b;
    }
}
