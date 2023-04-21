package org.epo.jpxi.shared;

/**
 * Creation date: (06/12/01 10:12:19) 
 * Enclose JpxiSessionStat objet, in order to
 * pass thru RMI 
 */
public class JpxiSessionStatInfo implements java.io.Serializable {

    private int currentActiveSession = 0;

    private double avgNbConcSession = 0;

    private int totalNbSession = 0;

    private int totalAbortedSession = 0;

    private long avgLifespan = 0;

    /**
 * JpxiSessionStatInfo constructor comment.
 * @param theCurrentActiveSession int
 * @param theAvgNbConcSession double
 * @param theTotalNbSession int
 * @param theTotalAbortedSession int
 * @param theAvgLifespan long
 */
    public JpxiSessionStatInfo(int theCurrentActiveSession, double theAvgNbConcSession, int theTotalNbSession, int theTotalAbortedSession, long theAvgLifespan) {
        super();
        setCurrentActiveSession(theCurrentActiveSession);
        setAvgNbConcSession(theAvgNbConcSession);
        setTotalNbSession(theTotalNbSession);
        setTotalAbortedSession(theTotalAbortedSession);
        setAvgLifespan(theAvgLifespan);
    }

    /**
 * Standard accessor, return the average life span
 * Creation date: (06/12/01 10:15:13)
 * @return long
 */
    public long getAvgLifespan() {
        return avgLifespan;
    }

    /**
 * Standard accessor, return the average concurent
 * session handled by the server.
 * Creation date: (06/12/01 10:15:13)
 * @return double
 */
    public double getAvgNbConcSession() {
        return avgNbConcSession;
    }

    /**
 * currentActiveSession accessor.
 * Creation date: (12/12/01 13:28:52)
 * @return int
 */
    public int getCurrentActiveSession() {
        return currentActiveSession;
    }

    /**
 * Standard accessor, return the total aborded
 * session by the server.
 * Creation date: (06/12/01 10:15:13)
 * @return int
 */
    public int getTotalAbortedSession() {
        return totalAbortedSession;
    }

    /**
 * Standard accessor, return the total number
 * of session.
 * Creation date: (06/12/01 10:15:13)
 * @return int
 */
    public int getTotalNbSession() {
        return totalNbSession;
    }

    /**
 * Standard accessor, set the average life span
 * Creation date: (06/12/01 10:15:13)
 * @param newAvgLifespan long
 */
    private void setAvgLifespan(long newAvgLifespan) {
        avgLifespan = newAvgLifespan;
    }

    /**
 * Standard accessor, set the average concurent
 * session handled by the server.
 * Creation date: (06/12/01 10:15:13)
 * @param newAvgNbConcSession double
 */
    private void setAvgNbConcSession(double newAvgNbConcSession) {
        avgNbConcSession = newAvgNbConcSession;
    }

    /**
 * currentActiveSession accessor.
 * Creation date: (12/12/01 13:28:52)
 * @param newCurrentActiveSession int
 */
    private void setCurrentActiveSession(int newCurrentActiveSession) {
        currentActiveSession = newCurrentActiveSession;
    }

    /**
 * Standard accessor, return the total aborded
 * session by the server.
 * Creation date: (06/12/01 10:15:13)
 * @param newTotalAbortedSession int
 */
    private void setTotalAbortedSession(int newTotalAbortedSession) {
        totalAbortedSession = newTotalAbortedSession;
    }

    /**
 * Standard accessor, return the total number
 * of session.
 * Creation date: (06/12/01 10:15:13)
 * @param newTotalNbSession int
 */
    private void setTotalNbSession(int newTotalNbSession) {
        totalNbSession = newTotalNbSession;
    }
}
