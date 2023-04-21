package de.ifgi.simcat.reasoner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import de.ifgi.simcat.reasoner.alcn.ALCNReasoner;
import de.ifgi.simcat.reasoner.alcq.ALCQReasoner;
import de.ifgi.simcat.reasoner.alcqh.ALCQHReasoner;
import de.ifgi.simcat.reasoner.constructors.ExistentialQuantification;
import de.ifgi.simcat.reasoner.constructors.Intersection;
import de.ifgi.simcat.reasoner.constructors.Negation;
import de.ifgi.simcat.reasoner.constructors.QualifiedNumberRestriction;
import de.ifgi.simcat.reasoner.constructors.Union;
import de.ifgi.simcat.reasoner.constructors.UniversalQuantification;
import de.ifgi.simcat.server.ServerConstants;

public class ConceptTaxonomyBuilder {

    private ArrayList<Concept> allConcepts;

    private ConceptTaxonomy taxonomy;

    private AbstractReasoner reasoner;

    private TBox tBox;

    public ConceptTaxonomy createConceptTaxonomy(TBox tBox) {
        this.tBox = tBox;
        allConcepts = (ArrayList<Concept>) tBox.getConcepts();
        taxonomy = new ConceptTaxonomy();
        String DLLogic = tBox.getDLLogic();
        if ("ALCN".equals(DLLogic)) {
            reasoner = new ALCNReasoner(tBox.getRoles());
        } else if ("ALCQ".equals(DLLogic)) {
            reasoner = new ALCQReasoner(tBox.getRoles());
        } else {
            reasoner = new ALCQHReasoner(tBox.getRoles());
        }
        ArrayList<Concept> primitiveConcepts = new ArrayList<Concept>();
        NamedConcept topTemp = null;
        for (Concept concept : allConcepts) {
            if (concept instanceof NamedConcept && ((NamedConcept) concept).isPrimitive()) {
                if ((ServerConstants.TOP.equals(((NamedConcept) concept).getName()))) {
                    topTemp = (NamedConcept) concept;
                } else {
                    ConceptTaxonomyNode node = taxonomy.getNode(concept);
                    taxonomy.getTop().addDirectSubNode(node);
                    node.addDirectSubNode(taxonomy.getBottom());
                    primitiveConcepts.add(concept);
                }
            }
        }
        ArrayList<Concept> nonPrimitiveConcepts = new ArrayList<Concept>(allConcepts);
        nonPrimitiveConcepts.removeAll(primitiveConcepts);
        nonPrimitiveConcepts.remove(topTemp);
        while (!nonPrimitiveConcepts.isEmpty()) {
            NamedConcept concept = (NamedConcept) nonPrimitiveConcepts.get(0);
            boolean conceptFound = false;
            Set<NamedConcept> inspectedConcepts = new HashSet<NamedConcept>();
            inspectedConcepts.add(concept);
            while (!conceptFound) {
                inspectedConcepts.add(concept);
                conceptFound = true;
                for (NamedConcept usedConcept : concept.getUsedConcepts()) {
                    if (!taxonomy.containsNode(usedConcept)) {
                        if (inspectedConcepts.contains(usedConcept)) {
                            continue;
                        }
                        concept = usedConcept;
                        inspectedConcepts.add(usedConcept);
                        conceptFound = false;
                        break;
                    }
                }
            }
            nonPrimitiveConcepts.remove(concept);
            ConceptTaxonomyNode node = taxonomy.getNode(concept);
            node.resetLists();
            Set<ConceptTaxonomyNode> topSearchResult = topSearch(node, taxonomy.getTop());
            node.resetLists();
            Set<ConceptTaxonomyNode> bottomSearchResult = bottomSearch(node, taxonomy.getBottom(), topSearchResult);
            Set<ConceptTaxonomyNode> equivalencesSearchResult = findEquivalences(topSearchResult, bottomSearchResult);
            for (ConceptTaxonomyNode superNode : topSearchResult) {
                superNode.addDirectSubNode(node);
            }
            for (ConceptTaxonomyNode subNode : bottomSearchResult) {
                node.addDirectSubNode(subNode);
            }
            for (ConceptTaxonomyNode equivalentNode : equivalencesSearchResult) {
                node.addEquivalentNode(equivalentNode);
            }
        }
        return taxonomy;
    }

    /**
	 * @param topSearchResult
	 * @param bottomSearchResult
	 */
    private Set<ConceptTaxonomyNode> findEquivalences(Set<ConceptTaxonomyNode> topSearchResult, Set<ConceptTaxonomyNode> bottomSearchResult) {
        Set<ConceptTaxonomyNode> equivalencesResult = new HashSet<ConceptTaxonomyNode>();
        for (ConceptTaxonomyNode node2 : topSearchResult) {
            if (bottomSearchResult.contains(node2)) {
                equivalencesResult.add(node2);
            }
        }
        topSearchResult.removeAll(equivalencesResult);
        bottomSearchResult.removeAll(equivalencesResult);
        return equivalencesResult;
    }

    private Set<ConceptTaxonomyNode> topSearch(ConceptTaxonomyNode c, ConceptTaxonomyNode x) {
        Set<ConceptTaxonomyNode> result = new HashSet<ConceptTaxonomyNode>();
        c.addToVisited(x);
        Set<ConceptTaxonomyNode> positiveSuccessors = new HashSet<ConceptTaxonomyNode>();
        for (ConceptTaxonomyNode y : x.getDirectSubNodes()) {
            if (isToldDisjoint(positiveSuccessors, c, y)) {
                c.addToNegative(y);
            } else if (c.isToldSubsumee(y)) {
                c.addToPositive(y);
                positiveSuccessors.add(y);
            } else if (enhancedTopSubs(y, c)) {
                positiveSuccessors.add(y);
            }
        }
        if (positiveSuccessors.isEmpty()) {
            result.add(x);
            return result;
        } else {
            for (ConceptTaxonomyNode y : positiveSuccessors) {
                if (!c.isVisited(y)) {
                    result.addAll(topSearch(c, y));
                }
            }
            return result;
        }
    }

    private boolean isToldDisjoint(Set<ConceptTaxonomyNode> positiveSuccessors, ConceptTaxonomyNode c, ConceptTaxonomyNode y) {
        Collection<NamedConcept> disjoints = new HashSet<NamedConcept>();
        disjoints.addAll(((NamedConcept) c.getConcept()).getDisjointConcepts());
        for (ConceptTaxonomyNode positiveSuccessor : positiveSuccessors) {
            disjoints.addAll(positiveSuccessor.getAllDisjointConcepts());
        }
        if (disjoints.contains(y.getConcept())) {
            return true;
        }
        return false;
    }

    private boolean enhancedTopSubs(ConceptTaxonomyNode y, ConceptTaxonomyNode c) {
        if (c.isPositive(y)) {
            return true;
        } else if (c.isNegative(y)) {
            return false;
        } else {
            boolean possibleSuperNode = true;
            if (possibleSuperNode && checkSubsumption(c.getConcept(), y.getConcept())) {
                c.addToPositive(y);
                return true;
            } else {
                c.addToNegative(y);
                return false;
            }
        }
    }

    private Set<ConceptTaxonomyNode> bottomSearch(ConceptTaxonomyNode c, ConceptTaxonomyNode x, Set<ConceptTaxonomyNode> topSearchResult) {
        Set<ConceptTaxonomyNode> result = new HashSet<ConceptTaxonomyNode>();
        c.addToVisited(x);
        Set<ConceptTaxonomyNode> positivePredeccessors = new HashSet<ConceptTaxonomyNode>();
        for (ConceptTaxonomyNode y : x.getDirectSuperNodes()) {
            if (enhancedBottomSupers(y, c, topSearchResult)) {
                positivePredeccessors.add(y);
            }
        }
        if (positivePredeccessors.isEmpty()) {
            result.add(x);
            return result;
        } else {
            for (ConceptTaxonomyNode y : positivePredeccessors) {
                if (!c.isVisited(y)) {
                    result.addAll(bottomSearch(c, y, topSearchResult));
                }
            }
            return result;
        }
    }

    private boolean enhancedBottomSupers(ConceptTaxonomyNode y, ConceptTaxonomyNode c, Set<ConceptTaxonomyNode> topSearchResult) {
        if (c.isPositive(y)) {
            return true;
        } else {
            boolean possibleSubNode = true;
            for (ConceptTaxonomyNode z : topSearchResult) {
                possibleSubNode = y.getAllSuperNodes().contains(z) || y == z;
                if (!possibleSubNode) break;
            }
            if (!possibleSubNode || c.isNegative(y)) {
                return false;
            } else if (checkSubsumption(y.getConcept(), c.getConcept())) {
                c.addToPositive(y);
                return true;
            } else {
                c.addToNegative(y);
                return false;
            }
        }
    }

    private boolean checkSubsumption(Concept subConcept, Concept superConcept) {
        boolean result = false;
        if (!result) {
            resetReasoner();
            result = !reasoner.checkSatisfiability((new Intersection(subConcept, (new Negation(superConcept)))).getNnfConcept());
        }
        return result;
    }

    private void resetReasoner() {
        if (reasoner instanceof ALCNReasoner) {
            reasoner = null;
            reasoner = new ALCNReasoner(tBox.getRoles());
        } else if (reasoner instanceof ALCQReasoner) {
            reasoner = null;
            reasoner = new ALCQReasoner(tBox.getRoles());
        } else if (reasoner instanceof ALCQHReasoner) {
            reasoner = null;
            reasoner = new ALCQHReasoner(tBox.getRoles());
        }
    }
}
