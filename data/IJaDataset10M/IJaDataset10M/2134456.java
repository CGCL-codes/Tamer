package de.creatronix.artist3k.controller.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import de.creatronix.artist3k.controller.form.EventDetailsForm;
import de.creatronix.artist3k.model.EventManager;
import de.creatronix.artist3k.model.ModelController;

public class EventDetailsAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        EventDetailsForm eventEditForm = (EventDetailsForm) form;
        EventManager eventManager = ModelController.getInstance().getEventManager();
        eventEditForm.setEvent(eventManager.loadEventByName(request.getParameter("name")));
        return mapping.findForward("eventDetails");
    }
}
