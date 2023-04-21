package de.tudarmstadt.ukp.wikipedia.api;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;

/**
 * An iterator over page objects selected by a query.
 * @author zesch
 *
 */
public class PageQueryIterator implements Iterator<Page> {

    private final Log logger = LogFactory.getLog(getClass());

    private Wikipedia wiki;

    private int iterPosition;

    private List<Integer> pageIDs;

    public PageQueryIterator(Wikipedia wiki, List<Integer> pPageIDs) {
        this.wiki = wiki;
        this.iterPosition = 0;
        this.pageIDs = pPageIDs;
    }

    public boolean hasNext() {
        if (iterPosition < this.pageIDs.size()) {
            return true;
        } else {
            return false;
        }
    }

    public Page next() {
        Page page = null;
        try {
            page = this.wiki.getPage(pageIDs.get(iterPosition));
        } catch (WikiApiException e) {
            logger.error("Could not load page with id " + pageIDs.get(iterPosition));
            e.printStackTrace();
        }
        iterPosition++;
        return page;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
