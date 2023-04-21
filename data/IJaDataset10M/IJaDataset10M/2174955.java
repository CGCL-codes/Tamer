package de.beas.explicanto.model.jaxb;

/**
 * Java content class for region element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Projects/Beck/explicanto32/VidyaServer/bin/model.xsd line 113)
 * <p>
 * <pre>
 * &lt;element name="region">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element ref="{http://www.explicanto.de/DesignModel}position"/>
 *           &lt;element ref="{http://www.explicanto.de/DesignModel}left"/>
 *           &lt;element ref="{http://www.explicanto.de/DesignModel}top"/>
 *           &lt;element ref="{http://www.explicanto.de/DesignModel}width"/>
 *           &lt;element ref="{http://www.explicanto.de/DesignModel}height"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface Region extends javax.xml.bind.Element, de.beas.explicanto.model.jaxb.RegionType {
}
