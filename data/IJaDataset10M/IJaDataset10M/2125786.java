package maltcms.io.xml.mzML;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * List and descriptions of precursors to the spectrum currently being
 * described.
 * 
 * <p>
 * Java class for PrecursorListType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;PrecursorListType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;precursor&quot; type=&quot;{http://psi.hupo.org/schema_revision/mzML_1.0.0}PrecursorType&quot; maxOccurs=&quot;unbounded&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;count&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}nonNegativeInteger&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PrecursorListType", propOrder = { "precursor" })
public class PrecursorListType {

    @XmlElement(required = true)
    protected List<PrecursorType> precursor;

    @XmlAttribute(required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger count;

    /**
	 * Gets the value of the count property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
    public BigInteger getCount() {
        return this.count;
    }

    /**
	 * Gets the value of the precursor property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the precursor property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getPrecursor().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link PrecursorType }
	 * 
	 * 
	 */
    public List<PrecursorType> getPrecursor() {
        if (this.precursor == null) {
            this.precursor = new ArrayList<PrecursorType>();
        }
        return this.precursor;
    }

    /**
	 * Sets the value of the count property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
    public void setCount(final BigInteger value) {
        this.count = value;
    }
}
