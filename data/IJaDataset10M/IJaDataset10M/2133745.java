package eu.redseeds.sc.query.engine.impl;

import eu.redseeds.sc.query.engine.DomainElementMatch;
import eu.redseeds.scl.rsl.rsldomainelements.domainelements.DomainElement;

public class DomainElementMatchImpl implements DomainElementMatch {

    private final double similarity;

    private final DomainElement currentDomainElement;

    private final DomainElement pastDomainElement;

    public DomainElementMatchImpl(DomainElement currentDomainElement, DomainElement pastDomainElement, double similarity) {
        this.currentDomainElement = currentDomainElement;
        this.pastDomainElement = pastDomainElement;
        this.similarity = similarity;
    }

    @Override
    public DomainElement getCurrentDomainElement() {
        return currentDomainElement;
    }

    @Override
    public DomainElement getPastDomainElement() {
        return pastDomainElement;
    }

    @Override
    public double getSimilarityValue() {
        return similarity;
    }

    @Override
    public int compareTo(DomainElementMatch arg0) {
        double simDiff = similarity - arg0.getSimilarityValue();
        if (simDiff < 0) {
            return -1;
        }
        if (simDiff > 0) {
            return 1;
        }
        return hashCode() - arg0.hashCode();
    }
}
