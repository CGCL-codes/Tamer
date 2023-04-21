package edu.washington.assist.report.event;

import java.util.LinkedList;
import java.util.List;
import edu.washington.assist.database.QueryModel;
import edu.washington.assist.report.ReportManager;

public class BasicSearchModel implements SearchModel {

    private final List<SearchListener> listeners = new LinkedList<SearchListener>();

    private ReportManager result = new ReportManager();

    private QueryModel query = null;

    public void addSearchListener(SearchListener rsl) {
        listeners.add(rsl);
    }

    public void removeSearchListener(SearchListener rsl) {
        listeners.remove(rsl);
    }

    public QueryModel getActiveQuery() {
        return query;
    }

    public ReportManager getActiveResult() {
        return result;
    }

    public void setSearchState(ReportManager rm, QueryModel q) {
        this.result = rm;
        this.query = q;
        fireStateChange(rm, q);
    }

    private void fireStateChange(ReportManager rm, QueryModel q) {
        for (SearchListener lst : listeners) {
            lst.searchUpdate(rm, q, this);
        }
    }
}
