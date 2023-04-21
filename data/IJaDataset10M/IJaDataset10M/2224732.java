package be.vds.jtbdive.client.view.core.browser;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import be.vds.jtbdive.client.actions.browser.DeleteDivesAction;
import be.vds.jtbdive.client.actions.browser.DuplicateDiveAction;
import be.vds.jtbdive.client.actions.browser.LogBookSortingAction;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.view.core.browser.nodeconstructor.LogBookSorting;
import be.vds.jtbdive.client.view.utils.UIAgent;

public class LogBookBrowserAction {

    public static final String ACTION_COLLAPSE = "collapse";

    public static final String ACTION_EXPAND = "expand";

    public static final String ACTION_DELETE_DIVE = "delete.dive";

    private LogBookBrowserPanel logBookBrowserPanel;

    private List<Action> sortActions = new ArrayList<Action>();

    private Map<String, Action> actions = new HashMap<String, Action>();

    private LogBookManagerFacade logBookManagerFacade;

    public LogBookBrowserAction(LogBookBrowserPanel logBookBrowserPanel, LogBookManagerFacade logBookManagerFacade) {
        this.logBookBrowserPanel = logBookBrowserPanel;
        this.logBookManagerFacade = logBookManagerFacade;
        initActions();
    }

    private void initActions() {
        createSortActions();
        createExpandAction();
        createCollapseAction();
        createDeleteDive();
        createDuplicateDiveAction();
    }

    private void createDuplicateDiveAction() {
        Action action = new DuplicateDiveAction(logBookBrowserPanel, logBookManagerFacade);
        actions.put(DuplicateDiveAction.class.getName(), action);
    }

    private void createDeleteDive() {
        Action action = new DeleteDivesAction(logBookBrowserPanel, logBookManagerFacade);
        actions.put(ACTION_DELETE_DIVE, action);
    }

    private void createExpandAction() {
        Action action = new AbstractAction() {

            private static final long serialVersionUID = -4449495853245548612L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                logBookBrowserPanel.expandTree();
            }
        };
        action.putValue(Action.SMALL_ICON, UIAgent.getInstance().getIcon(UIAgent.ICON_BTN_EXPAND_ALL_16));
        actions.put(ACTION_EXPAND, action);
    }

    private void createCollapseAction() {
        Action action = new AbstractAction() {

            private static final long serialVersionUID = -4435935450216905773L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                logBookBrowserPanel.collapseTree();
            }
        };
        action.putValue(Action.SMALL_ICON, UIAgent.getInstance().getIcon(UIAgent.ICON_BTN_COLLAPSE_ALL_16));
        actions.put(ACTION_COLLAPSE, action);
    }

    private void createSortActions() {
        for (LogBookSorting sort : LogBookSorting.values()) {
            Action action = new LogBookSortingAction(sort, logBookBrowserPanel);
            sortActions.add(action);
        }
    }

    public List<Action> getSortActions() {
        return sortActions;
    }

    public Action getAction(String action) {
        return actions.get(action);
    }
}
