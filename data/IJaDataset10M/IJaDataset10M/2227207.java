package com.ariessoftpro.events.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import com.ariessoftpro.events.EventService;
import com.ariessoftpro.events.EventServiceDao;

public class ListEventsAction extends Action {

    protected final Log logger = LogFactory.getLog(getClass());

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EventService eventService = new EventServiceDao();
        SaveEventActionForm saveEventActionForm = new SaveEventActionForm();
        DeleteEventsActionForm deleteEventsActionForm = new DeleteEventsActionForm();
        ActionMessages errors = new ActionMessages();
        Long id = null;
        try {
            id = new Long(request.getParameter("id"));
        } catch (NumberFormatException e) {
        }
        if (id != null) {
            try {
                saveEventActionForm.setEvent(eventService.getEvent(id));
            } catch (Exception e) {
                logger.error(e);
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("events.edit.error"));
            }
        }
        try {
            deleteEventsActionForm.setEvents(eventService.getEvents());
        } catch (Exception e) {
            logger.error(e);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("events.list.error"));
        }
        request.setAttribute("saveEventActionForm", saveEventActionForm);
        request.setAttribute("deleteEventsActionForm", deleteEventsActionForm);
        saveErrors(request, errors);
        return mapping.findForward("success");
    }
}
