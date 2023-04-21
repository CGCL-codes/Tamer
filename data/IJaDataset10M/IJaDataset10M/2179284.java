package lights.space;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import lights.exceptions.TupleSpaceError;
import lights.exceptions.TupleSpaceException;
import lights.interfaces.ITuple;
import lights.interfaces.ITupleSpace;

/**
 * Embodies the concept of a Linda-like tuple space. The traditional operations
 * provided by Linda, namely the insertion of a tuple with <code>out</code>
 * and pattern matching with the blocking operations <code>in</code> and
 * <code>rd</code>, are supported. In addition, the following features are
 * provided:
 * <UL>
 * 
 * <LI>non-blocking <I>probe </I> operations <code>inp</code> and
 * <code>rdp</code>;
 * 
 * <LI>non-blocking <I>group </I> operations <code>outg</code>,<code>ing</code>,
 * <code>rdg</code>, that allow to write and retrieve multiple tuples at
 * once;
 * <LI>a <code>count</code> operation returning the number of tuples matching
 * a given templates;
 * 
 * <LI>a name associated to the tuple space.
 * </ul>
 * The semantics of the tuple space are such that a <I>copy </I> of the original
 * tuple is inserted. In other words, modifications to the tuple object
 * subsequent to insertion are not reflected into the tuple object stored into
 * the tuple space. This semantics is ensured by serializing and deserializing
 * the tuple object being inserted, in order to obtain a deep copy of the tuple
 * that is the one actually inserted in the tuple space.
 * 
 * <b>Note:</b>  this implementation is based on the <code>CopyOnWriteArrayList</code>. 
 * Synchronizing the blocking tuple space operations is achieved using the 
 * <code>synchronized</code> keyword. The non-blocking read operations are not 
 * synchronized since it allow to return null values and operate on local copies 
 * of the underlying array of the <code>CopyOnWriteArrayList</code>. <code>inp</code> 
 * must be synchronized. Otherwise, possibly interleaving <code>inp</code> operations 
 * can consume a tuple that conceptionally is already consumed by another component 
 * (<i>defective reduplication</i>). The implementation performs well for read operations, 
 * but writes add a performance penalty since all mutative operations on the 
 * <code>CopyOnWriteArrayList</code> internally make a fresh copy of the underlying array.
 * 
 *  * @author <a href="mailto:stefan.gudenkauf@offis.de">Stefan Gudenkauf</a>
 * @author <a href="mailto:picco@elet.polimi.it">Gian Pietro Picco </a>
 */
public class CopyOnWriteTupleSpace implements ITupleSpace, java.io.Serializable {

    /**
	 * Generated serial version unique identifier.
	 */
    private static final long serialVersionUID = -7873619398229191788L;

    /** The name of the tuple space. */
    private String name = null;

    /** The actual implementation of the tuple space data structure. */
    private List<ITuple> ts = null;

    /**
	 * Creates a tuple space with the default name as specified in the interface <code>ITupleSpace</code>.
	 */
    public CopyOnWriteTupleSpace() {
        this(DEFAULT_NAME);
    }

    /**
	 * Creates a tuple space with the name specified by the user.
	 * 
	 * @param name the tuple space name.
	 * @exception IllegalArgumentException if the name is <code>null</code>.
	 */
    public CopyOnWriteTupleSpace(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Tuple space must have a non-null name.");
        }
        this.name = name;
        ts = new CopyOnWriteArrayList<ITuple>();
    }

    /**
	 * Returns the name of the tuple space.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Inserts a tuple in the tuple space. The operation is synchronous, i.e.,
	 * the tuple is guaranteed to be available in the tuple space after this
	 * method successfully completes execution.
	 * 
	 * @param tuple The tuple to be inserted.
	 * @exception TupleSpaceException if an error occurs in the implementation.
	 * @exception IllegalArgumentException if the tuple has no fields.
	 */
    public void out(ITuple tuple) throws TupleSpaceException {
        synchronized (this) {
            insertTuple(tuple);
            notifyAll();
        }
    }

    /**
	 * Inserts multiple tuples in the tuple space. The operation is performed
	 * atomically, i.e., each tuple is not available until all the tuples have
	 * been inserted.
	 * 
	 * @param tuples An array containing the tuples to be inserted.
	 * @exception TupleSpaceException if an error occurs in the implementation.
	 */
    public void outg(ITuple[] tuples) throws TupleSpaceException {
        synchronized (this) {
            for (int i = 0; i < tuples.length; i++) {
                insertTuple(tuples[i]);
            }
            notifyAll();
        }
    }

    /**
	 * Withdraws from the tuple space a tuple matching the templates specified;
	 * if no tuple is found, the caller is suspended until such a tuple shows up
	 * in the tuple space. If multiple matching tuples are found, the first one 
	 * is returned.
	 * 
	 * @param template the templates used for matching.
	 * @return a tuple matching the templates.
	 * @exception TupleSpaceException if an error in the implementation.
	 */
    public ITuple in(ITuple template) throws TupleSpaceException {
        ITuple result = null;
        synchronized (this) {
            while (result == null) {
                result = inp(template);
                if (result == null) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new TupleSpaceError("Internal Error. Halting...");
                    }
                }
            }
        }
        return result;
    }

    /**
	 * Withdraws from the tuple space a tuple matching the templates specified;
	 * if no tuple is found, <code>null</code> is returned. If multiple matching tuples are found, the first one 
	 * is returned.
	 * 
	 * @param template the templates used for matching.
	 * @return a tuple matching the templates, or <code>null</code> if none is
	 *         found.
	 * @exception TupleSpaceException if an error in the implementation.
	 */
    public ITuple inp(ITuple template) throws TupleSpaceException {
        ITuple result = null;
        synchronized (this) {
            if (ts.size() != 0) {
                result = lookupTuple(template, false);
            }
        }
        return result;
    }

    /**
	 * Withdraws from the tuple space <i>all </I> the tuple matching the
	 * templates specified. If no tuple is found, <code>null</code> is
	 * returned.
	 * 
	 * @param template the templates used for matching.
	 * @return a tuple matching the templates, or <code>null</code> if none is
	 *         found.
	 * @exception TupleSpaceException if an error in the implementation.
	 */
    public ITuple[] ing(ITuple template) throws TupleSpaceException {
        ITuple[] result = null;
        synchronized (this) {
            if (ts.size() != 0) {
                result = lookupTuples(template, false);
            }
        }
        return result;
    }

    /**
	 * Reads from the tuple space a copy of a tuple matching the templates
	 * specified. If no tuple is found, the caller is suspended until such a
	 * tuple shows up in the tuple space. If multiple matching tuples are found, the first one 
	 * is returned.
	 * 
	 * @param template the templates used for matching.
	 * @return a copy of a tuple matching the templates.
	 * @exception TupleSpaceException if an error in the implementation.
	 */
    public ITuple rd(ITuple template) throws TupleSpaceException {
        ITuple result = null;
        synchronized (this) {
            while (result == null) {
                result = rdp(template);
                if (result == null) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new TupleSpaceError("Internal Error. Halting...");
                    }
                }
            }
        }
        return result;
    }

    /**
	 * Reads from the tuple space a copy of a tuple matching the templates
	 * specified. If no tuple is found, <code>null</code> is returned. If multiple matching tuples are found, the first one 
	 * is returned.
	 * 
	 * This operation is not synchronized since it allows to return null values and operates on 
	 * a local copy of the underlying array of the <code>CopyOnWriteArrayList</code>.
	 * 
	 * @param template the templates used for matching.
	 * @return a copy of a tuple matching the templates, or <code>null</code>
	 *         if none is found.
	 * @exception TupleSpaceException if an error in the implementation.
	 */
    public ITuple rdp(ITuple template) throws TupleSpaceException {
        ITuple result = null;
        if (ts.size() != 0) {
            result = lookupTuple(template, true);
        }
        return result;
    }

    /**
	 * Reads from the tuple space a copy of <I>all </I> the tuples matching the
	 * given template. If no tuple is found, <code>null</code> is
	 * returned. Conventionally, a tuple with no fields matches any other tuple.
	 * 
	 * This operation is not synchronized since it allows to return null values and operates on 
	 * a local copy of the underlying array of the <code>CopyOnWriteArrayList</code>.
	 * 
	 * @param template the templates used for matching.
	 * @return a copy of a tuple matching the template, or <code>null</code>
	 *         if none is found.
	 * @exception TupleSpaceException if an error in the implementation.
	 */
    public ITuple[] rdg(ITuple template) throws TupleSpaceException {
        ITuple[] result = null;
        if (ts.size() != 0) {
            result = lookupTuples(template, true);
        }
        return result;
    }

    /**
	 * Returns a count of the tuples found in the tuple space that match the
	 * given template.
	 * 
	 * This operation is not synchronized since it allows to return null values and operates on 
	 * a local copy of the underlying array of the <code>CopyOnWriteArrayList</code>.
	 * 
	 * @param template the templates used for matching.
	 * @return the number of tuples currently in the tuple space that match the
	 *         given template.
	 * @exception TupleSpaceException if an error in the implementation.
	 */
    public int count(ITuple template) throws TupleSpaceException {
        ITuple result = null;
        int count = 0;
        Iterator<ITuple> iter = ts.iterator();
        while (iter.hasNext()) {
            result = (ITuple) iter.next();
            if (template.matches(result)) {
                count++;
            }
        }
        return count;
    }

    /** Returns a string representation of the tuple space. */
    public String toString() {
        return ts.toString();
    }

    protected void insertTuple(ITuple tuple) {
        if (tuple.length() == 0) {
            throw new IllegalArgumentException("Tuples without fields can not be inserted in the tuple space.");
        }
        ts.add((ITuple) tuple.clone());
    }

    protected ITuple lookupTuple(ITuple template, boolean isRead) {
        ITuple resultTuple = null;
        ITuple tempTuple;
        boolean foundTuple = false;
        if (isRead == true) {
            if (template.length() == 0) {
                resultTuple = (ITuple) ts.get(0).clone();
            } else {
                Iterator<ITuple> iter = ts.iterator();
                while (!foundTuple && iter.hasNext()) {
                    tempTuple = (ITuple) iter.next();
                    foundTuple = template.matches(tempTuple);
                    if (foundTuple) {
                        resultTuple = (ITuple) tempTuple.clone();
                    }
                }
            }
        } else {
            if (template.length() == 0) {
                resultTuple = (ITuple) ts.remove(0).clone();
            } else {
                Iterator<ITuple> iter = ts.iterator();
                while (!foundTuple && iter.hasNext()) {
                    tempTuple = (ITuple) iter.next();
                    foundTuple = template.matches(tempTuple);
                    if (foundTuple) {
                        resultTuple = (ITuple) tempTuple.clone();
                        ts.remove(tempTuple);
                    }
                }
            }
        }
        return resultTuple;
    }

    protected ITuple[] lookupTuples(ITuple template, boolean isRead) {
        List<ITuple> resultTuples = new ArrayList<ITuple>();
        ITuple tempTuple;
        if (isRead == true) {
            if (template.length() == 0) {
                for (ITuple t : ts) {
                    resultTuples.add((ITuple) t.clone());
                }
            } else {
                Iterator<ITuple> iter = ts.iterator();
                while (iter.hasNext()) {
                    tempTuple = iter.next();
                    if (template.matches(tempTuple)) {
                        resultTuples.add((ITuple) tempTuple.clone());
                    }
                }
            }
        } else {
            if (template.length() == 0) {
                resultTuples.addAll(ts);
                ts.clear();
            } else {
                Iterator<ITuple> iter = ts.iterator();
                while (iter.hasNext()) {
                    tempTuple = iter.next();
                    if (template.matches(tempTuple)) {
                        resultTuples.add((ITuple) tempTuple.clone());
                        ts.remove(tempTuple);
                    }
                }
            }
        }
        ITuple[] t = new ITuple[0];
        return resultTuples.toArray(t);
    }
}
