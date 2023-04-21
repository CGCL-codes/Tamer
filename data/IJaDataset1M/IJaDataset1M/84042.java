package net.sourceforge.ondex.predicate.concept;

import net.sourceforge.ondex.core.CV;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.predicate.Predicate;
import net.sourceforge.ondex.predicate.cv.CvPredicateVisitor;

/**
 * Filter concepts by annotation.
 *
 * @author Matthew Pocock
 */
public final class ByCV implements Predicate<ConceptPredicateVisitor, ONDEXConcept> {

    private Predicate<CvPredicateVisitor, CV> predicate;

    ByCV(Predicate<CvPredicateVisitor, CV> predicate) {
        this.predicate = predicate;
    }

    public Predicate<CvPredicateVisitor, CV> getPredicate() {
        return predicate;
    }

    public boolean accepts(ONDEXConcept concept) {
        return predicate.accepts(concept.getElementOf());
    }

    public void entertain(ConceptPredicateVisitor cpv) {
        cpv.visit(cpv, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ByCV that = (ByCV) o;
        if (!predicate.equals(that.predicate)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return predicate.hashCode();
    }

    @Override
    public String toString() {
        return "ByCV{" + "predicate=" + predicate + '}';
    }
}
