package net.sourceforge.ondex.core.memory;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.sourceforge.ondex.config.Config;
import net.sourceforge.ondex.core.Attribute;
import net.sourceforge.ondex.core.AttributeName;
import net.sourceforge.ondex.core.ConceptClass;
import net.sourceforge.ondex.core.DataSource;
import net.sourceforge.ondex.core.EvidenceType;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.ONDEXRelation;
import net.sourceforge.ondex.core.RelationKey;
import net.sourceforge.ondex.core.RelationType;
import net.sourceforge.ondex.core.base.AbstractONDEXGraph;
import net.sourceforge.ondex.core.base.RelationKeyImpl;
import net.sourceforge.ondex.event.ONDEXEventHandler;
import net.sourceforge.ondex.event.ONDEXListener;
import net.sourceforge.ondex.event.type.DuplicatedEntryEvent;
import net.sourceforge.ondex.event.type.EventType;
import org.apache.commons.collections15.BidiMap;
import org.apache.commons.collections15.bidimap.DualHashBidiMap;

/**
 * This class represents a pure memory based implementation of the abstract
 * ONDEX graph. It uses standard JAVA datatypes.
 * 
 * @author taubertj
 */
public class MemoryONDEXGraph extends AbstractONDEXGraph {

    private static final long serialVersionUID = 1L;

    private final Map<RelationKey, ONDEXRelation> keyToRelation;

    private final BidiMap<Integer, ONDEXRelation> idToRelation;

    private final BidiMap<Integer, ONDEXConcept> idToConcept;

    private final Map<DataSource, Set<ONDEXConcept>> dataSourceToConcepts;

    private final Map<ConceptClass, Set<ONDEXConcept>> conceptClassToConcepts;

    private final Map<ONDEXConcept, Set<ONDEXRelation>> conceptToRelations;

    private final Map<DataSource, Set<ONDEXRelation>> dataSourceToRelations;

    private final Map<ConceptClass, Set<ONDEXRelation>> conceptClassToRelations;

    private final Map<RelationType, Set<ONDEXRelation>> relationTypeToRelations;

    protected final Map<AttributeName, Set<ONDEXConcept>> attributeNameToConcepts;

    protected final Map<AttributeName, Set<ONDEXRelation>> attributeNameToRelations;

    protected final Map<EvidenceType, Set<ONDEXConcept>> evidenceTypeToConcepts;

    protected final Map<EvidenceType, Set<ONDEXRelation>> evidenceTypeToRelations;

    protected final Map<ONDEXConcept, Set<ONDEXConcept>> tagToConcepts;

    protected final Map<ONDEXConcept, Set<ONDEXRelation>> tagToRelations;

    protected final Map<ONDEXConcept, Set<ONDEXConcept>> conceptToTags;

    protected final Map<ONDEXRelation, Set<ONDEXConcept>> relationToTags;

    protected final Map<ONDEXConcept, Set<EvidenceType>> conceptToEvidence;

    protected final Map<ONDEXRelation, Set<EvidenceType>> relationToEvidence;

    /**
	 * Constructor which sets the name of the graph to the given name.
	 * 
	 * @param name
	 *            name of graph
	 */
    public MemoryONDEXGraph(String name) {
        this(name, null);
    }

    /**
	 * Constructor which sets the name of the graph to the given name.
	 * 
	 * @param name
	 *            name of graph
	 * @param l
	 *            ONDEXListener
	 */
    public MemoryONDEXGraph(String name, ONDEXListener l) {
        super(name, new MemoryONDEXGraphMetaData());
        if (l != null) ONDEXEventHandler.getEventHandlerForSID(getSID()).addONDEXONDEXListener(l);
        this.keyToRelation = new HashMap<RelationKey, ONDEXRelation>();
        this.idToRelation = new DualHashBidiMap<Integer, ONDEXRelation>();
        this.idToConcept = new DualHashBidiMap<Integer, ONDEXConcept>();
        this.dataSourceToConcepts = new HashMap<DataSource, Set<ONDEXConcept>>();
        this.conceptClassToConcepts = new HashMap<ConceptClass, Set<ONDEXConcept>>();
        this.attributeNameToConcepts = new HashMap<AttributeName, Set<ONDEXConcept>>();
        this.evidenceTypeToConcepts = new HashMap<EvidenceType, Set<ONDEXConcept>>();
        this.conceptToRelations = new HashMap<ONDEXConcept, Set<ONDEXRelation>>();
        this.dataSourceToRelations = new HashMap<DataSource, Set<ONDEXRelation>>();
        this.conceptClassToRelations = new HashMap<ConceptClass, Set<ONDEXRelation>>();
        this.relationTypeToRelations = new HashMap<RelationType, Set<ONDEXRelation>>();
        this.attributeNameToRelations = new HashMap<AttributeName, Set<ONDEXRelation>>();
        this.evidenceTypeToRelations = new HashMap<EvidenceType, Set<ONDEXRelation>>();
        this.tagToConcepts = new HashMap<ONDEXConcept, Set<ONDEXConcept>>();
        this.tagToRelations = new HashMap<ONDEXConcept, Set<ONDEXRelation>>();
        this.conceptToTags = new HashMap<ONDEXConcept, Set<ONDEXConcept>>();
        this.relationToTags = new HashMap<ONDEXRelation, Set<ONDEXConcept>>();
        this.conceptToEvidence = new HashMap<ONDEXConcept, Set<EvidenceType>>();
        this.relationToEvidence = new HashMap<ONDEXRelation, Set<EvidenceType>>();
    }

    @Override
    protected ONDEXConcept removeConcept(int id) {
        ONDEXConcept c = idToConcept.remove(id);
        assert c.getId() == id : "Concept appears to be registered under the wrong id";
        dataSourceToConcepts.get(c.getElementOf()).remove(c);
        conceptClassToConcepts.get(c.getOfType()).remove(c);
        for (Attribute attribute : c.getAttributes()) {
            attributeNameToConcepts.get(attribute.getOfType()).remove(c);
        }
        for (EvidenceType et : c.getEvidence()) {
            evidenceTypeToConcepts.get(et).remove(c);
        }
        for (ONDEXConcept tag : c.getTags()) {
            tagToConcepts.get(tag).remove(c);
        }
        tagToConcepts.remove(c);
        conceptToTags.remove(c);
        conceptToEvidence.remove(c);
        return c;
    }

    @Override
    protected boolean removeRelation(int id) {
        ONDEXRelation r = idToRelation.get(id);
        if (r != null) {
            ONDEXConcept from = r.getFromConcept();
            ONDEXConcept to = r.getToConcept();
            RelationType rt = r.getOfType();
            idToRelation.remove(id);
            this.removeRelation(from, to, rt);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean removeRelation(ONDEXConcept fromConcept, ONDEXConcept toConcept, RelationType ofType) {
        RelationKey id = new RelationKeyImpl(sid, fromConcept.getId(), toConcept.getId(), ofType.getId());
        ONDEXRelation r = keyToRelation.remove(id);
        if (r != null) {
            idToRelation.remove(r.getId());
            relationTypeToRelations.get(ofType).remove(r);
            conceptToRelations.get(fromConcept).remove(r);
            dataSourceToRelations.get(fromConcept.getElementOf()).remove(r);
            conceptClassToRelations.get(fromConcept.getOfType()).remove(r);
            conceptToRelations.get(toConcept).remove(r);
            dataSourceToRelations.get(toConcept.getElementOf()).remove(r);
            conceptClassToRelations.get(toConcept.getOfType()).remove(r);
            for (Attribute attribute : r.getAttributes()) {
                attributeNameToRelations.get(attribute.getOfType()).remove(r);
            }
            for (EvidenceType et : r.getEvidence()) {
                evidenceTypeToRelations.get(et).remove(r);
            }
            for (ONDEXConcept tag : r.getTags()) {
                tagToRelations.get(tag).remove(r);
            }
            relationToTags.remove(r);
            relationToEvidence.remove(r);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected ONDEXConcept retrieveConcept(int id) {
        return idToConcept.get(id);
    }

    @Override
    protected Set<ONDEXConcept> retrieveConceptAll() {
        return idToConcept.values();
    }

    @Override
    protected Set<ONDEXConcept> retrieveConceptAllAttributeName(AttributeName attributeName) {
        return attributeNameToConcepts.get(attributeName);
    }

    @Override
    protected Set<ONDEXConcept> retrieveConceptAllConceptClass(ConceptClass conceptClass) {
        return conceptClassToConcepts.get(conceptClass);
    }

    @Override
    protected Set<ONDEXConcept> retrieveConceptAllDataSource(DataSource dataSource) {
        return dataSourceToConcepts.get(dataSource);
    }

    @Override
    protected Set<ONDEXConcept> retrieveConceptAllEvidenceType(EvidenceType evidenceType) {
        return evidenceTypeToConcepts.get(evidenceType);
    }

    @Override
    protected Set<ONDEXConcept> retrieveConceptAllTag(ONDEXConcept concept) {
        return tagToConcepts.get(concept);
    }

    @Override
    protected ONDEXRelation retrieveRelation(int id) {
        return idToRelation.get(id);
    }

    @Override
    protected ONDEXRelation retrieveRelation(ONDEXConcept fromConcept, ONDEXConcept toConcept, RelationType ofType) {
        RelationKey key = new RelationKeyImpl(sid, fromConcept.getId(), toConcept.getId(), ofType.getId());
        return keyToRelation.get(key);
    }

    @Override
    protected Set<ONDEXRelation> retrieveRelationAll() {
        return idToRelation.values();
    }

    @Override
    protected Set<ONDEXRelation> retrieveRelationAllAttributeName(AttributeName attributeName) {
        return attributeNameToRelations.get(attributeName);
    }

    @Override
    protected Set<ONDEXRelation> retrieveRelationAllConcept(ONDEXConcept concept) {
        return conceptToRelations.get(concept);
    }

    @Override
    protected Set<ONDEXRelation> retrieveRelationAllConceptClass(ConceptClass conceptClass) {
        return conceptClassToRelations.get(conceptClass);
    }

    @Override
    protected Set<ONDEXRelation> retrieveRelationAllDataSource(DataSource dataSource) {
        return dataSourceToRelations.get(dataSource);
    }

    @Override
    protected Set<ONDEXRelation> retrieveRelationAllEvidenceType(EvidenceType evidenceType) {
        return evidenceTypeToRelations.get(evidenceType);
    }

    @Override
    protected Set<ONDEXRelation> retrieveRelationAllRelationType(RelationType relationType) {
        return relationTypeToRelations.get(relationType);
    }

    @Override
    protected Set<ONDEXRelation> retrieveRelationAllTag(ONDEXConcept concept) {
        return tagToRelations.get(concept);
    }

    @Override
    protected Set<ONDEXConcept> retrieveTags() {
        Set<ONDEXConcept> allTags = new HashSet<ONDEXConcept>(tagToConcepts.keySet());
        allTags.addAll(tagToRelations.keySet());
        return allTags;
    }

    @Override
    protected ONDEXConcept storeConcept(long sid, int id, String pid, String annotation, String description, DataSource elementOf, ConceptClass ofType, Collection<EvidenceType> evidence) {
        ONDEXConcept existingConcept = idToConcept.get(id);
        if (existingConcept != null) {
            fireEventOccurred(new DuplicatedEntryEvent(Config.properties.getProperty("memory.ONDEXGraph.DuplicatedConcept") + id + " pid= " + pid, "[MemoryONDEXGraph - storeConcept]"));
            return existingConcept;
        } else {
            ONDEXConcept c = new MemoryONDEXConcept(sid, this, id, pid, annotation, description, elementOf, ofType);
            conceptToEvidence.put(c, new HashSet<EvidenceType>());
            for (EvidenceType anEvidence : evidence) {
                c.addEvidenceType(anEvidence);
            }
            idToConcept.put(c.getId(), c);
            Set<ONDEXConcept> cvSet = dataSourceToConcepts.get(elementOf);
            if (cvSet == null) {
                cvSet = new HashSet<ONDEXConcept>();
                dataSourceToConcepts.put(elementOf, cvSet);
            }
            cvSet.add(c);
            Set<ONDEXConcept> ccSet = conceptClassToConcepts.get(ofType);
            if (ccSet == null) {
                ccSet = new HashSet<ONDEXConcept>();
                conceptClassToConcepts.put(ofType, ccSet);
            }
            ccSet.add(c);
            return c;
        }
    }

    @Override
    protected ONDEXRelation storeRelation(long sid, int id, ONDEXConcept fromConcept, ONDEXConcept toConcept, RelationType ofType, Collection<EvidenceType> evidence) {
        RelationKey relkey = new RelationKeyImpl(sid, fromConcept.getId(), toConcept.getId(), ofType.getId());
        ONDEXRelation existingRelation = keyToRelation.get(relkey);
        if (existingRelation != null) {
            fireEventOccurred(new DuplicatedEntryEvent(Config.properties.getProperty("memory.ONDEXGraph.DuplicatedRelation") + relkey + " pid from " + fromConcept.getPID() + "pid to " + toConcept.getPID(), "[MemoryONDEXGraph - storeRelation]"));
            return existingRelation;
        } else {
            ONDEXRelation r = new MemoryONDEXRelation(sid, this, id, fromConcept, toConcept, ofType);
            relationToEvidence.put(r, new HashSet<EvidenceType>());
            for (EvidenceType anEvidence : evidence) {
                r.addEvidenceType(anEvidence);
            }
            keyToRelation.put(r.getKey(), r);
            idToRelation.put(r.getId(), r);
            Set<ONDEXRelation> relationTypeSet = relationTypeToRelations.get(ofType);
            if (relationTypeSet == null) {
                relationTypeSet = new HashSet<ONDEXRelation>();
                relationTypeToRelations.put(ofType, relationTypeSet);
            }
            relationTypeSet.add(r);
            Set<ONDEXRelation> fromConceptSet = conceptToRelations.get(fromConcept);
            if (fromConceptSet == null) {
                fromConceptSet = new HashSet<ONDEXRelation>();
                conceptToRelations.put(fromConcept, fromConceptSet);
            }
            fromConceptSet.add(r);
            Set<ONDEXRelation> dataSourceFromSet = dataSourceToRelations.get(fromConcept.getElementOf());
            if (dataSourceFromSet == null) {
                dataSourceFromSet = new HashSet<ONDEXRelation>();
                dataSourceToRelations.put(fromConcept.getElementOf(), dataSourceFromSet);
            }
            dataSourceFromSet.add(r);
            Set<ONDEXRelation> conceptClassFromSet = conceptClassToRelations.get(fromConcept.getOfType());
            if (conceptClassFromSet == null) {
                conceptClassFromSet = new HashSet<ONDEXRelation>();
                conceptClassToRelations.put(fromConcept.getOfType(), conceptClassFromSet);
            }
            conceptClassFromSet.add(r);
            Set<ONDEXRelation> toConceptSet = conceptToRelations.get(toConcept);
            if (toConceptSet == null) {
                toConceptSet = new HashSet<ONDEXRelation>();
                conceptToRelations.put(toConcept, toConceptSet);
            }
            toConceptSet.add(r);
            Set<ONDEXRelation> dataSourceToSet = dataSourceToRelations.get(toConcept.getElementOf());
            if (dataSourceToSet == null) {
                dataSourceToSet = new HashSet<ONDEXRelation>();
                dataSourceToRelations.put(toConcept.getElementOf(), dataSourceToSet);
            }
            dataSourceToSet.add(r);
            Set<ONDEXRelation> conceptClassToSet = conceptClassToRelations.get(toConcept.getOfType());
            if (conceptClassToSet == null) {
                conceptClassToSet = new HashSet<ONDEXRelation>();
                conceptClassToRelations.put(toConcept.getOfType(), conceptClassToSet);
            }
            conceptClassToSet.add(r);
            return r;
        }
    }

    /**
	 * Propagate events to registered listeners
	 * 
	 * @param e
	 *            EventType to propagate
	 */
    protected void fireEventOccurred(EventType e) {
        ONDEXEventHandler.getEventHandlerForSID(getSID()).fireEventOccurred(e);
    }
}
