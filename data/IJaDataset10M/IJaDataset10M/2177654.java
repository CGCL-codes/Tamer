package com.liferay.netvibes.model;

import java.io.Serializable;

/**
 * <a href="NetvibesSearchResult.java.html"><b><i>View Source</i></b></a>
 *
 * @author Julio Camarero
 */
public class NetvibesSearchResult implements Serializable {

    public NetvibesSearchResult() {
    }

    public NetvibesSearchResult(String query, String sort, int category, String type, String region, int page) {
        _query = query;
        _sort = sort;
        _category = category;
        _type = type;
        _region = region;
        _page = page;
    }

    public String getQuery() {
        return _query;
    }

    public void setQuery(String query) {
        _query = query;
    }

    public String getSort() {
        return _sort;
    }

    public void setSort(String sort) {
        _sort = sort;
    }

    public String getRegion() {
        return _region;
    }

    public void setRegion(String region) {
        _region = region;
    }

    public int getCategory() {
        return _category;
    }

    public void setCategory(int category) {
        _category = category;
    }

    public String getType() {
        return _type;
    }

    public void setType(String type) {
        _type = type;
    }

    public int getPage() {
        return _page;
    }

    public void setPage(int page) {
        _page = page;
    }

    private String _query;

    private String _sort;

    private int _category;

    private String _region;

    private String _type;

    private int _page;
}
