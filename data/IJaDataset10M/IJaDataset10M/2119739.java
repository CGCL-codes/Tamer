package com.bing.maps.rest.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ImageryMetadata complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ImageryMetadata">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/search/local/ws/rest/v1}Resource">
 *       &lt;sequence>
 *         &lt;element name="ImageUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ImageUrlSubdomains" type="{http://schemas.microsoft.com/search/local/ws/rest/v1}ArrayOfString" minOccurs="0"/>
 *         &lt;element name="ImageWidth" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ImageHeight" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ZoomMin" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ZoomMax" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="VintageStart" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VintageEnd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ImageryProvider" type="{http://schemas.microsoft.com/search/local/ws/rest/v1}ImageryProvider" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ImageryMetadata", propOrder = { "imageUrl", "imageUrlSubdomains", "imageWidth", "imageHeight", "zoomMin", "zoomMax", "vintageStart", "vintageEnd", "imageryProvider" })
public class ImageryMetadata extends Resource implements Serializable {

    private static final long serialVersionUID = 2461660169443089969L;

    @XmlElement(name = "ImageUrl")
    protected String imageUrl;

    @XmlElement(name = "ImageUrlSubdomains")
    protected ArrayOfString imageUrlSubdomains;

    @XmlElement(name = "ImageWidth")
    protected int imageWidth;

    @XmlElement(name = "ImageHeight")
    protected int imageHeight;

    @XmlElement(name = "ZoomMin")
    protected int zoomMin;

    @XmlElement(name = "ZoomMax")
    protected int zoomMax;

    @XmlElement(name = "VintageStart")
    protected String vintageStart;

    @XmlElement(name = "VintageEnd")
    protected String vintageEnd;

    @XmlElement(name = "ImageryProvider")
    protected List<ImageryProvider> imageryProvider;

    /**
     * Gets the value of the imageUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the value of the imageUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImageUrl(String value) {
        this.imageUrl = value;
    }

    /**
     * Gets the value of the imageUrlSubdomains property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getImageUrlSubdomains() {
        return imageUrlSubdomains;
    }

    /**
     * Sets the value of the imageUrlSubdomains property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setImageUrlSubdomains(ArrayOfString value) {
        this.imageUrlSubdomains = value;
    }

    /**
     * Gets the value of the imageWidth property.
     * 
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     * Sets the value of the imageWidth property.
     * 
     */
    public void setImageWidth(int value) {
        this.imageWidth = value;
    }

    /**
     * Gets the value of the imageHeight property.
     * 
     */
    public int getImageHeight() {
        return imageHeight;
    }

    /**
     * Sets the value of the imageHeight property.
     * 
     */
    public void setImageHeight(int value) {
        this.imageHeight = value;
    }

    /**
     * Gets the value of the zoomMin property.
     * 
     */
    public int getZoomMin() {
        return zoomMin;
    }

    /**
     * Sets the value of the zoomMin property.
     * 
     */
    public void setZoomMin(int value) {
        this.zoomMin = value;
    }

    /**
     * Gets the value of the zoomMax property.
     * 
     */
    public int getZoomMax() {
        return zoomMax;
    }

    /**
     * Sets the value of the zoomMax property.
     * 
     */
    public void setZoomMax(int value) {
        this.zoomMax = value;
    }

    /**
     * Gets the value of the vintageStart property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVintageStart() {
        return vintageStart;
    }

    /**
     * Sets the value of the vintageStart property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVintageStart(String value) {
        this.vintageStart = value;
    }

    /**
     * Gets the value of the vintageEnd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVintageEnd() {
        return vintageEnd;
    }

    /**
     * Sets the value of the vintageEnd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVintageEnd(String value) {
        this.vintageEnd = value;
    }

    /**
     * Gets the value of the imageryProvider property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the imageryProvider property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImageryProvider().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ImageryProvider }
     * 
     * 
     */
    public List<ImageryProvider> getImageryProvider() {
        if (imageryProvider == null) {
            imageryProvider = new ArrayList<ImageryProvider>();
        }
        return this.imageryProvider;
    }
}
