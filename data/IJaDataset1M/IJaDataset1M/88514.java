package org.nightlabs.dionysos.rap;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class DionysosActionBuilder extends ActionBarAdvisor {

    public DionysosActionBuilder(IActionBarConfigurer arg0) {
        super(arg0);
    }

    public static final String ID_MENU_PERSPECTIVE = "menuPerspective";

    public static final String ID_MENU_VIEWS = "menuViews";

    private ActionFactory.IWorkbenchAction quitAction;

    private IContributionItem showViewMenu;

    private ActionFactory.IWorkbenchAction aboutAction;

    /**
	 * @see org.eclipse.ui.application.ActionBarAdvisor#makeActions(org.eclipse.ui.IWorkbenchWindow)
	 */
    protected void makeActions(IWorkbenchWindow window) {
        quitAction = ActionFactory.QUIT.create(window);
        showViewMenu = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
        aboutAction = ActionFactory.ABOUT.create(window);
    }

    /**
	 * @see org.eclipse.ui.application.ActionBarAdvisor#fillMenuBar(org.eclipse.jface.action.IMenuManager)
	 */
    public void fillMenuBar(IMenuManager menuBar) {
        IMenuManager fileMenu = new MenuManager(DionysosPlugin.getResourceString("menu.file.label"), IWorkbenchActionConstants.M_FILE);
        menuBar.add(fileMenu);
        fileMenu.add(quitAction);
        fileMenu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
        IMenuManager windowMenu = new MenuManager(DionysosPlugin.getResourceString("menu.window.label"), IWorkbenchActionConstants.M_WINDOW);
        menuBar.add(windowMenu);
        MenuManager showViewMenuMgr = new MenuManager(DionysosPlugin.getResourceString("menu.showView.label"), ID_MENU_VIEWS);
        showViewMenuMgr.add(showViewMenu);
        windowMenu.add(showViewMenuMgr);
        windowMenu.add(new Separator());
        IMenuManager helpMenu = new MenuManager(DionysosPlugin.getResourceString("menu.help.label"), IWorkbenchActionConstants.M_HELP);
        menuBar.add(helpMenu);
        helpMenu.add(aboutAction);
    }

    public void dispose() {
        quitAction.dispose();
        aboutAction.dispose();
        showViewMenu.dispose();
    }
}
