package client;

public class Result implements Comparable<Result> {

    int clientID;

    String query;

    long start;

    long stop;

    boolean success;

    public long getMilliseconds() {
        return stop - start;
    }

    public long getStop() {
        return stop;
    }

    public boolean getSuccess() {
        return success;
    }

    public Result(int _clientID, String _query, long _start, long _stop, boolean _success) {
        clientID = _clientID;
        query = _query;
        start = _start;
        stop = _stop;
        success = _success;
    }

    public String toString() {
        return clientID + "," + query + "," + start + "," + stop + "," + (success ? "success" : "fail");
    }

    public int compareTo(Result r) {
        long diff = this.stop - r.stop;
        return diff < 0 ? -1 : (diff == 0 ? 0 : 1);
    }
}
