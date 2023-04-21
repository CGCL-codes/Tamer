package org.herasaf.xacml.core.policy.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ObligationsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ObligationsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Obligation" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * See:	<a href="http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June 2006</a> page 65, for further information.
 * 
 * @version 1.0
 * @author <i>generated</i>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObligationsType", propOrder = { "obligations" })
public class ObligationsType implements Serializable {

    private static final long serialVersionUID = 632768732L;

    @XmlElement(name = "Obligation", required = true)
    protected List<ObligationType> obligations;

    /**
     * Gets the value of the obligations property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the obligations property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObligations().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ObligationType }
     * 
     * 
     */
    public List<ObligationType> getObligations() {
        if (obligations == null) {
            obligations = new ArrayList<ObligationType>();
        }
        return this.obligations;
    }

    @Override
    public String toString() {
        StringBuilder val = new StringBuilder("ObligationsType[obligations=");
        val.append(obligations);
        val.append("]");
        return val.toString();
    }
}
