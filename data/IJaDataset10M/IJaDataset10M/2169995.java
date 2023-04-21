package net.sourceforge.ondex.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.naming.OperationNotSupportedException;
import javax.swing.event.EventListenerList;
import net.sourceforge.ondex.config.Config;
import net.sourceforge.ondex.config.ONDEXRegistry;
import static net.sourceforge.ondex.core.security.perm.Permission.*;
import net.sourceforge.ondex.core.security.perm.Access;
import net.sourceforge.ondex.core.security.perm.AccessScope;
import net.sourceforge.ondex.core.security.perm.GlobalPermissions;
import net.sourceforge.ondex.core.security.perm.Permission;
import net.sourceforge.ondex.core.security.perm.PermissionInit;
import net.sourceforge.ondex.event.GraphListener;
import net.sourceforge.ondex.event.type.AccessDeniedEvent;
import net.sourceforge.ondex.event.type.NullValueEvent;
import net.sourceforge.ondex.exception.type.AccessDeniedException;
import net.sourceforge.ondex.exception.type.NullValueException;
import net.sourceforge.ondex.exception.type.WrongParameterException;

/**
 * This class is the core of the ONDEX backend and contains all Concepts and
 * Relations as well as additional data like CVs or ConceptClasses in the
 * AbstractONDEXGraphMetaData.
 * 
 * Concepts and Relations can only be created within the context of this class.
 * Additional data is added using the appropriate methods.
 * 
 * @author taubertj
 * 
 */
public abstract class AbstractONDEXGraph extends AbstractONDEXEntity {

    /**
	 * Name for this instance of AbstractONDEXGraph.
	 */
    private String name;

    /**
	 * Graph data for this instance of AbstractONDEXGraph.
	 */
    private AbstractONDEXGraphMetaData data;

    /**
	 * List for EventListeners.
	 */
    protected EventListenerList listenerList = new EventListenerList();

    /**
	 * Stores whether or not this ONDEXGraph is read only. For example a LUCENE
	 * ONDEX graph is read only.
	 */
    protected boolean readOnly = false;

    /**
	 * Stores the latest assigned int id to a concept. Every id gets assigned
	 * only once.
	 */
    protected int lastIdForConcept = 0;

    /**
	 * Stores the latest assigned int id to a relation. Every id gets assigned
	 * only once.
	 */
    protected int lastIdForRelation = 0;

    /**
	 * Constructor which fills all private fields of this class.
	 * 
	 * @param name
	 *            name of ONDEX graph
	 * @param data
	 *            ONDEX graph meta data
	 */
    @PermissionInit
    protected AbstractONDEXGraph(String name, AbstractONDEXGraphMetaData data) {
        this(System.nanoTime(), name, data);
    }

    /**
	 * Constructor which fills all private fields of this class and sets a given
	 * unique id to this graph.
	 * 
	 * @param sid
	 *            unique id
	 * @param name
	 *            name of ONDEX graph
	 * @param data
	 *            ONDEX graph meta data
	 */
    @PermissionInit
    protected AbstractONDEXGraph(long sid, String name, AbstractONDEXGraphMetaData data) {
        this.name = name;
        this.data = data;
        this.sid = sid;
        data.setSID(sid);
        ONDEXRegistry.graphs.put(sid, this);
    }

    /**
	 * Returns last assigned id for concepts.
	 * 
	 * @return last int id for concepts
	 */
    public int getLastIdForConcepts() {
        return this.lastIdForConcept;
    }

    /**
	 * Returns last assigned id for relations.
	 * 
	 * @return last int id for relations
	 */
    public int getLastIdForRelations() {
        return this.lastIdForRelation;
    }

    /**
	 * Adds a ActionListener to the model.
	 * 
	 * @param l
	 *            ActionListener
	 */
    public void addActionListener(ActionListener l) {
        listenerList.add(ActionListener.class, l);
    }

    /**
	 * Removes a ActionListener from the model.
	 * 
	 * @param l
	 *            ActionListener
	 */
    public void removeActionListener(ActionListener l) {
        listenerList.remove(ActionListener.class, l);
    }

    /**
	 * Notifies all listeners that have registered interest for notification on
	 * this event type.
	 * 
	 */
    protected void fireActionEvent(Viewable entity, String command) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ActionListener.class) {
                ActionEvent actionEvent = new ActionEvent(entity, ActionEvent.ACTION_PERFORMED, command);
                ((ActionListener) listeners[i + 1]).actionPerformed(actionEvent);
            }
        }
    }

    /**
	 * Returns whether or not this AbstractONDEXGraph is read only.
	 * 
	 * @return true if read only
	 */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
	 * Returns the name of this instance of AbstractONDEXGraph.
	 * 
	 * @return name of graph
	 */
    @Access(GET)
    public String getName() {
        return name;
    }

    public AbstractONDEXGraphMetaData getONDEXGraphData() {
        return data;
    }

    /**
	 * Creates a new AbstractRelation with the given fromConcept, toConcept,
	 * ofType and a single EvidenceType. Adds the new created AbstractRelation
	 * to the list of Relations of this graph.
	 * 
	 * @param fromConcept
	 *            from AbstractConcept for the new AbstractRelation
	 * @param toConcept
	 *            to AbstractConcept for the new AbstractRelation
	 * @param ofType
	 *            RelationType for the new AbstractRelation
	 * @param evidencetype
	 *            EvidenceTypes for the new AbstractRelation
	 * @return new AbstractRelation
	 */
    public AbstractRelation createRelation(AbstractConcept fromConcept, AbstractConcept toConcept, RelationType ofType, EvidenceType evidencetype) {
        if (evidencetype == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationEvidenceTypeNull"), "[AbstractONDEXGraph - createRelation]"));
            throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationEvidenceTypeNull"));
        } else {
            List<EvidenceType> v = new ArrayList<EvidenceType>();
            v.add(evidencetype);
            return createRelation(fromConcept, toConcept, null, ofType, v);
        }
    }

    /**
	 * Creates a new AbstractRelation with the given fromConcept, toConcept,
	 * ofType and a collection of EvidenceTypes. Adds the new created
	 * AbstractRelation to the list of Relations of this graph.
	 * 
	 * @param fromConcept
	 *            from AbstractConcept for the new AbstractRelation
	 * @param toConcept
	 *            to AbstractConcept for the new AbstractRelation
	 * @param ofType
	 *            RelationType for the new AbstractRelation
	 * @param evidence
	 *            collection of EvidenceTypes for the new AbstractRelation
	 * @return new AbstractRelation
	 */
    public AbstractRelation createRelation(AbstractConcept fromConcept, AbstractConcept toConcept, RelationType ofType, Collection<EvidenceType> evidence) {
        return createRelation(fromConcept, toConcept, null, ofType, evidence);
    }

    /**
	 * Creates a new AbstractRelation with the given fromConcept, toConcept,
	 * ofType and a single EvidenceType. Adds the new created AbstractRelation
	 * to the list of Relations of this graph.
	 * 
	 * @param fromConcept
	 *            from AbstractConcept for the new AbstractRelation
	 * @param toConcept
	 *            to AbstractConcept for the new AbstractRelation
	 * @param qualifier
	 *            qualifier AbstractConcept for trinary Relation
	 * @param ofType
	 *            RelationType for the new AbstractRelation
	 * @param evidencetype
	 *            EvidenceTypes for the new AbstractRelation
	 * @return new AbstractRelation
	 */
    public AbstractRelation createRelation(AbstractConcept fromConcept, AbstractConcept toConcept, AbstractConcept qualifier, RelationType ofType, EvidenceType evidencetype) {
        if (evidencetype == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationEvidenceTypeNull"), "[AbstractONDEXGraph - createRelation]"));
            throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationEvidenceTypeNull"));
        } else {
            List<EvidenceType> v = new ArrayList<EvidenceType>();
            v.add(evidencetype);
            return createRelation(fromConcept, toConcept, qualifier, ofType, v);
        }
    }

    /**
	 * Creates a new AbstractRelation with the given fromConcept, toConcept,
	 * ofType and a collection of EvidenceTypes. Adds the new created
	 * AbstractRelation to the list of Relations of this graph.
	 * 
	 * @param fromConcept
	 *            from AbstractConcept for the new AbstractRelation
	 * @param toConcept
	 *            to AbstractConcept for the new AbstractRelation
	 * @param qualifier
	 *            qualifier AbstractConcept for trinary Relation
	 * @param ofType
	 *            RelationType for the new AbstractRelation
	 * @param evidence
	 *            collection of EvidenceTypes for the new AbstractRelation
	 * @return new AbstractRelation
	 */
    @Access(CREATE)
    public AbstractRelation createRelation(AbstractConcept fromConcept, AbstractConcept toConcept, AbstractConcept qualifier, RelationType ofType, Collection<EvidenceType> evidence) {
        try {
            if (fromConcept == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationFromConceptNull"), "[AbstractONDEXGraph - createRelation]"));
                throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationFromConceptNull"));
            } else if (toConcept == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationToConceptNull"), "[AbstractONDEXGraph - createRelation]"));
                throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationToConceptNull"));
            } else if (ofType == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationOfTypeNull"), "[AbstractONDEXGraph - createRelation]"));
                throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationOfTypeNull"));
            } else if (evidence == null || evidence.size() == 0) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationEvidenceTypeNull"), "[AbstractONDEXGraph - createRelation]"));
                throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationEvidenceTypeNull"));
            } else {
                lastIdForRelation++;
                AbstractRelation r = storeRelation(sid, lastIdForRelation, fromConcept, toConcept, qualifier, ofType, evidence);
                fireActionEvent(r, "create");
                return r;
            }
        } catch (OperationNotSupportedException onse) {
            fireEventOccurred(new AccessDeniedEvent(Config.properties.getProperty("AbstractONDEXGraph.ReadOnly"), "[AbstractONDEXGraph - createRelation]"));
            throw new AccessDeniedException(Config.properties.getProperty("AbstractONDEXGraph.ReadOnly"));
        }
    }

    /**
	 * Deletes a AbstractRelation from the list of Relations of this graph
	 * specified by fromConcept, toConcept and ofType. Returns deleted Relation
	 * or null if unsuccessful.
	 * 
	 * @param fromConcept
	 *            from AbstractConcept
	 * @param toConcept
	 *            to AbstractConcept
	 * @param ofType
	 *            RelationType
	 * @return deleted AbstractRelation
	 */
    public AbstractRelation deleteRelation(AbstractConcept fromConcept, AbstractConcept toConcept, RelationType ofType) {
        return deleteRelation(fromConcept, toConcept, null, ofType);
    }

    /**
	 * Deletes a AbstractRelation from the list of Relations of this graph
	 * specified by fromConcept, toConcept and ofType. Returns deleted
	 * AbstractRelation or null if unsuccessful.
	 * 
	 * @param fromConcept
	 *            from AbstractConcept
	 * @param toConcept
	 *            to AbstractConcept
	 * @param qualifier
	 *            qualifier AbstractConcept
	 * @param ofType
	 *            RelationType
	 * @return deleted AbstractRelation
	 */
    @Access(DELETE)
    public AbstractRelation deleteRelation(AbstractConcept fromConcept, AbstractConcept toConcept, AbstractConcept qualifier, RelationType ofType) {
        try {
            if (fromConcept == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationFromConceptNull"), "[AbstractONDEXGraph - deleteRelation]"));
                throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationFromConceptNull"));
            } else if (toConcept == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationToConceptNull"), "[AbstractONDEXGraph - deleteRelation]"));
                throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationToConceptNull"));
            } else if (ofType == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationOfTypeNull"), "[AbstractONDEXGraph - deleteRelation]"));
                throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationOfTypeNull"));
            } else {
                AbstractRelation r = removeRelation(fromConcept, toConcept, qualifier, ofType);
                fireActionEvent(r, "delete");
                return r;
            }
        } catch (OperationNotSupportedException onse) {
            fireEventOccurred(new AccessDeniedEvent(Config.properties.getProperty("AbstractONDEXGraph.ReadOnly"), "[AbstractONDEXGraph - deleteRelation]"));
            throw new AccessDeniedException(Config.properties.getProperty("AbstractONDEXGraph.ReadOnly"));
        }
    }

    /**
	 * Deletes a AbstractRelation from the list of Relations of this graph
	 * specified by unique Id. Returns deleted AbstractRelation or null if
	 * unsuccessful.
	 * 
	 * @param id
	 *            Integer
	 * @return deleted AbstractRelation
	 */
    @Access(DELETE)
    public AbstractRelation deleteRelation(Integer id) {
        try {
            if (id == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationIDNull"), "[AbstractONDEXGraph - deleteRelation]"));
                throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationIDNull"));
            } else {
                AbstractRelation r = this.getRelation(id);
                if (r != null) {
                    removeRelation(id);
                    fireActionEvent(r, "delete");
                    return r;
                } else {
                    fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationIDNull"), "[AbstractONDEXGraph - deleteRelation]"));
                    throw new WrongParameterException(Config.properties.getProperty("relation id " + id + " unknown"));
                }
            }
        } catch (OperationNotSupportedException onse) {
            fireEventOccurred(new AccessDeniedEvent(Config.properties.getProperty("AbstractONDEXGraph.ReadOnly"), "[AbstractONDEXGraph - deleteRelation]"));
            throw new AccessDeniedException(Config.properties.getProperty("AbstractONDEXGraph.ReadOnly"));
        }
    }

    /**
	 * Returns a AbstractRelation from the list of Relations of this graph
	 * specified by fromConcept, toConcept and ofType or null if unsuccessful.
	 * 
	 * @param fromConcept
	 *            from AbstractConcept
	 * @param toConcept
	 *            to AbstractConcept
	 * @param ofType
	 *            RelationType
	 * @return existing AbstractRelaiton
	 */
    public AbstractRelation getRelation(AbstractConcept fromConcept, AbstractConcept toConcept, RelationType ofType) {
        return getRelation(fromConcept, toConcept, null, ofType);
    }

    /**
	 * Returns a trinary Relation from the list of Relations of this graph
	 * specified by fromConcept, toConcept, qualifier and ofType or null if
	 * unsuccessful.
	 * 
	 * @param fromConcept
	 *            from AbstractConcept
	 * @param toConcept
	 *            to AbstractConcept
	 * @param qualifier
	 *            qualifier (trinary relation)
	 * @param ofType
	 *            RelationType
	 * @return existing AbstractRelation
	 */
    @Access(GET)
    public AbstractRelation getRelation(AbstractConcept fromConcept, AbstractConcept toConcept, AbstractConcept qualifier, RelationType ofType) {
        if (fromConcept == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationFromConceptNull"), "[AbstractONDEXGraph - getRelation]"));
            throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationFromConceptNull"));
        } else if (toConcept == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationToConceptNull"), "[AbstractONDEXGraph - getRelation]"));
            throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationToConceptNull"));
        } else if (ofType == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationOfTypeNull"), "[AbstractONDEXGraph - getRelation]"));
            throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationOfTypeNull"));
        } else {
            return retrieveRelation(fromConcept, toConcept, qualifier, ofType);
        }
    }

    /**
	 * Returns a AbstractRelation from the list of Relations of this graph
	 * specified by unique Id or null if unsuccessful.
	 * 
	 * @param id
	 *            Integer
	 * @return existing AbstractRelation
	 * @throws AccessDeniedException 
	 * @throws NullValueException 
	 */
    @Access(GET)
    public AbstractRelation getRelation(Integer id) {
        if (id == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationIDNull"), "[AbstractONDEXGraph - getRelation]"));
            throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractRelationIDNull"));
        } else {
            return retrieveRelation(id);
        }
    }

    /**
	 * Returns all Relations contained in this graph.
	 * 
	 * @return all AbstractRelations as ONDEXView<AbstractRelation>
	 */
    @Access(GET)
    public ONDEXView<AbstractRelation> getRelations() {
        return retrieveRelationAll();
    }

    /**
	 * Returns all Relations connected to a given AbstractConcept contained in
	 * this graph. This includes all relations where the given concept is the "from", "to", 
	 * or "qualifier" within the relation. 
	 * 
	 * @param concept
	 *            AbstractConcept
	 * @return ONDEXView<AbstractRelation>
	 */
    @Access(GET)
    public ONDEXView<AbstractRelation> getRelationsOfConcept(AbstractConcept concept) {
        if (concept == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptNull"), "[AbstractONDEXGraph - getRelationsOfConcept]"));
            throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptNull"));
        } else {
            return retrieveRelationAllConcept(concept);
        }
    }

    /**
	 * Returns all Relations of a given CV contained in this graph.
	 * 
	 * @param cv
	 *            CV
	 * @return ONDEXView<AbstractRelation>
	 */
    @Access(GET)
    public ONDEXView<AbstractRelation> getRelationsOfCV(CV cv) {
        if (cv == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.CVNull"), "[AbstractONDEXGraph - getRelationsOfCV]"));
        } else {
            return retrieveRelationAllCV(cv);
        }
        return null;
    }

    /**
	 * Returns all Relations of a given ConceptClass contained in this graph.
	 * 
	 * @param cc
	 *            ConceptClass
	 * @return ONDEXView<AbstractRelation>
	 */
    @Access(GET)
    public ONDEXView<AbstractRelation> getRelationsOfConceptClass(ConceptClass cc) {
        if (cc == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.ConceptClassNull"), "[AbstractONDEXGraph - getRelationsOfConceptClass]"));
        } else {
            return retrieveRelationAllConceptClass(cc);
        }
        return null;
    }

    /**
	 * Returns all Relations of a given AttributeName contained in this graph.
	 * 
	 * @param an
	 *            AttributeName
	 * @return ONDEXView<AbstractRelation>
	 */
    @Access(GET)
    public ONDEXView<AbstractRelation> getRelationsOfAttributeName(AttributeName an) {
        if (an == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AttributeNameNull"), "[AbstractONDEXGraph - getRelationsOfAttributeName]"));
            throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AttributeNameNull"));
        } else {
            return retrieveRelationAllAttributeName(an);
        }
    }

    /**
	 * Returns all Relations of a given EvidenceType contained in this graph.
	 * 
	 * @param et
	 *            EvidenceType
	 * @return ONDEXView<AbstractRelation>
	 */
    @Access(GET)
    public ONDEXView<AbstractRelation> getRelationsOfEvidenceType(EvidenceType et) {
        if (et == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.EvidenceTypeNull"), "[AbstractONDEXGraph - getRelationsOfEvidenceType]"));
            throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.EvidenceTypeNull"));
        } else {
            return retrieveRelationAllEvidenceType(et);
        }
    }

    /**
	 * Returns all Relations of a given context contained in this graph.
	 * 
	 * @param ac
	 *            AbstractConcept
	 * @return ONDEXView<AbstractRelation>
	 */
    @Access(GET)
    public ONDEXView<AbstractRelation> getRelationsOfContext(AbstractConcept ac) {
        if (ac == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptNull"), "[AbstractONDEXGraph - getRelationsOfContext]"));
            throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptNull"));
        } else {
            return retrieveRelationAllContext(ac);
        }
    }

    /**
	 * Returns all Relations of a given RelationType contained in this graph. It
	 * only represents the state of RelationTypes in a AbstractRelationTypeSet
	 * at the time the AbstractRelation was created.
	 * 
	 * @param rt
	 *            RelationType
	 * @return ONDEXView<AbstractRelation>
	 */
    @Access(GET)
    public ONDEXView<AbstractRelation> getRelationsOfRelationType(RelationType rt) {
        if (rt == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.RelationTypeNull"), "[AbstractONDEXGraph - getRelationsOfRelationType]"));
            throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.RelationTypeNull"));
        } else {
            return retrieveRelationAllRelationType(rt);
        }
    }

    /**
	 * Creates a new AbstractConcept with the given id, CV and ConceptClass.
	 * Adds the new AbstractConcept to the list of Concepts of this graph and
	 * returns it.
	 * 
	 * @param id
	 *            PARSER ID of the new AbstractConcept
	 * @param annotation
	 *            relevant annotation of the new AbstractConcept
	 * @param elementOf
	 *            CV of the new AbstractConcept
	 * @param ofType
	 *            ConceptClass of the new AbstractConcept
	 * @param evidencetype
	 *            EvidenceType of the new AbstractConcept
	 * @return new AbstractConcept
	 */
    public AbstractConcept createConcept(String id, CV elementOf, ConceptClass ofType, EvidenceType evidencetype) {
        if (evidencetype == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptEvidenceTypeNull"), "[AbstractONDEXGraph - createConcept]"));
            throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptEvidenceTypeNull"));
        } else {
            List<EvidenceType> v = new ArrayList<EvidenceType>();
            v.add(evidencetype);
            return createConcept(id, "", "", elementOf, ofType, v);
        }
    }

    /**
	 * Creates a new AbstractConcept with the given id, CV and ConceptClass.
	 * Adds the new AbstractConcept to the list of Concepts of this graph and
	 * returns it.
	 * 
	 * @param id
	 *            PARSER ID of the new AbstractConcept
	 * @param annotation
	 *            relevant annotation of the new AbstractConcept
	 * @param elementOf
	 *            CV of the new AbstractConcept
	 * @param ofType
	 *            ConceptClass of the new AbstractConcept
	 * @param evidence
	 *            Collection of Evidence for the new AbstractConcept
	 * @return new AbstractConcept
	 */
    public AbstractConcept createConcept(String id, CV elementOf, ConceptClass ofType, Collection<EvidenceType> evidence) {
        return createConcept(id, "", "", elementOf, ofType, evidence);
    }

    /**
	 * Creates a new AbstractConcept with the given id, annotation, CV and
	 * ConceptClass. Adds the new AbstractConcept to the list of Concepts of
	 * this graph and returns it.
	 * 
	 * @param id
	 *            PARSER ID of the new AbstractConcept
	 * @param annotation
	 *            relevant annotation of the new AbstractConcept
	 * @param elementOf
	 *            CV of the new AbstractConcept
	 * @param ofType
	 *            ConceptClass of the new AbstractConcept
	 * @param evidencetype
	 *            EvidenceType of the new AbstractConcept
	 * @return new AbstractConcept
	 */
    public AbstractConcept createConcept(String id, String annotation, CV elementOf, ConceptClass ofType, EvidenceType evidencetype) {
        if (evidencetype == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptEvidenceTypeNull"), "[AbstractONDEXGraph - createConcept]"));
            throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptEvidenceTypeNull"));
        } else {
            List<EvidenceType> v = new ArrayList<EvidenceType>();
            v.add(evidencetype);
            return createConcept(id, annotation, "", elementOf, ofType, v);
        }
    }

    /**
	 * Creates a new AbstractConcept with the given id, annotation, CV and
	 * ConceptClass. Adds the new AbstractConcept to the list of Concepts of
	 * this graph and returns it.
	 * 
	 * @param id
	 *            PARSER ID of the new AbstractConcept
	 * @param annotation
	 *            relevant annotation of the new AbstractConcept
	 * @param elementOf
	 *            CV of the new AbstractConcept
	 * @param ofType
	 *            ConceptClass of the new AbstractConcept
	 * @return new AbstractConcept
	 */
    public AbstractConcept createConcept(String id, String annotation, CV elementOf, ConceptClass ofType, Collection<EvidenceType> evidence) {
        return createConcept(id, annotation, "", elementOf, ofType, evidence);
    }

    /**
	 * Creates a new AbstractConcept with the given id, annotation, description,
	 * CV and ConceptClass. Adds the new AbstractConcept to the list of Concepts
	 * of this graph and returns it.
	 * 
	 * @param id
	 *            PARSER ID of the new AbstractConcept
	 * @param annotation
	 *            relevant annotation of the new AbstractConcept
	 * @param description
	 *            other descriptions of the new AbstractConcept
	 * @param elementOf
	 *            CV of the new AbstractConcept
	 * @param ofType
	 *            ConceptClass of the new AbstractConcept
	 * @param evidencetype
	 *            EvidenceType of the new AbstractConcept
	 * @return new AbstractConcept
	 */
    public AbstractConcept createConcept(String id, String annotation, String description, CV elementOf, ConceptClass ofType, EvidenceType evidencetype) {
        if (evidencetype == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptEvidenceTypeNull"), "[AbstractONDEXGraph - createConcept]"));
            throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptEvidenceTypeNull"));
        } else {
            List<EvidenceType> v = new ArrayList<EvidenceType>();
            v.add(evidencetype);
            return createConcept(id, annotation, description, elementOf, ofType, v);
        }
    }

    /**
	 * Creates a new AbstractConcept with the given id, annotation, description,
	 * CV and ConceptClass. Adds the new AbstractConcept to the list of Concepts
	 * of this graph and returns it.
	 * 
	 * @param id
	 *            PARSER ID of the new AbstractConcept
	 * @param annotation
	 *            relevant annotation of the new AbstractConcept
	 * @param description
	 *            other descriptions of the new AbstractConcept
	 * @param elementOf
	 *            CV of the new AbstractConcept
	 * @param ofType
	 *            ConceptClass of the new AbstractConcept
	 * @return new AbstractConcept
	 */
    @Access(CREATE)
    public AbstractConcept createConcept(String id, String annotation, String description, CV elementOf, ConceptClass ofType, Collection<EvidenceType> evidence) {
        try {
            if (id == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptIDNull"), "[AbstractONDEXGraph - createConcept]"));
                throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptIDNull"));
            } else if (annotation == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptAnnotationNull"), "[AbstractONDEXGraph - createConcept]"));
                throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptAnnotationNull"));
            } else if (description == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptDescriptionNull"), "[AbstractONDEXGraph - createConcept]"));
                throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptDescriptionNull"));
            } else if (elementOf == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptElementOfNull"), "[AbstractONDEXGraph - createConcept]"));
                throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptElementOfNull"));
            } else if (ofType == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptOfTypeNull"), "[AbstractONDEXGraph - createConcept]"));
                throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptOfTypeNull"));
            } else if (evidence == null || evidence.size() == 0) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptEvidenceTypeNull"), "[AbstractONDEXGraph - createConcept]"));
                throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptEvidenceTypeNull"));
            } else {
                lastIdForConcept++;
                AbstractConcept c = storeConcept(sid, lastIdForConcept, id, annotation, description, elementOf, ofType, evidence);
                fireActionEvent(c, "create");
                return c;
            }
        } catch (OperationNotSupportedException onse) {
            fireEventOccurred(new AccessDeniedEvent(Config.properties.getProperty("AbstractONDEXGraph.ReadOnly"), "[AbstractONDEXGraph - createConcept]"));
            throw new AccessDeniedException(Config.properties.getProperty("AbstractONDEXGraph.ReadOnly"));
        }
    }

    /**
	 * Removes a AbstractConcept with the given ID from the list of Concepts of
	 * this graph and returns the removed AbstractConcept or null if
	 * unsuccessful.
	 * 
	 * @param id
	 *            unique ID of AbstractConcept to be removed
	 * @return deleted AbstractConcept
	 */
    @Access(DELETE)
    public AbstractConcept deleteConcept(Integer id) {
        try {
            if (id == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptIDNull"), "[AbstractONDEXGraph - deleteConcept]"));
                throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptIDNull"));
            } else {
                AbstractConcept c = this.getConcept(id);
                if (c != null) {
                    ONDEXView<AbstractConcept> viewC = this.getConceptsOfContext(c);
                    while (viewC.hasNext()) {
                        AbstractConcept ac = viewC.next();
                        ac.dropContext(c);
                    }
                    viewC.close();
                    ONDEXView<AbstractRelation> viewR = this.getRelationsOfContext(c);
                    while (viewR.hasNext()) {
                        AbstractRelation ar = viewR.next();
                        ar.dropContext(c);
                    }
                    viewR.close();
                    ONDEXView<AbstractRelation> view = this.getRelationsOfConcept(c);
                    while (view.hasNext()) {
                        this.deleteRelation(view.next().getId());
                    }
                    view.close();
                    removeConcept(id);
                    fireActionEvent(c, "delete");
                    return c;
                } else {
                    fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptIDNull"), "[AbstractONDEXGraph - deleteConcept]"));
                    throw new WrongParameterException(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptIDNull"));
                }
            }
        } catch (OperationNotSupportedException onse) {
            fireEventOccurred(new AccessDeniedEvent(Config.properties.getProperty("AbstractONDEXGraph.ReadOnly"), "[AbstractONDEXGraph - deleteConcept]"));
            throw new AccessDeniedException(Config.properties.getProperty("AbstractONDEXGraph.ReadOnly"));
        }
    }

    /**
	 * Returns the AbstractConcept associated with the given ID or null if no
	 * such Concept exists. Returns found AbstractConcept or null if
	 * unsuccessful.
	 * 
	 * @param id
	 *            unique ID of AbstractConcept
	 * @return found AbstractConcept
	 */
    @Access(GET)
    public AbstractConcept getConcept(Integer id) {
        if (id == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptIDNull"), "[AbstractONDEXGraph - getConcept]"));
            throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptIDNull"));
        } else {
            return retrieveConcept(id);
        }
    }

    /**
	 * Returns a ONDEXView of all Concepts contained in the list of Concepts of
	 * this graph.
	 * 
	 * @return ONDEXView<AbstractConcept>
	 */
    @Access(GET)
    public ONDEXView<AbstractConcept> getConcepts() {
        return retrieveConceptAll();
    }

    /**
	 * Returns a ONDEXView of all Concepts contained in the list of Concepts of
	 * this graph for a given CV.
	 * 
	 * @param cv
	 *            CV
	 * @return ONDEXView<AbstractConcept>
	 */
    @Access(GET)
    public ONDEXView<AbstractConcept> getConceptsOfCV(CV cv) {
        if (cv == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.CVNull"), "[AbstractONDEXGraph - getConceptsOfCV]"));
            throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.CVNull"));
        } else {
            return retrieveConceptAllCV(cv);
        }
    }

    /**
	 * Returns a ONDEXView of all Concepts contained in the list of Concepts of
	 * this graph for a given ConceptClass.
	 * 
	 * @param cc
	 *            ConceptClass
	 * @return ONDEXView<AbstractConcept>
	 */
    @Access(GET)
    public ONDEXView<AbstractConcept> getConceptsOfConceptClass(ConceptClass cc) {
        if (cc == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.ConceptClassNull"), "[AbstractONDEXGraph - getConceptsOfConceptClass]"));
            throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.ConceptClassNull"));
        } else {
            return retrieveConceptAllConceptClass(cc);
        }
    }

    /**
	 * Returns a ONDEXView of all Concepts contained in the list of Concepts of
	 * this graph for a given AttributeName.
	 * 
	 * @param an
	 *            AttributeName
	 * @return ONDEXView<AbstractConcept>
	 */
    @Access(GET)
    public ONDEXView<AbstractConcept> getConceptsOfAttributeName(AttributeName an) {
        if (an == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AttributeNameNull"), "[AbstractONDEXGraph - getConceptsOfAttributeName]"));
            throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AttributeNameNull"));
        } else {
            return retrieveConceptAllAttributeName(an);
        }
    }

    /**
	 * Returns a ONDEXView of all Concepts contained in the list of Concepts of
	 * this graph for a given EvidenceType.
	 * 
	 * @param et
	 *            EvidenceType
	 * @return ONDEXView<AbstractConcept>
	 */
    @Access(GET)
    public ONDEXView<AbstractConcept> getConceptsOfEvidenceType(EvidenceType et) {
        if (et == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.EvidenceTypeNull"), "[AbstractONDEXGraph - getConceptsOfEvidenceType]"));
            throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.getConceptsOfEvidenceType"));
        } else {
            return retrieveConceptAllEvidenceType(et);
        }
    }

    /**
	 * Returns a ONDEXView of all Concepts contained in the list of Concepts of
	 * this graph for a given context.
	 * 
	 * @param ac
	 *            AbstractConcept
	 * @return ONDEXView<AbstractConcept>
	 */
    @Access(GET)
    public ONDEXView<AbstractConcept> getConceptsOfContext(AbstractConcept ac) {
        if (ac == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptNull"), "[AbstractONDEXGraph - getConceptsOfContext]"));
            throw new NullValueException(Config.properties.getProperty("AbstractONDEXGraph.AbstractConceptNull"));
        } else {
            return retrieveConceptAllContext(ac);
        }
    }

    /**
	 * Returns a ONDEXView of all Concepts contained in the list of Concepts of
	 * this graph that are context for other Concepts/Relations.
	 * 
	 * @return ONDEXView<AbstractConcept>
	 */
    @Access(GET)
    public ONDEXView<AbstractConcept> getContext() {
        return retrieveContext();
    }

    /**
	 * Returns an array of ActionListeners
	 * 
	 * @return
	 */
    public ActionListener[] getActionListeners() {
        return listenerList.getListeners(ActionListener.class);
    }

    /**
	 * Adds a ONDEX graph listener to the list.
	 * 
	 * @param l
	 *            ONDEXGraphListener to add
	 */
    public void addONDEXGraphListener(GraphListener l) {
        listenerList.add(GraphListener.class, l);
    }

    /**
	 * Removes a ONDEX graph listener listener from the list.
	 * 
	 * @param l
	 *            ONDEXGraphListener
	 */
    public void removeONDEXGraphListener(GraphListener l) {
        listenerList.remove(GraphListener.class, l);
    }

    /**
	 * Returns the list of ONDEX graph listener listeners.
	 * 
	 * @return list of ONDEXGraphListeners
	 */
    public GraphListener[] getONDEXGraphListeners() {
        return listenerList.getListeners(GraphListener.class);
    }

    /**
	 * set permission on this object to p for the given scope s.
	 * @param s the access scope
	 * @param p the permissions level.
	 * @throws AccessDeniedException if not owner.
	 */
    @Override
    public void setPermission(AccessScope s, Permission p) throws AccessDeniedException {
        GlobalPermissions.getInstance(sid).setGraphPermission(sid, s, p);
    }

    /**
	 * returns the current permissions level on this object for
	 * a given access scope.
	 * @param s the access scope to query for
	 * @return the current permission level
	 */
    @Override
    public Permission getPermission(AccessScope s) {
        return GlobalPermissions.getInstance(sid).getGraphPermissionLevel(s);
    }

    /**
	 * if called by root, this sets the owner user id for this object
	 * to the given value. 
	 * @param uid the user id to set.
	 * @throws AccessDeniedException if not root.
	 */
    @Override
    public void setOwnerUserID(int uid) throws AccessDeniedException {
        GlobalPermissions.getInstance(sid).setGraphOwner(uid);
    }

    /**
	 * returns the owner user id for this object.
	 * @return the owner user id.
	 */
    @Override
    public int getOwnerUserID() {
        return GlobalPermissions.getInstance(sid).getGraphOwnerUserID();
    }

    /**
	 * if called by root, this sets the owner group id for this object
	 * to the given value. 
	 * @param gid the group id to set.
	 * @throws AccessDeniedException if not root.
	 */
    @Override
    public void setOwnerGroupID(int gid) throws AccessDeniedException {
        GlobalPermissions.getInstance(sid).setGraphGroup(gid);
    }

    /**
	 * returns the owner group id of this object.
	 * @return the owner group id
	 */
    @Override
    public int getOwnerGroupID() {
        return GlobalPermissions.getInstance(sid).getGraphOwnerGroupID();
    }

    /**
	 * This abstract method stores a given AbstractConcept.
	 * 
	 * @param sid
	 *            unique id for ondex graph
	 * @param id
	 *            unique id
	 * @param pid
	 *            parser id
	 * @param annotation
	 *            annotation of AbstractConcept
	 * @param description
	 *            description of AbstractConcept
	 * @param elementOf
	 *            CV of AbstractConcept
	 * @param ofType
	 *            ConceptClass of AbstractConcept
	 * @param evidence
	 *            Collection<EvidenceType> of AbstractConcept
	 * @return AbstractConcept
	 * @throws OperationNotSupportedException
	 * @throws AccessDeniedException 
	 * @throws NullValueException If the evidence collection contains null values.
	 */
    protected abstract AbstractConcept storeConcept(long sid, Integer id, String pid, String annotation, String description, CV elementOf, ConceptClass ofType, Collection<EvidenceType> evidence) throws OperationNotSupportedException;

    /**
	 * Removes a AbstractConcept for a given ID from the repository.
	 * 
	 * @param id
	 *            unique ID of AbstractConcept to be removed
	 * @return AbstractConcept
	 * @throws OperationNotSupportedException
	 * @throws AccessDeniedException 
	 */
    protected abstract AbstractConcept removeConcept(Integer id) throws OperationNotSupportedException;

    /**
	 * Retrieves a AbstractConcept for a given ID from the repository.
	 * 
	 * @param id
	 *            unique ID to look up
	 * @return AbstractConcept
	 */
    protected abstract AbstractConcept retrieveConcept(Integer id);

    /**
	 * Retrieves a ONDEXView of Concepts from the repository.
	 * 
	 * @return ONDEXView<AbstractConcept>
	 */
    protected abstract ONDEXView<AbstractConcept> retrieveConceptAll();

    /**
	 * Retrieves a ONDEXView of Concepts for a given CV from the repository.
	 * 
	 * @param cv
	 *            CV
	 * @return ONDEXView of Concepts
	 */
    protected abstract ONDEXView<AbstractConcept> retrieveConceptAllCV(CV cv);

    /**
	 * Retrieves a ONDEXView of Concepts for a given ConceptClass from the
	 * repository.
	 * 
	 * @param cc
	 *            ConceptClass
	 * @return ONDEXView<AbstractConcept>
	 */
    protected abstract ONDEXView<AbstractConcept> retrieveConceptAllConceptClass(ConceptClass cc);

    /**
	 * Retrieves a ONDEXView of Concepts for a given AttributeName from the
	 * repository.
	 * 
	 * @param an
	 *            AttributeName
	 * @return ONDEXView<AbstractConcept>
	 */
    protected abstract ONDEXView<AbstractConcept> retrieveConceptAllAttributeName(AttributeName an);

    /**
	 * Retrieves a ONDEXView of Concepts for a given EvidenceType from the
	 * repository.
	 * 
	 * @param et
	 *            EvidenceType
	 * @return ONDEXView<AbstractConcept>
	 */
    protected abstract ONDEXView<AbstractConcept> retrieveConceptAllEvidenceType(EvidenceType et);

    /**
	 * Retrieves a ONDEXView of Concepts for a given context from the
	 * repository.
	 * 
	 * @param concept
	 *            AbstractConcept
	 * @return ONDEXView<AbstractConcept>
	 * @throws AccessDeniedException 
	 */
    protected abstract ONDEXView<AbstractConcept> retrieveConceptAllContext(AbstractConcept concept);

    /**
	 * Retrieves a ONDEXView of Concepts that are the context for other
	 * Concepts/Relations from the repository.
	 * 
	 * @return ONDEXView<AbstractConcept>
	 */
    protected abstract ONDEXView<AbstractConcept> retrieveContext();

    /**
	 * This abstract method stores a given AbstractRelation.
	 * 
	 * @param sid
	 *            unique id
	 * @param id
	 *            Integer
	 * @param fromConcept
	 *            from AbstractConcept
	 * @param toConcept
	 *            to AbstractConcept
	 * @param qualifier
	 *            qualifier AbstractConcept
	 * @param ofType
	 *            RelationType
	 * @param evidence
	 *            Collection<EvidenceType> of AbstractRelation
	 * @return AbstractRelation
	 * @throws OperationNotSupportedException
	 * @throws NullValueException 
	 * @throws AccessDeniedException 
	 */
    protected abstract AbstractRelation storeRelation(long sid, Integer id, AbstractConcept fromConcept, AbstractConcept toConcept, AbstractConcept qualifier, RelationType ofType, Collection<EvidenceType> evidence) throws OperationNotSupportedException;

    /**
	 * Remove a AbstractRelation by the given from AbstractConcept, to
	 * AbstractConcept, qualifier AbstractConcept and RelationType from the
	 * repository.
	 * 
	 * @param fromConcept
	 *            from AbstractConcept
	 * @param toConcept
	 *            to AbstractConcept
	 * @param qualifier
	 *            qualifier AbstractConcept
	 * @param ofType
	 *            RelationType
	 * @return AbstractRelation
	 * @throws OperationNotSupportedException
	 * @throws AccessDeniedException 
	 */
    protected abstract AbstractRelation removeRelation(AbstractConcept fromConcept, AbstractConcept toConcept, AbstractConcept qualifier, RelationType ofType) throws OperationNotSupportedException;

    /**
	 * Remove a AbstractRelation by the given unique Id from the repository.
	 * 
	 * @param id
	 *            Integer
	 * @return AbstractRelation
	 * @throws OperationNotSupportedException
	 * @throws AccessDeniedException 
	 */
    protected abstract AbstractRelation removeRelation(Integer id) throws OperationNotSupportedException;

    /**
	 * Retrieves a AbstractRelation by the given from AbstractConcept, to
	 * AbstractConcept, qualifier AbstractConcept and RelationType from the
	 * repository.
	 * 
	 * @param fromConcept
	 *            from AbstractConcept
	 * @param toConcept
	 *            to AbstractConcept
	 * @param qualifier
	 *            qualifier AbstractConcept
	 * @param ofType
	 *            RelationType
	 * @return AbstractRelation
	 * @throws AccessDeniedException 
	 * @throws NullValueException if from or to concept are null.
	 */
    protected abstract AbstractRelation retrieveRelation(AbstractConcept fromConcept, AbstractConcept toConcept, AbstractConcept qualifier, RelationType ofType);

    /**
	 * Retrieves a AbstractRelation by the given unique Id from the repository.
	 * 
	 * @param id
	 *            Integer
	 * @return AbstractRelation
	 * @throws AccessDeniedException 
	 */
    protected abstract AbstractRelation retrieveRelation(Integer id);

    /**
	 * Retrieves a ONDEXView of Relations from the repository.
	 * 
	 * @return ONDEXView<AbstractRelation>
	 */
    protected abstract ONDEXView<AbstractRelation> retrieveRelationAll();

    /**
	 * Retrieves a ONDEXView of Relations that are connected to a given
	 * AbstractConcept from the repository.
	 * 
	 * @param c
	 *            AbstractConcept
	 * @return ONDEXView<AbstractRelation>
	 * @throws AccessDeniedException 
	 */
    protected abstract ONDEXView<AbstractRelation> retrieveRelationAllConcept(AbstractConcept c);

    /**
	 * Retrieves a ONDEXView of Relations with a given CV from the repository.
	 * 
	 * @param cv
	 *            CV
	 * @return ONDEXView<AbstractRelation>
	 */
    protected abstract ONDEXView<AbstractRelation> retrieveRelationAllCV(CV cv);

    /**
	 * Retrieves a ONDEXView of Relations with a given ConceptClass from the
	 * repository.
	 * 
	 * @param cc
	 *            ConceptClass
	 * @return ONDEXView<AbstractRelation>
	 */
    protected abstract ONDEXView<AbstractRelation> retrieveRelationAllConceptClass(ConceptClass cc);

    /**
	 * Retrieves a ONDEXView of Relations with a given AttributeName from the
	 * repository.
	 * 
	 * @param an
	 *            AttributeName
	 * @return ONDEXView<AbstractRelation>
	 */
    protected abstract ONDEXView<AbstractRelation> retrieveRelationAllAttributeName(AttributeName an);

    /**
	 * Retrieves a ONDEXView of Relations with a given EvidenceType from the
	 * repository.
	 * 
	 * @param et
	 *            EvidenceType
	 * @return ONDEXView<AbstractRelation>
	 */
    protected abstract ONDEXView<AbstractRelation> retrieveRelationAllEvidenceType(EvidenceType et);

    /**
	 * Retrieves a ONDEXView of Relations with a given context from the
	 * repository.
	 * 
	 * @param concept
	 *            AbstractConcept
	 * @return ONDEXView<AbstractRelation>
	 * @throws AccessDeniedException 
	 */
    protected abstract ONDEXView<AbstractRelation> retrieveRelationAllContext(AbstractConcept concept) throws AccessDeniedException;

    /**
	 * Retrieves a ONDEXView of Relations with a given RelationType from the
	 * repository.
	 * 
	 * @param rt
	 *            RelationType
	 * @return ONDEXView<AbstractRelation>
	 */
    protected abstract ONDEXView<AbstractRelation> retrieveRelationAllRelationType(RelationType rt);
}
