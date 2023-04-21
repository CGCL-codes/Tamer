package net.sourceforge.ondex.core.memory;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import javax.naming.OperationNotSupportedException;
import net.sourceforge.ondex.config.Config;
import net.sourceforge.ondex.core.AbstractConcept;
import net.sourceforge.ondex.core.AbstractONDEXGraph;
import net.sourceforge.ondex.core.AbstractONDEXIterator;
import net.sourceforge.ondex.core.AbstractRelation;
import net.sourceforge.ondex.core.AttributeName;
import net.sourceforge.ondex.core.CV;
import net.sourceforge.ondex.core.ConceptClass;
import net.sourceforge.ondex.core.ConceptGDS;
import net.sourceforge.ondex.core.EvidenceType;
import net.sourceforge.ondex.core.ONDEXView;
import net.sourceforge.ondex.core.RelationGDS;
import net.sourceforge.ondex.core.RelationKey;
import net.sourceforge.ondex.core.RelationType;
import net.sourceforge.ondex.core.util.DefaultBitSet;
import net.sourceforge.ondex.core.util.SparseBitSet;
import net.sourceforge.ondex.event.GraphListener;
import net.sourceforge.ondex.event.type.DuplicatedEntryEvent;
import net.sourceforge.ondex.exception.type.AccessDeniedException;
import net.sourceforge.ondex.exception.type.NullValueException;

/**
 * This class represents a pure memory based implementation of the abstract
 * ONDEX graph. It uses standard JAVA datatypes.
 * 
 * @author taubertj
 * 
 */
public class ONDEXGraph extends AbstractONDEXGraph {

    private static final long serialVersionUID = 1L;

    private static final DefaultBitSet EMPTYSPARSEBITSET = new DefaultBitSet(0);

    private HashMap<RelationKey, AbstractRelation> relations;

    private HashMap<Integer, AbstractRelation> relationIds;

    private HashMap<Integer, AbstractConcept> concepts;

    private HashMap<CV, IntOpenHashSet> cv2AbstractConcepts;

    private HashMap<ConceptClass, IntOpenHashSet> conceptClass2AbstractConcepts;

    private HashMap<AbstractConcept, IntOpenHashSet> concept2AbstractRelations;

    private HashMap<CV, IntOpenHashSet> cv2AbstractRelations;

    private HashMap<ConceptClass, IntOpenHashSet> conceptClass2AbstractRelations;

    private HashMap<RelationType, IntOpenHashSet> relationType2AbstractRelations;

    protected HashMap<AttributeName, IntOpenHashSet> attributeName2AbstractConcepts;

    protected HashMap<EvidenceType, IntOpenHashSet> evidenceType2AbstractConcepts;

    protected HashMap<AbstractConcept, IntOpenHashSet> context2AbstractConcepts;

    protected HashMap<AttributeName, IntOpenHashSet> attributeName2AbstractRelations;

    protected HashMap<EvidenceType, IntOpenHashSet> evidenceType2AbstractRelations;

    protected HashMap<AbstractConcept, IntOpenHashSet> context2AbstractRelations;

    /**
	 * Constructor which sets the name of the graph to the given name.
	 * 
	 * @param name
	 *            name of graph
	 */
    public ONDEXGraph(String name) {
        this(name, null);
    }

    /**
	 * Constructor which sets the name of the graph to the given name.
	 * 
	 * @param name
	 *            name of graph
	 * @param l
	 *            GraphListener
	 */
    public ONDEXGraph(String name, GraphListener l) {
        super(name, new ONDEXGraphMetaData());
        if (l != null) this.addONDEXGraphListener(l);
        this.relations = new HashMap<RelationKey, AbstractRelation>(2000);
        this.relationIds = new HashMap<Integer, AbstractRelation>(2000);
        this.concepts = new HashMap<Integer, AbstractConcept>(2000);
        this.cv2AbstractConcepts = new HashMap<CV, IntOpenHashSet>(2000);
        this.conceptClass2AbstractConcepts = new HashMap<ConceptClass, IntOpenHashSet>(2000);
        this.attributeName2AbstractConcepts = new HashMap<AttributeName, IntOpenHashSet>(2000);
        this.evidenceType2AbstractConcepts = new HashMap<EvidenceType, IntOpenHashSet>(2000);
        this.context2AbstractConcepts = new HashMap<AbstractConcept, IntOpenHashSet>(2000);
        this.concept2AbstractRelations = new HashMap<AbstractConcept, IntOpenHashSet>(2000);
        this.cv2AbstractRelations = new HashMap<CV, IntOpenHashSet>(2000);
        this.conceptClass2AbstractRelations = new HashMap<ConceptClass, IntOpenHashSet>(2000);
        this.relationType2AbstractRelations = new HashMap<RelationType, IntOpenHashSet>(2000);
        this.attributeName2AbstractRelations = new HashMap<AttributeName, IntOpenHashSet>(2000);
        this.evidenceType2AbstractRelations = new HashMap<EvidenceType, IntOpenHashSet>(2000);
        this.context2AbstractRelations = new HashMap<AbstractConcept, IntOpenHashSet>(2000);
    }

    @Override
    protected AbstractConcept removeConcept(Integer id) throws AccessDeniedException {
        AbstractConcept c = concepts.remove(id);
        int intid = c.getId().intValue();
        cv2AbstractConcepts.get(c.getElementOf()).remove(intid);
        conceptClass2AbstractConcepts.get(c.getOfType()).remove(intid);
        AbstractONDEXIterator<ConceptGDS> itgds = c.getConceptGDSs();
        while (itgds.hasNext()) {
            AttributeName an;
            an = itgds.next().getAttrname();
            attributeName2AbstractConcepts.get(an).remove(intid);
        }
        itgds.close();
        AbstractONDEXIterator<EvidenceType> itet = c.getEvidence();
        while (itet.hasNext()) {
            EvidenceType et = itet.next();
            evidenceType2AbstractConcepts.get(et).remove(intid);
        }
        itet.close();
        ONDEXView<AbstractConcept> itc = c.getContext();
        while (itc.hasNext()) {
            AbstractConcept ac = itc.next();
            IntOpenHashSet set = context2AbstractConcepts.get(ac);
            if (set.size() == 1) {
                context2AbstractConcepts.remove(ac);
            } else {
                set.remove(intid);
            }
        }
        itc.close();
        return c;
    }

    @Override
    protected AbstractRelation removeRelation(AbstractConcept fromConcept, AbstractConcept toConcept, AbstractConcept qualifier, RelationType ofType) throws AccessDeniedException {
        RelationKey id = new RelationKey(sid, fromConcept.getId(), toConcept.getId(), qualifier != null ? qualifier.getId() : null, ofType.getId());
        AbstractRelation r = relations.remove(id);
        if (r != null) {
            relationIds.remove(r.getId());
            int intid = r.getId().intValue();
            relationType2AbstractRelations.get(ofType).remove(intid);
            concept2AbstractRelations.get(fromConcept).remove(intid);
            cv2AbstractRelations.get(fromConcept.getElementOf()).remove(intid);
            conceptClass2AbstractRelations.get(fromConcept.getOfType()).remove(intid);
            concept2AbstractRelations.get(toConcept).remove(intid);
            cv2AbstractRelations.get(toConcept.getElementOf()).remove(intid);
            conceptClass2AbstractRelations.get(toConcept.getOfType()).remove(intid);
            if (qualifier != null) {
                concept2AbstractRelations.get(qualifier).remove(intid);
                cv2AbstractRelations.get(qualifier.getElementOf()).remove(intid);
                conceptClass2AbstractRelations.get(qualifier.getOfType()).remove(intid);
            }
            AbstractONDEXIterator<RelationGDS> itgds = r.getRelationGDSs();
            while (itgds.hasNext()) {
                AttributeName an;
                an = itgds.next().getAttrname();
                attributeName2AbstractRelations.get(an).remove(intid);
            }
            itgds.close();
            AbstractONDEXIterator<EvidenceType> itet = r.getEvidence();
            while (itet.hasNext()) {
                EvidenceType et = itet.next();
                evidenceType2AbstractRelations.get(et).remove(intid);
            }
            itet.close();
            ONDEXView<AbstractConcept> itc = r.getContext();
            while (itc.hasNext()) {
                AbstractConcept ac = itc.next();
                IntOpenHashSet set = context2AbstractRelations.get(ac);
                if (set.size() == 1) {
                    context2AbstractRelations.remove(ac);
                } else {
                    set.remove(intid);
                }
            }
            itc.close();
        }
        return r;
    }

    @Override
    protected AbstractRelation removeRelation(Integer id) throws OperationNotSupportedException, AccessDeniedException {
        AbstractRelation r = relationIds.get(id);
        if (r != null) {
            AbstractConcept from = r.getFromConcept();
            AbstractConcept to = r.getToConcept();
            AbstractConcept qual = r.getQualifier();
            RelationType rt = r.getOfType();
            relationIds.remove(id);
            return this.removeRelation(from, to, qual, rt);
        } else {
            return null;
        }
    }

    @Override
    protected AbstractConcept retrieveConcept(Integer id) {
        return concepts.get(id);
    }

    @Override
    protected ONDEXView<AbstractConcept> retrieveConceptAll() {
        DefaultBitSet sbs = new DefaultBitSet(concepts.size());
        Iterator<Integer> it = concepts.keySet().iterator();
        while (it.hasNext()) {
            sbs.set(it.next());
        }
        return new ONDEXView<AbstractConcept>(this, AbstractConcept.class, sbs);
    }

    @Override
    protected ONDEXView<AbstractConcept> retrieveConceptAllCV(CV cv) {
        IntOpenHashSet c4cv = cv2AbstractConcepts.get(cv);
        if (c4cv != null) {
            return new ONDEXView<AbstractConcept>(this, AbstractConcept.class, new SparseBitSet((IntOpenHashSet) c4cv.clone()));
        } else {
            return new ONDEXView<AbstractConcept>(this, AbstractConcept.class, EMPTYSPARSEBITSET);
        }
    }

    @Override
    protected ONDEXView<AbstractConcept> retrieveConceptAllConceptClass(ConceptClass cc) {
        IntOpenHashSet c4CC = conceptClass2AbstractConcepts.get(cc);
        if (c4CC != null) {
            return new ONDEXView<AbstractConcept>(this, AbstractConcept.class, new SparseBitSet((IntOpenHashSet) c4CC.clone()));
        } else {
            return new ONDEXView<AbstractConcept>(this, AbstractConcept.class, EMPTYSPARSEBITSET);
        }
    }

    @Override
    protected ONDEXView<AbstractConcept> retrieveConceptAllAttributeName(AttributeName an) {
        IntOpenHashSet c4attName = attributeName2AbstractConcepts.get(an);
        if (c4attName != null) {
            return new ONDEXView<AbstractConcept>(this, AbstractConcept.class, new SparseBitSet((IntOpenHashSet) c4attName));
        } else {
            return new ONDEXView<AbstractConcept>(this, AbstractConcept.class, EMPTYSPARSEBITSET);
        }
    }

    @Override
    protected ONDEXView<AbstractConcept> retrieveConceptAllEvidenceType(EvidenceType et) {
        IntOpenHashSet c4et = evidenceType2AbstractConcepts.get(et);
        if (c4et != null) {
            return new ONDEXView<AbstractConcept>(this, AbstractConcept.class, new SparseBitSet((IntOpenHashSet) c4et.clone()));
        } else {
            return new ONDEXView<AbstractConcept>(this, AbstractConcept.class, EMPTYSPARSEBITSET);
        }
    }

    @Override
    protected ONDEXView<AbstractConcept> retrieveConceptAllContext(AbstractConcept concept) {
        IntOpenHashSet c4Context = context2AbstractConcepts.get(concept);
        if (c4Context != null) {
            return new ONDEXView<AbstractConcept>(this, AbstractConcept.class, new SparseBitSet((IntOpenHashSet) c4Context.clone()));
        } else {
            return new ONDEXView<AbstractConcept>(this, AbstractConcept.class, EMPTYSPARSEBITSET);
        }
    }

    @Override
    protected ONDEXView<AbstractConcept> retrieveContext() {
        Iterator<AbstractConcept> it = context2AbstractConcepts.keySet().iterator();
        SparseBitSet set = new SparseBitSet(context2AbstractConcepts.size());
        while (it.hasNext()) {
            AbstractConcept key = it.next();
            if (context2AbstractConcepts.get(key).size() > 0) {
                try {
                    set.set(key.getId().intValue());
                } catch (AccessDeniedException e) {
                }
            }
        }
        it = context2AbstractRelations.keySet().iterator();
        while (it.hasNext()) {
            AbstractConcept key = it.next();
            if (context2AbstractRelations.get(key).size() > 0) {
                try {
                    set.set(key.getId().intValue());
                } catch (AccessDeniedException e) {
                }
            }
        }
        return new ONDEXView<AbstractConcept>(this, AbstractConcept.class, set);
    }

    @Override
    protected AbstractRelation retrieveRelation(AbstractConcept fromConcept, AbstractConcept toConcept, AbstractConcept qualifier, RelationType ofType) throws AccessDeniedException {
        RelationKey key = new RelationKey(sid, fromConcept.getId(), toConcept.getId(), qualifier != null ? qualifier.getId() : null, ofType.getId());
        return relations.get(key);
    }

    @Override
    protected AbstractRelation retrieveRelation(Integer id) {
        return relationIds.get(id);
    }

    @Override
    protected ONDEXView<AbstractRelation> retrieveRelationAll() {
        DefaultBitSet bset = new DefaultBitSet(relationIds.size());
        Iterator<Integer> it = relationIds.keySet().iterator();
        while (it.hasNext()) {
            bset.set(it.next());
        }
        return new ONDEXView<AbstractRelation>(this, AbstractRelation.class, bset);
    }

    @Override
    protected ONDEXView<AbstractRelation> retrieveRelationAllConcept(AbstractConcept c) {
        IntOpenHashSet r4Concept = concept2AbstractRelations.get(c);
        if (r4Concept != null) {
            return new ONDEXView<AbstractRelation>(this, AbstractRelation.class, new SparseBitSet((IntOpenHashSet) r4Concept.clone()));
        } else {
            return new ONDEXView<AbstractRelation>(this, AbstractRelation.class, EMPTYSPARSEBITSET);
        }
    }

    @Override
    protected ONDEXView<AbstractRelation> retrieveRelationAllCV(CV cv) {
        IntOpenHashSet r4Cv = cv2AbstractRelations.get(cv);
        if (r4Cv != null) {
            return new ONDEXView<AbstractRelation>(this, AbstractRelation.class, new SparseBitSet((IntOpenHashSet) r4Cv.clone()));
        } else {
            return new ONDEXView<AbstractRelation>(this, AbstractRelation.class, EMPTYSPARSEBITSET);
        }
    }

    @Override
    protected ONDEXView<AbstractRelation> retrieveRelationAllConceptClass(ConceptClass cc) {
        IntOpenHashSet r4ccSet = conceptClass2AbstractRelations.get(cc);
        if (r4ccSet != null) {
            return new ONDEXView<AbstractRelation>(this, AbstractRelation.class, new SparseBitSet((IntOpenHashSet) r4ccSet.clone()));
        } else {
            return new ONDEXView<AbstractRelation>(this, AbstractRelation.class, EMPTYSPARSEBITSET);
        }
    }

    @Override
    protected ONDEXView<AbstractRelation> retrieveRelationAllAttributeName(AttributeName an) {
        IntOpenHashSet r4attName = attributeName2AbstractRelations.get(an);
        if (r4attName != null) {
            return new ONDEXView<AbstractRelation>(this, AbstractRelation.class, new SparseBitSet((IntOpenHashSet) r4attName.clone()));
        } else {
            return new ONDEXView<AbstractRelation>(this, AbstractRelation.class, EMPTYSPARSEBITSET);
        }
    }

    @Override
    protected ONDEXView<AbstractRelation> retrieveRelationAllEvidenceType(EvidenceType et) {
        IntOpenHashSet r4et = evidenceType2AbstractRelations.get(et);
        if (r4et != null) {
            return new ONDEXView<AbstractRelation>(this, AbstractRelation.class, new SparseBitSet((IntOpenHashSet) r4et.clone()));
        } else {
            return new ONDEXView<AbstractRelation>(this, AbstractRelation.class, EMPTYSPARSEBITSET);
        }
    }

    @Override
    protected ONDEXView<AbstractRelation> retrieveRelationAllContext(AbstractConcept concept) {
        IntOpenHashSet r4Context = context2AbstractRelations.get(concept);
        if (r4Context != null) {
            return new ONDEXView<AbstractRelation>(this, AbstractRelation.class, new SparseBitSet((IntOpenHashSet) r4Context.clone()));
        } else {
            return new ONDEXView<AbstractRelation>(this, AbstractRelation.class, EMPTYSPARSEBITSET);
        }
    }

    @Override
    protected ONDEXView<AbstractRelation> retrieveRelationAllRelationType(RelationType rt) {
        IntOpenHashSet r4RT = relationType2AbstractRelations.get(rt);
        if (r4RT != null) {
            return new ONDEXView<AbstractRelation>(this, AbstractRelation.class, new SparseBitSet((IntOpenHashSet) r4RT.clone()));
        } else {
            return new ONDEXView<AbstractRelation>(this, AbstractRelation.class, EMPTYSPARSEBITSET);
        }
    }

    /**
	 * @throws NullValueException if evidence collection contains null values.
	 * @see net.sourceforge.ondex.core.AbstractONDEXGraph#storeConcept(long, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, net.sourceforge.ondex.core.CV, net.sourceforge.ondex.core.ConceptClass, java.util.Collection)
	 */
    @Override
    protected AbstractConcept storeConcept(long sid, Integer id, String pid, String annotation, String description, CV elementOf, ConceptClass ofType, Collection<EvidenceType> evidence) throws AccessDeniedException, NullValueException {
        AbstractConcept c = new Concept(sid, this, id, pid, annotation, description, elementOf, ofType);
        int intid = id.intValue();
        Iterator<EvidenceType> it = evidence.iterator();
        while (it.hasNext()) c.addEvidenceType(it.next());
        try {
            AbstractConcept existingConcept = concepts.get(c.getId());
            if (existingConcept != null) {
                this.fireEventOccurred(new DuplicatedEntryEvent(Config.properties.getProperty("memory.ONDEXGraph.DuplicatedConcept") + c.getId() + " pid= " + c.getPID(), "[ONDEXGraph - storeConcept]"));
                return existingConcept;
            } else {
                concepts.put(c.getId(), c);
                IntOpenHashSet cvSet = cv2AbstractConcepts.get(elementOf);
                if (cvSet == null) {
                    cvSet = new IntOpenHashSet();
                    cv2AbstractConcepts.put(elementOf, cvSet);
                }
                cvSet.add(intid);
                IntOpenHashSet ccSet = conceptClass2AbstractConcepts.get(ofType);
                if (ccSet == null) {
                    ccSet = new IntOpenHashSet();
                    conceptClass2AbstractConcepts.put(ofType, ccSet);
                }
                ccSet.add(intid);
            }
        } catch (AccessDeniedException e) {
            throw e;
        }
        return c;
    }

    @Override
    protected AbstractRelation storeRelation(long sid, Integer id, AbstractConcept fromConcept, AbstractConcept toConcept, AbstractConcept qualifier, RelationType ofType, Collection<EvidenceType> evidence) throws NullValueException, AccessDeniedException {
        AbstractRelation r = new Relation(sid, this, id, fromConcept, toConcept, qualifier, ofType);
        RelationKey relkey;
        try {
            Iterator<EvidenceType> it = evidence.iterator();
            while (it.hasNext()) {
                r.addEvidenceType(it.next());
            }
            relkey = r.getKey();
        } catch (AccessDeniedException e) {
            throw e;
        }
        AbstractRelation existingRelation = relations.get(r.getKey());
        if (existingRelation != null) {
            try {
                this.fireEventOccurred(new DuplicatedEntryEvent(Config.properties.getProperty("memory.ONDEXGraph.DuplicatedRelation") + relkey + " pid from " + r.getFromConcept().getPID() + "pid to " + r.getToConcept().getPID(), "[ONDEXGraph - storeRelation]"));
                return existingRelation;
            } catch (AccessDeniedException e) {
                throw e;
            }
        } else {
            int intid;
            try {
                relations.put(r.getKey(), r);
                relationIds.put(r.getId(), r);
                intid = r.getId().intValue();
            } catch (AccessDeniedException e) {
                throw e;
            }
            IntOpenHashSet rtSet = relationType2AbstractRelations.get(ofType);
            if (rtSet == null) {
                rtSet = new IntOpenHashSet();
                relationType2AbstractRelations.put(ofType, rtSet);
            }
            rtSet.add(intid);
            IntOpenHashSet fromSet = concept2AbstractRelations.get(fromConcept);
            if (fromSet == null) {
                fromSet = new IntOpenHashSet();
                concept2AbstractRelations.put(fromConcept, fromSet);
            }
            fromSet.add(intid);
            IntOpenHashSet cvFromSet = cv2AbstractRelations.get(fromConcept.getElementOf());
            if (cvFromSet == null) {
                cvFromSet = new IntOpenHashSet();
                cv2AbstractRelations.put(fromConcept.getElementOf(), cvFromSet);
            }
            cvFromSet.add(intid);
            IntOpenHashSet ccFromSet = conceptClass2AbstractRelations.get(fromConcept.getOfType());
            if (ccFromSet == null) {
                ccFromSet = new IntOpenHashSet();
                conceptClass2AbstractRelations.put(fromConcept.getOfType(), ccFromSet);
            }
            ccFromSet.add(intid);
            IntOpenHashSet toSet = concept2AbstractRelations.get(toConcept);
            if (toSet == null) {
                toSet = new IntOpenHashSet();
                concept2AbstractRelations.put(toConcept, toSet);
            }
            toSet.add(intid);
            IntOpenHashSet cvToSet = cv2AbstractRelations.get(toConcept.getElementOf());
            if (cvToSet == null) {
                cvToSet = new IntOpenHashSet();
                cv2AbstractRelations.put(toConcept.getElementOf(), cvToSet);
            }
            cvToSet.add(intid);
            IntOpenHashSet ccToSet = conceptClass2AbstractRelations.get(toConcept.getOfType());
            if (ccToSet == null) {
                ccToSet = new IntOpenHashSet();
                conceptClass2AbstractRelations.put(toConcept.getOfType(), ccToSet);
            }
            ccToSet.add(intid);
            if (qualifier != null) {
                IntOpenHashSet cQualSet = concept2AbstractRelations.get(qualifier);
                if (cQualSet == null) {
                    cQualSet = new IntOpenHashSet();
                    concept2AbstractRelations.put(qualifier, cQualSet);
                }
                cQualSet.add(intid);
                IntOpenHashSet cvQualSet = cv2AbstractRelations.get(qualifier.getElementOf());
                if (cvQualSet == null) {
                    cvQualSet = new IntOpenHashSet();
                    cv2AbstractRelations.put(qualifier.getElementOf(), cvQualSet);
                }
                cvQualSet.add(intid);
                IntOpenHashSet ccQualSet = conceptClass2AbstractRelations.get(qualifier.getOfType());
                if (ccQualSet == null) {
                    ccQualSet = new IntOpenHashSet();
                    conceptClass2AbstractRelations.put(qualifier.getOfType(), ccQualSet);
                }
                ccQualSet.add(intid);
            }
        }
        return r;
    }

    @Override
    public byte[] serialise() {
        return null;
    }
}
