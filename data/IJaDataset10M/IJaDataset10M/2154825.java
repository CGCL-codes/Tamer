package persister.data.impl;

import persister.data.TeamMember;

public class TeamMemberDataObject implements TeamMember {

    private String name;

    public TeamMemberDataObject() {
    }

    public TeamMemberDataObject(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TeamMemberDataObject clone() {
        TeamMemberDataObject clone = new TeamMemberDataObject();
        clone.name = getName();
        return clone;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    public boolean equals(Object other) {
        if (other == null) return false;
        return getName().equals(((TeamMember) other).getName());
    }

    public String toString() {
        return getName();
    }
}
