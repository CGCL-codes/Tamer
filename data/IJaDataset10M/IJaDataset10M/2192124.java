package org.mobicents.slee.container.component.deployment.jaxb.slee11.ra;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "description", "resourceAdaptorName", "resourceAdaptorVendor", "resourceAdaptorVersion", "resourceAdaptorTypeRef", "libraryRef", "profileSpecRef", "resourceAdaptorClasses", "configProperty" })
@XmlRootElement(name = "resource-adaptor")
public class ResourceAdaptor {

    @XmlAttribute(name = "ignore-ra-type-event-type-check")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String ignoreRaTypeEventTypeCheck;

    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;

    protected Description description;

    @XmlElement(name = "resource-adaptor-name", required = true)
    protected ResourceAdaptorName resourceAdaptorName;

    @XmlElement(name = "resource-adaptor-vendor", required = true)
    protected ResourceAdaptorVendor resourceAdaptorVendor;

    @XmlElement(name = "resource-adaptor-version", required = true)
    protected ResourceAdaptorVersion resourceAdaptorVersion;

    @XmlElement(name = "resource-adaptor-type-ref", required = true)
    protected List<ResourceAdaptorTypeRef> resourceAdaptorTypeRef;

    @XmlElement(name = "library-ref")
    protected List<LibraryRef> libraryRef;

    @XmlElement(name = "profile-spec-ref")
    protected List<ProfileSpecRef> profileSpecRef;

    @XmlElement(name = "resource-adaptor-classes", required = true)
    protected ResourceAdaptorClasses resourceAdaptorClasses;

    @XmlElement(name = "config-property")
    protected List<ConfigProperty> configProperty;

    /**
     * Gets the value of the ignoreRaTypeEventTypeCheck property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIgnoreRaTypeEventTypeCheck() {
        if (ignoreRaTypeEventTypeCheck == null) {
            return "False";
        } else {
            return ignoreRaTypeEventTypeCheck;
        }
    }

    /**
     * Sets the value of the ignoreRaTypeEventTypeCheck property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIgnoreRaTypeEventTypeCheck(String value) {
        this.ignoreRaTypeEventTypeCheck = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link Description }
     *     
     */
    public Description getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link Description }
     *     
     */
    public void setDescription(Description value) {
        this.description = value;
    }

    /**
     * Gets the value of the resourceAdaptorName property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceAdaptorName }
     *     
     */
    public ResourceAdaptorName getResourceAdaptorName() {
        return resourceAdaptorName;
    }

    /**
     * Sets the value of the resourceAdaptorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceAdaptorName }
     *     
     */
    public void setResourceAdaptorName(ResourceAdaptorName value) {
        this.resourceAdaptorName = value;
    }

    /**
     * Gets the value of the resourceAdaptorVendor property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceAdaptorVendor }
     *     
     */
    public ResourceAdaptorVendor getResourceAdaptorVendor() {
        return resourceAdaptorVendor;
    }

    /**
     * Sets the value of the resourceAdaptorVendor property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceAdaptorVendor }
     *     
     */
    public void setResourceAdaptorVendor(ResourceAdaptorVendor value) {
        this.resourceAdaptorVendor = value;
    }

    /**
     * Gets the value of the resourceAdaptorVersion property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceAdaptorVersion }
     *     
     */
    public ResourceAdaptorVersion getResourceAdaptorVersion() {
        return resourceAdaptorVersion;
    }

    /**
     * Sets the value of the resourceAdaptorVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceAdaptorVersion }
     *     
     */
    public void setResourceAdaptorVersion(ResourceAdaptorVersion value) {
        this.resourceAdaptorVersion = value;
    }

    /**
     * Gets the value of the resourceAdaptorTypeRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceAdaptorTypeRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceAdaptorTypeRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceAdaptorTypeRef }
     * 
     * 
     */
    public List<ResourceAdaptorTypeRef> getResourceAdaptorTypeRef() {
        if (resourceAdaptorTypeRef == null) {
            resourceAdaptorTypeRef = new ArrayList<ResourceAdaptorTypeRef>();
        }
        return this.resourceAdaptorTypeRef;
    }

    /**
     * Gets the value of the libraryRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the libraryRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLibraryRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LibraryRef }
     * 
     * 
     */
    public List<LibraryRef> getLibraryRef() {
        if (libraryRef == null) {
            libraryRef = new ArrayList<LibraryRef>();
        }
        return this.libraryRef;
    }

    /**
     * Gets the value of the profileSpecRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the profileSpecRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProfileSpecRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProfileSpecRef }
     * 
     * 
     */
    public List<ProfileSpecRef> getProfileSpecRef() {
        if (profileSpecRef == null) {
            profileSpecRef = new ArrayList<ProfileSpecRef>();
        }
        return this.profileSpecRef;
    }

    /**
     * Gets the value of the resourceAdaptorClasses property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceAdaptorClasses }
     *     
     */
    public ResourceAdaptorClasses getResourceAdaptorClasses() {
        return resourceAdaptorClasses;
    }

    /**
     * Sets the value of the resourceAdaptorClasses property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceAdaptorClasses }
     *     
     */
    public void setResourceAdaptorClasses(ResourceAdaptorClasses value) {
        this.resourceAdaptorClasses = value;
    }

    /**
     * Gets the value of the configProperty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the configProperty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConfigProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConfigProperty }
     * 
     * 
     */
    public List<ConfigProperty> getConfigProperty() {
        if (configProperty == null) {
            configProperty = new ArrayList<ConfigProperty>();
        }
        return this.configProperty;
    }
}
