package ua.od.lonewolf.Crow.Model.RIF;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="LONG-NAME" type="{http://automotive-his.de/schema/rif}STRING" minOccurs="0"/>
 *         &lt;element name="DESC" type="{http://automotive-his.de/schema/rif}STRING" minOccurs="0"/>
 *         &lt;element name="DEFAULT-VALUE" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice minOccurs="0">
 *                   &lt;element ref="{http://automotive-his.de/schema/rif}ATTRIBUTE-VALUE-SIMPLE"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="IDENTIFIER" type="{http://automotive-his.de/schema/rif}STRING" minOccurs="0"/>
 *         &lt;element name="LAST-CHANGE" type="{http://automotive-his.de/schema/rif}STRING" minOccurs="0"/>
 *         &lt;element name="TYPE" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice minOccurs="0">
 *                   &lt;element name="DATATYPE-DEFINITION-BOOLEAN-REF" type="{http://automotive-his.de/schema/rif}REF"/>
 *                   &lt;element name="DATATYPE-DEFINITION-INTEGER-REF" type="{http://automotive-his.de/schema/rif}REF"/>
 *                   &lt;element name="DATATYPE-DEFINITION-REAL-REF" type="{http://automotive-his.de/schema/rif}REF"/>
 *                   &lt;element name="DATATYPE-DEFINITION-STRING-REF" type="{http://automotive-his.de/schema/rif}REF"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {  })
@XmlRootElement(name = "ATTRIBUTE-DEFINITION-SIMPLE")
public class ATTRIBUTEDEFINITIONSIMPLE {

    @XmlElement(name = "LONG-NAME")
    protected String longname;

    @XmlElement(name = "DESC")
    protected String desc;

    @XmlElement(name = "DEFAULT-VALUE")
    protected ATTRIBUTEDEFINITIONSIMPLE.DEFAULTVALUE defaultvalue;

    @XmlElement(name = "IDENTIFIER")
    protected String identifier;

    @XmlElement(name = "LAST-CHANGE")
    protected String lastchange;

    @XmlElement(name = "TYPE")
    protected ATTRIBUTEDEFINITIONSIMPLE.TYPE type;

    /**
     * Gets the value of the longname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLONGNAME() {
        return longname;
    }

    /**
     * Sets the value of the longname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLONGNAME(String value) {
        this.longname = value;
    }

    /**
     * Gets the value of the desc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESC() {
        return desc;
    }

    /**
     * Sets the value of the desc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESC(String value) {
        this.desc = value;
    }

    /**
     * Gets the value of the defaultvalue property.
     * 
     * @return
     *     possible object is
     *     {@link ATTRIBUTEDEFINITIONSIMPLE.DEFAULTVALUE }
     *     
     */
    public ATTRIBUTEDEFINITIONSIMPLE.DEFAULTVALUE getDEFAULTVALUE() {
        return defaultvalue;
    }

    /**
     * Sets the value of the defaultvalue property.
     * 
     * @param value
     *     allowed object is
     *     {@link ATTRIBUTEDEFINITIONSIMPLE.DEFAULTVALUE }
     *     
     */
    public void setDEFAULTVALUE(ATTRIBUTEDEFINITIONSIMPLE.DEFAULTVALUE value) {
        this.defaultvalue = value;
    }

    /**
     * Gets the value of the identifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIDENTIFIER() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIDENTIFIER(String value) {
        this.identifier = value;
    }

    /**
     * Gets the value of the lastchange property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLASTCHANGE() {
        return lastchange;
    }

    /**
     * Sets the value of the lastchange property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLASTCHANGE(String value) {
        this.lastchange = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link ATTRIBUTEDEFINITIONSIMPLE.TYPE }
     *     
     */
    public ATTRIBUTEDEFINITIONSIMPLE.TYPE getTYPE() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link ATTRIBUTEDEFINITIONSIMPLE.TYPE }
     *     
     */
    public void setTYPE(ATTRIBUTEDEFINITIONSIMPLE.TYPE value) {
        this.type = value;
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
     *       &lt;choice minOccurs="0">
     *         &lt;element ref="{http://automotive-his.de/schema/rif}ATTRIBUTE-VALUE-SIMPLE"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "attributevaluesimple" })
    public static class DEFAULTVALUE {

        @XmlElement(name = "ATTRIBUTE-VALUE-SIMPLE")
        protected ATTRIBUTEVALUESIMPLE attributevaluesimple;

        /**
         * Gets the value of the attributevaluesimple property.
         * 
         * @return
         *     possible object is
         *     {@link ATTRIBUTEVALUESIMPLE }
         *     
         */
        public ATTRIBUTEVALUESIMPLE getATTRIBUTEVALUESIMPLE() {
            return attributevaluesimple;
        }

        /**
         * Sets the value of the attributevaluesimple property.
         * 
         * @param value
         *     allowed object is
         *     {@link ATTRIBUTEVALUESIMPLE }
         *     
         */
        public void setATTRIBUTEVALUESIMPLE(ATTRIBUTEVALUESIMPLE value) {
            this.attributevaluesimple = value;
        }
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
     *       &lt;choice minOccurs="0">
     *         &lt;element name="DATATYPE-DEFINITION-BOOLEAN-REF" type="{http://automotive-his.de/schema/rif}REF"/>
     *         &lt;element name="DATATYPE-DEFINITION-INTEGER-REF" type="{http://automotive-his.de/schema/rif}REF"/>
     *         &lt;element name="DATATYPE-DEFINITION-REAL-REF" type="{http://automotive-his.de/schema/rif}REF"/>
     *         &lt;element name="DATATYPE-DEFINITION-STRING-REF" type="{http://automotive-his.de/schema/rif}REF"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "datatypedefinitionbooleanref", "datatypedefinitionintegerref", "datatypedefinitionrealref", "datatypedefinitionstringref" })
    public static class TYPE {

        @XmlElement(name = "DATATYPE-DEFINITION-BOOLEAN-REF")
        protected String datatypedefinitionbooleanref;

        @XmlElement(name = "DATATYPE-DEFINITION-INTEGER-REF")
        protected String datatypedefinitionintegerref;

        @XmlElement(name = "DATATYPE-DEFINITION-REAL-REF")
        protected String datatypedefinitionrealref;

        @XmlElement(name = "DATATYPE-DEFINITION-STRING-REF")
        protected String datatypedefinitionstringref;

        /**
         * Gets the value of the datatypedefinitionbooleanref property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDATATYPEDEFINITIONBOOLEANREF() {
            return datatypedefinitionbooleanref;
        }

        /**
         * Sets the value of the datatypedefinitionbooleanref property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDATATYPEDEFINITIONBOOLEANREF(String value) {
            this.datatypedefinitionbooleanref = value;
        }

        /**
         * Gets the value of the datatypedefinitionintegerref property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDATATYPEDEFINITIONINTEGERREF() {
            return datatypedefinitionintegerref;
        }

        /**
         * Sets the value of the datatypedefinitionintegerref property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDATATYPEDEFINITIONINTEGERREF(String value) {
            this.datatypedefinitionintegerref = value;
        }

        /**
         * Gets the value of the datatypedefinitionrealref property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDATATYPEDEFINITIONREALREF() {
            return datatypedefinitionrealref;
        }

        /**
         * Sets the value of the datatypedefinitionrealref property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDATATYPEDEFINITIONREALREF(String value) {
            this.datatypedefinitionrealref = value;
        }

        /**
         * Gets the value of the datatypedefinitionstringref property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDATATYPEDEFINITIONSTRINGREF() {
            return datatypedefinitionstringref;
        }

        /**
         * Sets the value of the datatypedefinitionstringref property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDATATYPEDEFINITIONSTRINGREF(String value) {
            this.datatypedefinitionstringref = value;
        }
    }
}
