package net.virtualearth.dev.webservices.v1.route;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for RouteResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RouteResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Legs" type="{http://dev.virtualearth.net/webservices/v1/route}ArrayOfRouteLeg" minOccurs="0"/>
 *         &lt;element name="RoutePath" type="{http://dev.virtualearth.net/webservices/v1/route}RoutePath" minOccurs="0"/>
 *         &lt;element name="Summary" type="{http://dev.virtualearth.net/webservices/v1/route}RouteSummary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RouteResult", propOrder = { "legs", "routePath", "summary" })
public class RouteResult implements Serializable {

    private static final long serialVersionUID = 2461660169443089969L;

    @XmlElement(name = "Legs", nillable = true)
    protected ArrayOfRouteLeg legs;

    @XmlElement(name = "RoutePath", nillable = true)
    protected RoutePath routePath;

    @XmlElement(name = "Summary", nillable = true)
    protected RouteSummary summary;

    /**
     * Gets the value of the legs property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfRouteLeg }
     *     
     */
    public ArrayOfRouteLeg getLegs() {
        return legs;
    }

    /**
     * Sets the value of the legs property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfRouteLeg }
     *     
     */
    public void setLegs(ArrayOfRouteLeg value) {
        this.legs = value;
    }

    /**
     * Gets the value of the routePath property.
     * 
     * @return
     *     possible object is
     *     {@link RoutePath }
     *     
     */
    public RoutePath getRoutePath() {
        return routePath;
    }

    /**
     * Sets the value of the routePath property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoutePath }
     *     
     */
    public void setRoutePath(RoutePath value) {
        this.routePath = value;
    }

    /**
     * Gets the value of the summary property.
     * 
     * @return
     *     possible object is
     *     {@link RouteSummary }
     *     
     */
    public RouteSummary getSummary() {
        return summary;
    }

    /**
     * Sets the value of the summary property.
     * 
     * @param value
     *     allowed object is
     *     {@link RouteSummary }
     *     
     */
    public void setSummary(RouteSummary value) {
        this.summary = value;
    }
}
