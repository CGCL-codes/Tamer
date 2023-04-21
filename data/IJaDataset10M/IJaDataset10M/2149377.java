package org.icefaces.application.showcase.view.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="nodeRef" use="required" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "navigation-default")
public class NavigationDefault {

    @XmlAttribute(name = "nodeRef", required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Node navigationNode;

    /**
     * Gets the value of the navigationNode property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Node getNavigationNode() {
        return navigationNode;
    }

    /**
     * Sets the value of the navigationNode property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setNavigationNode(Node value) {
        this.navigationNode = value;
    }
}
