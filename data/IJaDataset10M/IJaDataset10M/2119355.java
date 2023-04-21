package tr.com.srdc.isurf.gs1.ucc.ean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for OperationalExceptionCriterionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OperationalExceptionCriterionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="activityType" use="required" type="{urn:ean.ucc:plan:2}ActivityTypeCodeListType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OperationalExceptionCriterionType")
public class OperationalExceptionCriterionType {

    @XmlAttribute(required = true)
    protected ActivityTypeCodeListType activityType;

    /**
     * Gets the value of the activityType property.
     * 
     * @return
     *     possible object is
     *     {@link ActivityTypeCodeListType }
     *     
     */
    public ActivityTypeCodeListType getActivityType() {
        return activityType;
    }

    /**
     * Sets the value of the activityType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActivityTypeCodeListType }
     *     
     */
    public void setActivityType(ActivityTypeCodeListType value) {
        this.activityType = value;
    }
}
