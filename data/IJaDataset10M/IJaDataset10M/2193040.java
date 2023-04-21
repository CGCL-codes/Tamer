package de.ifgi.simcat.reasoner.constructors;

import java.util.HashSet;
import de.ifgi.simcat.reasoner.Concept;
import de.ifgi.simcat.reasoner.NamedConcept;
import de.ifgi.simcat.reasoner.Role;
import de.ifgi.simcat.server.ServerConstants;

/**
 * @author janowicz
 * @version 1.0
 *
 */
public class QualifiedAtLeastRestriction extends QualifiedNumberRestriction {

    public static final String TYPESTRING = "atLeast";

    public QualifiedAtLeastRestriction(Role role, Integer cardinality, Concept range) {
        super(role, cardinality, range);
    }

    public String getTypeString() {
        return TYPESTRING;
    }

    public Concept getNnfConcept(HashSet<NamedConcept> blockingList) {
        if (nnfConcept == null) nnfConcept = (new QualifiedAtLeastRestriction(getRole().getNnfConcept(), getCardinality(), getRange().getNnfConcept(blockingList)));
        return nnfConcept;
    }

    public Concept getSimplifiedConcept() {
        if (simplifiedConcept == null) {
            simplifiedConcept = new QualifiedAtLeastRestriction(this.getRole(), this.getCardinality(), this.getRange().getSimplifiedConcept());
        }
        if (this.getCardinality() == 0) {
            Concept tempTop = new NamedConcept(ServerConstants.TOP);
            simplifiedConcept = tempTop;
        }
        return simplifiedConcept;
    }
}
