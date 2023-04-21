package net.gpslite.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * 				Abstract GIP type.
 * 				Implementors should not inherit from this type,
 * 				but use either AbstractSynchronousGIP or
 * 				AbstractAsynchronousGIP to mark the type of the GIP.
 * 			
 * 
 * <p>Java class for AbstractGip complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AbstractGip">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractGip")
@XmlSeeAlso({ AbstractAsynchronousGip.class, AbstractSynchronousGip.class })
public abstract class AbstractGip {
}
