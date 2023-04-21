package org.opennms.web.assets;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Servicemap implements Serializable {

    /** identifier field */
    private java.lang.String ipaddr;

    /** identifier field */
    private java.lang.String servicemapname;

    /** full constructor */
    public Servicemap(java.lang.String ipaddr, java.lang.String servicemapname) {
        this.ipaddr = ipaddr;
        this.servicemapname = servicemapname;
    }

    /** default constructor */
    public Servicemap() {
    }

    public java.lang.String getIpaddr() {
        return this.ipaddr;
    }

    public void setIpaddr(java.lang.String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public java.lang.String getServicemapname() {
        return this.servicemapname;
    }

    public void setServicemapname(java.lang.String servicemapname) {
        this.servicemapname = servicemapname;
    }

    public String toString() {
        return new ToStringBuilder(this).append("ipaddr", getIpaddr()).append("servicemapname", getServicemapname()).toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof Servicemap)) return false;
        Servicemap castOther = (Servicemap) other;
        return new EqualsBuilder().append(this.getIpaddr(), castOther.getIpaddr()).append(this.getServicemapname(), castOther.getServicemapname()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getIpaddr()).append(getServicemapname()).toHashCode();
    }
}
