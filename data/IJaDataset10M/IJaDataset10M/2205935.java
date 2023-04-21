package cn.com.believer.songyuanframework.openapi.storage.box.functions;

/**
 * @author jjia
 * 
 */
public interface SearchRequest extends BoxAuthRequest {

    /**
     * @return the query
     */
    public String getQuery();

    /**
     * @param query
     *            the query to set
     */
    public void setQuery(String query);

    /**
     * @return the page
     */
    public long getPage();

    /**
     * @param page
     *            the page to set
     */
    public void setPage(long page);

    /**
     * @return the perPage
     */
    public long getPerPage();

    /**
     * @param perPage
     *            the perPage to set
     */
    public void setPerPage(long perPage);

    /**
     * @return the sort
     */
    public String getSort();

    /**
     * @param sort
     *            the sort to set
     */
    public void setSort(String sort);

    /**
     * @return the direction
     */
    public String getDirection();

    /**
     * @param direction
     *            the direction to set
     */
    public void setDirection(String direction);

    /**
     * @return the params
     */
    public String[] getParams();

    /**
     * @param params
     *            the params to set
     */
    public void setParams(String[] params);
}
