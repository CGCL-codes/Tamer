package edu.asu.cri.MirkE.trace;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import edu.asu.cri.MirkE.MirkE;

/**
  * @author abuayyub
  * Mirke Logger used by the MirkeAspectLogger.
  * methods that log exception and method trace.  
  */
public class MirkELogger {

    /**
      * Logger
      * */
    private Log log = null;

    public MirkELogger() {
    }

    /**
      * log the information of the method entry joinpoint
      * @param aClass class name
      * @param message log message
      * */
    public void entry(String aClass, String message) {
        try {
            log = LogFactory.getLog(Class.forName(aClass));
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.debug("[ " + message + " ]");
    }

    /**
      * log the information of the method entry joinpoint
      * @param aClass name
      * @param message log message
      * */
    public void exit(String aClass, String message) {
        try {
            log = LogFactory.getLog(Class.forName(aClass));
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.debug("[ " + message + " ]");
    }

    /**
    * log the information of the method entry joinpoint
    * @param aClass class name
    * @param message log message
    * */
    public void error(String aClass, String message) {
        try {
            log = LogFactory.getLog(Class.forName(aClass));
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.error("[ " + message + "]");
    }

    /**
    * log the information of the method entry joinpoint
    * @param aClass class name
    * @param message log message
    * */
    public void warning(String aClass, String message) {
        try {
            log = LogFactory.getLog(Class.forName(aClass));
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.debug("[ " + message + " ]");
    }

    /**
      * @param aClass
      * @return <code>true</code> if debug is enabled for the logger for <code>aClass</code> 
      */
    public boolean isDebug(String aClass) {
        try {
            log = LogFactory.getLog(Class.forName(aClass));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return log.isDebugEnabled();
    }
}
