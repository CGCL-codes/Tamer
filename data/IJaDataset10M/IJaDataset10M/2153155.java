package net.sf.wgfa.search.crawler;

public interface CrawlParserEventListener {

    /**
	 * Event handler for CrawlURLFoundEvent. Gets fired when a parser detects a new URL
	 * @param cufe CrawlURLFoundEvent
	 */
    public void CrawlURLFound(CrawlURLFoundEvent cufe);

    /**
	 * Event handler for StatementFoundEvent. Gets fired when a parser has found a new statement
	 * @param sfe StatementFoundEvent
	 */
    public void StatementFound(StatementFoundEvent sfe);

    public void CrawlURLError(CrawlURLErrorEvent cuee);

    public void CrawlURLDone(CrawlURLDoneEvent cude);
}
