package gov.lanl.Database;

import java.io.*;
import java.util.*;
import java.util.Properties.*;
import gov.lanl.Utility.ConfigProperties;
import com.softwaretree.jdx.JDXS;
import com.softwaretree.jx.JXSession;

/**
 *  An implmentation of DatabaseMgr interface based on the Java Data 
 *   Exchange package from Software Tree, Inc. (http://www.softwaretree.com) 
 */
public class JDXDatabaseMgr implements DatabaseMgr {

    static DatabaseMgr dbMgr;

    String serverName;

    String dataBase;

    String configName;

    com.softwaretree.jx.JXSession jxSession;

    com.softwaretree.jdx.JDXS jdx1;

    private static PersistentObjectFactory factory;

    boolean open = false;

    /**
     * Constructor
	*/
    public JDXDatabaseMgr() {
    }

    /**
     * @param databaseURL 
     */
    public void init(String databaseURL) {
        System.out.println(databaseURL);
        try {
            jxSession = (com.softwaretree.jx.JXSession) new com.softwaretree.jx.JXSessionImpl();
            Vector v = null;
            v = jxSession.open(databaseURL);
            jdx1 = (com.softwaretree.jdx.JDXS) v.elementAt(0);
            String databaseVersion = com.softwaretree.jx.JXUtilities.dbVersion(jdx1);
            System.out.println("Connected with  " + databaseVersion);
        } catch (java.rmi.RemoteException re) {
            System.out.println(re.toString());
            System.exit(1);
        } catch (Exception jxe) {
            System.out.println(jxe.toString());
            System.exit(2);
        }
        JDXGlobalSequence.initSequences(jdx1);
    }

    /**
     * @param props 
     * @return 
     */
    public static DatabaseMgr open(ConfigProperties props) {
        String databaseURL = props.getProperty("databaseURL");
        dbMgr = new JDXDatabaseMgr();
        dbMgr.init(databaseURL);
        return dbMgr;
    }

    /**
     * @return 
     */
    public static DatabaseMgr current() {
        return dbMgr;
    }

    /**
     * set Transaction context
     * @exception gov.lanl.Database.DBException 
     */
    public void txn_begin() throws DBException {
        try {
            if (open) return;
            jxSession.tx_begin();
            open = true;
        } catch (com.softwaretree.jx.JXTransException jx) {
            throw new DBException();
        } catch (java.rmi.RemoteException jr) {
            throw new DBException();
        }
    }

    /**
     * complete transaction
     * @exception gov.lanl.Database.DBException 
     */
    public void txn_commit() throws DBException {
        try {
            if (open) jxSession.tx_commit();
            open = false;
        } catch (com.softwaretree.jx.JXTransException jx) {
            throw new DBException();
        } catch (java.rmi.RemoteException jr) {
            throw new DBException();
        }
    }

    /**
     * get single element (DEEP) with simple query
     * returns the first element (if there is one)
     * @param obj is the input object being requested
     * @param elementName is the element desired
     * @param value is the value of the element desired
     * @exception gov.lanl.Database.DBException 
     * @return 
     */
    public PersistentObject retrieveElement(Object obj, String elementName, String value) throws DBException {
        try {
            System.out.println(obj.getClass().getName() + "  -  " + elementName + " = " + "'" + value + "'");
            Vector queryResults = jdx1.query(obj.getClass().getName(), elementName + " = " + "'" + value + "'", 1, JDXS.FLAG_DEEP, null);
            if (queryResults.size() != 0) return (PersistentObject) queryResults.elementAt(0);
        } catch (com.softwaretree.jdx.JDXException jx) {
            throw new DBException();
        } catch (java.rmi.RemoteException jr) {
            throw new DBException();
        }
        return null;
    }

    /**
     * @param obj 
     * @param elementName 
     * @param value 
     * @param deep 
     * @exception gov.lanl.Database.DBException 
     * @return 
     */
    public PersistentObject retrieveElement(Object obj, String elementName, String value, int deep) throws DBException {
        long flag = 0;
        if (deep == DatabaseMgr.DEEP) flag = JDXS.FLAG_DEEP;
        if (deep == DatabaseMgr.SHALLOW) flag = JDXS.FLAG_SHALLOW;
        try {
            System.out.println("Query: " + elementName + " = " + value);
            Vector queryResults = jdx1.query(obj.getClass().getName(), elementName + " = " + "'" + value + "'", 1, flag, null);
            if (queryResults.size() != 0) return (PersistentObject) queryResults.elementAt(0);
        } catch (com.softwaretree.jdx.JDXException jx) {
            throw new DBException();
        } catch (java.rmi.RemoteException jr) {
            throw new DBException();
        }
        return null;
    }

    /**
     * update object optionally shallowly
     * @param obj 
     * @param deep 
     * @exception gov.lanl.Database.DBException 
     */
    public void updateElement(PersistentObject obj, int deep) throws DBException {
        long flag = 0;
        if (deep == DatabaseMgr.DEEP) flag = JDXS.FLAG_DEEP;
        if (deep == DatabaseMgr.SHALLOW) flag = JDXS.FLAG_SHALLOW;
        try {
            jdx1.update(obj, flag, null);
        } catch (com.softwaretree.jdx.JDXException jx) {
            throw new DBException();
        } catch (java.rmi.RemoteException jr) {
            throw new DBException();
        }
    }

    /**
     * update object fully
     * @param obj 
     * @exception gov.lanl.Database.DBException 
     */
    public void updateElement(PersistentObject obj) throws DBException {
        if (obj == null) System.out.println("updateElement: Object is null!");
        try {
            jdx1.update(obj, JDXS.FLAG_DEEP, null);
        } catch (com.softwaretree.jdx.JDXException jx) {
            throw new DBException();
        } catch (java.rmi.RemoteException jr) {
            throw new DBException();
        }
    }

    /**
     * Delete object completely
     * @param obj 
     * @exception gov.lanl.Database.DBException 
     */
    public void deleteElement(PersistentObject obj) throws DBException {
        if (obj == null) System.out.println("deleteElement: Object is null!");
        try {
            jdx1.delete(obj, JDXS.FLAG_DEEP, null);
        } catch (com.softwaretree.jdx.JDXException jx) {
            throw new DBException();
        } catch (java.rmi.RemoteException jr) {
            throw new DBException();
        }
    }

    /**
     * input an object into database
     * @param obj is the input object
     * @param name 
     * @param is <<Invalid>> the name of the object
     * @exception gov.lanl.Database.DBException 
     */
    public void insertElement(PersistentObject obj, String name) throws DBException {
        try {
            jdx1.insert(obj, JDXS.FLAG_DEEP, null);
        } catch (com.softwaretree.jdx.JDXException jx) {
            throw new DBException();
        } catch (java.rmi.RemoteException jr) {
            throw new DBException();
        }
    }

    /**
     * obtain a Vector of elements from the Object obj already obtained (via shallow copy)
     * @param obj is the input Object
     * @param element is the name of the element to be returned
     * @param subelement 
     * @exception gov.lanl.Database.DBException 
     * @return 
     */
    public Vector accessElements(PersistentObject obj, String element, String subelement) throws DBException {
        try {
            Vector queryResults = null;
            if (subelement == null) {
                queryResults = jdx1.access(obj, element, -1, 0, null);
                return queryResults;
            } else {
                queryResults = jdx1.query(obj.getClass().getName(), element + " = " + subelement, -1, JDXS.FLAG_SHALLOW, null);
                return queryResults;
            }
        } catch (com.softwaretree.jdx.JDXException jx) {
            throw new DBException();
        } catch (java.rmi.RemoteException jr) {
            throw new DBException();
        }
    }

    /**
     * retrieve elements with multiple constraints
     * Objects to be retrieved
     * elements String array of elements to be filtered on
     * name String array of values of the elements desired
     * @param obj 
     * @param elements 
     * @param name 
     * @exception gov.lanl.Database.DBException 
     * @return 
     */
    public Vector retrieveElements(Object obj, String[] elements, String[] name) throws DBException {
        String query = "";
        for (int i = 0; i < elements.length; i++) {
            query = query + "( " + elements[i] + " = " + "'" + name[i] + "'" + " )";
            if (i != elements.length - 1) {
                query = query + " AND ";
            }
        }
        System.out.println(query);
        try {
            Vector queryResults = jdx1.query(obj.getClass().getName(), query, -1, 0, null);
            return queryResults;
        } catch (com.softwaretree.jdx.JDXException jx) {
            throw new DBException();
        } catch (java.rmi.RemoteException jr) {
            throw new DBException();
        }
    }

    /**
     * Retrieve elements of type Object with SearchFilter query and depth flag
     * @param obj 
     * @param query 
     * @param deep 
     * @exception gov.lanl.Database.DBException 
     * @return 
     */
    public Vector retrieveElements(Object obj, SearchFilter query, int deep) throws DBException {
        long flag = 0;
        if (deep == DatabaseMgr.DEEP) flag = JDXS.FLAG_DEEP;
        if (deep == DatabaseMgr.SHALLOW) flag = JDXS.FLAG_SHALLOW;
        try {
            Vector queryResults = jdx1.query(obj.getClass().getName(), query.toString(), -1, flag, null);
            return queryResults;
        } catch (com.softwaretree.jdx.JDXException jdx) {
            throw new DBException();
        } catch (java.rmi.RemoteException jr) {
            throw new DBException();
        }
    }

    /**
     * retrieve elements with complext query
     * @param obj object to be retrieved
     * @param operator operator in query
     * @param element element in query
     * @param values array of elements in query
     * @param deep 
     * @exception gov.lanl.Database.DBException 
     * @return 
     */
    public Vector retrieveElements(Object obj, String operator, String element, String[] values, int deep) throws DBException {
        long flag = 0;
        if (deep == DatabaseMgr.DEEP) flag = JDXS.FLAG_DEEP;
        if (deep == DatabaseMgr.SHALLOW) flag = JDXS.FLAG_SHALLOW;
        String predicate = "";
        if (operator.equals("IN")) {
            predicate = createInStringsPredicate(element, values);
        } else if (operator.equals("OR")) predicate = createOrArrayPredicate(element, values);
        if (values.length == 0) predicate = "";
        System.out.println("In retrieveElements " + predicate);
        try {
            Vector queryResults = jdx1.query(obj.getClass().getName(), predicate, -1, flag, null);
            return queryResults;
        } catch (com.softwaretree.jdx.JDXException jx) {
            throw new DBException();
        } catch (java.rmi.RemoteException jr) {
            throw new DBException();
        }
    }

    /**
     * retrieve elements with complext query
     * @param obj object to be retrieved
     * @param operator operator in query
     * @param element element in query
     * @param values array of elements in query
     * @param deep 
     * @exception gov.lanl.Database.DBException 
     * @return 
     */
    public Vector retrieveElements(Object obj, String operator, String element, int[] values, int deep) throws DBException {
        long flag = 0;
        if (deep == DatabaseMgr.DEEP) flag = JDXS.FLAG_DEEP;
        if (deep == DatabaseMgr.SHALLOW) flag = JDXS.FLAG_SHALLOW;
        String predicate = "";
        if (operator.equals("IN")) {
            predicate = createInPredicate(element, values);
        } else if (operator.equals("OR")) predicate = createOrArrayPredicate(element, values);
        if (values.length == 0) predicate = "";
        System.out.println("In retrieveElements " + predicate);
        try {
            Vector queryResults = jdx1.query(obj.getClass().getName(), predicate, -1, flag, null);
            return queryResults;
        } catch (com.softwaretree.jdx.JDXException jx) {
            throw new DBException();
        } catch (java.rmi.RemoteException jr) {
            throw new DBException();
        }
    }

    /**
		* This method will create predicate of the form 
		* object.attribName = value1 OR object.attribName = value2 ...
		* @param attribName 
		* @param values 
		* @return 
		*/
    private String createOrArrayPredicate(String attribName, String[] values) {
        if (values.length == 0) {
            return "";
        }
        String predicate = "(";
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                predicate += " OR ";
            }
            predicate += attribName + " = " + getQuotedString(values[i]);
        }
        predicate += ")";
        return predicate;
    }

    /**
		* This method will create predicate of the form 
		* object.attribName = value1 OR object.attribName = value2 ...
		* @param attribName 
		* @param values 
		* @return 
		*/
    private String createOrArrayPredicate(String attribName, int[] values) {
        if (values.length == 0) {
            return "";
        }
        String predicate = "(";
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                predicate += " OR ";
            }
            predicate += attribName + " = " + values[i];
        }
        predicate += ")";
        return predicate;
    }

    private String createInPredicate(String attribName, int[] values) {
        if (values.length == 0) {
            return "";
        }
        String predicate = "jdxObject." + attribName + " IN (";
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                predicate += ",";
            }
            predicate += values[i];
        }
        predicate += ")";
        return predicate;
    }

    private String createInStringsPredicate(String attribName, String[] values) {
        if (values.length == 0) {
            return "";
        }
        String predicate = "jdxObject." + attribName + " IN (";
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                predicate += ",";
            }
            predicate += getQuotedString(values[i]);
        }
        predicate += ")";
        return predicate;
    }

    /**
     * @param str 
     * @return 
     */
    private String getQuotedString(String str) {
        return "'" + str + "'";
    }

    /**
	 * Cancel transaction
	*/
    public void txn_abort() {
        try {
            jxSession.tx_rollback();
            open = false;
        } catch (com.softwaretree.jx.JXTransException jx) {
        } catch (java.rmi.RemoteException jr) {
        }
    }

    /**
	* return next number in a persistent sequence
	*/
    public long getNextSeq() {
        return JDXGlobalSequence.ObjectIdSeq.getNextSeq();
    }

    /**
   *  Create Persistent Object Factory
   */
    public void setObjectFactory(String objectPackage) {
        factory = new DBObjectFactory(objectPackage);
    }

    /**
   * return persistent object factory
   */
    public PersistentObjectFactory getObjectFactory() {
        if (factory == null) factory = new DBObjectFactory();
        return factory;
    }
}
