package playground.gregor.grips.jaxb.inspire.roadtransportnetwork;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import playground.gregor.grips.jaxb.inspire.commontransportelements.TransportPropertyType;
import playground.gregor.grips.jaxb.inspire.geographicalnames.GeographicalNamePropertyType;

/**
 * <p>Java class for RoadNameType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RoadNameType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:x-inspire:specification:gmlas:CommonTransportElements:3.0}TransportPropertyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{urn:x-inspire:specification:gmlas:GeographicalNames:3.0}GeographicalNamePropertyType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RoadNameType", propOrder = { "rest" })
public class RoadNameType extends TransportPropertyType {

    @XmlElementRef(name = "name", namespace = "urn:x-inspire:specification:gmlas:RoadTransportNetwork:3.0", type = JAXBElement.class)
    protected List<JAXBElement<GeographicalNamePropertyType>> rest;

    /**
	 * Gets the rest of the content model.
	 * 
	 * <p>
	 * You are getting this "catch-all" property because of the following reason:
	 * The field name "Name" is used by two different parts of a schema. See:
	 * line 338 of file:/Users/laemmel/Documents/workspace/playgrounds/gregor/xsd/INSPIRE/inspire-foss-read-only/schemas/inspire/v3.0.1/RoadTransportNetwork.xsd
	 * line 42 of http://schemas.opengis.net/gml/3.2.1/gmlBase.xsd
	 * <p>
	 * To get rid of this property, apply a property customization to one
	 * of both of the following declarations to change their names:
	 * Gets the value of the rest property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the rest property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getRest().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link JAXBElement }{@code <}{@link GeographicalNamePropertyType }{@code >}
	 * 
	 * 
	 */
    public List<JAXBElement<GeographicalNamePropertyType>> getRest() {
        if (this.rest == null) {
            this.rest = new ArrayList<JAXBElement<GeographicalNamePropertyType>>();
        }
        return this.rest;
    }

    @Override
    public Object createNewInstance() {
        return null;
    }
}
