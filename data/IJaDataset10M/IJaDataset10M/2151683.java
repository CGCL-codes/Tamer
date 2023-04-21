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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import com.liferay.portal.kernel.util.JavaConstants;
import fr.gfi.gfinet.server.CollaboratorService;
import fr.gfi.gfinet.server.MissionService;
import fr.gfi.gfinet.server.ProjectService;
import fr.gfi.gfinet.server.info.Collaborator;
import fr.gfi.gfinet.server.info.Mission;
import fr.gfi.gfinet.server.info.Project;
import fr.gfi.gfinet.web.struts.business.missionManagement.form.MissionForm;

public class MissionUpdateAction extends Action {

    public static final String FORWARD_update = "update";

    private static final Log logger = LogFactory.getLog(MissionUpdateAction.class);

    public MissionService missionService;

    public CollaboratorService collaboratorService;

    public ProjectService projectService;

    public MissionUpdateAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RenderRequest renderRequest = (RenderRequest) request.getAttribute(JavaConstants.JAVAX_PORTLET_REQUEST);
        logger.debug("entr�e dans l'action update de la mission ");
        boolean err = false;
        ActionMessages am = new ActionMessages();
        MissionForm missionForm = (MissionForm) form;
        if ((missionForm.getParentProjectId() == null) || (missionForm.getParentProjectId().length() == 0)) {
            err = true;
            am.add("error", new ActionMessage("missionManagement.error.project"));
        }
        if ((missionForm.getCollaboratorId() == null) || (missionForm.getCollaboratorId().length() == 0)) {
            err = true;
            am.add("error", new ActionMessage("missionManagement.error.collaborateur"));
        }
        if ((missionForm.getMissionType() == null) || (missionForm.getMissionType().length() == 0)) {
            err = true;
            am.add("error", new ActionMessage("missionManagement.error.type"));
        }
        if ((missionForm.getDescription() == null) || (missionForm.getDescription().length() <= 1)) {
            err = true;
            am.add("error", new ActionMessage("missionManagement.error.description"));
        }
        if (!err) {
            logger.debug("projet ok - debut sauvegarde");
            try {
                Mission missionToSave = null;
                if ((missionForm.getMissionId() == null) || (missionForm.getMissionId().length() < 1)) {
                    missionToSave = new Mission();
                } else {
                    missionToSave = missionService.getMissionById(Long.valueOf(missionForm.getMissionId()));
                }
                missionToSave.setDescription(missionForm.getDescription());
                missionToSave.setTechnicalEnvironment(missionForm.getTechnicalEnvironment());
                Collaborator collabAffecte = collaboratorService.getCollaborator(Long.valueOf(missionForm.getCollaboratorId()));
                missionToSave.setTheCollaborator(collabAffecte);
                Project project = projectService.getProjectById(Long.valueOf(missionForm.getParentProjectId()));
                missionToSave.setTheParentProject(project);
                missionService.saveMission(missionToSave);
                am.add("succes", new ActionMessage("common.succes"));
                missionForm = missionForm.fill(missionForm, missionToSave);
                logger.info("sauvegarde du mission ok");
            } catch (Exception e) {
                logger.error("impossible de sauvegarder le mission", e);
                am.add("error", new ActionMessage("missionManagement.error", e.getLocalizedMessage()));
            }
        } else {
            logger.debug("sauvegarde mission KO");
        }
        request.setAttribute("projectList", projectService.listProjects());
        saveErrors(request, am);
        return mapping.findForward(FORWARD_update);
    }

    public void setMissionService(MissionService missionService) {
        this.missionService = missionService;
    }

    public void setCollaboratorService(CollaboratorService collaboratorService) {
        this.collaboratorService = collaboratorService;
    }

    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }
}
