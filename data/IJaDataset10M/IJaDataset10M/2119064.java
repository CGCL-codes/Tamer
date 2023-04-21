package backend.core.persistent.perst;

import java.util.Iterator;
import org.garret.perst.IPersistentMap;
import org.garret.perst.IPersistentSet;
import backend.core.AbstractConcept;
import backend.core.AbstractONDEXIterator;
import backend.core.AttributeName;
import backend.core.CV;
import backend.core.ConceptAccession;
import backend.core.ConceptClass;
import backend.core.ConceptGDS;
import backend.core.ConceptName;
import backend.core.EvidenceType;
import backend.core.security.Session;
import backend.event.type.DuplicatedEntry;

/**
 * This class represents a persistent implementation of
 * the abstract Concept. It uses Perst JAVA datatypes.
 * 
 * @author taubertj
 *
 */
public class PerstConcept extends AbstractConcept {

    private static final long serialVersionUID = 1L;

    private IPersistentSet<EvidenceType> evidence;

    private IPersistentMap<String, ConceptName> conames;

    private IPersistentMap<String, ConceptAccession> coaccessions;

    private IPersistentMap<String, ConceptGDS> cogds;

    /**
	 * Constructor which fills all fields of Concept and initalizies 
	 * empty hashtables for possible concept names and concept accessions
	 * 
	 * @param sid - unique id
	 * @param s - session context
	 * @param id - unique ID of this Concept
	 * @param pid - parser assigned ID
	 * @param annotation - relevant annotations of this Concept
	 * @param description - every associated description of this Concept
	 * @param elementOf - CV to which this Concept belongs to
	 * @param ofType - ConceptClass of this Concept
	 */
    PerstConcept(long sid, Session s, PerstEnv penv, Integer id, String pid, String annotation, String description, CV elementOf, ConceptClass ofType) {
        super(sid, s, id, pid, annotation, description, elementOf, ofType);
        this.evidence = penv.getStorage().createSet();
        this.conames = penv.getStorage().createMap(String.class);
        this.coaccessions = penv.getStorage().createMap(String.class);
        this.cogds = penv.getStorage().createMap(String.class);
    }

    @Override
    protected boolean dropEvidenceType(Session s, EvidenceType evidencetype) {
        return evidence.remove(evidencetype);
    }

    @Override
    protected void saveEvidenceType(Session s, EvidenceType evidencetype) {
        if (evidence.contains(evidencetype)) {
            fireEventOccurred(new DuplicatedEntry("EvidenceType with name: " + evidencetype.getName(s) + " already contained in set."));
        } else {
            evidence.add(evidencetype);
        }
    }

    @Override
    protected AbstractONDEXIterator<EvidenceType> getEvidenceTypes(Session s) {
        return new PerstONDEXIterator<EvidenceType>(evidence);
    }

    @Override
    protected ConceptName storeConceptName(Session s, ConceptName cn) {
        if (conames.containsKey(cn.getName(s))) {
            fireEventOccurred(new DuplicatedEntry("Duplicated ConceptName with name: " + cn.getName(s)));
        } else {
            conames.put(cn.getName(s), cn);
        }
        return cn;
    }

    @Override
    protected ConceptName removeConceptName(Session s, String name) {
        return conames.remove(name);
    }

    @Override
    protected ConceptName preferredConceptName(Session s) {
        Iterator it = conames.values().iterator();
        while (it.hasNext()) {
            ConceptName cn = (ConceptName) it.next();
            if (cn.isPreferred(s)) return cn;
        }
        return null;
    }

    @Override
    protected ConceptName retrieveConceptName(Session s, String name) {
        return conames.get(name);
    }

    @Override
    protected AbstractONDEXIterator<ConceptName> retrieveConceptNameAll(Session s) {
        return new PerstONDEXIterator<ConceptName>(conames.values());
    }

    @Override
    protected ConceptAccession storeConceptAccession(Session s, ConceptAccession ca) {
        if (coaccessions.containsKey(ca.getAccession(s) + ca.getElementOf(s).getName(s))) {
            fireEventOccurred(new DuplicatedEntry("Duplicated ConceptAccession with accession and cv: " + ca.getAccession(s) + " (" + ca.getElementOf(s).getName(s) + ")"));
        } else {
            coaccessions.put(ca.getAccession(s) + ca.getElementOf(s).getName(s), ca);
        }
        return ca;
    }

    @Override
    protected ConceptAccession removeConceptAccession(Session s, String accession, CV elementOf) {
        return coaccessions.remove(accession + elementOf.getName(s));
    }

    @Override
    protected ConceptAccession retrieveConceptAccession(Session s, String accession, CV elementOf) {
        return coaccessions.get(accession + elementOf.getName(s));
    }

    @Override
    protected AbstractONDEXIterator<ConceptAccession> retrieveConceptAccessionAll(Session s) {
        return new PerstONDEXIterator<ConceptAccession>(coaccessions.values());
    }

    @Override
    protected ConceptGDS storeConceptGDS(Session s, ConceptGDS gds) {
        if (cogds.containsKey(gds.getAttrname(s).getName(s))) {
            fireEventOccurred(new DuplicatedEntry("Duplicated ConceptGDS with AttributeName name: " + gds.getAttrname(s).getName(s)));
        } else {
            cogds.put(gds.getAttrname(s).getName(s), gds);
        }
        return gds;
    }

    @Override
    protected ConceptGDS removeConceptGDS(Session s, AttributeName attrname) {
        return cogds.remove(attrname.getName(s));
    }

    @Override
    protected ConceptGDS retrieveConceptGDS(Session s, AttributeName attrname) {
        return cogds.get(attrname.getName(s));
    }

    @Override
    protected AbstractONDEXIterator<ConceptGDS> retrieveConceptGDSAll(Session s) {
        return new PerstONDEXIterator<ConceptGDS>(cogds.values());
    }
}
