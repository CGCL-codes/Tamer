package it.pronetics.madstore.crawler.parser;

import it.pronetics.madstore.crawler.model.Link;
import it.pronetics.madstore.crawler.model.Page;
import it.pronetics.madstore.crawler.parser.filter.LinkFilter;
import java.util.Collection;

/**
 * Parser interface for extracting {@link it.pronetics.madstore.crawler.model.Link}s from a given 
 * {@link it.pronetics.madstore.crawler.model.Page}.
 * 
 * @author Salvatore Incandela
 * @author Sergio Bossa
 */
public interface Parser {

    /**
     * Parse the given {@link it.pronetics.madstore.crawler.model.Page} and extract the {@link it.pronetics.madstore.crawler.model.Link}s.
     * @param data The page data to parse.
     * @param linkFilter The {@link it.pronetics.madstore.crawler.parser.filter.LinkFilter} to use for filtering extracted links.
     * @return A collection of extracted links.
     */
    public Collection<Link> parse(Page data, LinkFilter linkFilter);
}
