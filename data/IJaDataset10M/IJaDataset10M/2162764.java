package javax.microedition.broadcast;

import javax.microedition.broadcast.BroadcastServiceException;
import javax.microedition.broadcast.InsufficientResourcesException;
import javax.microedition.broadcast.ServiceComponent;
import javax.microedition.broadcast.ServiceContextListener;
import javax.microedition.broadcast.esg.Service;
import javax.microedition.broadcast.connection.BroadcastDatagramConnection;
import javax.microedition.media.Player;

public abstract class ServiceContext {

    public static final String LANGUAGE = "language";

    public static final int CLOSED = 0;

    public static final int STOPPED = 1;

    public static final int ACQUIRING_SERVICE = 2;

    public static final int SERVICE_ACQUIRED = 3;

    public static final int PREPARING_MEDIA = 4;

    public static final int PRESENTING = 5;

    public static ServiceContext createServiceContext() throws SecurityException {
        return _getDefaultServiceContextSingleton()._newServiceContext();
    }

    public static ServiceContext getDefaultContext() throws SecurityException {
        return _getDefaultServiceContextSingleton();
    }

    public abstract void addListener(ServiceContextListener listener) throws IllegalStateException;

    public abstract void removeListener(ServiceContextListener listener) throws IllegalStateException;

    public abstract int getSignalQuality() throws IllegalStateException;

    public abstract int getState();

    public abstract Object getPreference(String key) throws IllegalStateException, NullPointerException;

    public abstract String[] getAllPreferenceKeys() throws IllegalStateException;

    public abstract Object setPreference(String key, Object value) throws IllegalStateException, NullPointerException;

    public abstract Service getService() throws IllegalStateException;

    public abstract void select(Service service) throws IllegalArgumentException, SecurityException, IllegalStateException, NullPointerException;

    public abstract void stop() throws IllegalStateException;

    public abstract ServiceComponent[] getSelectedComponents() throws IllegalStateException;

    public abstract ServiceComponent[] getUnselectedComponents() throws IllegalStateException;

    public abstract ServiceComponent[] getAllComponents() throws IllegalStateException;

    public abstract ServiceComponent[] getUnrecognizedComponents() throws IllegalStateException;

    public abstract void setComponents(ServiceComponent[] components) throws InsufficientResourcesException, IllegalStateException, BroadcastServiceException, NullPointerException;

    public abstract BroadcastDatagramConnection getBroadcastDatagramConnection(ServiceComponent component) throws BroadcastServiceException, SecurityException, IllegalStateException, NullPointerException;

    public abstract Player getPlayer(ServiceComponent component) throws BroadcastServiceException, IllegalStateException, NullPointerException;

    public abstract Player[] getPlayers() throws IllegalStateException;

    public abstract void close() throws SecurityException;

    protected static ServiceContext _defaultServiceContextSingleton;

    protected static ServiceContext _getDefaultServiceContextSingleton() {
        if (_defaultServiceContextSingleton == null) throw new NullPointerException("Default Service Context not initialized.");
        return _defaultServiceContextSingleton;
    }

    protected abstract ServiceContext _newServiceContext();
}
