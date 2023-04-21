package org.rubypeople.rdt.internal.debug.ui.cheatsheets.webservice;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;
import org.rubypeople.rdt.internal.debug.ui.RdtDebugUiPlugin;

public class OpenRubyPerspectiveAction extends Action implements ICheatSheetAction {

    public void run(String[] params, ICheatSheetManager manager) {
        IWorkbenchWindow window = RdtDebugUiPlugin.getActiveWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();
        IAdaptable input;
        if (page != null) input = page.getInput(); else input = ResourcesPlugin.getWorkspace().getRoot();
        try {
            PlatformUI.getWorkbench().showPerspective("org.rubypeople.rdt.ui.PerspectiveRuby", window, input);
            notifyResult(true);
        } catch (WorkbenchException e) {
            RdtDebugUiPlugin.log(e);
            notifyResult(false);
        }
    }
}
