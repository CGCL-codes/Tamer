package eduburner.crawler;

import java.util.List;
import eduburner.crawler.enumerations.CrawlStatus;
import eduburner.crawler.frontier.Frontier;
import eduburner.crawler.model.CrawlURI;
import eduburner.crawler.processor.IProcessor;

public interface Crawler {

    /**
	 * Operator requested crawl begin
	 */
    public void requestCrawlStart();

    /**
	 * Operator requested for crawl to stop.
	 */
    public void requestCrawlStop();

    public List<IProcessor> getProcessors();

    public Frontier getFrontier();

    public void requestCrawlStop(CrawlStatus message);

    public void schedule(CrawlURI uri);
}
