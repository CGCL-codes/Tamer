package org.rt.engine;

import org.rt.credential.*;

public class RoleSolution {

    static final String NL = System.getProperty("line.separator");

    private Role role;

    private CompoundConstraint constraint;

    public RoleSolution(Role role, CompoundConstraint constraint) {
        this.role = role;
        this.constraint = constraint;
    }

    public String toString() {
        return role.toString();
    }

    public String toStringVerbose() {
        return role.toString() + NL + this.constraint.toStringVerbose();
    }

    public CompoundConstraint getConstraint() {
        return constraint;
    }

    public Role getRole() {
        return role;
    }

    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof RoleSolution)) return false;
        if (this == o) return true;
        RoleSolution other = (RoleSolution) o;
        return this.role.equals(other.getRole());
    }
}
