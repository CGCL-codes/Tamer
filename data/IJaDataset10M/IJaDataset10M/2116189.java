package wpspider.client.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import wpspider.client.common.WPSpiderProperties;
import wpspider.client.dao.PageDAO;

public class PageListRetriever {

    /** Property key for PageDAO class. */
    private static final String PAGE_DAO_CLASS = "wpspider.client.model.PAGE_DAO_CLASS";

    /** Logger. */
    private static Logger _logger = Logger.getLogger(PageListRetriever.class);

    /** DAO. */
    private PageDAO _dao;

    /** Pages. */
    private Map<String, Page> _pages;

    /** Max depth of search tree. */
    private int _maxDepth;

    /**
     * Constructor.
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public PageListRetriever() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String daoClass = WPSpiderProperties.getInstance().getProperty(PAGE_DAO_CLASS);
        _dao = (PageDAO) Class.forName(daoClass).newInstance();
        _maxDepth = 0;
        _pages = null;
    }

    /**
     * Gets pages.
     * @param name Name of page.
     * @param maxDepth Max depth of search tree.
     * @return List of pages.
     * @throws Exception
     */
    public Map retrieve(String name, int maxDepth) throws Exception {
        _maxDepth = maxDepth;
        _pages = new HashMap<String, Page>();
        int depth = 0;
        Page page = _dao.findByName(name);
        gatherPagesRecursively(page, depth + 1);
        return _pages;
    }

    /**
     * Gathers pages recursively.
     * @param page Page
     * @param depth Depth.
     * @throws Exception
     */
    private void gatherPagesRecursively(Page page, int depth) throws Exception {
        if (page != null && filterPage(page)) {
            _logger.debug("name=" + page.getName() + ", depth=" + depth);
            _pages.put(page.getName(), page);
            if (depth <= _maxDepth) {
                List<String> words = page.getWords();
                if (words != null) {
                    List<Page> pages = _dao.findByNames(words);
                    for (Iterator iterator = pages.iterator(); iterator.hasNext(); ) {
                        Page xpage = (Page) iterator.next();
                        if (!_pages.containsKey(xpage.getName())) {
                            gatherPagesRecursively(xpage, depth + 1);
                        }
                    }
                }
            }
        }
    }

    /**
     * @param page
     * @return
     */
    private boolean filterPage(Page page) {
        String name = page.getName();
        return !(EXCLUDE_PATTERN1.matcher(name).matches() || EXCLUDE_PATTERN2.matcher(name).matches());
    }

    private static final Pattern EXCLUDE_PATTERN1 = Pattern.compile("[0-9]+月");

    private static final Pattern EXCLUDE_PATTERN2 = Pattern.compile("[0-9]+年");
}
