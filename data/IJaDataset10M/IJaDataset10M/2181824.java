package tr.com.srdc.isurf.gs1.ucc.ean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for EventCommentType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;EventCommentType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;commentText&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;265&quot;/&gt;
 *               &lt;minLength value=&quot;1&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;plannerIdentification&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;80&quot;/&gt;
 *               &lt;minLength value=&quot;1&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;dateTimeStamp&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}dateTime&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventCommentType", propOrder = { "commentText", "plannerIdentification", "dateTimeStamp" })
public class EventCommentType {

    @XmlElement(required = true)
    protected String commentText;

    @XmlElement(required = true)
    protected String plannerIdentification;

    @XmlElement(required = true)
    protected XMLGregorianCalendar dateTimeStamp;

    /**
   * Gets the value of the commentText property.
   * 
   * @return possible object is {@link String }
   * 
   */
    public String getCommentText() {
        return commentText;
    }

    /**
   * Sets the value of the commentText property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
    public void setCommentText(String value) {
        this.commentText = value;
    }

    /**
   * Gets the value of the plannerIdentification property.
   * 
   * @return possible object is {@link String }
   * 
   */
    public String getPlannerIdentification() {
        return plannerIdentification;
    }

    /**
   * Sets the value of the plannerIdentification property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
    public void setPlannerIdentification(String value) {
        this.plannerIdentification = value;
    }

    /**
   * Gets the value of the dateTimeStamp property.
   * 
   * @return possible object is {@link XMLGregorianCalendar }
   * 
   */
    public XMLGregorianCalendar getDateTimeStamp() {
        return dateTimeStamp;
    }

    /**
   * Sets the value of the dateTimeStamp property.
   * 
   * @param value
   *          allowed object is {@link XMLGregorianCalendar }
   * 
   */
    public void setDateTimeStamp(XMLGregorianCalendar value) {
        this.dateTimeStamp = value;
    }
}
