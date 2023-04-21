package net.sourceforge.retriever;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import net.sourceforge.retriever.analyzer.Analyzer;
import net.sourceforge.retriever.authentication.Authentication;
import net.sourceforge.retriever.feedback.CrawlerFeedback;
import net.sourceforge.retriever.fetcher.Fetcher;
import net.sourceforge.retriever.fetcher.hostlock.HostLock;
import net.sourceforge.retriever.fetcher.resource.Resource;
import net.sourceforge.retriever.fetcher.resource.ResourceMonitor;
import net.sourceforge.retriever.filter.Filter;
import net.sourceforge.retriever.urlfrontier.URLFrontier;

class URLParser implements Runnable {

    private final URLFrontier urlFrontier;

    private final Filter filter;

    private final List<Analyzer> analyzers;

    private final CrawlerFeedback feedback;

    private final HostLock hostLock;

    private final ResourceMonitor resourceMonitor;

    private final Authentication authentication;

    URLParser(final URLFrontier urlFrontier, final Filter filter, final List<Analyzer> analyzers, final CrawlerFeedback feedback, final HostLock hostLock, final ResourceMonitor resourceMonitor, final Authentication authentication) {
        this.urlFrontier = urlFrontier;
        this.filter = filter;
        this.analyzers = analyzers;
        this.feedback = feedback;
        this.hostLock = hostLock;
        this.resourceMonitor = resourceMonitor;
        this.authentication = authentication;
    }

    public void run() {
        String url = null;
        try {
            url = this.urlFrontier.dequeue();
            if (url == null) return;
            if (!this.filter.acceptBeforeFetch(url)) return;
            final Fetcher fetcher = Fetcher.create(new URL(url), this.hostLock);
            final Resource resource = fetcher.fetch();
            this.authenticate(resource);
            this.resourceMonitor.addMonitoringInfo(resource);
            if (!this.filter.acceptAfterFetch(resource)) return;
            this.analyze(resource);
            this.urlFrontier.enqueue(resource);
            this.enqueue(resource.getInnerURLs());
        } catch (final Exception e) {
            this.feedback.error("Exception fetching the resource " + url, e);
        } finally {
            this.wakeUpThreadsWatingForURLsInsideTheFrontier();
        }
    }

    private void authenticate(final Resource resource) {
        final URL resultinURL = this.authentication.authenticate(resource.getURL(), resource.getInputStream());
        if (resultinURL != null) {
            this.urlFrontier.enqueue(new Resource(resultinURL));
        }
    }

    private void wakeUpThreadsWatingForURLsInsideTheFrontier() {
        synchronized (this.urlFrontier) {
            this.urlFrontier.notifyAll();
        }
    }

    private void analyze(final Resource fetchedResource) {
        for (Analyzer analyzer : this.analyzers) {
            analyzer.analyze(fetchedResource);
        }
    }

    private void enqueue(final List<String> innerURLs) {
        for (String innerURL : innerURLs) {
            if (this.filter.acceptBeforeFetch(innerURL)) {
                try {
                    this.urlFrontier.enqueue(new Resource(new URL(innerURL)));
                } catch (final MalformedURLException e) {
                    this.feedback.error("Exception parsing the inner url " + innerURL, e);
                }
            }
        }
    }
}
