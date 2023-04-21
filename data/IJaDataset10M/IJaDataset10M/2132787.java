package net.sf.otrcutmp4.model.xml.series;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://otrcutmp4.sf.net/series}season"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="nr" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "season" })
@XmlRootElement(name = "episode")
public class Episode implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(required = true)
    protected Season season;

    @XmlAttribute(name = "id")
    protected Long id;

    @XmlAttribute(name = "nr")
    protected Integer nr;

    @XmlAttribute(name = "name")
    protected String name;

    /**
     * Gets the value of the season property.
     * 
     * @return
     *     possible object is
     *     {@link Season }
     *     
     */
    public Season getSeason() {
        return season;
    }

    /**
     * Sets the value of the season property.
     * 
     * @param value
     *     allowed object is
     *     {@link Season }
     *     
     */
    public void setSeason(Season value) {
        this.season = value;
    }

    public boolean isSetSeason() {
        return (this.season != null);
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(long value) {
        this.id = value;
    }

    public boolean isSetId() {
        return (this.id != null);
    }

    public void unsetId() {
        this.id = null;
    }

    /**
     * Gets the value of the nr property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getNr() {
        return nr;
    }

    /**
     * Sets the value of the nr property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNr(int value) {
        this.nr = value;
    }

    public boolean isSetNr() {
        return (this.nr != null);
    }

    public void unsetNr() {
        this.nr = null;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    public boolean isSetName() {
        return (this.name != null);
    }
}
