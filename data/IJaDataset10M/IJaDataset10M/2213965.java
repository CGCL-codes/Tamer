package org.jbjf.xml;

import java.util.List;
import org.jbjf.util.APIEncryption;
import org.jdom.Attribute;
import org.jdom.Element;

/**
 * The <code>JBJFDatabaseConnection</code> is a simple class object that
 * stores the <code>String</code> properties of a single JDBC database
 * connection definition.  The original class was developed to support
 * the <code>JBJFBatchDefinition</code> class object.  For details
 * on the <code>JBJFBatchDefinition</code> go to the See Also: section
 * and click on the JBJFBatchDefinition link.
 * <p>
 * From the JBJF Batch Definition, the <code>JBJFDatabaseConnection</code> object
 * represents a single database connection as defined in an JBJF Batch 
 * Definition file.  The properties of a database connection are :
 * <p>
 * <li>name - Name of the connection.  This must be unique within a
 * given JBJF Batch Definition file so that the <code>JBJFBatchDefinition</code>
 * object can fetch a connection by name.
 * <li>driver - A JDBC fully qualfied class file name that represents
 * the entry point for the JDBC <code>DriverManager</code> to instantiate.
 * <li>server - The name of the remote server where the physical
 * database resides...jbjf-001.adym.com, jbjf-002.adym.com, etc...
 * <li>database - Name of the database...XE, XE1, etc...
 * <li>port - For some DBMS engines you need a port number.  For
 * example, Oracle and MySQL.
 * <li>usr - Userid to access the database...this is 
 * encrypted using the APIEncrpytion class.
 * <li>pwd - Password for the userid to access the database...this is 
 * encrypted using the APIEncrpytion class.
 * <li>client - A <code>String</code> that contains the JDBC driver
 * name that gets placed onto the connection string...jdbc:oracle:thin.
 * <pre>
 * -------------- 
 * <b>History </b>: Begin 
 * -------------- 
 * &lt;history&gt;
 * &nbsp;&nbsp;&lt;change&gt; 
 *     1.0.0; ASL; Jul 24, 2006
 *     Initial version created and customized for the ...
 *     Naming Conventions 
 *     ------------------
 *     Scope Conventions
 *       >> g - global
 *       >> m - module/class
 *       >> l - local/method/function
 *     Variable Conventions
 *       >> str - string, text, character
 *       >> lng - integer, long, numeric
 *       >> flt - real, floating point
 *       >> the - object, class, module
 *     Examples
 *       >> lstrName - local string to contain name
 *       >> glngVerbose - global integer indicator for verbose mode
 *       >> mtheScanner - class/module for a document scanner
 * &nbsp;&nbsp;&lt;/change&gt; 
 * &lt;/history&gt; 
 * -------------- 
 * <b>History </b>: End 
 * -------------- 
 * </pre>
 * @see JBJFBatchDefinition
 * @see JBJFBaseDefinition
 * @author  Adym S. Lincoln<br>
 * Copyright (C) 2007. JBJF  All rights reserved.
 * @version 1.3.0
 * @since   1.0.0
 * @deprecated
 * <p>
 */
public class JBJFDatabaseConnection {

    /** 
     * Stores a fully qualified class name.  Used for debugging and 
     * auditing.
     * @since 1.0.0
     */
    public static final String ID = JBJFDatabaseConnection.class.getName();

    /** 
     * Stores the class name, primarily used for debugging and so 
     * forth.  Used for debugging and auditing.
     * @since 1.0.0
     */
    private String SHORT_NAME = "JBJFDatabaseConnection()";

    /** 
     * Stores a <code>SYSTEM IDENTITY HASHCODE</code>.  Used for
     * debugging and auditing.
     * @since 1.0.0
     */
    private String SYSTEM_IDENTITY = String.valueOf(System.identityHashCode(this));

    /** A constant that represents the name of an XML element.  */
    public final String ATTRBIUTE_XML_NAME = "name";

    /** A constant that represents the name of an XML element.  */
    public final String ELEMENT_XML_TYPE = "type";

    /** A constant that represents the driver parameter XML element.  */
    public final String ELEMENT_XML_DRIVER = "driver";

    /** A constant that represents the server parameter XML element.  */
    public final String ELEMENT_XML_SERVER = "server";

    /** A constant that represents the database parameter XML element.  */
    public final String ELEMENT_XML_DATABASE = "database";

    /** A constant that represents the database parameter XML element.  */
    public final String ELEMENT_XML_PORT = "port";

    /** A constant that represents the user parameter XML element.  */
    public final String ELEMENT_XML_USER = "usr";

    /** A constant that represents the password parameter XML element.  */
    public final String ELEMENT_XML_PASS = "pwd";

    /** A constant that represents the name of an XML element.  */
    public final String ELEMENT_XML_CLIENT = "client";

    /** Class property that stores the name of the connection definition. */
    private String mstrName;

    /** 
     * Class property that stores the fully qualified class name of 
     * the JDBC driver class to instantiate.
     */
    private String mstrDriver;

    /** 
     * Class property that stores the name of the connection server.
     */
    private String mstrServer;

    /** Class property that stores the name of the database.  */
    private String mstrDatabase;

    /** Class property that stores the database port number.  */
    private String mstrPort;

    /** 
     * Class property that stores the userid of the database connection.
     */
    private String mstrUser;

    /** 
     * Class property that stores the password of the database connection.
     */
    private String mstrPass;

    /** 
     * Class property that stores the type of connection defined in
     * the &lt;connection&gt; element...database, filesystem, etc...
     */
    private String mstrType;

    /** 
     * Class property that stores the client class within the 
     * &lt;driver&gt; element.
     */
    private String mstrClient;

    /**
     * Default constructor.
     */
    public JBJFDatabaseConnection() {
        super();
    }

    /**
     * Basic constructor that builds a simple connection from an XML
     * Batch Definition element.
     */
    public JBJFDatabaseConnection(Element ptheElement) {
        super();
        List ltheList = ptheElement.getAttributes();
        for (int i = 0; i < ltheList.size(); i++) {
            Attribute ltheAttribute = (Attribute) ltheList.get(i);
            if (ltheAttribute.getName().equals(this.ATTRBIUTE_XML_NAME)) {
                this.mstrName = ltheAttribute.getValue();
            } else if (ltheAttribute.getName().equals("id")) {
            } else {
                throw new IllegalArgumentException("Expecting <connection> attributes " + " but found <" + ltheAttribute.getName() + ">");
            }
        }
        ltheList = ptheElement.getChildren();
        for (int i = 0; i < ltheList.size(); i++) {
            Element ltheElement = (Element) ltheList.get(i);
            if (ltheElement.getName().equals(this.ELEMENT_XML_DRIVER)) {
                this.mstrDriver = ltheElement.getValue();
            } else if (ltheElement.getName().equals(this.ELEMENT_XML_SERVER)) {
                this.mstrServer = ltheElement.getValue();
            } else if (ltheElement.getName().equals(this.ELEMENT_XML_DATABASE)) {
                this.mstrDatabase = ltheElement.getValue();
            } else if (ltheElement.getName().equals(this.ELEMENT_XML_PORT)) {
                this.mstrPort = ltheElement.getValue();
            } else if (ltheElement.getName().equals(this.ELEMENT_XML_USER)) {
                this.mstrUser = ltheElement.getValue();
            } else if (ltheElement.getName().equals(this.ELEMENT_XML_PASS)) {
                this.mstrPass = ltheElement.getValue();
            } else if (ltheElement.getName().equals(this.ELEMENT_XML_TYPE)) {
                this.mstrType = ltheElement.getValue();
            } else if (ltheElement.getName().equals(this.ELEMENT_XML_CLIENT)) {
                this.mstrClient = ltheElement.getValue();
            } else {
                throw new IllegalArgumentException("Expecting <connection> elements " + " but found <" + ltheElement.getName() + ">");
            }
        }
    }

    /**
     * Standard getter() method that returns the database name.
     * @return  Returns the name of the database.
     */
    public String getDatabase() {
        return mstrDatabase;
    }

    /**
     * Standard setter() method that sets the database name.
     * @param pstrDatabase  Database name value to set.
     */
    public void setDatabase(String pstrDatabase) {
        mstrDatabase = pstrDatabase;
    }

    /**
     * Standard getter() method that returns the <code>DriverManager</code>
     * class name.
     * @return  Returns the name of the driver name class.
     */
    public String getDriver() {
        return mstrDriver;
    }

    /**
     * Standard setter() method that sets the database name.
     * @param pstrDriver    Driver name value to set.
     */
    public void setDriver(String pstrDriver) {
        mstrDriver = pstrDriver;
    }

    /**
     * Standard getter() method that returns the name of the connection.
     * @return  Returns the name of the connection definition.
     */
    public String getName() {
        return mstrName;
    }

    /**
     * Standard setter() method that sets the name of the connection
     * definition.
     * @param pstrName      Name of the connection definition to set.
     */
    public void setName(String pstrName) {
        mstrName = pstrName;
    }

    /**
     * Standard getter() method that returns the password of the connection.
     * @return  Returns the password of the connection definition.
     */
    public String getPass() {
        return mstrPass;
    }

    /**
     * Standard setter() method that sets the password of the connection
     * definition.
     * @param pstrPass      Password of the connection definition to set.
     *                      The password should be passed as a clear-text
     *                      string...i.e. not encrypted.
     */
    public void setPass(String pstrPass) {
        mstrPass = pstrPass;
    }

    /**
     * Standard getter() method that returns the server of the connection.
     * @return  Returns the server of the connection definition.
     */
    public String getServer() {
        return mstrServer;
    }

    /**
     * Standard setter() method that sets the server of the connection
     * definition.
     * @param pstrServer    Server of the connection definition to set.
     */
    public void setServer(String pstrServer) {
        mstrServer = pstrServer;
    }

    /**
     * Standard getter() method that returns the user of the connection.
     * @return  Returns the user of the connection definition.
     */
    public String getUser() {
        return mstrUser;
    }

    /**
     * Standard setter() method that sets the userid of the connection
     * definition.
     * @param pstrUser      Userid of the connection definition to set.
     *                      The password should be passed as a clear-text
     *                      string...i.e. not encrypted.
     */
    public void setUser(String pstrUser) {
        mstrUser = pstrUser;
    }

    /**
     * Standard getter() method that returns the port number of the connection.
     * @return  Returns the port number of the connection definition.
     */
    public String getPort() {
        return mstrPort;
    }

    /**
     * Standard setter() method that sets the port number of the connection
     * definition.
     * @param pstrPort      Port number of the connection definition to set.
     */
    public void setPort(String pstrPort) {
        mstrPort = pstrPort;
    }

    /**
     * Standard getter() method that returns the type of the connection.
     * @return  Returns the type of the connection definition.
     */
    public String getType() {
        return mstrType;
    }

    /**
     * Standard setter() method that sets the type of the connection
     * definition.
     * @param pstrType      Type of the connection definition to set.
     */
    public void setType(String pstrType) {
        mstrType = pstrType;
    }

    /**
     * Standard getter() method that returns the client for the connection.
     * @return  Returns the client for the connection definition.
     */
    public String getClient() {
        return mstrClient;
    }

    /**
     * Standard setter() method that sets the client of the connection
     * definition.
     * @param pstrClient    Client of the connection definition to set.
     */
    public void setClient(String pstrClient) {
        mstrClient = pstrClient;
    }

    /**
     * Overrides the standard toString() method and outputs all the
     * connection definition properties as a single <code>String</code>.
     */
    public String toString() {
        String lstrResults = "";
        lstrResults = this.mstrName;
        lstrResults = lstrResults + ";" + this.mstrDatabase;
        lstrResults = lstrResults + ";" + this.mstrPort;
        lstrResults = lstrResults + ";" + this.mstrDriver;
        lstrResults = lstrResults + ";" + this.mstrPass;
        lstrResults = lstrResults + ";" + this.mstrServer;
        lstrResults = lstrResults + ";" + this.mstrUser;
        lstrResults = lstrResults + ";" + this.mstrType;
        lstrResults = lstrResults + ";" + this.mstrClient;
        return lstrResults;
    }
}
