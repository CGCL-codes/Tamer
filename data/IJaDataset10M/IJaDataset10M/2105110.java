package be.bzbit.framework.domain.search;

/**
 * Search Criteria with support for paging
 * 
 * @author Jurgen Lust
 * @author $LastChangedBy: Jurgen.Lust $
 * 
 * @version $LastChangedRevision: 3 $
 */
public interface SearchCriteria {

    public abstract int getPageSize();

    public abstract void setPageSize(int pageSize);

    public abstract int getCurrentPage();

    public abstract void setCurrentPage(int currentPage);

    public abstract Sort getSort();

    public abstract void setSort(Sort sort);

    public abstract void nextPage();

    public abstract void previousPage();

    public abstract void resetPage();
}
