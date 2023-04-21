package org.osmius.webapp.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osmius.Constants;
import org.osmius.service.OsmAgentParameterManager;
import org.osmius.service.OsmTypagentDefparamManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Controller for the agentParameterList/AJAX view
 */
public class OsmAgentParameterController implements Controller {

    private final Log log = LogFactory.getLog(OsmAgentParameterController.class);

    private OsmTypagentDefparamManager osmTypagentDefparamManager;

    private OsmAgentParameterManager osmAgentParameterManager;

    public void setOsmTypagentDefparamManager(OsmTypagentDefparamManager osmTypagentDefparamManager) {
        this.osmTypagentDefparamManager = osmTypagentDefparamManager;
    }

    public void setOsmAgentParameterManager(OsmAgentParameterManager osmAgentParameterManager) {
        this.osmAgentParameterManager = osmAgentParameterManager;
    }

    /**
    * From Spring documentation:
    * <p/>
    * Process the request and return a ModelAndView object which the DispatcherServlet
    * will render. A <code>null</code> return value is not an error: It indicates that
    * this object completed request processing itself, thus there is no ModelAndView
    * to render.
    *
    * @param request  current HTTP request
    * @param response current HTTP response
    * @return a ModelAndView to render, or <code>null</code> if handled directly
    * @throws Exception in case of errors
    */
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("--> 'handleRequest' method...");
        }
        String idnMaster = request.getParameter("idnMaster");
        String typAgent = request.getParameter("typAgent");
        String defParams = request.getParameter("defParams");
        ModelAndView model = new ModelAndView("architecture/agentParameterListAJAX");
        List parameters;
        if (defParams == null || defParams.equals("") || !defParams.equals("true")) {
            parameters = osmAgentParameterManager.getOsmAgentParameters(idnMaster, typAgent);
            model.addObject(Constants.OSMAGENTPARAMETERS_LIST, parameters);
            model.addObject("def", false);
        } else {
            parameters = osmTypagentDefparamManager.getOsmTypagentDefparams(null);
            model.addObject(Constants.OSMAGENTPARAMETERSDEF_LIST, parameters);
            model.addObject("def", true);
        }
        return model;
    }
}
