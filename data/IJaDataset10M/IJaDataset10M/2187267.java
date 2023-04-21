package backend.core.persistent;

import java.util.Collection;
import java.util.Iterator;
import org.garret.perst.IPersistentMap;
import backend.core.AbstractConcept;
import backend.core.AbstractONDEXGraph;
import backend.core.AbstractRelation;
import backend.core.AbstractRelationTypeSet;
import backend.core.CV;
import backend.core.ConceptClass;
import backend.core.EvidenceType;

/**
 * This class represents a persistent implementation of
 * the abstract ONDEX graph. It uses Perst JAVA datatypes.
 * 
 * @author taubertj
 *
 */
public class ONDEXGraph extends AbstractONDEXGraph {

    private static final long serialVersionUID = 1L;

    private IPersistentMap<String, AbstractRelation> relations;

    private IPersistentMap<String, AbstractConcept> concepts;

    /**
	 * Default constructor initializing all fields of this class.
	 *
	 */
    ONDEXGraph() {
        this("ONDEX graph");
    }

    /**
	 * Constructor which sets the name of the graph to the given name.
	 * 
	 * @param name - name of graph
	 */
    ONDEXGraph(String name) {
        super(name, new ONDEXGraphMetaData());
        this.relations = PerstEnv.getDB().createMap(String.class);
        this.concepts = PerstEnv.getDB().createMap(String.class);
    }

    @Override
    protected AbstractConcept removeConcept(String id) {
        return concepts.remove(id);
    }

    @Override
    protected AbstractRelation removeRelation(AbstractConcept fromConcept, AbstractConcept toConcept, AbstractConcept qualifier, AbstractRelationTypeSet ofTypeSet) {
        AbstractRelation temp = new Relation(fromConcept, toConcept, qualifier, ofTypeSet);
        return relations.remove(temp.getId());
    }

    @Override
    protected AbstractConcept retrieveConcept(String id) {
        return concepts.get(id);
    }

    @Override
    protected Collection<AbstractConcept> retrieveConceptAll() {
        return concepts.values();
    }

    @Override
    protected AbstractRelation retrieveRelation(AbstractConcept fromConcept, AbstractConcept toConcept, AbstractConcept qualifier, AbstractRelationTypeSet ofTypeSet) {
        AbstractRelation temp = new Relation(fromConcept, toConcept, qualifier, ofTypeSet);
        return relations.get(temp.getId());
    }

    @Override
    protected Collection<AbstractRelation> retrieveRelationAll() {
        return relations.values();
    }

    @Override
    protected AbstractConcept storeConcept(String id, String annotation, String description, CV elementOf, ConceptClass ofType) {
        Concept c = new Concept(id, annotation, description, elementOf, ofType);
        concepts.put(c.getId(), c);
        return c;
    }

    @Override
    protected AbstractRelation storeRelation(AbstractConcept fromConcept, AbstractConcept toConcept, AbstractConcept qualifier, AbstractRelationTypeSet ofTypeSet, Collection<EvidenceType> evidence) {
        AbstractRelation r = new Relation(fromConcept, toConcept, qualifier, ofTypeSet);
        Iterator<EvidenceType> it = evidence.iterator();
        while (it.hasNext()) {
            r.addEvidenceType(it.next());
        }
        relations.put(r.getId(), r);
        return r;
    }
}
