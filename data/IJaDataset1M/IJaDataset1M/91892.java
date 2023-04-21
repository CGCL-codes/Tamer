package net.sf.rcer.conn.tools;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.sf.rcer.conn.Activator;
import net.sf.rcer.conn.Messages;
import net.sf.rcer.conn.connections.ConnectionData;
import net.sf.rcer.conn.connections.IConnectionData;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;

/**
 * A class to read the contents of the file <code>saplogon.ini</code>
 * as specified by SAP note 99435 and turn them into a set of 
 * {@link IConnectionData} objects.
 * 
 * @author vwegert
 *
 */
public class LogonIniReader {

    private static final String SECTION_ROUTER = "Router";

    private static final String SECTION_ROUTER_2 = "Router2";

    private static final String SECTION_ROUTER_CHOICE = "RouterChoice";

    private static final String SECTION_SERVER = "Server";

    private static final String SECTION_SYSTEM_NUMBER = "Database";

    private static final String SECTION_SYSTEM_TYPE = "System";

    private static final String SECTION_DESCRIPTION = "Description";

    private static final String SECTION_SYSTEM_ID = "MSSysName";

    private static final String SECTION_MSG_SERVER = "MSSrvName";

    private static final String SECTION_ORIGIN = "Origin";

    private static final String SECTION_MSG_SERVER_PORT = "MSSrvPort";

    private SimpleIniFileParser parser;

    private List<IConnectionData> connections;

    private MultiStatus status;

    /**
	 * Reads and parses the contents of a SAPlogon.ini file given via an {@link InputStream}.
	 * @param stream
	 */
    public LogonIniReader(InputStream stream) {
        status = new MultiStatus(Activator.PLUGIN_ID, 0, Messages.LogonIniReader_ResultHeaderMessage, null);
        try {
            parser = new SimpleIniFileParser(stream);
        } catch (IniFileFormatException e) {
            status.add(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
            return;
        } catch (IOException e) {
            status.add(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
            return;
        }
        checkSection(SECTION_DESCRIPTION);
        checkSection(SECTION_ORIGIN);
        checkSection(SECTION_SERVER);
        checkSection(SECTION_SYSTEM_TYPE);
        checkSection(SECTION_SYSTEM_NUMBER);
        checkSection(SECTION_SYSTEM_ID);
        checkSection(SECTION_MSG_SERVER);
        if (status.isOK()) {
            final Set<String> keys = parser.getKeys(SECTION_SERVER);
            connections = new ArrayList<IConnectionData>(keys.size());
            for (final String key : keys) {
                IConnectionData conn = readConnection(key);
                if (conn != null) {
                    connections.add(conn);
                }
            }
        }
    }

    /**
	 * Checks whether a certain section exists. 
	 * @param section
	 */
    private void checkSection(String section) {
        if (!parser.sectionExists(section)) {
            status.add(new Status(IStatus.ERROR, Activator.PLUGIN_ID, MessageFormat.format(Messages.LogonIniReader_SectionMissingError, section)));
        }
    }

    /**
	 * Parses a set of connection data identified by the common key. 
	 * @param key
	 * @return the connection data or <code>null</code> if the data could not be parsed.
	 */
    private IConnectionData readConnection(String key) {
        final String description = parser.keyExists(SECTION_DESCRIPTION, key) ? parser.getValue(SECTION_DESCRIPTION, key) : Messages.LogonIniReader_DefaultDescription;
        if (!parser.getValue(SECTION_SYSTEM_TYPE, key).equals("3")) {
            status.add(new Status(IStatus.WARNING, Activator.PLUGIN_ID, MessageFormat.format(Messages.LogonIniReader_NoR3ConnectionError, key, description)));
            return null;
        }
        final String systemNumberString = parser.getValue(SECTION_SYSTEM_NUMBER, key);
        int systemNumber = -1;
        if ((systemNumberString == null) || (systemNumberString.length() == 0)) {
            status.add(new Status(IStatus.WARNING, Activator.PLUGIN_ID, MessageFormat.format(Messages.LogonIniReader_MissingSystemNumberError, key, description)));
            return null;
        }
        try {
            systemNumber = Integer.parseInt(systemNumberString);
        } catch (NumberFormatException e1) {
            status.add(new Status(IStatus.WARNING, Activator.PLUGIN_ID, MessageFormat.format(Messages.LogonIniReader_InvalidSystemNumberError, key, description, systemNumberString)));
            return null;
        }
        ConnectionData connection = new ConnectionData();
        connection.setConnectionDataID(key);
        connection.setDescription(description);
        connection.setSystemID(parser.getValue(SECTION_SYSTEM_ID, key));
        connection.setSystemNumber(systemNumber);
        connection.setRouter(parser.getValue(SECTION_ROUTER, key));
        final String origin = parser.getValue(SECTION_ORIGIN, key);
        if (origin.equalsIgnoreCase("MS_SEL_GROUPS")) {
            int port;
            try {
                port = Integer.parseInt(parser.getValue(SECTION_MSG_SERVER_PORT, key));
            } catch (Exception e) {
                port = 3600 + systemNumber;
            }
            connection.setLoadBalancingConnection(parser.getValue(SECTION_MSG_SERVER, key), port, parser.getValue(SECTION_SERVER, key));
            if (parser.getValue(SECTION_ROUTER_CHOICE, key).equals("1")) {
                final String router2 = parser.getValue(SECTION_ROUTER_2, key);
                if ((router2 != null) && !(router2.length() == 0)) {
                    connection.setRouter(router2);
                }
            }
        } else if (origin.equalsIgnoreCase("MS_SEL_SERVER") || origin.equalsIgnoreCase("USEREDIT")) {
            connection.setDirectConnection(parser.getValue(SECTION_SERVER, key), systemNumber);
        } else {
            status.add(new Status(IStatus.WARNING, Activator.PLUGIN_ID, MessageFormat.format(Messages.LogonIniReader_UnknownOriginError, key, description, origin)));
            return null;
        }
        return connection;
    }

    /**
	 * @return the connections 
	 */
    public List<IConnectionData> getConnections() {
        return Collections.unmodifiableList(connections);
    }

    /**
	 * @return the status
	 */
    public IStatus getStatus() {
        return status;
    }
}
