package playground.tnicolai.matsim4opus.jaxb.test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>Java class for planCalcScoreType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="planCalcScoreType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="activityType_0" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *         &lt;element name="activityType_1" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "planCalcScoreType", propOrder = { "activityType0", "activityType1" })
public class PlanCalcScoreType {

    @XmlElement(name = "activityType_0", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String activityType0;

    @XmlElement(name = "activityType_1", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String activityType1;

    /**
     * Gets the value of the activityType0 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActivityType0() {
        return activityType0;
    }

    /**
     * Sets the value of the activityType0 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActivityType0(String value) {
        this.activityType0 = value;
    }

    /**
     * Gets the value of the activityType1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActivityType1() {
        return activityType1;
    }

    /**
     * Sets the value of the activityType1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActivityType1(String value) {
        this.activityType1 = value;
    }
}
