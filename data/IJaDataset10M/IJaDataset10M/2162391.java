package com.generalynx.ecos.page;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Criteria;

public class ScrollPage extends HibernatePage {

    private ScrollableResults scrollableResults;

    /**
     * Construct a new HibernatePage. HibernatePage numbers are zero-based so the
     * first page is page 0.
     *
     * @param query      the Hibernate Query
     * @param pageNumber the page number (zero-based); if Integer.MAX_VALUE will return the last page for the query
     * @param pageSize   the number of results to display on the page
     */
    public ScrollPage(Query query, int pageNumber, int pageSize) {
        super(pageNumber, pageSize);
        scrollableResults = query.scroll();
        createElements(query);
    }

    /**
     * Construct a new HibernatePage. HibernatePage numbers are zero-based so the
     * first page is page 0.
     *
     * @param criteria   the Hibernate Criteria
     * @param pageNumber the page number (zero-based); if Integer.MAX_VALUE will return the last page for the query
     * @param pageSize   the number of results to display on the page
     */
    public ScrollPage(Criteria criteria, int pageNumber, int pageSize) {
        super(pageNumber, pageSize);
        scrollableResults = criteria.scroll();
        createElements(criteria);
    }

    public int getTotalNumberOfElements() {
        if (totalElements < 0) {
            try {
                scrollableResults.last();
                totalElements = scrollableResults.getRowNumber();
            } catch (HibernateException e) {
                totalElements = 0;
                getLogger().error("Failed to get last row number from scollable results: " + e.getMessage());
            } finally {
                scrollableResults.close();
            }
        }
        return totalElements;
    }
}
