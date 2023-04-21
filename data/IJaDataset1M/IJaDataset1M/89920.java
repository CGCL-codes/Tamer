package org.gello.model.HL7RIM.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.gello.model.HL7RIM.generated.CO;
import org.gello.model.HL7RIM.generated.CV;

/**
 *
 *             Coded data, where the domain from which the codeset comes
 *             is ordered. The Coded Ordinal data type adds semantics
 *             related to ordering so that models that make use of such
 *             domains may introduce model elements that involve statements
 *             about the order of the terms in a domain.
 *
 *
 * <p>Java class for CO complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CO">
 *   &lt;complexContent>
 *     &lt;extension base="{}CV">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CO")
public class CO extends CV {
}
