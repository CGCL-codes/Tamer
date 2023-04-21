package org.emftext.language.pl0.resource.pl0.mopp;

/**
 * A representation for a range in a document where a structural feature (e.g., a
 * reference) is expected.
 */
public class Pl0ExpectedStructuralFeature extends org.emftext.language.pl0.resource.pl0.mopp.Pl0AbstractExpectedElement {

    private org.emftext.language.pl0.resource.pl0.grammar.Pl0Placeholder placeholder;

    public Pl0ExpectedStructuralFeature(org.emftext.language.pl0.resource.pl0.grammar.Pl0Placeholder placeholder) {
        super(placeholder.getMetaclass());
        this.placeholder = placeholder;
    }

    public org.eclipse.emf.ecore.EStructuralFeature getFeature() {
        return placeholder.getFeature();
    }

    public String getTokenName() {
        return placeholder.getTokenName();
    }

    public java.util.Set<String> getTokenNames() {
        return java.util.Collections.singleton(getTokenName());
    }

    public String toString() {
        return "EFeature " + getFeature().getEContainingClass().getName() + "." + getFeature().getName();
    }

    public boolean equals(Object o) {
        if (o instanceof Pl0ExpectedStructuralFeature) {
            return getFeature().equals(((Pl0ExpectedStructuralFeature) o).getFeature());
        }
        return false;
    }
}
