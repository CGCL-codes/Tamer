package org.plazmaforge.framework.erm.model;

public class MUserRole extends MModel {

    private MUser user;

    private MRole role;

    public MUser getUser() {
        return user;
    }

    public void setUser(MUser user) {
        this.user = user;
    }

    public MRole getRole() {
        return role;
    }

    public void setRole(MRole role) {
        this.role = role;
    }

    public String toString() {
        return "UserRole[" + toPropertiesString() + "]";
    }

    public String toPropertiesString() {
        return super.toPropertiesString() + ", " + (getUser() == null ? "User=[null]" : getUser().toString()) + ", " + (getRole() == null ? "Role=[null]" : getRole().toString());
    }
}
