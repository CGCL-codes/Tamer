package com.webdeninteractive.sbie;

import java.io.OutputStream;
import java.io.InputStream;
import java.util.Map;
import com.webdeninteractive.sbie.config.SystemConfig;

/** 
 * Interface defining components to perform individual tasks.
 * <p>
 * Services communicate with remove BIE instances, and take actions based
 * on the returned messages.
 * <p>
 * Typical tasks for Services include uploading data, synching configuration
 * information, and downloading system component upgrades.
 *
 * @author gfast
 * @version $Id: Service.java,v 1.1.1.1 2003/05/20 16:56:49 gdf Exp $ 
 */
public interface Service {

    /** Associate a configuration object with this Service.
     * @param config congifuration data from the parent Client.
     */
    public void setConfiguration(SystemConfig config);

    /** Return the configuration object associated with this Service. */
    public SystemConfig getConfiguration();

    /** Assign a ProtocolHandler to this Service.
     *  The Client which created this Service will define a handler
     *  for it.
     * @param handler ProtocolHandler for sending messages.
     */
    public void setProtocolHandler(ProtocolHandler handler);

    /** @return the ProtocolHandler assigned to this Service. */
    public ProtocolHandler getProtocolHandler();

    /** Set a reference to the Client which created this Service.
     *  A Service may need to query its parent for configuration or other
     *  information in order to do its work.
     * @param parent Client which created this Service.
     */
    public void setParentClient(Client parent);

    /** @return Client which created this Service. */
    public Client getParentClient();

    /** Have this service perform its task. */
    public void runService();

    /** Get a configuration parameter.
     *  The Client creating this Service will assign parameters as
     *  defined in the configuration.
     * @param param Name of the parameter to retrieve.
     * @return Value of the given parameter, or null if no such exists.
     */
    public String getParameter(String param);

    /** Set a runtime parameter.
     *  Setting a parameter this way does not change the stored
     *  configuration of the system.
     * @param param Name of the parameter to set.
     * @param value Value to set for the given parameter.
     */
    public void setParameter(String param, String value);

    /** Delete a stored configuration parameter.  
     *  Subsequent calls to <code>getParameter</code> for this name
     *  will return null.  Removing a parameter this way does not
     *  change the stored configuration of the system.
     * @param param Name of the parameter to delete.
     */
    public String removeParameter(String param);

    /** Override the entire set of parameters for this Service.
     * @param params Map of parameter names to String values.
     */
    public void setParameters(Map params);
}
