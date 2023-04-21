package si.ibloc.cms.logic.interfaces;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public interface FullTextSearch {

    String getSearchQuery();

    void setSearchQuery(String searchQuery);

    int getNumberOfResults();

    void nextPage();

    void prevPage();

    boolean isLastPage();

    boolean isFirstPage();

    String doSearch();

    void selectFromRequest();

    int getPageSize();

    void setPageSize(int pageSize);

    void reset();

    void destroy();
}
