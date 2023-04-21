package uk.ac.ebi.pride.jaxb.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * The GelFreeIdentification element (complex) is a concrete implementation of the abstract IdentificationType element, used to hold details of identifications that have arisen from techniques other than gel eletrophoresis, for example LC-MS/MS.
 * 
 * <p>Java class for gelFreeIdentificationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="gelFreeIdentificationType">
 *   &lt;complexContent>
 *     &lt;extension base="{}identificationType">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "gelFreeIdentificationType")
public class GelFreeIdentification extends Identification implements Serializable {

    private static final long serialVersionUID = 100L;
}
