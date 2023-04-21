package com.knowgate.scheduler;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.LinkedList;
import java.util.ListIterator;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import com.knowgate.debug.DebugFile;
import com.knowgate.dataobjs.DBBind;
import com.knowgate.jdc.JDCConnection;

/**
 * <p>Scheduler daemon</p>
 * <p>Keeps a thread pool and an atom queue for feeding the pool.</p>
 * @author Sergio Montoro Ten
 * @version 1.0
 */
public class SchedulerDaemon extends Thread {

    private boolean bContinue;

    private JDCConnection oCon;

    private WorkerThreadPool oThreadPool;

    private AtomQueue oQue = new AtomQueue();

    private Properties oEnvProps;

    private LinkedList oCallbacks;

    private static class SystemOutNotify extends WorkerThreadCallback {

        public SystemOutNotify() {
            super("SystemOutNotify");
        }

        public void call(String sThreadId, int iOpCode, String sMessage, Exception oXcpt, Object oParam) {
            if (WorkerThreadCallback.WT_EXCEPTION == iOpCode) System.out.println("Thread " + sThreadId + ": ERROR " + sMessage); else System.out.println("Thread " + sThreadId + ": " + sMessage);
        }
    }

    /**
   * <p>Create new SchedulerDaemon</p>
   * @param sPropertiesFilePath Full path to hipergate.cnf file.<br>
   * Constructor will read the following properties from hipergate.cnf:<br>
   * <b>driver</b> JDBC driver class<br>
   * <b>dburl</b> URL for database connection<br>
   * <b>dbuser</b> Database User<br>
   * <b>dbpassword</b> Database User Password<br>
   * @throws ClassNotFoundException
   * @throws IOException
   * @throws SQLException
   */
    public SchedulerDaemon(String sPropertiesFilePath) throws ClassNotFoundException, IOException, SQLException {
        oThreadPool = null;
        bContinue = true;
        FileInputStream oInProps = new FileInputStream(sPropertiesFilePath);
        oEnvProps = new Properties();
        oEnvProps.load(oInProps);
        oInProps.close();
        Class oDriver = Class.forName(oEnvProps.getProperty("driver"));
        oCallbacks = new LinkedList();
    }

    public AtomQueue atomQueue() {
        return oQue;
    }

    public WorkerThreadPool threadPool() {
        return oThreadPool;
    }

    /**
   * <p>Create AtomQueue and start WorkerThreadPool</p>
   */
    public void run() {
        Statement oStmt;
        ResultSet oRSet;
        int iJobCount;
        try {
            if (DebugFile.trace) DebugFile.writeln("new JDCConnection(" + oEnvProps.getProperty("dburl") + "," + oEnvProps.getProperty("dbuser") + ", ...)");
            oCon = new JDCConnection(DriverManager.getConnection(oEnvProps.getProperty("dburl"), oEnvProps.getProperty("dbuser"), oEnvProps.getProperty("dbpassword")), null);
            if (DebugFile.trace) DebugFile.writeln("JDCConnection.setAutoCommit(true)");
            oCon.setAutoCommit(true);
            if (DebugFile.trace) DebugFile.writeln("new AtomQueue()");
            oQue = new AtomQueue();
            if (DebugFile.trace) DebugFile.writeln("new AtomFeeder()");
            AtomFeeder oFdr = new AtomFeeder();
            if (DebugFile.trace) DebugFile.writeln("new AtomConsumer([JDCconnection], [AtomQueue])");
            AtomConsumer oCsr = new AtomConsumer(oCon, oQue);
            if (DebugFile.trace) DebugFile.writeln("new WorkerThreadPool([AtomConsumer], [Properties])");
            oThreadPool = new WorkerThreadPool(oCsr, oEnvProps);
            ListIterator oIter = oCallbacks.listIterator();
            while (oIter.hasNext()) oThreadPool.registerCallback((WorkerThreadCallback) oIter.next());
            do {
                try {
                    while (bContinue) {
                        oStmt = oCon.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                        try {
                            oStmt.setQueryTimeout(20);
                        } catch (SQLException sqle) {
                        }
                        if (DebugFile.trace) DebugFile.writeln("Statement.executeQuery(SELECT COUNT(*) FROM k_jobs WHERE id_status=" + String.valueOf(Job.STATUS_PENDING) + ")");
                        oRSet = oStmt.executeQuery("SELECT COUNT(*) FROM k_jobs WHERE id_status=" + String.valueOf(Job.STATUS_PENDING));
                        oRSet.next();
                        iJobCount = oRSet.getInt(1);
                        oRSet.close();
                        oStmt.close();
                        if (DebugFile.trace) DebugFile.writeln(String.valueOf(iJobCount) + " pending jobs");
                        if (0 == iJobCount) sleep(10000); else break;
                    }
                    if (bContinue) {
                        oFdr.loadAtoms(oCon, oThreadPool.size());
                        oFdr.feedQueue(oCon, oQue);
                        if (oQue.size() > 0) oThreadPool.launchAll();
                        do {
                            sleep(10000);
                            if (DebugFile.trace) DebugFile.writeln(String.valueOf(oThreadPool.livethreads()) + " live threads");
                        } while (oThreadPool.livethreads() == oThreadPool.size());
                    }
                } catch (InterruptedException e) {
                    if (DebugFile.trace) DebugFile.writeln("SchedulerDaemon InterruptedException " + e.getMessage());
                }
            } while (bContinue);
            if (DebugFile.trace) DebugFile.writeln(" exiting SchedulerDaemon");
            oThreadPool = null;
            oCsr.close();
            oCsr = null;
            oFdr = null;
            oQue = null;
            if (DebugFile.trace) DebugFile.writeln("JDConnection.close()");
            oCon.close();
            oCon = null;
        } catch (SQLException e) {
            try {
                if (oCon != null) if (!oCon.isClosed()) oCon.close();
            } catch (SQLException sqle) {
                if (DebugFile.trace) DebugFile.writeln("SchedulerDaemon SQLException on close() " + sqle.getMessage());
            }
            oCon = null;
            if (DebugFile.trace) DebugFile.writeln("SchedulerDaemon SQLException " + e.getMessage());
        }
    }

    public void registerCallback(WorkerThreadCallback oNewCallback) throws IllegalArgumentException {
        if (oThreadPool == null) oCallbacks.addLast(oNewCallback); else oThreadPool.registerCallback(oNewCallback);
    }

    public void unregisterCallback(String sCallbackName) {
        if (oThreadPool != null) oThreadPool.unregisterCallback(sCallbackName);
    }

    /**
   * <p>Stop worker threads</p>
   * @throws IllegalStateException If worker threads are not running
   */
    public void stopAll() throws IllegalStateException {
        if (null == oThreadPool) throw new IllegalStateException("Thread pool not initialized, call start() method before trying to stop worker threads");
        oThreadPool.haltAll();
        try {
            sleep(4000);
        } catch (InterruptedException e) {
        } finally {
            bContinue = false;
        }
        oThreadPool.stopAll();
    }

    private static void printUsage() {
        System.out.println("");
        System.out.println("Usage:");
        System.out.println("SchedulerDaemon cnf_file_path [verbose]");
    }

    public static void main(String[] argv) throws ClassNotFoundException, SQLException, IOException {
        DBBind oGlobalDBBind = new DBBind();
        SchedulerDaemon TheDaemon;
        if (argv.length < 1 || argv.length > 2) printUsage(); else if (argv.length == 2 && !argv[1].equals("verbose")) printUsage(); else {
            TheDaemon = new SchedulerDaemon(argv[0]);
            if (argv.length == 2) TheDaemon.registerCallback(new SystemOutNotify());
            TheDaemon.start();
        }
    }
}
