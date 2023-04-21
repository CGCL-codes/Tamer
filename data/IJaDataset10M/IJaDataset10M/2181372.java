package com.technoetic.xplanner.actions;

import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.classic.Session;
import com.technoetic.xplanner.db.AggregateTimesheetQuery;
import com.technoetic.xplanner.forms.AggregateTimesheetForm;

public class ViewAggregateTimesheetAction extends AbstractAction {

    private static final Logger log = Logger.getLogger("ViewAggregateTimesheetAction");

    @Override
    protected ActionForward doExecute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse reply) throws Exception {
        AggregateTimesheetForm form = (AggregateTimesheetForm) actionForm;
        try {
            Session session = getSession(request);
            try {
                form.setAllPeople(session.find("from people in class net.sf.xplanner.domain.Person " + "where people.hidden = false order by name"));
                AggregateTimesheetQuery query = new AggregateTimesheetQuery(getSession(request));
                query.setPersonIds(form.getSelectedPeople());
                query.setStartDate(form.getStartDate());
                query.setEndDate(form.getEndDate());
                form.setTimesheet(query.getTimesheet());
                if (form.getDateFormat() == null) {
                    String format = getResources(request).getMessage("format.date");
                    form.setDateFormat(new SimpleDateFormat(format));
                }
                return actionMapping.findForward("view/aggregateTimesheet");
            } catch (Exception ex) {
                session.connection().rollback();
                log.error("error", ex);
                throw new ServletException(ex);
            }
        } catch (ServletException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("error", ex);
            throw new ServletException(ex);
        }
    }
}
