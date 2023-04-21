package org.slaatsoi.business.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * 				Repeat every 'RepeatEvery' minute(s)
 * 			
 * 
 * <p>Java class for RepeatMinutelyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RepeatMinutelyType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.slaatsoi.org/BusinessSchema}PeriodicFrequencyType">
 *       &lt;sequence>
 *         &lt;element name="RepeatEvery" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RepeatMinutelyType", propOrder = { "repeatEvery" })
public class RepeatMinutelyType extends PeriodicFrequencyType implements Serializable {

    @XmlElement(name = "RepeatEvery")
    protected int repeatEvery;

    /**
     * Gets the value of the repeatEvery property.
     * 
     */
    public int getRepeatEvery() {
        return repeatEvery;
    }

    /**
     * Sets the value of the repeatEvery property.
     * 
     */
    public void setRepeatEvery(int value) {
        this.repeatEvery = value;
    }
}
