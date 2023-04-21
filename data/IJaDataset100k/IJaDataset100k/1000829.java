package de.ios.framework.remote.sv.co;

import java.rmi.*;
import java.util.*;
import de.ios.framework.basic.*;
import de.ios.framework.gui.ImageSourceCtrl;
import de.ios.framework.remote.auth.sv.co.MemberDC;

/**
 *
 */
public interface RemoteSession extends java.rmi.Remote {

    /**
   * Returns the Server.
   * @return the Remote-Server.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   * @exception de.ios.framework.basic.KontorException if the Server was not initialized yet (internal Error).
   */
    public RemoteServer getRemoteServer() throws java.rmi.RemoteException, KontorException;

    /**
   * Get the Member, this session belongs to (null for the internal Server-Session).
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public MemberDC getMember() throws java.rmi.RemoteException;

    /**
   * Get the language for this session.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public String getLanguage() throws java.rmi.RemoteException;

    /**
   * Create a DataBaseConnectFactory (protects the connection-data in private fields).
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public de.ios.framework.db.DataBaseConnectFactory createDataBaseConnectFactory() throws java.rmi.RemoteException;

    /**
   * Get the date and time the Session was created.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public EUCalendar getStartUp() throws java.rmi.RemoteException;

    /**
   * Force session-finalization.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public void finalizeSession() throws java.rmi.RemoteException;

    /**
   * Check, if a member has a certain Start-Right.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public boolean hasMemberStartRight(String right) throws java.rmi.RemoteException;

    /**
   * Get the matching Controller by it's name.
   * @param name the Name specifying the Controller.
   * @return the requested Controller.
   * @exception java.rmi.ServerException if the request failed due to a Server-Error.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public BasicController getControllerByName(String name) throws de.ios.framework.basic.ServerException, java.rmi.RemoteException;

    /**
   * Get all QueryIDs for a specified Controller.
   * @param name the Name of the Controller owning the Queries.
   * @return a VectorIterator (Strings) of all QueryIDs available for that Controller.
   * @exception java.rmi.ServerException if the request failed due to a Server-Error.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public VectorIterator getControllerQueryIDs(String name) throws de.ios.framework.basic.ServerException, java.rmi.RemoteException;

    /**
   * Execute a Controller's Query by a QueryID.
   * This method MUST NOT be available to the Client directly!
   * Within the QueryClause [Q1], [Q2], [Q3], ... are replaced by the Query-Values.
   * For each Result-Row a Result-Template is returned with [R1], [R2], [R3], ...
   * replaced by the Row's values ([R] by the Row#), each Line seperated by the Result-Row-Seperator,
   * where [LF], [CR], [TAB], [FF] are replaced by the corresponding characters.
   * ([LF], ... [Q1], ... are also replaced at the Result-Template.).
   * @param name the Name of the Controller owning the Query.
   * @param queryid the ID specifying the Query (which must belong to this Controller).
   * @param qvalues Vector of Values specifying the Where-Condition (the DataSet to be returned).
   * @return the String representing the loaded Rows received by expanding the Template specified
   * for that QueryID on the loaded Rows.
   * @exception java.rmi.ServerException if the request failed due to a Server-Error.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public String getObjectsByQueryID(String name, String queryId, Vector qvalues) throws de.ios.framework.basic.ServerException, java.rmi.RemoteException;

    /**
   * Execute a Controller's Query by a QueryID.  
   * This method MUST NOT be available to the Client directly!
   * Within the QueryClause [Q1], [Q2], [Q3], ... are replaced by the Query-Values.
   * For each Result-Row a Result-Template is returned with [R1], [R2], [R3], ...
   * replaced by the Row's values ([R] by the Row#), each Line seperated by the Result-Row-Seperator,
   * where [LF], [CR], [TAB], [FF] are replaced by the corresponding characters.
   * ([LF], ... [Q1], ... are also replaced at the Result-Template.).
   * @param name the Name of the Controller owning the Query.
   * @param queryid the ID specifying the Query (which must belong to this Controller).
   * @param qvalues Vector of Values specifying the Where-Condition (the DataSet to be returned).
   * @return the Vector of Strings each representing a loaded Rows received by expanding the Template specified
   * for that QueryID on the loaded Rows.
   * @exception java.rmi.ServerException if the request failed due to a Server-Error.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public Vector getObjectsVectorByQueryID(String name, String queryId, Vector qvalues) throws de.ios.framework.basic.ServerException, java.rmi.RemoteException;

    /**
   * Print method.
   * The creation and expansion of the Template hashtables is done in the Server.
   *
   * @param tableItems The table content as Vector of Vectors of Strings
   * @param tableOrient The orientation of the Table columns.
   * @param tableTitle The Table titles
   * @param templateName The name of the template
   * @param printer The printer to be used (if null, default printer). User properties are used.
   *
   * @return Exit value of print script
   *
   * @exception java.rmi.ServerException if the request failed due to a Server-Error.
   */
    public int printQueryResult(Vector tableItems, String tableOrient, String tableTitle[], String templateName, String printer) throws de.ios.framework.basic.ServerException, java.rmi.RemoteException;

    /**
   * Get all Controller-Names available for ControllerByName.
   * @param forRemReq if true, only Controllers with defined Remote-Requests are returned.
   * @return the List of Controllers (Moduls).
   * @exception java.rmi.ServerException if the request failed due to a Server-Error.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public VectorIterator getAllControllerNames(boolean onlyRemReq) throws de.ios.framework.basic.ServerException, RemoteException;

    /**
   * Get the names of the templates for printing (for use on the Client, e.g. in Choices).
   *
   * @exception java.rmi.ServerException if the request failed due to a Server-Error.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public Hashtable getPrintTemplates() throws de.ios.framework.basic.ServerException, RemoteException;

    /**
   * Clear the DB-Cache.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public void clearCache() throws java.rmi.RemoteException;

    /**
   * Test-method. Does nothing.
   */
    public boolean isAlive() throws java.rmi.RemoteException;

    /**
   * Returns a new Count object that can be used to update a ProgressBar on the client.
   * @param c Initial value (may be null)
   */
    public Counter getNewCounter(Long c) throws java.rmi.RemoteException;

    /**
   * Get the ImageSourceController to load images.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public ImageSourceCtrl getImageSourceController() throws java.rmi.RemoteException;

    /**
   * Get the RLanguageController.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public RLanguageController getLanguageController() throws java.rmi.RemoteException;

    /**
   * Get the RDescriptionController.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public RDescriptionController getDescriptionController() throws java.rmi.RemoteException;

    /**
   * Get the RDescriptionClassController.
   * @exception java.rmi.RemoteException if the connection to the Server
   * failed.
   */
    public RDescriptionClassController getRDescriptionClassController() throws java.rmi.RemoteException;
}
