package org.database;

import java.lang.Thread.UncaughtExceptionHandler;
import org.communications.CommunicationManager.STATUS;
import org.communications.ConnectionListener;
import org.communications.ConnectionStatusChangeEvent;
import org.database.structure.DatabaseStructureListener;
import org.database.structure.DatabaseStrutuctureChangeEvent;
import org.database.structure.DatabaseStrutuctureChangeEvent.DBSTRUCTURECHANGETYPE;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.postgresql.PGNotification;

/**
 * Listens to notifications from the database
 *
 * @author Joao Leal
 */
public class DbListenerHandler {

    private static final Logger logger = Logger.getLogger(DbListenerHandler.class.getName(), "org/database/i18n/messages");

    private DbListener listener;

    private final DatabaseManager manager;

    private final ConnectionListener connectionListener = new ConnectionListener() {

        @Override
        public void connectionStateChanged(ConnectionStatusChangeEvent e) {
            evaluateStart(e.newStatus);
        }
    };

    private final UncaughtExceptionHandler uncaughtExceptionHandler = new UncaughtExceptionHandler() {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            logger.log(Level.SEVERE, "The_database_notification_listener_thread_exited_due_to_an_unexpected_error", e);
            evaluateStart(manager.getConnectionStatus());
        }
    };

    private final long updateRate;

    private final Set<DbTableGroup> notifiedTableGroups = new HashSet<DbTableGroup>();

    /**
     * Creates a new instance of DbListenerHandler
     *
     * @param manager the database manager
     */
    public DbListenerHandler(DatabaseManager manager) {
        this(manager, 300);
    }

    /**
     * Creates a new instance of DbListenerHandler
     *
     * @param manager the database manager
     */
    public DbListenerHandler(DatabaseManager manager, long updateRate) {
        if (manager == null) {
            throw new IllegalArgumentException("The DatabaseManager cannot be null");
        }
        if (updateRate <= 0) {
            throw new IllegalArgumentException("The update rate must be positive");
        }
        this.manager = manager;
        this.updateRate = updateRate;
        manager.support.addConnectionListener(connectionListener);
        evaluateStart(manager.getConnectionStatus());
    }

    protected void tableGroupNotified(final DbTableGroup group) {
        notifiedTableGroups.add(group);
    }

    private void evaluateStart(STATUS newStatus) {
        if (listener != null) {
            listener.cancel();
            listener = null;
        }
        if (newStatus == STATUS.CONNECTED) {
            listener = new DbListener();
            Thread th = new Thread(manager.getThreadGroup(), listener, "Database tables listener");
            th.setUncaughtExceptionHandler(uncaughtExceptionHandler);
            th.start();
        }
    }

    /**
     * The runnable responsible for listenning for notifications from the
     * database
     *
     * @author Joao Leal
     */
    private class DbListener implements Runnable, DatabaseStructureListener {

        private final Map<String, DbTable> tableMap;

        private boolean cancelled = false;

        /**
         * Creates a new instance of DbListenerHandler
         */
        public DbListener() {
            tableMap = Collections.synchronizedMap(new HashMap<String, DbTable>(manager.structure.getTables().size()));
            manager.structure.addDatabaseStructureListener(listener);
            logger.setLevel(Level.ALL);
        }

        public synchronized void cancel() {
            manager.structure.removeDatabaseStructureListener(listener);
            cancelled = true;
        }

        public synchronized boolean isCancelled() {
            return cancelled;
        }

        @Override
        public void run() {
            Connection con = manager.getCon();
            org.postgresql.PGConnection pgconn = (org.postgresql.PGConnection) con;
            Map<? extends DbTable, Map<String, Integer>> dbt = manager.structure.getTables();
            addListenTo(dbt.keySet().toArray(new DbTable[dbt.size()]));
            logger.finest("Database_listener_thread_installed_notification_listeners");
            Statement stmt = null;
            while (!Thread.interrupted() && manager.getConnectionStatus() == STATUS.CONNECTED && !isCancelled()) {
                try {
                    stmt = con.createStatement();
                    stmt.executeQuery("SELECT 1");
                    PGNotification notifications[] = pgconn.getNotifications();
                    List<DbTable> notifiedTables = new ArrayList<DbTable>(tableMap.size());
                    if (notifications != null) {
                        notifiedTableGroups.clear();
                        for (PGNotification notification : notifications) {
                            DbTable table = tableMap.get(notification.getName());
                            if (table != null) {
                                if (!notifiedTables.contains(table)) {
                                    notifiedTables.add(table);
                                }
                            } else {
                                logger.log(Level.WARNING, "Stray_notification_received_from_the_database_{0}", notification.getName());
                            }
                        }
                        for (DbTable table : notifiedTables) {
                            try {
                                table.fireNotification();
                            } catch (Throwable e) {
                                logger.log(Level.SEVERE, "A_database_notification_listener_for_table_{0}_triggered_an_error_{1}", new Object[] { table.toString(), e.getMessage() });
                                e.printStackTrace();
                            }
                        }
                        for (DbTableGroup group : notifiedTableGroups) {
                            try {
                                group.fireTableGroupNotificationEvent();
                            } catch (Throwable e) {
                                logger.log(Level.SEVERE, "A_database_notification_listener_for_table_group_{0}_triggered_an_error_{1}", new Object[] { group.toString(), e.getMessage() });
                                e.printStackTrace();
                            }
                        }
                        notifiedTables.clear();
                    }
                    Thread.sleep(updateRate);
                } catch (SQLException sqle) {
                    if (manager.getConnectionStatus() == STATUS.CONNECTED) {
                        logger.log(Level.SEVERE, "SQL_exception_{0}", sqle.getLocalizedMessage());
                    }
                } catch (InterruptedException ie) {
                    logger.log(Level.SEVERE, "Thread_Interrupted_Exception_{0}", ie.getLocalizedMessage());
                } finally {
                    DatabaseManager.closeStatement(stmt);
                }
            }
        }

        private void addListenTo(DbTable... tables) {
            Connection con = manager.getCon();
            Statement stmt = null;
            try {
                stmt = con.createStatement();
                for (DbTable table : tables) {
                    if (!table.isReadable()) {
                        continue;
                    }
                    String notifyName = table.getNotificationName();
                    if (notifyName == null) {
                        logger.log(Level.SEVERE, "Table_{0}_does_not_have_a_notification_name", table);
                    } else {
                        DbTable t = tableMap.get(notifyName);
                        if (t != null) {
                            logger.log(Level.SEVERE, "Tables_{0}_and_{1}_have_the_same_notification_name", new Object[] { t, table });
                            return;
                        }
                        tableMap.put(notifyName, table);
                        stmt.execute("LISTEN " + table.getNotificationName());
                    }
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Unable_to_execute_all_listen_sql_commands_{0}", ex.getMessage());
            } finally {
                DatabaseManager.closeStatement(stmt);
            }
        }

        private void removeListenTo(DbTable... tables) {
            Connection con = manager.getCon();
            Statement stmt = null;
            try {
                stmt = con.createStatement();
                for (DbTable table : tables) {
                    if (!table.isReadable()) {
                        continue;
                    }
                    if (table.getNotificationName() == null) {
                        logger.log(Level.SEVERE, "Table_{0}_does_not_have_a_notification_name", table);
                    } else {
                        stmt.execute("UNLISTEN " + table.getNotificationName());
                        tableMap.remove(table.getNotificationName());
                    }
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Unable_to_execute_all_unlisten_sql_commands_{0}", ex.getMessage());
            } finally {
                DatabaseManager.closeStatement(stmt);
            }
        }

        @Override
        public void databaseStructureChanged(DatabaseStrutuctureChangeEvent event) {
            if (event.dataBaseStructure != manager.structure) {
                return;
            }
            if (event.type == DBSTRUCTURECHANGETYPE.ADD) {
                addListenTo(event.table);
            } else if (event.type == DBSTRUCTURECHANGETYPE.REMOVE) {
                removeListenTo(event.table);
            }
        }
    }
}
