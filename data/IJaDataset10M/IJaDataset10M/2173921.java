package edu.ucla.mbi.curator.actions.curator.ajax;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.ucla.mbi.xml.MIF.elements.interactionElements.*;
import edu.ucla.mbi.xml.MIF.elements.controlledVocabularies.MolecularInteractions.*;
import edu.ucla.mbi.xml.MIF.elements.controlledVocabularies.ControlledVocabularyTerm;
import edu.ucla.mbi.xml.MIF.elements.topLevelElements.Interaction;
import edu.ucla.mbi.xml.MIF.elements.topLevelElements.Experiment;
import edu.ucla.mbi.xml.MIF.elements.topLevelElements.ExperimentBuilder;
import edu.ucla.mbi.xml.MIF.elements.FeatureDescriptionElements.Feature;
import edu.ucla.mbi.xml.MIF.elements.FeatureDescriptionElements.FeatureBuilder;
import edu.ucla.mbi.curator.webutils.session.SessionManager;
import edu.ucla.mbi.curator.webutils.model.InteractorModel;
import edu.ucla.mbi.curator.webutils.model.InteractionModel;
import edu.ucla.mbi.curator.webutils.model.ParticipantModel;
import edu.ucla.mbi.curator.webutils.model.ExperimentModel;
import edu.ucla.mbi.curator.actions.curator.urlProcessor.GetParticipant;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.util.Enumeration;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: Dec 21, 2005
 * Time: 11:03:18 AM
 */
public class AddElementToElement extends Action {

    private Log log = LogFactory.getLog(AddElementToElement.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionManager sessionManager = SessionManager.getSessionManager(request);
        String element = request.getParameter("element");
        if (element == null) element = (String) request.getAttribute("element");
        String useRoot = request.getParameter("useRoot");
        if (useRoot == null) useRoot = (String) request.getAttribute("useRoot");
        String termId = request.getParameter("termId");
        if (termId == null) termId = (String) request.getAttribute("termId");
        String lastTermId = request.getParameter("lastTermId");
        assert element != null && element.length() > 0;
        String index = request.getParameter("index");
        new GetParticipant().execute(mapping, form, request, response);
        ParticipantModel participantModel = SessionManager.getParticipantModel(mapping, form, request, response);
        Integer participantId = (Integer) request.getAttribute("participantId");
        Participant participant = (Participant) request.getAttribute("participant");
        if (participantId != null) {
            ParticipantBuilder participantBuilder = sessionManager.getFileFactory().getParticipantBuilder();
            if (participant == null) participant = participantModel.getParticipantByInternalId(participantId);
            participantBuilder.setParticipant(participant);
            if (element.equals("cvTerm")) {
                ControlledVocabularyTerm cvTerm = MIVocabTree.getInstance().findByTermId(termId);
                String responseString = "";
                boolean exptlRole = useRoot != null && useRoot.equalsIgnoreCase("experimentalRole");
                if (cvTerm instanceof BiologicalRole.BiologicalRoleTerm && !exptlRole) {
                    participantModel.setBiologicalRole(participant, (BiologicalRole.BiologicalRoleTerm) cvTerm);
                    responseString = getBiologicalRoleResponse(participant, request, index);
                } else if (cvTerm instanceof ExperimentalRole.ExperimentalRoleTerm || exptlRole) {
                    ExperimentalRoleList erList = participant.getExperimentalRoleList();
                    if (!(cvTerm instanceof ExperimentalRole.ExperimentalRoleTerm)) {
                        Enumeration<ExperimentalRole.ExperimentalRoleTerm> e = MIVocabTree.getExptlRoleRoot().depthFirstEnumeration();
                        while (e.hasMoreElements()) {
                            ExperimentalRole.ExperimentalRoleTerm erTerm = e.nextElement();
                            if (erTerm.getTermId().equalsIgnoreCase(termId)) {
                                if (erList.contains(erTerm)) erList.remove(erTerm); else erList.add((ExperimentalRole.ExperimentalRoleTerm) erTerm.shallowClone());
                                cvTerm = (ControlledVocabularyTerm) erTerm.shallowClone();
                                break;
                            }
                        }
                    } else {
                        if (erList.contains(cvTerm)) erList.remove(cvTerm); else erList.add(((ExperimentalRole.ExperimentalRoleTerm) cvTerm).shallowClone());
                    }
                    responseString = getExperimentalRoleResponse(participant, request, index);
                } else if (cvTerm instanceof ParticipantIdentificationMethod.ParticipantIdentificationMethodTerm) {
                    ParticipantIdentificationMethodList pidmList = participant.getParticipantIDMethodList();
                    if (!pidmList.contains(cvTerm)) pidmList.add((ParticipantIdentificationMethod.ParticipantIdentificationMethodTerm) cvTerm); else pidmList.remove(cvTerm);
                    responseString = getParticipantIDMethodResponse(participant, request, index);
                } else if (cvTerm instanceof ExperimentalPreparation.ExperimentalPreparationTerm) {
                    ExperimentalPreparationList epList = participant.getExperimentalPreparationList();
                    if (!epList.contains(cvTerm)) epList.add((ExperimentalPreparation.ExperimentalPreparationTerm) cvTerm); else epList.remove(cvTerm);
                    responseString = getExperimentalPreparationResponse(participant, request, index);
                } else if (useRoot != null) {
                    if (useRoot.equalsIgnoreCase("participantIdentificationMethod")) {
                        Enumeration<ParticipantIdentificationMethod.ParticipantIdentificationMethodTerm> e = MIVocabTree.getParticipantIDMethodRoot().depthFirstEnumeration();
                        while (e.hasMoreElements()) {
                            ParticipantIdentificationMethod.ParticipantIdentificationMethodTerm pidmTerm = e.nextElement();
                            if (pidmTerm.getTermId().equalsIgnoreCase(termId)) {
                                cvTerm = pidmTerm.shallowClone();
                                ParticipantIdentificationMethodList pidmList = participant.getParticipantIDMethodList();
                                if (pidmList.contains(pidmTerm)) pidmList.remove(pidmTerm); else pidmList.add(pidmTerm.shallowClone());
                                break;
                            }
                        }
                        responseString = getParticipantIDMethodResponse(participant, request, index);
                    } else if (useRoot.equalsIgnoreCase("experimentPreparation")) {
                        Enumeration<ExperimentalPreparation.ExperimentalPreparationTerm> e = MIVocabTree.getExptlPreparationRoot().depthFirstEnumeration();
                        while (e.hasMoreElements()) {
                            ExperimentalPreparation.ExperimentalPreparationTerm epTerm = e.nextElement();
                            if (epTerm.getTermId().equalsIgnoreCase(termId)) {
                                cvTerm = epTerm.shallowClone();
                                ExperimentalPreparationList epList = participant.getExperimentalPreparationList();
                                if (epList.contains(epTerm)) epList.remove(epTerm); else epList.add(epTerm.shallowClone());
                                break;
                            }
                        }
                        responseString = getExperimentalPreparationResponse(participant, request, index);
                    }
                } else throw new ClassCastException("You're trying to add something weird to Participant.");
                sendResponse(response, responseString);
                return null;
            }
        }
        Integer interactorId;
        try {
            interactorId = Integer.valueOf(request.getParameter("interactorId"));
        } catch (NumberFormatException nfe) {
            interactorId = null;
        }
        Interactor interactor = null;
        if (interactorId == null) {
            interactorId = (Integer) request.getAttribute("interactorId");
            if (interactorId == null) {
                interactor = (Interactor) request.getAttribute("interactor");
                if (interactor != null) interactorId = interactor.getInternalReference().getReference();
            }
        }
        if (interactorId != null) {
            InteractorModel interactorModel = SessionManager.getInteractorModel(mapping, form, request, response);
            if (interactor == null) interactor = interactorModel.getInteractorByInternalId(interactorId);
            if (element.equals("attribute")) {
                request.setAttribute("parentElement", interactor);
                return mapping.findForward("addAttributes");
            } else if (element.equals("cvTerm")) {
                ControlledVocabularyTerm cvTerm = MIVocabTree.getInstance().findByTermId(termId);
                if (cvTerm instanceof InteractorType.InteractorTypeTerm) interactor.setInteractorType((InteractorType.InteractorTypeTerm) cvTerm);
                if (request.getAttribute("sendResponse") == null || (Boolean) request.getAttribute("sendResponse")) {
                    String r = "\n<response><elementUpdate>\n" + "\t<parentName>interactorId</parentName>\n" + "\t<parentId>" + interactorId + "</parentId>\n" + "\t<lastTermId>" + lastTermId + "</lastTermId>\n" + "\t<hiddenField>interactorTypeTermId</hiddenField>\n" + "\t<index>" + index + "</index>" + "\t<list>\n" + "\t\t<listTitle>" + getResources(request).getMessage("vocabBrowser.label.current") + " " + getResources(request).getMessage("vocabBrowser.label.interactortype") + "</listTitle>\n";
                    if (cvTerm != null) r += "\t<listItem>" + "<term>" + cvTerm.getTerm() + "</term>" + "<termId>" + cvTerm.getTermId() + "</termId>" + "</listItem>\n"; else r += "\t<listItem><term>" + getResources(request).getMessage("none") + "</term><termId>null</termId></listItem>";
                    r += "\t</list>\n" + "</elementUpdate></response>";
                    sendResponse(response, r);
                }
                return null;
            }
        }
        Integer interactionId;
        try {
            interactionId = Integer.valueOf(request.getParameter("interactionId"));
        } catch (NumberFormatException nfe) {
            interactionId = null;
        }
        Interaction interaction = null;
        if (interactionId == null) {
            interactionId = (Integer) request.getAttribute("interactionId");
            if (interactionId == null) {
                interaction = (Interaction) request.getAttribute("interaction");
                if (interaction != null) interactionId = interaction.getInternalReference().getReference();
            }
        }
        if (interactionId != null) {
            InteractionModel interactionModel = sessionManager.getInteractionModel();
            if (interaction == null) interaction = interactionModel.getInteractionByInternalId(interactionId);
            if (element.equals("cvTerm")) {
                ControlledVocabularyTerm cvTerm = MIVocabTree.getInstance().findByTermId(termId);
                if (cvTerm instanceof InteractionType.InteractionTypeTerm) {
                    interactionModel.setInteractionType(interaction, (InteractionType.InteractionTypeTerm) cvTerm);
                    if (request.getAttribute("sendResponse") == null || (Boolean) request.getAttribute("sendResponse")) {
                        String r = "<response><elementUpdate>\n" + "\t<parentName>interactionId</parentName>\n" + "\t<parentId>" + interactionId + "</parentId>\n" + "\t<lastTermId>" + lastTermId + "</lastTermId>\n" + "\t<hiddenField>interactionTypeTermId</hiddenField>\n" + "\t<list>\n" + "\t\t<listTitle>" + getResources(request).getMessage("vocabBrowser.label.current") + " " + getResources(request).getMessage("vocabBrowser.label.interactiontype") + "</listTitle>\n";
                        if (cvTerm != null) r += "\t<listItem>" + "<term>" + interaction.getInteractionType().getTerm() + "</term>" + "<termId>" + interaction.getInteractionType().getTermId() + "</termId>" + "</listItem>\n"; else r += "\t<listItem><term>" + getResources(request).getMessage("none") + "</term><termId>null</termId></listItem>";
                        r += "\t<index>" + index + "</index>\t</list></elementUpdate></response>";
                        sendResponse(response, r);
                    }
                    return null;
                }
            }
        }
        Integer experimentId;
        try {
            experimentId = Integer.valueOf(request.getParameter("experimentId"));
        } catch (NumberFormatException nfe) {
            experimentId = null;
        }
        if (experimentId != null) {
            String responseString = "";
            ExperimentModel experimentModel = sessionManager.getExperimentModel();
            Experiment experiment = experimentModel.getExperimentByInternalId(experimentId);
            ExperimentBuilder experimentBuilder = sessionManager.getFileFactory().getExperimentBuilder();
            experimentBuilder.setExperiment(experiment);
            if (element.equals("cvTerm")) {
                ControlledVocabularyTerm cvTerm = MIVocabTree.getInstance().findByTermId(termId);
                boolean usePidmTerm = useRoot != null && useRoot.equalsIgnoreCase("participantIdentificationMethod");
                boolean useIdmTerm = useRoot != null && useRoot.equalsIgnoreCase("interactionDetectionMethod");
                boolean useFdmTerm = useRoot != null && useRoot.equalsIgnoreCase("featureDetectionMethod");
                if (cvTerm instanceof ParticipantIdentificationMethod.ParticipantIdentificationMethodTerm) {
                    if (usePidmTerm) {
                        experimentBuilder.setParticipantIDMethod((ParticipantIdentificationMethod.ParticipantIdentificationMethodTerm) cvTerm);
                        responseString = getParticipantIDMethodResponse(experiment, request, index);
                    } else if (useIdmTerm) {
                        Enumeration<InteractionDetectionMethod.InteractionDetectionMethodTerm> e = MIVocabTree.getInteractionDetectionMethodRoot().depthFirstEnumeration();
                        while (e.hasMoreElements()) {
                            InteractionDetectionMethod.InteractionDetectionMethodTerm idmTerm = e.nextElement();
                            if (cvTerm.getTermId().equalsIgnoreCase(termId)) {
                                experimentBuilder.setInteractionDetectionMethod(idmTerm);
                                break;
                            }
                        }
                        responseString = getInteractionDetectionMethodResponse(experiment, request, index);
                    } else if (useFdmTerm) {
                        Enumeration<FeatureDetectionMethod.FeatureDetectionMethodTerm> e = MIVocabTree.getFeatureDetectionMethodRoot().depthFirstEnumeration();
                        while (e.hasMoreElements()) {
                            FeatureDetectionMethod.FeatureDetectionMethodTerm fdmTerm = e.nextElement();
                            if (cvTerm.getTermId().equalsIgnoreCase(termId)) {
                                experimentBuilder.setFeatureDetectionMethod(fdmTerm);
                                break;
                            }
                        }
                        responseString = getFeatureDetectionMethodResponse(experiment, request, index);
                    }
                    sendResponse(response, responseString);
                    return null;
                } else if (cvTerm instanceof InteractionDetectionMethod.InteractionDetectionMethodTerm || useIdmTerm) {
                    if (useIdmTerm) {
                        if (cvTerm instanceof InteractionDetectionMethod.InteractionDetectionMethodTerm) experimentBuilder.setInteractionDetectionMethod((InteractionDetectionMethod.InteractionDetectionMethodTerm) cvTerm); else {
                            Enumeration<InteractionDetectionMethod.InteractionDetectionMethodTerm> e = MIVocabTree.getInteractionDetectionMethodRoot().depthFirstEnumeration();
                            while (e.hasMoreElements()) {
                                InteractionDetectionMethod.InteractionDetectionMethodTerm idmTerm = e.nextElement();
                                if (cvTerm.getTermId().equalsIgnoreCase(idmTerm.getTermId())) {
                                    cvTerm = idmTerm;
                                    experimentBuilder.setInteractionDetectionMethod(idmTerm);
                                    break;
                                }
                            }
                        }
                        responseString = getInteractionDetectionMethodResponse(experiment, request, index);
                    } else if (useFdmTerm) {
                        Enumeration<FeatureDetectionMethod.FeatureDetectionMethodTerm> e = MIVocabTree.getFeatureDetectionMethodRoot().depthFirstEnumeration();
                        while (e.hasMoreElements()) {
                            FeatureDetectionMethod.FeatureDetectionMethodTerm fdmTerm = e.nextElement();
                            if (cvTerm.getTermId().equalsIgnoreCase(termId)) {
                                experimentBuilder.setFeatureDetectionMethod(fdmTerm);
                                break;
                            }
                        }
                        responseString = getFeatureDetectionMethodResponse(experiment, request, index);
                    } else if (usePidmTerm) {
                        Enumeration<ParticipantIdentificationMethod.ParticipantIdentificationMethodTerm> e = MIVocabTree.getParticipantIDMethodRoot().depthFirstEnumeration();
                        while (e.hasMoreElements()) {
                            ParticipantIdentificationMethod.ParticipantIdentificationMethodTerm pidmTerm = e.nextElement();
                            if (cvTerm.getTermId().equalsIgnoreCase(termId)) {
                                experimentBuilder.setParticipantIDMethod(pidmTerm.shallowClone());
                                break;
                            }
                        }
                        responseString = getParticipantIDMethodResponse(experiment, request, index);
                    }
                    sendResponse(response, responseString);
                    return null;
                } else if (cvTerm instanceof FeatureDetectionMethod.FeatureDetectionMethodTerm) {
                    if (useFdmTerm) {
                        experimentBuilder.setFeatureDetectionMethod((FeatureDetectionMethod.FeatureDetectionMethodTerm) cvTerm);
                        responseString = getFeatureDetectionMethodResponse(experiment, request, index);
                    } else if (usePidmTerm) {
                        Enumeration<ParticipantIdentificationMethod.ParticipantIdentificationMethodTerm> e = MIVocabTree.getParticipantIDMethodRoot().depthFirstEnumeration();
                        while (e.hasMoreElements()) {
                            ParticipantIdentificationMethod.ParticipantIdentificationMethodTerm pidmTerm = e.nextElement();
                            if (cvTerm.getTermId().equalsIgnoreCase(termId)) {
                                experimentBuilder.setParticipantIDMethod(pidmTerm.shallowClone());
                                break;
                            }
                        }
                        responseString = getParticipantIDMethodResponse(experiment, request, index);
                    } else if (useIdmTerm) {
                        Enumeration<InteractionDetectionMethod.InteractionDetectionMethodTerm> e = MIVocabTree.getInteractionDetectionMethodRoot().depthFirstEnumeration();
                        while (e.hasMoreElements()) {
                            InteractionDetectionMethod.InteractionDetectionMethodTerm idmTerm = e.nextElement();
                            if (cvTerm.getTermId().equalsIgnoreCase(termId)) {
                                experimentBuilder.setInteractionDetectionMethod(idmTerm);
                                break;
                            }
                        }
                        responseString = getInteractionDetectionMethodResponse(experiment, request, index);
                    }
                    sendResponse(response, responseString);
                    return null;
                }
                return mapping.findForward("experiment");
            }
        }
        Integer featureId;
        try {
            featureId = Integer.valueOf(request.getParameter("featureId"));
        } catch (NumberFormatException nfe) {
            featureId = null;
        }
        Feature feature = null;
        if (feature == null) {
            if (featureId == null) featureId = (Integer) request.getAttribute("featureId");
            if (featureId == null) {
                feature = (Feature) request.getAttribute("feature");
                if (feature != null && featureId == null) featureId = feature.getInternalReference().getReference();
            }
        }
        if (featureId != null) {
            String responseString = null;
            Feature myFeature = SessionManager.getSessionManager(request).getFeatureModel().getFeatureByInternalId(Integer.valueOf(request.getParameter("featureId")));
            FeatureBuilder featureBuilder = sessionManager.getFileFactory().getFeatureBuilder();
            featureBuilder.setFeature(myFeature);
            if (element.equals("cvTerm")) {
                ControlledVocabularyTerm cvTerm = MIVocabTree.getInstance().findByTermId(termId);
                if (cvTerm instanceof FeatureDetectionMethod.FeatureDetectionMethodTerm) {
                    featureBuilder.setFeatureDetectionMethod((FeatureDetectionMethod.FeatureDetectionMethodTerm) cvTerm);
                    responseString = getFeatureDetectionMethodResponse(featureBuilder.getFeature(), request, index);
                } else if (cvTerm instanceof FeatureType.FeatureTypeTerm) {
                    featureBuilder.setFeatureType((FeatureType.FeatureTypeTerm) cvTerm);
                    responseString = getFeatureTypeTermResponse(featureBuilder.getFeature(), request, request.getParameter("index"));
                } else if (useRoot != null && useRoot.equalsIgnoreCase("featureDetectionMethod")) {
                    Enumeration<FeatureDetectionMethod.FeatureDetectionMethodTerm> e = MIVocabTree.getFeatureDetectionMethodRoot().depthFirstEnumeration();
                    while (e.hasMoreElements()) {
                        FeatureDetectionMethod.FeatureDetectionMethodTerm fdmTerm = e.nextElement();
                        if (fdmTerm.getTermId().equalsIgnoreCase(cvTerm.getTermId())) {
                            cvTerm = fdmTerm;
                            break;
                        }
                    }
                    featureBuilder.setFeatureDetectionMethod((FeatureDetectionMethod.FeatureDetectionMethodTerm) cvTerm);
                    responseString = getFeatureDetectionMethodResponse(featureBuilder.getFeature(), request, request.getParameter("index"));
                } else if (useRoot != null && useRoot.equalsIgnoreCase("featureType")) {
                    Enumeration<FeatureType.FeatureTypeTerm> e = MIVocabTree.getFeatureTypeRoot().depthFirstEnumeration();
                    while (e.hasMoreElements()) {
                        FeatureType.FeatureTypeTerm ftTerm = e.nextElement();
                        if (ftTerm.getTermId().equalsIgnoreCase(cvTerm.getTermId())) {
                            cvTerm = ftTerm;
                            break;
                        }
                    }
                    featureBuilder.setFeatureType((FeatureType.FeatureTypeTerm) cvTerm);
                    responseString = getFeatureTypeTermResponse(featureBuilder.getFeature(), request, request.getParameter("index"));
                }
                sendResponse(response, responseString);
                return null;
            }
        }
        return null;
    }

    private String getFeatureTypeTermResponse(Feature feature, HttpServletRequest request, String index) {
        String responseString = "<response><elementUpdate>\n" + "\t<parentName>participantId</parentName>\n" + "\t<parentId>" + feature.getInternalReference().getReference() + "</parentId>\n" + "\t<index>" + index + "</index>" + "\t<list>\n" + "\t\t<listTitle>" + getResources(request).getMessage("vocabBrowser.label.current") + " " + getResources(request).getMessage("vocabBrowser.label.featuretype") + "</listTitle>\n";
        FeatureType.FeatureTypeTerm ftTerm = feature.getFeatureType();
        String ftt = (ftTerm != null) ? ftTerm.getTerm() : getResources(request).getMessage("none");
        if (ftTerm != null) responseString += "\t<listItem>" + "<term>" + ftTerm.getTerm() + "</term>" + "<termId>" + ftTerm.getTermId() + "</termId>" + "</listItem>\n"; else responseString += "\t<listItem><term>" + getResources(request).getMessage("none") + "</term><termId>null</termId></listItem>";
        responseString += "\n\t</list>\n</elementUpdate>\n</response>\n";
        return responseString;
    }

    private String getInteractionDetectionMethodResponse(Experiment experiment, HttpServletRequest request, String index) {
        String responseString = "<response><elementUpdate>\n" + "\t<parentName>participantId</parentName>\n" + "\t<index>" + index + "</index>" + "\t<parentId>" + experiment.getInternalReference().getReference() + "</parentId>\n" + "\t<list>\n" + "\t\t<listTitle>" + getResources(request).getMessage("vocabBrowser.label.current") + " " + getResources(request).getMessage("vocabBrowser.label.interactiondetectionmethod") + "</listTitle>\n";
        ArrayList<InteractionDetectionMethod.InteractionDetectionMethodTerm> idmList = experiment.getInteractionDetectionMethodList();
        for (InteractionDetectionMethod.InteractionDetectionMethodTerm idmTerm : idmList) responseString += "\t\t<listItem>" + "<term>" + idmTerm.getTerm() + "</term>" + "<termId>" + idmTerm.getTermId() + "</termId>" + "</listItem>\n";
        if (idmList.size() == 0) responseString += "\t\t<listItem>" + "<term>" + getResources(request).getMessage("none") + "</term>" + "<termId>null</termId>" + "</listItem>";
        responseString += "\t</list>\n</elementUpdate></response>\n";
        return responseString;
    }

    private String getParticipantIDMethodResponse(Experiment experiment, HttpServletRequest request, String index) {
        String responseString = "<response><elementUpdate>\n" + "\t<parentName>participantId</parentName>\n" + "\t<parentId>" + experiment.getInternalReference().getReference() + "</parentId>\n" + "\t<index>" + index + "</index>" + "\t<list>\n" + "\t\t<listTitle>" + getResources(request).getMessage("vocabBrowser.label.current") + " " + getResources(request).getMessage("vocabBrowser.label.participantidentificationmethod") + "</listTitle>\n";
        ArrayList<ParticipantIdentificationMethod.ParticipantIdentificationMethodTerm> pidmList = experiment.getParticipantIdentificationMethodList();
        for (ParticipantIdentificationMethod.ParticipantIdentificationMethodTerm pidmTerm : pidmList) {
            responseString += "\t\t<listItem>" + "<term>" + pidmTerm.getTerm() + "</term>" + "<termId>" + pidmTerm.getTermId() + "</termId>" + "</listItem>\n";
        }
        if (pidmList.size() == 0) responseString += "\t\t<listItem><term>" + getResources(request).getMessage("none") + "</term><termId>null</termId></listItem>";
        responseString += "\t</list>\n</elementUpdate><multiValued/></response>\n";
        return responseString;
    }

    private String getFeatureDetectionMethodResponse(Experiment experiment, HttpServletRequest request, String index) {
        String responseString = "<response><elementUpdate>\n" + "\t<parentName>participantId</parentName>\n" + "\t<parentId>" + experiment.getInternalReference().getReference() + "</parentId>\n" + "\t<index>" + index + "</index>" + "\t<list>\n" + "\t\t<listTitle>" + getResources(request).getMessage("vocabBrowser.label.current") + " " + getResources(request).getMessage("vocabBrowser.label.featuredetectionmethod") + "</listTitle>\n";
        ArrayList<FeatureDetectionMethod.FeatureDetectionMethodTerm> fdmList = experiment.getFeatureDetectionMethodList();
        for (FeatureDetectionMethod.FeatureDetectionMethodTerm fdmTerm : fdmList) responseString += "\t\t<listItem>" + fdmTerm.getTerm() + "</listItem>\n";
        if (fdmList.size() == 0) responseString += "\t\t<listItem>" + getResources(request).getMessage("none") + "</listItem>";
        responseString += "\t</list>\n</elementUpdate></response>\n";
        return responseString;
    }

    private String getFeatureDetectionMethodResponse(Feature feature, HttpServletRequest request, String index) {
        String responseString = "<response><elementUpdate>\n" + "\t<parentName>participantId</parentName>\n" + "\t<parentId>" + feature.getInternalReference().getReference() + "</parentId>\n" + "\t<index>" + index + "</index>" + "\t<list>\n" + "\t\t<listTitle>" + getResources(request).getMessage("vocabBrowser.label.current") + " " + getResources(request).getMessage("vocabBrowser.label.featuredetectionmethod") + "</listTitle>\n";
        ArrayList<FeatureDetectionMethod.FeatureDetectionMethodTerm> fdmList = feature.getFeatureDetectionMethodList();
        for (FeatureDetectionMethod.FeatureDetectionMethodTerm fdmTerm : fdmList) responseString += "\t\t<listItem>" + "<term>" + fdmTerm.getTerm() + "</term>" + "<termId>" + fdmTerm.getTermId() + "</termId>" + "</listItem>\n";
        if (fdmList.size() == 0) responseString += "\t\t<listItem><term>" + getResources(request).getMessage("none") + "</term><termId>null</termId></listItem>";
        responseString += "\t</list>\n</elementUpdate></response>\n";
        return responseString;
    }

    private String getParticipantIDMethodResponse(Participant participant, HttpServletRequest request, String index) {
        String responseString = "<response><elementUpdate>\n" + "\t<parentName>participantId</parentName>\n" + "\t<parentId>" + participant.getInternalReference().getReference() + "</parentId>\n" + "\t<index>" + index + "</index>" + "\t<useRoot>participantIdentificationMethod</useRoot>" + "\t<list>\n" + "<listTitle>" + getResources(request).getMessage("vocabBrowser.label.current") + " " + getResources(request).getMessage("vocabBrowser.label.participantidentificationmethod") + "</listTitle>\n";
        ParticipantIdentificationMethodList pidmList = participant.getParticipantIDMethodList();
        for (Object pidmTerm : pidmList) {
            responseString += "\t\t<listItem><term>" + ((ParticipantIdentificationMethod.ParticipantIdentificationMethodTerm) pidmTerm).getTerm() + "</term>" + "<termId>" + ((ParticipantIdentificationMethod.ParticipantIdentificationMethodTerm) pidmTerm).getTermId() + "</termId></listItem>\n";
        }
        if (pidmList.size() == 0) responseString += "\t\t<listItem><term>" + getResources(request).getMessage("none") + "</term>" + "<termId>null</termId></listItem>";
        responseString += "\t</list>\n</elementUpdate><multiValued/></response>\n";
        return responseString;
    }

    private String getExperimentalRoleResponse(Participant participant, HttpServletRequest request, String index) {
        String responseString = "<response><elementUpdate>\n" + "\t<parentName>participantId</parentName>\n" + "\t<parentId>" + participant.getInternalReference().getReference() + "</parentId>\n" + "\t<index>" + index + "</index>" + "\t<useRoot>experimentalRole</useRoot>" + "\t<list>\n" + "<listTitle>" + getResources(request).getMessage("vocabBrowser.label.current") + " " + getResources(request).getMessage("vocabBrowser.label.experimentalrole") + "</listTitle>\n";
        for (Object erTerm : participant.getExperimentalRoleList()) responseString += "\t\t<listItem><term>" + ((ExperimentalRole.ExperimentalRoleTerm) erTerm).getTerm() + "</term>" + "<termId>" + ((ExperimentalRole.ExperimentalRoleTerm) erTerm).getTermId() + "</termId></listItem>\n";
        if (participant.getExperimentalRoleList().size() == 0) responseString += "\t\t<listItem><term>" + getResources(request).getMessage("none") + "</term>" + "<termId>null</termId></listItem>";
        responseString += "\t</list>" + "</elementUpdate><multiValued/></response>";
        return responseString;
    }

    private String getBiologicalRoleResponse(Participant participant, HttpServletRequest request, String index) {
        String responseString = "<response><elementUpdate>\n" + "\t<parentName>participantId</parentName>\n" + "\t<parentId>" + participant.getInternalReference().getReference() + "</parentId>\n" + "\t<index>" + index + "</index>" + "\t<list>\n" + "\t\t<listTitle>" + getResources(request).getMessage("vocabBrowser.label.current") + " " + getResources(request).getMessage("vocabBrowser.label.biologicalrole") + "</listTitle>\n";
        BiologicalRole.BiologicalRoleTerm brTerm = participant.getBiologicalRole();
        if (brTerm != null) responseString += "\t<listItem>" + "<term>" + brTerm.getTerm() + "</term>" + "<termId>" + brTerm.getTermId() + "</termId>" + "</listItem>\n"; else responseString += "\t<listItem><term>" + getResources(request).getMessage("none") + "</term><termId>null</termId></listItem>";
        responseString += "\t</list>" + "</elementUpdate></response>";
        return responseString;
    }

    private String getExperimentalPreparationResponse(Participant participant, HttpServletRequest request, String index) {
        String responseString = "<response><elementUpdate>\n" + "\t<parentName>participantId</parentName>\n" + "\t<parentId>" + participant.getInternalReference().getReference() + "</parentId>\n" + "\t<index>" + index + "</index>" + "\t<useRoot>experimentPreparation</useRoot>" + "\t<list>\n" + "<listTitle>" + getResources(request).getMessage("vocabBrowser.label.current") + " " + getResources(request).getMessage("vocabBrowser.label.experimentalpreparation") + "</listTitle>\n";
        ExperimentalPreparationList epList = participant.getExperimentalPreparationList();
        for (Object epTerm : epList) responseString += "\t\t<listItem><term>" + ((ControlledVocabularyTerm) epTerm).getTerm() + "</term>" + "<termId>" + ((ControlledVocabularyTerm) epTerm).getTermId() + "</termId></listItem>\n";
        if (epList.size() == 0) responseString += "\t\t<listItem>" + getResources(request).getMessage("none") + "</listItem>";
        responseString += "\t</list>\n</elementUpdate><multiValued/></response>\n";
        return responseString;
    }

    private void sendResponse(HttpServletResponse response, String responseString) throws Exception {
        OutputStream out = response.getOutputStream();
        responseString = stripNewlinesAndTabs(responseString);
        StringBufferInputStream in = new StringBufferInputStream(responseString);
        response.setContentType("text/xml;charset=utf-8");
        byte[] buffer = new byte[2048];
        int count = 0;
        while ((count = in.read(buffer)) >= 0) out.write(buffer, 0, count);
        out.close();
    }

    private String stripNewlinesAndTabs(String str) {
        char[] buf = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : buf) {
            if (c != '\t' && c != '\n') sb.append(c);
        }
        return sb.toString();
    }
}
