package edu.upc.lsi.kemlg.aws.webservices.AgentEndpointProxy.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.3.1-hudson-417-SNAPSHOT
 * Generated source version: 2.1.3.1
 * 
 */
@XmlRootElement(name = "MalformedURLException", namespace = "http://AgentEndpointProxy.webservices.aws.kemlg.lsi.upc.edu/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MalformedURLException", namespace = "http://AgentEndpointProxy.webservices.aws.kemlg.lsi.upc.edu/")
public class MalformedURLExceptionBean {

    private String message;

    /**
     * 
     * @return
     *     returns String
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * 
     * @param message
     *     the value for the message property
     */
    public void setMessage(String message) {
        this.message = message;
    }
}