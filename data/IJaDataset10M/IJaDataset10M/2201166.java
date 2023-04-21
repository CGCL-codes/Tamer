package org.hackystat.socnet.server.resource.socialmediagraph.jaxb;

import java.io.Serializable;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;attribute ref=&quot;{http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd}Name use=&quot;required&quot;&quot;/&gt;
 *       &lt;attribute ref=&quot;{http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd}Units use=&quot;required&quot;&quot;/&gt;
 *       &lt;attribute ref=&quot;{http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd}NumberType&quot;/&gt;
 *       &lt;attribute ref=&quot;{http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd}LowerBound&quot;/&gt;
 *       &lt;attribute ref=&quot;{http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd}UpperBound&quot;/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "Y-Axis")
public class YAxis implements Serializable {

    private static final long serialVersionUID = 12343L;

    @XmlAttribute(name = "Name", namespace = "http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd", required = true)
    protected String name;

    @XmlAttribute(name = "Units", namespace = "http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd", required = true)
    protected String units;

    @XmlAttribute(name = "NumberType", namespace = "http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd")
    protected String numberType;

    @XmlAttribute(name = "LowerBound", namespace = "http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd")
    protected BigInteger lowerBound;

    @XmlAttribute(name = "UpperBound", namespace = "http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd")
    protected BigInteger upperBound;

    /**
   * Gets the value of the name property.
   * 
   * @return possible object is {@link String }
   * 
   */
    public String getName() {
        return name;
    }

    /**
   * Sets the value of the name property.
   * 
   * @param value allowed object is {@link String }
   * 
   */
    public void setName(String value) {
        this.name = value;
    }

    public boolean isSetName() {
        return (this.name != null);
    }

    /**
   * Gets the value of the units property.
   * 
   * @return possible object is {@link String }
   * 
   */
    public String getUnits() {
        return units;
    }

    /**
   * Sets the value of the units property.
   * 
   * @param value allowed object is {@link String }
   * 
   */
    public void setUnits(String value) {
        this.units = value;
    }

    public boolean isSetUnits() {
        return (this.units != null);
    }

    /**
   * Gets the value of the numberType property.
   * 
   * @return possible object is {@link String }
   * 
   */
    public String getNumberType() {
        return numberType;
    }

    /**
   * Sets the value of the numberType property.
   * 
   * @param value allowed object is {@link String }
   * 
   */
    public void setNumberType(String value) {
        this.numberType = value;
    }

    public boolean isSetNumberType() {
        return (this.numberType != null);
    }

    /**
   * Gets the value of the lowerBound property.
   * 
   * @return possible object is {@link BigInteger }
   * 
   */
    public BigInteger getLowerBound() {
        return lowerBound;
    }

    /**
   * Sets the value of the lowerBound property.
   * 
   * @param value allowed object is {@link BigInteger }
   * 
   */
    public void setLowerBound(BigInteger value) {
        this.lowerBound = value;
    }

    public boolean isSetLowerBound() {
        return (this.lowerBound != null);
    }

    /**
   * Gets the value of the upperBound property.
   * 
   * @return possible object is {@link BigInteger }
   * 
   */
    public BigInteger getUpperBound() {
        return upperBound;
    }

    /**
   * Sets the value of the upperBound property.
   * 
   * @param value allowed object is {@link BigInteger }
   * 
   */
    public void setUpperBound(BigInteger value) {
        this.upperBound = value;
    }

    public boolean isSetUpperBound() {
        return (this.upperBound != null);
    }
}
