package hermes.classifier;

import com.hp.hpl.jena.ontology.OntResource;

/**
 * This class is build to process the matching of a word against all news-items. And if a match is found
 * in a news-item store the found relationship.
 */
public class ItemChecker extends Thread {

    private String lookFor;

    private NewsItem[] newsItems;

    private OntResource currentResource;

    /**
     * This constructor stores the essential data the class needs to do the comparison of the word 
     * against all news-items.
     * @param lookFor The word that should be searched for
     * @param newsItems The news-items that should be searched through
     * @param currentResource The currentResource where the word is a representative of.
     */
    public ItemChecker(String lookFor, NewsItem[] newsItems, OntResource currentResource) {
        this.lookFor = lookFor;
        this.newsItems = newsItems;
        this.currentResource = currentResource;
    }

    /**
     * This function loops through all news-items and call for each news-items the matchItem method
     * in the compare class. If this yields result, it store the relationship in the news-item.
     */
    public void loopNewsItems() {
        synchronized (this) {
            for (int i = 0; i < newsItems.length; i++) {
                int timesFound = Compare.matchItem(lookFor, newsItems[i].getText());
                if (timesFound > 0) {
                    synchronized (newsItems[i]) {
                        newsItems[i].addRelation(currentResource, lookFor, timesFound);
                    }
                }
            }
        }
    }

    /**
     * This function calls loopNewsItem and makes the class runs a separate thread.
     */
    public void run() {
        System.out.println(lookFor);
        loopNewsItems();
        Counter counter = Ontology.getCounter();
        synchronized (counter) {
            counter.decrease();
        }
    }
}
