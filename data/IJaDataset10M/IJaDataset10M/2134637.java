package uk.ac.ebi.intact.struts.controller;

import uk.ac.ebi.intact.struts.framework.IntactBaseAction;
import uk.ac.ebi.intact.struts.framework.util.WebIntactConstants;
import uk.ac.ebi.intact.struts.view.CvEditForm;
import org.apache.struts.action.*;
import javax.servlet.http.*;

public class CvEditAction extends IntactBaseAction {

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
     * @return - represents a destination to which the controller servlet,
     * <code>ActionServlet</code>, might be directed to perform a RequestDispatcher.forward()
     * or HttpServletResponse.sendRedirect() to, as a result of processing
     * activities of an <code>Action</code> class
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        CvEditForm theForm = (CvEditForm) form;
        if (theForm.isSubmitted()) {
            super.log("Form is submitted");
        } else if (theForm.isDeleted()) {
            super.log("Cv Object is deleted");
        } else if (theForm.isCancelled()) {
            super.log("Form is cancelled");
        } else {
            super.log("Unknown action selected - should never happen");
        }
        return mapping.findForward(WebIntactConstants.FORWARD_SUCCESS);
    }
}
