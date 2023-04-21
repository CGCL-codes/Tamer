package uk.ac.ebi.intact.application.editor.struts.action.experiment;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import uk.ac.ebi.intact.application.editor.business.EditUserI;
import uk.ac.ebi.intact.application.editor.business.EditorService;
import uk.ac.ebi.intact.application.editor.struts.action.CommonDispatchAction;
import uk.ac.ebi.intact.application.editor.struts.view.experiment.ExperimentActionForm;
import uk.ac.ebi.intact.business.IntactHelper;
import uk.ac.ebi.intact.model.Interaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Links to an interaction button from an experiment.
 *
 * @author Sugath Mudali (smudali@ebi.ac.uk)
 * @version $Id: InteractionLinkAction.java 3955 2005-05-10 10:52:49Z smudali $
 *
 * @struts.action
 *      path="/int/link"
 *      name="expForm"
 *      input="edit.layout"
 *      scope="session"
 *
 * @struts.action-forward
 *      name="success"
 *      path="/do/int/fill/form"
 */
public class InteractionLinkAction extends CommonDispatchAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forward = save(mapping, form, request, response);
        if (!forward.equals(mapping.findForward(SUCCESS))) {
            return forward;
        }
        EditUserI user = getIntactUser(request);
        String intAc = (String) ((ExperimentActionForm) form).getIntac();
        IntactHelper helper = user.getIntactHelper();
        Interaction inter = (Interaction) helper.getObjectByAc(Interaction.class, intAc);
        assert inter != null;
        ActionErrors errors = acquire(intAc, user.getUserName(), "err.interaction");
        if (errors != null) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        user.setSelectedTopic(EditorService.getTopic(Interaction.class));
        user.setView(inter, false);
        return mapping.findForward(SUCCESS);
    }
}
