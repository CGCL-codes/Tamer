package com.trackerdogs.search;

import java.util.*;
import java.net.*;
import java.io.*;
import java.lang.*;
import javax.naming.*;
import javax.naming.directory.*;
import com.trackerdogs.websources.dispatcher.*;
import com.trackerdogs.websources.statistics.*;
import com.trackerdogs.websources.results.*;
import com.trackerdogs.websources.*;

/**
 * All the results are stored here.
 * Also stores misc info (search engines used etc).
 *
 * @author Koen Witters
 * @version $Header: /cvsroot/trackerdogs/trackerdogs/src/com/trackerdogs/search/Results.java,v 1.5 2002/09/03 14:36:03 kwitters Exp $
 */
public class Results implements Serializable {

    /** After this timeout, do all to give results */
    private static final long TIMEOUT = 5000;

    /** Select the next best result out of # candidates  */
    private static final int EXTRA_RESULTS = 25;

    private Keywords query_;

    private Vector results_;

    private int resultsStaticUntil_;

    private boolean allPolled_;

    private long timeStamp_;

    private Dispatcher dispatcher_;

    /**********************************************************************
     * Initialize the results with these keywords.
     * It fetches the results!
     * 
     * @param keys the keywords
     */
    public Results(Keywords keys) {
        query_ = keys;
        allPolled_ = false;
        this.timeStamp_ = (new Date()).getTime();
        resultsStaticUntil_ = 0;
        results_ = new Vector();
    }

    /**
     * Adds a page to the results
     *
     * @param page the web page to add
     *
     * @exception Exception when WebPage or SEPage is null
     */
    public void addElement(Result page, WebSourcePage sePage) throws Exception {
        if (page == null) {
            throw new Exception("Results.addElement(): WebPage == null");
        } else if (sePage == null) {
            throw new Exception("Results.addElement(): WebSourcePage == null");
        }
        Result wp;
        boolean isNew = true;
        if (isNew) {
            sePage.addNewPage();
            int i = resultsStaticUntil_;
            while ((i < results_.size()) && (page.getScore(query_) <= ((Result) results_.elementAt(i)).getScore(query_))) {
                i++;
            }
            results_.insertElementAt(page, i);
        }
    }

    /** 
     * Returns the keywords from the results
     *
     * @return the keywords
     */
    public Keywords getKeywords() {
        return query_;
    }

    /**
     * Returns the number of webpages (total)
     *
     * @return real number of web pages
     */
    public int resultNo() {
        return results_.size();
    }

    /**
     * Returns true if the results are ready
     *
     * @return true if the results are ready
     */
    public boolean allPolled() {
        return this.allPolled_;
    }

    /**********************************************************************
     * Sets if all results are polled (no more results will be added)
     *
     * @param polled true if no more results will be added
     */
    public void setAllPolled(boolean polled) {
        this.allPolled_ = polled;
    }

    /**
     * Checks if two searches are the same
     */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        Results res = (Results) obj;
        return query_.equals(res.getKeywords());
    }

    /**********************************************************************
     * Returns all the pages in this resultset
     *
     * @return a Vector that contains <code>WebPage</code> objects.
     */
    public Result getResultAt(int i) {
        return (Result) results_.elementAt(i);
    }

    /**********************************************************************
     * Returns true if the result is ready. This means that there are
     * enouph results polled so the place of this result is relatively
     * certain. Best use {@link #waitOnResult(int, int) waitOnResult}
     * first.
     *
     * @return true if that result is ready
     */
    public boolean resultReadyAt(int i) {
        if (this.resultNo() >= (EXTRA_RESULTS + i) || this.allPolled_ || resultsStaticUntil_ > i) {
            return true;
        } else {
            return false;
        }
    }

    /**********************************************************************
     * Returns true if the result is already polled. This means that
     * there is a result in this position, but its ranking cannot be
     * certain.
     * Best use {@link #waitOnResult(int, int) waitOnResult}
     * first.
     *
     * @return true if that result is polled
     */
    public boolean resultPolledAt(int i) {
        if (results_.size() > i) {
            return true;
        } else {
            return false;
        }
    }

    /**********************************************************************
     * Waits until a result is ready. If it takes too long a timeout
     * will occur and the waiting will be over
     *
     * @param i the number of the result
     * @param timeout the timeout in ms to stop this funtion
     */
    public void waitOnResult(int i, int timeout) {
        Date begin = new Date();
        while (!resultReadyAt(i) && ((new Date()).getTime() < (begin.getTime() + timeout))) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                assert false : ex.getMessage();
            }
        }
    }

    /**********************************************************************
     * Sets that the results may not change until i
     *
     * @param i static until i
     */
    public void setResultsStaticUntil(int i) {
        this.resultsStaticUntil_ = i;
    }

    private TreeMap addWebSourcePageTo(WebSourcePage page, TreeMap pagesMap) {
        String seName = page.getWebSource().getSEName();
        Integer pageNr = new Integer(page.getPageNr());
        TreeSet set;
        if (pagesMap.containsKey(seName)) {
            set = (TreeSet) pagesMap.get(seName);
        } else {
            set = new TreeSet();
        }
        set.add(pageNr);
        pagesMap.put(seName, set);
        return pagesMap;
    }

    /************************************************************
     * Sets a new time stamp (at current time)
     */
    public void setTimeStamp() {
        this.timeStamp_ = (new Date()).getTime();
    }

    /************************************************************
     * Returns the age of this result (current time - last 
     * accessed)
     *
     * @return the age of these results in seconds
     */
    public long getAge() {
        return ((new Date()).getTime() - this.timeStamp_) / 1000;
    }

    /************************************************************
     * Returns the time these results were last accessed
     */
    public Date getTimeStamp() {
        return new Date(this.timeStamp_);
    }

    /**********************************************************************
     * Returns the dispatcher with the given statistics.
     *
     * @param stats the statistics
     */
    public Dispatcher getDispatcher(Statistics stats) {
        if (this.dispatcher_ == null) {
            this.dispatcher_ = new Dispatcher(stats, this);
        }
        return this.dispatcher_;
    }
}
