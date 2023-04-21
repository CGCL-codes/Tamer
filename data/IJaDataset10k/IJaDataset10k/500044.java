package net.virtualearth.dev.webservices.v1.imagery;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfstring;
import net.virtualearth.dev.webservices.v1.common.ImageType;
import net.virtualearth.dev.webservices.v1.common.MapStyle;
import net.virtualearth.dev.webservices.v1.common.SizeOfint;
import net.virtualearth.dev.webservices.v1.common.UriScheme;

/**
 * <p>Java class for MapUriOptions complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MapUriOptions">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DisplayLayers" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOfstring" minOccurs="0"/>
 *         &lt;element name="ImageSize" type="{http://dev.virtualearth.net/webservices/v1/common}SizeOfint" minOccurs="0"/>
 *         &lt;element name="ImageType" type="{http://dev.virtualearth.net/webservices/v1/common}ImageType" minOccurs="0"/>
 *         &lt;element name="PreventIconCollision" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="Style" type="{http://dev.virtualearth.net/webservices/v1/common}MapStyle" minOccurs="0"/>
 *         &lt;element name="UriScheme" type="{http://dev.virtualearth.net/webservices/v1/common}UriScheme" minOccurs="0"/>
 *         &lt;element name="ZoomLevel" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MapUriOptions", propOrder = { "displayLayers", "imageSize", "imageType", "preventIconCollision", "style", "uriScheme", "zoomLevel" })
public class MapUriOptions implements Serializable {

    private static final long serialVersionUID = 2461660169443089969L;

    @XmlElement(name = "DisplayLayers", nillable = true)
    protected ArrayOfstring displayLayers;

    @XmlElement(name = "ImageSize", nillable = true)
    protected SizeOfint imageSize;

    @XmlElement(name = "ImageType")
    protected ImageType imageType;

    @XmlElement(name = "PreventIconCollision")
    protected Boolean preventIconCollision;

    @XmlElement(name = "Style")
    protected MapStyle style;

    @XmlElement(name = "UriScheme")
    protected UriScheme uriScheme;

    @XmlElement(name = "ZoomLevel", nillable = true)
    protected Integer zoomLevel;

    /**
     * Gets the value of the displayLayers property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfstring }
     *     
     */
    public ArrayOfstring getDisplayLayers() {
        return displayLayers;
    }

    /**
     * Sets the value of the displayLayers property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfstring }
     *     
     */
    public void setDisplayLayers(ArrayOfstring value) {
        this.displayLayers = value;
    }

    /**
     * Gets the value of the imageSize property.
     * 
     * @return
     *     possible object is
     *     {@link SizeOfint }
     *     
     */
    public SizeOfint getImageSize() {
        return imageSize;
    }

    /**
     * Sets the value of the imageSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link SizeOfint }
     *     
     */
    public void setImageSize(SizeOfint value) {
        this.imageSize = value;
    }

    /**
     * Gets the value of the imageType property.
     * 
     * @return
     *     possible object is
     *     {@link ImageType }
     *     
     */
    public ImageType getImageType() {
        return imageType;
    }

    /**
     * Sets the value of the imageType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageType }
     *     
     */
    public void setImageType(ImageType value) {
        this.imageType = value;
    }

    /**
     * Gets the value of the preventIconCollision property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPreventIconCollision() {
        return preventIconCollision;
    }

    /**
     * Sets the value of the preventIconCollision property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPreventIconCollision(Boolean value) {
        this.preventIconCollision = value;
    }

    /**
     * Gets the value of the style property.
     * 
     * @return
     *     possible object is
     *     {@link MapStyle }
     *     
     */
    public MapStyle getStyle() {
        return style;
    }

    /**
     * Sets the value of the style property.
     * 
     * @param value
     *     allowed object is
     *     {@link MapStyle }
     *     
     */
    public void setStyle(MapStyle value) {
        this.style = value;
    }

    /**
     * Gets the value of the uriScheme property.
     * 
     * @return
     *     possible object is
     *     {@link UriScheme }
     *     
     */
    public UriScheme getUriScheme() {
        return uriScheme;
    }

    /**
     * Sets the value of the uriScheme property.
     * 
     * @param value
     *     allowed object is
     *     {@link UriScheme }
     *     
     */
    public void setUriScheme(UriScheme value) {
        this.uriScheme = value;
    }

    /**
     * Gets the value of the zoomLevel property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getZoomLevel() {
        return zoomLevel;
    }

    /**
     * Sets the value of the zoomLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setZoomLevel(Integer value) {
        this.zoomLevel = value;
    }
}
