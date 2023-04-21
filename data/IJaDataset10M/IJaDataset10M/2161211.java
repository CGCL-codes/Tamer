package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import datastructures.Concept;
import datastructures.ConceptInstance;

/**
 * Some utility methods for handling of concepts
 * @author Redl
 */
public class ConceptUtil {

    /**
	 * Finds the concept with a given name in a list of concepts. Will return null if the concept is not in the list.
	 * @param conceptList Source
	 * @param name Name of the concept to search for
	 * @return Concept The concept with the given name or null if it is not in the list
	 */
    public static Concept conceptByName(List<Concept> conceptList, String name) {
        Concept template = new Concept(name);
        int index;
        if ((index = conceptList.indexOf(template)) >= 0) {
            return conceptList.get(index);
        } else {
            return null;
        }
    }

    /**
	 * Creates a dummy concept with exactly one instance, namely the given text.
	 * @param name Name of the dummy concept 
	 * @param text Concent of the dummy concept (this text is treated as value of the only instance)
	 * @return Concept
	 */
    public static Concept dummyConcept(String name, String text) {
        Concept dummy = new Concept(name);
        dummy.addInstance(new ConceptInstance(text));
        return dummy;
    }

    /**
	 * Takes a concept list, cleans it up by merging concepts with synonym names or names which are substrings of each other, and returns a pruned list of
	 * concepts. Also concept instances will be pruned.
	 * @param conceptlist
	 * @return
	 */
    public static List<Concept> pruning(List<Concept> conceptlist) {
        Collection<String> relevantConceptNames = new ArrayList<String>();
        for (Concept c : conceptlist) {
            relevantConceptNames.add(Synonyms.unify(c.getName()));
        }
        relevantConceptNames = util.CollectionUtil.removeDuplicates(relevantConceptNames);
        relevantConceptNames = util.CollectionUtil.keepSubsetMinimal(relevantConceptNames);
        List<Concept> newConceptList = new ArrayList<Concept>();
        for (String conceptName : relevantConceptNames) {
            if (conceptName.length() > 2) {
                newConceptList.add(new Concept(conceptName));
            }
        }
        for (Concept cNew : newConceptList) {
            Collection<String> relevantConceptInstances = new ArrayList<String>();
            for (Concept cOrig : conceptlist) {
                if (Synonyms.unify(cOrig.getName()).contains(cNew.getName())) {
                    relevantConceptInstances.addAll(util.CollectionUtil.collectionToStringCollection(cOrig.getInstances()));
                }
            }
            relevantConceptInstances = util.CollectionUtil.removeDuplicates(relevantConceptInstances);
            relevantConceptInstances = util.CollectionUtil.keepSubsetMaximal(relevantConceptInstances);
            for (String conceptInstance : relevantConceptInstances) {
                cNew.addInstance(new ConceptInstance(conceptInstance));
            }
        }
        return newConceptList;
    }

    /**
	 * Removes all concepts from a list of concepts that have less than minInstances instances.
	 * @param conceptlist
	 * @param minInstances minimal number of instances
	 * @return  a new list of concepts
	 */
    public static List<Concept> pruneSize(List<Concept> conceptlist, int minInstances) {
        List<Concept> pruned = new ArrayList<Concept>(conceptlist.size());
        for (Concept c : conceptlist) {
            if (c.getInstances().size() >= minInstances) pruned.add(c);
        }
        return pruned;
    }
}
