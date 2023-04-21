package evolaris.framework.sys.web.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import evolaris.framework.sys.business.GroupManager;
import evolaris.framework.sys.business.exception.ConfigurationException;
import evolaris.framework.um.datamodel.Group;

/**
 * server-side part of ajax communication for switching group
 * @author robert.brandner
 * @date 2007-08-08
 */
public class SwitchGroupAction extends LocalizedAction {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(SwitchGroupAction.class);

    protected ActionForward executeAccordingToMethod(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp, String method) throws Exception {
        if ("switchCurrentGroup".equals(method)) {
            return switchCurrentGroup(mapping, form, req, resp);
        } else {
            return null;
        }
    }

    private ActionForward switchCurrentGroup(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("name");
        GroupManager groupMgr = new GroupManager(locale, session);
        Group group = groupMgr.getGroup(name);
        if (group == null) {
            throw new ConfigurationException("Switching groups failed: No group named `" + name + "` found.");
        }
        setCurrentGroup(req, resp, group);
        LOGGER.debug("Switched to group `" + name + "`");
        String jsonString = "[result:\"ok\"]";
        resp.setContentType("application/json");
        resp.setContentLength(jsonString.getBytes().length);
        try {
            resp.getOutputStream().write(jsonString.getBytes());
            resp.getOutputStream().flush();
        } catch (IOException e) {
            LOGGER.error("error writing to response.");
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        return map;
    }

    @Override
    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp) {
        return null;
    }

    @Override
    protected ActionForward defaultMethod(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp) {
        return null;
    }
}
