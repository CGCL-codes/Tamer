package fr.gfi.gfinet.web.struts.business.missionManagement.action;

import javax.portlet.RenderRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import fr.gfi.gfinet.server.CollaboratorService;
import fr.gfi.gfinet.server.MissionPeriodService;
import fr.gfi.gfinet.server.MissionService;
import fr.gfi.gfinet.server.ProjectService;
import fr.gfi.gfinet.server.info.Mission;
import fr.gfi.gfinet.server.info.MissionPeriod;
import fr.gfi.gfinet.web.struts.business.missionManagement.form.MissionForm;
import fr.gfi.gfinet.web.struts.business.missionManagement.form.MissionPeriodForm;
import fr.gfi.gfinet.web.struts.business.projectManagement.action.ProjectDisplayAction;

public class MissionDisplayAction extends Action {

    public static final int ACTION_viewAdd = 1;

    public static final int ACTION_viewUpdate = 2;

    public static final int ACTION_viewDisplay = 3;

    public static final int ACTION_viewDisplayFromProject = 4;

    public static final int ACTION_viewAddFromProject = 5;

    public static final int ACTION_viewDispalyMissionPeriode = 6;

    public static final String FORWARD_viewAdd = "viewAdd";

    public static final String FORWARD_viewDisplay = "viewDisplay";

    public static final String FORWARD_displayTabs = "displayTabs";

    public static final String FORWARD_viewUpdate = "viewUpdate";

    public static final String FORWARD_viewDispalyMissionPeriode = "viewDispalyMissionPeriode";

    public static final String FORWARD_viewUpdateMissionPeriode = "viewUpdateMissionPeriode";

    CollaboratorService collaboratorService;

    ProjectService projectService;

    MissionService missionService;

    MissionPeriodService missionPeriodService;

    private static final Log logger = LogFactory.getLog(ProjectDisplayAction.class);

    public MissionDisplayAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RenderRequest renderRequest = (RenderRequest) request.getAttribute(JavaConstants.JAVAX_PORTLET_REQUEST);
        int action = ParamUtil.get(renderRequest, "action", ACTION_viewAdd);
        request.setAttribute("action", action);
        logger.debug("execute l'action Mission" + action);
        switch(action) {
            case ACTION_viewAddFromProject:
            case ACTION_viewAdd:
                return executeCreateMission(mapping, form, renderRequest, response, action);
            case ACTION_viewUpdate:
                return executeUpdateMission(mapping, form, renderRequest, response, action);
            case ACTION_viewDisplayFromProject:
            case ACTION_viewDisplay:
                return executeDisplayMissions(mapping, form, renderRequest, response, action);
            case ACTION_viewDispalyMissionPeriode:
                return mapping.findForward(FORWARD_viewDispalyMissionPeriode);
        }
        return mapping.findForward(FORWARD_viewAdd);
    }

    /**
	 * Affiche une mission
	 * @param mapping
	 * @param form
	 * @param renderRequest
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward executeDisplayMissions(ActionMapping mapping, ActionForm form, RenderRequest renderRequest, HttpServletResponse response, int action) throws Exception {
        Mission mission = getMission(renderRequest);
        renderRequest.setAttribute("missionToDisplay", mission);
        if (action == ACTION_viewDisplayFromProject) return mapping.findForward(FORWARD_displayTabs); else return mapping.findForward(FORWARD_viewDisplay);
    }

    /**
	 * 
	 * @param mapping
	 * @param form
	 * @param renderRequest
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward executeUpdateMission(ActionMapping mapping, ActionForm form, RenderRequest renderRequest, HttpServletResponse response, int action) throws Exception {
        Mission mission = getMission(renderRequest);
        form = MissionForm.fill((MissionForm) form, mission);
        return executeCreateMission(mapping, form, renderRequest, response, action);
    }

    /**
	 * 
	 * @param mapping
	 * @param form
	 * @param renderRequest
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward executeCreateMission(ActionMapping mapping, ActionForm form, RenderRequest renderRequest, HttpServletResponse response, int action) throws Exception {
        renderRequest.setAttribute("projectList", projectService.listProjects());
        if (action == ACTION_viewAddFromProject) return mapping.findForward(FORWARD_displayTabs); else return mapping.findForward(FORWARD_viewAdd);
    }

    public Mission getMission(RenderRequest renderRequest) throws Exception {
        long action = ParamUtil.get(renderRequest, "missionId", 0);
        Mission mission = missionService.getMissionById(action);
        logger.debug("mission " + mission);
        return mission;
    }

    public MissionPeriod getMissionPeriod(RenderRequest renderRequest) throws Exception {
        long missionPeriodId = ParamUtil.get(renderRequest, "missionPeriodId", 0);
        if (missionPeriodId == 0) throw new Exception();
        MissionPeriod missionPeriod = missionPeriodService.getMissionPeriodById(missionPeriodId);
        logger.debug("missionPeriod " + missionPeriod);
        return missionPeriod;
    }

    public void setCollaboratorService(CollaboratorService collaboratorService) {
        this.collaboratorService = collaboratorService;
    }

    public void setMissionService(MissionService missionService) {
        this.missionService = missionService;
    }

    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    public void setMissionPeriodService(MissionPeriodService missionPeriodService) {
        this.missionPeriodService = missionPeriodService;
    }
}
