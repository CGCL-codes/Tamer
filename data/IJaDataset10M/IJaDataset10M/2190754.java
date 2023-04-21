package org.wsml.reasoner.api.inconsistency;

public class UnNamedUserConstraintViolation extends UserConstraintViolation {

    public UnNamedUserConstraintViolation() {
        super();
    }

    public String toString() {
        return "User constraint Violation due to anonymous axiom (for more details you must name your axioms)";
    }
}
