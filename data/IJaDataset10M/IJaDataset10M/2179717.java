package edu.berkeley.cs.db.yfilter.filterinstance;

import java.util.Hashtable;
import java.util.Vector;
import edu.berkeley.cs.db.yfilter.filter.EXfilter;
import edu.berkeley.cs.db.yfilterplus.queryparser.Query;

/**
 * Implements a circular queue.
 *  
 */
public class Queue {

    /**
	 * For threads, the wait period for notification. 
	 */
    public static int WAIT = 1000;

    public static String NO_RESULT = "No results yet!";

    public static String OBJ_EXPIRED = "Object expired from queue!";

    public static String OBJ_NA = "Object not available yet!";

    /**
	 * Indicates that the queue contains documents.
	 */
    protected static int DOC_QUEUE = 0;

    /**
	 * Indicates that the queue contains queries.
	 */
    protected static int QUERY_QUEUE = 1;

    /**
	 * This queue is a document queue by default.
	 */
    protected int qType = 0;

    /**
	 * The length of the queue.
	 */
    static int LENGTH;

    /**
	 * The least index present in the queue.
	 */
    protected int min_index = 0;

    /**
	 * The max index present in the queue.
	 */
    protected int max_index = 0;

    /**
	 * A cumulative index for the elements passing through the queue. 
	 */
    protected int index = -1;

    /**
	 * Stores the actual elements. 
	 * e.g. queries (Query), documents (XMLTree)
	 * 
	 */
    protected Vector elements = null;

    /**
	 * To store additional data, per element in the Vector.
	 * e.g. A document can have a Hashtable of matched queries.
	 * 		A query can have matched elements.
	 */
    protected Vector data = null;

    /**
	 * The current obect being processed.
	 * e.g. the doc ID being processed by the filter.
	 * 
	 * @param capacity
	 */
    protected int idInProcess = 0;

    /**
	 * The filter object, used to handle query additions and deletions.
	 * 
	 */
    protected EXfilter filter = null;

    /**
	 * Handles the map between queue and filter ids for queries, since the filter asynchronously assignes IDs to queries. 
	 * 
	 */
    protected Hashtable queryIDMap = null;

    /**
	 * @param capacity
	 */
    public Queue(int capacity) {
        LENGTH = capacity;
        elements = new Vector();
        data = new Vector();
        data.setSize(LENGTH);
        this.qType = Queue.DOC_QUEUE;
    }

    public Queue(int capacity, EXfilter filter) {
        LENGTH = capacity;
        elements = new Vector();
        data = new Vector();
        data.setSize(LENGTH);
        this.qType = Queue.QUERY_QUEUE;
        this.filter = filter;
        this.queryIDMap = new Hashtable();
    }

    /**
	 * Default constructor. 
	 * constructs a document queue of length 100.
	 */
    public Queue() {
        LENGTH = 100;
        elements = new Vector();
        data = new Vector();
        data.setSize(LENGTH);
        this.qType = Queue.DOC_QUEUE;
    }

    void setLength(int length) {
        LENGTH = length;
        elements = new Vector();
        data = new Vector();
        data.setSize(LENGTH);
    }

    /**
	 * Adds an object to the queue. If the queue is full, pops out the oldest object.
	 * 
	 * @param o The object to be added.
	 * @return int The id of the object in the queue.
	 */
    public synchronized int set(Object o) {
        index += 1;
        if (this.elements.size() == LENGTH) {
            elements.remove(0);
            if (data.size() > 0) data.remove(0);
            min_index += 1;
        }
        elements.add(o);
        if (this.filter != null) {
            synchronized (filter) {
                int id = filter.addQuery((Query) o);
                if (FilterInstance.DEBUG) System.err.println("Added query " + id + " to filter..");
                queryIDMap.put(new Integer(id), new Integer(index));
                return id;
            }
        }
        return index;
    }

    /**
	 * Gets an object from the queue.
	 * 
	 * @param id The object id
	 * @return Object A status string if the object is not in the queue else, the object.
	 */
    public synchronized Object get(int id) {
        Object o = null;
        int index = id;
        if (filter != null) {
            index = ((Integer) queryIDMap.get(new Integer(id))).intValue();
        }
        if (index < min_index) o = new String("Object expired from queue!"); else if (index > index) o = new String("Object not yet in queue!"); else o = this.elements.get(index - min_index);
        return o;
    }

    /**
	 * Gets the data associated with the id. e.g. For documents, the data could be matching query elements.
	 *   
	 * @return Object The data object
	 */
    public synchronized Object getData(int id) {
        Object o = null;
        if (id < min_index) o = new String(Queue.OBJ_EXPIRED); else if (id > index) o = new String(Queue.OBJ_NA); else if (data.size() == 0) o = new String(Queue.NO_RESULT); else {
            o = this.data.get(id - min_index);
            if (o == null) o = new String(Queue.NO_RESULT);
        }
        if (o.equals(Queue.NO_RESULT)) try {
            synchronized (data) {
                data.wait(Queue.WAIT);
            }
        } catch (Exception ie) {
            o = "Queue.getData: " + ie.getMessage();
        }
        return o;
    }

    /**
	 * Adds a data object corresponding to an id. 
	 * e.g. For a document, this could be the matching query elements.
	 * 
	 * @param o The data object to be added.
	 * @param id The id of the corressponding object
	 * @return boolean the status of the action.
	 */
    public synchronized boolean setData(Object o, int id) {
        boolean status = false;
        if (elements.get(id - min_index) != null) {
            if ((id - min_index) == 0) data.add(o); else data.set(id - min_index, o);
            status = true;
        }
        synchronized (data) {
            data.notifyAll();
        }
        return status;
    }

    public Hashtable processNext() {
        Hashtable hasht = null;
        if (idInProcess < min_index) {
            idInProcess = min_index;
        }
        if (idInProcess > index) return null;
        if (elements.get(idInProcess - min_index) != null) {
            hasht = new Hashtable();
            hasht.put(new Integer(idInProcess), elements.get(idInProcess - min_index));
        }
        idInProcess += 1;
        return hasht;
    }
}
