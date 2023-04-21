package de.beas.explicanto.model.jaxb;

/**
 * Java content class for value element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Projects/Beck/explicanto32/VidyaServer/bin/model.xsd line 82)
 * <p>
 * <pre>
 * &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * </pre>
 * 
 */
public interface Value extends javax.xml.bind.Element {

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getValue();

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setValue(java.lang.String value);
}
