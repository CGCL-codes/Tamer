package edu.ucla.mbi.curator.actions.curator.file;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.ucla.mbi.xml.parsers.MIFParser;
import edu.ucla.mbi.xml.MIF.elements.topLevelElements.EntrySet;
import edu.ucla.mbi.xml.MIF.elements.topLevelElements.Availability;
import edu.ucla.mbi.xml.MIF.elements.topLevelElements.Experiment;
import edu.ucla.mbi.xml.MIF.elements.topLevelElements.Interaction;
import edu.ucla.mbi.xml.MIF.elements.adminElements.Entry;
import edu.ucla.mbi.xml.MIF.elements.referencing.InternalReferenceFactory;
import edu.ucla.mbi.xml.MIF.elements.referencing.InternalReference;
import edu.ucla.mbi.xml.MIF.elements.referencing.Referent;
import edu.ucla.mbi.xml.MIF.elements.MIFElement;
import edu.ucla.mbi.xml.MIF.elements.FeatureDescriptionElements.Feature;
import edu.ucla.mbi.xml.MIF.elements.interactionElements.Participant;
import edu.ucla.mbi.xml.MIF.elements.interactionElements.Interactor;
import edu.ucla.mbi.curator.webutils.session.SessionManager;
import edu.ucla.mbi.curator.webutils.model.*;
import edu.ucla.mbi.curator.actions.curator.initialization.InitSessionManager;
import edu.ucla.mbi.curator.actions.curator.initialization.RecalculateCurrentState;
import edu.ucla.mbi.curator.actions.curator.model.ReadEntrySetToModel;
import edu.ucla.mbi.curator.actions.curator.model.MixModel;
import edu.ucla.mbi.curator.actions.curator.generic.*;
import edu.ucla.mbi.curator.actions.database.Connect;
import edu.ucla.mbi.curator.actions.user.GetUserFromServlet;
import edu.ucla.mbi.curator.Constants;
import java.io.StringBufferInputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: Apr 19, 2006
 * Time: 10:20:09 AM
 */
public class LoadSavedFile extends Action {

    private Log log = LogFactory.getLog(LoadSavedFile.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        new Connect().execute(mapping, form, request, response);
        Connection dbConnection = (Connection) request.getAttribute("dbConnection");
        Integer pubmedId = Integer.valueOf(request.getParameter("pubmedId"));
        new GetUserFromServlet().execute(mapping, form, request, response);
        Integer curatorId = (Integer) request.getAttribute("curatorId");
        if (curatorId == null || curatorId == -1) return mapping.findForward("undefinedUser");
        Integer curationStatusId = Constants.PRE_SUBMISSION_STATUS;
        String qry = "select file from miffiles " + "where pubmed_id = " + pubmedId + " " + "and curator_id = " + curatorId + " " + "and curation_status = " + curationStatusId;
        Statement statement = dbConnection.createStatement();
        ResultSet fileSet = null;
        try {
            fileSet = statement.executeQuery(qry);
        } catch (SQLException sqlException) {
            log.error("sql error: " + qry);
        }
        assert fileSet != null;
        fileSet.next();
        String file = fileSet.getString("file");
        fileSet.close();
        statement.close();
        dbConnection.close();
        MIFParser mifParser = new MIFParser();
        mifParser.parseDocument(new InputSource(new StringBufferInputStream(file)));
        EntrySet entrySet = mifParser.getEntrySet();
        Entry entry = entrySet.getEntryList().get(0);
        String filePubmedId = mifParser.getFileFactory().getPubmedIdHolder();
        SessionManager.deleteSessionManager(request);
        new InitSessionManager().execute(mapping, form, request, response);
        SessionManager sessionManager = SessionManager.getSessionManager(request);
        if (filePubmedId != null && filePubmedId.trim().length() > 0) {
            sessionManager.setDefaultPubmedId(filePubmedId);
        }
        InternalReferenceFactory internalReferenceFactory = sessionManager.getInternalReferenceFactory();
        sessionManager.resetInternalReferenceFactory();
        ExperimentModel experimentModel = sessionManager.getExperimentModel();
        InteractionModel interactionModel = sessionManager.getInteractionModel();
        ParticipantModel participantModel = sessionManager.getParticipantModel();
        InteractorModel interactorModel = sessionManager.getInteractorModel();
        FeatureModel featureModel = sessionManager.getFeatureModel();
        AvailabilityModel availabilityModel = sessionManager.getAvailabilityModel();
        for (Availability availability : entry.getAvailabilityList()) {
            availabilityModel.addAvailability(availability);
            internalReferenceFactory.writeToRegister(availability.getInternalReference(), availability);
        }
        for (MIFElement mifElement : entry.getExperimentAndInternalReferenceList()) {
            Experiment experiment = null;
            if (mifElement instanceof Experiment) experiment = (Experiment) mifElement; else experiment = (Experiment) ((InternalReference) mifElement).getReferent();
            experimentModel.addExperiment(experiment);
            internalReferenceFactory.writeToRegister(experiment.getInternalReference(), experiment);
        }
        for (Interaction interaction : entry.getInteractionList()) {
            interactionModel.addInteraction(interaction);
            for (Participant participant : interaction.getParticipants()) {
                if (participant.belongsTo() == null) {
                    participant.setBelongsTo(interaction);
                }
                participantModel.addParticipant(participant);
                for (Object feature : participant.getFeatureList()) {
                    featureModel.addFeature(((Feature) feature));
                    internalReferenceFactory.writeToRegister(((Feature) feature).getInternalReference(), (Referent) feature);
                    if (((Feature) feature).belongsTo() == null) {
                        ((Feature) feature).setBelongsTo(participant);
                    }
                }
                internalReferenceFactory.writeToRegister(participant.getInternalReference(), participant);
            }
            internalReferenceFactory.writeToRegister(interaction.getInternalReference(), interaction);
        }
        for (Interactor interactor : entry.getInteractorList()) {
            interactorModel.addInteractor(interactor);
            internalReferenceFactory.writeToRegister(interactor.getInternalReference(), interactor);
        }
        internalReferenceFactory.reindexMaster();
        internalReferenceFactory.recalculateCurrent();
        sessionManager.setEntrySet(entrySet);
        sessionManager.setNextExptIndex(experimentModel.size());
        new RecalculateCurrentState().execute(mapping, form, request, response);
        new ReadEntrySetToModel().execute(mapping, form, request, response);
        new MixModel().execute(mapping, form, request, response);
        new EstablishReferentParentage().execute(mapping, form, request, response);
        new RecheckOrganismNames().execute(mapping, form, request, response);
        new UpdateCVTerms().execute(mapping, form, request, response);
        new DemoteExperimentTerms().execute(mapping, form, request, response);
        return new CheckForPubmedId().execute(mapping, form, request, response);
    }
}
