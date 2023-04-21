package uk.co.ordnancesurvey.confluence.ui.rabbitframe;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObject;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntology;
import uk.co.ordnancesurvey.confluence.IConfluenceEditorKit;
import uk.co.ordnancesurvey.confluence.ui.basemvc.ConfluenceUIModel;
import uk.co.ordnancesurvey.confluence.ui.basemvc.modelevent.IUIModelChangeType;
import uk.co.ordnancesurvey.confluence.ui.basemvc.modelevent.UIModelChangeType;
import uk.co.ordnancesurvey.confluence.ui.listener.ontologychange.BaseOWLEntityAddAndRemoveListener;
import uk.co.ordnancesurvey.confluence.ui.rabbitframe.section.RbtSentencesOfOWLObjectFrameSectionModel;
import uk.co.ordnancesurvey.confluence.ui.rabbitframe.section.factory.RabbitFrameSectionModelFactory;

/**
 * This class contains methods for accessing and updating the model that is
 * shown by a rabbit frame. It is inspired by the OWL frame component in protege
 * 4. This frame model consists of a list of sections.
 * 
 * @author rdenaux
 * @deprecated use {@link RbtFrameModel} instead.
 * 
 */
public class RabbitFrameModel extends ConfluenceUIModel {

    private static final long serialVersionUID = -5828215786476260169L;

    public static final IUIModelChangeType SECTION_ADDED = new UIModelChangeType("SECTION_ADDED");

    public static final IUIModelChangeType SECTION_REMOVED = new UIModelChangeType("SECTION_REMOVED");

    /**
	 * Listens to changes in the ontology. When a new declaration axiom is
	 * detected, it updates the sections to include or remove the added or
	 * deleted OWLEntity.
	 * 
	 * @author rdenaux
	 * 
	 */
    private final class OntologyChangeListener extends BaseOWLEntityAddAndRemoveListener {

        private static final long serialVersionUID = -7372958155293382955L;

        public OntologyChangeListener() {
            super(getConfluenceModelManager());
        }

        @Override
        protected void handleClassAdded(OWLClass aOWLClass) {
            resetSections();
        }

        @Override
        protected void handleClassDeleted(OWLClass aOWLClass) {
            resetSections();
        }

        @Override
        protected void handleObjectPropertyAdded(OWLObjectProperty objProperty) {
            resetSections();
        }

        @Override
        protected void handleObjectPropertyDeleted(OWLObjectProperty objProperty) {
            resetSections();
        }

        /**
		 * @deprecated replace this method with more efficient updates when the //
		 *             detection of deleted entities is fixed.
		 */
        private void resetSections() {
            initSections();
            notifyUpdate(MODEL_REFRESHED);
        }
    }

    private static final Logger log = Logger.getLogger(RabbitFrameModel.class.getName());

    private Map<OWLObject, RbtSentencesOfOWLObjectFrameSectionModel<? extends OWLObject>> sections;

    private RbtSentencesOfOWLObjectFrameSectionModel<OWLOntology> ontologySection;

    private OntologyChangeListener ontologyChangeListener;

    private RabbitFrameSectionModelFactory sectionModelFactory;

    public RabbitFrameModel(IConfluenceEditorKit aEditorKit) {
        super(aEditorKit);
        log.finest("Created " + this);
        sectionModelFactory = RabbitFrameSectionModelFactory.getInstance(getConfluenceEditorKit());
        sections = new HashMap<OWLObject, RbtSentencesOfOWLObjectFrameSectionModel<? extends OWLObject>>();
        initSections();
        ontologyChangeListener = new OntologyChangeListener();
        getConfluenceModelManager().addOntologyChangeListener(ontologyChangeListener);
    }

    /**
	 * Dispose of listeners.
	 * 
	 * @see uk.co.ordnancesurvey.confluence.ui.basemvc.ConfluenceUIModel#dispose()
	 */
    @Override
    public void dispose() {
        super.dispose();
        getConfluenceModelManager().removeOntologyChangeListener(ontologyChangeListener);
    }

    /**
	 * Returns an unmodifiable copy of the sections.
	 * 
	 * @return
	 */
    public Collection<RbtSentencesOfOWLObjectFrameSectionModel<? extends OWLObject>> getSections() {
        return Collections.unmodifiableCollection(sections.values());
    }

    /**
	 * Initialises all section models
	 * 
	 */
    protected void initSections() {
        sections.clear();
        initOntologySection();
        initConceptSections();
        initRelationSections();
    }

    /**
	 * Initialises the sections containing relation related rows.
	 */
    private void initRelationSections() {
        for (OWLOntology ont : getActiveOntologies()) {
            for (OWLObjectProperty objProp : ont.getReferencedObjectProperties()) {
                addSectionModel(createSectionModel(objProp));
            }
        }
    }

    /**
	 * Initialises the sections containing concept related rows.
	 */
    private void initConceptSections() {
        for (OWLOntology ont : getActiveOntologies()) {
            for (OWLClass owlClass : ont.getReferencedClasses()) {
                addSectionModel(createSectionModel(owlClass));
            }
        }
    }

    /**
	 * Initialises the section containing rows related to the active ontology.
	 */
    private void initOntologySection() {
        for (OWLOntology ont : getActiveOntologies()) {
            addSectionModel(createSectionModel(ont));
        }
    }

    /**
	 * Adds aSectionModel to the list of sections in this frame model.
	 * 
	 * @param aSectionModel
	 */
    private void addSectionModel(RbtSentencesOfOWLObjectFrameSectionModel<? extends OWLObject> aSectionModel) {
        if (aSectionModel != null) {
            if (sections.get(aSectionModel.getRootObject()) != null) {
                log.severe("Trying to add an already existing section " + " for entity " + aSectionModel.getRootObject());
            }
            sections.put(aSectionModel.getRootObject(), aSectionModel);
        }
    }

    /**
	 * Creates a section model containing rows related to aOntology. May return
	 * <code>null</code>.
	 */
    protected RbtSentencesOfOWLObjectFrameSectionModel<OWLOntology> createSectionModel(OWLOntology aOntology) {
        RbtSentencesOfOWLObjectFrameSectionModel<OWLOntology> ontSectionModel = sectionModelFactory.getCurrentOntologySectionModel(aOntology);
        return ontSectionModel;
    }

    /**
	 * Creates a section model containing rows related to aOWLClass. May return
	 * <code>null</code>.
	 * 
	 * @param aOWLClass
	 * @return
	 */
    protected RbtSentencesOfOWLObjectFrameSectionModel<OWLClass> createSectionModel(OWLClass aOWLClass) {
        RbtSentencesOfOWLObjectFrameSectionModel<OWLClass> conceptSectionModel = sectionModelFactory.getCurrentOWLClassSectionModel(aOWLClass);
        return conceptSectionModel;
    }

    /**
	 * Creates a section model containing rows related to aOWLIndividual. May
	 * return <code>null</code>.
	 * 
	 * @param aOWLIndividual
	 * @return
	 */
    protected RbtSentencesOfOWLObjectFrameSectionModel<OWLIndividual> createSectionModel(OWLIndividual aOWLIndividual) {
        RbtSentencesOfOWLObjectFrameSectionModel<OWLIndividual> result = sectionModelFactory.getCurrentOWLIndividualSectionModel(aOWLIndividual);
        return result;
    }

    /**
	 * Creates a section model containing rows related to aOWLObjectProperty.
	 * May return <code>null</code>.
	 * 
	 * @param aOWLObjectProperty
	 * @return
	 */
    private RbtSentencesOfOWLObjectFrameSectionModel<OWLObjectProperty> createSectionModel(OWLObjectProperty aOWLObjectProperty) {
        RbtSentencesOfOWLObjectFrameSectionModel<OWLObjectProperty> result = sectionModelFactory.getCurrentOWLObjectPropertySectionModel(aOWLObjectProperty);
        return result;
    }

    /**
	 * Returns the currently active ontology.
	 * 
	 * @return
	 */
    private OWLOntology getActiveOntology() {
        return getConfluenceModelManager().getActiveOntology();
    }

    /**
	 * Returns the currently active ontologies.
	 * 
	 * @return
	 */
    private Set<OWLOntology> getActiveOntologies() {
        return getConfluenceModelManager().getActiveOntologies();
    }
}
