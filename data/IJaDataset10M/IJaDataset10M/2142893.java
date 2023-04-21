package uk.co.ordnancesurvey.confluence.model.usage;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * This class uses a helper to visit an {@link OWLAxiom} and extract a list of
 * main {@link OWLEntity} instances that axiom is about. To use this class use
 * the {@link #getMainEntities(OWLAxiom)} method.
 * 
 * Note that this class is not thread safe. If you call the
 * {@link #getMainEntities(OWLAxiom)} method before another thread has already
 * called the method, the resulting {@link Set}s will be wrong.
 * 
 * @author rdenaux
 * 
 */
public class MainAxiomPartExtractor {

    /**
     * Stores the set of main entities found in the visited {@link OWLAxiom}s.
     * Note that having this variable as a class variable makes this class
     * thread unsafe.
     */
    private final Set<OWLEntity> mainEntities = new HashSet<OWLEntity>();

    /**
     * Helper class, visits the {@link OWLAxiom} and calls
     * {@link #add(OWLEntity)} for each main entity found in every visited
     * {@link OWLAxiom}
     */
    private final OWLAxiomVisitor helper = new AbstractMainAxiomPartHandler() {

        /**
         * Adds entity to the set of main entities.
         * 
         * @see uk.co.ordnancesurvey.confluence.model.usage.AbstractMainAxiomPartHandler#handleMainAxiomPart(org.semanticweb.owlapi.model.OWLEntity)
         */
        @Override
        protected void handleMainAxiomPart(OWLEntity entity) {
            add(entity);
        }
    };

    /**
     * Adds aOWLEntity to the list of main Entities in the
     * 
     * @param aOWLEntity
     */
    private void add(OWLEntity aOWLEntity) {
        mainEntities.add(aOWLEntity);
    }

    /**
     * Returns the main entities
     * 
     * @param aAxiom
     * @return
     */
    public Set<OWLEntity> getMainEntities(OWLAxiom aAxiom) {
        mainEntities.clear();
        aAxiom.accept(helper);
        return Collections.unmodifiableSet(mainEntities);
    }
}
