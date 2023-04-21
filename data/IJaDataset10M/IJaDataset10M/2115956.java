package com.continuent.bristlecone.benchmark.scenarios;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import com.continuent.bristlecone.benchmark.db.SqlDialect;
import com.continuent.bristlecone.benchmark.db.Table;

/**
 * Perform updates on a table in a manner designed to provoke deadlocks. 
 * This scenario performance updates on arbitrary table rows 
 * 
 * @author rhodges
 */
public class DeadlockScenario extends ScenarioBase {

    private static Logger logger = Logger.getLogger(DeadlockScenario.class);

    private int operations = 1;

    private long delaymillis = 0;

    private boolean autocommit = false;

    private String tag;

    private int execCount;

    protected PreparedStatement[] pstmtArray;

    /** 
   * Set the number of operations per transaction.  Must be at least
   * 2 to trigger deadlocks. 
   */
    public void setOperations(int operations) {
        this.operations = operations;
    }

    /**
   * Set the number of milliseconds to delay between transactions.  0 
   * means no delay.  
   */
    public void setDelaymillis(long delaymillis) {
        this.delaymillis = delaymillis;
    }

    /** Determine whether to use autocommit or actual transactions. */
    public void setAutocommit(boolean autocommit) {
        this.autocommit = autocommit;
    }

    /** Create a prepared statement array. */
    public void prepare() throws Exception {
        tag = Thread.currentThread().getName();
        SqlDialect dialect = helper.getSqlDialect();
        Table tables[] = tableSet.getTables();
        pstmtArray = new PreparedStatement[tables.length];
        for (int i = 0; i < tables.length; i++) {
            String sql = dialect.getUpdateByKey(tables[i]);
            pstmtArray[i] = conn.prepareStatement(sql);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Tag for this scenario: " + tag);
            logger.debug("Operations: " + operations);
            logger.debug("Delay millis: " + delaymillis);
            logger.debug("Autocommit setting:" + autocommit);
        }
    }

    /** Execute an interation. */
    public void iterate(long iterationCount) throws Exception {
        conn.setAutoCommit(autocommit);
        try {
            for (int i = 0; i < operations; i++) {
                int index = (int) (Math.random() * pstmtArray.length);
                int key = (int) (Math.random() * this.datarows);
                PreparedStatement pstmt = pstmtArray[index];
                if (i > 0 && delaymillis > 0) {
                    Thread.sleep(delaymillis);
                    if (logger.isDebugEnabled()) logger.debug("Delayed between transactions");
                }
                pstmt.setInt(1, execCount++);
                pstmt.setString(2, tag);
                pstmt.setInt(3, key);
                pstmt.execute();
                if (logger.isDebugEnabled()) {
                    logger.debug("Updated row: table=" + tableSet.getTables()[index].getName() + " key=" + key + " i=" + i + " tag=" + tag);
                }
            }
            if (!autocommit) {
                conn.commit();
                if (logger.isDebugEnabled()) {
                    logger.debug("Committing transaction");
                }
            }
        } catch (SQLException e) {
            if (!autocommit) conn.rollback();
            if (logger.isDebugEnabled()) logger.debug("Operation failed with SQL exception: " + e.getMessage());
            throw e;
        }
    }

    /** Clean up resources used by scenario. */
    public void cleanup() throws Exception {
        for (int i = 0; i < pstmtArray.length; i++) pstmtArray[i].close();
        if (conn != null) conn.close();
    }
}
