package org.opennms.netmgt.collectd.tca.config;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * The Class TcaDataCollectionConfig.
 * 
 * @author <a href="mailto:agalue@opennms.org">Alejandro Galue</a>
 */
@XmlRootElement(name = "tca-collection")
public class TcaDataCollection implements Serializable, Comparable<TcaDataCollection> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 4105044141350925553L;

    /** The Collection name. */
    @XmlAttribute(name = "name", required = true)
    private String m_name;

    /** The RRD configuration object. */
    @XmlElement(name = "rrd", required = true)
    private TcaRrd m_rrd;

    /**
	 * Instantiates a new TCA data collection.
	 */
    public TcaDataCollection() {
    }

    /**
	 * Gets the collection name.
	 *
	 * @return the collection name
	 */
    @XmlTransient
    public String getName() {
        return m_name;
    }

    /**
	 * Sets the collection name.
	 *
	 * @param name the collection name
	 */
    public void setName(String name) {
        m_name = name;
    }

    /**
	 * Gets the RRD.
	 *
	 * @return the RRD
	 */
    @XmlTransient
    public TcaRrd getRrd() {
        return m_rrd;
    }

    /**
	 * Sets the RRD.
	 *
	 * @param rrd the new RRD
	 */
    public void setRrd(TcaRrd rrd) {
        m_rrd = rrd;
    }

    public int compareTo(TcaDataCollection obj) {
        return new CompareToBuilder().append(getName(), obj.getName()).append(getRrd(), obj.getRrd()).toComparison();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TcaDataCollection) {
            TcaDataCollection other = (TcaDataCollection) obj;
            return new EqualsBuilder().append(getName(), other.getName()).append(getRrd(), other.getRrd()).isEquals();
        }
        return false;
    }
}
