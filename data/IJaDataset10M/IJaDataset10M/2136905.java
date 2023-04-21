package net.sf.qagesa.stats;

/**
 *
 * @author Giovanni Novelli
 */
public class QAGESAStat {

    private static int replication = 0;

    private static int numUsers;

    private static int activeUsers;

    private static boolean cachingEnabled;

    private static int whichMeasure;

    private static RequestsHistory requestsHistory;

    /**
     * Creates a new instance of QAGESAStat
     */
    public QAGESAStat() {
    }

    public static synchronized void reset(int numCEs) {
        QAGESAStat.replication++;
        QAGESAStat.setRequestsHistory(new RequestsHistory(numCEs));
    }

    public static synchronized void incRequests(double clock) {
        QAGESAStat.getRequestsHistory().inc(clock);
    }

    public static synchronized void decRequests(double clock) {
        QAGESAStat.getRequestsHistory().dec(clock);
    }

    public static synchronized int getRequests() {
        return QAGESAStat.getRequestsHistory().getPlayRequests();
    }

    public static synchronized RequestsHistory getRequestsHistory() {
        return requestsHistory;
    }

    public static synchronized void setRequestsHistory(RequestsHistory aRequestsHistory) {
        requestsHistory = aRequestsHistory;
    }

    public static int getReplication() {
        return replication;
    }

    public static void setReplication(int aReplication) {
        replication = aReplication;
    }

    public static int getNumUsers() {
        return numUsers;
    }

    public static void setNumUsers(int aNumUsers) {
        numUsers = aNumUsers;
    }

    public static boolean isCachingEnabled() {
        return cachingEnabled;
    }

    public static void setCachingEnabled(boolean aCachingEnabled) {
        cachingEnabled = aCachingEnabled;
    }

    public static int getWhichMeasure() {
        return whichMeasure;
    }

    public static void setWhichMeasure(int aWhichMeasure) {
        whichMeasure = aWhichMeasure;
    }

    public static int getActiveUsers() {
        return activeUsers;
    }

    public static void setActiveUsers(int aActiveUsers) {
        activeUsers = aActiveUsers;
    }
}
