package de.beas.explicanto.client.sec.jaxb;

/**
 * Java content class for Screenplay element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Projects/Beck/Explicanto/ExplicantoClient/autogen/explicantoScreenplay.xsd line 21)
 * <p>
 * <pre>
 * &lt;element name="Screenplay">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="objective" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="targetGroup" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="story" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="language" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="mediaResource" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="animationResource" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="characters" type="{http://www.bea-services.de/explicanto}Character" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="cast" type="{http://www.bea-services.de/explicanto}Member" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="locations" type="{http://www.bea-services.de/explicanto}Location" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="timeOfDays" type="{http://www.bea-services.de/explicanto}TimeOfDay" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="scenes" type="{http://www.bea-services.de/explicanto}Scene" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface Screenplay extends de.beas.explicanto.client.sec.jaxb.ScreenplayType {
}
