package org.gello.model.HL7RIM.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.gello.model.HL7RIM.generated.ED;
import org.gello.model.HL7RIM.generated.Thumbnail;

/**
 *
 *                      A thumbnail is an abbreviated rendition of the full
 *                      data. A thumbnail requires significantly fewer
 *                      resources than the full data, while still maintaining
 *                      some distinctive similarity with the full data. A
 *                      thumbnail is typically used with by-reference
 *                      encapsulated data. It allows a user to select data
 *                      more efficiently before actually downloading through
 *                      the reference.
 *
 *
 * <p>Java class for thumbnail complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="thumbnail">
 *   &lt;complexContent>
 *     &lt;restriction base="{}ED">
 *       &lt;sequence>
 *         &lt;element name="reference" type="{}TEL" minOccurs="0"/>
 *         &lt;element name="thumbnail" type="{}thumbnail" maxOccurs="0" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "thumbnail")
public class Thumbnail extends ED {
}
