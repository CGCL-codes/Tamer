package org.osmius.webapp.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osmius.model.OsmNGuard;
import org.osmius.service.OsmNGuardManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestGuardController implements Controller {

    private final Log log = LogFactory.getLog(TestGuardController.class);

    private OsmNGuardManager osmNGuardManager;

    public void setOsmNGuardManager(OsmNGuardManager osmNGuardManager) {
        this.osmNGuardManager = osmNGuardManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String idnGuard = request.getParameter("idnGuard");
        OsmNGuard osmNGuard = osmNGuardManager.getOsmNGuard(new Long(idnGuard));
        ModelAndView model = new ModelAndView("testGuardView");
        model.addObject("osmNGuard", osmNGuard);
        return model;
    }
}
