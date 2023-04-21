package gen.core.tbx;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}fileDesc"/>
 *         &lt;element ref="{}encodingDesc" minOccurs="0"/>
 *         &lt;element ref="{}revisionDesc" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "fileDesc", "encodingDesc", "revisionDesc" })
@XmlRootElement(name = "martifHeader")
public class MartifHeader {

    @XmlElement(required = true)
    protected FileDesc fileDesc;

    protected EncodingDesc encodingDesc;

    protected RevisionDesc revisionDesc;

    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the fileDesc property.
     * 
     * @return
     *     possible object is
     *     {@link FileDesc }
     *     
     */
    public FileDesc getFileDesc() {
        return fileDesc;
    }

    /**
     * Sets the value of the fileDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link FileDesc }
     *     
     */
    public void setFileDesc(FileDesc value) {
        this.fileDesc = value;
    }

    /**
     * Gets the value of the encodingDesc property.
     * 
     * @return
     *     possible object is
     *     {@link EncodingDesc }
     *     
     */
    public EncodingDesc getEncodingDesc() {
        return encodingDesc;
    }

    /**
     * Sets the value of the encodingDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link EncodingDesc }
     *     
     */
    public void setEncodingDesc(EncodingDesc value) {
        this.encodingDesc = value;
    }

    /**
     * Gets the value of the revisionDesc property.
     * 
     * @return
     *     possible object is
     *     {@link RevisionDesc }
     *     
     */
    public RevisionDesc getRevisionDesc() {
        return revisionDesc;
    }

    /**
     * Sets the value of the revisionDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link RevisionDesc }
     *     
     */
    public void setRevisionDesc(RevisionDesc value) {
        this.revisionDesc = value;
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
}
