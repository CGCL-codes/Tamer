package org.epo.jbpa.heart;

import org.epo.jbpa.bpaInterface.*;
import org.epo.jbpa.archiveInterface.*;
import org.epo.jbpa.generic.*;
import org.epo.jbpa.xmlparsing.XmlTagsTable;
import org.epo.jbpa.databaseInterface.DbConnectionPool;
import org.epo.jbpa.databaseInterface.DbFunctionnalException;
import org.epo.jbpa.dl.*;
import org.epo.jbpa.rmi.JBpaService;
import org.epo.jbpa.xmlparsing.PrinterListSaxHandler;
import org.epo.eventfac.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.io.*;
import org.epo.jphoenix.userauthentication.DatabaseInfo;
import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * JBPA System parameters
 * Some parameters are loaded from configuration file
 * and others are the references of the components of the system
 * Creation date: (13/06/01 11:29:21)
 * @author INFOTEL Conseil
 */
public class JBpaSystemParam {

    private static Properties jsfPropFile = null;

    public static Properties propFile = null;

    public static Properties dbPropFile;

    private int diskUsageHigh = 0;

    private int diskUsageLow = 0;

    private String serverName = "";

    private String monitoringPort = "";

    private String workDirectory = "";

    private String staticDirectory = "";

    private String description = "";

    private String jbpaLocation = "";

    private int DBMaxConnection = 0;

    private String DBDriver = "";

    private String DBUrl = "";

    private String DBUserId = "";

    private String DBPassword = "";

    private int blobSize = 0;

    private DatabaseInfo databaseInfo;

    private String bpaUserId = "";

    private String bpaPassword = "";

    private String bpaHostUrl = "";

    private BpaNetworkLink bpaNetworkLink = null;

    private String phxUserId = "";

    private String phxPassword = "";

    private String phxHostUrl = "";

    private PxiNetworkLink pxiNetworkLink = null;

    private String bnsUserId = "";

    private String bnsPassword = "";

    private String bnsHostUrl = "";

    private BnsNetworkLink bnsNetworkLink = null;

    private String jPxiUserId = "";

    private String jPxiPassword = "";

    private String jPxiHostUrl = "";

    private JPxiNetworkLink jPxiNetworkLink = null;

    private int cleanerTimeout = 0;

    private int checkerTimeout = 0;

    private int loaderTimeout = 0;

    private int sessionTimeout = 0;

    private String bsiTraceFileName = null;

    private int bsiTraceLevel = 0;

    private JBpaSessionList connectedClientList;

    private boolean Administrator = false;

    private XmlTagsTable xmlTagsTable;

    private int currentReqNum = 0;

    private int currentTempAppdocNum = 0;

    private DbConnectionPool dbConnectionPool = null;

    private JBpaChecker checker = null;

    private JBpaCleaner cleaner = null;

    private JBpaLoader loader = null;

    private LinkedList bpsList = null;

    private JBpaService jbpaServerRef = null;

    private String ctlfileDtdUrl = null;

    private String killKey = null;

    private String printerListUrl = null;

    private PrinterListSaxHandler printerListHandler = null;

    private boolean authenticationActivation;

    private JBpaLogEvent jbpaLogEvent;

    /**
 * SystemParameter constructor comment
 * @param theServerRef JBpaService
 * @param thePropFile Properties
 */
    public JBpaSystemParam(JBpaService theServerRef, Properties thePropFile) {
        super();
        setJbpaServerRef(theServerRef);
        setJsfPropFile(thePropFile);
        loadStoredParameters();
        setXmlTagsTable(new XmlTagsTable());
    }

    /**
 * SystemParameter constructor comment
 * @param theServerRef JBpaService
 */
    public JBpaSystemParam(JBpaService theServerRef) {
        super();
        setJbpaServerRef(theServerRef);
        loadStoredParameters();
        setXmlTagsTable(new XmlTagsTable());
    }

    /**
 * Initialize the Db Connection Pool
 */
    private void InitDbConnectionPool() {
        try {
            setDbConnectionPool(new DbConnectionPool("", getDBDriver(), getDBUrl(), getDBUserId(), getDBPassword(), getDBMaxConnection()));
        } catch (JBpaException e) {
        }
    }

    /**
 * Getting a properties value from the properties configuration file
 * of the jBPA system. Checks are done on this property value.
 * @return String
 * @param prop Properties
 * @param token String
 * @exception JBpaException
 */
    private String checkGetProperty(Properties theProp, String theToken) throws org.epo.jbpa.dl.JBpaException {
        String myReadValue;
        myReadValue = theProp.getProperty(theToken);
        if (myReadValue == null || myReadValue.trim().equals("")) {
            try {
                Object[] myArgs = { new String(theToken), new String("jBpa Server Configuration File") };
            } catch (IOException e) {
                fail(e, "Error on log/event files");
            } catch (EventFacException e) {
                fail(e, "Error on event facility");
            }
        }
        if (myReadValue == null) {
            throw new JBpaException(JBpaException.ERR_PROPERTY, "The propertie " + theToken + " is missing in the properties file");
        }
        return myReadValue;
    }

    /**
 * Action on exception occuring during jBPA system Parameters processing
 * @param theException Exception
 * @param theMessage String
 */
    public void fail(Exception theException, String theMessage) {
        try {
        } catch (Exception e) {
            String[] args = { theMessage + theException.getMessage() };
            try {
                Producer.send("EVT_JBPA0020", args);
            } catch (Exception e2) {
                System.exit(1);
            }
        }
        System.exit(1);
    }

    /**bnsHostUrl variable accesser
 * Creation date: (03/07/01 17:31:05)
 * @return String
 */
    public String getBnsHostUrl() {
        return bnsHostUrl;
    }

    /**bnsNetworkLink variable accesser
 * Creation date: (31/07/01 14:20:21)
 * @return BnsNetworkLink
 */
    public BnsNetworkLink getBnsNetworkLink() {
        return bnsNetworkLink;
    }

    /**variable accesser
 * Creation date: (03/07/01 17:30:41)
 * @return String
 */
    public String getBnsPassword() {
        return bnsPassword;
    }

    /**variable accesser
 * Creation date: (03/07/01 17:30:27)
 * @return String
 */
    public String getBnsUserId() {
        return bnsUserId;
    }

    /**variable accesser
 * Creation date: (03/07/01 16:15:26)
 * @return String
 */
    public String getBpaHostUrl() {
        return bpaHostUrl;
    }

    /**variable accesser
 * Creation date: (31/07/01 12:03:04)
 * @return BpaNetworkLink
 */
    public BpaNetworkLink getBpaNetworkLink() {
        return bpaNetworkLink;
    }

    /**variable accesser
 * Creation date: (03/07/01 16:15:11)
 * @return String
 */
    public String getBpaPassword() {
        return bpaPassword;
    }

    /**variable accesser
 * Creation date: (03/07/01 16:14:57)
 * @return String
 */
    public String getBpaUserId() {
        return bpaUserId;
    }

    /**variable accesser
 * Creation date: (16/08/01 10:01:28)
 * @return LinkedList
 */
    public LinkedList getBpsList() {
        return bpsList;
    }

    /**variable accesser
 * Creation date: (13/08/01 15:20:35)
 * @return org.epo.jbpa.heart.JBpaChecker
 */
    public JBpaChecker getChecker() {
        return checker;
    }

    /**variable accesser
 * Creation date: (04/07/01 09:40:56)
 * @return int
 */
    public int getCheckerTimeout() {
        return checkerTimeout;
    }

    /**variable accesser
 * Creation date: (13/08/01 15:20:53)
 * @return org.epo.jbpa.heart.JBpaCleaner
 */
    public JBpaCleaner getCleaner() {
        return cleaner;
    }

    /**variable accesser
 * Creation date: (04/07/01 09:40:44)
 * @return int
 */
    public int getCleanerTimeout() {
        return cleanerTimeout;
    }

    /**variable accesser
 * Creation date: (02/07/01 15:18:07)
 * @return JBpaSessionList
 */
    public JBpaSessionList getConnectedClientList() {
        return connectedClientList;
    }

    /**variable accesser
 * Creation date: (06/11/01 10:33:30)
 * @return String
 */
    public String getCtlfileDtdUrl() {
        return ctlfileDtdUrl;
    }

    /**variable accesser
 * Creation date: (02/08/01 15:15:06)
 * @return int
 */
    public int getCurrentReqNum() {
        return currentReqNum;
    }

    /**variable accesser
 * Creation date: (06/08/01 16:45:34)
 * @return int
 */
    public int getCurrentTempAppdocNum() {
        return currentTempAppdocNum;
    }

    /**variable accesser
 * Creation date: (02/07/01 14:47:46)
 * @return DatabaseInfo
 */
    public DatabaseInfo getDatabaseInfo() {
        return databaseInfo;
    }

    /**variable accesser
 * Creation date: (03/08/01 15:12:30)
 * @return DbConnectionPool
 */
    public DbConnectionPool getDbConnectionPool() {
        return dbConnectionPool;
    }

    /**variable accesser
 * Creation date: (13/06/01 11:58:59)
 * @return String
 */
    public String getDBDriver() {
        return DBDriver;
    }

    /**variable accesser
 * Creation date: (13/06/01 12:05:34)
 * @return int
 */
    public int getDBMaxConnection() {
        return DBMaxConnection;
    }

    /**variable accesser
 * Creation date: (13/06/01 12:01:05)
 * @return String
 */
    public String getDBPassword() {
        return DBPassword;
    }

    /**variable accesser
 * Creation date: (13/06/01 16:40:04)
 * @return Properties
 */
    public static Properties getDbPropFile() {
        return dbPropFile;
    }

    /**variable accesser
 * Creation date: (13/06/01 11:59:29)
 * @return String
 */
    public String getDBUrl() {
        return DBUrl;
    }

    /**variable accesser
 * Creation date: (13/06/01 12:00:38)
 * @return String
 */
    public String getDBUserId() {
        return DBUserId;
    }

    /**variable accesser
 * Creation date: (03/07/01 10:16:58)
 * @return String
 */
    public String getDescription() {
        return description;
    }

    /**variable accesser
 * Creation date: (04/07/01 09:34:33)
 * @return int
 */
    public int getDiskUsageHigh() {
        return diskUsageHigh;
    }

    /**variable accesser
 * Creation date: (04/07/01 09:34:45)
 * @return int
 */
    public int getDiskUsageLow() {
        return diskUsageLow;
    }

    /**variable accesser
 * Creation date: (16/08/01 11:00:38)
 * @return org.epo.jbpa.rmi.JBpaService
 */
    public JBpaService getJbpaServerRef() {
        return jbpaServerRef;
    }

    /**variable accesser
 * Creation date: (13/09/01 11:46:00)
 * @return String
 */
    public String getJPxiHostUrl() {
        return jPxiHostUrl;
    }

    /**variable accesser
 * Creation date: (13/09/01 11:47:08)
 * @return org.epo.jbpa.archiveInterface.JPxiNetworkLink
 */
    public JPxiNetworkLink getJPxiNetworkLink() {
        return jPxiNetworkLink;
    }

    /**variable accesser
 * Creation date: (13/09/01 11:45:42)
 * @return String
 */
    public String getJPxiPassword() {
        return jPxiPassword;
    }

    /**variable accesser
 * Creation date: (13/09/01 11:45:19)
 * @return String
 */
    public String getJPxiUserId() {
        return jPxiUserId;
    }

    /**variable accesser
 * Creation date: (01/02/02 16:17:01)
 * @return java.lang.String
 */
    public String getKillKey() {
        return killKey;
    }

    /**variable accesser
 * Creation date: (14/08/01 14:54:53)
 * @return org.epo.jbpa.heart.JBpaLoader
 */
    public JBpaLoader getLoader() {
        return loader;
    }

    /**variable accesser
 * Creation date: (14/08/01 17:21:51)
 * @return int
 */
    public int getLoaderTimeout() {
        return loaderTimeout;
    }

    /**variable accesser
 * Creation date: (03/07/01 17:29:12)
 * @return String
 */
    public String getPhxHostUrl() {
        return phxHostUrl;
    }

    /**variable accesser
 * Creation date: (03/07/01 17:28:56)
 * @return String
 */
    public String getPhxPassword() {
        return phxPassword;
    }

    /**variable accesser
 * Creation date: (03/07/01 17:28:39)
 * @return String
 */
    public String getPhxUserId() {
        return phxUserId;
    }

    /**variable accesser
 * Creation date: (11/03/02 16:16:27)
 * @return org.epo.jbpa.xmlparsing.PrinterListSaxHandler
 */
    public org.epo.jbpa.xmlparsing.PrinterListSaxHandler getPrinterListHandler() {
        return printerListHandler;
    }

    /**variable accesser
 * Creation date: (22/02/02 09:40:10)
 * @return java.lang.String
 */
    public String getPrinterListUrl() {
        return printerListUrl;
    }

    /**variable accesser
 * Creation date: (22/02/02 09:40:10)
 * @return Properties
 */
    private static Properties getJsfPropFile() {
        return jsfPropFile;
    }

    /**variable accesser
 * Creation date: (02/07/01 15:30:14)
 * @return Properties
 */
    public static Properties getPropFile() {
        return propFile;
    }

    /**variable accesser
 * Creation date: (31/07/01 14:19:46)
 * @return PxiNetworkLink
 */
    public PxiNetworkLink getPxiNetworkLink() {
        return pxiNetworkLink;
    }

    /**variable accesser
 * Creation date: (03/07/01 09:41:00)
 * @return String
 */
    public String getServerName() {
        return serverName;
    }

    /**variable accesser
 * Creation date: (22/08/01 13:51:13)
 * @return int
 */
    public int getSessionTimeout() {
        return sessionTimeout;
    }

    /**variable accesser
 * Creation date: (03/07/01 10:16:41)
 * @return String
 */
    public String getStaticDirectory() {
        return staticDirectory;
    }

    /**variable accesser
 * Creation date: (15/06/01 16:25:03)
 * @return String
 */
    public String getWorkDirectory() {
        return workDirectory;
    }

    /**variable accesser
 * Creation date: (01/08/01 10:34:43)
 * @return XmlTagsTable
 */
    public XmlTagsTable getXmlTagsTable() {
        return xmlTagsTable;
    }

    /**variable accesser
 * Creation date: (09/07/01 13:13:31)
 * @return boolean
 */
    public boolean isAdministrator() {
        return Administrator;
    }

    /**
 * Loading last request number at the start of the jBPA system
 * from the request number table
 * Creation date: (02/08/01 14:12:01)
 * @return int
 */
    public int loadLastRequestNumber() {
        int myRequestNum = 0;
        String myStrRequestNumber = null;
        String myDataBaseName = null;
        String mySequenceColumnName = null;
        try {
            myDataBaseName = checkGetProperty(getDbPropFile(), "jbpaserver.database.seq_number.tablename");
            mySequenceColumnName = checkGetProperty(getDbPropFile(), "jbpaserver.database.seq_number.column.request_number");
        } catch (JBpaException e) {
        }
        String sqlQuery = "SELECT " + mySequenceColumnName + " FROM " + myDataBaseName;
        ResultSet myResultSet = null;
        try {
            myResultSet = getDbConnectionPool().executeQuery(sqlQuery, this);
            myResultSet.next();
            myStrRequestNumber = myResultSet.getString(mySequenceColumnName);
            myResultSet.close();
            myRequestNum = Integer.parseInt(myStrRequestNumber.trim());
        } catch (DbFunctionnalException e1) {
        } catch (SQLException e2) {
        } finally {
            if (myResultSet != null) {
                getDbConnectionPool().release(myResultSet, getDbConnectionPool().getPoller());
            }
        }
        return myRequestNum;
    }

    /**
 * Loading last temporary appdoc number at the start of the jBPA system
 * from the temporary appdoc number table
 * Creation date: (02/08/01 14:12:01)
 * @return int
 */
    public int loadLastTempAppdocNumber() {
        int myAppdocNum = 0;
        String myDataBaseName = null;
        String myTempAppDocColumnName = null;
        try {
            myDataBaseName = checkGetProperty(getDbPropFile(), "jbpaserver.database.seq_number.tablename");
            myTempAppDocColumnName = checkGetProperty(getDbPropFile(), "jbpaserver.database.seq_number.column.tempAppDOC_number");
        } catch (JBpaException e) {
        }
        String sqlQuery = "SELECT " + myTempAppDocColumnName + " FROM " + myDataBaseName;
        ResultSet myResultSet = null;
        try {
            myResultSet = getDbConnectionPool().executeQuery(sqlQuery, this);
            if (myResultSet.next()) {
                myAppdocNum = myResultSet.getInt(myTempAppDocColumnName);
            } else {
            }
        } catch (DbFunctionnalException e1) {
        } catch (SQLException e2) {
        } finally {
            try {
                myResultSet.close();
            } catch (SQLException e3) {
            }
            if (myResultSet != null) getDbConnectionPool().release(myResultSet, getDbConnectionPool().getPoller());
        }
        return myAppdocNum;
    }

    /**
 * Loading printers list from xml file
 * Creation date: (11/03/02 16:09:14)
 * @exception JBpaException
 */
    private void loadPrintersList() throws org.epo.jbpa.dl.JBpaException {
        try {
        } catch (SAXParseException err) {
            throw (new JBpaException(JBpaException.ERR_SAX_PARSER, ("Unrecoverable parsing error:" + err.getMessage())));
        } catch (SAXException err) {
            throw (new JBpaException(JBpaException.ERR_SAX_PARSER, ("Error during parsing:" + err.getMessage())));
        } catch (IOException err) {
            throw (new JBpaException(JBpaException.ERR_SAX_PARSER, ("Unrecoverable parsing error:" + err.getMessage())));
        }
    }

    /**
 * Loading the parameters stored in the jBPA system configuration file
 */
    public void loadStoredParameters() {
        String myString = null;
        Enumeration myEnumeration = null;
        Properties myProp;
        Properties myDbProp = null;
        myProp = getJsfPropFile();
        setPropFile(myProp);
        setJbpaLogEvent(new JBpaLogEvent(getJsfPropFile()));
        try {
            String myTraceFileName = checkGetProperty(myProp, "jbpaserver.trace.name");
            this.setBsiTraceFileName(myTraceFileName);
        } catch (JBpaException e) {
            fail(e, "Exception while loading stored parameters : " + e.getMessage());
        }
        try {
            int myTraceLevel = Integer.parseInt(checkGetProperty(myProp, "jbpaserver.trace.level"));
            this.setBsiTraceLevel(myTraceLevel);
        } catch (JBpaException e) {
            fail(e, "Exception while loading stored parameters : " + e.getMessage());
        }
        try {
            setServerName(checkGetProperty(myProp, "jbpaserver.name"));
            getJbpaServerRef().setSrvInstanceId(getServerName());
            setMonitoringPort(checkGetProperty(myProp, "org.epoline.monitoring.port"));
        } catch (JBpaException e) {
            fail(e, "Exception while loading stored parameters : " + e.getMessage());
        }
        myDbProp = getJsfPropFile();
        setDbPropFile(myDbProp);
        try {
            setDBDriver(checkGetProperty(myDbProp, "jbpaserver.database.driver"));
            setDBUrl(checkGetProperty(myDbProp, "jbpaserver.database.url"));
            setDBUserId(checkGetProperty(myDbProp, "jbpaserver.database.userid"));
            setDBPassword(checkGetProperty(myDbProp, "jbpaserver.database.password"));
            setBlobSize(Integer.parseInt(checkGetProperty(myDbProp, "jbpaserver.database.blobsize")));
            setDBMaxConnection(Integer.parseInt(checkGetProperty(myProp, "jbpaserver.database.maxconnection")));
            setDatabaseInfo(new DatabaseInfo(checkGetProperty(myDbProp, "jbpaserver.userauthent.url"), checkGetProperty(myDbProp, "jbpaserver.userauthent.driver"), checkGetProperty(myDbProp, "jbpaserver.userauthent.userid"), checkGetProperty(myDbProp, "jbpaserver.userauthent.password")));
            InitDbConnectionPool();
        } catch (JBpaException e) {
            fail(e, "Exception while loading stored parameters :" + e.getMessage());
        }
        try {
            setDiskUsageHigh(Integer.parseInt(checkGetProperty(myProp, "jbpaserver.system.diskusage.high")));
            setDiskUsageLow(Integer.parseInt(checkGetProperty(myProp, "jbpaserver.system.diskusage.low")));
            setServerName(checkGetProperty(myProp, "jbpaserver.name"));
            setCurrentReqNum(loadLastRequestNumber());
            setCurrentTempAppdocNum(loadLastTempAppdocNumber());
            setWorkDirectory(checkGetProperty(myProp, "jbpaserver.working.directory"));
            setStaticDirectory(checkGetProperty(myProp, "jbpaserver.static.directory"));
            setCtlfileDtdUrl(checkGetProperty(myProp, "jbpaserver.ctlfile.dtdURL"));
            setPrinterListUrl(checkGetProperty(myProp, "jbpaserver.printerList.URL"));
            loadPrintersList();
            setDescription(checkGetProperty(myProp, "jbpaserver.description"));
            setJbpaLocation(checkGetProperty(myProp, "jbpaserver.location"));
            setKillKey(checkGetProperty(myProp, "jbpaserver.killKey"));
            if (checkGetProperty(myProp, "jbpaserver.authentication.activate").equalsIgnoreCase("YES")) setAuthenticationActivation(true); else setAuthenticationActivation(false);
            setBpaUserId(checkGetProperty(myProp, "jbpaserver.bpa.userid"));
            setBpaPassword(checkGetProperty(myProp, "jbpaserver.bpa.password"));
            setBpaHostUrl(checkGetProperty(myProp, "jbpaserver.bpa.hosturl"));
            setBpaNetworkLink(new BpaNetworkLink(getBpaUserId(), getBpaPassword(), getBpaHostUrl(), this, "BPANETWORK"));
            Object[] myArgs = { new String(getBpaHostUrl()) };
            try {
            } catch (IOException e) {
            } catch (EventFacException e) {
            }
            setPhxUserId(checkGetProperty(myProp, "jbpaserver.phx.userid"));
            setPhxPassword(checkGetProperty(myProp, "jbpaserver.phx.password"));
            setPhxHostUrl(checkGetProperty(myProp, "jbpaserver.phx.hosturl"));
            setPxiNetworkLink(new PxiNetworkLink(getPhxUserId(), getPhxPassword(), getPhxHostUrl(), "PHXNETWORK"));
            myArgs[0] = new String(getPhxHostUrl());
            try {
            } catch (IOException e) {
            } catch (EventFacException e) {
            }
            setBnsUserId(checkGetProperty(myProp, "jbpaserver.bns.userid"));
            setBnsPassword(checkGetProperty(myProp, "jbpaserver.bns.password"));
            setBnsHostUrl(checkGetProperty(myProp, "jbpaserver.bns.hosturl"));
            setBnsNetworkLink(new BnsNetworkLink(getBnsUserId(), getBnsPassword(), getBnsHostUrl(), "BNSNETWORK"));
            myArgs[0] = new String(getBnsHostUrl());
            try {
            } catch (IOException e) {
            } catch (EventFacException e) {
            }
            setJPxiUserId(checkGetProperty(myProp, "jbpaserver.jpxi.userid"));
            setJPxiPassword(checkGetProperty(myProp, "jbpaserver.jpxi.password"));
            setJPxiHostUrl(checkGetProperty(myProp, "jbpaserver.jpxi.hosturl"));
            setJPxiNetworkLink(new JPxiNetworkLink(getJPxiUserId(), getJPxiPassword(), getJPxiHostUrl(), "JPXINETWORK"));
            myArgs[0] = new String(getJPxiHostUrl());
            try {
            } catch (IOException e) {
            } catch (EventFacException e) {
            }
            if (getBnsNetworkLink() == null & getPxiNetworkLink() == null & getJPxiNetworkLink() == null) {
                throw new JBpaException(JBpaException.ERR_PROPERTY, "At least one document archive (BNS, PHX or JPXI) must be declared in the properties file");
            }
            setBpsList(new LinkedList());
            myEnumeration = myProp.propertyNames();
            while (myEnumeration.hasMoreElements()) {
                myString = (String) myEnumeration.nextElement();
                if (myString.startsWith("jbpaserver.jbps.name")) {
                    String myJBps = checkGetProperty(myProp, myString);
                    getBpsList().addLast(myJBps);
                }
            }
            setLoaderTimeout(Integer.parseInt(checkGetProperty(myProp, "jbpaLoader.timeout")));
            setCleanerTimeout(Integer.parseInt(checkGetProperty(myProp, "jbpaCleaner.timeout")));
            setSessionTimeout(Integer.parseInt(checkGetProperty(myProp, "jbpaServer.session.timeout")));
            setCheckerTimeout(Integer.parseInt(checkGetProperty(myProp, "jbpaChecker.timeout")));
        } catch (JBpaException e) {
            fail(e, "Exception while loading stored parameters : " + e.getMessage());
        }
        try {
            setConnectedClientList(new JBpaSessionList(this));
            getConnectedClientList().setMaxConcurrentSession(Integer.parseInt(checkGetProperty(myProp, "jbpaserver.maxclient")));
        } catch (JBpaException e) {
            fail(e, "Exception while loading stored parameters :" + e.getMessage());
        }
    }

    /**variable setter
 * Creation date: (09/07/01 13:13:31)
 * @param newAdministrator boolean
 */
    public void setAdministrator(boolean newAdministrator) {
        Administrator = newAdministrator;
    }

    /**variable setter
 * Creation date: (03/07/01 17:31:05)
 * @param newBnsHostUrl String
 */
    public void setBnsHostUrl(String newBnsHostUrl) {
        if ((newBnsHostUrl.charAt(0) == '"') && (newBnsHostUrl.charAt(newBnsHostUrl.length() - 1) == '"')) newBnsHostUrl = newBnsHostUrl.substring(1, newBnsHostUrl.length() - 1);
        bnsHostUrl = newBnsHostUrl;
    }

    /**variable setter
 * Creation date: (31/07/01 14:20:21)
 * @param newBnsNetworkLink BnsNetworkLink
 */
    public void setBnsNetworkLink(BnsNetworkLink newBnsNetworkLink) {
        bnsNetworkLink = newBnsNetworkLink;
    }

    /**variable setter
 * Creation date: (03/07/01 17:30:41)
 * @param newBnsPassword String
 */
    public void setBnsPassword(String newBnsPassword) {
        bnsPassword = newBnsPassword;
    }

    /**variable setter
 * Creation date: (03/07/01 17:30:27)
 * @param newBnsUserId String
 */
    public void setBnsUserId(String newBnsUserId) {
        bnsUserId = newBnsUserId;
    }

    /**variable setter
 * Creation date: (03/07/01 16:15:26)
 * @param newBpaHostUrl String
 */
    public void setBpaHostUrl(String newBpaHostUrl) {
        if ((newBpaHostUrl.charAt(0) == '"') && (newBpaHostUrl.charAt(newBpaHostUrl.length() - 1) == '"')) newBpaHostUrl = newBpaHostUrl.substring(1, newBpaHostUrl.length() - 1);
        bpaHostUrl = newBpaHostUrl;
    }

    /**variable setter
 * Creation date: (31/07/01 12:03:04)
 * @param newBpaNetworkLink BpaNetworkLink
 */
    public void setBpaNetworkLink(BpaNetworkLink newBpaNetworkLink) {
        bpaNetworkLink = newBpaNetworkLink;
    }

    /**variable setter
 * Creation date: (03/07/01 16:15:11)
 * @param newBpaPassword String
 */
    public void setBpaPassword(String newBpaPassword) {
        bpaPassword = newBpaPassword;
    }

    /**variable setter
 * Creation date: (03/07/01 16:14:57)
 * @param newBpaUserId String
 */
    public void setBpaUserId(String newBpaUserId) {
        bpaUserId = newBpaUserId;
    }

    /**variable setter
 * Creation date: (16/08/01 10:01:28)
 * @param newBpsList LinkedList
 */
    public void setBpsList(LinkedList newBpsList) {
        bpsList = newBpsList;
    }

    /**variable setter
 * Creation date: (13/08/01 15:20:35)
 * @param newChecker org.epo.jbpa.heart.JBpaChecker
 */
    public void setChecker(JBpaChecker newChecker) {
        checker = newChecker;
    }

    /**variable setter
 * Creation date: (04/07/01 09:40:56)
 * @param newCheckerTimeout int
 */
    public void setCheckerTimeout(int newCheckerTimeout) {
        checkerTimeout = newCheckerTimeout;
    }

    /**variable setter
 * Creation date: (13/08/01 15:20:53)
 * @param newCleaner org.epo.jbpa.heart.JBpaCleaner
 */
    public void setCleaner(JBpaCleaner newCleaner) {
        cleaner = newCleaner;
    }

    /**variable setter
 * Creation date: (04/07/01 09:40:44)
 * @param newCleanerTimeout int
 */
    public void setCleanerTimeout(int newCleanerTimeout) {
        cleanerTimeout = newCleanerTimeout;
    }

    /**variable setter
 * Creation date: (02/07/01 15:18:07)
 * @param newConnectedClientList JBpaSessionList
 */
    public void setConnectedClientList(JBpaSessionList newConnectedClientList) {
        connectedClientList = newConnectedClientList;
    }

    /**variable setter
 * Creation date: (06/11/01 10:33:30)
 * @param newCtlfileDtdUrl String
 */
    private void setCtlfileDtdUrl(String newCtlfileDtdUrl) {
        ctlfileDtdUrl = newCtlfileDtdUrl;
    }

    /**variable setter
 * Creation date: (02/08/01 15:15:06)
 * @param newCurrentReqNum int
 */
    public void setCurrentReqNum(int newCurrentReqNum) {
        currentReqNum = newCurrentReqNum;
    }

    /**variable setter
 * Creation date: (06/08/01 16:45:34)
 * @param newCurrentTempAppdocNum int
 */
    public void setCurrentTempAppdocNum(int newCurrentTempAppdocNum) {
        currentTempAppdocNum = newCurrentTempAppdocNum;
    }

    /**variable setter
 * Creation date: (02/07/01 14:47:46)
 * @param newDatabaseInfo DatabaseInfo
 */
    public void setDatabaseInfo(DatabaseInfo newDatabaseInfo) {
        databaseInfo = newDatabaseInfo;
    }

    /**variable setter
 * Creation date: (03/08/01 15:12:30)
 * @param newDbConnectionPool DbConnectionPool
 */
    public void setDbConnectionPool(DbConnectionPool newDbConnectionPool) {
        dbConnectionPool = newDbConnectionPool;
    }

    /**variable setter
 * Creation date: (13/06/01 11:58:59)
 * @param newDBDriver String
 */
    public void setDBDriver(String newDBDriver) {
        DBDriver = newDBDriver;
    }

    /**variable setter
 * Creation date: (13/06/01 12:05:34)
 * @param newDBMaxConnection int
 */
    public void setDBMaxConnection(int newDBMaxConnection) {
        DBMaxConnection = newDBMaxConnection;
    }

    /**variable setter
 * Creation date: (13/06/01 12:01:05)
 * @param newPassword String
 */
    public void setDBPassword(String newPassword) {
        DBPassword = newPassword;
    }

    /**variable setter
 * Creation date: (13/06/01 16:40:04)
 * @param newDbPropFile Properties
 */
    public static void setDbPropFile(Properties newDbPropFile) {
        dbPropFile = newDbPropFile;
    }

    /**variable setter
 * Creation date: (13/06/01 11:59:29)
 * @param newUrl String
 */
    public void setDBUrl(String newUrl) {
        DBUrl = newUrl;
    }

    /**variable setter
 * Creation date: (13/06/01 12:00:38)
 * @param newUserId String
 */
    public void setDBUserId(String newUserId) {
        DBUserId = newUserId;
    }

    /**variable setter
 * Creation date: (03/07/01 10:16:58)
 * @param newDescription String
 */
    public void setDescription(String newDescription) {
        description = newDescription;
    }

    /**variable setter
 * Creation date: (04/07/01 09:34:33)
 * @param newDiskUsageHigh int
 */
    public void setDiskUsageHigh(int newDiskUsageHigh) {
        diskUsageHigh = newDiskUsageHigh;
    }

    /**variable setter
 * Creation date: (04/07/01 09:34:45)
 * @param newDiskUsageLow int
 */
    public void setDiskUsageLow(int newDiskUsageLow) {
        diskUsageLow = newDiskUsageLow;
    }

    /**variable setter
 * Creation date: (16/08/01 11:00:38)
 * @param newJbpaServerRef org.epo.jbpa.rmi.JBpaService
 */
    public void setJbpaServerRef(JBpaService newJbpaServerRef) {
        jbpaServerRef = newJbpaServerRef;
    }

    /**variable setter
 * Creation date: (13/09/01 11:46:00)
 * @param newJPxiHostUrl String
 */
    public void setJPxiHostUrl(String newJPxiHostUrl) {
        if ((newJPxiHostUrl.charAt(0) == '"') && (newJPxiHostUrl.charAt(newJPxiHostUrl.length() - 1) == '"')) newJPxiHostUrl = newJPxiHostUrl.substring(1, newJPxiHostUrl.length() - 1);
        jPxiHostUrl = newJPxiHostUrl;
    }

    /**variable setter
 * Creation date: (13/09/01 11:47:08)
 * @param newJPxiNetworkLink org.epo.jbpa.archiveInterface.JPxiNetworkLink
 */
    public void setJPxiNetworkLink(JPxiNetworkLink newJPxiNetworkLink) {
        jPxiNetworkLink = newJPxiNetworkLink;
    }

    /**variable setter
 * Creation date: (13/09/01 11:45:42)
 * @param newJPxiPassword String
 */
    public void setJPxiPassword(String newJPxiPassword) {
        jPxiPassword = newJPxiPassword;
    }

    /**variable setter
 * @param newJPxiUserId String
 */
    public void setJPxiUserId(String newJPxiUserId) {
        jPxiUserId = newJPxiUserId;
    }

    /**variable setter
 * Creation date: (01/02/02 16:17:01)
 * @param newKillKey java.lang.String
 */
    private void setKillKey(String newKillKey) {
        killKey = newKillKey;
    }

    /**variable setter
 * Creation date: (14/08/01 14:54:53)
 * @param newLoader org.epo.jbpa.heart.JBpaLoader
 */
    public void setLoader(JBpaLoader newLoader) {
        loader = newLoader;
    }

    /**variable setter
 * Creation date: (14/08/01 17:21:51)
 * @param newLoaderTimeout int
 */
    public void setLoaderTimeout(int newLoaderTimeout) {
        loaderTimeout = newLoaderTimeout;
    }

    /**variable setter
 * Creation date: (03/07/01 17:29:12)
 * @param newPhxHostUrl String
 */
    public void setPhxHostUrl(String newPhxHostUrl) {
        if ((newPhxHostUrl.charAt(0) == '"') && (newPhxHostUrl.charAt(newPhxHostUrl.length() - 1) == '"')) newPhxHostUrl = newPhxHostUrl.substring(1, newPhxHostUrl.length() - 1);
        phxHostUrl = newPhxHostUrl;
    }

    /**variable setter
 * Creation date: (03/07/01 17:28:56)
 * @param newPhxPassword String
 */
    public void setPhxPassword(String newPhxPassword) {
        phxPassword = newPhxPassword;
    }

    /**variable setter
 * Creation date: (03/07/01 17:28:39)
 * @param newPhxUserId String
 */
    public void setPhxUserId(String newPhxUserId) {
        phxUserId = newPhxUserId;
    }

    /**
 * variable setter
 * Creation date: (11/03/02 16:16:27)
 * @param newPrinterListHandler org.epo.jbpa.xmlparsing.PrinterListSaxHandler
 */
    private void setPrinterListHandler(org.epo.jbpa.xmlparsing.PrinterListSaxHandler newPrinterListHandler) {
        printerListHandler = newPrinterListHandler;
    }

    /**
 * variable setter
 * Creation date: (22/02/02 09:40:10)
 * @param newPrinterListUrl java.lang.String
 */
    private void setPrinterListUrl(String newPrinterListUrl) {
        printerListUrl = newPrinterListUrl;
    }

    /**
 * variable setter
 * Creation date: (22/02/02 09:40:10)
 * @param newPropFile Properties
 */
    private void setJsfPropFile(Properties newPropFile) {
        jsfPropFile = newPropFile;
    }

    /**variable setter
 * Creation date: (02/07/01 15:30:14)
 * @param newPropFile Properties
 */
    public static void setPropFile(Properties newPropFile) {
        propFile = newPropFile;
    }

    /**variable setter
 * Creation date: (31/07/01 14:19:46)
 * @param newPxiNetworkLink PxiNetworkLink
 */
    public void setPxiNetworkLink(PxiNetworkLink newPxiNetworkLink) {
        pxiNetworkLink = newPxiNetworkLink;
    }

    /**variable setter
 * Creation date: (03/07/01 09:41:00)
 * @param newServerName String
 */
    public void setServerName(String newServerName) {
        serverName = newServerName;
    }

    /**variable setter
 * Creation date: (22/08/01 13:51:13)
 * @param newSessionTimeout int
 */
    public void setSessionTimeout(int newSessionTimeout) {
        sessionTimeout = newSessionTimeout;
    }

    /**variable setter
 * Creation date: (03/07/01 10:16:41)
 * @param newStaticDirectory String
 */
    public void setStaticDirectory(String newStaticDirectory) {
        staticDirectory = newStaticDirectory;
    }

    /**variable setter
 * Creation date: (15/06/01 16:25:03)
 * @param newWorkDirectory String
 */
    public void setWorkDirectory(String newWorkDirectory) {
        workDirectory = newWorkDirectory;
    }

    /**variable setter
 * Creation date: (01/08/01 10:34:43)
 * @param newXmlTagsTable XmlTagsTable
 */
    public void setXmlTagsTable(XmlTagsTable newXmlTagsTable) {
        xmlTagsTable = newXmlTagsTable;
    }

    /**
 * Storing in the request number file the current request number
 * Creation date: (10/07/01 10:44:08)
 * @param theReqNum int
 */
    public void storeCurrentReqNum(int theReqNum) {
        String myDataBaseName = null;
        String mySequenceColumnName = null;
        try {
            myDataBaseName = checkGetProperty(getDbPropFile(), "jbpaserver.database.seq_number.tablename");
            mySequenceColumnName = checkGetProperty(getDbPropFile(), "jbpaserver.database.seq_number.column.request_number");
        } catch (JBpaException e) {
        }
        String myRequestNum = Integer.toString(theReqNum);
        String sqlQuery = "update " + myDataBaseName + " set " + mySequenceColumnName + "='" + myRequestNum + "'";
        int SqlReturnCode = 0;
        try {
            SqlReturnCode = getDbConnectionPool().executeUpdate(sqlQuery, this);
        } catch (DbFunctionnalException e) {
        }
    }

    /**
 * Storing in the temporary appdoc number file the current temporary appdoc number
 * Creation date: (10/07/01 10:44:08)
 * @param theTempAppdocNum int
 */
    public void storeCurrentTempAppdocNum(int theTempAppdocNum) {
        String myDataBaseName = null;
        String myTempAppDocColumnName = null;
        try {
            myDataBaseName = checkGetProperty(getDbPropFile(), "jbpaserver.database.seq_number.tablename");
            myTempAppDocColumnName = checkGetProperty(getDbPropFile(), "jbpaserver.database.seq_number.column.tempAppDOC_number");
        } catch (JBpaException e) {
        }
        String myTempAppdocNum = Integer.toString(theTempAppdocNum);
        String sqlQuery = "update " + myDataBaseName + " set " + myTempAppDocColumnName + "=" + myTempAppdocNum;
        int SqlReturnCode = 0;
        try {
            SqlReturnCode = getDbConnectionPool().executeUpdate(sqlQuery, this);
        } catch (DbFunctionnalException e) {
        }
    }

    /**
	 * Returns the jbpa location.
	 * @return String
	 */
    public String getJbpaLocation() {
        return jbpaLocation;
    }

    /**
	 * Sets the location.
	 * @param jbpaLocation The location to set
	 */
    public void setJbpaLocation(String jbpaLocation) {
        this.jbpaLocation = jbpaLocation;
    }

    /**
	 * @return int
	 */
    public int getBlobSize() {
        return blobSize;
    }

    /**variable setter
	 * @param int i
	 */
    public void setBlobSize(int i) {
        blobSize = i;
    }

    /**
	 * @return boolean
	 */
    public boolean isAuthenticationActivation() {
        return authenticationActivation;
    }

    /**variable setter
	 * @param b
	 */
    public void setAuthenticationActivation(boolean b) {
        authenticationActivation = b;
    }

    /**
	 * Returns the bsiTraceFileName.
	 * @return String
	 */
    public String getBsiTraceFileName() {
        return bsiTraceFileName;
    }

    /**
	 * Sets the bsiTraceFileName.
	 * @param bsiTraceFileName The bsiTraceFileName to set
	 */
    public void setBsiTraceFileName(String bsiTraceFileName) {
        this.bsiTraceFileName = bsiTraceFileName;
    }

    /**
	 * Returns the bsiTraceLevel.
	 * @return int
	 */
    public int getBsiTraceLevel() {
        return bsiTraceLevel;
    }

    /**
	 * Sets the bsiTraceLevel.
	 * @param bsiTraceLevel The bsiTraceLevel to set
	 */
    public void setBsiTraceLevel(int bsiTraceLevel) {
        this.bsiTraceLevel = bsiTraceLevel;
    }

    public Level getLevel() {
    }

    /**
	 * @return JBpaLogEvent
	 */
    public JBpaLogEvent getJbpaLogEvent() {
        return jbpaLogEvent;
    }

    /**
	 * @param event JBpaLogEvent
	 */
    public void setJbpaLogEvent(JBpaLogEvent event) {
        jbpaLogEvent = event;
    }

    /**
	 * @return monitoringPort
	 */
    public String getMonitoringPort() {
        return monitoringPort;
    }

    /**
	 * @param string monitoringPort
	 */
    public void setMonitoringPort(String string) {
        monitoringPort = string;
    }
}
