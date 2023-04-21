package com.jettmarks.bkthn.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import com.jettmarks.bkthn.domain.LogEntry;
import com.jettmarks.bkthn.domain.LogEntryDAO;
import com.jettmarks.bkthn.forms.LogEntryForm;
import com.jettmarks.db.HibernateUtil;

public class EditLogEntry extends org.apache.struts.action.Action {

    public static final String GLOBAL_FORWARD_getCommuterName = "getCommuterName";

    public static final String FORWARD_logEntry = "logEntry";

    public EditLogEntry() {
    }

    /**
   * Action for receiving a request to present a single LogEntry for editing.
   * 
   * Pull the LogEntry record from the DB and add it to the LogEntryForm which
   * knows what fields are presented to the user.  We then forward to that 
   * screen for editing.
   */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogEntryForm logEntryForm = (LogEntryForm) form;
        Integer logEntryId = logEntryForm.getLogEntryId();
        Session session = HibernateUtil.getSession();
        LogEntryDAO logEntryDao = new LogEntryDAO();
        logEntryDao.shareSession(session);
        LogEntry logEntry = logEntryDao.findById(logEntryId);
        logEntryForm.setLogEntry(logEntry);
        session.close();
        return mapping.findForward(FORWARD_logEntry);
    }
}
