package org.opennms.web.assets;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Service implements Serializable {

    /** identifier field */
    private java.lang.Integer serviceid;

    /** persistent field */
    private java.lang.String servicename;

    /** persistent field */
    private Set outags;

    /** persistent field */
    private Set ifservics;

    /** full constructor */
    public Service(java.lang.Integer serviceid, java.lang.String servicename, Set outags, Set ifservics) {
        this.serviceid = serviceid;
        this.servicename = servicename;
        this.outags = outags;
        this.ifservics = ifservics;
    }

    /** default constructor */
    public Service() {
    }

    public java.lang.Integer getServiceid() {
        return this.serviceid;
    }

    public void setServiceid(java.lang.Integer serviceid) {
        this.serviceid = serviceid;
    }

    public java.lang.String getServicename() {
        return this.servicename;
    }

    public void setServicename(java.lang.String servicename) {
        this.servicename = servicename;
    }

    public java.util.Set getOutags() {
        return this.outags;
    }

    public void setOutags(java.util.Set outags) {
        this.outags = outags;
    }

    public java.util.Set getIfservics() {
        return this.ifservics;
    }

    public void setIfservics(java.util.Set ifservics) {
        this.ifservics = ifservics;
    }

    public String toString() {
        return new ToStringBuilder(this).append("serviceid", getServiceid()).toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof Service)) return false;
        Service castOther = (Service) other;
        return new EqualsBuilder().append(this.getServiceid(), castOther.getServiceid()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getServiceid()).toHashCode();
    }
}
