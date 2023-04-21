package org.o14x.alpha.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.o14x.alpha.domain.ExplorerTab;
import org.o14x.alpha.ui.controller.UIController;

/**
 * View next tab command.
 * 
 * @author Olivier DANGREAUX
 */
public class CloseOtherTabsCommand extends AbstractHandler {

    /**
	 * UIController
	 */
    private UIController uiController;

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        ExplorerTab currentExplorerTab = uiController.getDomain().getCurrentExplorerTab();
        ExplorerTab[] explorerTabs = uiController.getDomain().getExplorerTabManager().getExplorerTabMap().values().toArray(new ExplorerTab[0]);
        for (ExplorerTab explorerTab : explorerTabs) {
            if (explorerTab != currentExplorerTab) {
                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(explorerTab.getFolderView());
            }
        }
        return null;
    }

    /**
	 * Returns the value of uiController.
	 *
	 * @return The value of uiController.
	 */
    public UIController getUiController() {
        return uiController;
    }

    /**
	 * Sets the value of uiController.
	 *
	 * @param uiController The value of uiController to set.
	 */
    public void setUiController(UIController uiController) {
        this.uiController = uiController;
    }
}
