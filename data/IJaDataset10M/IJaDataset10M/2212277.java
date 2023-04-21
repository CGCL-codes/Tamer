package net.sourceforge.oradoc.jdbc;

import java.sql.*;

/**
 * This class defines the <code>executeQuery()</code> method, which is similar
 * to <code>Statement.executeQuery()</code> method. The difference is that
 * this method can be interrupted by another thread.
 */
public class QueryExecutor implements Runnable {

    private Thread worker;

    private Params params;

    private Results results;

    private volatile boolean cancelRequest;

    private volatile boolean closeRequest;

    private class Params {

        public Statement statement;

        public String query;

        public boolean pending;
    }

    private class Results {

        public ResultSet rs;

        public SQLException exception;

        public boolean serviced;
    }

    public QueryExecutor() {
        params = new Params();
        results = new Results();
        worker = new Thread(this);
        worker.start();
    }

    /**
   * Executes an SQL query. The method can be interrupted by another thread at
   * any moment.
   * 
   * @return <code>ResultSet</code> if execution successful
   * @exception SQLException
   *              if a database error occurs
   * @exception InterruptedException
   *              if interrupted by another thread
   */
    public synchronized ResultSet executeQuery(Statement statement, String query) throws SQLException, InterruptedException {
        synchronized (params) {
            params.statement = statement;
            params.query = query;
            params.pending = true;
            params.notify();
        }
        synchronized (results) {
            try {
                while (!results.serviced) {
                    results.wait();
                }
                if (results.exception != null) {
                    throw results.exception;
                }
            } catch (InterruptedException e) {
                cancel();
                throw e;
            } finally {
                results.serviced = false;
            }
            return results.rs;
        }
    }

    private void cancel() {
        cancelRequest = true;
        try {
            params.statement.cancel();
            synchronized (results) {
                while (!results.serviced) {
                    results.wait();
                }
            }
        } catch (SQLException e) {
            return;
        } catch (InterruptedException e) {
            return;
        } finally {
            cancelRequest = false;
        }
    }

    public void close() {
        closeRequest = true;
        worker.interrupt();
        try {
            worker.join();
        } catch (InterruptedException e) {
        }
    }

    public void run() {
        ResultSet rs = null;
        SQLException ex = null;
        while (!closeRequest) {
            synchronized (params) {
                try {
                    while (!params.pending) {
                        params.wait();
                    }
                    params.pending = false;
                } catch (InterruptedException e) {
                    if (closeRequest) {
                        return;
                    }
                }
                try {
                    if (params.statement instanceof PreparedStatement) {
                        rs = ((PreparedStatement) params.statement).executeQuery();
                    } else if (params.statement instanceof CallableStatement) {
                        rs = ((CallableStatement) params.statement).executeQuery();
                    } else {
                        rs = params.statement.executeQuery(params.query);
                    }
                } catch (SQLException e) {
                    if (!cancelRequest) {
                        ex = e;
                    }
                }
            }
            synchronized (results) {
                results.rs = rs;
                results.exception = ex;
                results.serviced = true;
                results.notify();
            }
        }
    }
}
