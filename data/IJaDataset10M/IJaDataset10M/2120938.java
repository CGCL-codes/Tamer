package nl.gridshore.newsfeed.web.tags;

import nl.gridshore.newsfeed.domain.NewsItem;

/**
 * @author Jettro Coenradie
 */
public class NewsItemSecurityTag extends SecurityTag {

    private NewsItem newsItem;

    protected NewsItem newsItem() {
        return newsItem;
    }

    /**
     * Used to set the news item to check for authorization options.
     * @param newsItem NewsItem to check
     */
    public void setNewsItem(NewsItem newsItem) {
        this.newsItem = newsItem;
    }
}
