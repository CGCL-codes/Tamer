package org.osmius.webapp.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osmius.model.OsmAgent;
import org.osmius.service.OsmAgentManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class TestUnDeployController implements Controller {

    private final Log log = LogFactory.getLog(TestUnDeployController.class);

    private OsmAgentManager osmAgentManager;

    public void setOsmAgentManager(OsmAgentManager osmAgentManager) {
        this.osmAgentManager = osmAgentManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String idnMaster = request.getParameter("idnMaster");
        String agents = request.getParameter("agents");
        ArrayList listAgents = new ArrayList();
        if (agents != null && !agents.equals("")) {
            String[] splittedAgents = agents.split(";");
            for (int i = 0; i < splittedAgents.length; i++) {
                String splittedAgent = splittedAgents[i];
                OsmAgent osmAgent = osmAgentManager.getOsmAgent(idnMaster, splittedAgent);
                if (osmAgent != null && osmAgent.getOsmAgentInstances().size() > 0) {
                    listAgents.add(osmAgent.getId().getTypAgent());
                }
            }
        }
        ModelAndView model = new ModelAndView("testUnDeployAJAXView");
        model.addObject("agentsInstances", listAgents);
        return model;
    }
}
