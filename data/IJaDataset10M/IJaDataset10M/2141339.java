package org.osmius.webapp.action;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.joda.time.DateTime;
import org.osmius.dao.jdbc.OsmServiceInstanceDaoJDBC;
import org.osmius.service.OsmServiceSlaManager;
import org.osmius.service.OsmSlaManager;
import org.osmius.service.UtilsManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;

public class SLAsTreeController implements Controller {

    private OsmSlaManager osmSlaManager;

    private OsmServiceSlaManager osmServiceSlaManager;

    private UtilsManager utilsManager;

    public void setUtilsManager(UtilsManager utilsManager) {
        this.utilsManager = utilsManager;
    }

    public void setOsmServiceInstanceDaoJDBC(OsmServiceInstanceDaoJDBC osmServiceInstanceDaoJDBC) {
        this.osmServiceInstanceDaoJDBC = osmServiceInstanceDaoJDBC;
    }

    private OsmServiceInstanceDaoJDBC osmServiceInstanceDaoJDBC;

    public void setOsmSlaManager(OsmSlaManager osmSlaManager) {
        this.osmSlaManager = osmSlaManager;
    }

    public void setOsmServiceSlaManager(OsmServiceSlaManager osmServiceSlaManager) {
        this.osmServiceSlaManager = osmServiceSlaManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String idnUser = null;
        SecurityContext ctx = SecurityContextHolder.getContext();
        if (ctx.getAuthentication() != null) {
            Authentication auth = ctx.getAuthentication();
            idnUser = auth.getName();
        }
        ModelAndView modelView = new ModelAndView("slasTreeView");
        String node = request.getParameter("node");
        if (node == null || node.equals("") || node.equals("undefined") || node.equals("/")) {
            List osmSlas = osmSlaManager.getOsmSLAsTree();
            modelView.addObject("slas", osmSlas);
        } else if (node.substring(0, 3).equals("GRP")) {
            List osmServices = osmServiceSlaManager.getOsmServicesSlaBySla(node.substring(4));
            modelView.addObject("services", osmServices);
        } else {
            List instances = osmServiceInstanceDaoJDBC.getInstancesByService(node.substring(4));
            modelView.addObject("instances", instances);
        }
        Timestamp time = utilsManager.getActualTimestamp();
        DateTime now = new DateTime(time.getTime());
        modelView.addObject("now", now);
        return modelView;
    }
}
