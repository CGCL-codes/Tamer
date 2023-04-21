package org.epo.jpxi.generic;

/**
 * Object in which are stored statistics on JpxiSessions
 * Creation date: (18/10/01 15:42:21)
 */
public class JpxiSessionStat {

    private long resetTime = 0;

    private int nbSession = 0;

    private int nbAbort = 0;

    private long sessionLifespan = 0;

    private int concSessionValue = 0;

    private int concSessionNbEntries = 0;

    /**
 * JpxiSessionStat constructor
 * initialize the reset time
 */
    public JpxiSessionStat() {
        super();
        resetTime = System.currentTimeMillis();
    }

    /**
 * Add a new entry for the transaction statistics
 * Creation date: (18/10/01 15:54:21)
 * @param theLifespan long
 */
    public synchronized void addAbort(long theLifespan) {
        nbSession++;
        sessionLifespan += theLifespan;
        nbAbort++;
    }

    /**
 * Add a new entry for the number of concurrent sessions statistics
 * Creation date: (18/10/01 15:54:21)
 * @param theNbConcSession int
 */
    public synchronized void addConcEntry(int theNbConcSession) {
        if (theNbConcSession > 0) {
            concSessionValue += theNbConcSession;
            concSessionNbEntries++;
        }
    }

    /**
 * Add a new entry for the Archiving sessions statistics
 * Creation date: (18/10/01 15:54:21)
 * @param theLifespan long
 */
    public synchronized void addEntry(long theLifespan) {
        nbSession++;
        sessionLifespan += theLifespan;
    }

    /**
 * Get the average concurrent sessions
 * Creation date: (18/10/01 16:07:45)
 * @return double
 */
    public double getAvgConcSession() {
        double myPerc = 0;
        if (concSessionNbEntries > 0) {
            myPerc = (double) concSessionValue / (double) concSessionNbEntries;
            myPerc = ((int) (myPerc * 1000)) / 1000.0;
        } else myPerc = 1;
        return myPerc;
    }

    /**
 * Get the average lifespan per entry in ms
 * Creation date: (18/10/01 16:07:45)
 * @return long
 */
    public long getAvgLifespan() {
        if (nbSession > 0) return sessionLifespan / nbSession; else return 0;
    }

    /**
 * Standard accessor, return the number of aborded session.
 * Creation date: (18/10/01 15:58:12)
 * @return int
 */
    public int getNbAbort() {
        return nbAbort;
    }

    /**
 * Standard accessor, return the number of session
 * Creation date: (18/10/01 15:58:12)
 * @return int
 */
    public int getNbSession() {
        return nbSession;
    }

    /**
 * Standard accessor, return the reset time.
 * Creation date: (18/10/01 16:48:29)
 * @return java.sql.Timestamp
 */
    public java.sql.Timestamp getResetTime() {
        return new java.sql.Timestamp(resetTime);
    }

    /**
 * Reset the stats.
 * Creation date: (18/10/01 15:46:19)
 */
    public synchronized void reset() {
        nbSession = 0;
        nbAbort = 0;
        sessionLifespan = 0;
        concSessionValue = 0;
        concSessionNbEntries = 0;
        resetTime = System.currentTimeMillis();
    }
}
