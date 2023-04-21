package edu.ucla.mbi.curator.webutils.model;

import edu.ucla.mbi.xml.MIF.elements.interactionElements.Participant;
import edu.ucla.mbi.xml.MIF.elements.interactionElements.ParticipantBuilder;
import edu.ucla.mbi.xml.MIF.elements.interactionElements.Interactor;
import edu.ucla.mbi.xml.MIF.elements.interactionElements.Interactable;
import edu.ucla.mbi.xml.MIF.elements.referencing.InternalReferenceFactory;
import edu.ucla.mbi.xml.MIF.elements.referencing.Referent;
import edu.ucla.mbi.xml.MIF.elements.controlledVocabularies.MolecularInteractions.ParticipantIdentificationMethod;
import edu.ucla.mbi.xml.MIF.elements.controlledVocabularies.MolecularInteractions.BiologicalRole;
import edu.ucla.mbi.xml.MIF.elements.controlledVocabularies.MolecularInteractions.ExperimentalPreparation;
import edu.ucla.mbi.xml.MIF.elements.controlledVocabularies.MolecularInteractions.ExperimentalRole;
import edu.ucla.mbi.xml.MIF.elements.topLevelElements.Interaction;
import edu.ucla.mbi.xml.MIF.elements.topLevelElements.Experiment;
import edu.ucla.mbi.xml.MIF.elements.topLevelElements.ExperimentBuilder;
import edu.ucla.mbi.curator.webutils.model.validation.SpecificationCompliance;
import edu.ucla.mbi.curator.webutils.model.validation.ValidationError;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: Nov 28, 2005
 * Time: 3:27:17 PM
 */
public class ParticipantModel implements SpecificationCompliance {

    private ParticipantBuilder participantBuilder;

    private ParticipantBuilder tempBuilder = new ParticipantBuilder(new InternalReferenceFactory());

    private ArrayList<Participant> participants = new ArrayList<Participant>();

    private ArrayList<ArrayList<Participant>> candidates = new ArrayList<ArrayList<Participant>>();

    private InteractorModel interactorModel;

    private InteractionModel interactionModel;

    /**
     * disable...
     */
    private ParticipantModel() {
    }

    private ParticipantModel(InteractorModel interactorModel) {
        this.interactorModel = interactorModel;
    }

    /**
     * require use of this constructor...
     * @param numParticipants
     */
    public ParticipantModel(int numParticipants, InteractorModel interactorModel) {
        this(interactorModel);
        this.participantBuilder = interactorModel.getParticipantBuilder();
        for (int i = 0; i < numParticipants; i++) {
            participantBuilder.createParticipant();
            participants.add(participantBuilder.getParticipant());
        }
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public ArrayList<ArrayList<Participant>> getCandidates() {
        return candidates;
    }

    public int size() {
        return participants.size();
    }

    public void setInteractionModel(InteractionModel interactionModel) {
        this.interactionModel = interactionModel;
    }

    public Participant getParticipantByInternalId(int internalId) {
        for (Participant p : participants) {
            if (p.getInternalReference().getReference() == internalId) return p;
        }
        return null;
    }

    public Participant getCandidate(int majorIndex, int minorIndex) {
        return candidates.get(majorIndex).get(minorIndex);
    }

    public Participant getParticipantByString(String participantString) {
        for (Participant p : participants) {
            if (p.toString().equals(participantString)) return p;
        }
        return null;
    }

    public void deleteParticipantByInternalId(Integer participantId) {
        Participant p = getParticipantByInternalId(participantId);
        if (p != null) {
            int index = participants.indexOf(p);
            participants.remove(index);
            Interactable interactable;
            if ((interactable = p.getInteractable()) != null && interactable instanceof Interactor) {
                Interactor interactor = (Interactor) interactable;
                if (interactor.getParent().equals(p)) interactor.setBelongsTo(null);
                for (Participant participant : participants) {
                    if (!participant.equals(p)) {
                        if (p.getInteractable().equals(interactor)) {
                            interactor.setBelongsTo(participant);
                            break;
                        }
                    }
                }
            }
            if (p.getParent() != null) {
                Interaction ixn = (Interaction) p.getParent();
                ixn.getParticipantAndInternalReferenceList().remove(p);
                ixn.getParticipantAndInternalReferenceList().remove(p.getInternalReference());
            }
            participantBuilder.deregister(p);
        }
    }

    public ArrayList<? extends Referent> doNotMeetSpecification() {
        return new ArrayList<Participant>();
    }

    public ArrayList<String> findErrors(Referent referent) {
        return new ArrayList<String>();
    }

    public boolean hasValidReferent() {
        for (Participant participant : getParticipants()) if (meetsSpecification(participant)) return true;
        return false;
    }

    public ArrayList<ValidationError> validationErrors(Referent referent, Integer validationMode) {
        Participant participant = (Participant) referent;
        ParticipantBuilder participantBuilder = new ParticipantBuilder(null);
        participantBuilder.setParticipant(participant);
        ArrayList<ValidationError> errors = new ArrayList<ValidationError>();
        switch(validationMode) {
            case SpecificationCompliance.DIP_VALIDATION:
                {
                    if (participant.getInteractor() == null) errors.add(new ValidationError("Participant contains no interactor", SpecificationCompliance.MIF_VALIDATION));
                    return errors;
                }
            case SpecificationCompliance.IMEX_VALIDATION:
                {
                    return errors;
                }
            case SpecificationCompliance.MIF_VALIDATION:
                {
                    return errors;
                }
        }
        return errors;
    }

    public boolean meetsSpecification(Referent referent) {
        Interactable i = ((Participant) referent).getInteractable();
        if (i instanceof Interactor) return interactorModel.meetsSpecification((Interactor) i);
        return false;
    }

    public int getSize() {
        return size();
    }

    public Participant addDecoratedInteractor(Interactor interactor) {
        Participant participant = participantBuilder.decorateInteractor(interactor);
        participants.add(participant);
        return participant;
    }

    public ArrayList<Participant> getParticipantsContainingInteractorId(int interactorId) {
        ArrayList<Participant> ppts = new ArrayList<Participant>();
        for (Participant p : participants) if (p.getInteractable().getInternalReference().getReference() == interactorId) ppts.add(p);
        return ppts;
    }

    public Participant decorateInteractorWithId(int interactorId) {
        Interactor i = (Interactor) participantBuilder.getInternalReferenceFactory().readFromRegister(interactorId);
        return participantBuilder.decorateInteractor(i);
    }

    public void addIDMethod(Participant participant, ParticipantIdentificationMethod.ParticipantIdentificationMethodTerm participantIdentificationMethodTerm) {
        participantBuilder.setParticipant(participant);
        participantBuilder.addParticipantIDMethod(participantIdentificationMethodTerm);
    }

    public void removeIDMethod(Participant participant, ParticipantIdentificationMethod.ParticipantIdentificationMethodTerm participanIdentificationMethodTerm) {
        participantBuilder.setParticipant(participant);
        participantBuilder.removeParticipantIDMethod(participanIdentificationMethodTerm);
    }

    public void setBiologicalRole(Participant participant, BiologicalRole.BiologicalRoleTerm biologicalRoleTerm) {
        participantBuilder.setParticipant(participant);
        participantBuilder.setBiologicalRole(biologicalRoleTerm);
    }

    public void addParticipant(Participant participant) {
        if (!participants.contains(participant)) participants.add(participant);
    }

    public void removeExptlPrep(Participant participant, ExperimentalPreparation.ExperimentalPreparationTerm experimentalPreparationTerm) {
        participantBuilder.setParticipant(participant);
        participantBuilder.removeExptlPreparationTerm(experimentalPreparationTerm);
    }

    public void addExptlPrep(Participant participant, ExperimentalPreparation.ExperimentalPreparationTerm experimentalPreparationTerm) {
        participantBuilder.setParticipant(participant);
        participantBuilder.addExptlPreparationTerm(experimentalPreparationTerm);
    }

    public void removeExptlRole(Participant participant, ExperimentalRole.ExperimentalRoleTerm experimentalRoleTerm) {
        participantBuilder.setParticipant(participant);
        participantBuilder.removeExptlRoleTerm(experimentalRoleTerm);
    }

    public void addExptlRole(Participant participant, ExperimentalRole.ExperimentalRoleTerm experimentalRoleTerm) {
        participantBuilder.setParticipant(participant);
        participantBuilder.addExptlRoleTerm(experimentalRoleTerm);
    }
}
