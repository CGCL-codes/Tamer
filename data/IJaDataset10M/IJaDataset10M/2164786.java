package tr.com.srdc.isurf.gs1.ucc.ean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for DocumentCommandType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;DocumentCommandType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{urn:ean.ucc:2}AbstractCommandType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;documentCommandHeader&quot; type=&quot;{urn:ean.ucc:2}DocumentCommandHeaderType&quot;/&gt;
 *         &lt;element name=&quot;documentCommandOperand&quot; type=&quot;{urn:ean.ucc:2}DocumentCommandOperandType&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentCommandType", namespace = "", propOrder = { "documentCommandHeader", "documentCommandOperand" })
public class DocumentCommandType extends AbstractCommandType {

    @XmlElement(required = true)
    protected DocumentCommandHeaderType documentCommandHeader;

    @XmlElement(required = true)
    protected DocumentCommandOperandType documentCommandOperand;

    /**
   * Gets the value of the documentCommandHeader property.
   * 
   * @return possible object is {@link DocumentCommandHeaderType }
   * 
   */
    public DocumentCommandHeaderType getDocumentCommandHeader() {
        return documentCommandHeader;
    }

    /**
   * Sets the value of the documentCommandHeader property.
   * 
   * @param value
   *          allowed object is {@link DocumentCommandHeaderType }
   * 
   */
    public void setDocumentCommandHeader(DocumentCommandHeaderType value) {
        this.documentCommandHeader = value;
    }

    /**
   * Gets the value of the documentCommandOperand property.
   * 
   * @return possible object is {@link DocumentCommandOperandType }
   * 
   */
    public DocumentCommandOperandType getDocumentCommandOperand() {
        return documentCommandOperand;
    }

    /**
   * Sets the value of the documentCommandOperand property.
   * 
   * @param value
   *          allowed object is {@link DocumentCommandOperandType }
   * 
   */
    public void setDocumentCommandOperand(DocumentCommandOperandType value) {
        this.documentCommandOperand = value;
    }
}
