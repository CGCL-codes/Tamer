package net.sf.qagesa.stats;

/**
 *
 * @author Giovanni Novelli
 */
public class RequestsHistoryEntry {

    private double clock;

    private int playRequests;

    /** Creates a new instance of RequestsHistoryEntry */
    public RequestsHistoryEntry(double clock, int playRequests) {
        this.setClock(clock);
        this.setPlayRequests(playRequests);
    }

    public double getClock() {
        return clock;
    }

    public void setClock(double clock) {
        this.clock = clock;
    }

    public int getPlayRequests() {
        return playRequests;
    }

    public void setPlayRequests(int playRequests) {
        this.playRequests = playRequests;
    }

    public String toString() {
        return "ReF;" + this.getClock() + ";" + this.getPlayRequests();
    }
}
