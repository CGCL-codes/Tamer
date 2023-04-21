package eu.fbk.hlt.edits.etaf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for GroupQuestion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GroupQuestion">
 *   &lt;complexContent>
 *     &lt;extension base="{}AnnotatedText">
 *       &lt;sequence>
 *         &lt;element name="phrase" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="valid" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GroupQuestion", propOrder = { "phrase" })
public class GroupQuestion extends AnnotatedText {

    @XmlElement(required = true)
    protected String phrase;

    @XmlAttribute
    protected Boolean valid;

    /**
     * Gets the value of the phrase property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhrase() {
        return phrase;
    }

    /**
     * Sets the value of the phrase property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhrase(String value) {
        this.phrase = value;
    }

    /**
     * Gets the value of the valid property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isValid() {
        return valid;
    }

    /**
     * Sets the value of the valid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setValid(Boolean value) {
        this.valid = value;
    }
}
