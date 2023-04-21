package framework;

import java.util.ArrayList;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.dialogs.PreferencesUtil;
import com.generatescape.baseobjects.CONSTANTS;
import com.generatescape.baseobjects.SearchHolder;
import com.generatescape.preferences.PrefPageOne;
import com.generatescape.views.AutoDetectView;
import com.generatescape.views.BrowserView;
import com.generatescape.xml.XMLParamReader;

/*******************************************************************************
 * Copyright (c) 2005, 2007 GenerateScape and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the GNU General Public License which accompanies this distribution, and is
 * available at http://www.gnu.org/copyleft/gpl.html
 * 
 * @author kentgibson : http://www.bigblogzoo.com
 * 
 ******************************************************************************/
public class MyActionBarAdvisor extends ActionBarAdvisor {

    private static IAction licenseProductAction;

    public static IAction resetPerspective;

    private IAction newTabAction, preferencesAction;

    private RetargetAction stopAction, refreshAction, forwardAction, backAction;

    private ActionFactory.IWorkbenchAction quitAction, aboutAction;

    private RetargetAction searchAction, spidersearchAction, quickexpandAction, sendemailAction, newsSpiderAction;

    private RetargetAction cutAction, pasteAction, homeAction;

    private IWorkbenchAction helpAction, welcomeAction;

    private static IMenuManager fileMenu;

    private static IMenuManager windowMenu;

    private static IMenuManager searchAlgolMenu;

    private IActionBarConfigurer configurer;

    private SearchHolder algorithmns[];

    private Action typesOfSearch[];

    /**
   * @param configurer
   */
    public MyActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
        this.configurer = configurer;
    }

    @Override
    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolBar3 = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        toolBar3.add(newsSpiderAction);
        toolBar3.add(spidersearchAction);
        toolBar3.add(searchAction);
        toolBar3.add(sendemailAction);
        toolBar3.add(quickexpandAction);
        toolBar3.add(cutAction);
        toolBar3.add(pasteAction);
        coolBar.add(new ToolBarContributionItem(toolBar3, "spider"));
        IToolBarManager toolBar2 = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        ActionContributionItem backCI = new ActionContributionItem(backAction);
        backCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
        toolBar2.add(backCI);
        ActionContributionItem forwardCI = new ActionContributionItem(forwardAction);
        forwardCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
        toolBar2.add(forwardCI);
        toolBar2.add(stopAction);
        toolBar2.add(refreshAction);
        toolBar2.add(homeAction);
        coolBar.add(new ToolBarContributionItem(toolBar2, "browser"));
        IToolBarManager toolBar1 = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        ActionContributionItem newtab = new ActionContributionItem(newTabAction);
        newtab.setMode(ActionContributionItem.MODE_FORCE_TEXT);
        ActionContributionItem resetPers = new ActionContributionItem(resetPerspective);
        resetPers.setMode(ActionContributionItem.MODE_FORCE_TEXT);
        toolBar1.add(newtab);
        toolBar1.add(resetPers);
        coolBar.add(new ToolBarContributionItem(toolBar1, "newtab"));
    }

    @Override
    protected void fillMenuBar(IMenuManager menuBar) {
        XMLParamReader xmlreader = new XMLParamReader();
        ArrayList algorithmnslist = xmlreader.getSearchAlogrithms();
        if (algorithmnslist != null) {
            algorithmns = new SearchHolder[algorithmnslist.size()];
            algorithmnslist.toArray(algorithmns);
            typesOfSearch = new Action[algorithmns.length];
        }
        if (algorithmns != null) {
            for (int i = 0; i < algorithmns.length; i++) {
                typesOfSearch[i] = new Action((algorithmns[i].getAlgorithm())) {

                    public void run() {
                        IPreferenceStore prefs = MyWindowAdvisor.getPrefs();
                        String text = this.getText();
                        boolean status = prefs.getBoolean(text);
                        prefs.setValue(text, !status);
                    }
                };
                IPreferenceStore prefs = MyWindowAdvisor.getPrefs();
                if (!prefs.getBoolean(CONSTANTS.PREF_INTRO_DONE)) {
                    if (algorithmns[i].getDefault()) {
                        typesOfSearch[i].setChecked(true);
                        prefs.setValue(typesOfSearch[i].getText(), true);
                    } else {
                        typesOfSearch[i].setChecked(false);
                        prefs.setValue(typesOfSearch[i].getText(), false);
                    }
                } else {
                    boolean checked = prefs.getBoolean(typesOfSearch[i].getText());
                    typesOfSearch[i].setChecked(checked);
                }
            }
        }
        AutoDetectView.setAlgorithmns(algorithmns);
        AutoDetectView.setTypesOfSearch(typesOfSearch);
        fileMenu = new MenuManager("&File", "file");
        menuBar.add(fileMenu);
        fileMenu.add(preferencesAction);
        fileMenu.add(new Separator());
        fileMenu.add(quitAction);
        windowMenu = new MenuManager("&Window", "window");
        menuBar.add(windowMenu);
        windowMenu.add(resetPerspective);
        windowMenu.add(newTabAction);
        searchAlgolMenu = new MenuManager("&Search With", "Search With");
        menuBar.add(searchAlgolMenu);
        for (int i = 0; i < typesOfSearch.length; i++) {
            searchAlgolMenu.add(typesOfSearch[i]);
        }
        IMenuManager helpMenu = new MenuManager("&Help", "help");
        menuBar.add(helpMenu);
        helpMenu.add(aboutAction);
        helpMenu.add(helpAction);
    }

    @Override
    protected void fillStatusLine(IStatusLineManager statusLine) {
        statusLine.removeAll();
    }

    @Override
    protected void makeActions(IWorkbenchWindow window) {
        ISharedImages images = window.getWorkbench().getSharedImages();
        resetPerspective = ActionFactory.RESET_PERSPECTIVE.create(window);
        resetPerspective.setText("&Reset Perspective");
        resetPerspective.setImageDescriptor(CONSTANTS.resetDesc);
        configurer.registerGlobalAction(resetPerspective);
        preferencesAction = new Action("Preferences") {

            public void run() {
                boolean idle = Job.getJobManager().isIdle();
                PrefPageOne.setIdle(idle);
                PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(null, "com.generatescape.preferences.PrefPageOne", null, null);
                dialog.open();
            }
        };
        preferencesAction.setText("&Preferences");
        configurer.registerGlobalAction(preferencesAction);
        if (licenseProductAction != null) {
            licenseProductAction.setId(CONSTANTS.REGISTERACTION);
        }
        newTabAction = new Action("New &Tab") {

            public void run() {
                try {
                    String secondaryId = Integer.toString(BrowserView.tabcounter++);
                    BrowserView.getCurrentpartsite().showView(CONSTANTS.BROWSER_VIEW_ID, secondaryId, IWorkbenchPage.VIEW_ACTIVATE);
                } catch (PartInitException e) {
                    e.printStackTrace();
                }
            }
        };
        newTabAction.setImageDescriptor(CONSTANTS.newtabDesc);
        quitAction = ActionFactory.QUIT.create(window);
        configurer.registerGlobalAction(quitAction);
        backAction = new RetargetAction("back", "&Back");
        backAction.setActionDefinitionId(CONSTANTS.COMMAND_PREFIX + "back");
        backAction.setToolTipText("Back");
        backAction.setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_BACK));
        window.getPartService().addPartListener(backAction);
        configurer.registerGlobalAction(backAction);
        newsSpiderAction = new RetargetAction("newsspider", "&News Spider");
        newsSpiderAction.setActionDefinitionId(CONSTANTS.COMMAND_PREFIX + "newsspider");
        newsSpiderAction.setToolTipText("News Spider");
        newsSpiderAction.setImageDescriptor(BrowserApp.getImageDescriptor("spider.gif"));
        window.getPartService().addPartListener(newsSpiderAction);
        configurer.registerGlobalAction(newsSpiderAction);
        homeAction = new RetargetAction("home", "&Home");
        homeAction.setActionDefinitionId(CONSTANTS.COMMAND_PREFIX + "home");
        homeAction.setToolTipText("Home");
        homeAction.setImageDescriptor(BrowserApp.getImageDescriptor("home.gif"));
        window.getPartService().addPartListener(homeAction);
        configurer.registerGlobalAction(homeAction);
        cutAction = new RetargetAction(ActionFactory.CUT.getId(), "&Cut");
        cutAction.setActionDefinitionId(CONSTANTS.COMMAND_PREFIX + ActionFactory.CUT.getId());
        cutAction.setToolTipText("Cut");
        cutAction.setImageDescriptor(BrowserApp.getImageDescriptor("cut_edit.png"));
        window.getPartService().addPartListener(cutAction);
        configurer.registerGlobalAction(cutAction);
        pasteAction = new RetargetAction(ActionFactory.PASTE.getId(), "&Paste");
        pasteAction.setActionDefinitionId(CONSTANTS.COMMAND_PREFIX + ActionFactory.PASTE.getId());
        pasteAction.setToolTipText("Paste");
        pasteAction.setImageDescriptor(BrowserApp.getImageDescriptor("paste_edit.png"));
        window.getPartService().addPartListener(pasteAction);
        configurer.registerGlobalAction(pasteAction);
        spidersearchAction = new RetargetAction("spidersearch", "&News Spider");
        spidersearchAction.setActionDefinitionId(CONSTANTS.COMMAND_PREFIX + "spidersearch");
        spidersearchAction.setToolTipText("Spider Search");
        spidersearchAction.setImageDescriptor(BrowserApp.getImageDescriptor("spidersearchv3.png"));
        window.getPartService().addPartListener(spidersearchAction);
        configurer.registerGlobalAction(spidersearchAction);
        searchAction = new RetargetAction("search", "&Channel Search");
        searchAction.setActionDefinitionId(CONSTANTS.COMMAND_PREFIX + "search");
        searchAction.setToolTipText("Channel Search");
        searchAction.setImageDescriptor(BrowserApp.getImageDescriptor("magglass.png"));
        window.getPartService().addPartListener(searchAction);
        configurer.registerGlobalAction(searchAction);
        sendemailAction = new RetargetAction("emailthis", "&Email This");
        sendemailAction.setActionDefinitionId(CONSTANTS.COMMAND_PREFIX + "emailthis");
        sendemailAction.setToolTipText("Email This");
        sendemailAction.setImageDescriptor(BrowserApp.getImageDescriptor("sendemail.gif"));
        window.getPartService().addPartListener(sendemailAction);
        configurer.registerGlobalAction(sendemailAction);
        quickexpandAction = new RetargetAction("quickexpand", "&Quick Expand");
        quickexpandAction.setActionDefinitionId(CONSTANTS.COMMAND_PREFIX + "quickexpand");
        quickexpandAction.setToolTipText("Quick Expand");
        quickexpandAction.setImageDescriptor(BrowserApp.getImageDescriptor("quick.png"));
        window.getPartService().addPartListener(quickexpandAction);
        configurer.registerGlobalAction(quickexpandAction);
        forwardAction = new RetargetAction("forward", "&Forward");
        forwardAction.setActionDefinitionId(CONSTANTS.COMMAND_PREFIX + "forward");
        forwardAction.setToolTipText("Forward");
        forwardAction.setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_FORWARD));
        window.getPartService().addPartListener(forwardAction);
        configurer.registerGlobalAction(forwardAction);
        stopAction = new RetargetAction("stop", "Sto&p");
        stopAction.setActionDefinitionId(CONSTANTS.COMMAND_PREFIX + "stop");
        stopAction.setToolTipText("Stop Loading Page");
        stopAction.setImageDescriptor(BrowserApp.getImageDescriptor("stoploading.gif"));
        window.getPartService().addPartListener(stopAction);
        configurer.registerGlobalAction(stopAction);
        refreshAction = new RetargetAction("refresh", "&Refresh");
        refreshAction.setActionDefinitionId(CONSTANTS.COMMAND_PREFIX + "refresh");
        refreshAction.setToolTipText("Refresh");
        refreshAction.setImageDescriptor(CONSTANTS.refeshDesc);
        window.getPartService().addPartListener(refreshAction);
        configurer.registerGlobalAction(refreshAction);
        aboutAction = ActionFactory.ABOUT.create(window);
        configurer.registerGlobalAction(aboutAction);
        helpAction = ActionFactory.HELP_CONTENTS.create(window);
        configurer.registerGlobalAction(helpAction);
    }
}
