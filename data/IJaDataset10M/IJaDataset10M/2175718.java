package net.sf.log2web.web.log.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.log2web.web.LogSession;
import net.sf.log2web.web.common.actions.BaseAction;
import net.sf.log2web.web.log.forms.OptionsForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Guillermo Manzato (manzato@gmail.com)
 * @date Jan 11, 2008
 */
public class ShowOptionsAction extends BaseAction {

    public ActionForward executeAction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        OptionsForm form = (OptionsForm) actionForm;
        LogSession logSession = ActionHelper.getLogSession(request);
        form.setAutoUpdate(logSession.isAutoUpdate());
        form.setDisplaySize(logSession.getDisplaySize());
        form.setLevelFilter(ActionHelper.toDojoInt(logSession.getLevelFilter()));
        form.setLoggerFilter(logSession.getLoggerFilter());
        form.setMessageFilter(logSession.getMessageFilter());
        form.setThreadFilter(logSession.getThreadFilter());
        form.setUpdateInterval(logSession.getUpdateInterval());
        return mapping.findForward("success");
    }
}
