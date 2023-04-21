package org.didicero.rap.base;

import java.net.URL;
import java.util.Dictionary;
import org.didicero.rap.base.editor.FooEditorInput;
import org.didicero.rap.base.wizard.SurveyWizard;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.*;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.*;

public class DemoActionBarAdvisor extends ActionBarAdvisor {

    private IWebBrowser browser;

    private IWorkbenchAction exitAction;

    private IWorkbenchAction importAction;

    private IWorkbenchAction exportAction;

    private Action aboutAction;

    private Action rapWebSiteAction;

    private MenuManager showViewMenuMgr;

    private IWorkbenchAction preferencesAction;

    private Action wizardAction;

    private Action browserAction;

    public IWorkbenchAction saveAction;

    private IWorkbenchAction saveAllAction;

    private Action newEditorAction;

    private static int browserIndex;

    public DemoActionBarAdvisor(final IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(final IWorkbenchWindow window) {
        ImageDescriptor quitActionImage = AbstractUIPlugin.imageDescriptorFromPlugin("org.didicero.rap.base", "icons/ttt.gif");
        ImageDescriptor helpActionImage = AbstractUIPlugin.imageDescriptorFromPlugin("org.didicero.rap.base", "icons/help.gif");
        ImageDescriptor wizardActionImage = AbstractUIPlugin.imageDescriptorFromPlugin("org.didicero.rap.base", "icons/login.gif");
        ImageDescriptor browserActionImage = AbstractUIPlugin.imageDescriptorFromPlugin("org.didicero.rap.base", "icons/internal_browser.gif");
        ImageDescriptor rapWebSiteActionImage = AbstractUIPlugin.imageDescriptorFromPlugin("org.didicero.rap.base", "icons/browser.gif");
        exitAction = ActionFactory.QUIT.create(window);
        exitAction.setImageDescriptor(quitActionImage);
        register(exitAction);
        importAction = ActionFactory.IMPORT.create(window);
        register(importAction);
        exportAction = ActionFactory.EXPORT.create(window);
        register(exportAction);
        saveAction = ActionFactory.SAVE.create(window);
        register(saveAction);
        saveAllAction = ActionFactory.SAVE_ALL.create(window);
        register(saveAllAction);
        preferencesAction = ActionFactory.PREFERENCES.create(window);
        register(preferencesAction);
        newEditorAction = new Action() {

            public void run() {
                try {
                    window.getActivePage().openEditor(new FooEditorInput(DemoActionBarAdvisor.this), "org.didicero.rap.base.editor", true);
                } catch (PartInitException e) {
                    e.printStackTrace();
                }
            }
        };
        newEditorAction.setText("Open new editor");
        newEditorAction.setId("org.didicero.rap.base.neweditor");
        newEditorAction.setImageDescriptor(window.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD));
        register(newEditorAction);
        aboutAction = new Action() {

            public void run() {
                Shell shell = window.getShell();
                Bundle bundle = Platform.getBundle(PlatformUI.PLUGIN_ID);
                Dictionary headers = bundle.getHeaders();
                Object version = headers.get(Constants.BUNDLE_VERSION);
                MessageDialog.openInformation(shell, "Didicero RAP Demo", "Running on RAP version " + version);
            }
        };
        aboutAction.setText("About");
        aboutAction.setId("org.didicero.rap.base.about");
        aboutAction.setImageDescriptor(helpActionImage);
        register(aboutAction);
        rapWebSiteAction = new Action() {

            public void run() {
                IWorkbenchBrowserSupport browserSupport;
                browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
                try {
                    int style = IWorkbenchBrowserSupport.AS_EXTERNAL;
                    browser = browserSupport.createBrowser(style, rapWebSiteAction.getId(), "", "");
                    browser.openURL(new URL("http://eclipse.org/rap"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        rapWebSiteAction.setText("RAP Home Page");
        rapWebSiteAction.setId("org.didicero.rap.base.rapWebSite");
        rapWebSiteAction.setImageDescriptor(rapWebSiteActionImage);
        register(rapWebSiteAction);
        showViewMenuMgr = new MenuManager("Show View", "showView");
        IContributionItem showViewMenu = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
        showViewMenuMgr.add(showViewMenu);
        wizardAction = new Action() {

            public void run() {
                SurveyWizard wizard = new SurveyWizard();
                WizardDialog dlg = new WizardDialog(window.getShell(), wizard);
                dlg.open();
            }
        };
        wizardAction.setText("Open wizard");
        wizardAction.setId("org.didicero.rap.base.wizard");
        wizardAction.setImageDescriptor(wizardActionImage);
        register(wizardAction);
        browserAction = new Action() {

            public void run() {
                browserIndex++;
                try {
                    window.getActivePage().showView("org.didicero.rap.base.DemoBrowserViewPart", String.valueOf(browserIndex), IWorkbenchPage.VIEW_ACTIVATE);
                } catch (PartInitException e) {
                    e.printStackTrace();
                }
            }
        };
        browserAction.setText("Open new Browser View");
        browserAction.setId("org.didicero.rap.base.browser");
        browserAction.setImageDescriptor(browserActionImage);
        register(browserAction);
    }

    protected void fillMenuBar(final IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager("File", IWorkbenchActionConstants.M_FILE);
        MenuManager windowMenu = new MenuManager("Window", IWorkbenchActionConstants.M_WINDOW);
        MenuManager helpMenu = new MenuManager("Help", IWorkbenchActionConstants.M_HELP);
        menuBar.add(fileMenu);
        fileMenu.add(importAction);
        fileMenu.add(exportAction);
        fileMenu.add(exitAction);
        windowMenu.add(showViewMenuMgr);
        windowMenu.add(preferencesAction);
        menuBar.add(windowMenu);
        menuBar.add(helpMenu);
        helpMenu.add(rapWebSiteAction);
        helpMenu.add(new Separator("about"));
        helpMenu.add(aboutAction);
    }

    protected void fillCoolBar(final ICoolBarManager coolBar) {
        createToolBar(coolBar, "main");
        createToolBar(coolBar, "editor");
    }

    private void createToolBar(final ICoolBarManager coolBar, final String name) {
        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, name));
        if (name != "editor") {
            toolbar.add(wizardAction);
            toolbar.add(browserAction);
            toolbar.add(aboutAction);
            toolbar.add(exitAction);
        } else {
            toolbar.add(newEditorAction);
            toolbar.add(saveAction);
            toolbar.add(saveAllAction);
        }
    }

    protected void fillStatusLine(final IStatusLineManager statusLine) {
        statusLine.add(aboutAction);
    }
}
