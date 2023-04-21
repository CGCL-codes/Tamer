package net.sf.wgfa.search.crawler;

public class CrawlURLErrorEvent {

    private CrawlURL cUrl;

    private String reason;

    private CrawlURLErrorEvent() {
    }

    public static CrawlURLErrorEvent getInstance(CrawlURL cUrl, String reason) {
        CrawlURLErrorEvent cuee = new CrawlURLErrorEvent();
        cuee.cUrl = cUrl;
        cuee.reason = reason;
        return cuee;
    }

    public String getReason() {
        return reason;
    }

    public CrawlURL getCrawlURL() {
        return cUrl;
    }
}
