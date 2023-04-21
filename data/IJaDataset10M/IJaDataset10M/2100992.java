package net.virtualearth.dev.webservices.v1.route;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ArrayOfRouteResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRouteResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RouteResult" type="{http://dev.virtualearth.net/webservices/v1/route}RouteResult" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfRouteResult", propOrder = { "routeResult" })
public class ArrayOfRouteResult implements Serializable {

    private static final long serialVersionUID = 2461660169443089969L;

    @XmlElement(name = "RouteResult", nillable = true)
    protected List<RouteResult> routeResult;

    /**
     * Gets the value of the routeResult property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the routeResult property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRouteResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RouteResult }
     * 
     * 
     */
    public List<RouteResult> getRouteResult() {
        if (routeResult == null) {
            routeResult = new ArrayList<RouteResult>();
        }
        return this.routeResult;
    }
}
