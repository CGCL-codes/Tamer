package com.sun.jini.mercury;

import com.sun.jini.proxy.ConstrainableProxyUtil;
import com.sun.jini.proxy.ThrowThis;
import net.jini.core.constraint.MethodConstraints;
import net.jini.core.constraint.RemoteMethodControl;
import net.jini.id.ReferentUuid;
import net.jini.id.ReferentUuids;
import net.jini.id.Uuid;
import net.jini.security.proxytrust.ProxyTrustIterator;
import net.jini.security.proxytrust.SingletonProxyTrustIterator;
import net.jini.security.TrustVerifier;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import javax.security.auth.Subject;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.RemoteEvent;
import net.jini.core.event.UnknownEventException;

/**
 * The <code>ListenerProxy</code> class implements the
 * <code>RemoteEventListener</code> interface. Instances of this class are
 * provided as the event "forwarding" target to clients of the mailbox service.
 * 
 * @author Sun Microsystems, Inc.
 * 
 * @since 1.1
 */
class ListenerProxy implements RemoteEventListener, Serializable, ReferentUuid {

    private static final long serialVersionUID = 2L;

    /**
	 * The reference to the event mailbox service implementation
	 * 
	 * @serial
	 */
    final MailboxBackEnd server;

    /**
	 * The proxy's <code>Uuid</code>
	 * 
	 * @serial
	 */
    final Uuid registrationID;

    /**
	 * Creates a mailbox listener proxy, returning an instance that implements
	 * RemoteMethodControl if the server does too.
	 * 
	 * @param id
	 *            the ID of the proxy
	 * @param server
	 *            the server's listener proxy
	 */
    static ListenerProxy create(Uuid id, MailboxBackEnd server) {
        if (server instanceof RemoteMethodControl) {
            return new ConstrainableListenerProxy(server, id, null);
        } else {
            return new ListenerProxy(server, id);
        }
    }

    /** Simple constructor */
    private ListenerProxy(MailboxBackEnd ref, Uuid regID) {
        if (ref == null || regID == null) throw new IllegalArgumentException("Cannot accept null arguments");
        server = ref;
        registrationID = regID;
    }

    public void notify(RemoteEvent theEvent) throws UnknownEventException, RemoteException {
        try {
            server.notify(registrationID, theEvent);
        } catch (ThrowThis tt) {
            tt.throwRemoteException();
        }
    }

    public final Uuid getReferentUuid() {
        return registrationID;
    }

    /** Proxies for servers with the same proxyID have the same hash code. */
    public int hashCode() {
        return registrationID.hashCode();
    }

    /**
	 * Proxies for servers with the same <code>proxyID</code> are considered
	 * equal.
	 */
    public boolean equals(Object o) {
        return ReferentUuids.compare(this, o);
    }

    /**
	 * When an instance of this class is deserialized, this method is
	 * automatically invoked. This implementation of this method validates the
	 * state of the deserialized instance.
	 * 
	 * @throws <code>InvalidObjectException</code> if the state of the
	 *         deserialized instance of this class is found to be invalid.
	 */
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        if (server == null) {
            throw new InvalidObjectException("ListenerProxy.readObject " + "failure - server " + "field is null");
        }
        if (registrationID == null) {
            throw new InvalidObjectException("ListenerProxy.readObject " + "failure - registrationID " + "field is null");
        }
    }

    /**
	 * During deserialization of an instance of this class, if it is found that
	 * the stream contains no data, this method is automatically invoked.
	 * Because it is expected that the stream should always contain data, this
	 * implementation of this method simply declares that something must be
	 * wrong.
	 * 
	 * @throws <code>InvalidObjectException</code> to indicate that there was no
	 *         data in the stream during deserialization of an instance of this
	 *         class; declaring that something is wrong.
	 */
    private void readObjectNoData() throws ObjectStreamException {
        throw new InvalidObjectException("no data found when attempting to " + "deserialize ListenerProxy instance");
    }

    /** A subclass of ListenerProxy that implements RemoteMethodControl. */
    static final class ConstrainableListenerProxy extends ListenerProxy implements RemoteMethodControl {

        private static final long serialVersionUID = 2L;

        private static final Method[] methodMap1 = { ProxyUtil.getMethod(RemoteEventListener.class, "notify", new Class[] { RemoteEvent.class }), ProxyUtil.getMethod(MailboxBackEnd.class, "notify", new Class[] { Uuid.class, RemoteEvent.class }) };

        /**
		 * The client constraints placed on this proxy or <code>null</code>.
		 * 
		 * @serial
		 */
        private MethodConstraints methodConstraints;

        /** Creates an instance of this class. */
        private ConstrainableListenerProxy(MailboxBackEnd server, Uuid id, MethodConstraints methodConstraints) {
            super(constrainServer(server, methodConstraints), id);
            this.methodConstraints = methodConstraints;
        }

        /**
		 * Returns a copy of the server proxy with the specified client
		 * constraints and methods mapping.
		 */
        private static MailboxBackEnd constrainServer(MailboxBackEnd server, MethodConstraints methodConstraints) {
            return (MailboxBackEnd) ((RemoteMethodControl) server).setConstraints(ConstrainableProxyUtil.translateConstraints(methodConstraints, methodMap1));
        }

        /** {@inheritDoc} */
        public RemoteMethodControl setConstraints(MethodConstraints constraints) {
            return new ConstrainableListenerProxy(server, registrationID, constraints);
        }

        /** {@inheritDoc} */
        public MethodConstraints getConstraints() {
            return methodConstraints;
        }

        /**
		 * Returns a proxy trust iterator that is used in
		 * <code>ProxyTrustVerifier</code> to retrieve this object's trust
		 * verifier.
		 */
        private ProxyTrustIterator getProxyTrustIterator() {
            return new SingletonProxyTrustIterator(server);
        }

        /**
		 * Verifies that the registrationID and server fields are not null, that
		 * server implements RemoteMethodControl, and that the server proxy has
		 * the appropriate method constraints.
		 * 
		 * @throws InvalidObjectException
		 *             if registrationID or mailbox is null, if server does not
		 *             implement RemoteMethodControl, or if server has the wrong
		 *             constraints
		 */
        private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
            s.defaultReadObject();
            ConstrainableProxyUtil.verifyConsistentConstraints(methodConstraints, server, methodMap1);
        }
    }
}
