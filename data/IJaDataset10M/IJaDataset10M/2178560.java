package com.flaptor.util.cache;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.apache.log4j.Logger;
import com.flaptor.util.remote.AClientStub;
import com.flaptor.util.remote.ExponentialFallbackPolicy;
import com.flaptor.util.remote.RmiServer;
import com.flaptor.util.remote.RpcException;

/**
 * Stub for accessing a remote cache through RMI
 * 
 * @author Martin Massera
 */
public class RmiCacheStub<T extends Serializable> extends AClientStub implements RemoteCache<T> {

    private static Logger logger = Logger.getLogger(com.flaptor.util.Execute.whoAmI());

    private RmiCache<T> remoteCache = null;

    public RmiCacheStub(int port, String host) {
        super(port, host, new ExponentialFallbackPolicy());
    }

    public void addItem(final String key, final T value) throws RpcException {
        doRemote(new RemoteAction() {

            public Object execute() throws RemoteException {
                remoteCache.addItem(key, value);
                return null;
            }
        });
    }

    public boolean hasItem(final String key) throws RpcException {
        return (Boolean) doRemote(new RemoteAction() {

            public Object execute() throws RemoteException {
                return remoteCache.hasItem(key);
            }
        });
    }

    public boolean removeItem(final String key) throws RpcException {
        return (Boolean) doRemote(new RemoteAction() {

            public Object execute() throws RemoteException {
                return remoteCache.removeItem(key);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public T getItem(final String key) throws RpcException {
        return (T) doRemote(new RemoteAction() {

            public Object execute() throws RemoteException {
                return remoteCache.getItem(key);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void connect() {
        Registry registry = null;
        logger.info("connecting to remote cache at " + host + ":" + port);
        try {
            registry = LocateRegistry.getRegistry(host, port);
        } catch (RemoteException e) {
            logger.error("Could not locate registry.", e);
            policy.markFailure();
            return;
        }
        try {
            remoteCache = (RmiCache<T>) registry.lookup(RmiServer.DEFAULT_SERVICE_NAME);
            logger.info("(re)connected to remote cache at " + host + ":" + port);
            policy.markSuccess();
        } catch (RemoteException e) {
            logger.error("Error while looking up.", e);
        } catch (NotBoundException e) {
            logger.error("The registry is not bound, but I just reconnected with it... odd.", e);
        }
    }

    public Object doRemote(RemoteAction remoteAction) throws RpcException {
        if (policy.shouldReconnect()) {
            connect();
        }
        if (policy.callServer()) {
            try {
                if (null == remoteCache) {
                    throw new RpcException();
                }
                Object ret = remoteAction.execute();
                policy.markSuccess();
                return ret;
            } catch (RemoteException e) {
                policy.markFailure();
                throw new RpcException(e);
            }
        } else {
            throw new RpcException("The policy requested to skip this server.");
        }
    }

    public static interface RemoteAction {

        public Object execute() throws RemoteException;
    }
}
