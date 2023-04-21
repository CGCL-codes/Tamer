package org.zaproxy.zap.extension.search;

public class SearchMatch {

    public enum Locations {

        REQUEST_HEAD, REQUEST_BODY, RESPONSE_HEAD, RESPONSE_BODY
    }

    ;

    private Locations location;

    private int start;

    private int end;

    public SearchMatch(Locations location, int start, int end) {
        super();
        this.location = location;
        this.start = start;
        this.end = end;
    }

    public Locations getLocation() {
        return location;
    }

    public void setLocation(Locations location) {
        this.location = location;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
