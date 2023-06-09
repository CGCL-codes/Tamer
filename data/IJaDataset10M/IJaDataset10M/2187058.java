package net.jini.discovery;

import com.sun.jini.discovery.Discovery;
import com.sun.jini.discovery.DiscoveryConstraints;
import com.sun.jini.discovery.UnicastResponse;
import com.sun.jini.discovery.UnicastSocketTimeout;
import com.sun.jini.discovery.internal.MultiIPDiscovery;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import net.jini.core.constraint.InvocationConstraints;
import net.jini.core.constraint.MethodConstraints;
import net.jini.core.constraint.RemoteMethodControl;
import net.jini.core.discovery.LookupLocator;
import net.jini.core.lookup.ServiceRegistrar;

/**
 * <code>LookupLocator</code> subclass which supports constraint operations
 * through the {@link RemoteMethodControl} interface. The constraints of a
 * <code>ConstrainableLookupLocator</code> instance control how it performs
 * unicast discovery, and apply only to its {@link LookupLocator#getRegistrar()
 * getRegistrar()} and {@link LookupLocator#getRegistrar(int) getRegistrar(int)}
 * methods. The constraints may also be used by other utilities, such as
 * {@link LookupLocatorDiscovery}, to determine how unicast discovery should be
 * performed on behalf of a given <code>ConstrainableLookupLocator</code>
 * instance. Untrusted <code>ConstrainableLookupLocator</code> instances can be
 * verified using the {@link ConstrainableLookupLocatorTrustVerifier} trust
 * verifier.
 * 
 * @author Sun Microsystems, Inc.
 * @since 2.0
 * 
 * @com.sun.jini.impl This class supports use of the following constraint types
 *                    to control unicast discovery behavior:
 *                    <ul>
 *                    <li>
 *                    {@link com.sun.jini.discovery.DiscoveryProtocolVersion}:
 *                    this constraint can be used to control which version of
 *                    the unicast discovery protocol is used.
 *                    <li> {@link com.sun.jini.discovery.UnicastSocketTimeout}:
 *                    this constraint can be used to control the read timeout
 *                    set on sockets over which unicast discovery is performed.
 *                    <li>
 *                    {@link net.jini.core.constraint.ConnectionRelativeTime}:
 *                    this constraint can be used to control the relative
 *                    connection timeout set on sockets over which unicast
 *                    discovery is performed.
 *                    <li>
 *                    {@link net.jini.core.constraint.ConnectionAbsoluteTime}:
 *                    this constraint can be used to control the absolute
 *                    connection timeout set on sockets over which unicast
 *                    discovery is performed.
 *                    </ul>
 *                    <p>
 *                    In addition, the
 *                    {@link com.sun.jini.discovery.MulticastMaxPacketSize} and
 *                    {@link com.sun.jini.discovery.MulticastTimeToLive}
 *                    constraint types are trivially supported, but do not have
 *                    any effect on unicast discovery operations. Constraints
 *                    other than those mentioned above are passed on to the
 *                    underlying implementations of versions 1 and 2 of the
 *                    discovery protocols.
 *                    <p>
 *                    An example of using constraints with
 *                    <code>ConstrainableLookupLocator</code> is: <blockquote>
 * 
 *                    <pre>
 * new ConstrainableLookupLocator(&quot;target_host&quot;, 4160, new {@link net.jini.constraint.BasicMethodConstraints}(
 *     new {@link InvocationConstraints}(
 *         DiscoveryProtocolVersion.TWO, new UnicastSocketTimeout(120000))));
 * </pre>
 * 
 *                    </blockquote> The resulting
 *                    <code>ConstrainableLookupLocator</code> instance would
 *                    (when used) perform unicast discovery to the host
 *                    <code>target_host</code> on port 4160 using discovery
 *                    protocol version 2, with a socket read timeout of 120000
 *                    milliseconds unless one was explicitly specified using the
 *                    {@link #getRegistrar(int)} method.
 */
public final class ConstrainableLookupLocator extends LookupLocator implements RemoteMethodControl {

    private static final long serialVersionUID = 7061417093114347317L;

    private static final int DEFAULT_TIMEOUT = 60 * 1000;

    private static final Method getRegistrarMethod;

    private static final Method getRegistrarTimeoutMethod;

    static {
        try {
            getRegistrarMethod = LookupLocator.class.getMethod("getRegistrar", new Class[0]);
            getRegistrarTimeoutMethod = LookupLocator.class.getMethod("getRegistrar", new Class[] { int.class });
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }

    /**
	 * The client-side method constraints for unicast discovery.
	 * 
	 * @serial
	 */
    private final MethodConstraints constraints;

    /**
	 * Constructs a new <code>ConstrainableLookupLocator</code> instance which
	 * can be used to perform unicast discovery to the host and port named by
	 * the given URL with the provided constraints applied. This constructor
	 * invokes its superclass {@link LookupLocator#LookupLocator(String)}
	 * constructor. Any exceptions thrown by the superclass constructor are
	 * rethrown.
	 * <p>
	 * The <code>url</code> must be a valid URL of scheme <code>"jini"</code> as
	 * described in <code>LookupLocator(String)</code>. A <code>null
	 * constraints</code> value is interpreted as mapping both
	 * <code>getRegistrar</code> methods to empty constraints.
	 * 
	 * @param url
	 *            the URL to use
	 * @param constraints
	 *            the constraints to apply to unicast discovery, or
	 *            <code>null</code>
	 * @throws MalformedURLException
	 *             if <code>url</code> cannot be parsed
	 * @throws NullPointerException
	 *             if <code>url</code> is <code>null</code>
	 */
    public ConstrainableLookupLocator(String url, MethodConstraints constraints) throws MalformedURLException {
        super(url);
        this.constraints = constraints;
    }

    /**
	 * Constructs a new <code>ConstrainableLookupLocator</code> instance which
	 * can be used to perform unicast discovery to the given host and port with
	 * the provided constraints applied. This constructor invokes its superclass
	 * {@link LookupLocator#LookupLocator(String, int)} constructor. Any
	 * exceptions thrown by the superclass constructor are rethrown.
	 * 
	 * <p>
	 * A <code>null constraints</code> value is interpreted as mapping both
	 * <code>getRegistrar</code> methods to empty constraints. The
	 * <code>host</code> and <code>port</code> must satisfy the requirements of
	 * the <code>LookupLocator(String, int)</code> constructor.
	 * 
	 * @param host
	 *            the name of the host to contact
	 * @param port
	 *            the number of the port to connect to
	 * @param constraints
	 *            the constraints to apply to unicast discovery, or
	 *            <code>null</code>
	 * @throws NullPointerException
	 *             if <code>host</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the port and host do not meet the requirements of
	 *             <code>LookupLocator(String, int)</code>.
	 */
    public ConstrainableLookupLocator(String host, int port, MethodConstraints constraints) {
        super(host, port);
        this.constraints = constraints;
    }

    /**
	 * Performs unicast discovery as specified by
	 * <code>LookupLocator.getRegistrar()</code> with the following differences.
	 * <ul>
	 * <li> It applies the supplied constraints (if any) for this method. <li>
	 * It does not invoke the <code>getRegistrar(int)</code> method. <li> If no
	 * timeout constraints are specified, this method assumes a default timeout
	 * of 60 seconds. A non default timeout can only be specified through the
	 * supplied constraints, the <code>net.jini.discovery.timeout</code> system
	 * property is ignored.
	 * </ul>
	 * <code>ConstrainableLookupLocator</code> implements this method to use the
	 * values of the <code>host</code> and <code>port</code> field in
	 * determining the host and port to connect to.
	 * 
	 * @throws net.jini.io.UnsupportedConstraintException
	 *             if the discovery-related constraints contain conflicts, or
	 *             otherwise cannot be processed
	 */
    public ServiceRegistrar getRegistrar() throws IOException, ClassNotFoundException {
        return getRegistrar((constraints != null) ? constraints.getConstraints(getRegistrarMethod) : InvocationConstraints.EMPTY);
    }

    /**
	 * Performs unicast discovery as specified by
	 * <code>LookupLocator.getRegistrar(int)</code>, additionally applying the
	 * supplied discovery constraints. The <code>timeout</code> is considered a
	 * requirement with respect to other constraints specified for this
	 * instance.
	 * 
	 * @throws net.jini.io.UnsupportedConstraintException
	 *             if the discovery-related constraints contain conflicts, or
	 *             otherwise cannot be processed
	 */
    public ServiceRegistrar getRegistrar(int timeout) throws IOException, ClassNotFoundException {
        InvocationConstraints ic = (constraints != null) ? constraints.getConstraints(getRegistrarTimeoutMethod) : InvocationConstraints.EMPTY;
        Collection reqs = new ArrayList(ic.requirements());
        reqs.add(new UnicastSocketTimeout(timeout));
        return getRegistrar(new InvocationConstraints(reqs, ic.preferences()));
    }

    /**
	 * Returns a string representation of this object.
	 * 
	 * @return the string representation
	 */
    public String toString() {
        return "ConstrainableLookupLocator[[" + super.toString() + "], [" + constraints + "]]";
    }

    public RemoteMethodControl setConstraints(MethodConstraints constraints) {
        return new ConstrainableLookupLocator(host, port, constraints);
    }

    public MethodConstraints getConstraints() {
        return constraints;
    }

    private ServiceRegistrar getRegistrar(InvocationConstraints constraints) throws IOException, ClassNotFoundException {
        UnicastResponse resp = new MultiIPDiscovery() {

            protected UnicastResponse performDiscovery(Discovery disco, DiscoveryConstraints dc, Socket s) throws IOException, ClassNotFoundException {
                return disco.doUnicastDiscovery(s, dc.getUnfulfilledConstraints(), null, null, null);
            }
        }.getResponse(host, port, constraints);
        return resp.getRegistrar();
    }
}
