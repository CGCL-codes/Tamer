package org.etexascode.j2735;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ITIScodesAndText complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ITIScodesAndText">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="100">
 *         &lt;element name="SEQUENCE">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="item">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;choice>
 *                             &lt;element name="itis" type="{http://www.DSRC-Adopted-02-00-36/ITIS}ITIScodes"/>
 *                             &lt;element name="text" type="{http://www.DSRC-Adopted-02-00-36/ITIS}ITIStext"/>
 *                           &lt;/choice>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
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
@XmlType(name = "ITIScodesAndText", namespace = "http://www.DSRC-Adopted-02-00-36/ITIS", propOrder = { "sequence" })
public class ITIScodesAndText {

    @XmlElement(name = "SEQUENCE", required = true)
    protected List<ITIScodesAndText.SEQUENCE> sequence;

    /**
     * Gets the value of the sequence property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sequence property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSEQUENCE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ITIScodesAndText.SEQUENCE }
     * 
     * 
     */
    public List<ITIScodesAndText.SEQUENCE> getSEQUENCE() {
        if (sequence == null) {
            sequence = new ArrayList<ITIScodesAndText.SEQUENCE>();
        }
        return this.sequence;
    }

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
     *         &lt;element name="item">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;choice>
     *                   &lt;element name="itis" type="{http://www.DSRC-Adopted-02-00-36/ITIS}ITIScodes"/>
     *                   &lt;element name="text" type="{http://www.DSRC-Adopted-02-00-36/ITIS}ITIStext"/>
     *                 &lt;/choice>
     *               &lt;/restriction>
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
    @XmlType(name = "", propOrder = { "item" })
    public static class SEQUENCE {

        @XmlElement(namespace = "http://www.DSRC-Adopted-02-00-36/ITIS", required = true)
        protected ITIScodesAndText.SEQUENCE.Item item;

        /**
         * Gets the value of the item property.
         * 
         * @return
         *     possible object is
         *     {@link ITIScodesAndText.SEQUENCE.Item }
         *     
         */
        public ITIScodesAndText.SEQUENCE.Item getItem() {
            return item;
        }

        /**
         * Sets the value of the item property.
         * 
         * @param value
         *     allowed object is
         *     {@link ITIScodesAndText.SEQUENCE.Item }
         *     
         */
        public void setItem(ITIScodesAndText.SEQUENCE.Item value) {
            this.item = value;
        }

        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;choice>
         *         &lt;element name="itis" type="{http://www.DSRC-Adopted-02-00-36/ITIS}ITIScodes"/>
         *         &lt;element name="text" type="{http://www.DSRC-Adopted-02-00-36/ITIS}ITIStext"/>
         *       &lt;/choice>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = { "itis", "text" })
        public static class Item {

            @XmlElement(namespace = "http://www.DSRC-Adopted-02-00-36/ITIS")
            protected Long itis;

            @XmlElement(namespace = "http://www.DSRC-Adopted-02-00-36/ITIS")
            protected String text;

            /**
             * Gets the value of the itis property.
             * 
             * @return
             *     possible object is
             *     {@link Long }
             *     
             */
            public Long getItis() {
                return itis;
            }

            /**
             * Sets the value of the itis property.
             * 
             * @param value
             *     allowed object is
             *     {@link Long }
             *     
             */
            public void setItis(Long value) {
                this.itis = value;
            }

            /**
             * Gets the value of the text property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getText() {
                return text;
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
        }
    }
}
