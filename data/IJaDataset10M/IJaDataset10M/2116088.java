package edu.ucla.mbi.curator.actions.curator.participant;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.ucla.mbi.curator.forms.curator.ParticipantForm;
import edu.ucla.mbi.curator.webutils.session.SessionManager;
import edu.ucla.mbi.curator.webutils.model.ParticipantModel;
import edu.ucla.mbi.curator.actions.curator.initialization.InitParticipantModel;
import edu.ucla.mbi.curator.actions.curator.urlProcessor.GetParticipant;
import edu.ucla.mbi.xml.MIF.elements.interactionElements.Participant;
import edu.ucla.mbi.xml.MIF.elements.interactionElements.ParticipantBuilder;
import edu.ucla.mbi.xml.MIF.elements.interactionElements.Interactable;
import edu.ucla.mbi.xml.MIF.elements.adminElements.Names;
import edu.ucla.mbi.xml.MIF.elements.adminElements.Label;
import edu.ucla.mbi.xml.MIF.elements.adminElements.FullName;
import edu.ucla.mbi.xml.MIF.elements.referencing.InternalReference;
import edu.ucla.mbi.xml.MIF.elements.referencing.Referent;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: Nov 28, 2005
 * Time: 4:15:39 PM
 */
public class UpdateParticipant extends Action {

    Log log = LogFactory.getLog(UpdateParticipant.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ParticipantBuilder participantBuilder = SessionManager.getSessionManager(request).getFileFactory().getParticipantBuilder();
        new GetParticipant().execute(mapping, form, request, response);
        participantBuilder.setParticipant((Participant) request.getAttribute("participant"));
        ParticipantForm participantForm = (ParticipantForm) form;
        if (participantForm.getShortName() != null) {
            participantBuilder.setShortName(participantForm.getShortName().trim());
        }
        if (participantForm.getFullName() != null) {
            participantBuilder.setFullName(participantForm.getFullName().trim());
        }
        return mapping.findForward("success");
    }
}
