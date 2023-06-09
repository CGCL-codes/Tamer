package org.opencarto.kml.v22.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for FolderType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FolderType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/kml/2.2}AbstractContainerType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}AbstractFeatureGroup" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}FolderSimpleExtensionGroup" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}FolderObjectExtensionGroup" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FolderType", propOrder = { "abstractFeatureGroups", "folderSimpleExtensionGroups", "folderObjectExtensionGroups" })
public class FolderType extends AbstractContainerType {

    @XmlElementRef(name = "AbstractFeatureGroup", namespace = "http://www.opengis.net/kml/2.2", type = JAXBElement.class)
    protected List<JAXBElement<? extends AbstractFeatureType>> abstractFeatureGroups;

    @XmlElement(name = "FolderSimpleExtensionGroup")
    @XmlSchemaType(name = "anySimpleType")
    protected List<Object> folderSimpleExtensionGroups;

    @XmlElement(name = "FolderObjectExtensionGroup")
    protected List<AbstractObjectType> folderObjectExtensionGroups;

    /**
     * Gets the value of the abstractFeatureGroups property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractFeatureGroups property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractFeatureGroups().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link ScreenOverlayType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractFeatureType }{@code >}
     * {@link JAXBElement }{@code <}{@link GroundOverlayType }{@code >}
     * {@link JAXBElement }{@code <}{@link FolderType }{@code >}
     * {@link JAXBElement }{@code <}{@link PlacemarkType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractOverlayType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractContainerType }{@code >}
     * {@link JAXBElement }{@code <}{@link NetworkLinkType }{@code >}
     * {@link JAXBElement }{@code <}{@link PhotoOverlayType }{@code >}
     * {@link JAXBElement }{@code <}{@link DocumentType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AbstractFeatureType>> getAbstractFeatureGroups() {
        if (abstractFeatureGroups == null) {
            abstractFeatureGroups = new ArrayList<JAXBElement<? extends AbstractFeatureType>>();
        }
        return this.abstractFeatureGroups;
    }

    /**
     * Gets the value of the folderSimpleExtensionGroups property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the folderSimpleExtensionGroups property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFolderSimpleExtensionGroups().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getFolderSimpleExtensionGroups() {
        if (folderSimpleExtensionGroups == null) {
            folderSimpleExtensionGroups = new ArrayList<Object>();
        }
        return this.folderSimpleExtensionGroups;
    }

    /**
     * Gets the value of the folderObjectExtensionGroups property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the folderObjectExtensionGroups property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFolderObjectExtensionGroups().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AbstractObjectType }
     * 
     * 
     */
    public List<AbstractObjectType> getFolderObjectExtensionGroups() {
        if (folderObjectExtensionGroups == null) {
            folderObjectExtensionGroups = new ArrayList<AbstractObjectType>();
        }
        return this.folderObjectExtensionGroups;
    }
}
