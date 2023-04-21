package edu.ucla.mbi.curator.actions.curator.generic;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.ucla.mbi.curator.actions.curator.ajax.SendResponse;
import edu.ucla.mbi.curator.actions.curator.ajax.SendErrorResponse;
import edu.ucla.mbi.curator.webutils.session.SessionManager;
import edu.ucla.mbi.xml.MIF.elements.interactionElements.InteractorWorker;
import edu.ucla.mbi.xml.MIF.elements.adminElements.Attribute;
import edu.ucla.mbi.xml.MIF.elements.topLevelElements.InteractionBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: May 24, 2006
 * Time: 4:14:54 PM
 */
public class AddAttribute extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionManager sessionManager = SessionManager.getSessionManager(request);
        String responseString = "";
        if (request.getParameter("interactorId") != null) {
            InteractorWorker interactorWorker = new InteractorWorker(sessionManager.getInteractorModel().getInteractorByInternalId(Integer.valueOf(request.getParameter("interactorId"))));
            String name = request.getParameter("name");
            String value = request.getParameter("value");
            if (name == null || value == null || name.length() == 0 || value.length() == 0) return new SendErrorResponse().execute(mapping, form, request, response);
            boolean alreadyThere = false;
            for (Attribute a : interactorWorker.getInteractor().getAttributeList()) if (a.getName().equals(name)) alreadyThere = true;
            if (!alreadyThere) {
                interactorWorker.addAttribute(name, value);
                responseString = "<response><success/><name>" + name + "</name><value>" + value + "</value><numAttributes>" + interactorWorker.getInteractor().getAttributeList().size() + "</numAttributes></response>";
            } else responseString = "<response><success/><alreadyThere/></response>";
        } else if (request.getParameter("interactionId") != null) {
            InteractionBuilder intearctionBuilder = new InteractionBuilder(sessionManager.getInternalReferenceFactory());
            intearctionBuilder.setInteraction(sessionManager.getInteractionModel().getInteractionByInternalId(Integer.valueOf(request.getParameter("interactionId"))));
            String name = request.getParameter("name");
            String value = request.getParameter("value");
            if (name == null || value == null || name.length() == 0 || value.length() == 0) return new SendErrorResponse().execute(mapping, form, request, response);
            boolean alreadyThere = false;
            for (Attribute a : intearctionBuilder.getInteraction().getAttributeList()) if (a.getName().equals(name)) alreadyThere = true;
            if (!alreadyThere) {
                intearctionBuilder.addAttribute(name, value);
                responseString = "<response><success/><name>" + name + "</name><value>" + value + "</value><numAttributes>" + intearctionBuilder.getInteraction().getAttributeList().size() + "</numAttributes></response>";
            } else responseString = "<response><success/><alreadyThere/></response>";
        } else return new SendErrorResponse().execute(mapping, form, request, response);
        request.setAttribute("responseString", responseString);
        return new SendResponse().execute(mapping, form, request, response);
    }
}
