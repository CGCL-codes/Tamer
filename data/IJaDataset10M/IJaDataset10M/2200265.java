package org.osmius.model;

import org.acegisecurity.GrantedAuthority;
import java.util.HashSet;
import java.util.Set;

public class OsmRole extends BaseObject implements java.io.Serializable, GrantedAuthority {

    private String idnRole;

    private String desRole;

    private static final long serialVersionUID = -5563858700289813175L;

    public OsmRole() {
    }

    public OsmRole(String idnRole) {
        this.idnRole = idnRole;
    }

    public OsmRole(String idnRole, String desRole) {
        this.idnRole = idnRole;
        this.desRole = desRole;
    }

    public String getIdnRole() {
        return this.idnRole;
    }

    public void setIdnRole(String idnRole) {
        this.idnRole = idnRole;
    }

    public String getDesRole() {
        return this.desRole;
    }

    public void setDesRole(String desRole) {
        this.desRole = desRole;
    }

    public String getAuthority() {
        return idnRole;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OsmRole osmRole = (OsmRole) o;
        if (desRole != null ? !desRole.equals(osmRole.desRole) : osmRole.desRole != null) return false;
        if (idnRole != null ? !idnRole.equals(osmRole.idnRole) : osmRole.idnRole != null) return false;
        return true;
    }

    public int hashCode() {
        int result;
        result = (idnRole != null ? idnRole.hashCode() : 0);
        result = 31 * result + (desRole != null ? desRole.hashCode() : 0);
        return result;
    }

    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("OsmRole");
        sb.append("{idnRole='").append(idnRole).append('\'');
        sb.append(", desRole='").append(desRole).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
