package org.opennms.netmgt.capsd;

import java.net.InetAddress;
import java.util.Map;

/**
 * <p>
 * This class provides a basic implementation for most of the interface methods
 * of the <code>Plugin</code> class. Since most plugins do not do any special
 * initialization, and only require that the interface is an
 * <code>InetAddress</code> object this class provides eveything but the
 * <code>poll<code> interface.
 *
 * @author <A HREF="mike@opennms.org">Mike</A>
 * @author <A HREF="weave@oculan.com">Weave</A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS</A>
 *
 */
public abstract class AbstractPlugin implements Plugin {

    /**
     * Returns the name of the protocol that this plugin checks on the target
     * system for support.
     * 
     * @return The protocol name for this plugin.
     */
    public abstract String getProtocolName();

    /**
     * Returns true if the protocol defined by this plugin is supported. If the
     * protocol is not supported then a false value is returned to the caller.
     * 
     * @param address
     *            The address to check for support.
     * 
     * @return True if the protocol is supported by the address.
     */
    public abstract boolean isProtocolSupported(InetAddress address);

    /**
     * Returns true if the protocol defined by this plugin is supported. If the
     * protocol is not supported then a false value is returned to the caller.
     * The qualifier map passed to the method is used by the plugin to return
     * additional information by key-name. These key-value pairs can be added to
     * service events if needed.
     * 
     * @param address
     *            The address to check for support.
     * @param qualifiers
     *            The map where qualification are set by the plugin.
     * 
     * @return True if the protocol is supported by the address.
     */
    public abstract boolean isProtocolSupported(InetAddress address, Map<String, Object> qualifiers);
}
