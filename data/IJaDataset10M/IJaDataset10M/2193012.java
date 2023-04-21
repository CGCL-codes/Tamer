package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph;

import edu.smu.tspell.wordnet.Synset;
import org.semanticweb.owlapi.model.IRI;
import ru.spbu.math.ontologycomparison.zhukova.util.IHashTable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public interface IOntologyGraph {

    Map<IRI, IOntologyConcept> getUriToConcept();

    Set<IRI> getConceptUris();

    Set<IRI> getPropertyUris();

    Set<String> getConceptLabels();

    Set<String> getPropertyLabels();

    Set<Synset> getSynsets();

    IHashTable<String, IOntologyConcept, Set<IOntologyConcept>> getLabelToConcept();

    IHashTable<Synset, IOntologyConcept, Set<IOntologyConcept>> getSynsetToConcept();

    void setSynsetToConcept(IHashTable<Synset, IOntologyConcept, Set<IOntologyConcept>> synsetToConcept);

    Set<IOntologyConcept> getRoots();

    Map<IRI, IOntologyProperty> getUriToProperty();

    IHashTable<String, IOntologyProperty, Set<IOntologyProperty>> getLabelToProperty();

    IOntologyConcept getConceptByURI(IRI uri);

    Collection<IOntologyConcept> getConcepts();

    Collection<IOntologyProperty> getProperties();
}
