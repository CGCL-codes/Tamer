package org.slaatsoi.business.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for BusinessParameter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BusinessParameter">
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
@XmlType(name = "BusinessParameter")
@XmlSeeAlso({ ServiceDescription.class, ProductOfferingPrices.class, ContactPoint.class, Support.class, UpdateProcess.class, SupportProcedures.class, BackupRecoveryMechanism.class, Monitoring.class, Reporting.class, TerminationClauses.class })
public class BusinessParameter implements Serializable {
}
