package org.coode.existentialtree.model;

import org.coode.outlinetree.util.ExistentialFillerAccumulator;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.hierarchy.OWLObjectHierarchyProvider;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Apr 24, 2007<br><br>
 * <p/>
 */
public class OWLExistentialHierarchyProvider extends AbstractHierarchyProvider<OWLClassExpression> {

    private Set<OWLOntology> ontologies;

    private ExistentialFillerAccumulator fillerAccumulator = new ExistentialFillerAccumulator();

    private OWLClassExpression root;

    private OWLObjectHierarchyProvider<OWLObjectProperty> hp;

    public OWLExistentialHierarchyProvider(OWLModelManager owlOntologyManager) {
        super(owlOntologyManager.getOWLOntologyManager());
        ontologies = owlOntologyManager.getOntologies();
        root = owlOntologyManager.getOWLDataFactory().getOWLThing();
        hp = owlOntologyManager.getOWLHierarchyManager().getOWLObjectPropertyHierarchyProvider();
    }

    public void setOntologies(Set<OWLOntology> ontologies) {
        this.ontologies = ontologies;
    }

    public Set<OWLClassExpression> getRoots() {
        return root != null ? Collections.singleton(root) : Collections.EMPTY_SET;
    }

    public Set<OWLClassExpression> getChildren(OWLClassExpression object) {
        return fillerAccumulator.getExistentialFillers(object, ontologies);
    }

    public Set<OWLClassExpression> getParents(OWLClassExpression object) {
        return Collections.emptySet();
    }

    public Set<OWLClassExpression> getEquivalents(OWLClassExpression object) {
        return Collections.emptySet();
    }

    public boolean containsReference(OWLClassExpression object) {
        return object.equals(root);
    }

    public void setRoot(OWLClassExpression selectedClass) {
        root = selectedClass;
    }

    public void setProp(OWLObjectProperty prop) {
        if (prop != null) {
            Set<OWLObjectProperty> propAndDescendants = new HashSet<OWLObjectProperty>(hp.getDescendants(prop));
            propAndDescendants.add(prop);
            fillerAccumulator.setProperties(propAndDescendants);
        } else {
            fillerAccumulator.setProperties(null);
        }
    }
}
