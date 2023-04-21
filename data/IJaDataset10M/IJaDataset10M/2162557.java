package de.beas.explicanto.distribution.web.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRDataSource;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import de.beas.explicanto.distribution.config.Constants;
import de.beas.explicanto.distribution.reports.datasources.UserMonitorDS;
import de.beas.explicanto.distribution.util.ExplicantoMessages;

/**
 * Created by mihai.
 * User: mihai
 * Date: Apr 13, 2005
 * Time: 1:32:08 PM
 */
public class UserMonitoringAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(UserMonitoringAction.class);

    /**
	 * Method execute
	 *
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse res) throws Exception {
        if (isCancelled(request)) {
            return mapping.getInputForward();
        }
        List selectedList = getSelection(request);
        ActionMessages messages = new ActionMessages();
        ActionForward fwd = mapping.findForward(Constants.SUCCESS_KEY);
        try {
            JRDataSource ds = null;
            List data = getCourseMonitoringService().getUserStatusAndSuccess(selectedList);
            ds = new UserMonitorDS(data);
            request.setAttribute("dataSource", ds);
            Map parameters = new HashMap();
            ExplicantoMessages resources = (ExplicantoMessages) request.getSession().getServletContext().getAttribute("resource");
            parameters.put("title", resources.getMessage("user.column.title", null, getLocale(request)));
            parameters.put("col1_title", resources.getMessage("user.column.first", null, getLocale(request)));
            parameters.put("col2_title", resources.getMessage("user.column.second", null, getLocale(request)));
            parameters.put("col3_title", resources.getMessage("user.column.third", null, getLocale(request)));
            parameters.put("col4_title", resources.getMessage("user.column.fourth", null, getLocale(request)));
            request.setAttribute("parameters", parameters);
        } catch (Exception e) {
            logger.error("At UserMonitoringAction !!!", e);
            messages.add(Constants.ERROR_MESSAGE, new ActionMessage("global.error.message", e.getMessage()));
        }
        if (!messages.isEmpty()) {
            saveMessages(request, messages);
        }
        return fwd;
    }
}
