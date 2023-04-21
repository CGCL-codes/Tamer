package org.meanbean.test.beans;

/**
 * A bean that can have-a instance of itself as a parent.
 * 
 * @author Graham Williamson
 */
public class SelfReferencingBean {

    private String firstName;

    private String lastName;

    private SelfReferencingBean parent;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public SelfReferencingBean getParent() {
        return parent;
    }

    public void setParent(SelfReferencingBean parent) {
        this.parent = parent;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SelfReferencingBean other = (SelfReferencingBean) obj;
        if (firstName == null) {
            if (other.firstName != null) return false;
        } else if (!firstName.equals(other.firstName)) return false;
        if (lastName == null) {
            if (other.lastName != null) return false;
        } else if (!lastName.equals(other.lastName)) return false;
        if (parent == null) {
            if (other.parent != null) return false;
        } else if (!parent.equals(other.parent)) return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SelfReferencingBean [");
        if (firstName != null) builder.append("firstName=").append(firstName).append(", ");
        if (lastName != null) builder.append("lastName=").append(lastName).append(", ");
        if (parent != null) builder.append("parent=").append(parent);
        builder.append("]");
        return builder.toString();
    }
}
