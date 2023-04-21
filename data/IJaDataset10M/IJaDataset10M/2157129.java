package org.soda.dpws.addressing;

import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.ws.policy.Policy;

/**
 * A WS-Addressing endpoint reference which can be considered as a set of
 * information on a network communication endpoint including its physical or
 * logical address.
 */
public interface EndpointReference {

    /**
   * Method accessing the reference parameters.
   * 
   * @return a map of reference parameters.
   */
    public Map<QName, Object> getReferenceParameters();

    /**
   * Method accessing the address.
   * 
   * @return a string <code>URI</code>.
   */
    public String getAddress();

    /**
   * Method accessing the reference parameter by its name.
   * 
   * @param key
   * 
   * @return an object representing the reference parameter.
   */
    public Object getReferenceParameter(QName key);

    /**
   * Method accessing the list of policies.
   * 
   * @return the list of policies.
   */
    public List<Policy> getPolicies();
}
