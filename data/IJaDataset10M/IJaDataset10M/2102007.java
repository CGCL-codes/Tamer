package net.lateeye.search.thesissearch;

import java.util.LinkedList;
import net.lateeye.search.SearchResult;

/**
 * A single search result class generally defined for thesis search. Note that
 * this is concrete (not abstract) class so that it can be used as a concrete
 * instance in the sub packages (as of Jan 13, 2011, ACM package does so).
 * 
 * @author Isao Isaac Saito <130s@lateeye.net>
 * 
 */
public class ThesisSearchResult extends SearchResult {

    /**
	 * @author ISAO Saito <130s@1995.sfc.ne.jp>
	 * @since 2008/04/04
	 */
    private static final long serialVersionUID = 2191547569407768182L;

    private int publishedyear = -1;

    private LinkedList<String> keywords = new LinkedList<String>();

    private LinkedList<String> authors = new LinkedList<String>();

    private String publisher = null;

    private String fulltextUrl = null;

    private String detailPageUrl = null;

    private int citationCount = -1;

    private String abs = null;

    private String postedConference = null;

    public ThesisSearchResult() {
        super();
    }

    public int getPublishedyear() {
        return this.publishedyear;
    }

    public void setPublishedyear(int publishedyear) {
        this.publishedyear = publishedyear;
    }

    public LinkedList<String> getKeywords() {
        return this.keywords;
    }

    public void setKeywords(LinkedList<String> keywords) {
        this.keywords = keywords;
    }

    public LinkedList<String> getAuthors() {
        return this.authors;
    }

    public void setAuthors(LinkedList<String> authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getFulltextUrl() {
        return this.fulltextUrl;
    }

    public void setFulltextUrl(String fulltextUrl) {
        this.fulltextUrl = fulltextUrl;
    }

    public String getDetailPageUrl() {
        return this.detailPageUrl;
    }

    public void setDetailPageUrl(String detailPageUrl) {
        this.detailPageUrl = detailPageUrl;
    }

    public int getCitationCount() {
        return this.citationCount;
    }

    public void setCitationCount(int citationCount) {
        this.citationCount = citationCount;
    }

    public String getAbs() {
        return super.getDescription();
    }

    public String getPostedConference() {
        return this.postedConference;
    }

    public void setPostedConference(String postedConference) {
        this.postedConference = postedConference;
    }
}
