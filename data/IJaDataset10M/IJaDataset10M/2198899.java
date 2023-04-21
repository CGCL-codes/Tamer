package com.google.inject.tools.ideplugin.eclipse;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

/**
 * Show the Guice Errors View.
 * 
 * @author Darren Creutz (dcreutz@gmail.com)
 */
public class ShowErrorsViewAction extends EclipseMenuAction {

    public ShowErrorsViewAction() {
        super(PluginTextValues.SHOW_GUICE_ERRORS, PluginDefinitionValues.GUICE_ERRORS_ICON);
    }

    @Override
    protected String myTooltip() {
        return PluginTextValues.SHOW_GUICE_ERRORS;
    }

    @Override
    protected String myStatusFailedMessage() {
        return PluginTextValues.CANT_OPEN_ERRORS;
    }

    @Override
    protected boolean runMyAction(IEditorPart part) {
        try {
            IWorkbenchPage activePage = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getActivePage();
            activePage.showView(PluginDefinitionValues.ERROR_VIEW_ID);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
