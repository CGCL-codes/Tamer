package mf.torneo.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ClassificaPK implements Serializable {

    private static final long serialVersionUID = -120456543891263407L;

    /** identifier field */
    private Integer idgirone;

    /** identifier field */
    private Integer team;

    /** full constructor */
    public ClassificaPK(Integer idgirone, Integer team) {
        this.idgirone = idgirone;
        this.team = team;
    }

    /** default constructor */
    public ClassificaPK() {
    }

    public Integer getIdgirone() {
        return this.idgirone;
    }

    public void setIdgirone(Integer idgirone) {
        this.idgirone = idgirone;
    }

    public Integer getTeam() {
        return this.team;
    }

    public void setTeam(Integer team) {
        this.team = team;
    }

    public String toString() {
        return new ToStringBuilder(this).append("idgirone", getIdgirone()).append("team", getTeam()).toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof ClassificaPK)) return false;
        ClassificaPK castOther = (ClassificaPK) other;
        return new EqualsBuilder().append(this.getIdgirone(), castOther.getIdgirone()).append(this.getTeam(), castOther.getTeam()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getIdgirone()).append(getTeam()).toHashCode();
    }
}
