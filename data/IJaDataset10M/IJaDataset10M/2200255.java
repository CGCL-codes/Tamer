package de.fau.cs.dosis.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for user-reference complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="user-reference">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="user" type="{http://www.w3.org/2001/XMLSchema}IDREF"/>
 *           &lt;element name="weak-user" type="{http://dosis/schema}weak-reference"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "user-reference", propOrder = { "user", "weakUser" })
public class UserReference {

    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object user;

    @XmlElement(name = "weak-user")
    protected WeakReference weakUser;

    /**
     * Gets the value of the user property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getUser() {
        return user;
    }

    /**
     * Sets the value of the user property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setUser(Object value) {
        this.user = value;
    }

    /**
     * Gets the value of the weakUser property.
     * 
     * @return
     *     possible object is
     *     {@link WeakReference }
     *     
     */
    public WeakReference getWeakUser() {
        return weakUser;
    }

    /**
     * Sets the value of the weakUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link WeakReference }
     *     
     */
    public void setWeakUser(WeakReference value) {
        this.weakUser = value;
    }
}
