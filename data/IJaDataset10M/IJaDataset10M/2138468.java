package dsb.support.rmi;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.ServerException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Manage an RMI {@link Registry} instance. The registry is exported to a known
 * port. Then {@link Remote} objects can be exported with a specific name.
 * Clients can then connect to this registry and lookup the objects by those
 * names.
 */
public class RMIRegistryManager {

    private static final Logger logger = Logger.getLogger(RMIRegistryManager.class);

    /** The RMI registry managed by this {@link RMIRegistryManager} instance. */
    private Registry registry;

    /**
	 * Create a new {@link RMIRegistryManager} instance.
	 * 
	 * @param port
	 *            The port to listen on for lookup requests.
	 */
    public RMIRegistryManager(int port) {
        logger.trace("Creating registry with port: " + port);
        try {
            this.registry = LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            throw new RMIException.RegistryExportFailedException(port, e);
        }
    }

    /**
	 * Create a new {@link RMIRegistryManager} instance, without a
	 * {@link Registry}. Use the property setter for {@link #registry}.
	 * 
	 * @see #setRegistry(Registry)
	 */
    public RMIRegistryManager() {
        logger.trace("Not creating registry. Use property setter instead.");
    }

    /**
	 * Set the managed RMI {@link Registry}.
	 * 
	 * @param registry
	 *            An initialized {@link Registry}.
	 */
    public void setRegistry(Registry registry) {
        Validate.notNull(registry, "registry should be non-null");
        this.registry = registry;
    }

    /**
	 * Unbind and unexport all bound objects.
	 */
    public void clear() {
        logger.trace("Unbinding and unexporting all bound objects ...");
        for (final String name : this.getBoundNames()) {
            try {
                this.unexportObject(name);
            } catch (RMIException e) {
                logger.warn("Could not unexport object bound with name: " + name, e);
            }
        }
    }

    /**
	 * Get all names bound in this registry.
	 * 
	 * @return An array of {@link String}s.
	 */
    String[] getBoundNames() {
        String[] result = null;
        try {
            result = this.registry.list();
        } catch (AccessException e) {
            throw new RMIException.LocalRegistryAccessException(e);
        } catch (RemoteException e) {
            this.handleRemoteException(e);
        }
        return result;
    }

    /**
	 * Export an object and bind it to some name.
	 * 
	 * @param instance
	 *            The {@link Remote} instance to export. Should be non-
	 *            <code>null</code>.
	 * @param name
	 *            The name to bind to. Should be a non-empty {@link String}.
	 * @return A {@link Remote} stub object representing the original instance.
	 */
    public Remote exportObject(Remote instance, String name) {
        logger.trace("Exporting Remote instance: " + instance + " with name: " + name);
        Validate.notNull(instance, "instance should not be null");
        Validate.notEmpty(name, "name should be a non-empty string");
        Remote stub = null;
        try {
            stub = UnicastRemoteObject.exportObject(instance, 0);
        } catch (RemoteException e) {
            throw new RMIException.ObjectExportFailedException(instance, e);
        }
        try {
            this.registry.bind(name, instance);
        } catch (AccessException e) {
            throw new RMIException.LocalRegistryAccessException(e);
        } catch (RemoteException e) {
            this.handleRemoteException(e);
        } catch (AlreadyBoundException e) {
            throw new RMIException.NameAlreadyBoundException(name, e);
        }
        return stub;
    }

    /**
	 * Unexport a bound object.
	 * 
	 * @param name
	 *            The name to which the object is bound.
	 */
    public void unexportObject(String name) {
        logger.trace("Unexporting name: " + name);
        Remote instance = null;
        try {
            instance = this.registry.lookup(name);
        } catch (AccessException e) {
            throw new RMIException.LocalRegistryAccessException(e);
        } catch (RemoteException e) {
            this.handleRemoteException(e);
        } catch (NotBoundException e) {
            throw new RMIException.NameNotBoundException(name, e);
        }
        try {
            this.registry.unbind(name);
        } catch (AccessException e) {
            throw new RMIException.LocalRegistryAccessException(e);
        } catch (RemoteException e) {
            this.handleRemoteException(e);
        } catch (NotBoundException e) {
            throw new RMIException.NameNotBoundException(name, e);
        }
        try {
            UnicastRemoteObject.unexportObject(instance, false);
        } catch (NoSuchObjectException e) {
            throw new RMIException.ObjectNotExportedException(instance, e);
        }
    }

    /**
	 * Handle a thrown {@link RemoteException}, testing for a
	 * {@link ServerException} containing an {@link AccessException}.
	 * 
	 * @param e
	 *            The {@link RemoteException} to handle.
	 */
    private void handleRemoteException(RemoteException e) {
        if (e instanceof ServerException) {
            ServerException se = (ServerException) e;
            if (se.getCause() instanceof AccessException) {
                throw new RMIException.RemoteRegistryAccessException(e);
            } else {
                throw new RMIException("Unknown cause of exception", e);
            }
        } else {
            throw new RMIException.RemoteCommunicationException(e);
        }
    }
}
