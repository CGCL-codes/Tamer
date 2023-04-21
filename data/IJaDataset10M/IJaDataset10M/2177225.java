package org.sqlanyware.struts.actions;

import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sqlanyware.model.hibernate.GlobalOptions;
import org.sqlanyware.model.hibernate.LogEntry;
import org.sqlanyware.model.hibernate.dao.GlobalOptionsDAO;
import org.sqlanyware.model.hibernate.dao.LogEntryDAO;
import org.sqlanyware.struts.Constants;
import org.sqlanyware.struts.forms.GlobalOptionsForm;

/**
 * @author Propri�taire
 *
 */
public class GlobalOptionsViewAction extends ActionBase {

    public ActionForward execute(ActionMapping _oMap, ActionForm _oForm, HttpServletRequest _oReq, HttpServletResponse _oResp) throws Exception {
        GlobalOptionsForm oOptForm = (GlobalOptionsForm) _oForm;
        if (GlobalOptionsForm.MODE_READ == oOptForm.getMode()) {
            GlobalOptions oOpt = GlobalOptionsDAO.getInstance().get(Constants.GLOBAL_OPTIONS_CODE);
            if (null == oOpt) {
                return _oMap.findForward("Default");
            }
            oOptForm.setUserCreationAllowed(oOpt.isUserCreationAllowed());
            oOptForm.setLogonLogged(oOpt.isLogonLogged());
            oOptForm.setUpdateLogged(oOpt.isUpdateLogged());
            oOptForm.setSelectLogged(oOpt.isSelectLogged());
            oOptForm.setMessageOfTheDay(oOpt.getMessageOfTheDay());
            List lLogEntries = LogEntryDAO.getInstance().findAll();
            LogEntry[] aLogEntries = lLogEntries != null ? new LogEntry[lLogEntries.size()] : new LogEntry[0];
            Iterator oIter = lLogEntries != null ? lLogEntries.iterator() : null;
            int nIdx = 0;
            while (oIter != null && oIter.hasNext()) {
                LogEntry oEntry = (LogEntry) oIter.next();
                aLogEntries[nIdx++] = oEntry;
            }
            _oReq.setAttribute(Constants.ALL_LOGS_KEY, aLogEntries);
            return _oMap.findForward("Default");
        } else {
            if (this.isCancelled(_oReq)) {
                return _oMap.findForward("SQLForward");
            }
            GlobalOptions oOpt = GlobalOptionsDAO.getInstance().get(Constants.GLOBAL_OPTIONS_CODE);
            if (null != oOpt) {
                oOpt.setUserCreationAllowed(oOptForm.isUserCreationAllowed());
                oOpt.setLogonLogged(oOptForm.isLogonLogged());
                oOpt.setUpdateLogged(oOptForm.isUpdateLogged());
                oOpt.setSelectLogged(oOptForm.isSelectLogged());
                oOpt.setMessageOfTheDay(oOptForm.getMessageOfTheDay());
            }
            GlobalOptionsDAO.getInstance().update(oOpt);
            return _oMap.findForward("SQLForward");
        }
    }
}
