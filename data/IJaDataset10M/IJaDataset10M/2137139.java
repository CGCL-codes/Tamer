package org.fedora.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="asOfDateTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dsState" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "pid", "asOfDateTime", "dsState" })
@XmlRootElement(name = "getDatastreams")
public class GetDatastreams {

    /** The pid. */
    @XmlElement(required = true)
    protected String pid;

    /** The as of date time. */
    @XmlElement(required = true)
    protected String asOfDateTime;

    /** The ds state. */
    @XmlElement(required = true)
    protected String dsState;

    /**
     * Gets the value of the pid property.
     * 
     * @return the pid possible object is {@link String }
     */
    public String getPid() {
        return pid;
    }

    /**
     * Sets the value of the pid property.
     * 
     * @param value
     *        allowed object is {@link String }
     */
    public void setPid(String value) {
        this.pid = value;
    }

    /**
     * Gets the value of the asOfDateTime property.
     * 
     * @return the as of date time possible object is {@link String }
     */
    public String getAsOfDateTime() {
        return asOfDateTime;
    }

    /**
     * Sets the value of the asOfDateTime property.
     * 
     * @param value
     *        allowed object is {@link String }
     */
    public void setAsOfDateTime(String value) {
        this.asOfDateTime = value;
    }

    /**
     * Gets the value of the dsState property.
     * 
     * @return the ds state possible object is {@link String }
     */
    public String getDsState() {
        return dsState;
    }

    /**
     * Sets the value of the dsState property.
     * 
     * @param value
     *        allowed object is {@link String }
     */
    public void setDsState(String value) {
        this.dsState = value;
    }
}
