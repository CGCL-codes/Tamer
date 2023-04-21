package org.dmd.util.exceptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

/**
 * The ResultSet class manages a set of Results that can be passed back from
 * an arbitrarily deep set of function calls. It maintains an indication of the
 * total number of results that came back and what the worst severity encountered
 * was.
 * <P>
 * The ResultSet can also be used in conjunction with the MessageManager class
 * that provide Locale independent messaging. The ResultSet has an associated
 * Locale that users of the class can use to generate the appropriate messages
 * dependent on the Locale.
 * @author Peter Strong
 * @version 1.0
 */
public class ResultSet {

    /**
     * A set of previously constructed Result objects that we reuse.
     */
    @SuppressWarnings("unchecked")
    ArrayList available;

    /**
     * The current set of populated Result objects.
     */
    @SuppressWarnings("unchecked")
    ArrayList current;

    /**
     * Indicates the most serious result in the set.
     */
    int worstV;

    /**
     * The number of errors in the result set.
     */
    int errorsV;

    /**
     * The number of warnings in the result set.
     */
    int warningsV;

    /**
     * The Locale for which results should be generated - this defaults
     * to Locale.US
     */
    Locale localeV;

    /**
    * Creates a new Results container.
    */
    @SuppressWarnings("unchecked")
    public ResultSet() {
        available = new ArrayList();
        current = new ArrayList();
        errorsV = 0;
        warningsV = 0;
        worstV = Result.NONE;
        localeV = Locale.US;
    }

    /**
    * Creates a new Results container with the specified Locale.
    */
    @SuppressWarnings("unchecked")
    public ResultSet(Locale l) {
        available = new ArrayList();
        current = new ArrayList();
        errorsV = 0;
        warningsV = 0;
        worstV = Result.NONE;
        localeV = l;
    }

    /**
     * Resets the result to have no results.
     */
    @SuppressWarnings("unchecked")
    public void reset() {
        Result curr;
        while (current.size() > 0) {
            curr = (Result) current.remove(0);
            curr.reset();
            available.add(curr);
        }
        errorsV = 0;
        warningsV = 0;
        worstV = Result.NONE;
    }

    /**
     * Returns an iterator over the current set of results.
     */
    @SuppressWarnings("unchecked")
    public Iterator iterator() {
        return (current.iterator());
    }

    /**
     * Returns the number of results in the set.
     */
    public Locale locale() {
        return (localeV);
    }

    /**
     * Returns the number of results in the set.
     */
    public int resultCount() {
        return (current.size());
    }

    /**
     * Returns the number of errors encountered.
     */
    public int errors() {
        return (errorsV);
    }

    /**
     * Returns the number of warnings encountered.
     */
    public int warnings() {
        return (warningsV);
    }

    /**
     * Returns a Result container that can be populated with further information
     * if required. This increases the number of results in the result set by 1.
     */
    @SuppressWarnings("unchecked")
    public Result addResult(int sev, String message) {
        Result curr;
        if (available.size() > 0) curr = (Result) available.remove(0); else curr = new Result();
        curr.reset(sev, message);
        current.add(curr);
        if (sev > worstV) worstV = sev;
        if ((sev == Result.ERROR) || (sev == Result.FATAL)) errorsV++;
        if (sev == Result.WARNING) warningsV++;
        return (curr);
    }

    /**
     * Makes a copy of the specified result and adds it to this result set.
     */
    @SuppressWarnings("unchecked")
    public Result addResult(Result r) {
        Result curr = new Result(r);
        current.add(curr);
        if (curr.severity() > worstV) worstV = curr.severity();
        if ((curr.severity() == Result.ERROR) || (curr.severity() == Result.FATAL)) errorsV++;
        if (curr.severity() == Result.WARNING) warningsV++;
        return (curr);
    }

    /**
     * Provides access to the last result that was added to the set.
     * @return The last Result or null if no results in the set.
     */
    public Result lastResult() {
        if (current.size() == 0) return (null); else return ((Result) current.get(current.size() - 1));
    }

    /**
     * Returns the worst severity that's been added to the set.
     */
    public int worst() {
        return (worstV);
    }

    /**
     * Returns the entire set of results as a string.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < current.size(); i++) sb.append(((Result) current.get(i)).toString());
        return (sb.toString());
    }

    /**
     * Adds copies of the results in the specified result set to this result set.
     */
    public void addResults(ResultSet rs) {
        for (int i = 0; i < rs.current.size(); i++) {
            Result r = (Result) rs.current.get(i);
            this.addResult(r);
        }
    }
}
