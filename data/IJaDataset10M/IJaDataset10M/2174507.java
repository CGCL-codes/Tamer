package imtek.optsuite.base.application;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * Creates the Menu 
 * @author Alexander Bieber <alex[AT]nightlabs[DOT]de>
 *
 */
public class OptSuiteActionBuilder extends ActionBarAdvisor {

    private MenuManager newMenu;

    private ActionFactory.IWorkbenchAction newAction;

    private ActionFactory.IWorkbenchAction quitAction;

    private ActionFactory.IWorkbenchAction helpAction;

    private ActionFactory.IWorkbenchAction aboutAction;

    private IContributionItem openPerspectiveMenu;

    private IContributionItem showViewMenu;

    private ActionFactory.IWorkbenchAction preferencesAction;

    private IActionBarConfigurer configurer;

    public OptSuiteActionBuilder(IActionBarConfigurer configurer) {
        super(configurer);
        this.configurer = configurer;
    }

    /**
	 * @see org.eclipse.ui.application.ActionBarAdvisor#fillActionBars(int)
	 */
    public void fillActionBars(int flags) {
        if ((flags & ActionBarAdvisor.FILL_PROXY) == 0) {
            makeActions();
        }
        if ((flags & ActionBarAdvisor.FILL_MENU_BAR) != 0) {
            fillMenuBar(configurer.getMenuManager());
        }
        if ((flags & ActionBarAdvisor.FILL_COOL_BAR) != 0) {
            fillCoolBar(configurer.getCoolBarManager());
        }
    }

    private void makeActions() {
        newMenu = new MenuManager("New", ActionFactory.NEW.getId());
        newMenu.add((ActionFactory.NEW.create(configurer.getWindowConfigurer().getWindow())));
        newMenu.add(new GroupMarker(ActionFactory.NEW.getId()));
        newAction = ActionFactory.NEW.create(configurer.getWindowConfigurer().getWindow());
        quitAction = ActionFactory.QUIT.create(configurer.getWindowConfigurer().getWindow());
        openPerspectiveMenu = ContributionItemFactory.PERSPECTIVES_SHORTLIST.create(configurer.getWindowConfigurer().getWindow());
        showViewMenu = ContributionItemFactory.VIEWS_SHORTLIST.create(configurer.getWindowConfigurer().getWindow());
        preferencesAction = ActionFactory.PREFERENCES.create(configurer.getWindowConfigurer().getWindow());
        helpAction = ActionFactory.HELP_CONTENTS.create(configurer.getWindowConfigurer().getWindow());
        aboutAction = ActionFactory.ABOUT.create(configurer.getWindowConfigurer().getWindow());
    }

    public void fillMenuBar(IMenuManager menuBar) {
        IMenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
        menuBar.add(fileMenu);
        fileMenu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
        fileMenu.add(newMenu);
        fileMenu.add(new Separator());
        fileMenu.add(new GroupMarker("import.export"));
        fileMenu.add(new Separator());
        fileMenu.add(new GroupMarker(IWorkbenchActionConstants.IMPORT_EXT));
        fileMenu.add(new Separator());
        fileMenu.add(quitAction);
        fileMenu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        IMenuManager windowMenu = new MenuManager("&Window", IWorkbenchActionConstants.M_WINDOW);
        menuBar.add(windowMenu);
        MenuManager openPerspectiveMenuMgr = new MenuManager("Open Perspective", "openPerspective");
        openPerspectiveMenuMgr.add(openPerspectiveMenu);
        windowMenu.add(openPerspectiveMenuMgr);
        MenuManager showViewMenuMgr = new MenuManager("Show View", "showView");
        showViewMenuMgr.add(showViewMenu);
        windowMenu.add(showViewMenuMgr);
        windowMenu.add(new Separator());
        windowMenu.add(preferencesAction);
        IMenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
        menuBar.add(helpMenu);
        helpMenu.add(helpAction);
        helpMenu.add(new Separator());
        helpMenu.add(aboutAction);
    }

    public void fillCoolBar(ICoolBarManager coolBar) {
    }

    public void dispose() {
        aboutAction.dispose();
        helpAction.dispose();
        newAction.dispose();
        preferencesAction.dispose();
        quitAction.dispose();
    }
}
