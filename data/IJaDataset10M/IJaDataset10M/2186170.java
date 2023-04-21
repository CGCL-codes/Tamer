package org.light.portal.search.model;

import java.util.List;
import org.light.portal.util.LabelBean;

/**
 * 
 * @author Jianmin Liu
 **/
public class SearchResult {

    private int total;

    private List<SearchResultItem> items;

    private List<LabelBean> facets1;

    private List<LabelBean> facets2;

    public SearchResult() {
    }

    public SearchResult(int total, List<SearchResultItem> items) {
        this();
        this.total = total;
        this.items = items;
    }

    public SearchResult(int total, List<SearchResultItem> items, List<LabelBean> facets1, List<LabelBean> facets2) {
        this(total, items);
        this.facets1 = facets1;
        this.facets2 = facets2;
    }

    public List<SearchResultItem> getItems() {
        return items;
    }

    public int getTotal() {
        return total;
    }

    public List<LabelBean> getFacets1() {
        return facets1;
    }

    public List<LabelBean> getFacets2() {
        return facets2;
    }
}
