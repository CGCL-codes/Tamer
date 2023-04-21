package de.lema.client.eclipse.app;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    private IWorkbenchAction aboutAction;

    private IWorkbenchAction resetPerspectiveAction;

    public ApplicationActionBarAdvisor(final IActionBarConfigurer configurer) {
        super(configurer);
    }

    @Override
    protected void makeActions(final IWorkbenchWindow window) {
        register(ActionFactory.SAVE.create(window));
        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);
        resetPerspectiveAction = ActionFactory.RESET_PERSPECTIVE.create(window);
        register(resetPerspectiveAction);
    }

    @Override
    protected void fillMenuBar(final IMenuManager menuBar) {
        MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menuBar.add(helpMenu);
        helpMenu.add(aboutAction);
        helpMenu.add(resetPerspectiveAction);
    }
}
