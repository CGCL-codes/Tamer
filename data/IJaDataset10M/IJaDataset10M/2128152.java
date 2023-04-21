package org.epo.jdist.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.epo.jdist.dl.JDistException;
import org.epo.jdist.heart.JDistSysParam;
import org.epoline.jsf.utils.Log4jManager;
import org.epoline.service.support.ServiceSupport;

/**
 * Stand alone process purging the jDist DBMS tables from timeouted messages.
 * Creation date: 10/2001
 * @author Infotel Conseil
 */
public class JDistMsgPurge extends Thread {

    private int inProcessTimeout = 0;

    private int inErrorTimeout = 0;

    private java.lang.String usrDBDriver;

    private java.lang.String usrDBUrl;

    private java.lang.String usrDBUserid;

    private java.lang.String usrDBUserpwd;

    /**
 * JDistMsgPurge constructor.
 */
    public JDistMsgPurge() {
        super("MESSAGE_PURGE");
    }

    /**
 * Check of a mandatory parameter in a properties file.
 * @return java.lang.String
 * @param prop java.util.Properties
 * @param token java.lang.String
 * @exception org.epo.jdist.dl.JDistException
 */
    private String checkGetProperty(Properties theProp, String theToken) throws JDistException {
        String myReadValue;
        myReadValue = theProp.getProperty(theToken);
        if (myReadValue == null || (myReadValue.trim().equals(""))) {
            String myMsg = "Missing mandatory field in the properties file: " + theToken;
            throw new JDistException(JDistException.ERR_MISSING_PARAM, myMsg);
        }
        return myReadValue.trim();
    }

    /**
 * Clean the table of messages in error.
 */
    private void cleanInErrorMsgsTable(Properties theProp) {
        Connection myConnection = null;
        Statement myStatement = null;
        Timestamp myErrorTS = new Timestamp(System.currentTimeMillis() - getInErrorTimeout() * 24 * 3600 * 1000);
        String IN_ERROR_MSGS = "";
        String UPDATETS = "";
        String mySQLCommand = "";
        try {
            IN_ERROR_MSGS = checkGetProperty(theProp, JDistSysParam.DB_INERROR_TABLENAME_PRP);
            UPDATETS = checkGetProperty(theProp, JDistSysParam.DB_INERROR_COLUMN_UPDATETS_PRP);
        } catch (JDistException je) {
            System.exit(1);
        }
        try {
            myConnection = DriverManager.getConnection(getUsrDBUrl(), getUsrDBUserid(), getUsrDBUserpwd());
            myStatement = myConnection.createStatement();
        } catch (SQLException se) {
            System.exit(1);
        }
        try {
            mySQLCommand = "DELETE FROM " + IN_ERROR_MSGS + "  WHERE ( " + UPDATETS + " < {ts '" + myErrorTS + "'} ) ";
            int myRowCount = myStatement.executeUpdate(mySQLCommand);
            if (myRowCount > 0) ; else ;
        } catch (SQLException se) {
            System.exit(1);
        } finally {
            try {
                if (myStatement != null) myStatement.close();
                if (myConnection != null) myConnection.close();
            } catch (SQLException se) {
            }
        }
    }

    /**
 * Clean the table of messages (to be) processed.
 */
    private void cleanInProcessMsgsTable(Properties theProp) {
        Connection myConnection = null;
        Statement myStatement = null;
        Timestamp myProcessTS = new Timestamp(System.currentTimeMillis() - getInProcessTimeout() * 24 * 3600 * 1000);
        String IN_PROCESS_MSGS = "";
        String STATUS = "";
        String UPDATETS = "";
        String mySQLCommand = "";
        try {
            IN_PROCESS_MSGS = checkGetProperty(theProp, JDistSysParam.DB_INPROCESS_TABLENAME_PRP);
            STATUS = checkGetProperty(theProp, JDistSysParam.DB_INPROCESS_COLUMN_STATUS_PRP);
            UPDATETS = checkGetProperty(theProp, JDistSysParam.DB_INPROCESS_COLUMN_UPDATETS_PRP);
        } catch (JDistException je) {
            System.exit(1);
        }
        try {
            myConnection = DriverManager.getConnection(getUsrDBUrl(), getUsrDBUserid(), getUsrDBUserpwd());
            myStatement = myConnection.createStatement();
        } catch (SQLException se) {
            System.exit(1);
        }
        try {
            mySQLCommand = "DELETE FROM " + IN_PROCESS_MSGS + "  WHERE ( " + STATUS + "= 1 AND " + UPDATETS + " < {ts '" + myProcessTS + "'} ) ";
            int myRowCount = myStatement.executeUpdate(mySQLCommand);
            if (myRowCount > 0) ; else ;
        } catch (SQLException se) {
            System.exit(1);
        } finally {
            try {
                if (myStatement != null) myStatement.close();
                if (myConnection != null) myConnection.close();
            } catch (SQLException se) {
            }
        }
    }

    /**
 * Standard accessor.
 * @return int
 */
    private int getInErrorTimeout() {
        return inErrorTimeout;
    }

    /**
 * Standard accessor.
 * @return int
 */
    private int getInProcessTimeout() {
        return inProcessTimeout;
    }

    /**
 * Standard accessor.
 * @return java.lang.String
 */
    private String getUsrDBDriver() {
        return usrDBDriver;
    }

    /**
 * Standard accessor.
 * @return java.lang.String
 */
    private String getUsrDBUrl() {
        return usrDBUrl;
    }

    /**
 * Standard accessor.
 * @return java.lang.String
 */
    private String getUsrDBUserid() {
        return usrDBUserid;
    }

    /**
 * Standard accessor.
 * @return java.lang.String
 */
    private String getUsrDBUserpwd() {
        return usrDBUserpwd;
    }

    /**
 * Initialize the system parameters with data stored in the properties file.
 * @exception org.epo.jdist.dl.JDistException
 */
    private void loadStoredParameters(Properties theProp) throws JDistException {
        try {
            setInProcessTimeout(Integer.parseInt(checkGetProperty(theProp, "org.epo.jdist.tools.msgpurge.timeout.processed")));
            setInErrorTimeout(Integer.parseInt(checkGetProperty(theProp, "org.epo.jdist.tools.msgpurge.timeout.in_error")));
        } catch (NumberFormatException nfe) {
            String myMsg = "The timeout value (" + nfe.getMessage() + ") must be numerical.";
            throw new JDistException(JDistException.ERR_INVALID_PARAM, myMsg);
        }
        setUsrDBDriver(checkGetProperty(theProp, "org.epo.jdist.database.driver"));
        setUsrDBUrl(checkGetProperty(theProp, "org.epo.jdist.database.url"));
        setUsrDBUserid(checkGetProperty(theProp, "org.epo.jdist.database.userid"));
        setUsrDBUserpwd(checkGetProperty(theProp, "org.epo.jdist.database.userpwd"));
        try {
            Class.forName(getUsrDBDriver());
        } catch (ClassNotFoundException cnfe) {
            System.exit(1);
        }
    }

    /**
 * This method starts the JDistMsgPurge.
 * @param args java.lang.String[]
 */
    public static void main(String[] args) {
        JDistMsgPurge myPurgeThread = new JDistMsgPurge();
        myPurgeThread.start();
    }

    /**
 * During its life, the thread purge the 'in process' and 'in error' tables.
 */
    public void run() {
        Properties myProp = null;
        try {
            myProp = ServiceSupport.getProperties("org.epo.jdist.rmi.JDistService");
            loadStoredParameters(myProp);
        } catch (JDistException je) {
            System.exit(1);
        } catch (Exception e) {
            System.exit(1);
        }
        cleanInProcessMsgsTable(myProp);
        cleanInErrorMsgsTable(myProp);
    }

    /**
 * Standard accessor.
 * @param newInErrorTimeout int
 */
    private void setInErrorTimeout(int newInErrorTimeout) {
        this.inErrorTimeout = newInErrorTimeout;
    }

    /**
 * Standard accessor.
 * @param newInProcessTimeout int
 */
    private void setInProcessTimeout(int newInProcessTimeout) {
        this.inProcessTimeout = newInProcessTimeout;
    }

    /**
 * Standard accessor.
 * @param newUsrDBDriver java.lang.String
 */
    private void setUsrDBDriver(String newUsrDBDriver) {
        this.usrDBDriver = newUsrDBDriver;
    }

    /**
 * Standard accessor.
 * @param newUsrDBUrl java.lang.String
 */
    private void setUsrDBUrl(String newUsrDBUrl) {
        this.usrDBUrl = newUsrDBUrl;
    }

    /**
 * Standard accessor.
 * @param newUsrDBUserid java.lang.String
 */
    private void setUsrDBUserid(String newUsrDBUserid) {
        this.usrDBUserid = newUsrDBUserid;
    }

    /**
 * Standard accessor.
 * @param newUsrDBUserpwd java.lang.String
 */
    private void setUsrDBUserpwd(String newUsrDBUserpwd) {
        this.usrDBUserpwd = newUsrDBUserpwd;
    }
}
