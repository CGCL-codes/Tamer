package net.openchrom.rcp;

import java.util.ArrayList;
import java.util.List;
import net.openchrom.rcp.internal.dialogs.AdditionalPluginDialog;
import net.openchrom.rcp.preferences.support.PreferenceSupplier;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }

    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.getWorkbenchConfigurer().setSaveAndRestore(true);
        configurer.setInitialSize(new Point(800, 600));
        configurer.setTitle("OpenChrom");
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(true);
        configurer.setShowPerspectiveBar(true);
        configurer.setShowProgressIndicator(true);
        configurer.setShowFastViewBars(true);
        PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
        PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR, "topRight");
    }

    @Override
    public void postWindowOpen() {
        super.postWindowOpen();
        removeUnnecessaryMenus();
        removeUnnecessaryCoolBarEntries();
        showAdditionalPluginDialog();
    }

    /**
	 * Removes unnecessary menus like the run menu.
	 */
    private void removeUnnecessaryMenus() {
        List<String> unnecessaryMenus = new ArrayList<String>();
        unnecessaryMenus.add("org.eclipse.ui.run");
        MenuManager menuManager = ((ApplicationWindow) getWindowConfigurer().getWindow()).getMenuBarManager();
        IContributionItem contributionItem;
        for (String unecessaryMenu : unnecessaryMenus) {
            contributionItem = menuManager.find(unecessaryMenu);
            if (contributionItem != null) {
                menuManager.remove(contributionItem);
                contributionItem.dispose();
            }
        }
    }

    /**
	 * Removes unnecessary coolbar entries.
	 */
    private void removeUnnecessaryCoolBarEntries() {
        List<String> unnecessaryEntries = new ArrayList<String>();
        unnecessaryEntries.add("org.eclipse.ui.externaltools.ExternalToolsSet");
        unnecessaryEntries.add("org.eclipse.ui.edit.text.actionSet.annotationNavigation");
        unnecessaryEntries.add("org.eclipse.ui.edit.text.actionSet.navigation");
        CoolBarManager coolBarManager = ((ApplicationWindow) getWindowConfigurer().getWindow()).getCoolBarManager();
        IContributionItem contributionItem;
        for (String unnecessaryEntry : unnecessaryEntries) {
            contributionItem = coolBarManager.find(unnecessaryEntry);
            if (contributionItem != null) {
                coolBarManager.remove(contributionItem);
                contributionItem.dispose();
            }
        }
    }

    /**
	 * Show an additional plugin dialog.
	 */
    private void showAdditionalPluginDialog() {
        if (PreferenceSupplier.isShowAdditionalPluginDialog()) {
            Shell shell = getWindowConfigurer().getWindow().getShell();
            AdditionalPluginDialog dialog = new AdditionalPluginDialog(shell);
            dialog.open();
        }
    }
}
