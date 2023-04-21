package net.sf.rcer.conn.preferences;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import net.sf.rcer.conn.Activator;
import net.sf.rcer.conn.Messages;
import net.sf.rcer.conn.connections.ConnectionData;
import net.sf.rcer.conn.connections.ConnectionNotFoundException;
import net.sf.rcer.conn.connections.IConnectionData;
import net.sf.rcer.conn.locales.Locale;
import net.sf.rcer.conn.locales.LocaleNotFoundException;
import net.sf.rcer.conn.locales.LocaleRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;

/**
 * A singleton class that manages the connection data entries that are stored in the preferences.
 * @author vwegert
 *
 */
public class PreferencesConnectionManager implements IPreferenceConstants {

    /**
	 * The singleton instance.
	 */
    private static volatile PreferencesConnectionManager instance;

    /**
	 * The cached preferences scope used to store the preferences.
	 */
    private IEclipsePreferences preferences;

    /**
	 * Private constructor to prevent secondary instantiation.
	 */
    private PreferencesConnectionManager() {
        preferences = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
    }

    /**
	 * @return the singleton instance
	 */
    public static PreferencesConnectionManager getInstance() {
        if (instance == null) {
            synchronized (PreferencesConnectionManager.class) {
                if (instance == null) {
                    instance = new PreferencesConnectionManager();
                }
            }
        }
        return instance;
    }

    /**
	 * @return a list of all connection IDs stored in the preferences
	 */
    public Set<String> getConnectionIDs() {
        synchronizePreferences();
        Set<String> connectionIDs = new HashSet<String>();
        final int num = getInt(CONNECTION_NUMBER);
        for (int i = 1; i <= num; i++) {
            connectionIDs.add(Integer.toString(i));
        }
        return connectionIDs;
    }

    /**
	 * @param connectionID the ID of the connection
	 * @return the connection corresponding to the connection ID requested
	 * @throws ConnectionNotFoundException 
	 */
    public ConnectionData getConnectionData(String connectionID) throws ConnectionNotFoundException {
        return getConnectionData(getConnectionPosition(connectionID));
    }

    /**
	 * @return the connections stored in the preferences
	 */
    public Collection<ConnectionData> getConnectionData() {
        synchronizePreferences();
        Collection<ConnectionData> connections = new Vector<ConnectionData>();
        final int num = getInt(CONNECTION_NUMBER);
        for (int i = 1; i <= num; i++) {
            try {
                connections.add(getConnectionData(i));
            } catch (ConnectionNotFoundException e) {
                logError(MessageFormat.format(Messages.PreferencesConnectionManager_ConnectionContainsErrors, i), e);
            }
        }
        return connections;
    }

    /**
	 * @param position
	 * @return the connection stored at the designated position
	 * @throws ConnectionNotFoundException
	 */
    private ConnectionData getConnectionData(int position) throws ConnectionNotFoundException {
        synchronizePreferences();
        final String connectionID = Integer.toString(position);
        ConnectionData connection;
        if (getString(CONNECTION_TYPE, position).equals(CONNECTION_TYPE_DIRECT)) {
            connection = new ConnectionData(connectionID, getString(CONNECTION_DESCRIPTION, position), getString(CONNECTION_SYSTEM_ID, position), getString(CONNECTION_ROUTER, position), getString(CONNECTION_APPLICATION_SERVER, position), getInt(CONNECTION_SYSTEM_NUMBER, position));
        } else if (getString(CONNECTION_TYPE, position).equals(CONNECTION_TYPE_LOAD_BALANCING)) {
            connection = new ConnectionData(connectionID, getString(CONNECTION_DESCRIPTION, position), getString(CONNECTION_SYSTEM_ID, position), getString(CONNECTION_ROUTER, position), getString(CONNECTION_MESSAGE_SERVER, position), getInt(CONNECTION_MESSAGE_SERVER_PORT, position), getString(CONNECTION_LOAD_BALANCING_GROUP, position));
        } else {
            throw new ConnectionNotFoundException(MessageFormat.format(Messages.PreferencesConnectionManager_InvalidConnectionType, connectionID, getString(CONNECTION_TYPE, position)), false);
        }
        connection.setDefaultUser(getString(CONNECTION_DEFAULT_USER, position), true);
        connection.setDefaultClient(getString(CONNECTION_DEFAULT_CLIENT, position), true);
        final String localeISOCode = getString(CONNECTION_DEFAULT_LOCALE, position);
        if ((localeISOCode != null) && (!localeISOCode.equals(""))) {
            try {
                final Locale locale = LocaleRegistry.getInstance().getLocaleByISO(localeISOCode);
                connection.setDefaultLocale(locale, true);
            } catch (LocaleNotFoundException e) {
                logError(MessageFormat.format(Messages.PreferencesConnectionManager_InvalidDefaultLocale, connectionID), e);
            }
        }
        return connection;
    }

    /**
	 * Saves the connections to the preferences, <b>replacing all existing connections</b>. Note that <b>the
	 * connection IDs are not guaranteed to be stable</b> (although the registry will try to use the
	 * connection IDs provided).
	 * @param connections
	 * @throws BackingStoreException 
	 */
    public void saveConnectionData(Collection<IConnectionData> connections) throws BackingStoreException {
        Map<Integer, IConnectionData> connectionMap = new TreeMap<Integer, IConnectionData>();
        Vector<IConnectionData> remainingConnections = new Vector<IConnectionData>();
        for (final IConnectionData connection : connections) {
            try {
                final Integer id = Integer.parseInt(connection.getConnectionDataID());
                if (id <= 0) {
                    remainingConnections.add(connection);
                } else if (connectionMap.containsKey(id)) {
                    remainingConnections.add(connection);
                } else {
                    connectionMap.put(id, connection);
                }
            } catch (NumberFormatException e) {
                remainingConnections.add(connection);
            }
        }
        int position = 1;
        while (!remainingConnections.isEmpty()) {
            while (connectionMap.containsKey(position)) {
                position++;
            }
            connectionMap.put(position, remainingConnections.remove(0));
        }
        for (position = 1; position <= connectionMap.size(); position++) {
            if (!connectionMap.containsKey(position)) {
                int oldPosition = position;
                for (final int i : connectionMap.keySet()) {
                    if (i > oldPosition) {
                        oldPosition = i;
                    }
                }
                final IConnectionData entry = connectionMap.remove(oldPosition);
                connectionMap.put(position, entry);
            }
        }
        for (final Integer id : connectionMap.keySet()) {
            final IConnectionData connection = connectionMap.get(id);
            setValue(CONNECTION_DESCRIPTION, id, connection.getDescription());
            setValue(CONNECTION_SYSTEM_ID, id, connection.getSystemID());
            setValue(CONNECTION_ROUTER, id, connection.getRouter());
            switch(connection.getConnectionType()) {
                case DIRECT:
                    setValue(CONNECTION_TYPE, id, CONNECTION_TYPE_DIRECT);
                    setValue(CONNECTION_APPLICATION_SERVER, id, connection.getApplicationServer());
                    setValue(CONNECTION_SYSTEM_NUMBER, id, connection.getSystemNumber());
                    setToDefault(CONNECTION_MESSAGE_SERVER, id);
                    setToDefault(CONNECTION_MESSAGE_SERVER_PORT, id);
                    setToDefault(CONNECTION_LOAD_BALANCING_GROUP, id);
                    break;
                case LOAD_BALANCED:
                    setValue(CONNECTION_TYPE, id, CONNECTION_TYPE_LOAD_BALANCING);
                    setToDefault(CONNECTION_APPLICATION_SERVER, id);
                    setToDefault(CONNECTION_SYSTEM_NUMBER, id);
                    setValue(CONNECTION_MESSAGE_SERVER, id, connection.getMessageServer());
                    setValue(CONNECTION_MESSAGE_SERVER_PORT, id, connection.getMessageServerPort());
                    setValue(CONNECTION_LOAD_BALANCING_GROUP, id, connection.getLoadBalancingGroup());
                    break;
            }
            if (connection.getDefaultUser() == null) {
                setToDefault(CONNECTION_DEFAULT_USER, id);
            } else {
                setValue(CONNECTION_DEFAULT_USER, id, connection.getDefaultUser());
            }
            if (connection.getDefaultClient() == null) {
                setToDefault(CONNECTION_DEFAULT_CLIENT, id);
            } else {
                setValue(CONNECTION_DEFAULT_CLIENT, id, connection.getDefaultClient());
            }
            if (connection.getDefaultLocale() == null) {
                setToDefault(CONNECTION_DEFAULT_LOCALE, id);
            } else {
                setValue(CONNECTION_DEFAULT_LOCALE, id, connection.getDefaultLocale().getISOCode());
            }
        }
        preferences.putInt(CONNECTION_NUMBER, connectionMap.size());
        preferences.flush();
    }

    /**
	 * This method performs {@link IEclipsePreferences#sync()} with some basic error handling.
	 */
    private void synchronizePreferences() {
        try {
            preferences.sync();
        } catch (BackingStoreException e) {
            Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, Messages.PreferencesConnectionManager_ConnectionDataStale, e));
        }
    }

    /**
	 * Auxiliary method to parse and validate the connection ID. 
	 * @param connectionID
	 * @return the position designated by the connection ID
	 * @throws ConnectionNotFoundException
	 */
    private int getConnectionPosition(String connectionID) throws ConnectionNotFoundException {
        int number = getInt(CONNECTION_NUMBER);
        int position = -1;
        try {
            position = Integer.parseInt(connectionID);
        } catch (NumberFormatException e) {
            throw new ConnectionNotFoundException(MessageFormat.format(Messages.PreferencesConnectionManager_ConnectionIDNotNumeric, connectionID), e, false);
        }
        if ((position <= 0) || (position > number)) {
            throw new ConnectionNotFoundException(MessageFormat.format(Messages.PreferencesConnectionManager_ConnectionIDOutOfBounds, connectionID, number), false);
        }
        return position;
    }

    /**
	 * Auxiliary method to log an error message
	 * @param message the message to log
	 * @param exception the exception that occurred
	 */
    private void logError(String message, Exception exception) {
        Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, exception));
    }

    /**
	 * Auxiliary method to get the value of a positional preference.
	 * @param prefix
	 * @param position
	 * @return the string value
	 */
    private String getString(String prefix, int position) {
        return Platform.getPreferencesService().getString(Activator.PLUGIN_ID, MessageFormat.format("{0}.{1}", prefix, position), "", null);
    }

    /**
	 * Auxiliary method to get the value of a preference.
	 * @param key
	 * @return the integer value
	 */
    private int getInt(String key) {
        return Platform.getPreferencesService().getInt(Activator.PLUGIN_ID, key, -1, null);
    }

    /**
	 * Auxiliary method to get the value of a positional preference.
	 * @param prefix
	 * @param position
	 * @return the integer value
	 */
    private int getInt(String prefix, int position) {
        return Platform.getPreferencesService().getInt(Activator.PLUGIN_ID, MessageFormat.format("{0}.{1}", prefix, position), -1, null);
    }

    /**
	 * Auxiliary method to reset a value to its default.
	 * @param prefix
	 * @param position
	 */
    private void setToDefault(String prefix, int position) {
        preferences.remove(MessageFormat.format("{0}.{1}", prefix, position));
    }

    /**
	 * Auxiliary method to set a string value. 
	 * @param prefix
	 * @param position
	 * @param value
	 */
    private void setValue(String prefix, int position, String value) {
        preferences.put(MessageFormat.format("{0}.{1}", prefix, position), value);
    }

    /**
	 * Auxiliary method to set an integer value.
	 * @param prefix
	 * @param position
	 * @param value
	 */
    private void setValue(String prefix, int position, int value) {
        preferences.putInt(MessageFormat.format("{0}.{1}", prefix, position), value);
    }
}
