package maltcms.io.xml.mzData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * Description of the source file, including location and type.
 * 
 * <p>
 * Java class for sourceFileType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;sourceFileType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;nameOfFile&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;pathToFile&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}anyURI&quot;/&gt;
 *         &lt;element name=&quot;fileType&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sourceFileType", propOrder = { "nameOfFile", "pathToFile", "fileType" })
public class SourceFileType {

    @XmlElement(required = true)
    protected String nameOfFile;

    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String pathToFile;

    protected String fileType;

    /**
	 * Gets the value of the fileType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
    public String getFileType() {
        return this.fileType;
    }

    /**
	 * Gets the value of the nameOfFile property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
    public String getNameOfFile() {
        return this.nameOfFile;
    }

    /**
	 * Gets the value of the pathToFile property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
    public String getPathToFile() {
        return this.pathToFile;
    }

    /**
	 * Sets the value of the fileType property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
    public void setFileType(final String value) {
        this.fileType = value;
    }

    /**
	 * Sets the value of the nameOfFile property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
    public void setNameOfFile(final String value) {
        this.nameOfFile = value;
    }

    /**
	 * Sets the value of the pathToFile property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
    public void setPathToFile(final String value) {
        this.pathToFile = value;
    }
}
