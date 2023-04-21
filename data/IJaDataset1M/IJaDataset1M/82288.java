package de.barmenia.cs.centera;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.BasicConfigurator;
import de.barmenia.cs.util.AdapterStatistics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.*;
import com.filepool.fplibrary.FPClip;
import com.filepool.fplibrary.FPClip.*;
import com.filepool.fplibrary.FPFileInputStream;
import com.filepool.fplibrary.FPLibraryConstants;
import com.filepool.fplibrary.FPLibraryException;
import com.filepool.fplibrary.FPLibraryErrors.*;
import com.filepool.fplibrary.FPPool;
import com.filepool.fplibrary.FPPool.*;
import com.filepool.fplibrary.FPTag;
import com.filepool.fplibrary.FPTag.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

/**
*
* (c) Barmenia Versicherungen e.G.
*
* Implementation of a cs access to EMC Centera Clusters.
*
* Design: 
*
* a) Design is specified to fullfill only the basic needs of a project like pr2dms.
*    We use nothing that centera provides to have meta data stuctured in node trees
*    or other features. We set the retention to 0 so we have no retention and the application 
*    is able to handle retention itself. We leave all more complicated things for more advaned projets that
*    need such things working properly.
*
* b) We introduce a storage layer internal database that hold the relationship of application domain
*    ID's to content with the storage system internal used ID's. This is especially true for a CAS
*    system like centera that calculates a ID and gives this back so the caller has the only performant
*    way to access the data by useing this access key. The search for Application domain keys (ID's) 
*    in centera is ONLY PROVIDED FOR ADMINISTRATIVE USAGE and in NO WAY PERFORMANT. 
*
*    NOTE: You can completely degrade the centera system performance if you induce such querys for
*          "normal" access amounts.
*
* c) We place on all applications the respnsibillity to know the access to content and ensure user security
*    of visibility to endusers. The system acces is only secured for a technical user that handles all access.
*
*    Possibly we partition the content objects by application user hirachies. But at this time we do not
*    know is and how this is possibly in a centera system.
*
*    TODO: Learn more about access security and how we could partition content between main applications
*          and their own stored content.
*
*    TODO: This will be complicated if we have the need to form views cutting through many applcations content.
*          We leave this for future investigation.
*
* d) Access stack structure:
*
*    In a first play with the sample sources we formed a basic package that can store and retreive content
*    to and from centera with the minimal set of functionality that is needed for the above design goals.
*
*    In the first version of this implementation we layer the calles like this:
*
*    Adapter storage = new Adapter( "test", "pr2dms" );     // Create Adapter in test mode for application pr2dms.
*
*    storage.Startup();                                     // connect to storage and init all internal variables.
*                                                           // For centera we connect to primary and secondary nodes concurrently.
*    store content
*    =============
*
*    storage.Push( APP_CREATOR_ID, APP_OBJECT_ID, <input_type> );
*                                                           // Store content <input_type> to storage and save query key "APP_CREATOR_ID"
*                                                           // and APP_OBJECT_ID to query database, not nessesarily to storage metadata itself.
*        clipID = storage.Push( <input_type> );
*        storage.SaveClip( APP_CREATOR_ID, APP_OBJECT_ID, "centera", clipID );
*
*    retreive content
*    ================
*
*    storage.Pull( APP_CREATOR_ID, APP_OBJECT_ID, <output_type> );
*
*        clipID = storage.SelectRelationship( APP_CREATOR_ID, APP_OBJECT_ID )
*        storage.Pull( clipID, <output_type> );
*
*
*    Description:
*
*    Low level methodes to access node blob content over a given or returned canonical clipID and minimal <input_type> or <output_type>
*    to satisfy the design goal above.
*
*    Higher level methodes that access nodes by application ID's that we match against an relation db tabe.
*    The blobs itself are accessed by the centera internal canonical ClipID. But we do not show them outside.
* 
* ===========================================================
*
* Analst:    Frank Schulte (SLT) Frank.Schulte@barmemia.de
* Developer: Frank Schulte (SLT) Frank.Schulte@barmemia.de
*
* ===========================================================
*
* Version Date        Changer Info
*
* 0.00    10.03.2006  SLT     Create:   First Implementation.
* 0.01    10.03.2006  SLT     Add:      First interface proposal for "pr2dms" 
*                                       application using Push() and Pull() methods - SimpleCenteraAdapter.
* 0.02    10.03.2006  SLT     Add:      Design thought that would satisfy the first hand needs for
*                                       project pr2dms and BOAS like handling.
* 0.03    10.03.2006  SLT     Change:   Take sample sources of SaveContent and RetreiveContent and
*                                       form methods of low and higher order according our first access stack
*                                       layout. We want to verify if we can satisfy the needs at hand by this
*                                       layout.
* 0.04    11.03.2006  SLT     Add:      MySQL JDBC Driver access and prototype implementations.
*                                       MySQL SQL Statements SelectRelationship().
*                                       MySQL SQL Statements InsertRelationship().
* 0.05    13.03.2006  SLT     Change:   Rename interface SimpleCenteraAdapter to SimpleAdapter and move it
*                                       to package de.barmenia.cs..
* 0.06    14.03.2006  SLT     Add:      getUseDatabase() and setUseDatabase(). We control if we connect to a
*                                       database or not. This is used by the AdaterTest.java test cases.
* 0.07    15.03.2006  SLT     Add:      Debuggin code to investigate loadLibray problems of the centera API version 3.1.477 on AIX 5.3.
*                                       RESULT A) We had to copy the libFPLibrary.a lib and all other libs
*                                                 to the path /usr/java14/jre/bin/. then the JNI native libs
*                                                 were loaded.
*                                       RESULT B) The Centera API 3.1.477 Version on AIX 5.3 had an Exception in the native JNI
*                                                 library inplementing FP_GetStringFormat() method. This prevents usage of the
*                                                 new 40-Bytes binary format of a centera clipID. We had interest in using this format
*                                                 but had no luck. So we must use the variable length string format (up to 64 chars) 
*                                                 and hope that NONE database encoding for strings will break this. Centera
*                                                 had added support for binary format to reduce DB storage for millions of clipID's
*                                                 and the hoped if all uses the binary version no database would have any more
*                                                 encoding issues with this format.
* 0.08    23.03.2006  SLT     Change:   Fix DB_CONNECT_URL to point to informix test instance in Barmenia Network.
*                             Change:   Constructor Adaptor( runMode, callerName, jdbcDriverName, jdbcConnectUrl ) to init db connection.
*                             Remove:   DB_CONNECT_URL usage and change to Contructor parameter usage.
* 0.09    29.03.2006  SLT     Add:      Automatic schema creation if it does not exists.
* 0.10    03.04.2006  SLT     Add:      swap sql statements according choosed schema of mysql or ifx.
* 0.11    04.04.2006  SLT     Remove:   Hardcoded System.out.println() calles and change them to logging with http://www.slf4j.org usage.
* 0.12    05.04.2006  SLT     Add:      AdapterStatistics stats usage to all methods.
* 0.13    04.05.2006  SLT     Change:   Use enhanced APP_TO_EXT Table columns.
* ===========================================================
*
* @param       (classes, interfaces, methods and constructors only)
* @return      (methods only)
* @throws      (thrown exception)
* @author      (classes and interfaces only, required)
* @version     (classes and interfaces only, required. See footnote 1)
* @see         
* @since       
* @serial      (or @serialField or @serialData)
* @deprecated  (see How and When To Deprecate APIs)
*
*/
public class Adapter implements de.barmenia.cs.SimpleAdapter {

    final Logger logger = LoggerFactory.getLogger(Adapter.class);

    static AdapterStatistics stats = null;

    private boolean isAdapterStartedUp = false;

    private String JDBC_DRIVER_NAME = "";

    private String JDBC_CONNECT_URL = "";

    private String JDBC_schemaObjectPrefix = "";

    private String JDBC_schema_path = "";

    private String JCR_DRIVER_NAME = "";

    private String JCR_CONNECT_URL = "";

    private boolean bUseDatabase = true;

    public static final String PRIMARY_NODE = "PRIMARY_NODE";

    public static final String REPLICA_NODE = "REPLICA_NODE";

    /** application creator/object id to identify object uniquely in application domains */
    private String APP_CREATOR_ID = "";

    private String APP_OBJECT_ID = "";

    /** external storage creator/object id to identify object uniquely in storage domains */
    private String EXT_CREATOR_ID = "";

    private String EXT_OBJECT_ID = "";

    private String APP_VENDOR = "Barmenia";

    private String APP_NAME = "de.barmenia.cs.centera.Adapter";

    private String APP_VERSION = "0.11";

    private String CLIP_NAME = "de.barmenia.cs.pr2dms";

    /** identify application / function that wrote this data */
    private String TAG_NAME = APP_NAME + "." + "Push";

    private static com.filepool.fplibrary.FPPool FPprimaryPool = null;

    private static com.filepool.fplibrary.FPPool FPreplicaPool = null;

    /** counter that represents to references we delivered to clients to perform actions on centera */
    private static long FPprimaryPoolRefCount = 0;

    private static long FPreplicaPoolRefCount = 0;

    private java.sql.Connection dbConnection = null;

    private String runMode = null;

    private String runForApplication = null;

    private static java.util.Properties appSystemProperties = null;

    private static String appFileSeparator = null;

    static {
        appSystemProperties = System.getProperties();
        appFileSeparator = appSystemProperties.getProperty("file.separator");
    }

    /** 
	*
	* Adapter init
	*
	* runMode = test => connect to TEST centera.
	* runMode = prod => connect to PROD centera.
	*	
	* caller_name = aooication that uses this adapter.
	*	
	* example: de.barmenia.cs.centera.Adapter adapter = new de.barmenia.cs.centera.Adapter( "test", "pr2dms" );
	*
	* @param       runMode
	* @param       caller_name
	* @param       jdbcDriverName
	* @param       jdbcConnectUrl
	* @param       jdbcSchemaPath
	* @param       jcrDriverName
	* @param       jcrConnectUrl

	* @throws      Exception
	* @author      Frank.Schulte@barmenia.de
	* @version     0.11
	* @see         
	* @since       
	* @serial      (or @serialField or @serialData)
	* @deprecated  (see How and When To Deprecate APIs)
	*
	*/
    public Adapter(String runMode, String caller_name, String jdbcDriverName, String jdbcConnectUrl, String jdbcSchemaPath, String jcrDriverName, String jcrConnectUrl) throws Exception {
        BasicConfigurator.configure();
        isAdapterStartedUp = false;
        if (!(runMode.equals("test")) && !(runMode.equals("prod"))) {
            logger.error("ERROR: parameter invalid [{}={}=]", "runMode", runMode);
            throw new Exception("ERROR: parameter invalid !");
        } else {
            this.runMode = runMode;
        }
        if (runMode.equals("prod")) {
            EXT_CREATOR_ID = "barmenia_centera_pool_prod";
        } else {
            EXT_CREATOR_ID = "barmenia_centera_pool_test";
        }
        if (caller_name == "") {
            APP_CREATOR_ID = "<unknown>";
        } else {
            APP_CREATOR_ID = caller_name;
        }
        runForApplication = APP_CREATOR_ID;
        FPprimaryPool = null;
        FPreplicaPool = null;
        JDBC_DRIVER_NAME = jdbcDriverName;
        JDBC_CONNECT_URL = jdbcConnectUrl;
        JDBC_schemaObjectPrefix = "";
        JDBC_schema_path = jdbcSchemaPath;
        JCR_DRIVER_NAME = jcrDriverName;
        JCR_CONNECT_URL = jcrConnectUrl;
        try {
            java.lang.Class.forName(JDBC_DRIVER_NAME).newInstance();
        } catch (Exception ex) {
            logger.error("ERROR: can't load jdbc driver ! [{}={}=][{}]", new Object[] { "JDBC_DRIVER_NAME", JDBC_DRIVER_NAME, ex });
            throw new Exception("ERROR: can't load jdbc driver !");
        }
        stats = new AdapterStatistics();
    }

    /**	
	* 
	* Startup the adapter by doing Storage connect and db connect.
	* Init all variables.
	*
	* @return      void
	* @throws      Exception
	* @see         
	* @since       
	* @serial      (or @serialField or @serialData)
	* @deprecated  (see How and When To Deprecate APIs)
	*
	*/
    public void Startup() throws FPLibraryException, Exception {
        this.stats.StartCollecting();
        this.stats.Start("de.barmenia.cs.centera.Adapter.Startup()");
        if (!isAdapterStartedUp) {
            com.filepool.fplibrary.FPPool.setGlobalOption(com.filepool.fplibrary.FPLibraryConstants.FP_OPTION_OPENSTRATEGY, com.filepool.fplibrary.FPLibraryConstants.FP_LAZY_OPEN);
            FPprimaryPoolRefCount = 0;
            FPprimaryPool = getFPPoolConnection(PRIMARY_NODE);
            if (FPprimaryPool == null) {
                logger.error("ERROR: can't connect to PRIMARY_NODE ! [{}]", "getFPPoolConnection( PRIMARY_NODE )");
                throw new Exception("ERROR: can't connect to PRIMARY_NODE !");
            } else {
                FPprimaryPoolRefCount = FPprimaryPoolRefCount + 1;
            }
            FPreplicaPoolRefCount = 0;
            FPreplicaPool = getFPPoolConnection(REPLICA_NODE);
            if (FPreplicaPool == null) {
                logger.error("ERROR: can't connect to REPLICA_NODE ! [{}]", "getFPPoolConnection( REPLICA_NODE )");
                throw new Exception("ERROR: can't connect to REPLICA_NODE !");
            } else {
                FPreplicaPoolRefCount = FPprimaryPoolRefCount + 1;
            }
            logger.debug("INFO: de.barmenia.cs.centera.Adapter.Startup(): bUseDatabase=" + bUseDatabase + "=");
            if (bUseDatabase) {
                try {
                    dbConnection = DriverManager.getConnection(JDBC_CONNECT_URL);
                    logger.debug("INFO: db connection is open.");
                } catch (SQLException ex) {
                    logger.error("ERROR: can't open db connection ! [{}={}=][{}]", new Object[] { "JDBC_CONNECT_URL=", JDBC_CONNECT_URL, ex });
                    throw new Exception("ERROR: can't open db connection !");
                } catch (Exception e) {
                    logger.error("ERROR: can't open db connection ! [{}={}=][{}]", new Object[] { "JDBC_CONNECT_URL=", JDBC_CONNECT_URL, e });
                    throw new Exception("ERROR: can't open db connection !");
                }
                try {
                    checkSchema(JDBC_DRIVER_NAME);
                    logger.debug("INFO: db schema is checked.");
                } catch (Exception e) {
                    logger.error("ERROR: can't check database schema ! [{}={}=][{}]", new Object[] { "schema", JDBC_DRIVER_NAME, e });
                    throw new Exception("ERROR: can't check database schema !");
                }
            }
            isAdapterStartedUp = true;
            this.stats.Stop("de.barmenia.cs.centera.Adapter.Startup()");
        }
    }

    /**	
	*
	* Shutdown storage adapter, close db and storage connection and clean all internal states.
	*
	* @return      void
	* @throws      FPLibraryException, SQLException
	* @see         
	* @since       
	* @serial      (or @serialField or @serialData)
	* @deprecated  (see How and When To Deprecate APIs)
	*
	*/
    public void Shutdown() throws Exception, FPLibraryException, SQLException {
        this.stats.Start("de.barmenia.cs.centera.Adapter.Shutdown()");
        if (isAdapterStartedUp) {
            if (FPprimaryPool != null) {
                try {
                    FPprimaryPool.Close();
                    FPprimaryPool = null;
                } catch (Exception e) {
                    logger.error("ERROR: can't close centera cluster connection ! [{}][{}]", "FPprimaryPool", e);
                }
            } else {
                logger.debug("DEBUG: no open centera cluster connection ! [{}]", "FPprimaryPool");
            }
            if (FPreplicaPool != null) {
                try {
                    FPreplicaPool.Close();
                    FPreplicaPool = null;
                } catch (Exception e) {
                    logger.error("ERROR: can't close centera cluster connection ! [{}][{}]", "FPreplicaPool", e);
                }
            } else {
                logger.debug("DEBUG: no open centera cluster connection ! [{}]", "FPreplicaPool");
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                    dbConnection = null;
                } catch (Exception e) {
                    logger.error("ERROR: can't close db connection ! [{}][{}]", "dbConnection", e);
                }
            } else {
                logger.debug("DEBUG: no open db connection ! [{}]", "dbConnection");
            }
        } else {
            logger.warn("WARNING: no Startup() did occure before Shutdown() !");
        }
        this.stats.Stop("de.barmenia.cs.centera.Adapter.Shutdown()");
        this.stats.StopCollecting();
        logger.info("TIMER [{}]", this.stats.toString());
    }

    /**	
	*
	* @return      boolean
	* @see         
	* @since       
	* @serial      (or @serialField or @serialData)
	* @deprecated  (see How and When To Deprecate APIs)
	*
	*/
    public boolean getUseDatabase() {
        return (bUseDatabase);
    }

    /**	
	*
	* @param       bValue
	* @return      void
	* @see         
	* @since       
	* @serial      (or @serialField or @serialData)
	* @deprecated  (see How and When To Deprecate APIs)
	*
	*/
    public void setUseDatabase(boolean bValue) {
        bUseDatabase = bValue;
    }

    /**	
	*
	* closeStream dows what is says.
	*
	* @param       in
	* @return      void
	* @throws      ?
	* @see         
	* @since       
	* @serial      (or @serialField or @serialData)
	* @deprecated  (see How and When To Deprecate APIs)
	*
	*/
    protected void closeStream(InputStream in) throws Exception {
        this.stats.Start("de.barmenia.cs.centera.Adapter.closeStream()");
        if (in != null) {
            try {
                in.close();
            } catch (IOException ignore) {
            }
        }
        this.stats.Stop("de.barmenia.cs.centera.Adapter.closeStream()");
    }

    /**	
	*
	* closeStatement closes db statements if possible.
	*
	* @param       stmt
	* @return      void
	* @see         
	* @since       
	* @serial      (or @serialField or @serialData)
	* @deprecated  (see How and When To Deprecate APIs)
	*
	*/
    protected void closeStatement(Statement stmt) throws Exception {
        this.stats.Start("de.barmenia.cs.centera.Adapter.closeStatement()");
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException se) {
                logger.error("ERROR: can't close db Statement ! [{}]", se);
            }
        }
        this.stats.Stop("de.barmenia.cs.centera.Adapter.closeStatement()");
    }

    /**
	* Checks if the required schema objects exist and creates them if they
	* don't exist yet.
	*
	* @param       in_schema
	* @return      void
	* @throws      Exception if an error occurs
	* @see         
	* @since       
	* @serial      (or @serialField or @serialData)
	* @deprecated  (see How and When To Deprecate APIs)
	*
	*/
    protected void checkSchema(String in_schema) throws Exception {
        this.stats.Start("de.barmenia.cs.centera.Adapter.checkSchema()");
        String schema = "";
        DatabaseMetaData metaData = null;
        ResultSet rs = null;
        if (in_schema.equals("com.mysql.jdbc.Driver")) {
            schema = "mysql";
        }
        if (in_schema.equals("com.informix.jdbc.IfxDriver")) {
            schema = "informix";
        }
        if (schema.equals("")) {
            logger.error("ERROR: parameter invalid ! [{}={}]", "in_schema", in_schema);
            throw new IllegalArgumentException("ERROR: parameter invalid !");
        }
        try {
            metaData = dbConnection.getMetaData();
        } catch (Exception e) {
            logger.error("ERROR: can't get db metadata ! [{}][{}]", "dbConnection", e);
            return;
        }
        String tableName = JDBC_schemaObjectPrefix + "app_to_ext";
        if (metaData.storesLowerCaseIdentifiers()) {
            tableName = tableName.toLowerCase();
        } else if (metaData.storesUpperCaseIdentifiers()) {
            tableName = tableName.toUpperCase();
        }
        try {
            rs = metaData.getTables(null, null, tableName, null);
        } catch (Exception e) {
            logger.error("ERROR: can't perform db metadata getTables() ! [{}={}][{}]", new Object[] { "tableName", tableName, e });
            return;
        }
        boolean schemaExists;
        try {
            schemaExists = rs.next();
        } finally {
            rs.close();
        }
        if (!schemaExists) {
            logger.warn("WARNING: db metadata schema does not exists ! try to create it ...");
            BufferedReader reader = new BufferedReader(new FileReader(new File(JDBC_schema_path + appFileSeparator + schema + ".ddl")));
            Statement stmt = dbConnection.createStatement();
            try {
                String sql = reader.readLine();
                while (sql != null) {
                    if (sql.equals("")) {
                    } else {
                        logger.debug("DO: stmt.executeUpdate(sql={}=);", sql);
                        stmt.executeUpdate(sql);
                    }
                    sql = reader.readLine();
                }
            } finally {
                closeStatement(stmt);
            }
        }
        this.stats.Stop("de.barmenia.cs.centera.Adapter.checkSchema()");
    }

    /**
	*
	* determine the ip list of the centera cluster configuration.
	* You can choose if PRIMARY cluster and REPLICATE cluster ip list.
	*
	* TODO: This is actually a hardcoded list of the EMC Centera test cluster on the
	*       internet for first application validation. For internal usage we have to
	*       replace this method implementation by property read proccess for configuration
	*       files. 
	*
	* TODO: Question - Should we load configuration depending runMode to be able to connect to
	*       different system for devlopment/test and production usage ?
	*       Would this need a real separat centera ? Or can we simulate test/dev usage in theproduction cluster ?
	*
	*
	* @param       nodeType
	* @return      String[] list of ip numbers
	* @throws      ?
	* @see         
	* @since       
	* @serial      (or @serialField or @serialData)
	* @deprecated  (see How and When To Deprecate APIs)
	*
	*/
    private String[] getIpList(String nodeType) throws Exception {
        this.stats.Start("de.barmenia.cs.centera.Adapter.getIpList()");
        String ipList[] = new String[4];
        int i = 0;
        if (nodeType.equals(PRIMARY_NODE)) {
            i = 0;
            ipList[i++] = "128.221.200.56";
            ipList[i++] = "128.221.200.57";
            ipList[i++] = "128.221.200.58";
            ipList[i++] = "128.221.200.59";
        }
        if (nodeType.equals(REPLICA_NODE)) {
            i = 0;
            ipList[i++] = "128.221.200.60";
            ipList[i++] = "128.221.200.61";
            ipList[i++] = "128.221.200.62";
            ipList[i++] = "128.221.200.63";
        }
        this.stats.Stop("de.barmenia.cs.centera.Adapter.getIpList()");
        return (ipList);
    }

    /**	
	*
	* Open connectioon to storage and return connection handle back.
	*
	* @param       nodeType
	* @return      FPPool
	* @throws      FPLibraryException
	* @see         
	* @since       
	* @serial      (or @serialField or @serialData)
	* @deprecated  (see How and When To Deprecate APIs)
	*
	*/
    private FPPool getFPPoolConnection(String nodeType) throws Exception, FPLibraryException {
        this.stats.Start("de.barmenia.cs.centera.Adapter.getFPPoolConnection()");
        String[] ipList = null;
        FPPool outPool = null;
        try {
            ipList = getIpList(nodeType);
            if (ipList == null) {
                throw new FPLibraryException(com.filepool.fplibrary.FPLibraryErrors.FP_NO_POOL_ERR, "can't get ip list of nodyType=" + nodeType);
            }
            if (ipList.length == 0) {
                throw new FPLibraryException(com.filepool.fplibrary.FPLibraryErrors.FP_NO_POOL_ERR, "can't get ip list of nodyType=" + nodeType);
            }
            outPool = new FPPool(ipList);
        } catch (Exception e) {
            logger.error("ERROR: can't get storage connection ! [{}]", e);
        }
        this.stats.Stop("de.barmenia.cs.centera.Adapter.getFPPoolConnection()");
        return (outPool);
    }

    public FPPool getConnection() {
        FPprimaryPoolRefCount = FPprimaryPoolRefCount + 1;
        FPreplicaPoolRefCount = FPreplicaPoolRefCount + 1;
        return (FPprimaryPool);
    }

    /**
	*
	* WriteClipSetup creates a new clip and fills it with basic informaction.
	*
	* @param       sClipName
	* @param       sAppVendor
	* @param       sAppName
	* @param       sAppVersion
	* @param       lRetentionPeriod
	* @return      FPClip
	* @throws      FPLibraryException
	* @see         
	* @since       
	* @serial      (or @serialField or @serialData)
	* @deprecated  (see How and When To Deprecate APIs)
	*
	*/
    private FPClip WriteClipSetup(String sClipName, String sAppVendor, String sAppName, String sAppVersion, long lRetentionPeriod) throws Exception, FPLibraryException {
        this.stats.Start("de.barmenia.cs.centera.Adapter.WriteClipSetup()");
        FPClip theClip = null;
        try {
            theClip = new FPClip(FPprimaryPool, sClipName);
            theClip.setDescriptionAttribute("app-vendor", sAppVendor);
            theClip.setDescriptionAttribute("app-name", sAppName);
            theClip.setDescriptionAttribute("app-version", sAppVersion);
            theClip.setRetentionPeriod(lRetentionPeriod);
        } catch (Exception e) {
            logger.error("ERROR: WriteClipSetup() faild ! [WriteClipSetup( sClipName={}=, sAppVendor={}=, sAppName={}=, lRetentionPeriod=" + lRetentionPeriod + "=][{}]", new Object[] { sClipName, sAppVendor, sAppName, e });
        }
        this.stats.Stop("de.barmenia.cs.centera.Adapter.WriteClipSetup()");
        return (theClip);
    }

    /**	
	*
	* SelectRelationship registers two pairs (app_creator, app_id) (ext_creator. ext_id) in a database.
	*
	* @param       in_app_creator_id
	* @param       in_app_object_id
	* @return      String
	* @throws      Exception
	* @see         
	* @since       
	* @serial      (or @serialField or @serialData)
	* @deprecated  (see How and When To Deprecate APIs)
	*
	*/
    public String SelectRelationship(String in_app_creator_id, String in_app_object_id) throws Exception {
        this.stats.Start("de.barmenia.cs.centera.Adapter.SelectRelationship()");
        java.sql.Statement stmt = null;
        java.sql.ResultSet rs = null;
        String clipID = "";
        String sSQL = "";
        if (this.JDBC_DRIVER_NAME.equals("com.mysql.jdbc.Driver")) {
            sSQL = "SELECT EXT_OBJECT_ID FROM APP_TO_EXT WHERE APP_CREATOR_ID='" + in_app_creator_id + "' AND APP_OBJECT_ID='" + in_app_object_id + "' AND EXT_CREATOR_ID='" + this.EXT_CREATOR_ID + "'";
        }
        if (this.JDBC_DRIVER_NAME.equals("com.informix.jdbc.IfxDriver")) {
            sSQL = "SELECT EXT_OBJECT_ID FROM APP_TO_EXT WHERE APP_CREATOR_ID='" + in_app_creator_id + "' AND APP_OBJECT_ID='" + in_app_object_id + "' AND EXT_CREATOR_ID='" + this.EXT_CREATOR_ID + "'";
        }
        if (!bUseDatabase) {
            logger.warn("WARNING: do not use database ! [bUseDatabase=" + bUseDatabase + "][sSQL={}=", sSQL);
            return (clipID);
        }
        try {
            stmt = dbConnection.createStatement();
            rs = stmt.executeQuery(sSQL);
            if (rs.next()) {
                clipID = rs.getObject(1) != null ? ((String) rs.getObject(1)) : null;
                if (rs.next()) {
                    clipID = "";
                    logger.error("ERROR: duplicate results found ! [sSQL={}]", sSQL);
                    throw new Exception("ERROR: duplicate results found !");
                }
            } else {
                logger.error("ERROR: no results found ! [sSQL={}]", sSQL);
                throw new Exception("ERROR: no results found !");
            }
        } catch (Exception e) {
            logger.error("ERROR: excution faild ! [sSQL={}][{}]", sSQL, e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                    rs = null;
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                    stmt = null;
                }
            }
        }
        this.stats.Stop("de.barmenia.cs.centera.Adapter.SelectRelationship()");
        return (clipID);
    }

    /**
	*
	* Check if (in_app_creator_id, in_app_object_id) is known in relationships.
	*
	* @param       in_app_creator_id 
	* @param       in_app_object_id
	* @return      -1 = does not exsists
	*               0 = exactly one match found
	*               1 = duplicates found
	* @throws      Exception
	* @see         
	* @since       
	* @serial      (or @serialField or @serialData)
	* @deprecated  (see How and When To Deprecate APIs)
	*
	*/
    public int ExistsRelationship(String in_app_creator_id, String in_app_object_id) throws Exception {
        this.stats.Start("de.barmenia.cs.centera.Adapter.ExistsRelationship()");
        int iResult = -1;
        java.sql.Statement stmt = null;
        java.sql.ResultSet rs = null;
        String clipID = "";
        String sSQL = "";
        if (this.JDBC_DRIVER_NAME.equals("com.mysql.jdbc.Driver")) {
            sSQL = "SELECT EXT_OBJECT_ID, EXT_CREATOR_ID FROM APP_TO_EXT WHERE APP_CREATOR_ID='" + in_app_creator_id + "' AND APP_OBJECT_ID='" + in_app_object_id + "'";
        }
        if (this.JDBC_DRIVER_NAME.equals("com.informix.jdbc.IfxDriver")) {
            sSQL = "SELECT EXT_OBJECT_ID, EXT_CREATOR_ID FROM APP_TO_EXT WHERE APP_CREATOR_ID='" + in_app_creator_id + "' AND APP_OBJECT_ID='" + in_app_object_id + "'";
        }
        if (!bUseDatabase) {
            logger.warn("ExistsRelationship(): WARNING: getUseDatabase() = " + bUseDatabase + "= WANT TO DO=" + sSQL);
            return (iResult);
        }
        try {
            stmt = dbConnection.createStatement();
            rs = stmt.executeQuery(sSQL);
            if (rs.next()) {
                iResult = 0;
                clipID = rs.getObject(1) != null ? ((String) rs.getObject(1)) : null;
                if (rs.next()) {
                    iResult = 1;
                }
            }
            return (iResult);
        } catch (Exception e) {
            logger.error("ERROR: ExistsRelationship faild ! [{}={}][{}]", new Object[] { "sSQL", sSQL, e });
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                    rs = null;
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                    stmt = null;
                }
            }
        }
        this.stats.Stop("de.barmenia.cs.centera.Adapter.ExistsRelationship()");
        return (iResult);
    }

    /**	
	* InsertRelationship registers the application id and the storage internal id in a db table so we can access
	* a document by only knowing the application id.
	*
	* @param       in_app_creator_id
	* @param       in_app_object_id
	* @param       in_ext_creator_id
	* @param       in_ext_object_id
	* @return      void
	* @throws      Exception
	* @author      Frank.Schulte@barmenia.de
	* @see         
	* @since       
	* @serial      (or @serialField or @serialData)
	* @deprecated  (see How and When To Deprecate APIs)
	*
	*/
    public void InsertRelationship(String in_app_creator_id, String in_app_object_id, String in_ext_creator_id, String in_ext_object_id) throws Exception {
        this.stats.Start("de.barmenia.cs.centera.Adapter.InsertRelationship()");
        String sSQL_mysql = "";
        String sSQL_ifx = "";
        String sSQL = "";
        java.sql.Statement stmt = null;
        java.sql.ResultSet rs = null;
        sSQL_mysql = "INSERT INTO APP_TO_EXT (APP_CREATOR_ID, APP_OBJECT_ID, EXT_CREATOR_ID, EXT_OBJECT_ID, CREATE_DATE, UPDATE_DATE, DELETE_DATE, STATUS, HISTORY ) VALUES ('" + in_app_creator_id + "', '" + in_app_object_id + "', '" + in_ext_creator_id + "', '" + in_ext_object_id + "', SYSDATE(), NULL, NULL, NULL, 'CREATE DATASET' )";
        sSQL_ifx = "INSERT INTO APP_TO_EXT (APP_CREATOR_ID, APP_OBJECT_ID, EXT_CREATOR_ID, EXT_OBJECT_ID, CREATE_DATE, UPDATE_DATE, DELETE_DATE, STATUS, HISTORY ) VALUES ('" + in_app_creator_id + "', '" + in_app_object_id + "', '" + in_ext_creator_id + "', '" + in_ext_object_id + "', TODAY, NULL, NULL, NULL, NULL )";
        sSQL_mysql = "INSERT INTO APP_TO_EXT (APP_CREATOR_ID, APP_OBJECT_ID, EXT_CREATOR_ID, EXT_OBJECT_ID, EXT_PART_ID, EXT_PART_HOFF_BEGIN, EXT_PART_HOFF_END, EXT_PART_POFF_BEGIN, EXT_PART_POFF_END, BLOB_MIMETYPE, BLOB_PART_MIMETYPE, BLOB_LENGTH, BLOB_PAGES, BLOB_PAGE_FROM, BLOB_PAGE_TO, CREATE_DATE, UPDATE_DATE, DELETE_DATE, STATUS, HISTORY, DOCUMENT_DESC, BLOB_DATA) VALUES ('" + in_app_creator_id + "', '" + in_app_object_id + "', '" + in_ext_creator_id + "', '" + in_ext_object_id + "', null,    null,                null,              null,                null,              'application/pdf', null,               null,        null,       null,           null,         SYSDATE(),   NULL,        NULL,        NULL,   'CREATE DATASET', 'exsample description', null)";
        sSQL_ifx = "INSERT INTO APP_TO_EXT (APP_CREATOR_ID, APP_OBJECT_ID, EXT_CREATOR_ID, EXT_OBJECT_ID, EXT_PART_ID, EXT_PART_HOFF_BEGIN, EXT_PART_HOFF_END, EXT_PART_POFF_BEGIN, EXT_PART_POFF_END, BLOB_MIMETYPE, BLOB_PART_MIMETYPE, BLOB_LENGTH, BLOB_PAGES, BLOB_PAGE_FROM, BLOB_PAGE_TO, CREATE_DATE, UPDATE_DATE, DELETE_DATE, STATUS, HISTORY, DOCUMENT_DESC, BLOB_DATA) VALUES ('" + in_app_creator_id + "', '" + in_app_object_id + "', '" + in_ext_creator_id + "', '" + in_ext_object_id + "', null,    null,                null,              null,                null,              'application/pdf', null,               null,        null,       null,           null,         TODAY    ,   NULL,        NULL,        NULL,   'CREATE DATASET', 'exsample description', null)";
        if (this.JDBC_DRIVER_NAME.equals("com.mysql.jdbc.Driver")) {
            sSQL = sSQL_mysql;
        }
        if (this.JDBC_DRIVER_NAME.equals("com.informix.jdbc.IfxDriver")) {
            sSQL = sSQL_ifx;
        }
        if (sSQL.equals("")) {
            logger.error("ERROR: invalid paramter ! [{}={}=]", "JDBC_DRIVER_NAME", JDBC_DRIVER_NAME);
            throw new IllegalArgumentException("ERROR: JDBC_DRIVER_NAME is unknown !");
        }
        if (!bUseDatabase) {
            logger.warn("WARNING: InsertRelationship() - db is deactivated ! [{}={}=]", "sSQL", sSQL);
            this.stats.Stop("de.barmenia.cs.centera.Adapter.InsertRelationship()");
            return;
        }
        try {
            stmt = dbConnection.createStatement();
            stmt.executeUpdate(sSQL);
        } catch (Exception e) {
            logger.error("ERROR: can't InsertRelationship ! [{}={}=][{}={}=][{}]", new Object[] { "dbConnection", dbConnection, "sSQL", sSQL, e });
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                } catch (SQLException sqlEx) {
                    logger.error("ERROR: can't close rs ! [{}]", sqlEx);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                    stmt = null;
                } catch (SQLException sqlEx) {
                    logger.error("ERROR: can't close stmt ! [{}]", sqlEx);
                }
            }
        }
        this.stats.Stop("de.barmenia.cs.centera.Adapter.InsertRelationship()");
    }

    /**	
	* Push document identified by the parameter set into storage.
	*
	* @param       in_array
	* @return      String id of document on storage
	* @throws      Exception
	* @author      Frank.Schulte@barmenia.de
	* @see         
	* @since       
	* @serial      (or @serialField or @serialData)
	* @deprecated  (see How and When To Deprecate APIs)
	*
	*/
    public String Push(byte in_array[]) throws Exception {
        this.stats.Start("de.barmenia.cs.centera.Adapter.Push_A()");
        FPClip theClip = null;
        long retentionPeriod = 0;
        String clipID = "";
        try {
            theClip = WriteClipSetup(CLIP_NAME, this.APP_VENDOR, this.TAG_NAME, this.APP_VERSION, retentionPeriod);
            if (theClip == null) {
                logger.error("ERROR: WriteClipSetup() faild ! [WriteClipSetup( {}, {}, {}, {}, ... )]", new Object[] { CLIP_NAME, this.APP_VENDOR, this.TAG_NAME, this.APP_VERSION });
                throw new Exception("ERROR: WriteClipSetup() faild !");
            }
            FPTag topTag = theClip.getTopTag();
            FPTag newTag = new FPTag(topTag, this.TAG_NAME);
            topTag.Close();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(in_array);
            newTag.setAttribute("filename", "ByteArrayInputStream");
            logger.debug("DEBUG: TRY - WRITE BYTE ARRAY ! [{}={}=][{}={}=]", "in_array", in_array);
            logger.debug("DEBUG: BlobWrite(<byte_array>) started !");
            newTag.BlobWrite(inputStream);
            logger.debug("DEBUG: BlobWrite(<file>) finished !");
            clipID = theClip.Write();
            logger.debug("DEBUG: byte array stored ! [{}={}=]", "clipID", clipID);
            inputStream.close();
            newTag.Close();
            theClip.Close();
            logger.debug("DEBUG: Closed stream, tag and clip !");
        } catch (Exception e) {
            logger.error("ERROR: can't write byte array ! [{}={}=][{}]", new Object[] { "in_array", in_array.toString(), e });
        }
        this.stats.Stop("de.barmenia.cs.centera.Adapter.Push_A()");
        return (clipID);
    }

    /**	
	* Push document identified by the parameter set into storage.
	*
	* @param       in_filename
	* @return      String id of document on storage
	* @throws      Exception
	* @author      Frank.Schulte@barmenia.de
	* @see         
	* @since       
	* @serial      (or @serialField or @serialData)
	* @deprecated  (see How and When To Deprecate APIs)
	*
	*/
    public String Push(String in_filename) throws Exception {
        this.stats.Start("de.barmenia.cs.centera.Adapter.Push_B()");
        FPClip theClip = null;
        long retentionPeriod = 0;
        String clipID = "";
        try {
            theClip = WriteClipSetup(CLIP_NAME, this.APP_VENDOR, this.TAG_NAME, this.APP_VERSION, retentionPeriod);
            if (theClip == null) {
                logger.error("ERROR: WriteClipSetup() faild ! [WriteClipSetup( {}, {}, {}, {}, ... )]", new Object[] { CLIP_NAME, this.APP_VENDOR, this.TAG_NAME, this.APP_VERSION });
                throw new Exception("ERROR: WriteClipSetup() faild !");
            }
            FPTag topTag = theClip.getTopTag();
            FPTag newTag = new FPTag(topTag, TAG_NAME);
            topTag.Close();
            FPFileInputStream inputStream = new FPFileInputStream(new File(in_filename));
            newTag.setAttribute("filename", in_filename);
            logger.debug("DEBUG: TRY - WRITE FILE ! [{}={}=][{}={}=]", "file", in_filename);
            logger.debug("DEBUG: BlobWrite(<file>) started !");
            newTag.BlobWrite(inputStream);
            logger.debug("DEBUG: BlobWrite(<file>) finished !");
            clipID = theClip.Write();
            logger.debug("DEBUG: file stored ! [{}={}=]", "clipID", clipID);
            inputStream.close();
            newTag.Close();
            theClip.Close();
            logger.debug("DEBUG: Closed file, tag and clip !");
        } catch (FileNotFoundException e) {
            logger.error("ERROR: can't find file ! [{}={}=][{}]", new Object[] { "in_filename", in_filename, e });
            throw new IllegalArgumentException("ERROR: can't open file for reading ! [file=" + in_filename + "]");
        } catch (IOException e) {
            logger.error("ERROR: can't read from file ! [{}={}=][{}]", new Object[] { "in_filename", in_filename, e });
        }
        this.stats.Stop("de.barmenia.cs.centera.Adapter.Push_B()");
        return (clipID);
    }

    /**
	* Push document identified by the parameter set into storage.
	*
	* Push: write byte array to centera storage and associate it with the "in_app_object_id" in a database
	*       so you can "Pull" this byte array back from centera by giving this "in_app_object_id".
	*
	*       Internally we save ( "in_app_creator_id", "in_app_object_id", "centera_clip_id" ) to a database
	*       to lookup the object again.
	*
	*       "in_app_creator_id" is used to add application domains that manage for themselves the uniqe 
	*       "in_app_object_id", so we have not to worry about duplicates so long the application is handling
	*       duplicate colision itself.
	*
	*       example: Push( "pr2dms", generatedKey, documentByteArray );
	*
	* @param       in_app_creator_id
	* @param       in_app_object_id
	* @param       in_array
	* @return      void
	* @throws      FPLibraryException, Exception
	* @author      Frank.Schulte@barmenia.de
	* @see         
	* @since       
	* @serial      (or @serialField or @serialData)
	* @deprecated  (see How and When To Deprecate APIs)
	*
	*/
    public void Push(String in_app_creator_id, String in_app_object_id, byte in_array[]) throws FPLibraryException, Exception {
        this.stats.Start("de.barmenia.cs.centera.Adapter.Push_C()");
        String clipID = "";
        try {
            logger.debug("DEBUG: Push(<byte_array>) started !");
            clipID = Push(in_array);
            InsertRelationship(in_app_creator_id, in_app_object_id, this.EXT_CREATOR_ID, clipID);
            logger.debug("DEBUG: Push(<byte_array>) finished ! [{}={}=]", "clipID", clipID);
        } catch (Exception e) {
            logger.error("ERROR: can't push byte array to storage ! [{}={}=][{}={}=][{}={}=][{}]", new Object[] { "in_app_creator_id", in_app_creator_id, "in_app_object_id", in_app_object_id, "in_array", in_array, e });
            throw new Exception("ERROR: can't push byte array to storage !");
        }
        this.stats.Stop("de.barmenia.cs.centera.Adapter.Push_C()");
    }

    /**
	* Push document identified by the parameter set into storage.
	*
	* @return clipID
	* @throws FPLibraryException
	*
	* @param       in_app_creator_id
	* @param       in_app_object_id
	* @param       in_filename
	* @return      void
	* @throws      FPLibraryException, Exception
	* @author      Frank.Schulte@barmenia.de
	* @see         
	* @since       
	* @serial      (or @serialField or @serialData)
	* @deprecated  (see How and When To Deprecate APIs)
	*
	*/
    public void Push(String in_app_creator_id, String in_app_object_id, String in_filename) throws FPLibraryException, Exception {
        this.stats.Start("de.barmenia.cs.centera.Adapter.Push_D()");
        String clipID = "";
        try {
            logger.debug("DEBUG: Push(<file>) started !");
            clipID = Push(in_filename);
            InsertRelationship(in_app_creator_id, in_app_object_id, this.EXT_CREATOR_ID, clipID);
            logger.debug("DEBUG: Push(<file>) finished ! [{}={}=]", "clipID", clipID);
        } catch (Exception e) {
            logger.error("ERROR: can't push file to storage ! [{}={}=][{}={}=][{}={}=][{}]", new Object[] { "in_app_creator_id", in_app_creator_id, "in_app_object_id", in_app_object_id, "in_filename", in_filename, e });
            throw new Exception("ERROR: can't push file to storage !");
        }
        this.stats.Stop("de.barmenia.cs.centera.Adapter.Push_D()");
    }

    /**	
	*
	* Pull document identified by the parameter set into a file.
	* <p>
	* @param       in_clipID
	* @param       in_filename_to_write_to
	* @return      void
	* @throws      FPLibraryException
	* @author      Frank.Schulte@barmenia.de
	* @see         
	* @since       
	* @serial      (or @serialField or @serialData)
	* @deprecated  (see How and When To Deprecate APIs)
	*
	*/
    public void Pull(String in_clipID, String in_filename_to_write_to) throws Exception, FPLibraryException {
        this.stats.Start("de.barmenia.cs.centera.Adapter.Pull_A()");
        long exitCode = 0;
        String clipID = null;
        FileOutputStream outFile = null;
        FPClip theClip = null;
        FPTag topTag = null;
        PrintStream outFilePrint = null;
        try {
            clipID = in_clipID;
            try {
                theClip = new FPClip(FPprimaryPool, clipID, FPLibraryConstants.FP_OPEN_FLAT);
                logger.debug("DEBUG: clipID found ! [{}={}=][{}={}=]", new Object[] { "FPprimaryPool", FPprimaryPool, "clipID", clipID, "FPLibraryConstants.FP_OPEN_FLAT" });
            } catch (Exception e) {
                logger.error("ERROR: can't find clipID  ! [{}={}=][{}={}=][{}]", new Object[] { "FPprimaryPool", FPprimaryPool, "clipID", clipID, "FPLibraryConstants.FP_OPEN_FLAT", e });
                this.stats.Stop("de.barmenia.cs.centera.Adapter.Pull_A()");
                return;
            }
            topTag = theClip.getTopTag();
            if (!topTag.getTagName().equals(this.TAG_NAME)) {
                logger.error("ERROR: document was not written by us ! [usTAG_NAME={}=, foundTAG_NAME={}=]", this.TAG_NAME, topTag.getTagName());
                throw new IllegalArgumentException("ERROR: document was not written by us !");
            }
            String origFilename = topTag.getStringAttribute("filename");
            String saveFilename = in_filename_to_write_to;
            String saveFilenameInfo = in_filename_to_write_to + ".original_filename";
            outFile = new FileOutputStream(in_filename_to_write_to);
            logger.debug("DEBUG: TRY - READ CLIP TO FILE ! [{}={}=][{}={}=][{}={}=]", new Object[] { "clipID", clipID, "file", in_filename_to_write_to, "orig_file", origFilename });
            logger.debug("DEBUG: BlobRead(<out_array>) started !");
            topTag.BlobRead(outFile);
            logger.debug("DEBUG: BlobRead(<out_array>) finished !");
            outFile = new FileOutputStream(saveFilenameInfo);
            outFilePrint = new PrintStream(outFile);
            outFilePrint.println("ORIGINAL_FILENAME=" + origFilename + "\n");
            outFilePrint.close();
            outFile.close();
            topTag.Close();
            theClip.Close();
            logger.debug("DEBUG: Closed file, tag and clip !");
        } catch (FPLibraryException e) {
            exitCode = e.getErrorCode();
            logger.error("ERROR: Centera SDK occured ! [{}]", e);
        } catch (IllegalArgumentException e) {
            exitCode = -1;
            logger.error("ERROR: IllegalArgumentException occured ! [{}]", e);
        } catch (IOException e) {
            exitCode = -1;
            logger.error("ERROR: IOException occured ! [{}]", e);
        }
        this.stats.Stop("de.barmenia.cs.centera.Adapter.Pull_A()");
    }

    /**	
	*
	* Pull document identified by the parameter set into a byte array.
	* <p>
	* @param       in_clipID
	* @param       out_array
	* @return      void
	* @throws      FPLibraryException
	* @author      Frank.Schulte@barmenia.de
	* @see         Push, Pull
	* @since       
	* @serial      (or @serialField or @serialData)
	* @deprecated  (see How and When To Deprecate APIs)
	*
	*/
    public void Pull(String in_clipID, byte out_array[]) throws Exception, FPLibraryException {
        this.stats.Start("de.barmenia.cs.centera.Adapter.Pull_B()");
        long exitCode = 0;
        try {
            String clipID = in_clipID;
            FPClip theClip = new FPClip(FPprimaryPool, clipID, FPLibraryConstants.FP_OPEN_FLAT);
            logger.debug("DEBUG: clipID found !");
            FPTag topTag = theClip.getTopTag();
            if (!topTag.getTagName().equals(TAG_NAME)) {
                logger.error("ERROR: document was not written by us ! [usTAG_NAME={}=, foundTAG_NAME={}=]", TAG_NAME, topTag.getTagName());
                throw new IllegalArgumentException("ERROR: document was not written by us !");
            }
            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
            logger.debug("DEBUG: BlobRead(<out_array>) started !");
            topTag.BlobRead(outByteStream);
            out_array = outByteStream.toByteArray();
            logger.debug("DEBUG: BlobRead(<out_array>) finished !");
            outByteStream.close();
            topTag.Close();
            theClip.Close();
            logger.debug("DEBUG: Closed file, tag and clip !");
        } catch (FPLibraryException e) {
            exitCode = e.getErrorCode();
            logger.error("ERROR: Centera SDK occured ! [{}]", e);
        } catch (IllegalArgumentException e) {
            exitCode = -1;
            logger.error("ERROR: IllegalArgumentException occured ! [{}]", e);
        } catch (IOException e) {
            exitCode = -1;
            logger.error("ERROR: IOException occured ! [{}]", e);
        }
        this.stats.Stop("de.barmenia.cs.centera.Adapter.Pull_B()");
    }

    /**
	* Pull: Read byte array from centera storage associated with the "in_app_object_id".
	*
	*       Internally we lookup ( "in_app_creator_id", "in_app_object_id", "centera_clip_id" ) in a database
	*       and read the blob by the centera internal "centera_clip_id".
	*
	*       "in_app_creator_id" is used to add application domains that manage for themselves the unique 
	*       "in_app_object_id", so we have not to worry about duplicates so long the application is handling
	*       duplicate colision itself.
	*
	*       example:
	*            byte[] out_documentByteArray = null;
	*
	*            Pull( "pr2dms", generatedKey, out_documentByteArray );
	*
	* Pull document identified by the parameter set into a byte array.
	* <p>
	* @param       in_app_creator_id
	* @param       in_app_object_id
	* @param       out_array
	* @return      void
	* @throws      Exception
	* @author      Frank.Schulte@barmenia.de
	* @see         Push, Pull
	* @since       
	* @serial      (or @serialField or @serialData)
	* @deprecated  (see How and When To Deprecate APIs)
	*
	*/
    public void Pull(String in_app_creator_id, String in_app_object_id, byte out_array[]) throws Exception {
        this.stats.Start("de.barmenia.cs.centera.Adapter.Pull_C()");
        String clipID = "";
        try {
            clipID = SelectRelationship(in_app_creator_id, in_app_object_id);
            Pull(clipID, out_array);
        } catch (Exception e) {
            logger.error("ERROR: can't pull document into byte array ! [{}={}][{}={}][{}={}][{}]", new Object[] { "in_app_creator_id", in_app_creator_id, "in_app_object_id", in_app_object_id, "out_array", out_array, e });
            throw new Exception("ERROR: can't pull document into byte array !");
        }
        this.stats.Stop("de.barmenia.cs.centera.Adapter.Pull_C()");
    }

    /**	
	* 
	* Pull document identified by the parameter set into a file.
	* <p>
	* @param       in_app_creator_id
	* @param       in_app_object_id
	* @param       in_filename
	* @return      void
	* @throws      FPLibraryException, Exception
	* @author      Frank.Schulte@barmenia.de
	* @see         Push, Pull
	* @since       
	* @serial      (or @serialField or @serialData)
	* @deprecated  (see How and When To Deprecate APIs)
	*
	*/
    public void Pull(String in_app_creator_id, String in_app_object_id, String in_filename) throws FPLibraryException, Exception {
        this.stats.Start("de.barmenia.cs.centera.Adapter.Pull_D()");
        String clipID = "";
        try {
            clipID = SelectRelationship(in_app_creator_id, in_app_object_id);
            Pull(clipID, in_filename + "-" + clipID);
        } catch (Exception e) {
            logger.error("ERROR: can't pull document into file ! [{}={}][{}={}][{}={}][{}]", new Object[] { "in_app_creator_id", in_app_creator_id, "in_app_object_id", in_app_object_id, "in_filename", in_filename, e });
            throw new Exception("ERROR: can't pull document into file !");
        }
        this.stats.Stop("de.barmenia.cs.centera.Adapter.Pull_D()");
    }

    /**
	*
	* JTA - Java Transaction Service handling.
	*
	*/
    public void begin() {
    }

    public void commit() {
    }

    public int getStatus() {
        int myStatus = 0;
        return myStatus;
    }

    public void rollback() {
    }

    public void setRollbackOnly() {
    }

    public void setTransactionTimeout(int seconds) {
    }
}
