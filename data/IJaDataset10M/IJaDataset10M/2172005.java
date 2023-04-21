package eu.itsyourparliament.api.countryinfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter1;

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
 *         &lt;element ref="{}mepid"/>
 *         &lt;element ref="{}mepinfo"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "mepid", "mepinfo" })
@XmlRootElement(name = "mep")
public class Mep {

    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "integer")
    protected Integer mepid;

    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String mepinfo;

    /**
     * Gets the value of the mepid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getMepid() {
        return mepid;
    }

    /**
     * Sets the value of the mepid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMepid(Integer value) {
        this.mepid = value;
    }

    /**
     * Gets the value of the mepinfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMepinfo() {
        return mepinfo;
    }

    /**
     * Sets the value of the mepinfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMepinfo(String value) {
        this.mepinfo = value;
    }
}
