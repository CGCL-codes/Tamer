package org.bionote.search;

import java.util.List;
import org.bionote.om.IPage;
import org.bionote.om.IRevision;
import org.bionote.om.ISpace;
import org.bionote.om.service.PageService;

/**
 * @author mbreese
 *
 */
public interface Search {

    /**
     * Adds a page to the index using the current revision, and only the current revision.
     * Perhaps I'll add revisions later, but for now, just deal with the current rev.
     * @param page
     */
    public abstract void indexPage(IPage page);

    public abstract void indexPage(IRevision rev);

    public abstract List search(String query, String pageTypeClassName, boolean restrictSpace, ISpace[] spaces, boolean restrictPublic, Integer labelId);

    /**
     * @param path The path to set.
     */
    public abstract void setPath(String path);

    /**
     * Sets the search index path
     * @param path
     * @param absolute - is this an absolute path?  false = find path with webapp root
     */
    public abstract void setPath(String path, boolean absolute);

    /**
     * @param pageService The pageService to set.
     */
    public abstract void setPageService(PageService pageService);
}
