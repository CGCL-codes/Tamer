package hub.sam.mof.simulator.editor.diagram.providers;

import hub.sam.mof.simulator.editor.diagram.actions.RestoreRelatedLinksAction;
import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.AbstractContributionItemProvider;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;
import org.eclipse.jface.action.IAction;

/**
 * 
 * TODO Describe the class here <br>
 * creation : 13 nov. 07
 * 
 * @author <a href="mailto:gilles.cannenterre@anyware-tech.com">Gilles
 *         Cannenterre</a>
 */
public class DiagramActionsContributionItemProvider extends AbstractContributionItemProvider {

    /**
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.AbstractContributionItemProvider#createAction(java.lang.String,
	 *      org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor)
	 */
    @Override
    protected IAction createAction(String actionId, IWorkbenchPartDescriptor partDescriptor) {
        if (actionId.equals(RestoreRelatedLinksAction.ID)) {
            return new RestoreRelatedLinksAction();
        }
        return super.createAction(actionId, partDescriptor);
    }
}
