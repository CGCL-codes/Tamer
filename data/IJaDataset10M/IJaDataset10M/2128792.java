package maltcms.io.xml.mzData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Software information (the software that produced the peak list).
 * 
 * <p>
 * Java class for softwareType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;softwareType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;name&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;version&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;comments&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;completionTime&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}dateTime&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "softwareType", propOrder = { "name", "version", "comments" })
@XmlSeeAlso({ maltcms.io.xml.mzData.DataProcessingType.Software.class })
public class SoftwareType {

    @XmlElement(required = true)
    protected String name;

    @XmlElement(required = true)
    protected String version;

    protected String comments;

    @XmlAttribute
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar completionTime;

    /**
	 * Gets the value of the comments property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
    public String getComments() {
        return this.comments;
    }

    /**
	 * Gets the value of the completionTime property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
    public XMLGregorianCalendar getCompletionTime() {
        return this.completionTime;
    }

    /**
	 * Gets the value of the name property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
    public String getName() {
        return this.name;
    }

    /**
	 * Gets the value of the version property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
    public String getVersion() {
        return this.version;
    }

    /**
	 * Sets the value of the comments property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
    public void setComments(final String value) {
        this.comments = value;
    }

    /**
	 * Sets the value of the completionTime property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
    public void setCompletionTime(final XMLGregorianCalendar value) {
        this.completionTime = value;
    }

    /**
	 * Sets the value of the name property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
    public void setName(final String value) {
        this.name = value;
    }

    /**
	 * Sets the value of the version property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
    public void setVersion(final String value) {
        this.version = value;
    }
}
