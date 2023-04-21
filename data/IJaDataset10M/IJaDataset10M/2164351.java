package net.sf.wgfa.struts.action;

import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.wgfa.taglib.ModuleRequestCache;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author tobias
 */
public class SelectNodeAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String CONFIG = ShowSelectNodeAction.getCONFIG();
        String result = request.getParameter("uri");
        String module = mapping.getModuleConfig().getPrefix();
        ModuleRequestCache mrc = ModuleRequestCache.getInstance(request.getSession(), module);
        HashMap<String, Object> attributes = (HashMap<String, Object>) mrc.getAttribute(CONFIG + ".oldAttributes");
        Iterator<String> it = attributes.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            request.setAttribute(key, attributes.get(key));
        }
        request.setAttribute((String) mrc.getAttribute(CONFIG + ".id"), result);
        return (ActionForward) mrc.getAttribute(CONFIG + ".oldForward");
    }
}
