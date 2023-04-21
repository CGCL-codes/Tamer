package com.framework.util.displaytag;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.displaytag.properties.SortOrderEnum;

/**
 * <code>com.gorti.project.web.ui.action.PaginatedListImpl</code> implemnts </code>com.gorti.project.web.ui.action.IExtendedPaginatedList</code>
 * This class can be used for pagination purpose.
 * This class depends upon HttpServletRequest object.
 * To be used by Controllers in case of Http requests.
 * @author Ram Gorti
 */
public class PaginatedListImpl implements IExtendedPaginatedList {

    /** current page index, starts at 0 */
    private int index;

    /** number of results per page (number of rows per page to be displayed )  */
    private int pageSize;

    /** total number of results (the total number of rows  ) */
    private int fullListSize;

    /** list of results (rows found ) in the current page */
    private List list;

    /** default sorting order */
    private SortOrderEnum sortDirection = SortOrderEnum.ASCENDING;

    /** sort criteria (sorting property name) */
    private String sortCriterion;

    /** Http servlet request **/
    private HttpServletRequest request;

    /**  default constructor **/
    public PaginatedListImpl() {
    }

    /**Create <code>PaginatedListImpl</code> instance using the <code>HttpServletRequest</code> object.
     * @param request <code>HttpServletRequest</code> object.
     * @param pageSize the page size - the total number of rows per page.
     */
    public PaginatedListImpl(HttpServletRequest request, int pageSize) {
        sortCriterion = request.getParameter(IExtendedPaginatedList.IRequestParameters.SORT);
        sortDirection = IExtendedPaginatedList.IRequestParameters.DESC.equals(request.getParameter(IExtendedPaginatedList.IRequestParameters.DIRECTION)) ? SortOrderEnum.DESCENDING : SortOrderEnum.ASCENDING;
        pageSize = pageSize != 0 ? pageSize : DEFAULT_PAGE_SIZE;
        String page = request.getParameter(IExtendedPaginatedList.IRequestParameters.PAGE);
        index = page == null ? 0 : Integer.parseInt(page) - 1;
    }

    /** Create <code>PaginatedListImpl</code> instance .
     * @param pageSize the page size - the total number of rows per page.
     * @return <code>IExtendedPaginatedList</code> instance.
     * @throws Exception - problem while creating paginatedlist object.
     */
    public IExtendedPaginatedList getPaginatedListObject(int pageSize) throws Exception {
        if (request == null) {
            throw new Exception("Cannot create paginated list. Depends upon HttpServletRequest.");
        }
        return new PaginatedListImpl(request, pageSize);
    }

    /** Set the non-null <code>HttpServletRequest</code> object.
     * @param request a <code>HttpServletRequest</code> object.
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public int getFirstRecordIndex() {
        return index * pageSize;
    }

    /** return the index.
     * @return the index.
     */
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List getList() {
        return list;
    }

    public void setList(List results) {
        this.list = results;
    }

    public int getFullListSize() {
        return fullListSize;
    }

    public void setTotalNumberOfRows(int total) {
        this.fullListSize = total;
    }

    /** return the total number of pages for the total number of rows.
     * @return the total.
     */
    public int getTotalPages() {
        return (int) Math.ceil(((double) fullListSize) / pageSize);
    }

    public int getObjectsPerPage() {
        return pageSize;
    }

    public int getPageNumber() {
        return index + 1;
    }

    public String getSearchId() {
        return null;
    }

    public String getSortCriterion() {
        return sortCriterion;
    }

    public SortOrderEnum getSortDirection() {
        return sortDirection;
    }

    public void setSortCriterion(String sortCriterion) {
        this.sortCriterion = sortCriterion;
    }

    public void setSortDirection(SortOrderEnum sortDirection) {
        this.sortDirection = sortDirection;
    }
}
