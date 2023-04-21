package com.linguamathematica.oa4j.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ActionResult complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ActionResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Action" type="{http://amplify.hapax.com}Result"/>
 *         &lt;element name="ActionType" type="{http://amplify.hapax.com}ArrayOfResult"/>
 *         &lt;element name="Decisiveness" type="{http://amplify.hapax.com}Result"/>
 *         &lt;element name="TemporalityResult" type="{http://amplify.hapax.com}ArrayOfTemporalityResult"/>
 *         &lt;element name="OfferingGuidance" type="{http://amplify.hapax.com}Result"/>
 *         &lt;element name="RequestingGuidance" type="{http://amplify.hapax.com}Result"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ActionResult", propOrder = { "action", "actionType", "decisiveness", "temporalityResult", "offeringGuidance", "requestingGuidance" })
public class ActionResult {

    @XmlElement(name = "Action", required = true, nillable = true)
    protected Result action;

    @XmlElement(name = "ActionType", required = true, nillable = true)
    protected ArrayOfResult actionType;

    @XmlElement(name = "Decisiveness", required = true, nillable = true)
    protected Result decisiveness;

    @XmlElement(name = "TemporalityResult", required = true, nillable = true)
    protected ArrayOfTemporalityResult temporalityResult;

    @XmlElement(name = "OfferingGuidance", required = true, nillable = true)
    protected Result offeringGuidance;

    @XmlElement(name = "RequestingGuidance", required = true, nillable = true)
    protected Result requestingGuidance;

    /**
	 * Gets the value of the action property.
	 * 
	 * @return possible object is {@link Result }
	 * 
	 */
    public Result getAction() {
        return action;
    }

    /**
	 * Gets the value of the actionType property.
	 * 
	 * @return possible object is {@link ArrayOfResult }
	 * 
	 */
    public ArrayOfResult getActionType() {
        return actionType;
    }

    /**
	 * Gets the value of the decisiveness property.
	 * 
	 * @return possible object is {@link Result }
	 * 
	 */
    public Result getDecisiveness() {
        return decisiveness;
    }

    /**
	 * Gets the value of the offeringGuidance property.
	 * 
	 * @return possible object is {@link Result }
	 * 
	 */
    public Result getOfferingGuidance() {
        return offeringGuidance;
    }

    /**
	 * Gets the value of the requestingGuidance property.
	 * 
	 * @return possible object is {@link Result }
	 * 
	 */
    public Result getRequestingGuidance() {
        return requestingGuidance;
    }

    /**
	 * Gets the value of the temporalityResult property.
	 * 
	 * @return possible object is {@link ArrayOfTemporalityResult }
	 * 
	 */
    public ArrayOfTemporalityResult getTemporalityResult() {
        return temporalityResult;
    }

    /**
	 * Sets the value of the action property.
	 * 
	 * @param value
	 *            allowed object is {@link Result }
	 * 
	 */
    public void setAction(final Result value) {
        action = value;
    }

    /**
	 * Sets the value of the actionType property.
	 * 
	 * @param value
	 *            allowed object is {@link ArrayOfResult }
	 * 
	 */
    public void setActionType(final ArrayOfResult value) {
        actionType = value;
    }

    /**
	 * Sets the value of the decisiveness property.
	 * 
	 * @param value
	 *            allowed object is {@link Result }
	 * 
	 */
    public void setDecisiveness(final Result value) {
        decisiveness = value;
    }

    /**
	 * Sets the value of the offeringGuidance property.
	 * 
	 * @param value
	 *            allowed object is {@link Result }
	 * 
	 */
    public void setOfferingGuidance(final Result value) {
        offeringGuidance = value;
    }

    /**
	 * Sets the value of the requestingGuidance property.
	 * 
	 * @param value
	 *            allowed object is {@link Result }
	 * 
	 */
    public void setRequestingGuidance(final Result value) {
        requestingGuidance = value;
    }

    /**
	 * Sets the value of the temporalityResult property.
	 * 
	 * @param value
	 *            allowed object is {@link ArrayOfTemporalityResult }
	 * 
	 */
    public void setTemporalityResult(final ArrayOfTemporalityResult value) {
        temporalityResult = value;
    }
}
