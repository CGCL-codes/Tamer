package org.jaffa.tools.patternmetaengine.domain;

/**
 * Java content class for app-builder element declaration.
 *  <p>The following schema fragment specifies the expected content contained within this java content object.
 * <p>
 * <pre>
 * &lt;element name="app-builder">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="app-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="package-prefix" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="domain-object-path" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *           &lt;element name="module" type="{}module" maxOccurs="unbounded"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface AppBuilder extends javax.xml.bind.Element, org.jaffa.tools.patternmetaengine.domain.AppBuilderType {
}
