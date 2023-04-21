package au.com.kelpie.fgfp;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import au.com.kelpie.fgfp.action.AutoPlanToggle;
import au.com.kelpie.fgfp.action.NewPlan;
import au.com.kelpie.fgfp.action.OpenPlan;
import au.com.kelpie.fgfp.action.ReloadAirports;
import au.com.kelpie.fgfp.action.ReloadNavaids;
import au.com.kelpie.fgfp.action.ViewMap;

public class FGFPActionBarAdvisor extends ActionBarAdvisor {

    private IAction _newPlan;

    private IAction _openPlan;

    private IAction _close;

    private IAction _closeAll;

    private IAction _save;

    private IAction _saveAs;

    private IAction _saveAll;

    private IAction _print;

    private IAction _quit;

    private IAction _helpAbout;

    private IAction _helpContents;

    private IAction _helpIntro;

    private IAction _reloadNavaids;

    private IAction _reloadAirports;

    private IAction _autoPlanToggle;

    private IAction _lockToolbar;

    private IAction _viewMap;

    private IAction _perspective;

    private IAction _preferences;

    public FGFPActionBarAdvisor(final IActionBarConfigurer configurer) {
        super(configurer);
        createActions();
    }

    void createActions() {
        final IWorkbenchWindow window = getActionBarConfigurer().getWindowConfigurer().getWindow();
        _newPlan = new NewPlan();
        _openPlan = new OpenPlan();
        _close = ActionFactory.CLOSE.create(window);
        _closeAll = ActionFactory.CLOSE_ALL.create(window);
        _save = ActionFactory.SAVE.create(window);
        _saveAs = ActionFactory.SAVE_AS.create(window);
        _saveAll = ActionFactory.SAVE_ALL.create(window);
        _print = ActionFactory.PRINT.create(window);
        _quit = ActionFactory.QUIT.create(window);
        _autoPlanToggle = new AutoPlanToggle();
        _reloadAirports = new ReloadAirports();
        _reloadNavaids = new ReloadNavaids();
        _lockToolbar = ActionFactory.LOCK_TOOL_BAR.create(window);
        _lockToolbar.setEnabled(true);
        _viewMap = new ViewMap();
        _perspective = ActionFactory.RESET_PERSPECTIVE.create(window);
        _perspective.setEnabled(true);
        _preferences = ActionFactory.PREFERENCES.create(window);
        _helpContents = ActionFactory.HELP_CONTENTS.create(window);
        _helpIntro = ActionFactory.INTRO.create(window);
        _helpAbout = ActionFactory.ABOUT.create(window);
    }

    @Override
    public void fillActionBars(final int flags) {
        super.fillActionBars(flags);
        if ((flags & FILL_MENU_BAR) != 0) {
            fillMenuBar();
        }
        if ((flags & FILL_COOL_BAR) != 0) {
            fillCoolBar();
        }
    }

    /**
     * @param window
     * @param configurer
     */
    private void fillCoolBar() {
        final ICoolBarManager coolBar = getActionBarConfigurer().getCoolBarManager();
        coolBar.add(createFileBar());
        coolBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    /**
     * @param window
     * @param configurer
     */
    private void fillMenuBar() {
        final IMenuManager menuBar = getActionBarConfigurer().getMenuManager();
        menuBar.add(createFileMenu());
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menuBar.add(createToolMenu());
        menuBar.add(createWindowMenu());
        menuBar.add(createHelpMenu());
    }

    /**
     * @param window
     * @return
     */
    private IToolBarManager createFileBar() {
        final ToolBarManager bar = new ToolBarManager(SWT.FLAT);
        bar.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
        bar.add(_newPlan);
        bar.add(_openPlan);
        bar.add(_save);
        bar.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
        bar.add(new Separator());
        bar.add(_print);
        bar.add(new Separator());
        bar.add(_autoPlanToggle);
        bar.add(new Separator());
        bar.add(_viewMap);
        bar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        return bar;
    }

    /**
     * @param window
     * @return
     */
    private MenuManager createFileMenu() {
        final MenuManager menu = new MenuManager(Messages.getString("FGFPActionBarAdvisor.0"), IWorkbenchActionConstants.M_FILE);
        menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
        menu.add(_newPlan);
        menu.add(_openPlan);
        menu.add(_close);
        menu.add(_closeAll);
        menu.add(_save);
        menu.add(_saveAs);
        menu.add(_saveAll);
        menu.add(new Separator());
        menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
        menu.add(new Separator());
        menu.add(_print);
        menu.add(new Separator());
        menu.add(_quit);
        menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        return menu;
    }

    /**
     * @param window
     * @return
     */
    private MenuManager createHelpMenu() {
        final MenuManager menu = new MenuManager(Messages.getString("FGFPActionBarAdvisor.1"), IWorkbenchActionConstants.M_HELP);
        menu.add(new GroupMarker(IWorkbenchActionConstants.HELP_START));
        menu.add(_helpContents);
        menu.add(_helpIntro);
        menu.add(new GroupMarker(IWorkbenchActionConstants.GROUP_HELP));
        menu.add(new GroupMarker("group.tutorials"));
        menu.add(new Separator());
        menu.add(_helpAbout);
        menu.add(new GroupMarker(IWorkbenchActionConstants.HELP_END));
        menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        return menu;
    }

    /**
     * @param window
     * @return
     */
    private MenuManager createToolMenu() {
        final MenuManager menu = new MenuManager(Messages.getString("FGFPActionBarAdvisor.3"), "Tool");
        menu.add(_autoPlanToggle);
        menu.add(new Separator());
        menu.add(_reloadAirports);
        menu.add(_reloadNavaids);
        menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        return menu;
    }

    /**
     * @param window
     * @return
     */
    private MenuManager createWindowMenu() {
        final MenuManager menu = new MenuManager(Messages.getString("FGFPActionBarAdvisor.5"), IWorkbenchActionConstants.M_WINDOW);
        menu.add(_lockToolbar);
        menu.add(new Separator());
        menu.add(_viewMap);
        menu.add(_perspective);
        menu.add(new Separator());
        menu.add(_preferences);
        menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        return menu;
    }

    @Override
    protected IAction getAction(final String id) {
        return super.getAction(id);
    }

    public IAction getAutoPlanToggle() {
        return _autoPlanToggle;
    }

    public void setAutoPlanToggle(final IAction autoPlanToggle) {
        _autoPlanToggle = autoPlanToggle;
    }
}
