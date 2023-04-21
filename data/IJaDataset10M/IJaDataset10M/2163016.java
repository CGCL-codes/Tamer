package org.dllearner.utilities;

import java.util.Comparator;
import java.util.Set;
import org.dllearner.dl.All;
import org.dllearner.dl.AtomicConcept;
import org.dllearner.dl.Bottom;
import org.dllearner.dl.Concept;
import org.dllearner.dl.Exists;
import org.dllearner.dl.MultiConjunction;
import org.dllearner.dl.MultiDisjunction;
import org.dllearner.dl.Negation;
import org.dllearner.dl.Quantification;
import org.dllearner.dl.Top;

public class ConceptComparator implements Comparator<Concept> {

    RoleComparator rc = new RoleComparator();

    public ConceptComparator() {
    }

    public ConceptComparator(Set<AtomicConcept> atomicConcepts) {
    }

    public int compare(Concept concept1, Concept concept2) {
        if (concept1 instanceof Bottom) {
            if (concept2 instanceof Bottom) return 0; else return -1;
        } else if (concept1 instanceof AtomicConcept) {
            if (concept2 instanceof Bottom) return 1; else if (concept2 instanceof AtomicConcept) return ((AtomicConcept) concept1).getName().compareTo(((AtomicConcept) concept2).getName()); else return -1;
        } else if (concept1 instanceof Top) {
            if (concept2 instanceof Bottom || concept2 instanceof AtomicConcept) return 1; else if (concept2 instanceof Top) return 0; else return -1;
        } else if (concept1 instanceof Negation) {
            if (concept2.getChildren().size() < 1) return 1; else if (concept2 instanceof Negation) return compare(concept1.getChild(0), concept2.getChild(0)); else return -1;
        } else if (concept1 instanceof Exists) {
            if (concept2.getChildren().size() < 1 || concept2 instanceof Negation) return 1; else if (concept2 instanceof Exists) {
                int roleCompare = rc.compare(((Quantification) concept1).getRole(), ((Quantification) concept2).getRole());
                if (roleCompare == 0) return compare(concept1.getChild(0), concept2.getChild(0)); else return roleCompare;
            } else return -1;
        } else if (concept1 instanceof All) {
            if (concept2.getChildren().size() < 1 || concept2 instanceof Negation || concept2 instanceof Exists) return 1; else if (concept2 instanceof All) {
                int roleCompare = rc.compare(((Quantification) concept1).getRole(), ((Quantification) concept2).getRole());
                if (roleCompare == 0) return compare(concept1.getChild(0), concept2.getChild(0)); else return roleCompare;
            } else return -1;
        } else if (concept1 instanceof MultiConjunction) {
            if (concept2.getChildren().size() < 2) return 1; else if (concept2 instanceof MultiConjunction) {
                int nrOfChildrenConcept1 = concept1.getChildren().size();
                int nrOfChildrenConcept2 = concept2.getChildren().size();
                if (nrOfChildrenConcept1 > nrOfChildrenConcept2) return 1; else if (nrOfChildrenConcept1 == nrOfChildrenConcept2) {
                    for (int i = 0; i < nrOfChildrenConcept1; i++) {
                        int compareValue = compare(concept1.getChild(i), concept2.getChild(i));
                        if (compareValue > 0) return 1; else if (compareValue < 0) return -1;
                    }
                    return 0;
                } else return -1;
            } else return -1;
        } else if (concept1 instanceof MultiDisjunction) {
            if (concept2.getChildren().size() < 2 || concept2 instanceof MultiConjunction) return 1; else if (concept2 instanceof MultiDisjunction) {
                int nrOfChildrenConcept1 = concept1.getChildren().size();
                int nrOfChildrenConcept2 = concept2.getChildren().size();
                if (nrOfChildrenConcept1 > nrOfChildrenConcept2) return 1; else if (nrOfChildrenConcept1 == nrOfChildrenConcept2) {
                    for (int i = 0; i < nrOfChildrenConcept1; i++) {
                        int compareValue = compare(concept1.getChild(i), concept2.getChild(i));
                        if (compareValue > 0) return 1; else if (compareValue < 0) return -1;
                    }
                    return 0;
                } else return -1;
            } else return -1;
        } else throw new RuntimeException(concept1.toString());
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof ConceptComparator);
    }
}
