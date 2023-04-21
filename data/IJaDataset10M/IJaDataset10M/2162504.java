package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl;

import edu.smu.tspell.wordnet.Synset;
import org.semanticweb.owlapi.model.IRI;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyProperty;
import ru.spbu.math.ontologycomparison.zhukova.util.IHashTable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class OntologyGraph implements IOntologyGraph {

    private final Map<IRI, IOntologyConcept> uriToConcept;

    private final IHashTable<String, IOntologyConcept, Set<IOntologyConcept>> labelToConcept;

    private IHashTable<Synset, IOntologyConcept, Set<IOntologyConcept>> synsetToConcept;

    private final Set<IOntologyConcept> roots;

    private final Map<IRI, IOntologyProperty> uriToProperty;

    private final IHashTable<String, IOntologyProperty, Set<IOntologyProperty>> labelToProperty;

    public OntologyGraph(Set<IOntologyConcept> roots, Map<IRI, IOntologyConcept> uriToConcept, IHashTable<String, IOntologyConcept, Set<IOntologyConcept>> labelToConcept, Map<IRI, IOntologyProperty> uriToProperty, IHashTable<String, IOntologyProperty, Set<IOntologyProperty>> labelToProperty) {
        this.uriToConcept = uriToConcept;
        this.labelToConcept = labelToConcept;
        this.roots = roots;
        this.uriToProperty = uriToProperty;
        this.labelToProperty = labelToProperty;
    }

    public Map<IRI, IOntologyConcept> getUriToConcept() {
        return uriToConcept;
    }

    public Set<IRI> getConceptUris() {
        return uriToConcept.keySet();
    }

    public Set<IRI> getPropertyUris() {
        return uriToProperty.keySet();
    }

    public Set<String> getConceptLabels() {
        return labelToConcept.keySet();
    }

    public Set<String> getPropertyLabels() {
        return labelToProperty.keySet();
    }

    public Set<Synset> getSynsets() {
        return synsetToConcept.keySet();
    }

    public IHashTable<String, IOntologyConcept, Set<IOntologyConcept>> getLabelToConcept() {
        return labelToConcept;
    }

    public IHashTable<Synset, IOntologyConcept, Set<IOntologyConcept>> getSynsetToConcept() {
        return synsetToConcept;
    }

    public void setSynsetToConcept(IHashTable<Synset, IOntologyConcept, Set<IOntologyConcept>> synsetToConcept) {
        this.synsetToConcept = synsetToConcept;
    }

    public Set<IOntologyConcept> getRoots() {
        return roots;
    }

    public Map<IRI, IOntologyProperty> getUriToProperty() {
        return uriToProperty;
    }

    public IHashTable<String, IOntologyProperty, Set<IOntologyProperty>> getLabelToProperty() {
        return labelToProperty;
    }

    public IOntologyConcept getConceptByURI(IRI uri) {
        return this.uriToConcept != null ? this.uriToConcept.get(uri) : null;
    }

    public Collection<IOntologyProperty> getProperties() {
        return uriToProperty.values();
    }

    public Collection<IOntologyConcept> getConcepts() {
        return uriToConcept.values();
    }
}
