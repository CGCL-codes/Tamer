package employment.client;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for extendedPositionInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="extendedPositionInfoType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://xml.netbeans.org/schema/commonSchema}positionInfoType">
 *       &lt;sequence>
 *         &lt;element name="hoursPerWeek" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="location" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="exitReason" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "extendedPositionInfoType", propOrder = { "hoursPerWeek", "location", "exitReason" })
public class ExtendedPositionInfoType extends PositionInfoType {

    @XmlElement(required = true)
    protected BigInteger hoursPerWeek;

    @XmlElement(required = true)
    protected String location;

    @XmlElement(required = true)
    protected String exitReason;

    /**
     * Gets the value of the hoursPerWeek property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getHoursPerWeek() {
        return hoursPerWeek;
    }

    /**
     * Sets the value of the hoursPerWeek property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setHoursPerWeek(BigInteger value) {
        this.hoursPerWeek = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocation(String value) {
        this.location = value;
    }

    /**
     * Gets the value of the exitReason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExitReason() {
        return exitReason;
    }

    /**
     * Sets the value of the exitReason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExitReason(String value) {
        this.exitReason = value;
    }
}
