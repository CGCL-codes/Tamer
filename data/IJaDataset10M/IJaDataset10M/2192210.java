package com.idna.trace.core.model.logics.dream;

import java.util.List;
import net.icdpublishing.dream.results.SearchResult;
import org.apache.commons.collections.Bag;
import com.idna.trace.core.handler.result.RecordsWrapper;

public class LogicResponse {

    private SearchResult searchResult;

    private List<RecordsWrapper> list;

    private Bag stats;

    private int totalPeople;

    private int totalBusiness;

    private int totalPlaces;

    private int totalAvailable;

    private int totalUnavailable;

    private int totalRecordSize;

    private String tab;

    public LogicResponse() {
    }

    public LogicResponse(SearchResult searchResult, List<RecordsWrapper> list, Bag stats, int totalPeople, int totalBusiness, int totalPlaces, String tab) {
        this.searchResult = searchResult;
        this.list = list;
        this.stats = stats;
        this.totalPeople = totalPeople;
        this.totalBusiness = totalBusiness;
        this.totalPlaces = totalPlaces;
        this.tab = tab;
    }

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    public List<RecordsWrapper> getList() {
        return list;
    }

    public void setList(List<RecordsWrapper> list) {
        this.list = list;
    }

    public Bag getStats() {
        return stats;
    }

    public void setStats(Bag stats) {
        this.stats = stats;
    }

    public int getTotalPeople() {
        return totalPeople;
    }

    public void setTotalPeople(int totalPeople) {
        this.totalPeople = totalPeople;
    }

    public int getTotalBusiness() {
        return totalBusiness;
    }

    public void setTotalBusiness(int totalBusiness) {
        this.totalBusiness = totalBusiness;
    }

    public void setTotalPlaces(int totalPlaces) {
        this.totalPlaces = totalPlaces;
    }

    public int getTotalPlaces() {
        return totalPlaces;
    }

    public int getTotalAvailable() {
        return totalAvailable;
    }

    public void setTotalAvailable(int totalAvailable) {
        this.totalAvailable = totalAvailable;
    }

    public int getTotalUnavailable() {
        return totalUnavailable;
    }

    public void setTotalUnavailable(int totalUnavailable) {
        this.totalUnavailable = totalUnavailable;
    }

    public int getTotalRecordSize() {
        return totalRecordSize;
    }

    public void setTotalRecordSize(int totalRecordSize) {
        this.totalRecordSize = totalRecordSize;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }
}
