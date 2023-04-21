package org.sti2.elly.basics;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.sti2.elly.api.basics.IConcept;
import org.sti2.elly.api.basics.IConceptOrRole;
import org.sti2.elly.api.basics.IIntersectionConcept;

/**
 * @author Daniel Winkler
 *
 */
public class IntersectionConcept extends ConceptOrRole implements IIntersectionConcept {

    private final List<IConcept> concepts;

    IntersectionConcept(List<IConcept> concepts) {
        if (concepts == null) throw new IllegalArgumentException("Concepts must not be null");
        if (concepts.contains(null)) throw new IllegalArgumentException("Concepts must not contain null");
        if (concepts.size() <= 1) throw new IllegalArgumentException("Concepts must contain at least 2 elements");
        this.concepts = concepts;
    }

    IntersectionConcept(IConcept... concepts) {
        this(Arrays.asList(concepts));
    }

    @Override
    public List<IConcept> getConcepts() {
        return concepts;
    }

    public String toString() {
        StringBuffer returnString = new StringBuffer();
        boolean first = true;
        returnString.append("intersectionOf(");
        for (IConcept concept : getConcepts()) {
            if (first) first = false; else returnString.append(", ");
            returnString.append(concept);
        }
        returnString.append(")");
        return returnString.toString();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        IntersectionConcept otherConcept = (IntersectionConcept) obj;
        return new EqualsBuilder().append(this.concepts.toArray(), otherConcept.concepts.toArray()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(95, 49).append(this.concepts.toArray()).toHashCode();
    }

    @Override
    public int compareTo(IConceptOrRole o) {
        IntersectionConcept otherConcept = (IntersectionConcept) o;
        return new CompareToBuilder().append(this.concepts.toArray(), otherConcept.concepts.toArray()).toComparison();
    }
}
