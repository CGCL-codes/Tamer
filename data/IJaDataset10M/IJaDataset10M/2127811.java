package org.oclc.da.ndiipp.ingester.lookup;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * BatchLookupQueue class.  Holds a linked list of objects describing batches
 * waiting to be processed.
 *
 * @author Leah Houser  November 21, 2002
 */
public class BatchLookupQueue {

    private static String me = "BatchLookupQueue ";

    protected static boolean debug = false;

    private static LinkedList<BatchLookupData> m_batchLinkedList = new LinkedList<BatchLookupData>();

    /** 
     * Initialize
     * @throws Exception 
     */
    public BatchLookupQueue() throws Exception {
        debug = true;
    }

    /**
     * returns the entire list of batches
     * @return list of batches
     */
    public LinkedList getAll() {
        return m_batchLinkedList;
    }

    /** 
     * Add an object to the end of the queue    
     * @param a_LookupData 
     */
    public void addLast(BatchLookupData a_LookupData) {
        m_batchLinkedList.addLast(a_LookupData);
    }

    /** 
     * Retrieve & remove the first object from the queue  
     * @return data
     */
    public BatchLookupData removeFirst() {
        try {
            return ((BatchLookupData) m_batchLinkedList.removeFirst());
        } catch (Exception e) {
            return (null);
        }
    }

    /** 
     *  Retrieve the first object from the queue  
     *  @return  the first object
     */
    public BatchLookupData getFirst() {
        return ((BatchLookupData) m_batchLinkedList.getFirst());
    }

    /** 
     *  remove an object from the queue  
     * @param i 
     */
    public void remove(int i) {
        m_batchLinkedList.remove(i);
    }

    /**
     * Dump of queue
     * @return  string
     */
    public String toString() {
        StringBuffer dump = new StringBuffer("batch queue:\n");
        ListIterator list = m_batchLinkedList.listIterator();
        while (list.hasNext()) {
            BatchLookupData data = (BatchLookupData) list.next();
            dump.append(data.toString());
        }
        return (dump.toString());
    }

    /** Test routines for this class
     * @param args 
     */
    public static void main(String args[]) {
        System.out.println(me + "test main\n");
        BatchLookupQueue queue = null;
        BatchLookupData data = null;
        try {
            queue = new BatchLookupQueue();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println("adding to the queue\n");
        data = new BatchLookupData();
        data.setBatchDirectoryName("batchdir1");
        data.setBatchNumber("one");
        queue.addLast(data);
        data = new BatchLookupData();
        data.setBatchDirectoryName("batchdir2");
        data.setBatchNumber("two");
        queue.addLast(data);
        data = new BatchLookupData();
        data.setBatchDirectoryName("batchdir3");
        data.setBatchNumber("three");
        queue.addLast(data);
        System.out.println("current queue: " + queue.toString() + "\nend queue dump");
        try {
            data = queue.removeFirst();
            System.out.println(data.toString());
            data = queue.removeFirst();
            System.out.println(data.toString());
            data = queue.removeFirst();
            System.out.println(data.toString());
            System.out.println("Expecting exception here: ");
            data = queue.removeFirst();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println("test completed");
    }
}
