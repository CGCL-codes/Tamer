package edu.ucla.mbi.curator.actions.curator.experiment;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.ucla.mbi.xml.MIF.elements.topLevelElements.Experiment;
import edu.ucla.mbi.xml.MIF.elements.topLevelElements.Interaction;
import edu.ucla.mbi.xml.MIF.elements.referencing.ExperimentRefList;
import edu.ucla.mbi.curator.webutils.session.SessionManager;
import edu.ucla.mbi.curator.webutils.session.CurrentState;
import edu.ucla.mbi.curator.webutils.model.ExperimentModel;
import edu.ucla.mbi.curator.actions.curator.initialization.InitExperimentModel;
import edu.ucla.mbi.curator.forms.curator.StoichiometryForm;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: Jan 2, 2006
 * Time: 2:12:58 PM
 */
public class CreateExperiments extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionManager sessionManager = SessionManager.getSessionManager(request);
        ExperimentModel experimentModel;
        if ((experimentModel = sessionManager.getExperimentModel()) == null) {
            new InitExperimentModel().execute(mapping, form, request, response);
            experimentModel = sessionManager.getExperimentModel();
        }
        Integer numExperiments = 1;
        Interaction interaction = (Interaction) request.getAttribute("interaction");
        ExperimentRefList expts;
        ArrayList<Experiment> createdExperiments = new ArrayList<Experiment>();
        for (int i = 0; i < numExperiments; i++) {
            Experiment experiment = experimentModel.createExperiment();
            createdExperiments.add(experiment);
            experimentModel.addExperimentToModel(experiment);
        }
        request.setAttribute("createdExperiments", createdExperiments);
        if (sessionManager.getCurrentState().getState() < CurrentState.EXPERIMENT_DEFINED && experimentModel.size() > 0) while (sessionManager.getCurrentState().getState() < CurrentState.EXPERIMENT_DEFINED) sessionManager.advanceCurrentState();
        new GetExperiments().execute(mapping, form, request, response);
        return mapping.findForward("success");
    }
}
