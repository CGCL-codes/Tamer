package edu.ucla.mbi.curator.actions.curator.experiment;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.ucla.mbi.curator.webutils.session.SessionManager;
import edu.ucla.mbi.curator.webutils.model.ExperimentModel;
import edu.ucla.mbi.curator.actions.curator.initialization.InitExperimentModel;
import edu.ucla.mbi.xml.MIF.elements.topLevelElements.Experiment;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: Dec 28, 2005
 * Time: 10:10:19 AM
 */
public class DeleteExperiment extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionManager mgr = SessionManager.getSessionManager(request);
        ExperimentModel experimentModel;
        if ((experimentModel = mgr.getExperimentModel()) == null) {
            new InitExperimentModel().execute(mapping, form, request, response);
            experimentModel = mgr.getExperimentModel();
        }
        assert experimentModel != null;
        Integer experimentId;
        try {
            experimentId = Integer.valueOf(request.getParameter("experimentId"));
        } catch (NumberFormatException nfe) {
            try {
                experimentId = ((Experiment) request.getAttribute("experiment")).getInternalReference().getReference();
            } catch (NullPointerException npe) {
                experimentId = (Integer) request.getAttribute("experimentId");
            }
        }
        experimentModel.deleteExperimentByExperimentId(experimentId, mgr.getInteractionModel().getInteractions());
        return mapping.findForward("success");
    }
}
