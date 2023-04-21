package com.centraview.projects.timeslip;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class NewTimeSlipHandler extends org.apache.struts.action.Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String ticketId = request.getParameter("ticketId");
        String subject = request.getParameter("subject");
        TimeSlipForm dynaForm = (TimeSlipForm) form;
        if (subject != null && ticketId != null && !ticketId.equals("")) {
            dynaForm.setTicketID(ticketId);
            dynaForm.setTicket(subject);
            dynaForm.setReference(subject);
            dynaForm.setLookupList("2");
        }
        return mapping.findForward(".view.projects.new.timeslip");
    }
}
