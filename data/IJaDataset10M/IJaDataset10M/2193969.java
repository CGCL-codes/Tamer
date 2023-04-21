package iwallet.client.gui;

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

    private Action exportAllAction;

    private Action genGraphAction;

    private Action updateXchgAction;

    private IWorkbenchAction preferencesAction;

    private SearchAction searchAction;

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(final IWorkbenchWindow window) {
        exitAction = ActionFactory.QUIT.create(window);
        register(exitAction);
        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);
        exportAllAction = new ExportAllAction("��ɱ���...", window);
        register(exportAllAction);
        genGraphAction = new GenGraphAction("��ʾͳ��ͼ...", window);
        register(genGraphAction);
        updateXchgAction = new UpdateXchgAction("���»��ʱ�...", window);
        register(updateXchgAction);
        preferencesAction = ActionFactory.PREFERENCES.create(window);
        register(preferencesAction);
        searchAction = new SearchAction("����", window);
        register(searchAction);
    }

    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
        MenuManager iwalletMenu = new MenuManager("&iWallet", "iwallet");
        MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
        menuBar.add(fileMenu);
        menuBar.add(iwalletMenu);
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menuBar.add(helpMenu);
        iwalletMenu.add(exportAllAction);
        iwalletMenu.add(genGraphAction);
        iwalletMenu.add(updateXchgAction);
        iwalletMenu.add(new Separator());
        iwalletMenu.add(preferencesAction);
        helpMenu.add(aboutAction);
    }

    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, "main"));
        toolbar.add(exportAllAction);
        toolbar.add(genGraphAction);
        toolbar.add(updateXchgAction);
        toolbar.add(searchAction);
    }
}
