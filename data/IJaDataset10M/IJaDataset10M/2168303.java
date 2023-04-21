package net.hypotenubel.jaicwain.gui.swing;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import net.hypotenubel.jaicwain.App;
import net.hypotenubel.jaicwain.gui.docking.*;
import net.hypotenubel.jaicwain.local.LocalizationEventListener;
import net.hypotenubel.jaicwain.session.*;

/**
 * Displays all registered {@code Session}s and monitors their status.
 * 
 * @author Christoph Daniel Schulze
 * @version $Id: ConnectionStatusPanel.java 138 2006-10-01 21:10:46Z captainnuss $
 */
public class ConnectionStatusPanel extends DockingPanel implements LocalizationEventListener {

    private static SessionTableModel tableModel = null;

    private JTable table = null;

    private JScrollPane tableScrollPane = null;

    /**
     * Creates a new {@code ConnectionStatusPanel} object and initializes
     * it.
     */
    public ConnectionStatusPanel() {
        App.localization.addLocalizationEventListener(this);
        createUI();
        languageChanged();
    }

    /**
     * Initializes the GUI stuff.
     */
    protected void createUI() {
        if (tableModel == null) tableModel = new SessionTableModel();
        table = new JTable(tableModel);
        table.setAutoCreateColumnsFromModel(true);
        table.setCellSelectionEnabled(false);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableScrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setLayout(new BorderLayout());
        add(tableScrollPane, BorderLayout.CENTER);
    }

    /**
     * Removes us from all listener lists.
     */
    public void shutdown() {
        App.localization.removeLocalizationEventListener(this);
    }

    /**
     * {@inheritDoc}
     */
    public void languageChanged() {
        setTitle(App.localization.localize("app", "connectionstatuspanel.title", "Connection Monitor"));
        setLongTitle(App.localization.localize("app", "connectionstatuspanel.title", "Connection Monitor"));
        setDescription(App.localization.localize("app", "connectionstatuspanel.description", "Keeps track of open connections."));
    }

    /**
     * Provides a table model to keep track of all that session stuff.
     * 
     * @author Christoph Daniel Schulze
     * @version $Id: ConnectionStatusPanel.java 138 2006-10-01 21:10:46Z captainnuss $
     */
    private static class SessionTableModel extends AbstractTableModel implements LocalizationEventListener, SessionEventListener, SessionStatusEventListener {

        /**
         * Internal column names, used to localize theem. Didn't expect me
         * putting an extra "e" into "them", eh? Didn't?
         */
        private static final String[] columns = new String[] { "protocol", "type", "status", "remtaddress", "remtport", "lclport" };

        /**
         * Default column names.
         */
        private static final String[] defaultNames = new String[] { "Protocol", "Type", "Status", "Remote Address", "Remote Port", "Local Port" };

        /**
         * Maps a list of {@code Session}s to each registered {@code Protocol}.
         */
        private HashMap<Protocol, ArrayList<Session>> sessions = new HashMap<Protocol, ArrayList<Session>>(8);

        /**
         * The number of rows.
         */
        private int rows = 0;

        /**
         * Creates a new {@code SessionTableModel} object and initializes
         * it.
         */
        public SessionTableModel() {
            App.localization.addLocalizationEventListener(this);
            App.sessions.addSessionEventListener(this);
            Protocol[] protocols = App.sessions.getProtocols();
            Session[] sessions;
            for (int i = 0; i < protocols.length; i++) {
                protocolRegistered(protocols[i]);
                sessions = App.sessions.getSessions(protocols[i]);
                for (int j = 0; j < sessions.length; j++) sessionAdded(sessions[j]);
            }
        }

        /**
         * Returns the list index of the specified session.
         * 
         * @param session {@code Session} whose list index to return.
         * @return the session's list index.
         */
        private int getListIndex(Session session) {
            Protocol sessionProtocol = session.getProtocol();
            Protocol[] protocols = App.sessions.getProtocols();
            ArrayList<Session> list = sessions.get(sessionProtocol);
            int index = 0;
            for (int i = 0; i < protocols.length; i++) {
                if (protocols[i].equals(sessionProtocol)) {
                    for (int j = 0; j < list.size(); j++) {
                        if (list.get(j) == session) {
                            i = protocols.length;
                            break;
                        }
                        index++;
                    }
                } else {
                    index += (App.sessions.getSessions(protocols[i])).length;
                }
            }
            return index;
        }

        /**
         * Returns the number of rows.
         * 
         * @return the number of rows.
         */
        public int getRowCount() {
            return rows;
        }

        /**
         * Returns the number of columns. Typically, that should be 6.
         * 
         * @return the number of columns.
         */
        public int getColumnCount() {
            return columns.length;
        }

        /**
         * Returns the value at the specified position.
         * 
         * @param row    the row whose value is to be returned.
         * @param column the column whose value is to be queried.
         * @return the value at the specified location.
         */
        public Object getValueAt(int row, int column) {
            Protocol[] protocols = App.sessions.getProtocols();
            Session session = null;
            ArrayList<Session> list;
            int index = 0;
            for (int i = 0; i < protocols.length; i++) {
                list = sessions.get(protocols[i]);
                for (int j = 0; j < list.size(); j++) {
                    if (index == row) {
                        session = list.get(j);
                        i = protocols.length;
                        break;
                    }
                    index++;
                }
            }
            if (session == null) return "";
            switch(column) {
                case 0:
                    return session.getProtocol().getShortName();
                case 1:
                    if (session.isServer()) {
                        return App.localization.localize("app", "connectionstatuspanel.sessiontablemodel.columns." + "type.server", "Server");
                    } else {
                        return App.localization.localize("app", "connectionstatuspanel.sessiontablemodel.columns." + "type.client", "Client");
                    }
                case 2:
                    return App.localization.localize("app", "connectionstatuspanel.sessiontablemodel.columns." + "status." + session.getStatus().getID(), session.getStatus().getDisplayText());
                case 3:
                    return session.getRemoteAddress();
                case 4:
                    return String.valueOf(session.getRemotePort());
                case 5:
                    return String.valueOf(session.getLocalPort());
            }
            return "";
        }

        /**
         * Returns the name of the specified column.
         * 
         * @param column the column's index.
         * @return the column's name.
         */
        public String getColumnName(int column) {
            if (column < 0 || column >= columns.length) return "";
            return App.localization.localize("app", "connectionstatuspanel.sessiontablemodel.columns." + columns[column], defaultNames[column]);
        }

        /**
         * {@inheritDoc}
         */
        public void languageChanged() {
            fireTableStructureChanged();
        }

        /**
         * {@inheritDoc}
         */
        public void sessionAdded(Session session) {
            ArrayList<Session> list = sessions.get(session.getProtocol());
            list.add(session);
            rows++;
            session.addSessionStatusEventListener(this);
            int index = getListIndex(session);
            fireTableRowsInserted(index, index);
        }

        /**
         * {@inheritDoc}
         */
        public void sessionRemoved(Session session) {
            ArrayList list = sessions.get(session.getProtocol());
            int index = getListIndex(session);
            list.remove(session);
            rows--;
            session.removeSessionStatusEventListener(this);
            fireTableRowsDeleted(index, index);
        }

        /**
         * {@inheritDoc}
         */
        public void protocolRegistered(Protocol protocol) {
            if (protocol == null) return;
            sessions.put(protocol, new ArrayList<Session>(5));
        }

        /**
         * {@inheritDoc}
         */
        public void sessionStatusChanged(Session session, SessionStatus oldStatus) {
            int index = getListIndex(session);
            fireTableRowsUpdated(index, index);
        }
    }
}
