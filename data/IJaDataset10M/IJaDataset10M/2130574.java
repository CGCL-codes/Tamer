package org.osmius.webapp.action;

import org.apache.commons.lang.StringUtils;
import org.osmius.model.*;
import org.osmius.service.*;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class OsmAgentFormController extends BaseFormController {

    OsmMasteragentManager osmMasteragentManager;

    OsmTaskManager osmTaskManager;

    OsmAgentManager osmAgentManager;

    OsmTypagentManager osmTypagentManager;

    OsmTypagentDefparamManager osmTypagentDefparamManager;

    OsmAgentParameterManager osmAgentParameterManager;

    OsmAgentInstanceManager osmAgentInstanceManager;

    OsmInstanceManager osmInstanceManager;

    UtilsManager utilsManager;

    public void setUtilsManager(UtilsManager utilsManager) {
        this.utilsManager = utilsManager;
    }

    public void setOsmTypagentManager(OsmTypagentManager osmTypagentManager) {
        this.osmTypagentManager = osmTypagentManager;
    }

    public void setOsmMasteragentManager(OsmMasteragentManager osmMasteragentManager) {
        this.osmMasteragentManager = osmMasteragentManager;
    }

    public void setOsmTaskManager(OsmTaskManager osmTaskManager) {
        this.osmTaskManager = osmTaskManager;
    }

    public void setOsmAgentManager(OsmAgentManager osmAgentManager) {
        this.osmAgentManager = osmAgentManager;
    }

    public void setOsmTypagentDefparamManager(OsmTypagentDefparamManager osmTypagentDefparamManager) {
        this.osmTypagentDefparamManager = osmTypagentDefparamManager;
    }

    public void setOsmAgentParameterManager(OsmAgentParameterManager osmAgentParameterManager) {
        this.osmAgentParameterManager = osmAgentParameterManager;
    }

    public void setOsmAgentInstanceManager(OsmAgentInstanceManager osmAgentInstanceManager) {
        this.osmAgentInstanceManager = osmAgentInstanceManager;
    }

    public void setOsmInstanceManager(OsmInstanceManager osmInstanceManager) {
        this.osmInstanceManager = osmInstanceManager;
    }

    public OsmAgentFormController() {
        setSessionForm(true);
        setCommandName("osmAgent");
        setCommandClass(OsmAgent.class);
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String idnMaster = request.getParameter("idnMaster");
        String typAgent = request.getParameter("typAgent");
        OsmAgent osmAgent;
        if (!StringUtils.isEmpty(idnMaster) && !StringUtils.isEmpty(typAgent)) {
            osmAgent = osmAgentManager.getOsmAgent(idnMaster, typAgent);
        } else {
            osmAgent = new OsmAgent();
        }
        return osmAgent;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("---> 'onSubmit' method...");
        }
        String master = request.getParameter("osmMasteragent.idnMaster");
        String typAgent = request.getParameter("osmTypagent.typAgent");
        String[] arrParam = request.getParameterValues("idnParam[]");
        String[] arrValue = request.getParameterValues("txtValue[]");
        String[] arrCheck = request.getParameterValues("check[]");
        String indState = request.getParameter("indState");
        String flagApply = request.getParameter("flagAgt");
        if (flagApply.equals("0")) {
            OsmAgentParameter[] parameters = new OsmAgentParameter[arrParam.length];
            for (int i = 0; i < arrValue.length; i++) {
                String value = arrValue[i];
                String param = arrParam[i];
                OsmAgentParameter parameter = new OsmAgentParameter();
                parameter.setOsmAgent(osmAgentManager.getOsmAgent(master, typAgent));
                parameter.setOsmTypagentDefparam(osmTypagentDefparamManager.getOsmTypagentagentDefparam(param));
                parameter.setTxtValue(value);
                parameter.setId(new OsmAgentParameterId(master, typAgent, param));
                parameters[i] = parameter;
            }
            osmAgentParameterManager.saveOsmAgentParameters(parameters);
        } else {
            if (arrCheck != null) {
                OsmAgentInstance[] osmAgentInstances = new OsmAgentInstance[arrCheck.length];
                for (int i = 0; i < arrCheck.length; i++) {
                    String instance = arrCheck[i];
                    osmAgentInstances[i] = new OsmAgentInstance(new OsmAgentInstanceId(master, typAgent, instance), osmAgentManager.getOsmAgent(master, typAgent), osmInstanceManager.getOsmInstance(instance), new Date(utilsManager.getActualTimestamp().getTime()));
                }
                osmAgentInstanceManager.assignAgentInstances(master, typAgent, osmAgentInstances);
            } else {
                osmAgentInstanceManager.removeAgentIntances(master, typAgent);
            }
        }
        OsmTask task = new OsmTask();
        task.setOsmMasteragent(osmMasteragentManager.getOsmMasteragent(master));
        task.setOsmTypagent(osmTypagentManager.getOsmTypagent(typAgent));
        task.setIndState(new Integer(0));
        task.setDtiExecute(new Date(utilsManager.getActualTimestamp().getTime()));
        task.setNumRetries(new Integer(0));
        OsmTyptask osmTyptask = new OsmTyptask();
        osmTyptask.setTypTask(new Integer(1));
        task.setOsmTyptask(osmTyptask);
        osmTaskManager.saveTask(task);
        request.setAttribute("idnMaster", master);
        request.setAttribute("typAgent", typAgent);
        return showForm(request, response, errors);
    }
}
