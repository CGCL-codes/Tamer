package de.ifgi.simcat.reasoner.alcqh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import de.ifgi.simcat.reasoner.ABox;
import de.ifgi.simcat.reasoner.Assertion;
import de.ifgi.simcat.reasoner.AssertionList;
import de.ifgi.simcat.reasoner.Concept;
import de.ifgi.simcat.reasoner.ConceptAssertion;
import de.ifgi.simcat.reasoner.Individual;
import de.ifgi.simcat.reasoner.NamedConcept;
import de.ifgi.simcat.reasoner.Role;
import de.ifgi.simcat.reasoner.constructors.Intersection;
import de.ifgi.simcat.reasoner.constructors.Negation;
import de.ifgi.simcat.reasoner.constructors.QualifiedAtLeastRestriction;
import de.ifgi.simcat.reasoner.constructors.QualifiedAtMostRestriction;
import de.ifgi.simcat.reasoner.constructors.QualifiedNumberRestriction;
import de.ifgi.simcat.server.ServerConstants;

/**
 * @author janowicz
 * @version 1.0
 *
 */
public final class ALCQHABox extends ABox {

    public boolean isClashFree(Collection<Role> roles) {
        ClashFactory clash = new ClashFactory(roles);
        if (clash.aBoxIsInconsistent()) {
            clash = null;
            setInconsistent();
            return false;
        }
        return true;
    }

    public boolean isClashFree(Individual individual, Collection<Role> roles) {
        ClashFactory clash = new ClashFactory(individual, roles);
        if (clash.aBoxIsInconsistent()) {
            clash = null;
            setInconsistent();
            return false;
        }
        return true;
    }

    final class ClashFactory {

        private boolean inconsistent;

        private ArrayList<Individual> individuals;

        ALCQHReasoner reasoner;

        public ClashFactory(Collection<Role> roles) {
            reasoner = new ALCQHReasoner(roles);
            individuals = getAllIndividuals();
            if ((simpleNegationClash() || cardinalityClash(roles))) inconsistent = true; else inconsistent = false;
        }

        public ClashFactory(Individual individual, Collection<Role> roles) {
            if (simpleNegationClash(individual) || cardinalityClash(individual, roles)) inconsistent = true; else inconsistent = false;
        }

        private boolean simpleNegationClash() {
            boolean result = false;
            for (Iterator instanceIter = individuals.iterator(); instanceIter.hasNext(); ) {
                result = simpleNegationClash((Individual) instanceIter.next());
                if (result) return true;
            }
            return false;
        }

        private boolean simpleNegationClash(Individual individual) {
            AssertionList assertions = getAssertionsByRelatingIndividual(individual);
            for (Iterator assertionIter = assertions.iterator(); assertionIter.hasNext(); ) {
                Assertion individualAssertion = (Assertion) assertionIter.next();
                if (individualAssertion instanceof ConceptAssertion) {
                    ConceptAssertion conceptAssertion = (ConceptAssertion) individualAssertion;
                    Concept assertedConcept = conceptAssertion.getAssertedConcept();
                    if (assertedConcept instanceof Negation && ((Negation) assertedConcept).getConcept().conceptsAreEqual(new NamedConcept(ServerConstants.TOP))) {
                        branchDependencySet = new HashSet<Integer>();
                        branchDependencySet.addAll(conceptAssertion.getBranchDependencySet());
                        return true;
                    }
                    if (assertedConcept.conceptsAreEqual(new NamedConcept(ServerConstants.BOTTOM))) {
                        branchDependencySet = new HashSet<Integer>();
                        branchDependencySet.addAll(conceptAssertion.getBranchDependencySet());
                        return true;
                    }
                    for (Iterator assertionIter2 = assertions.iterator(); assertionIter2.hasNext(); ) {
                        Assertion individualAssertion2 = (Assertion) assertionIter2.next();
                        if (individualAssertion2 instanceof ConceptAssertion) {
                            ConceptAssertion conceptAssertion2 = (ConceptAssertion) individualAssertion2;
                            Concept assertedConcept2 = conceptAssertion2.getAssertedConcept();
                            if (assertedConcept2 instanceof Negation) {
                                if (((Negation) assertedConcept2).getConcept() instanceof NamedConcept && ServerConstants.TOP.equals(((NamedConcept) ((Negation) assertedConcept2).getConcept()).getName())) {
                                    branchDependencySet = new HashSet<Integer>();
                                    branchDependencySet.addAll(conceptAssertion.getBranchDependencySet());
                                    branchDependencySet.addAll(conceptAssertion2.getBranchDependencySet());
                                    return true;
                                }
                                if (conceptsAreEqual(new Negation(assertedConcept), assertedConcept2)) {
                                    branchDependencySet = new HashSet<Integer>();
                                    branchDependencySet.addAll(conceptAssertion.getBranchDependencySet());
                                    branchDependencySet.addAll(conceptAssertion2.getBranchDependencySet());
                                    return true;
                                }
                                if (assertedConcept instanceof NamedConcept && ((NamedConcept) assertedConcept).isToldSubsumee(((Negation) assertedConcept2).getConcept().getNnfConcept())) {
                                    branchDependencySet = new HashSet<Integer>();
                                    branchDependencySet.addAll(conceptAssertion.getBranchDependencySet());
                                    branchDependencySet.addAll(conceptAssertion2.getBranchDependencySet());
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }

        private boolean cardinalityClash(Collection<Role> roles) {
            boolean result = false;
            for (Iterator instanceIter = individuals.iterator(); instanceIter.hasNext(); ) {
                result = cardinalityClash((Individual) instanceIter.next(), roles);
                if (result) return true;
            }
            return false;
        }

        private boolean cardinalityClash(Individual individual, Collection<Role> roles) {
            try {
                AssertionList assertions = getAssertionsByRelatingIndividual(individual).getAssertionsByConstructorType(Class.forName("de.ifgi.simcat.reasoner.constructors.QualifiedNumberRestriction"));
                for (Iterator assertionIter = assertions.iterator(); assertionIter.hasNext(); ) {
                    Assertion individualAssertion = (Assertion) assertionIter.next();
                    QualifiedNumberRestriction assertedConcept = (QualifiedNumberRestriction) ((ConceptAssertion) individualAssertion).getAssertedConcept();
                    for (Iterator assertionIter2 = assertions.iterator(); assertionIter2.hasNext(); ) {
                        Assertion individualAssertion2 = (Assertion) assertionIter2.next();
                        QualifiedNumberRestriction assertedConcept2 = (QualifiedNumberRestriction) ((ConceptAssertion) individualAssertion2).getAssertedConcept();
                        if (assertedConcept instanceof QualifiedAtLeastRestriction && assertedConcept2 instanceof QualifiedAtMostRestriction && (assertedConcept.getRole() == assertedConcept2.getRole()) && assertedConcept.getCardinality() > assertedConcept2.getCardinality()) {
                            resetReasoner(roles);
                            if (conceptsAreEqual(assertedConcept.getRange(), assertedConcept2.getRange())) {
                                branchDependencySet = new HashSet<Integer>();
                                branchDependencySet.addAll(((ConceptAssertion) individualAssertion).getBranchDependencySet());
                                branchDependencySet.addAll(((ConceptAssertion) individualAssertion2).getBranchDependencySet());
                                return true;
                            } else if (!reasoner.checkSatisfiability((new Intersection(assertedConcept.getRange(), (new Negation(assertedConcept2.getRange())))).getNnfConcept())) branchDependencySet = new HashSet<Integer>();
                            branchDependencySet.addAll(((ConceptAssertion) individualAssertion).getBranchDependencySet());
                            branchDependencySet.addAll(((ConceptAssertion) individualAssertion2).getBranchDependencySet());
                            return true;
                        }
                    }
                }
                return false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean aBoxIsInconsistent() {
            return inconsistent;
        }

        private void resetReasoner(Collection<Role> roles) {
            reasoner = null;
            reasoner = new ALCQHReasoner(roles);
        }
    }
}
