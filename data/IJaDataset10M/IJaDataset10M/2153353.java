package de.beas.explicanto.client.rcp.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.PlatformUI;
import de.beas.explicanto.client.I18N;
import de.beas.explicanto.client.rcp.ApplicationWorkbenchAdvisor;
import de.beas.explicanto.client.rcp.dialogs.ExplicantoMessageDialog;
import de.beas.explicanto.client.rcp.projects.views.GenericNodeProperties;

/**
 * Action for logging off from the application.
 * After the logoff is performed the login action is invoked. If the user does not
 * want to continue the application close.
 * 
 * @author marius.staicu
 * @version 1.0
 *
 */
public class LogoutAction extends GenericAction {

    /**
	 * Main function performing the loggoff
	 */
    public void run(IAction action) {
        if (GenericNodeProperties.isEditMode()) {
            ExplicantoMessageDialog.openWarning(window.getShell(), translate("explicanto.messages.inEditMode"));
            return;
        }
        if (!ExplicantoMessageDialog.openQuestion(window.getShell(), I18N.translate("action.logout.confirm"))) return;
        ApplicationWorkbenchAdvisor.setExitAction(false);
        PlatformUI.getWorkbench().restart();
    }

    public String getTranslatedName() {
        return translate("actions.logout.title");
    }

    protected String getTranslatedToolTip() {
        return translate("actions.logout.tooltip");
    }
}
