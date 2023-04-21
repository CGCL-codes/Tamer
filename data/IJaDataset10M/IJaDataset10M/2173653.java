package by.prognoz.exjaxb.test.adhoc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for tag.caption complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tag.caption">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Caption">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;extension base="{http://www.prognoz.ru/Adhoc}obj">
 *                 &lt;attribute name="Text" type="{http://www.w3.org/2001/XMLSchema}string" default="CaptionText" />
 *                 &lt;attribute name="SubText" type="{http://www.w3.org/2001/XMLSchema}string" default="" />
 *                 &lt;attribute name="VisibleText" type="{http://www.prognoz.ru/Adhoc}bool" default="True" />
 *                 &lt;attribute name="VisibleSubText" type="{http://www.prognoz.ru/Adhoc}bool" />
 *               &lt;/extension>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tag.caption", propOrder = { "caption" })
public class TagCaption {

    @XmlElement(name = "Caption", required = true)
    protected TagCaption.Caption caption;

    /**
     * Gets the value of the caption property.
     * 
     * @return
     *     possible object is
     *     {@link TagCaption.Caption }
     *     
     */
    public TagCaption.Caption getCaption() {
        return caption;
    }

    /**
     * Sets the value of the caption property.
     * 
     * @param value
     *     allowed object is
     *     {@link TagCaption.Caption }
     *     
     */
    public void setCaption(TagCaption.Caption value) {
        this.caption = value;
    }

    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;extension base="{http://www.prognoz.ru/Adhoc}obj">
     *       &lt;attribute name="Text" type="{http://www.w3.org/2001/XMLSchema}string" default="CaptionText" />
     *       &lt;attribute name="SubText" type="{http://www.w3.org/2001/XMLSchema}string" default="" />
     *       &lt;attribute name="VisibleText" type="{http://www.prognoz.ru/Adhoc}bool" default="True" />
     *       &lt;attribute name="VisibleSubText" type="{http://www.prognoz.ru/Adhoc}bool" />
     *     &lt;/extension>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Caption extends Obj {

        @XmlAttribute(name = "Text")
        protected String text;

        @XmlAttribute(name = "SubText")
        protected String subText;

        @XmlAttribute(name = "VisibleText")
        protected String visibleText;

        @XmlAttribute(name = "VisibleSubText")
        protected String visibleSubText;

        /**
         * Gets the value of the text property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getText() {
            if (text == null) {
                return "CaptionText";
            } else {
                return text;
            }
        }

        /**
         * Sets the value of the text property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setText(String value) {
            this.text = value;
        }

        /**
         * Gets the value of the subText property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSubText() {
            if (subText == null) {
                return "";
            } else {
                return subText;
            }
        }

        /**
         * Sets the value of the subText property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSubText(String value) {
            this.subText = value;
        }

        /**
         * Gets the value of the visibleText property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVisibleText() {
            if (visibleText == null) {
                return "True";
            } else {
                return visibleText;
            }
        }

        /**
         * Sets the value of the visibleText property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVisibleText(String value) {
            this.visibleText = value;
        }

        /**
         * Gets the value of the visibleSubText property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVisibleSubText() {
            return visibleSubText;
        }

        /**
         * Sets the value of the visibleSubText property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVisibleSubText(String value) {
            this.visibleSubText = value;
        }
    }
}
