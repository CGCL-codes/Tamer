package com.neptuny.xgrapher.gen.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for Graph complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Graph">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.neptuny.it/XGrapher}graphicRepresentation" minOccurs="0"/>
 *         &lt;element ref="{http://www.neptuny.it/XGrapher}topology" minOccurs="0"/>
 *         &lt;element ref="{http://www.neptuny.it/XGrapher}node" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.neptuny.it/XGrapher}edge" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="directed" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Graph", propOrder = { "graphicRepresentation", "topology", "node", "edge" })
public class Graph {

    protected GraphicRepresentation graphicRepresentation;

    protected Topology topology;

    @XmlElement(required = true)
    protected List<Node> node;

    protected List<Edge> edge;

    @XmlAttribute
    protected Boolean directed;

    /**
     * Gets the value of the graphicRepresentation property.
     * 
     * @return
     *     possible object is
     *     {@link GraphicRepresentation }
     *     
     */
    public GraphicRepresentation getGraphicRepresentation() {
        return graphicRepresentation;
    }

    /**
     * Sets the value of the graphicRepresentation property.
     * 
     * @param value
     *     allowed object is
     *     {@link GraphicRepresentation }
     *     
     */
    public void setGraphicRepresentation(GraphicRepresentation value) {
        this.graphicRepresentation = value;
    }

    /**
     * Gets the value of the topology property.
     * 
     * @return
     *     possible object is
     *     {@link Topology }
     *     
     */
    public Topology getTopology() {
        return topology;
    }

    /**
     * Sets the value of the topology property.
     * 
     * @param value
     *     allowed object is
     *     {@link Topology }
     *     
     */
    public void setTopology(Topology value) {
        this.topology = value;
    }

    /**
     * Gets the value of the node property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the node property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Node }
     * 
     * 
     */
    public List<Node> getNode() {
        if (node == null) {
            node = new ArrayList<Node>();
        }
        return this.node;
    }

    /**
     * Gets the value of the edge property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the edge property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEdge().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Edge }
     * 
     * 
     */
    public List<Edge> getEdge() {
        if (edge == null) {
            edge = new ArrayList<Edge>();
        }
        return this.edge;
    }

    /**
     * Gets the value of the directed property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isDirected() {
        if (directed == null) {
            return false;
        } else {
            return directed;
        }
    }

    /**
     * Sets the value of the directed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDirected(Boolean value) {
        this.directed = value;
    }
}
