package lights.extensions;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import org.apache.log4j.Logger;
import lights.interfaces.ITuple;
import lights.interfaces.ITupleSpace;
import lights.interfaces.TupleSpaceError;
import lights.interfaces.TupleSpaceException;

public class FastTupleSpace implements ITupleSpace {

    public enum Mode {

        READ, WRITE, REMOVE
    }

    ;

    private static final int PERMIT_NUMBER = 10;

    private SynchronizedLinkedList tuples;

    private String name;

    public static Logger LOG;

    public static final String LOGGER = "up2p.tuplespace.fastTS";

    /** default constructor
	 * 
	 */
    public FastTupleSpace() {
        this("AnonymousFastTupleSpace");
    }

    /**
	 * constructor with a name for the tuple space
	 * @param name
	 */
    public FastTupleSpace(String name) {
        LOG = Logger.getLogger(LOGGER);
        this.name = name;
        tuples = new SynchronizedLinkedList();
    }

    /** 
	 * warning: not thread safe! only present for test purposes
	 */
    public String toString() {
        String toreturn = "";
        TupleNode node = tuples.getRoot();
        while (node != null) {
            toreturn += node.tuple.toString() + "\n";
            node = node.next;
        }
        return toreturn;
    }

    /**
	 *  a thread-safe linked list to optimize concurrent access for read, write, insert.
	 *  Read / remove should be done with the iterator. The write mode is not used at the moment for a tuple-space.
	 * @author Alan
	 *
	 */
    private class SynchronizedLinkedList {

        private TupleNode root;

        private Semaphore listSemaphore;

        public SynchronizedLinkedList() {
            listSemaphore = new Semaphore(PERMIT_NUMBER, true);
        }

        /**
		 * acquire permits on the semaphore controlling the head of this list
		 * @param permits number of permits
		 * @throws InterruptedException
		 */
        public void lockHead(int permits) throws InterruptedException {
            listSemaphore.acquire(permits);
        }

        /**
		 *  release the number of permits of the semaphore controlling the head of this list
		 * @param permits
		 */
        public void releaseHead(int permits) {
            listSemaphore.release(permits);
        }

        /**
		 * check if the list isn't empty.
		 * Warning: this is only valid at a snapshot in time when the query is made, 
		 * and operations may happen before the caller can execute his next line
		 * @return true is the list is empty, otherwise false
		 */
        public boolean isEmpty() {
            boolean ans = (root == null);
            return ans;
        }

        /**
		 *  remove and return the first tuple in this list.
		 *  @return the tuple in the first node of the linked list
		 */
        public ITuple removeHeadTuple() {
            if (root == null) return null;
            ITuple toReturn = null;
            try {
                lockHead(PERMIT_NUMBER);
                root.semaphore.acquire();
                toReturn = root.tuple;
                root.semaphore.release();
                root = root.next;
                releaseHead(PERMIT_NUMBER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return toReturn;
        }

        /**
		 *  read the first tuple in the List
		 * @return the tuple in the head node of the linked list
		 */
        public ITuple readHeadTuple() {
            if (root == null) return null;
            ITuple toReturn = null;
            try {
                lockHead(1);
                root.semaphore.acquire();
                toReturn = root.tuple;
                releaseHead(1);
                root.semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return toReturn;
        }

        /** 
		 * get the root node of the linked list
		 * @return the root node
		 */
        public TupleNode getRoot() {
            return root;
        }

        /**
		 *  creates an iterator for this linked list, capable of the *mode* operations
		 * @param mode READ, WRITE, or REMOVE : rights that the iterator has on the list [determines how the list is locked for concurrent access]
		 * @return
		 */
        public SyncIterator iterator(Mode mode) {
            return new SyncIterator(this, mode);
        }

        public void setRoot(TupleNode node) {
            root = node;
        }
    }

    private class SyncIterator {

        private SynchronizedLinkedList thelist;

        private TupleNode current;

        private TupleNode previous;

        private Mode currentMode;

        private boolean removalFlag;

        /** create an iterator based on the root node
		 *  wait to
		 * @param tup
		 * @param mode
		 */
        public SyncIterator(SynchronizedLinkedList list, Mode mode) {
            thelist = list;
            removalFlag = false;
            currentMode = mode;
        }

        /**
		 * determine whether the iterator is at the end of the list or not
		 * @return
		 */
        public boolean hasNext() {
            if (current == null) {
                if (thelist.isEmpty() || removalFlag) return false;
                return true;
            }
            if (current.next == null) return false;
            return true;
        }

        /** Initialize the iterator by locking the head of the list*/
        public void start() {
            int permits = PERMIT_NUMBER;
            if (currentMode == Mode.READ) permits = 1;
            try {
                thelist.lockHead(permits);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
		 * standard iterator next() function, except that depending on the current mode it will lock the 
		 * nodes with different constraints
		 * @return
		 */
        public ITuple next() {
            switch(currentMode) {
                case READ:
                    return getNext(1);
                case WRITE:
                    return getNext(PERMIT_NUMBER);
                case REMOVE:
                    return getNextWithRemoveLock();
                default:
                    return null;
            }
        }

        /**
		 * move to the next node in the linked list by acquiring the specified number 
		 * of permits from the tuples's semaphore. This is used in READ and WRITE mode.
		 * @param permits
		 * @return
		 */
        private ITuple getNext(int permits) {
            try {
                if (thelist.getRoot() == null) return null;
                if (current == null) {
                    thelist.getRoot().semaphore.acquire(permits);
                    current = thelist.getRoot();
                    thelist.releaseHead(permits);
                    return current.tuple;
                } else {
                    TupleNode temp = current;
                    current = current.next;
                    if (current != null) {
                        current.semaphore.acquire(permits);
                        temp.semaphore.release(permits);
                    } else {
                        temp.semaphore.release(permits);
                        return null;
                    }
                    return current.tuple;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
		 *  insert tuples into the list after the current node. The method is thread safe (I hope!!).
		 * @param tuplelist
		 * @return success
		 */
        public boolean insertTuplesHere(ITuple[] tuplelist) {
            if (tuplelist.length == 0) return true;
            if (currentMode != Mode.WRITE) return false;
            TupleNode templast = null, builder = null;
            for (ITuple tup : tuplelist) {
                builder = new TupleNode(tup, builder);
                if (builder.next == null) templast = builder;
            }
            if (current == null) {
                templast.next = thelist.getRoot();
                thelist.setRoot(builder);
            } else {
                templast.next = current.next;
                current.next = builder;
            }
            return true;
        }

        /**
		 * move one node forward in the iterator, locking the current and previous for possible removal of the 
		 * current node.
		 * States are :
		 * at initialization, before reading the first node: previous == null, current == null
		 * when reading the first node : previous == null, list is locked, current = root node of list, current locked
		 * when reading any other node : previous and current both locked.
		 * After reading the last node, all locks are released. 
		 * @return the tuple associated with the current node
		 */
        private ITuple getNextWithRemoveLock() {
            if (removalFlag) {
                removalFlag = false;
                return current.tuple;
            }
            try {
                if (thelist.getRoot() == null) return null;
                if (current == null) {
                    thelist.getRoot().semaphore.acquire(PERMIT_NUMBER);
                    current = thelist.getRoot();
                    return current.tuple;
                } else if (current == thelist.getRoot()) {
                    thelist.releaseHead(PERMIT_NUMBER);
                } else previous.semaphore.release(PERMIT_NUMBER);
                previous = current;
                TupleNode temp = current.next;
                if (temp == null) {
                    return null;
                }
                temp.semaphore.acquire(PERMIT_NUMBER);
                current = temp;
                return current.tuple;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
		 * remove the current tuple / node from the list 
		 * @return
		 */
        public boolean removeCurrent() {
            if (current == null || currentMode != Mode.REMOVE) return false;
            try {
                if (previous == null) {
                    thelist.setRoot(current.next);
                } else {
                    previous.next = current.next;
                }
                if (current.next != null) {
                    current.next.semaphore.acquire(PERMIT_NUMBER);
                }
                TupleNode temp = current;
                current = current.next;
                temp.semaphore.release(PERMIT_NUMBER);
                removalFlag = true;
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }

        /**Stop iterating and release all locks
		 * 
		 */
        public void quit() {
            int permits = PERMIT_NUMBER;
            switch(currentMode) {
                case READ:
                    permits = 1;
                case WRITE:
                    if (current != null) {
                        current.semaphore.release(permits);
                    } else thelist.releaseHead(permits);
                    break;
                case REMOVE:
                    if (current != null) current.semaphore.release(PERMIT_NUMBER);
                    if (previous != null) {
                        previous.semaphore.release(PERMIT_NUMBER);
                    } else thelist.releaseHead(PERMIT_NUMBER);
            }
        }
    }

    /**
	 *  A tuple node is a node in a linked list, with an ITuple for value, 
	 *  and it comes with a pointer to the next node of the list plus a semaphore for concurrent access control. 
	 * @author alan
	 *
	 */
    private class TupleNode {

        public ITuple tuple;

        public TupleNode next;

        public Semaphore semaphore;

        public TupleNode(ITuple tup, TupleNode nex) {
            tuple = tup;
            next = nex;
            semaphore = new Semaphore(10, true);
        }
    }

    protected void insertTuple(ITuple[] tuplist) {
        ITuple[] clones = new ITuple[tuplist.length];
        for (int i = 0; i < tuplist.length; i++) {
            if (tuplist[i].length() == 0) throw new IllegalArgumentException("Tuples with no fields can be used only as templates.");
            clones[i] = (ITuple) tuplist[i].clone();
        }
        SyncIterator iter = tuples.iterator(Mode.WRITE);
        iter.start();
        for (; iter.hasNext(); ) {
            iter.next();
        }
        iter.insertTuplesHere(clones);
        iter.quit();
    }

    protected void insertTuple(ITuple singletuple) {
        ITuple[] list = new ITuple[] { singletuple };
        insertTuple(list);
    }

    protected ITuple lookupTuple(ITuple template, boolean isRead) {
        ITuple toReturn = null;
        if (template.length() == 0) {
            if (!isRead) {
                toReturn = tuples.removeHeadTuple();
            } else {
                toReturn = tuples.readHeadTuple();
                if (toReturn != null) toReturn = (ITuple) toReturn.clone();
            }
        } else {
            ITuple tuple;
            Mode mymode = Mode.REMOVE;
            if (isRead) {
                mymode = Mode.READ;
            }
            SyncIterator iter = tuples.iterator(mymode);
            for (iter.start(); iter.hasNext(); ) {
                tuple = iter.next();
                if (template.matches(tuple)) {
                    if (isRead) toReturn = (ITuple) tuple.clone(); else {
                        toReturn = tuple;
                        iter.removeCurrent();
                    }
                }
            }
            iter.quit();
        }
        return toReturn;
    }

    /**
	 *  lookup tuples matching the template
	 * @param template
	 * @param isRead
	 * @return
	 */
    protected ITuple[] lookupTuples(ITuple template, boolean isRead) {
        List<ITuple> allResult = new LinkedList<ITuple>();
        ITuple[] toReturn = null;
        if (template.length() == 0) {
            if (!isRead) {
                ITuple t;
                SyncIterator iter = tuples.iterator(Mode.REMOVE);
                for (iter.start(); iter.hasNext(); ) {
                    t = iter.next();
                    allResult.add(t);
                    iter.removeCurrent();
                }
                iter.quit();
            } else {
                ITuple t;
                SyncIterator iter = tuples.iterator(Mode.READ);
                for (iter.start(); iter.hasNext(); ) {
                    t = iter.next();
                    allResult.add((ITuple) t.clone());
                }
                iter.quit();
            }
        } else {
            ITuple tuple;
            Mode mymode = Mode.REMOVE;
            if (isRead) {
                mymode = Mode.READ;
            }
            SyncIterator iter = tuples.iterator(mymode);
            for (iter.start(); iter.hasNext(); ) {
                tuple = iter.next();
                if (template.matches(tuple)) {
                    if (isRead) allResult.add((ITuple) tuple.clone()); else {
                        allResult.add(tuple);
                        iter.removeCurrent();
                    }
                }
            }
            iter.quit();
        }
        if (allResult.isEmpty()) toReturn = null; else {
            toReturn = allResult.toArray(new ITuple[allResult.size()]);
        }
        return toReturn;
    }

    public int count(ITuple template) throws TupleSpaceException {
        return lookupTuples(template, true).length;
    }

    public String getName() {
        return name;
    }

    public ITuple in(ITuple template) throws TupleSpaceException {
        return blockingInput(template, false);
    }

    public ITuple[] ing(ITuple template) throws TupleSpaceException {
        ITuple[] result = null;
        result = lookupTuples(template, false);
        return result;
    }

    /**
	 * in this implementation the returned tuple is the first one. In future improvements this may be randomized
	 */
    public ITuple inp(ITuple template) throws TupleSpaceException {
        return lookupTuple(template, false);
    }

    public void out(ITuple tuple) throws TupleSpaceException {
        insertTuple(tuple);
        synchronized (this) {
            notifyAll();
        }
    }

    public void outg(ITuple[] tuples) throws TupleSpaceException {
        insertTuple(tuples);
        synchronized (this) {
            notifyAll();
        }
    }

    protected ITuple blockingInput(ITuple template, boolean isRead) {
        ITuple result = null;
        do {
            result = lookupTuple(template, isRead);
            synchronized (this) {
                if (result == null) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new TupleSpaceError("Internal Error. Halting...");
                    }
                }
            }
        } while (result == null);
        return result;
    }

    public ITuple rd(ITuple template) throws TupleSpaceException {
        return blockingInput(template, true);
    }

    public ITuple[] rdg(ITuple template) throws TupleSpaceException {
        return lookupTuples(template, true);
    }

    public ITuple rdp(ITuple template) throws TupleSpaceException {
        return lookupTuple(template, true);
    }
}
