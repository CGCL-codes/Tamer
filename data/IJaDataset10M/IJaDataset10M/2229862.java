package com.meatfreezer.nms.cacti;

import com.meatfreezer.nms.IPollingItem;
import java.sql.*;
import java.util.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Alex Shepard
 */
public class CactiInserter implements Runnable {

    Connection db;

    Queue<IPollingItem> results;

    Logger logger;

    private boolean isApplicationFinished = false;

    /** 
     * Creates a new instance of CactiInserter.
     *
     * @param   db  The database connection to use for inserting.
     * @param   results The results queue to poll and insert from.
     */
    public CactiInserter(Connection db, Queue<IPollingItem> results) {
        this.db = db;
        this.results = results;
        logger = Logger.getLogger("com.meatfreezer.jactid");
    }

    /**
     * When the main app is done, it will set the inserter to finished.
     */
    public void setFinished() {
        isApplicationFinished = true;
    }

    /**
     * When the main app is done, the inserter needs to update the Poller
     * statistics table in Cacti.
     * NOTE - there's a bug in this, see below
     */
    public void recordPollerStats() {
        while (results.peek() != null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        long now = Calendar.getInstance().getTimeInMillis();
        java.sql.Timestamp nowTimeStamp = new java.sql.Timestamp(now);
        StringBuilder sb = new StringBuilder();
        sb.append("insert into poller_time (poller_id, start_time, end_time) values (0, '");
        sb.append(nowTimeStamp);
        sb.append("', '");
        sb.append(nowTimeStamp);
        sb.append("')");
        insertSql(sb.toString());
    }

    /**
     * CactiInserter run in its own Thread.
     */
    public void run() {
        PreparedStatement pStatement;
        StringBuilder sql = new StringBuilder("INSERT into poller_output ");
        sql.append("(local_data_id,rrd_name,time,output) VALUES(?,?,?,?)");
        try {
            pStatement = db.prepareStatement(sql.toString());
        } catch (SQLException e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Error creating prepared statement: " + e.getMessage());
            }
            return;
        }
        while (true) {
            if (results.peek() != null) {
                try {
                    while (results.peek() != null) {
                        IPollingItem item = results.poll();
                        pStatement.setInt(1, item.getId());
                        pStatement.setString(2, item.getDbName());
                        pStatement.setTimestamp(3, item.getTime());
                        pStatement.setString(4, item.getOutput());
                        if (logger.isEnabledFor(Level.INFO)) {
                            logger.info(pStatement.toString());
                        }
                        pStatement.addBatch();
                    }
                } catch (SQLException e) {
                    if (logger.isEnabledFor(Level.ERROR)) {
                        logger.error("Error adding batch statements: " + e.getMessage());
                    }
                    continue;
                }
                try {
                    pStatement.executeBatch();
                } catch (SQLException e) {
                    if (logger.isEnabledFor(Level.ERROR)) {
                        logger.error("Error executing batch: " + e.getMessage());
                    }
                }
            }
            if (isApplicationFinished) {
                try {
                    db.close();
                } catch (SQLException e) {
                    if (logger.isEnabledFor(Level.ERROR)) {
                        logger.error("Error closing database connection: " + e.getMessage());
                    }
                }
                return;
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    /**
     * We shouldn't need this, but I've been kinda lazy.  InsertSql allows
     * us to insert arbitrary SQL into the database.  Currently use it to
     * update the host table with the device availability/etc stuff, but once
     * we fix that this should go away.
     */
    public void insertSql(String sql) {
        try {
            Statement statement = db.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            if (logger.isEnabledFor(Level.WARN)) {
                logger.warn("Error inserting SQL: " + e.getMessage());
                logger.warn("SQL was: " + sql);
            }
        }
    }
}
