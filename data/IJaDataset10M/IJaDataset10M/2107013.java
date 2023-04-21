package org.adempiere.webui.apps;

import java.awt.*;
import java.io.*;
import java.lang.reflect.*;
import java.rmi.*;
import java.sql.*;
import java.util.logging.*;
import javax.swing.*;
import org.adempiere.webui.EnvWeb;
import org.compiere.db.*;
import org.compiere.interfaces.*;
import org.compiere.model.*;
import org.compiere.print.*;
import org.compiere.process.*;
import org.compiere.util.*;
import org.compiere.wf.*;
import org.compiere.apps.ADialog;
import org.compiere.apps.AWindow;
import org.compiere.apps.ProcessParameter;
import org.compiere.apps.Waiting;
import org.compiere.common.constants.*;

/**
 *	Process Interface Controller.
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: ProcessCtl.java,v 1.2 2006/07/30 00:51:27 jjanke Exp $
 */
public class ProcessCtl extends Thread {

    /**
	 *	Async Process - Do it all.
	 *  <code>
	 *	- Get Instance ID
	 *	- Get Parameters
	 *	- execute (lock - start process - unlock)
	 *  </code>
	 *  Creates a ProcessCtl instance, which calls
	 *  lockUI and unlockUI if parent is a ASyncProcess
	 *  <br>
	 *	Called from ProcessCtl.startProcess, ProcessDialog.actionPerformed,
	 *  APanel.cmd_print, APanel.actionButton, VPaySelect.cmd_generate
	 *
	 *  @param parent ASyncProcess & Container
	 *  @param WindowNo window no
	 *  @param pi ProcessInfo process info
	 *  @param p_trx Transaction
	 *  @return worker started ProcessCtl instance or null for workflow
	 */
    public static ProcessCtl process(ASyncProcess parent, int WindowNo, ProcessInfo pi, Trx p_trx) {
        log.fine("WindowNo=" + WindowNo + " - " + pi);
        MPInstance instance = null;
        try {
            instance = new MPInstance(EnvWeb.getCtx(), pi.getAD_Process_ID(), pi.getRecord_ID());
        } catch (Exception e) {
            pi.setSummary(e.getLocalizedMessage());
            pi.setError(true);
            log.warning(pi.toString());
            return null;
        } catch (Error e) {
            pi.setSummary(e.getLocalizedMessage());
            pi.setError(true);
            log.warning(pi.toString());
            return null;
        }
        if (!instance.save()) {
            pi.setSummary(Msg.getMsg(EnvWeb.getCtx(), "ProcessNoInstance"));
            pi.setError(true);
            return null;
        }
        pi.setAD_PInstance_ID(instance.getAD_PInstance_ID());
        ProcessParameter para = new ProcessParameter(Env.getFrame((Container) parent), WindowNo, pi);
        if (para.initDialog()) {
            para.setModal(false);
            para.getModalCtl(parent, pi, p_trx);
            para.setVisible(true);
            return null;
        } else {
            ProcessCtl worker = new ProcessCtl(parent, pi, p_trx);
            worker.start();
            return worker;
        }
    }

    /**************************************************************************
	 *  Constructor
	 *  @param parent Container & ASyncProcess
	 *  @param pi Process info
	 *  @param p_trx Transaction
	 *  Created in process(), VInvoiceGen.generateInvoices
	 */
    public ProcessCtl(ASyncProcess parent, ProcessInfo pi, Trx p_trx) {
        m_parent = parent;
        m_pi = pi;
        m_trx = p_trx;
    }

    /**	Parenr				*/
    ASyncProcess m_parent;

    /** Process Info		*/
    ProcessInfo m_pi;

    private Trx m_trx;

    private Waiting m_waiting;

    private boolean m_IsServerProcess = false;

    /**	Static Logger	*/
    static CLogger log = CLogger.getCLogger(ProcessCtl.class);

    /**
	 *	Execute Process Instance and Lock UI.
	 *  Calls lockUI and unlockUI if parent is a ASyncProcess
	 *  <pre>
	 *		- Get Process Information
	 *      - Call Class
	 *		- Submit SQL Procedure
	 *		- Run SQL Procedure
	 *	</pre>
	 */
    @Override
    public void run() {
        log.fine("AD_PInstance_ID=" + m_pi.getAD_PInstance_ID() + ", Record_ID=" + m_pi.getRecord_ID());
        lock();
        String ProcedureName = "";
        int AD_ReportView_ID = 0;
        int AD_Workflow_ID = 0;
        boolean IsReport = false;
        boolean IsDirectPrint = false;
        String sql = "SELECT p.Name, p.ProcedureName,p.Classname, p.AD_Process_ID," + " p.IsReport,p.IsDirectPrint,p.AD_ReportView_ID,p.AD_Workflow_ID," + " CASE WHEN COALESCE(p.Statistic_Count,0)=0 THEN 0 ELSE p.Statistic_Seconds/p.Statistic_Count END CASE," + " p.IsServerProcess " + "FROM AD_Process p" + " INNER JOIN AD_PInstance i ON (p.AD_Process_ID=i.AD_Process_ID) " + "WHERE p.IsActive='Y'" + " AND i.AD_PInstance_ID=?";
        if (!Env.isBaseLanguage(EnvWeb.getCtx(), "AD_Process")) sql = "SELECT t.Name, p.ProcedureName,p.Classname, p.AD_Process_ID," + " p.IsReport, p.IsDirectPrint,p.AD_ReportView_ID,p.AD_Workflow_ID," + " CASE WHEN COALESCE(p.Statistic_Count,0)=0 THEN 0 ELSE p.Statistic_Seconds/p.Statistic_Count END CASE," + " p.IsServerProcess " + "FROM AD_Process p" + " INNER JOIN AD_PInstance i ON (p.AD_Process_ID=i.AD_Process_ID) " + " INNER JOIN AD_Process_Trl t ON (p.AD_Process_ID=t.AD_Process_ID" + " AND t.AD_Language='" + Env.getAD_Language(EnvWeb.getCtx()) + "') " + "WHERE p.IsActive='Y'" + " AND i.AD_PInstance_ID=?";
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, null);
            pstmt.setInt(1, m_pi.getAD_PInstance_ID());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                m_pi.setTitle(rs.getString(1));
                if (m_waiting != null) m_waiting.setTitle(m_pi.getTitle());
                ProcedureName = rs.getString(2);
                m_pi.setClassName(rs.getString(3));
                m_pi.setAD_Process_ID(rs.getInt(4));
                if ("Y".equals(rs.getString(5))) {
                    IsReport = true;
                    if ("Y".equals(rs.getString(6)) && !Ini.isPropertyBool(Ini.P_PRINTPREVIEW)) IsDirectPrint = true;
                }
                IsDirectPrint = true;
                AD_ReportView_ID = rs.getInt(7);
                AD_Workflow_ID = rs.getInt(8);
                int estimate = rs.getInt(9);
                if (estimate != 0) {
                    m_pi.setEstSeconds(estimate + 1);
                    if (m_waiting != null) m_waiting.setTimerEstimate(m_pi.getEstSeconds());
                }
                m_IsServerProcess = "Y".equals(rs.getString(10));
            } else log.log(Level.SEVERE, "No AD_PInstance_ID=" + m_pi.getAD_PInstance_ID());
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            m_pi.setSummary(Msg.getMsg(EnvWeb.getCtx(), "ProcessNoProcedure") + " " + e.getLocalizedMessage(), true);
            unlock();
            log.log(Level.SEVERE, "run", e);
            return;
        }
        if (ProcedureName == null) ProcedureName = "";
        if (AD_Workflow_ID > 0) {
            startWorkflow(AD_Workflow_ID);
            unlock();
            return;
        }
        if (m_pi.getClassName() != null) {
            if (!startProcess()) {
                unlock();
                return;
            }
            if (!IsReport && ProcedureName.length() == 0) {
                unlock();
                return;
            }
            if (IsReport && AD_ReportView_ID == 0) {
                unlock();
                return;
            }
        }
        if (!IsReport && ProcedureName.length() == 0) {
            m_pi.setSummary(Msg.getMsg(EnvWeb.getCtx(), "ProcessNoProcedure"), true);
            unlock();
            return;
        }
        if (IsReport) {
            if (ProcedureName.length() > 0) {
                if (!startDBProcess(ProcedureName)) {
                    unlock();
                    return;
                }
            }
            ReportEngine re = ReportCtl.start(EnvWeb.getCtx(), m_pi, IsDirectPrint);
            if (m_pi.getAD_Process_ID() == 313 && re == null) ADialog.error(0, null, GlobalMessageConstants.PAYMENT_NOT_COMPLETED);
            if (re != null && !IsDirectPrint) new Viewer(re);
            m_pi.setSummary("Report", re != null);
            unlock();
        } else {
            if (!startDBProcess(ProcedureName)) {
                unlock();
                return;
            }
            ProcessInfoUtil.setSummaryFromDB(m_pi);
            unlock();
        }
    }

    /**
	 *  Lock UI & show Waiting
	 */
    private void lock() {
        if (m_parent == null) return;
        JFrame frame = Env.getFrame((Container) m_parent);
        if (frame instanceof AWindow) ((AWindow) frame).setBusyTimer(m_pi.getEstSeconds());
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                log.finer("lock");
                if (m_parent != null) m_parent.lockUI(m_pi);
            }
        });
        if (m_waiting != null) {
            m_waiting.toFront();
            m_waiting.setVisible(true);
        }
    }

    /**
	 *  Unlock UI & dispose Waiting.
	 * 	Called from run()
	 */
    private void unlock() {
        if (m_pi.isBatch()) m_pi.setIsTimeout(true);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                String summary = m_pi.getSummary();
                log.finer(summary);
                if (summary != null && summary.indexOf("@") != -1) m_pi.setSummary(Msg.parseTranslation(EnvWeb.getCtx(), summary));
                if (m_parent != null) m_parent.unlockUI(m_pi);
            }
        });
        if (m_waiting != null) m_waiting.dispose();
        m_waiting = null;
    }

    /**************************************************************************
	 *  Start Workflow.
	 *
	 *  @param AD_Workflow_ID workflow
	 *  @return     true if started
	 */
    private boolean startWorkflow(int AD_Workflow_ID) {
        log.fine(AD_Workflow_ID + " - " + m_pi);
        boolean started = false;
        if (DB.isRemoteProcess()) {
            Server server = CConnection.get().getServer();
            try {
                if (server != null) {
                    m_pi = server.workflow(EnvWeb.getCtx(), m_pi, AD_Workflow_ID);
                    log.finest("server => " + m_pi);
                    started = true;
                }
            } catch (RemoteException ex) {
                log.log(Level.SEVERE, "AppsServer error", ex);
                started = false;
            }
        }
        if (!started && !m_IsServerProcess) {
            MWorkflow wf = MWorkflow.get(EnvWeb.getCtx(), AD_Workflow_ID);
            MWFProcess wfProcess = null;
            if (m_pi.isBatch()) wfProcess = wf.start(m_pi); else wfProcess = wf.startWait(m_pi);
            started = wfProcess != null;
        }
        return started;
    }

    /**************************************************************************
	 *  Start Java Process Class.
	 *      instanciate the class implementing the interface ProcessCall.
	 *  The class can be a Server/Client class (when in Package
	 *  org compiere.process or org.compiere.model) or a client only class
	 *  (e.g. in org.compiere.report)
	 *
	 *  @return     true if success
	 */
    private boolean startProcess() {
        log.fine(m_pi.toString());
        boolean started = false;
        if (DB.isRemoteProcess()) {
            Server server = CConnection.get().getServer();
            try {
                if (server != null) {
                    m_pi = server.process(EnvWeb.getCtx(), m_pi);
                    log.finest("server => " + m_pi);
                    started = true;
                }
            } catch (UndeclaredThrowableException ex) {
                Throwable cause = ex.getCause();
                if (cause != null) {
                    if (cause instanceof InvalidClassException) log.log(Level.SEVERE, "Version Server <> Client: " + cause.toString() + " - " + m_pi, ex); else log.log(Level.SEVERE, "AppsServer error(1b): " + cause.toString() + " - " + m_pi, ex);
                } else log.log(Level.SEVERE, " AppsServer error(1) - " + m_pi, ex);
                started = false;
            } catch (RemoteException ex) {
                Throwable cause = ex.getCause();
                if (cause == null) cause = ex;
                log.log(Level.SEVERE, "AppsServer error - " + m_pi, cause);
                started = false;
            }
        }
        if (!started && !m_IsServerProcess) {
            ProcessCall myObject = null;
            try {
                Class<?> myClass = Class.forName(m_pi.getClassName());
                myObject = (ProcessCall) myClass.newInstance();
                if (myObject == null) m_pi.setSummary("No Instance for " + m_pi.getClassName(), true); else myObject.startProcess(EnvWeb.getCtx(), m_pi, m_trx);
                if (m_trx != null) {
                    m_trx.commit();
                    log.fine("Commit " + m_trx.toString());
                    m_trx.close();
                }
            } catch (Exception e) {
                if (m_trx != null) {
                    m_trx.rollback();
                    log.fine("Rollback " + m_trx.toString());
                    m_trx.close();
                }
                m_pi.setSummary("Error starting Class " + m_pi.getClassName(), true);
                log.log(Level.SEVERE, m_pi.getClassName(), e);
            }
        }
        return !m_pi.isError();
    }

    /**************************************************************************
	 *  Start Database Process
	 *  @param ProcedureName PL/SQL procedure name
	 *  @return true if success
	 */
    private boolean startDBProcess(String ProcedureName) {
        if (DB.isDB2()) {
            log.warning("DB Procedures not supported here: " + ProcedureName);
            return false;
        }
        log.fine(ProcedureName + "(" + m_pi.getAD_PInstance_ID() + ")");
        String sql = "{call " + ProcedureName + "(?)}";
        try {
            org.compierezk.util.DB.executeCall(sql, m_pi.getAD_PInstance_ID());
        } catch (Exception e) {
            log.log(Level.SEVERE, sql, e);
            m_pi.setSummary(Msg.getMsg(EnvWeb.getCtx(), "ProcessRunError") + " " + e.getLocalizedMessage());
            m_pi.setError(true);
            return false;
        }
        return true;
    }
}
