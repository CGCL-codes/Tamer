package ru.adv.web.app.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import ru.adv.web.MozartController;
import ru.adv.mozart.Defaults;
import ru.adv.mozart.framework.HostContext;

/**
 * 
 * Controller of Mozart status page
 * 
 * @author vic
 * @version $Id$
 *
 */
public class MozartStatusController implements InitializingBean, Controller {

    private HostContext hostContext;

    private MozartController mozartController;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(getMozartController(), "Property mozartController is not set");
        Assert.notNull(getHostContext(), "Property hostContext is not set");
    }

    public MozartController getMozartController() {
        return mozartController;
    }

    public void setMozartController(MozartController mozartController) {
        this.mozartController = mozartController;
    }

    public HostContext getHostContext() {
        return hostContext;
    }

    public void setHostContext(HostContext hostContext) {
        this.hostContext = hostContext;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean gc = request.getParameter("gc") != null;
        boolean memory = request.getParameter("memory") != null;
        String segment = request.getParameter("cachedump");
        if (null != segment) {
            response.setContentType("text/plain; charset=UTF-8");
            MozartStatus.writeCacheDump(getHostContext(), segment, response.getOutputStream(), memory);
            response.getOutputStream().flush();
        } else if (gc) {
            long freeMem = Runtime.getRuntime().freeMemory();
            System.gc();
            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().print(freeMem + " bytes before collecting\n");
            response.getWriter().print(Runtime.getRuntime().freeMemory() + " bytes after collecting\n");
            response.getWriter().flush();
        } else {
            response.setContentType("text/html; charset=" + Defaults.ENCODING);
            response.getWriter().print("<pre>\n");
            response.getWriter().print(MozartStatus.createStatusString(getMozartController().getMozartServlet(), request.getServerName(), memory));
            response.getWriter().print("</pre>\n");
            response.getWriter().flush();
        }
        return null;
    }
}
