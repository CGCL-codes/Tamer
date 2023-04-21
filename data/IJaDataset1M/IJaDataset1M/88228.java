package uk.ac.ebi.intact.struts.controller;

import java.util.*;
import org.apache.struts.action.*;
import javax.servlet.http.*;
import uk.ac.ebi.intact.struts.framework.util.WebIntactConstants;
import uk.ac.ebi.intact.struts.framework.IntactBaseAction;
import uk.ac.ebi.intact.struts.framework.IntactUserSession;
import uk.ac.ebi.intact.struts.service.IntactUser;
import uk.ac.ebi.intact.struts.service.IntactService;
import uk.ac.ebi.intact.struts.view.ListObject;
import uk.ac.ebi.intact.struts.view.SearchForm;
import uk.ac.ebi.intact.business.IntactException;
import uk.ac.ebi.intact.business.IntactHelper;
import uk.ac.ebi.intact.model.CvObject;

/**
 * <p>This class provides the actions required for the search page
 * for intact. The search criteria are obtained from a Form object
 * and then the search is carried out, via the Intact Helper functionality,
 * which provides the business logic. </p>
 *
 * @author Chris Lewington, Sugath Mudali (smudali@ebi.ac.uk)
 * @version $Id: SearchAction.java 8 2002-10-03 17:52:00Z hhe $
 */
public class SearchAction extends IntactBaseAction {

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
        String searchParam = null;
        String searchValue = null;
        SearchForm theForm = (SearchForm) form;
        String action = theForm.getAction();
        if (action.equals(WebIntactConstants.SEARCH_BY_AC)) {
            super.log("search by AC requested");
            searchParam = WebIntactConstants.SEARCH_BY_AC_FIELD;
            searchValue = theForm.getAcNumber();
        } else if (action.equals(WebIntactConstants.SEARCH_BY_LABEL)) {
            searchParam = WebIntactConstants.SEARCH_BY_LABEL_FIELD;
            searchValue = theForm.getLabel();
        }
        HttpSession session = super.getSession(request);
        IntactUserSession user = (IntactUserSession) session.getAttribute(WebIntactConstants.USER_SESSION);
        String topic = user.getSelectedTopic();
        super.log("search action: action value is " + action);
        super.log("search action: search param is " + searchParam);
        super.log("search action: search value is " + searchValue);
        super.log("search action: topic is " + topic);
        IntactService service = super.getIntactService();
        IntactHelper helper = null;
        try {
            helper = service.getIntactHelper();
        } catch (IntactException ie) {
            String msg = "unable to create intact helper class - no datasource";
            super.log("search action: " + msg);
            super.log(ie.getMessage());
            super.log(ie.getNestedMessage());
            ActionErrors errors = new ActionErrors();
            errors.add("no.ds", new ActionError("error.no.datasource"));
            super.saveErrors(request, errors);
            return (mapping.findForward(WebIntactConstants.FORWARD_FAILURE));
        }
        String className = service.getClassName(topic);
        try {
            Collection result = helper.search(className, searchParam, searchValue);
            if (result.isEmpty()) {
                String msg = "Sorry, no matches were found for your specified search criteria";
                super.log(msg);
                ActionErrors errors = new ActionErrors();
                errors.add("no.match", new ActionError("error.no.match"));
                super.saveErrors(request, errors);
                return (mapping.findForward(WebIntactConstants.FORWARD_FAILURE));
            } else {
                super.log("search action: search results retrieved OK");
                session.setAttribute(WebIntactConstants.SEARCH_CRITERIA, searchParam + "=" + searchValue);
                Collection container = new ArrayList();
                for (Iterator iter = result.iterator(); iter.hasNext(); ) {
                    container.add(new ListObject((CvObject) iter.next()));
                }
                session.setAttribute(WebIntactConstants.FORWARD_MATCHES, container);
            }
        } catch (IntactException ie) {
            super.log("search action: problem during search..");
            super.log(ie.getMessage());
            super.log(ie.getNestedMessage());
            String msg = "unable to perform search operation";
            super.log("search action: " + msg);
            ActionErrors errors = new ActionErrors();
            errors.add("search.error", new ActionError("error.search"));
            super.saveErrors(request, errors);
            return (mapping.findForward(WebIntactConstants.FORWARD_FAILURE));
        }
        return mapping.findForward(WebIntactConstants.FORWARD_SUCCESS);
    }
}
