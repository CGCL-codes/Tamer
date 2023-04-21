package de.iritgo.aktera.webservices.webservices;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.iritgo.aktera.webservices.webservices package. 
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

    /**
	 * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.iritgo.aktera.webservices.webservices
	 * 
	 */
    public ObjectFactory() {
    }

    /**
	 * Create an instance of {@link EchoRequest }
	 * 
	 */
    public EchoRequest createEchoRequest() {
        return new EchoRequest();
    }

    /**
	 * Create an instance of {@link EchoResponse }
	 * 
	 */
    public EchoResponse createEchoResponse() {
        return new EchoResponse();
    }

    /**
	 * Create an instance of {@link PingRequest }
	 * 
	 */
    public PingRequest createPingRequest() {
        return new PingRequest();
    }

    /**
	 * Create an instance of {@link PongResponse }
	 * 
	 */
    public PongResponse createPongResponse() {
        return new PongResponse();
    }
}
