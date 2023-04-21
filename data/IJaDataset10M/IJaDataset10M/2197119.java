package org.apache.catalina.deploy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

/**
 * Representation of a handler reference for a web service, as
 * represented in a <code>&lt;handler&gt;</code> element in the
 * deployment descriptor.
 *
 * @author Fabien Carrion
 */
public class ContextHandler extends ResourceBase implements Serializable {

    /**
     * The Handler reference class.
     */
    private String handlerclass = null;

    public String getHandlerclass() {
        return (this.handlerclass);
    }

    public void setHandlerclass(String handlerclass) {
        this.handlerclass = handlerclass;
    }

    /**
     * A list of QName specifying the SOAP Headers the handler will work on. 
     * -namespace and locapart values must be found inside the WSDL.
     *
     * A service-qname is composed by a namespaceURI and a localpart.
     *
     * soapHeader[0] : namespaceURI
     * soapHeader[1] : localpart
     */
    private HashMap soapHeaders = new HashMap();

    public Iterator getLocalparts() {
        return soapHeaders.keySet().iterator();
    }

    public String getNamespaceuri(String localpart) {
        return (String) soapHeaders.get(localpart);
    }

    public void addSoapHeaders(String localpart, String namespaceuri) {
        soapHeaders.put(localpart, namespaceuri);
    }

    /**
     * Set a configured property.
     */
    public void setProperty(String name, String value) {
        this.setProperty(name, (Object) value);
    }

    /**
     * The soapRole.
     */
    private ArrayList<String> soapRoles = new ArrayList();

    public String getSoapRole(int i) {
        return this.soapRoles.get(i);
    }

    public int getSoapRolesSize() {
        return this.soapRoles.size();
    }

    public void addSoapRole(String soapRole) {
        this.soapRoles.add(soapRole);
    }

    /**
     * The portName.
     */
    private ArrayList<String> portNames = new ArrayList();

    public String getPortName(int i) {
        return this.portNames.get(i);
    }

    public int getPortNamesSize() {
        return this.portNames.size();
    }

    public void addPortName(String portName) {
        this.portNames.add(portName);
    }

    /**
     * Return a String representation of this object.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("ContextHandler[");
        sb.append("name=");
        sb.append(getName());
        if (handlerclass != null) {
            sb.append(", class=");
            sb.append(handlerclass);
        }
        if (this.soapHeaders != null) {
            sb.append(", soap-headers=");
            sb.append(this.soapHeaders);
        }
        if (this.getSoapRolesSize() > 0) {
            sb.append(", soap-roles=");
            sb.append(soapRoles);
        }
        if (this.getPortNamesSize() > 0) {
            sb.append(", port-name=");
            sb.append(portNames);
        }
        if (this.listProperties() != null) {
            sb.append(", init-param=");
            sb.append(this.listProperties());
        }
        sb.append("]");
        return (sb.toString());
    }
}
