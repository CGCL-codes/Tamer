package com.sun.jini.mercury;

import com.sun.jini.admin.DestroyAdmin;
import net.jini.core.constraint.MethodConstraints;
import net.jini.core.constraint.RemoteMethodControl;
import net.jini.id.ReferentUuid;
import net.jini.id.ReferentUuids;
import net.jini.id.Uuid;
import net.jini.security.proxytrust.ProxyTrustIterator;
import net.jini.security.proxytrust.SingletonProxyTrustIterator;
import java.lang.reflect.Method;
import java.io.InvalidObjectException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.rmi.RemoteException;
import javax.security.auth.Subject;
import net.jini.admin.JoinAdmin;
import net.jini.core.discovery.LookupLocator;
import net.jini.core.entry.Entry;

/**
 * A <tt>MailboxAdminProxy</tt> is a client-side proxy for a mailbox service.
 * This interface provides access to the administrative functions of the mailbox
 * service as defined by the <tt>MailboxAdmin</tt> interface.
 * 
 * @author Sun Microsystems, Inc.
 * 
 * @since 1.1
 */
class MailboxAdminProxy implements MailboxAdmin, Serializable, ReferentUuid {

    private static final long serialVersionUID = 2L;

    /**
	 * The registrar
	 * 
	 * @serial
	 */
    final MailboxBackEnd server;

    /**
	 * The registrar's service ID
	 * 
	 * @serial
	 */
    final Uuid proxyID;

    /**
	 * Creates a mailbox proxy, returning an instance that implements
	 * RemoteMethodControl if the server does too.
	 * 
	 * @param mailbox
	 *            the server proxy
	 * @param id
	 *            the ID of the server
	 */
    static MailboxAdminProxy create(MailboxBackEnd mailbox, Uuid id) {
        if (mailbox instanceof RemoteMethodControl) {
            return new ConstrainableMailboxAdminProxy(mailbox, id, null);
        } else {
            return new MailboxAdminProxy(mailbox, id);
        }
    }

    /** Simple constructor. */
    private MailboxAdminProxy(MailboxBackEnd server, Uuid serviceProxyID) {
        this.server = server;
        this.proxyID = serviceProxyID;
    }

    public Entry[] getLookupAttributes() throws RemoteException {
        return server.getLookupAttributes();
    }

    public void addLookupAttributes(Entry[] attrSets) throws RemoteException {
        server.addLookupAttributes(attrSets);
    }

    public void modifyLookupAttributes(Entry[] attrSetTemplates, Entry[] attrSets) throws RemoteException {
        server.modifyLookupAttributes(attrSetTemplates, attrSets);
    }

    public String[] getLookupGroups() throws RemoteException {
        return server.getLookupGroups();
    }

    public void addLookupGroups(String[] groups) throws RemoteException {
        server.addLookupGroups(groups);
    }

    public void removeLookupGroups(String[] groups) throws RemoteException {
        server.removeLookupGroups(groups);
    }

    public void setLookupGroups(String[] groups) throws RemoteException {
        server.setLookupGroups(groups);
    }

    public LookupLocator[] getLookupLocators() throws RemoteException {
        return server.getLookupLocators();
    }

    public void addLookupLocators(LookupLocator[] locators) throws RemoteException {
        server.addLookupLocators(locators);
    }

    public void removeLookupLocators(LookupLocator[] locators) throws RemoteException {
        server.removeLookupLocators(locators);
    }

    public void setLookupLocators(LookupLocator[] locators) throws RemoteException {
        server.setLookupLocators(locators);
    }

    public void destroy() throws RemoteException {
        server.destroy();
    }

    /**
	 * Returns the universally unique identifier that has been assigned to the
	 * resource this proxy represents.
	 * 
	 * @return the instance of <code>Uuid</code> that is associated with the
	 *         resource this proxy represents. This method will not return
	 *         <code>null</code>.
	 * 
	 * @see net.jini.id.ReferentUuid
	 */
    public Uuid getReferentUuid() {
        return proxyID;
    }

    public int hashCode() {
        return proxyID.hashCode();
    }

    /** Proxies for servers with the same serviceProxyID are considered equal. */
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
            throw new InvalidObjectException("MailboxProxy.readObject " + "failure - server " + "field is null");
        }
        if (proxyID == null) {
            throw new InvalidObjectException("MailboxProxy.proxyID " + "failure - proxyID " + "field is null");
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
        throw new InvalidObjectException("no data found when attempting to " + "deserialize MailboxProxy instance");
    }

    static final class ConstrainableMailboxAdminProxy extends MailboxAdminProxy implements RemoteMethodControl {

        private static final long serialVersionUID = 2L;

        /**
		 * Constructs a new <code>ConstrainableMailboxAdminProxy</code>
		 * instance.
		 * <p>
		 * For a description of all but the <code>methodConstraints</code>
		 * argument (provided below), refer to the description for the
		 * constructor of this class' super class.
		 * 
		 * @param methodConstraints
		 *            the client method constraints to place on this proxy (may
		 *            be <code>null</code>).
		 */
        private ConstrainableMailboxAdminProxy(MailboxBackEnd server, Uuid proxyID, MethodConstraints methodConstraints) {
            super(constrainServer(server, methodConstraints), proxyID);
        }

        /**
		 * Returns a copy of the given server proxy having the client method
		 * constraints that result after the specified method mapping is applied
		 * to the given client method constraints.
		 */
        private static MailboxBackEnd constrainServer(MailboxBackEnd server, MethodConstraints constraints) {
            RemoteMethodControl constrainedServer = ((RemoteMethodControl) server).setConstraints(constraints);
            return ((MailboxBackEnd) constrainedServer);
        }

        /**
		 * Returns a new copy of this proxy class (
		 * <code>ConstrainableMailboxAdminProxy</code>) with its client
		 * constraints set to the specified constraints. A <code>null</code>
		 * value is interpreted as mapping all methods to empty constraints.
		 */
        public RemoteMethodControl setConstraints(MethodConstraints constraints) {
            return (new ConstrainableMailboxAdminProxy(server, proxyID, constraints));
        }

        /**
		 * Returns the client constraints placed on the current instance of this
		 * proxy class (<code>ConstrainableMailboxAdminProxy</code>). The value
		 * returned by this method can be <code>null</code>, which is
		 * interpreted as mapping all methods to empty constraints.
		 */
        public MethodConstraints getConstraints() {
            return (((RemoteMethodControl) server).getConstraints());
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
		 * Performs various functions related to the trust verification process
		 * for the current instance of this proxy class, as detailed in the
		 * description for this class.
		 * 
		 * @throws <code>InvalidObjectException</code> if any of the
		 *         requirements for trust verification (as detailed in the class
		 *         description) are not satisfied.
		 */
        private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
            s.defaultReadObject();
            if (!(server instanceof RemoteMethodControl)) {
                throw new InvalidObjectException("MailboxAdminProxy.readObject failure - server " + "does not implement RemoteMethodControl");
            }
        }
    }
}
