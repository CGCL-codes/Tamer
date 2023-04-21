package eu.vph.predict.vre.in_silico.business.application.chaste.chaste_parameters.jaxb.v1_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for load_mesh_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="load_mesh_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="conductivity_media" use="required" type="{}media_type" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "load_mesh_type")
public class LoadMeshType {

    @XmlAttribute(required = true)
    protected String name;

    @XmlAttribute(name = "conductivity_media", required = true)
    protected MediaType conductivityMedia;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the conductivityMedia property.
     * 
     * @return
     *     possible object is
     *     {@link MediaType }
     *     
     */
    public MediaType getConductivityMedia() {
        return conductivityMedia;
    }

    /**
     * Sets the value of the conductivityMedia property.
     * 
     * @param value
     *     allowed object is
     *     {@link MediaType }
     *     
     */
    public void setConductivityMedia(MediaType value) {
        this.conductivityMedia = value;
    }
}
