package org.tolven.ccr;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for OrderRxHistoryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrderRxHistoryType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:astm-org:CCR}CCRCodedDataObjectType">
 *       &lt;sequence>
 *         &lt;element name="FulfillmentMethod" type="{urn:astm-org:CCR}CodedDescriptionType" minOccurs="0"/>
 *         &lt;element name="Provider" type="{urn:astm-org:CCR}ActorReferenceType" minOccurs="0"/>
 *         &lt;element name="Location" type="{urn:astm-org:CCR}ActorReferenceType" minOccurs="0"/>
 *         &lt;element ref="{urn:astm-org:CCR}Reaction" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ProductName" type="{urn:astm-org:CCR}CodedDescriptionType" minOccurs="0"/>
 *         &lt;element name="BrandName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Manufacturer" type="{urn:astm-org:CCR}ActorReferenceType" minOccurs="0"/>
 *         &lt;element name="IDs" type="{urn:astm-org:CCR}IDType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Strength" type="{urn:astm-org:CCR}MeasureType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Form" type="{urn:astm-org:CCR}CodedDescriptionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Concentration" type="{urn:astm-org:CCR}MeasureType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Quantity" type="{urn:astm-org:CCR}MeasureType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="LabelInstructions" type="{urn:astm-org:CCR}InstructionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="SeriesNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderRxHistoryType", propOrder = { "rest" })
public class OrderRxHistoryType extends CCRCodedDataObjectType {

    @XmlElementRefs({ @XmlElementRef(name = "Reaction", namespace = "urn:astm-org:CCR", type = Reaction.class), @XmlElementRef(name = "Quantity", namespace = "urn:astm-org:CCR", type = JAXBElement.class), @XmlElementRef(name = "Strength", namespace = "urn:astm-org:CCR", type = JAXBElement.class), @XmlElementRef(name = "IDs", namespace = "urn:astm-org:CCR", type = JAXBElement.class), @XmlElementRef(name = "Location", namespace = "urn:astm-org:CCR", type = JAXBElement.class), @XmlElementRef(name = "ProductName", namespace = "urn:astm-org:CCR", type = JAXBElement.class), @XmlElementRef(name = "Manufacturer", namespace = "urn:astm-org:CCR", type = JAXBElement.class), @XmlElementRef(name = "FulfillmentMethod", namespace = "urn:astm-org:CCR", type = JAXBElement.class), @XmlElementRef(name = "Provider", namespace = "urn:astm-org:CCR", type = JAXBElement.class), @XmlElementRef(name = "Concentration", namespace = "urn:astm-org:CCR", type = JAXBElement.class), @XmlElementRef(name = "SeriesNumber", namespace = "urn:astm-org:CCR", type = JAXBElement.class), @XmlElementRef(name = "BrandName", namespace = "urn:astm-org:CCR", type = JAXBElement.class), @XmlElementRef(name = "Form", namespace = "urn:astm-org:CCR", type = JAXBElement.class), @XmlElementRef(name = "LabelInstructions", namespace = "urn:astm-org:CCR", type = JAXBElement.class) })
    protected List<Object> rest;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "IDs" is used by two different parts of a schema. See: 
     * line 997 of file:/C:/Kul/Work/FromJohn/ADJE2369.8762.xsd
     * line 262 of file:/C:/Kul/Work/FromJohn/ADJE2369.8762.xsd
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
     * {@link Reaction }
     * {@link JAXBElement }{@code <}{@link MeasureType }{@code >}
     * {@link JAXBElement }{@code <}{@link MeasureType }{@code >}
     * {@link JAXBElement }{@code <}{@link IDType }{@code >}
     * {@link JAXBElement }{@code <}{@link ActorReferenceType }{@code >}
     * {@link JAXBElement }{@code <}{@link CodedDescriptionType }{@code >}
     * {@link JAXBElement }{@code <}{@link ActorReferenceType }{@code >}
     * {@link JAXBElement }{@code <}{@link CodedDescriptionType }{@code >}
     * {@link JAXBElement }{@code <}{@link ActorReferenceType }{@code >}
     * {@link JAXBElement }{@code <}{@link MeasureType }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link CodedDescriptionType }{@code >}
     * {@link JAXBElement }{@code <}{@link InstructionType }{@code >}
     * 
     * 
     */
    public List<Object> getRest() {
        if (rest == null) {
            rest = new ArrayList<Object>();
        }
        return this.rest;
    }
}
