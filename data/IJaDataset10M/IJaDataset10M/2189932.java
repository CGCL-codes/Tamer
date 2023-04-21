package com.idna.trace.core.dto;

import java.util.List;
import org.apache.commons.collections.Bag;
import net.icdpublishing.dream.results.SearchResult;
import com.idna.trace.core.handler.result.RecordsWrapper;
import com.idna.trace.utils.SearchType;
import com.idna.trace.utils.exceptions.ErrorCorporate;
import com.idna.trace.utils.parameters.LocationParameters;

public class RecordSetDTO {

    private final List<RecordsWrapper> recordsWrapper;

    private final Bag stats;

    private final Integer currentPage;

    private final String clientId;

    private final Integer resultsPerPage;

    private final LocationParameters locationParameters;

    private final SearchType searchType;

    private final Integer state;

    private final String previousSearch;

    private final SearchType originalSearchType;

    private final String clientType;

    private final List<ErrorCorporate> errors;

    private final SearchResult searchResult;

    private final String tab;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private List<RecordsWrapper> recordsWrapper;

        private Bag stats;

        private Integer currentPage;

        private String clientId;

        private Integer resultsPerPage;

        private LocationParameters locationParameters;

        private SearchType searchType;

        private Integer state;

        private String previousSearch;

        private SearchType originalSearchType;

        private String clientType;

        private List<ErrorCorporate> errors;

        private SearchResult searchResult;

        private String tab;

        public Builder searchResult(SearchResult searchResult) {
            this.searchResult = searchResult;
            return this;
        }

        public Builder tab(String tab) {
            this.tab = tab;
            return this;
        }

        public Builder clientType(String clientType) {
            this.clientType = clientType;
            return this;
        }

        public Builder originalSearchType(SearchType originalSearchType) {
            this.originalSearchType = originalSearchType;
            return this;
        }

        public Builder previousSearch(String previousSearch) {
            this.previousSearch = previousSearch;
            return this;
        }

        public Builder state(Integer state) {
            this.state = state;
            return this;
        }

        public Builder searchType(SearchType searchType) {
            this.searchType = searchType;
            return this;
        }

        public Builder locationParameters(LocationParameters locationParameters) {
            this.locationParameters = locationParameters;
            return this;
        }

        public Builder recordsWrapper(List<RecordsWrapper> recordsWrapper) {
            this.recordsWrapper = recordsWrapper;
            return this;
        }

        public Builder stats(Bag stats) {
            this.stats = stats;
            return this;
        }

        public Builder currentPage(Integer currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder resultsPerPage(Integer resultsPerPage) {
            this.resultsPerPage = resultsPerPage;
            return this;
        }

        public Builder errors(List<ErrorCorporate> errors) {
            this.errors = errors;
            return this;
        }

        public RecordSetDTO build() {
            return new RecordSetDTO(searchResult, recordsWrapper, stats, currentPage, clientId, resultsPerPage, locationParameters, searchType, state, previousSearch, originalSearchType, clientType, errors, tab);
        }
    }

    public RecordSetDTO(SearchResult searchResult, List<RecordsWrapper> recordsWrapper, Bag stats, Integer currentPage, String clientId, Integer resultsPerPage, LocationParameters locationParameters, SearchType searchType, Integer state, String previousSearch, SearchType originalSearchType, String clientType, List<ErrorCorporate> errors, String tab) {
        this.searchResult = searchResult;
        this.recordsWrapper = recordsWrapper;
        this.stats = stats;
        this.currentPage = currentPage;
        this.clientId = clientId;
        this.resultsPerPage = resultsPerPage;
        this.searchType = searchType;
        this.locationParameters = locationParameters;
        this.state = state;
        this.previousSearch = previousSearch;
        this.originalSearchType = originalSearchType;
        this.clientType = clientType;
        this.errors = errors;
        this.tab = tab;
    }

    public String getTab() {
        return tab;
    }

    public String getClientType() {
        return clientType;
    }

    public SearchType getOriginalSearchType() {
        return originalSearchType;
    }

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public List<RecordsWrapper> getRecordsWrapper() {
        return recordsWrapper;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public String getClientId() {
        return clientId;
    }

    public Integer getResultsPerPage() {
        return resultsPerPage;
    }

    public LocationParameters getLocationParameters() {
        return locationParameters;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public Integer getState() {
        return state;
    }

    public String getPreviousSearch() {
        return previousSearch;
    }

    public List<ErrorCorporate> getErrors() {
        return errors;
    }

    public Bag getStats() {
        return stats;
    }
}
