package org.eclipse.plugin.worldwind;

import java.util.ArrayList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.views.IViewDescriptor;
import org.eclipse.ui.views.IViewRegistry;
import org.eclipse.plugin.worldwind.actions.ICommandIds;
import org.eclipse.plugin.worldwind.actions.OpenFileAction;
import org.eclipse.plugin.worldwind.actions.OpenViewAction;
import org.eclipse.plugin.worldwind.actions.ShowPerspectiveAction;
import org.eclipse.plugin.worldwind.actions.WeatherWizardAction;
import org.eclipse.plugin.worldwind.actions.OpenViewAction.VIEW_TYPE;
import org.eclipse.plugin.worldwind.operation.UpdatesCheckJob;
import org.eclipse.plugin.worldwind.utils.CacheManagerDialog;
import org.eclipse.plugin.worldwind.utils.StatusLine;
import org.eclipse.plugin.worldwind.views.NetCDFView;
import org.eclipse.plugin.worldwind.views.WebBrowserView;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    private OpenFileAction openFileAction;

    private WeatherWizardAction weatherWizardAction;

    private OpenViewAction openWebBrowser;

    private OpenViewAction openDataSet;

    private IWorkbenchAction exitAction;

    private Action cacheManagerAction;

    private Action updatesCheckAction;

    private IWorkbenchAction aboutAction;

    private IWorkbenchAction showHelpAction;

    private IWorkbenchAction searchHelpAction;

    private IContributionItem showViewsItem;

    /** List of Open perspective Actions (Perspective menu) */
    private ArrayList<Action> perspectiveActions = new ArrayList<Action>();

    /** List of Open Views Actions (Perspective menu) */
    private ArrayList<Action> openViewActions = new ArrayList<Action>();

    private static StatusLine statusLine;

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(final IWorkbenchWindow window) {
        openFileAction = new OpenFileAction(Messages.getString("file.open.tooltip"), window);
        register(openFileAction);
        weatherWizardAction = new WeatherWizardAction(Messages.getString("weather.wiz.tooltip"), window);
        register(weatherWizardAction);
        openWebBrowser = new OpenViewAction(Messages.getString("open.web.browset.tooltip"), window, WebBrowserView.ID, true, VIEW_TYPE.WEB_BROWSER);
        openWebBrowser.initMenu(ICommandIds.CMD_OPEN_WEB_BROWSER, Activator.ICON_WEB_BROWSER);
        register(openWebBrowser);
        openDataSet = new OpenViewAction("Open NetCDF Dataset", window, NetCDFView.ID, true, VIEW_TYPE.NETCDF);
        exitAction = ActionFactory.QUIT.create(window);
        register(exitAction);
        IWorkbench workbench = window.getWorkbench();
        IPerspectiveRegistry registry = workbench.getPerspectiveRegistry();
        IPerspectiveDescriptor[] descriptors = registry.getPerspectives();
        for (IPerspectiveDescriptor perspective : descriptors) {
            perspectiveActions.add(new ShowPerspectiveAction(perspective.getLabel(), window, perspective.getId(), perspective.getImageDescriptor()));
        }
        IViewRegistry viewRegistry = workbench.getViewRegistry();
        IViewDescriptor[] views = viewRegistry.getViews();
        for (int i = 0; i < 4; i++) {
            final IViewDescriptor view = views[i];
            openViewActions.add(new ShowPerspectiveAction(view.getLabel(), window, view.getId(), view.getImageDescriptor(), true));
        }
        showViewsItem = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
        cacheManagerAction = new Action() {

            public void run() {
                new CacheManagerDialog(window.getShell()).open();
            }
        };
        cacheManagerAction.setText(Messages.getString("menu.cache.name"));
        updatesCheckAction = new Action() {

            public void run() {
                UpdatesCheckJob job = new UpdatesCheckJob(window);
                job.schedule();
            }
        };
        updatesCheckAction.setText(Messages.getString("menu.upd.name"));
        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);
        showHelpAction = ActionFactory.HELP_CONTENTS.create(window);
        register(showHelpAction);
        searchHelpAction = ActionFactory.HELP_SEARCH.create(window);
        register(searchHelpAction);
    }

    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager(Messages.getString("menu.file.name"), IWorkbenchActionConstants.M_FILE);
        MenuManager helpMenu = new MenuManager(Messages.getString("menu.help.name"), IWorkbenchActionConstants.M_HELP);
        menuBar.add(fileMenu);
        fileMenu.add(openFileAction);
        fileMenu.add(openDataSet);
        fileMenu.add(weatherWizardAction);
        fileMenu.add(new Separator());
        fileMenu.add(openWebBrowser);
        fileMenu.add(updatesCheckAction);
        fileMenu.add(new Separator());
        fileMenu.add(exitAction);
        MenuManager perspectiveMenu = new MenuManager(Messages.getString("menu.perspective.name"), "org.eclipse.plugin.worldwind.PERSPECTIVES");
        if (perspectiveActions.size() > 1) {
            for (Action action : perspectiveActions) {
                perspectiveMenu.add(action);
            }
        }
        MenuManager showViewMenuMgr = new MenuManager(Messages.getString("menu.showview.name"), "showView");
        for (Action action : openViewActions) {
            showViewMenuMgr.add(action);
        }
        showViewMenuMgr.add(showViewsItem);
        perspectiveMenu.add(showViewMenuMgr);
        perspectiveMenu.add(new Separator());
        perspectiveMenu.add(cacheManagerAction);
        menuBar.add(perspectiveMenu);
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menuBar.add(helpMenu);
        helpMenu.add(showHelpAction);
        helpMenu.add(searchHelpAction);
        helpMenu.add(aboutAction);
        helpMenu.add(new Separator());
        helpMenu.add(new GroupMarker(IWorkbenchActionConstants.HELP_START));
        helpMenu.add(new GroupMarker(IWorkbenchActionConstants.HELP_END));
        helpMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    @Override
    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, "main"));
        toolbar.add(weatherWizardAction);
        toolbar.add(openWebBrowser);
    }

    protected void fillStatusLine(IStatusLineManager statusLine) {
        ApplicationActionBarAdvisor.statusLine = new StatusLine(statusLine);
    }

    public static StatusLine getDefaultStatusLine() {
        return statusLine;
    }
}
