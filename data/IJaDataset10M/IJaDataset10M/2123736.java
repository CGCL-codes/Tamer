package backend.core.memory;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import backend.core.AbstractConcept;
import backend.core.AttributeName;
import backend.core.CV;
import backend.core.ConceptAccession;
import backend.core.ConceptClass;
import backend.core.ConceptGDS;
import backend.core.ConceptName;

/**
 * This class represents a pure memory based implementation of
 * the abstract Concept. It uses standard JAVA datatypes.
 * 
 * @author taubertj
 *
 */
public class Concept extends AbstractConcept {

    private static final long serialVersionUID = 1L;

    private Hashtable<String, ConceptName> conames;

    private Hashtable<String, Hashtable<CV, ConceptAccession>> coaccessions;

    private Hashtable<AttributeName, ConceptGDS> cogds;

    /**
	 * Constructor which fills all fields of Concept and initalizies 
	 * empty hashtables for possible concept names and concept accessions
	 * 
	 * @param id - unique ID of this Concept
	 * @param annotation - relevant annotations of this Concept
	 * @param description - every associated description of this Concept
	 * @param elementOf - CV to which this Concept belongs to
	 * @param ofType - ConceptClass of this Concept
	 */
    Concept(String id, String annotation, String description, CV elementOf, ConceptClass ofType) {
        super(id, annotation, description, elementOf, ofType);
        this.conames = new Hashtable<String, ConceptName>();
        this.coaccessions = new Hashtable<String, Hashtable<CV, ConceptAccession>>();
        this.cogds = new Hashtable<AttributeName, ConceptGDS>();
    }

    @Override
    protected ConceptName storeConceptName(ConceptName cn) {
        conames.put(cn.getName(), cn);
        return cn;
    }

    @Override
    protected ConceptName removeConceptName(String name) {
        return conames.remove(name);
    }

    @Override
    protected ConceptName preferredConceptName() {
        Iterator it = conames.values().iterator();
        while (it.hasNext()) {
            ConceptName cn = (ConceptName) it.next();
            if (cn.isPreferred()) return cn;
        }
        return null;
    }

    @Override
    protected ConceptName retrieveConceptName(String name) {
        return conames.get(name);
    }

    @Override
    protected Collection<ConceptName> retrieveConceptNameAll() {
        return conames.values();
    }

    @Override
    protected ConceptAccession storeConceptAccession(ConceptAccession ca) {
        if (coaccessions.get(ca.getAccession()) == null) coaccessions.put(ca.getAccession(), new Hashtable<CV, ConceptAccession>());
        Hashtable<CV, ConceptAccession> coaccessions2 = coaccessions.get(ca.getAccession());
        coaccessions2.put(ca.getElementOf(), ca);
        return ca;
    }

    @Override
    protected ConceptAccession removeConceptAccession(String accession, CV elementOf) {
        if (coaccessions.containsKey(accession)) {
            Hashtable<CV, ConceptAccession> coaccessions2 = coaccessions.get(accession);
            ConceptAccession ca = coaccessions2.remove(elementOf);
            if (coaccessions2.size() == 0) {
                coaccessions.remove(accession);
            }
            return ca;
        }
        return null;
    }

    @Override
    protected ConceptAccession retrieveConceptAccession(String accession, CV elementOf) {
        if (coaccessions.containsKey(accession)) {
            Hashtable<CV, ConceptAccession> coaccessions2 = coaccessions.get(accession);
            return coaccessions2.get(elementOf);
        }
        return null;
    }

    @Override
    protected Collection<ConceptAccession> retrieveConceptAccessionAll() {
        Vector<ConceptAccession> v = new Vector<ConceptAccession>();
        Iterator it = coaccessions.keySet().iterator();
        while (it.hasNext()) {
            Hashtable<CV, ConceptAccession> coaccessions2 = coaccessions.get(it.next());
            v.addAll(coaccessions2.values());
        }
        return v;
    }

    @Override
    protected ConceptGDS storeConceptGDS(ConceptGDS gds) {
        cogds.put(gds.getAttrname(), gds);
        return gds;
    }

    @Override
    protected ConceptGDS removeConceptGDS(AttributeName attrname) {
        return cogds.remove(attrname);
    }

    @Override
    protected ConceptGDS retrieveConceptGDS(AttributeName attrname) {
        return cogds.get(attrname);
    }

    @Override
    protected Collection<ConceptGDS> retrieveConceptGDSAll() {
        return cogds.values();
    }
}
