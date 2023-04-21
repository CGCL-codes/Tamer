package it.pronetics.madstore.crawler.parser.impl;

import it.pronetics.madstore.crawler.model.Link;
import it.pronetics.madstore.crawler.model.Page;
import it.pronetics.madstore.crawler.parser.Parser;
import it.pronetics.madstore.crawler.parser.filter.LinkFilter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link it.pronetics.madstore.crawler.parser.Parser} implementation extracting links and providing
 * URI normalization by:
 * <ul>
 * <li>Removing unneeded sub-paths.</li>
 * <li>Removing the URI fragment.</li>
 * <li>Removing the URI query string.</li>
 * <li>Making the URI absolute.</li>
 * </ul>
 *
 * @author Salvatore Incandela
 * @author Sergio Bossa
 */
public class ParserImpl implements Parser {

    private static final Logger LOG = LoggerFactory.getLogger(ParserImpl.class);

    public Collection<Link> parse(Page page, LinkFilter linkFilter) {
        LOG.info("Parsing and extracting links from: {}", page.getLink());
        Collection<String> extractedLinks = extractLinks(page);
        Collection<Link> parsedLinks = new HashSet<Link>(extractedLinks.size());
        for (String link : extractedLinks) {
            try {
                String normalizedLink = removeFragment(link);
                normalizedLink = removeQueryString(normalizedLink);
                normalizedLink = makeAbsolute(page.getLink().getLink(), normalizedLink);
                Link linkToAdd = new Link(normalizedLink);
                if (linkFilter.accept(linkToAdd)) {
                    parsedLinks.add(linkToAdd);
                }
            } catch (Exception ex) {
                LOG.warn("Error parsing link: {}", link);
                LOG.warn(ex.getMessage());
                LOG.debug(ex.getMessage(), ex);
            }
        }
        return parsedLinks;
    }

    private Collection<String> extractLinks(Page page) {
        try {
            org.htmlparser.Parser htmlParser = new org.htmlparser.Parser(page.getData());
            NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
            NodeList linkNodes = htmlParser.extractAllNodesThatMatch(linkFilter);
            Collection<String> links = new ArrayList<String>(linkNodes.size());
            for (int i = 0; i < linkNodes.size(); i++) {
                String link = ((LinkTag) linkNodes.elementAt(i)).extractLink().trim();
                links.add(link);
            }
            return links;
        } catch (Exception ex) {
            LOG.warn("Error extracting links from: {}", page.getLink());
            LOG.warn(ex.getMessage());
            LOG.debug(ex.getMessage(), ex);
            return new ArrayList<String>(0);
        }
    }

    private String removeFragment(String url) {
        String result = url;
        int fragmentIndex = url.indexOf('#');
        if (fragmentIndex >= 0) {
            result = url.substring(0, fragmentIndex);
        }
        return result;
    }

    private String removeQueryString(String url) {
        String result = url;
        int queryStringIndex = url.indexOf('?');
        if (queryStringIndex >= 0) {
            result = url.substring(0, queryStringIndex);
        }
        return result;
    }

    private String makeAbsolute(String base, String link) throws Exception {
        if (link == null || link.equals("")) {
            return new URI(base).normalize().toString();
        } else {
            URI absoluteUri = new URI(base).resolve(link).normalize();
            return absoluteUri.toString();
        }
    }
}
