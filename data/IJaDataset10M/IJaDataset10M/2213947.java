package org.apache.rat.pd.report;

import java.util.ArrayList;
import java.util.List;
import org.apache.rat.pd.engines.SearchResult;

/**
 * Represents one suspected code part which man must check manually
 * 
 * @author maka
 */
public class ReportEntry {

    private String fileName;

    private final String code;

    List<SearchResult> searchResults = new ArrayList<SearchResult>();

    /**
     * @param searchResults list of search results
     * @param code code for query
     * @param fileName file name where code is found
     */
    public ReportEntry(final List<SearchResult> searchResults, final String code, String fileName) {
        this.searchResults.addAll(searchResults);
        this.code = code;
        this.fileName = fileName;
    }

    /**
     * @return list of search results
     */
    public List<SearchResult> getSearchResult() {
        return searchResults;
    }

    /**
     * @return code for query
     */
    public String getCode() {
        return code;
    }

    /**
     * @return source file name
     */
    public String getFileName() {
        return fileName;
    }
}
