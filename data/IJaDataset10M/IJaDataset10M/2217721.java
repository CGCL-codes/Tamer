package uk.ac.ebi.intact.application.editor.struts.action.experiment;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import uk.ac.ebi.intact.application.editor.struts.framework.AbstractEditorAction;
import uk.ac.ebi.intact.application.editor.struts.view.experiment.ExperimentActionForm;
import uk.ac.ebi.intact.application.editor.struts.view.experiment.ExperimentViewBean;
import uk.ac.ebi.intact.application.editor.struts.view.experiment.InteractionRowData;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Invoked when either Add/Hide button is pressed in the form for holding
 * interactions yet to add to the Experiment.
 *
 * @author Sugath Mudali (smudali@ebi.ac.uk)
 * @version $Id: InteractionHoldAction.java 3897 2005-04-25 13:00:20Z smudali $
 *
 * @struts.action
 *      path="/exp/int/hold"
 *      name="expForm"
 *      input="edit.layout"
 *      scope="session"
 *      validate="false"
 */
public class InteractionHoldAction extends AbstractEditorAction {

    /**
     * Process the specified HTTP request, and create the corresponding
     * HTTP response (or forward to another web component that will create
     * it). Return an ActionForward instance describing where and how
     * control should be forwarded, or null if the response has
     * already been completed.
     *
     * @param mapping - The <code>ActionMapping</code> used to select this instance
     * @param form - The optional <code>ActionForm</code> bean for this request (if any)
     * @param request - The HTTP request we are processing
     * @param response - The HTTP response we are creating
     *
     * @return - represents a destination to which the action servlet,
     * <code>ActionServlet</code>, might be directed to perform a RequestDispatcher.forward()
     * or HttpServletResponse.sendRedirect() to, as a result of processing
     * activities of an <code>Action</code> class
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ExperimentActionForm expForm = (ExperimentActionForm) form;
        ExperimentViewBean view = (ExperimentViewBean) getIntactUser(request).getView();
        if (expForm.getDispatch().equals(getResources(request).getMessage("exp.int.button.add"))) {
            InteractionRowData row = view.getHoldInteraction(expForm.getIntac());
            assert row != null;
            row.setActionString();
            view.addInteraction(row);
            view.clearInteractionToHold();
        } else {
            view.hideInteractionToHold(expForm.getIntac());
        }
        return mapping.getInputForward();
    }
}
