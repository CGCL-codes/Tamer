package edu.ucla.mbi.xml.MIF;

import edu.ucla.mbi.xml.MIF.elements.adminElements.EntryBuilder;
import edu.ucla.mbi.xml.MIF.elements.adminElements.SourceBuilder;
import edu.ucla.mbi.xml.MIF.elements.FeatureDescriptionElements.FeatureBuilder;
import edu.ucla.mbi.xml.MIF.elements.interactionElements.InferredInteractionBuilder;
import edu.ucla.mbi.xml.MIF.elements.interactionElements.ParticipantBuilder;
import edu.ucla.mbi.xml.MIF.elements.interactionElements.ExperimentalInteractorBuilder;
import edu.ucla.mbi.xml.MIF.elements.interactionElements.InteractorWorker;
import edu.ucla.mbi.xml.MIF.elements.referencing.IMExIdentifierFactory;
import edu.ucla.mbi.xml.MIF.elements.referencing.InternalReferenceFactory;
import edu.ucla.mbi.xml.MIF.elements.topLevelElements.AvailabilityBuilder;
import edu.ucla.mbi.xml.MIF.elements.topLevelElements.EntrySetBuilder;
import edu.ucla.mbi.xml.MIF.elements.topLevelElements.ExperimentBuilder;
import edu.ucla.mbi.xml.MIF.elements.topLevelElements.InteractionBuilder;
import edu.ucla.mbi.xml.MIF.elements.controlledVocabularies.OrganismBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: Nov 18, 2005
 * Time: 2:40:51 PM
 * This class defines a Factory object for the creation & modification of MIF files. All
 * object accessors (i.e. accessors into deeper levels of the Object heirarchy) should be available
 * by accessing the entrySet contained by any particular FileFactory, since it guarantees the
 * cohesive construction of a MIF file and all of its sub-elements.
 */
public class FileFactory {

    /**
     * Note: This internalReferenceFactory will guarantee the uniqueness of any internal references
     * for the file being created by this FileFactory only.
     */
    private IMExIdentifierFactory iMExIdentifierFactory = new IMExIdentifierFactory();

    private InternalReferenceFactory internalReferenceFactory = new InternalReferenceFactory();

    /**
     * These are the ReferentBuilder classes that will generate the various MIF entrySet sub-elements anticipated
     * to be necessary to the proper functioning of this Factory...
     */
    private EntrySetBuilder entrySetBuilder = new EntrySetBuilder();

    private InteractionBuilder interactionBuilder = new InteractionBuilder(internalReferenceFactory);

    private ExperimentBuilder experimentBuilder = new ExperimentBuilder(internalReferenceFactory);

    private ParticipantBuilder participantBuilder = new ParticipantBuilder(internalReferenceFactory);

    private FeatureBuilder featureBuilder = new FeatureBuilder(internalReferenceFactory);

    private InferredInteractionBuilder inferredInteractionBuilder = new InferredInteractionBuilder(internalReferenceFactory);

    private ExperimentalInteractorBuilder experimentalInteractorBuilder = new ExperimentalInteractorBuilder(internalReferenceFactory);

    private AvailabilityBuilder availabilityBuilder = new AvailabilityBuilder(internalReferenceFactory);

    /**
     * These are other necessary Builder classes...
     */
    private EntryBuilder entryBuilder = new EntryBuilder();

    private SourceBuilder sourceBuilder = new SourceBuilder();

    private OrganismBuilder organismBuilder = new OrganismBuilder();

    private InteractorWorker interactorWorker = new InteractorWorker();

    public InternalReferenceFactory getInternalReferenceFactory() {
        return internalReferenceFactory;
    }

    public IMExIdentifierFactory getiMExIdentifierFactory() {
        return iMExIdentifierFactory;
    }

    public EntrySetBuilder getEntrySetBuilder() {
        return entrySetBuilder;
    }

    public InteractionBuilder getInteractionBuilder() {
        return interactionBuilder;
    }

    public ExperimentBuilder getExperimentBuilder() {
        return experimentBuilder;
    }

    public ParticipantBuilder getParticipantBuilder() {
        return participantBuilder;
    }

    public FeatureBuilder getFeatureBuilder() {
        return featureBuilder;
    }

    public InferredInteractionBuilder getInferredInteractionBuilder() {
        return inferredInteractionBuilder;
    }

    public AvailabilityBuilder getAvailabilityBuilder() {
        return availabilityBuilder;
    }

    public EntryBuilder getEntryBuilder() {
        return entryBuilder;
    }

    public SourceBuilder getSourceBuilder() {
        return sourceBuilder;
    }

    public OrganismBuilder getHostOrganismBuilder() {
        return organismBuilder;
    }

    public ExperimentalInteractorBuilder getExperimentalInteractorBuilder() {
        return experimentalInteractorBuilder;
    }

    public InteractorWorker getInteractorWorker() {
        return interactorWorker;
    }

    public void resetInternalReferenceFactory() {
        internalReferenceFactory = new InternalReferenceFactory();
    }

    private String pubmedIdHolder;

    public String getPubmedIdHolder() {
        return pubmedIdHolder;
    }

    public void setPubmedIdHolder(String pubmedIdHolder) {
        this.pubmedIdHolder = pubmedIdHolder;
    }
}
