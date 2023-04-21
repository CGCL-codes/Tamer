package org.isurf.spmpilot.reconciliation;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for reconciliationStatus complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="reconciliationStatus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="correlationID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reconciled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reconciliationStatus", propOrder = { "correlationID", "reconciled" })
public class ReconciliationStatus implements Serializable {

    private static final long serialVersionUID = 650677285542368469L;

    protected String correlationID;

    protected boolean reconciled;

    /**
	 * Gets the value of the correlationID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
    public String getCorrelationID() {
        return correlationID;
    }

    /**
	 * Sets the value of the correlationID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
    public void setCorrelationID(String value) {
        this.correlationID = value;
    }

    /**
	 * Gets the value of the reconciled property.
	 * 
	 */
    public boolean isReconciled() {
        return reconciled;
    }

    /**
	 * Sets the value of the reconciled property.
	 * 
	 */
    public void setReconciled(boolean value) {
        this.reconciled = value;
    }
}
