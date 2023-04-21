package net.sf.snvergui;

import net.sf.snvergui.action.MessagePopupAction;
import net.sf.snvergui.action.OpenLoadDataViewAction;
import net.sf.snvergui.action.OpenMoreOptionViewAction;
import net.sf.snvergui.action.OpenResultsViewAction;
import net.sf.snvergui.action.OpenRunSNVerViewAction;
import net.sf.snvergui.view.LoadDataView;
import net.sf.snvergui.view.MoreOptionView;
import net.sf.snvergui.view.ResultView;
import net.sf.snvergui.view.RunSNVerView;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of the
 * actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    private IWorkbenchAction exitAction;

    private IWorkbenchAction aboutAction;

    private OpenLoadDataViewAction openLoadDataViewAction;

    private OpenMoreOptionViewAction openMoreOptionViewAction;

    private OpenResultsViewAction openResultsViewAction;

    private OpenRunSNVerViewAction openRunSNVerViewAction;

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(final IWorkbenchWindow window) {
        exitAction = ActionFactory.QUIT.create(window);
        register(exitAction);
        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);
        openLoadDataViewAction = new OpenLoadDataViewAction(window, "Open Another Message View", LoadDataView.ID);
        register(openLoadDataViewAction);
        openMoreOptionViewAction = new OpenMoreOptionViewAction(window, "Open Another Message View", MoreOptionView.ID);
        register(openMoreOptionViewAction);
        openResultsViewAction = new OpenResultsViewAction(window, "Open Another Message View", ResultView.ID);
        register(openResultsViewAction);
        openRunSNVerViewAction = new OpenRunSNVerViewAction(window, "Open Another Message View", RunSNVerView.ID);
        register(openRunSNVerViewAction);
    }

    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
        MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
        menuBar.add(fileMenu);
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menuBar.add(helpMenu);
        fileMenu.add(exitAction);
        helpMenu.add(aboutAction);
    }
}
