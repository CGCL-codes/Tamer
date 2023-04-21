package com.objectcarpentry.kaylee.ui;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import com.objectcarpentry.kaylee.ui.menus.ShowViewMenu;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    protected IContributionItem showViews;

    protected IContributionItem newPlaylistContribution;

    protected IContributionItem deleteContribution;

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(IWorkbenchWindow window) {
        showViews = new ShowViewMenu(window, null);
    }

    protected void fillMenuBar(IMenuManager menuBar) {
        IMenuManager file = new MenuManager("File", KayleeUIPlugin.ID_PREFIX + ".menus.file");
        menuBar.add(file);
        IMenuManager window = new MenuManager("Window", KayleeUIPlugin.ID_PREFIX + ".menus.show");
        window.add(showViews);
        menuBar.add(window);
    }
}
