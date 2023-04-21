package com.matthewmadson.util.search.yahooboss;

public class YBWebQueryMetadata {

    private long totalHits;

    private long count;

    private long start;

    private String nextPage;

    private int responseCode;

    private long deephits;

    public void setTotalHits(final long hits) {
        this.totalHits = hits;
    }

    public long getTotalHits() {
        return this.totalHits;
    }

    public void setCount(final long count) {
        this.count = count;
    }

    public long getCount() {
        return this.count;
    }

    public void setStart(final long start) {
        this.start = start;
    }

    public long getStart() {
        return this.start;
    }

    public void setNextPage(final String nextPage) {
        this.nextPage = nextPage;
    }

    public String getNextPage() {
        return this.nextPage;
    }

    public void setResponseCode(final int code) {
        this.responseCode = code;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public void setDeepHits(final long hits) {
        this.deephits = hits;
    }

    public long getDeepHits() {
        return this.deephits;
    }
}
