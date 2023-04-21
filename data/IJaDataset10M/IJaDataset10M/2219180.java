package com.ihafer.switchboard.jaxb;

/**
 * Java content class for switchboard element declaration.
 *  <p>The following schema fragment specifies the expected content contained within this java content object.
 * <p>
 * <pre>
 * &lt;element name="switchboard">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="mapping" type="{http://www.ihafer.com/xml/switchboard}mapping" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface Switchboard extends javax.xml.bind.Element, com.ihafer.switchboard.jaxb.SwitchboardType {
}
