package ezi.crawler.service;

import java.util.LinkedList;
import java.util.List;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;
import ezi.crawler.db.model.WebPage;
import ezi.crawler.manager.CrawlerManager;
import ezi.crawler.manager.CrawlerManagerFactory;
import ezi.crawler.util.Statistics;

public class Crawler {

    private static long counter = 0;

    private static long getNextCounter() {
        return counter++;
    }

    private Statistics stats = new Statistics();

    private CrawlerManager cm = CrawlerManagerFactory.getCrawlerManager();

    public List<String> extractLinks(String fromUrl) {
        List<String> result = new LinkedList<String>();
        try {
            Parser parser = new Parser(fromUrl);
            NodeFilter filter = new NodeClassFilter(LinkTag.class);
            NodeList linkNodeList = parser.extractAllNodesThatMatch(filter);
            SimpleNodeIterator iter = linkNodeList.elements();
            String link = null;
            while (iter.hasMoreNodes()) {
                LinkTag node = (LinkTag) iter.nextNode();
                link = node.getLink();
                if (link.startsWith("http://") && !link.endsWith(".exe") && !link.endsWith(".jpg") && !link.endsWith(".gif") && !link.endsWith(".pdf") && !link.endsWith(".rar") && !link.endsWith(".zip") && !link.endsWith(".mpg") && !link.endsWith(".mpeg") && !link.endsWith(".avi") && !link.endsWith(".mov")) {
                    result.add(link);
                }
            }
        } catch (ParserException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void visitPage(String fromUrl) {
        System.out.println("NUMBER OF LINKS: " + stats.getNumberOfLink());
        System.out.println("NUMBER OF VISITED: " + stats.getNumberOfVisitedPages());
        System.out.println(getNextCounter() + ": VISITING PAGE: " + fromUrl);
        cm.setVisited(fromUrl);
        List<String> newUrls = extractLinks(fromUrl);
        for (String url : newUrls) {
            if (cm.isAlreadyVisited(fromUrl, url)) {
                continue;
            }
            cm.makeTransition(fromUrl, url);
        }
    }

    public void start(String initialUrl) {
        if (initialUrl != null) {
            visitPage(initialUrl);
        }
        WebPage page = null;
        while ((page = cm.getInitialWebPage()) != null) {
            visitPage(page.getUrl());
        }
    }

    public static void main(String[] args) {
        String initialURL = null;
        if (args.length >= 1) {
            if (args[0].startsWith("http://")) {
                initialURL = args[0];
            }
        }
        Crawler crawler = new Crawler();
        crawler.start(initialURL);
    }
}
