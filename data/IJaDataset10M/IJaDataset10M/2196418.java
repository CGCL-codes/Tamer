package org.isurf.spmpilot.context;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.isurf.spmiddleware.model.BusinessContext;

/**
 * <p>
 * Java class for setRFIDBusinessContext complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setRFIDBusinessContext">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="businessContext" type="{urn:org.isurf.spmiddleware.model}BusinessContext" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setRFIDBusinessContext", propOrder = { "businessContext" })
public class SetRFIDBusinessContext {

    protected BusinessContext businessContext;

    /**
	 * Gets the value of the businessContext property.
	 * 
	 * @return possible object is {@link BusinessContext }
	 * 
	 */
    public BusinessContext getBusinessContext() {
        return businessContext;
    }

    /**
	 * Sets the value of the businessContext property.
	 * 
	 * @param value
	 *            allowed object is {@link BusinessContext }
	 * 
	 */
    public void setBusinessContext(BusinessContext value) {
        this.businessContext = value;
    }
}
