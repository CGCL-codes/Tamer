package fitnesse.components;

import java.util.Set;
import fitnesse.wiki.PageCrawler;
import fitnesse.wiki.WikiPage;

public class CrawlingTagManager implements TagManager {

    private static final TagManager instance = new CrawlingTagManager();

    public static TagManager getInstance() {
        return instance;
    }

    public void recordTagsForPage(WikiPage page) {
    }

    public Set pagesForTags(WikiPage root, String[] tags, boolean anyTags, boolean noTags) throws Exception {
        PageCrawler crawler = root.getPageCrawler();
        TagMatchingListener tagMatchingListener = new TagMatchingListener(tags, anyTags, noTags);
        crawler.traverse(root, tagMatchingListener);
        return tagMatchingListener.getPages();
    }

    public void clearIndex() {
    }

    public void removeExistingTagsForPage(WikiPage wikiPage) {
    }
}
