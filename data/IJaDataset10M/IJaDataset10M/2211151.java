package org.donnchadh.gaelbot.tasks;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Queue;
import java.util.Set;
import org.donnchadh.gaelbot.documentprocessors.DocumentProcessor;
import org.donnchadh.gaelbot.robots.RobotsChecker;
import org.donnchadh.gaelbot.urlprocessors.CompositeUrlProcessor;
import org.donnchadh.gaelbot.urlprocessors.UrlProcessor;
import org.donnchadh.gaelbot.urlprocessors.impl.DocumentHandlingUrlProcessor;
import org.donnchadh.gaelbot.urlprocessors.impl.LinkExtractingDocumentProcessor;
import org.htmlparser.Tag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;

public class LinkVisitorTask extends AbstractLinkVisitorTask {

    private final String newLink;

    private final Set<String> processed;

    private final Queue<String> urlQueue;

    private final RobotsChecker robotsChecker;

    private final LinkExtractingDocumentProcessor documentProcessor;

    public LinkVisitorTask(String newLink, Set<String> processed, Queue<String> urlQueue, RobotsChecker robotsChecker) {
        this(newLink, processed, urlQueue, robotsChecker, new LinkExtractingDocumentProcessor());
    }

    private LinkVisitorTask(String newLink, Set<String> processed, Queue<String> urlQueue, RobotsChecker robotsChecker, LinkExtractingDocumentProcessor documentProcessor) {
        this(newLink, processed, urlQueue, robotsChecker, documentProcessor, new DocumentHandlingUrlProcessor(documentProcessor));
    }

    private LinkVisitorTask(String newLink, Set<String> processed, Queue<String> urlQueue, RobotsChecker robotsChecker, LinkExtractingDocumentProcessor documentProcessor, DocumentProcessor otherDP, UrlProcessor otherUP) {
        this(newLink, processed, urlQueue, robotsChecker, documentProcessor, new CompositeUrlProcessor(new DocumentHandlingUrlProcessor(documentProcessor, otherDP), otherUP));
    }

    LinkVisitorTask(String newLink, Set<String> processed, Queue<String> urlQueue, RobotsChecker robotsChecker, UrlProcessor urlProcessor, DocumentProcessor documentProcessor) {
        this(newLink, processed, urlQueue, robotsChecker, new LinkExtractingDocumentProcessor(), documentProcessor, urlProcessor);
    }

    private LinkVisitorTask(String newLink, Set<String> processed, Queue<String> urlQueue, RobotsChecker robotsChecker, LinkExtractingDocumentProcessor documentProcessor, UrlProcessor urlProcessor) {
        super(urlProcessor);
        this.newLink = newLink;
        this.processed = processed;
        this.urlQueue = urlQueue;
        this.robotsChecker = robotsChecker;
        this.documentProcessor = documentProcessor;
    }

    public void run() {
        try {
            visitUrl(newLink);
            NodeList links = documentProcessor.getLinks();
            links.visitAllNodesWith(new NodeVisitor() {

                @Override
                public void visitTag(Tag tag) {
                    if (tag instanceof LinkTag) {
                        String linkUrl = ((LinkTag) tag).extractLink();
                        boolean isNotGoogleUrl = !linkUrl.contains(".google.ie/") && !linkUrl.contains(".google.com/") && !linkUrl.contains("/search?q=cache:") && linkUrl.startsWith("http://") && !processed.contains(linkUrl);
                        if (isNotGoogleUrl) {
                            urlQueue.add(linkUrl);
                            processed.add(linkUrl);
                        }
                    }
                }
            });
        } catch (ParserException e) {
            throw new RuntimeException(e);
        }
    }

    protected void visitUrl(String newLink) {
        try {
            URL url = new URL(newLink);
            boolean canCrawl = true;
            try {
                canCrawl = robotsChecker.checkRobots(url);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            if (canCrawl) {
                processUrl(url);
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
    }
}
