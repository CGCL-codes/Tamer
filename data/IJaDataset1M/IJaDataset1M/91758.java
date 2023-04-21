package com.softaria.spkiller.dependencies;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.softaria.spkiller.dependencies package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _Graph_QNAME = new QName("http://www.example.org/dependencyGraph", "graph");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.softaria.spkiller.dependencies
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Graph }
     * 
     */
    public Graph createGraph() {
        return new Graph();
    }

    /**
     * Create an instance of {@link Layer }
     * 
     */
    public Layer createLayer() {
        return new Layer();
    }

    /**
     * Create an instance of {@link ClassElement }
     * 
     */
    public ClassElement createClassElement() {
        return new ClassElement();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Graph }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/dependencyGraph", name = "graph")
    public JAXBElement<Graph> createGraph(Graph value) {
        return new JAXBElement<Graph>(_Graph_QNAME, Graph.class, null, value);
    }
}
