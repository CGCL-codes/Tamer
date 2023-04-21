package cz.cuni.mff.ksi.jinfer.projecttype.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for Tjinfer complex type.
 * 
 * 
 * @author sviro
 */
@SuppressWarnings("PMD")
@XmlRootElement(name = "jinferinput")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Tjinfer", propOrder = { "xml", "schemas", "queries", "fds" })
public class Tjinfer {

    @XmlElement(required = true)
    protected Txml xml;

    @XmlElement(required = true)
    protected Tschemas schemas;

    @XmlElement(required = true)
    protected Tqueries queries;

    @XmlElement(required = false)
    protected Tfds fds;

    /**
     * Gets the value of the xml property.
     * 
     * @return
     *     possible object is
     *     {@link Txml }
     *     
     */
    public Txml getXml() {
        return xml;
    }

    /**
     * Sets the value of the xml property.
     * 
     * @param value
     *     allowed object is
     *     {@link Txml }
     *     
     */
    public void setXml(final Txml value) {
        this.xml = value;
    }

    /**
     * Gets the value of the schemas property.
     * 
     * @return
     *     possible object is
     *     {@link Tschemas }
     *     
     */
    public Tschemas getSchemas() {
        return schemas;
    }

    /**
     * Sets the value of the schemas property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tschemas }
     *     
     */
    public void setSchemas(final Tschemas value) {
        this.schemas = value;
    }

    /**
     * Gets the value of the queries property.
     * 
     * @return
     *     possible object is
     *     {@link Tqueries }
     *     
     */
    public Tqueries getQueries() {
        return queries;
    }

    /**
     * Sets the value of the queries property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tqueries }
     *     
     */
    public void setQueries(final Tqueries value) {
        this.queries = value;
    }

    /**
     * Gets the value of the fds property.
     * 
     * @return
     *     possible object is
     *     {@link Tfds }
     *     
     */
    public Tfds getFds() {
        return fds;
    }

    /**
     * Sets the value of the fds property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tfds }
     *     
     */
    public void setFds(final Tfds fds) {
        this.fds = fds;
    }
}
