package org.apache.harmony.jndi.provider.rmi;

import java.util.Hashtable;
import javax.naming.CompositeName;
import javax.naming.NamingException;
import javax.naming.spi.ResolveResult;
import org.apache.harmony.jndi.internal.nls.Messages;
import org.apache.harmony.jndi.provider.GenericURLContext;
import org.apache.harmony.jndi.provider.rmi.registry.RegistryContext;

/**
 * RMI URL context implementation.
 */
public class rmiURLContext extends GenericURLContext {

    /**
     * Creates instance of this context with empty environment.
     */
    public rmiURLContext() {
        super(null);
    }

    /**
     * Creates instance of this context with specified environment.
     * 
     * @param environment
     *            Environment to copy.
     */
    public rmiURLContext(Hashtable<?, ?> environment) {
        super(environment);
    }

    /**
     * Determines the proper {@link RegistryContext} from the specified URL and
     * returns the {@link ResolveResult} object with that context as resolved
     * object and the rest of the URL as remaining name.
     * 
     * @param url
     *            URL.
     * 
     * @param environment
     *            Environment.
     * 
     * @return {@link ResolveResult} object with resolved context as resolved
     *         object the rest of the URL as remaining name.
     * 
     * @throws NamingException
     *             If some naming error occurs.
     */
    @Override
    protected ResolveResult getRootURLContext(String url, Hashtable<?, ?> environment) throws NamingException {
        if (!url.startsWith(RegistryContext.RMI_URL_PREFIX)) {
            throw new IllegalArgumentException(Messages.getString("jndi.74", url));
        }
        int length = url.length();
        int start = RegistryContext.RMI_URL_PREFIX.length();
        String hostName = null;
        int port = 0;
        if ((start < length) && (url.charAt(start) == '/')) {
            start++;
            if ((start < length) && (url.charAt(start) == '/')) {
                start++;
                int end = url.indexOf('/', start);
                if (end < 0) {
                    end = length;
                }
                int hostEnd = url.indexOf(':', start);
                if ((hostEnd < 0) || (hostEnd > end)) {
                    hostEnd = end;
                }
                if (start < hostEnd) {
                    hostName = url.substring(start, hostEnd);
                }
                int portStart = hostEnd + 1;
                if (portStart < end) {
                    try {
                        port = Integer.parseInt(url.substring(portStart, end));
                    } catch (NumberFormatException e) {
                        throw (IllegalArgumentException) new IllegalArgumentException(Messages.getString("jndi.75", url)).initCause(e);
                    }
                }
                start = ((end < length) ? (end + 1) : length);
            }
        }
        CompositeName name = new CompositeName();
        if (start < length) {
            name.add(url.substring(start));
        }
        return new ResolveResult(new RegistryContext(hostName, port, environment), name);
    }
}
