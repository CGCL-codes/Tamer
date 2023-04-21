package oopex.eclipselink1.jpax.usecases.keys;

import java.io.Serializable;

public class PersonPK implements Serializable {

    private static final long serialVersionUID = 385807644134201206L;

    public PersonPK() {
    }

    private String uniquePersonNumber;

    private String upnIssuer;

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uniquePersonNumber == null) ? 0 : uniquePersonNumber.hashCode());
        result = prime * result + ((upnIssuer == null) ? 0 : upnIssuer.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PersonPK other = (PersonPK) obj;
        if (uniquePersonNumber == null) {
            if (other.uniquePersonNumber != null) return false;
        } else if (!uniquePersonNumber.equals(other.uniquePersonNumber)) return false;
        if (upnIssuer == null) {
            if (other.upnIssuer != null) return false;
        } else if (!upnIssuer.equals(other.upnIssuer)) return false;
        return true;
    }

    public String toString() {
        return "PersonPK(" + upnIssuer + ":" + uniquePersonNumber + ")";
    }
}
