package org.emftext.language.pl0.resource.pl0.grammar;

public class Pl0Containment extends org.emftext.language.pl0.resource.pl0.grammar.Pl0Terminal {

    public Pl0Containment(org.eclipse.emf.ecore.EStructuralFeature feature, org.emftext.language.pl0.resource.pl0.grammar.Pl0Cardinality cardinality, int mandatoryOccurencesAfter) {
        super(feature, cardinality, mandatoryOccurencesAfter);
    }

    public String toString() {
        return getFeature().getName();
    }
}
