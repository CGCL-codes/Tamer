package org.dbe.composer.wfengine.wsio;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;

/**
 * Partner endpoint reference data.
 */
public interface IWebServiceEndpointReference extends Serializable {

    /**
     * Returns the list of policies if any were specified.
     */
    public List getPolicies();

    /**
     * Returns an Iterator of extensibility elements defined here.
     * @return Iterator the extensibility elements.
     */
    public Iterator getExtensibilityElements();

    /**
     * Returns the name of the WSDL file which contains the definition of the
     * service element. This value may be null.
     */
    public QName getServiceName();

    /**
     * Returns the port name of the service element. This value may be null.
     */
    public String getServicePort();

    /**
     * Returns the username to set on the call object or null if not set
     */
    public String getUsername();

    /**
     * Returns the password to set on the call object or null if not set
     */
    public String getPassword();

    /**
     * Returns the map of properties if any were specified.
     */
    public Map getProperties();

    /**
     * Returns the address for the endpoint.
     */
    public String getAddress();

    /**
     * Returns the port type for the endpoint.
     */
    public QName getPortType();
}
