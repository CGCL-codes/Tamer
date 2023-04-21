package org.compiere.process;

import java.sql.*;
import java.util.logging.*;
import org.compiere.util.*;
import org.compiere.model.*;

/**
 *	Compiere Server Base
 *	
 *  @author Jorg Janke
 *  @version $Id: CompiereServer.java,v 1.2 2006/07/30 00:54:44 jjanke Exp $
 */
public abstract class CompiereServer extends Thread {

    /**
	 * 	CompiereServer
	 *	@param name server name
	 */
    public CompiereServer(String name) {
        super(s_threadGroup, name);
    }

    /**	Thread Group			*/
    private static ThreadGroup s_threadGroup = new ThreadGroup("CompiereServer");

    /**	Logger					*/
    protected CLogger log = CLogger.getCLogger(getClass());

    /** Working Status			*/
    private volatile boolean m_working = false;

    /** Working Count			*/
    private int m_count = 0;

    /** Poll Count				*/
    private int m_pollCount = 0;

    /** Working Time (ms)		*/
    private volatile int m_time = 0;

    /** Work Start				*/
    private volatile long m_start = 0;

    /** Last Work Start			*/
    private volatile long m_lastStart = 0;

    /**	Sleep Seconds			*/
    private int m_sleepSeconds = 10;

    /**	Processor Instance		*/
    protected PO p_processor = null;

    /** Server can continue		*/
    private boolean m_canContinue = true;

    /**
	 * 	Is Working
	 *	@return true if working
	 */
    public boolean isWorking() {
        return m_working;
    }

    /**
	 * 	Get Poll Count
	 *	@return number of polls
	 */
    public int getPollCount() {
        return m_pollCount;
    }

    /**
	 * 	Get Work Count
	 *	@return number of work runs
	 */
    public int getWorkCount() {
        return m_count;
    }

    /**
	 * 	Get Working Time
	 *	@return working time in ms
	 */
    public int getWorkTime() {
        return m_time;
    }

    /**
	 * 	Get Start of Server
	 *	@return start of server
	 */
    public Timestamp getStart() {
        if (m_start == 0) return null;
        return new Timestamp(m_start);
    }

    /**
	 * 	Get Last Start of Server
	 *	@return last start of server
	 */
    public Timestamp getLastStart() {
        if (m_lastStart == 0) return null;
        return new Timestamp(m_lastStart);
    }

    /**
	 * 	Get Sleep Seconds
	 * 	@return sleep seconds
	 */
    public int getSleepSeconds() {
        return m_sleepSeconds;
    }

    /**
	 * 	Set Sleep Seconds
	 *	@param sleepSeconds sleep seconds
	 */
    public void setSleepSeconds(int sleepSeconds) {
        m_sleepSeconds = sleepSeconds;
    }

    /**
	 * 	Set Server Processor
	 *	@param processor processor
	 */
    public void setProcessor(PO processor) {
        p_processor = processor;
        setName(getProcessorName());
    }

    /**
	 * 	Statistics
	 *	@return info
	 */
    public String getStatistics() {
        StringBuffer sb = new StringBuffer();
        sb.append("Alive=").append(isAlive()).append(", Start=").append(getStart()).append(", WorkCount=").append(getWorkCount()).append(", WorkTime=").append(getWorkTime()).append(", PollCount=").append(getPollCount()).append(", Working=").append(isWorking()).append(", Last=").append(getLastStart());
        return sb.toString();
    }

    /**
	 * 	String Representation
	 *	@return info
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer("CompiereServer[");
        sb.append(getStatistics()).append("]");
        return sb.toString();
    }

    /**************************************************************************
	 * 	Run - Do the Work
	 * @see java.lang.Runnable#run()
	 */
    public final void run() {
        if (m_start == 0) m_start = System.currentTimeMillis();
        m_canContinue = true;
        while (m_canContinue) {
            if (isInterrupted()) return;
            m_lastStart = System.currentTimeMillis();
            m_working = true;
            try {
                m_pollCount++;
                if (canDoWork()) {
                    m_canContinue = doWork();
                    m_count++;
                }
            } catch (Exception e) {
                log.log(Level.SEVERE, "run", e);
            }
            m_working = false;
            long end = System.currentTimeMillis();
            m_time += (end - m_lastStart);
            if (isInterrupted()) return;
            try {
                log.fine("sleeping ... " + m_sleepSeconds);
                sleep(m_sleepSeconds * 1000);
            } catch (InterruptedException e1) {
                log.warning("run - " + e1.getLocalizedMessage());
                return;
            }
        }
    }

    /**
	 * 	Get Processor Name
	 * 	@return Processor Name
	 */
    public abstract String getProcessorName();

    /**
	 * 	Is there work for the Worker?
	 * 	@return true if doWork should be called
	 */
    public abstract boolean canDoWork();

    /**
	 * 	Worker - do the work
	 * 	@return true if worker can continue
	 */
    public abstract boolean doWork();
}
