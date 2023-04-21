package com.knowgate.scheduler;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.mail.MessagingException;
import com.knowgate.jdc.JDCConnection;
import com.knowgate.dataobjs.DB;
import com.knowgate.dataobjs.DBBind;
import com.knowgate.dataobjs.DBSubset;
import com.knowgate.misc.Gadgets;

/**
 * <p>Single Thread Scheduler Executor</p>
 * <p>SingleThreadExecutor is a class that processes jobs and atoms in a simple way,
 * unlike SchedulerDaemon witch is based on an AtomQueue and a WorkerThreadPool,
 * SingleThreadExecutor uses directly the database for tracking execution progress
 * for a single thread.</p>
 * @author Sergio Montoro Ten
 * @version 1.0
 */
public class SingleThreadExecutor extends Thread {

    private String sEnvProps;

    private Properties oEnvProps;

    private boolean bContinue;

    private String sLastError;

    private String sJob;

    private Job oJob;

    private Atom oAtm;

    private LinkedList oCallbacks;

    private int iCallbacks;

    private static class SystemOutNotify extends WorkerThreadCallback {

        public SystemOutNotify() {
            super("SystemOutNotify");
        }

        public void call(String sThreadId, int iOpCode, String sMessage, Exception oXcpt, Object oParam) {
            if (WorkerThreadCallback.WT_EXCEPTION == iOpCode) System.out.println("Thread " + sThreadId + ": ERROR " + sMessage); else System.out.println("Thread " + sThreadId + ": " + sMessage);
        }
    }

    /**
   * <p>Create new SingleThreadExecutor</p>
   * @param sPropertiesFilePath Absolute path to hipergate.cnf properties file
   * @throws FileNotFoundException
   * @throws IOException
   */
    public SingleThreadExecutor(String sPropertiesFilePath) throws FileNotFoundException, IOException {
        sJob = null;
        bContinue = true;
        if (sPropertiesFilePath.lastIndexOf(System.getProperty("file.separator")) == -1) sEnvProps = sPropertiesFilePath; else sEnvProps = sPropertiesFilePath.substring(sPropertiesFilePath.lastIndexOf(System.getProperty("file.separator")) + 1);
        FileInputStream oInProps = new FileInputStream(sPropertiesFilePath);
        oEnvProps = new Properties();
        oEnvProps.load(oInProps);
        oInProps.close();
        oCallbacks = new LinkedList();
    }

    /**
   * <p>Create new SingleThreadExecutor for a single Job</p>
   * @param sPropertiesFilePath Absolute path to hipergate.cnf properties file
   * @throws FileNotFoundException
   * @throws IOException
   */
    public SingleThreadExecutor(String sPropertiesFilePath, String sJobId) throws FileNotFoundException, IOException {
        sJob = sJobId;
        bContinue = true;
        if (sPropertiesFilePath.lastIndexOf(System.getProperty("file.separator")) == -1) sEnvProps = sPropertiesFilePath; else sEnvProps = sPropertiesFilePath.substring(sPropertiesFilePath.lastIndexOf(System.getProperty("file.separator")) + 1);
        FileInputStream oInProps = new FileInputStream(sPropertiesFilePath);
        oEnvProps = new Properties();
        oEnvProps.load(oInProps);
        oInProps.close();
        oCallbacks = new LinkedList();
    }

    public Atom activeAtom() {
        return oAtm;
    }

    public Job activeJob() {
        return oJob;
    }

    public String lastError() {
        return sLastError;
    }

    /**
   * Register a thread callback object
   * @param oNewCallback WorkerThreadCallback subclass instance
   * @throws IllegalArgumentException If a callback with same name has oNewCallback was already registered
   */
    public void registerCallback(WorkerThreadCallback oNewCallback) throws IllegalArgumentException {
        WorkerThreadCallback oCallback;
        ListIterator oIter = oCallbacks.listIterator();
        while (oIter.hasNext()) {
            oCallback = (WorkerThreadCallback) oIter.next();
            if (oCallback.name().equals(oNewCallback.name())) {
                throw new IllegalArgumentException("Callback " + oNewCallback.name() + " is already registered");
            }
        }
        oCallbacks.addLast(oNewCallback);
        iCallbacks++;
    }

    /**
   * Unregister a thread callback object
   * @param sCallbackName Name of callback to be unregistered
   * @return <b>true</b> if a callback with such name was found and unregistered,
   * <b>false</b> otherwise
   */
    public boolean unregisterCallback(String sCallbackName) {
        WorkerThreadCallback oCallback;
        ListIterator oIter = oCallbacks.listIterator();
        while (oIter.hasNext()) {
            oCallback = (WorkerThreadCallback) oIter.next();
            if (oCallback.name().equals(sCallbackName)) {
                oIter.remove();
                iCallbacks--;
                return true;
            }
        }
        return false;
    }

    private void callBack(int iOpCode, String sMessage, Exception oXcpt, Object oParam) {
        WorkerThreadCallback oCallback;
        ListIterator oIter = oCallbacks.listIterator();
        while (oIter.hasNext()) {
            oCallback = (WorkerThreadCallback) oIter.next();
            oCallback.call(getName(), iOpCode, sMessage, oXcpt, oParam);
        }
    }

    public void run() {
        Statement oStm;
        JDCConnection oCon;
        AtomFeeder oFdr;
        DBSubset oDBS;
        String sSQL;
        String sJId;
        Job oJob = null;
        Atom oAtm;
        ResultSet oRst;
        ResultSetMetaData oMDt;
        DBBind oDBB = null;
        try {
            oDBB = new DBBind(sEnvProps);
            oCon = new JDCConnection(DriverManager.getConnection(oEnvProps.getProperty("dburl"), oEnvProps.getProperty("dbuser"), oEnvProps.getProperty("dbpassword")), null);
            bContinue = true;
            sLastError = "";
            while (bContinue) {
                oFdr = new AtomFeeder();
                if (sJob == null) oDBS = oFdr.loadAtoms(oCon, 1); else oDBS = oFdr.loadAtoms(oCon, sJob);
                if (oDBS.getRowCount() > 0) {
                    sJId = oDBS.getString(0, 0);
                    oJob = Job.instantiate(oCon, sJId, oEnvProps);
                    oStm = oCon.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                    sSQL = "SELECT a.*, j." + DB.tx_parameters + " FROM " + DB.k_job_atoms + " a, " + DB.k_jobs + " j WHERE a." + DB.id_status + "=" + String.valueOf(Atom.STATUS_PENDING) + " AND j." + DB.gu_job + "=a." + DB.gu_job + " AND j." + DB.gu_job + "='" + sJId + "'";
                    oRst = oStm.executeQuery(sSQL);
                    oMDt = oRst.getMetaData();
                    while (oRst.next()) {
                        oAtm = new Atom(oRst, oMDt);
                        oJob.process(oAtm);
                    }
                    oRst.close();
                    oStm.close();
                } else bContinue = false;
            }
            oCon.close();
            oDBB.close();
        } catch (MessagingException e) {
            oDBB.close();
            sLastError = "MessagingException " + e.getMessage();
            if (iCallbacks > 0) callBack(-1, sLastError, new MessagingException(e.getMessage(), e.getNextException()), null);
            if (oJob != null) oJob.log(sLastError + "\n");
        } catch (SQLException e) {
            if (null != oDBB) oDBB.close();
            sLastError = "SQLException " + e.getMessage();
            if (iCallbacks > 0) callBack(-1, sLastError, new SQLException(e.getMessage(), e.getSQLState(), e.getErrorCode()), null);
            if (oJob != null) oJob.log(sLastError + "\n");
        } catch (FileNotFoundException e) {
            if (null != oDBB) oDBB.close();
            sLastError = "FileNotFoundException " + e.getMessage();
            if (iCallbacks > 0) callBack(-1, sLastError, new FileNotFoundException(e.getMessage()), null);
            if (oJob != null) oJob.log(sLastError + "\n");
        } catch (IOException e) {
            if (null != oDBB) oDBB.close();
            sLastError = "IOException " + e.getMessage();
            if (iCallbacks > 0) callBack(-1, sLastError, new IOException(e.getMessage()), null);
            if (oJob != null) oJob.log(sLastError + "\n");
        } catch (ClassNotFoundException e) {
            if (null != oDBB) oDBB.close();
            sLastError = "ClassNotFoundException " + e.getMessage();
            if (iCallbacks > 0) callBack(-1, sLastError, new ClassNotFoundException(e.getMessage()), null);
            if (oJob != null) oJob.log(sLastError + "\n");
        } catch (InstantiationException e) {
            if (null != oDBB) oDBB.close();
            sLastError = "InstantiationException " + e.getMessage();
            if (iCallbacks > 0) callBack(-1, sLastError, new InstantiationException(e.getMessage()), null);
            if (oJob != null) oJob.log(sLastError + "\n");
        } catch (IllegalAccessException e) {
            if (null != oDBB) oDBB.close();
            sLastError = "IllegalAccessException " + e.getMessage();
            if (iCallbacks > 0) callBack(-1, sLastError, new IllegalAccessException(e.getMessage()), null);
            if (oJob != null) oJob.log(sLastError + "\n");
        } catch (NullPointerException e) {
            if (null != oDBB) oDBB.close();
            sLastError = "NullPointerException " + e.getMessage();
            if (iCallbacks > 0) callBack(-1, sLastError, new NullPointerException(e.getMessage()), null);
            if (oJob != null) oJob.log(sLastError + "\n");
        }
    }

    /**
   * <p>Halt thread execution commiting all operations in course before stopping</p>
   * If a thread is dead-locked by any reason halting it will not cause any effect.<br>
   * halt() method only sends a signals to the each WokerThread telling it that must
   * finish pending operations and stop.
   */
    public void halt() {
        bContinue = false;
    }

    private static void printUsage() {
        System.out.println("");
        System.out.println("Usage:");
        System.out.println("SingleThreadExecutor {run | lrun} job_type cnf_file_path {gu_job | xml_file_path} [verbose]");
        System.out.println("job_type is one of {MAIL | FAX | SAVE | FTP}");
    }

    public static void main(String[] argv) throws java.io.FileNotFoundException, java.io.IOException, SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException, org.xml.sax.SAXException {
        SingleThreadExecutor oExec;
        if (argv.length != 4 && argv.length != 5) printUsage(); else if (argv.length == 5 && !argv[4].equals("verbose")) printUsage(); else if (!argv[0].equals("run") && !argv[0].equals("lrun")) printUsage(); else if (!argv[1].equalsIgnoreCase("MAIL") && !argv[1].equalsIgnoreCase("FAX") && !argv[1].equalsIgnoreCase("SAVE") && !argv[1].equalsIgnoreCase("FTP")) printUsage(); else {
            if (argv[0].equals("run")) oExec = new SingleThreadExecutor(argv[2], argv[3]); else {
                String sJobGUID = Gadgets.generateUUID();
                Job.main(new String[] { "create", argv[1], argv[2], argv[3], sJobGUID });
                oExec = new SingleThreadExecutor(argv[2], sJobGUID);
            }
            if (argv.length == 5) oExec.registerCallback(new SystemOutNotify());
            oExec.start();
        }
    }
}
