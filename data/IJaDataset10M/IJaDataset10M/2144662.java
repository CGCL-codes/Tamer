package de.beas.explicanto.model.jaxb;

/**
 * Java content class for document element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Projects/Beck/explicanto32/VidyaServer/bin/model.xsd line 4)
 * <p>
 * <pre>
 * &lt;element name="document">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element ref="{http://www.explicanto.de/DesignModel}template-id"/>
 *           &lt;element ref="{http://www.explicanto.de/DesignModel}elements"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface Document extends javax.xml.bind.Element, de.beas.explicanto.model.jaxb.DocumentType {
}
