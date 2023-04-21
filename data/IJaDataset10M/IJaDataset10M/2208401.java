package org.herasaf.xacml.core.policy.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ObligationType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;ObligationType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}AttributeAssignment&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;ObligationId&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}anyURI&quot; /&gt;
 *       &lt;attribute name=&quot;FulfillOn&quot; use=&quot;required&quot; type=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}EffectType&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 66, for further information.
 * 
 * @version 1.0
 * @author <i>generated</i>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObligationType", propOrder = { "attributeAssignments" })
public class ObligationType implements Serializable {

    private static final long serialVersionUID = 632768732L;

    @XmlElement(name = "AttributeAssignment")
    protected List<AttributeAssignmentType> attributeAssignments;

    @XmlAttribute(name = "ObligationId", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String obligationId;

    @XmlAttribute(name = "FulfillOn", required = true)
    protected EffectType fulfillOn;

    public ObligationType() {
    }

    public ObligationType(String id, EffectType effect) {
        this.obligationId = id;
        this.fulfillOn = effect;
    }

    /**
	 * Gets the value of the attributeAssignments property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the attributeAssignments property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getAttributeAssignments().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link AttributeAssignmentType }
	 * 
	 * 
	 */
    public List<AttributeAssignmentType> getAttributeAssignments() {
        if (attributeAssignments == null) {
            attributeAssignments = new ArrayList<AttributeAssignmentType>();
        }
        return this.attributeAssignments;
    }

    /**
	 * Gets the value of the obligationId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
    public String getObligationId() {
        return obligationId;
    }

    /**
	 * Sets the value of the obligationId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
    public void setObligationId(String value) {
        this.obligationId = value;
    }

    /**
	 * Gets the value of the fulfillOn property.
	 * 
	 * @return possible object is {@link EffectType }
	 * 
	 */
    public EffectType getFulfillOn() {
        return fulfillOn;
    }

    /**
	 * Sets the value of the fulfillOn property.
	 * 
	 * @param value
	 *            allowed object is {@link EffectType }
	 * 
	 */
    public void setFulfillOn(EffectType value) {
        this.fulfillOn = value;
    }

    public String toString() {
        StringBuilder val = new StringBuilder("ObligationType[attributeAssignment=");
        val.append(attributeAssignments);
        val.append(", obligationId=");
        val.append(obligationId);
        val.append(", fulfillOn=");
        val.append(fulfillOn);
        val.append("]");
        return val.toString();
    }
}
