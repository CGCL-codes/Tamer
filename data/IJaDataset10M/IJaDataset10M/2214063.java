package uk.ac.ebi.intact.binarysearch.webapp.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.search.Sort;
import org.apache.myfaces.trinidad.model.SortCriterion;
import org.apache.myfaces.trinidad.model.SortableModel;
import psidev.psi.mi.search.SearchResult;
import psidev.psi.mi.search.engine.SearchEngineException;
import psidev.psi.mi.tab.PsimiTabColumn;
import uk.ac.ebi.intact.psimitab.search.IntActSearchEngine;
import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO comment this!
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: SearchResultDataModel.java 10962 2008-01-31 00:26:34Z baranda $
 */
public class SearchResultDataModel extends SortableModel implements Serializable {

    private static final Log log = LogFactory.getLog(SearchResultDataModel.class);

    private static final String DEFAULT_SORT_COLUMN = PsimiTabColumn.ID_A.getSortableColumnName();

    private String searchQuery;

    private String indexDirectory;

    private SearchResult result;

    private int rowIndex = -1;

    private int firstResult = 0;

    private int pageSize;

    private String sortColumn = DEFAULT_SORT_COLUMN;

    private boolean ascending = true;

    private long elapsedTimeMillis = -1;

    private Map<String, Boolean> columnSorts;

    public SearchResultDataModel(String searchQuery, String indexDirectory, int pageSize) throws TooManyResults {
        this.searchQuery = searchQuery;
        this.indexDirectory = indexDirectory;
        this.pageSize = pageSize;
        columnSorts = new HashMap<String, Boolean>(16);
        try {
            setRowIndex(0);
            fetchResults();
        } catch (SearchEngineException e) {
            throw new TooManyResults(e);
        }
        setWrappedData(result);
    }

    public void fetchResults() throws SearchEngineException {
        if (log.isDebugEnabled()) log.debug("Fetching results: " + searchQuery + " - First: " + firstResult + " - Sorting: " + sortColumn + " " + (ascending ? "ASC)" : "DESC)"));
        Sort sort = new Sort(sortColumn, !ascending);
        long startTime = System.currentTimeMillis();
        IntActSearchEngine engine;
        try {
            engine = new IntActSearchEngine(indexDirectory);
        } catch (IOException e) {
            throw new SearchEngineException(e);
        }
        this.result = engine.search(searchQuery, firstResult, pageSize, sort);
        elapsedTimeMillis = System.currentTimeMillis() - startTime;
    }

    public int getRowCount() {
        return result.getTotalCount();
    }

    public Object getRowData() {
        if (result == null) {
            return null;
        }
        if (!isRowWithinResultRange()) {
            firstResult = getRowIndex();
            fetchResults();
        }
        if (!isRowAvailable()) {
            throw new IllegalArgumentException("row is unavailable");
        }
        return result.getInteractions().get(rowIndex - result.getFirstResult());
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public Object getWrappedData() {
        return result;
    }

    public boolean isRowAvailable() {
        if (result == null) {
            return false;
        }
        return rowIndex >= 0 && rowIndex < result.getTotalCount();
    }

    protected boolean isRowWithinResultRange() {
        return (getRowIndex() >= firstResult) && (getRowIndex() < (firstResult + pageSize));
    }

    public void setRowIndex(int rowIndex) {
        if (rowIndex < -1) {
            throw new IllegalArgumentException("illegal rowIndex " + rowIndex);
        }
        int oldRowIndex = rowIndex;
        this.rowIndex = rowIndex;
        if (result != null && oldRowIndex != this.rowIndex) {
            Object data = isRowAvailable() ? getRowData() : null;
            DataModelEvent event = new DataModelEvent(this, this.rowIndex, data);
            DataModelListener[] listeners = getDataModelListeners();
            for (int i = 0; i < listeners.length; i++) {
                listeners[i].rowSelected(event);
            }
        }
    }

    @Override
    public void setSortCriteria(List<SortCriterion> criteria) {
        if ((criteria == null) || (criteria.isEmpty())) {
            this.sortColumn = DEFAULT_SORT_COLUMN;
            columnSorts.clear();
        } else {
            SortCriterion criterion = criteria.get(0);
            this.sortColumn = criterion.getProperty();
            if (columnSorts.containsKey(sortColumn)) {
                this.ascending = !columnSorts.get(sortColumn);
            } else {
                this.ascending = criterion.isAscending();
            }
            columnSorts.put(sortColumn, ascending);
            if (log.isDebugEnabled()) log.debug("\tSorting by '" + criterion.getProperty() + "' " + (criterion.isAscending() ? "ASC" : "DESC"));
        }
        fetchResults();
    }

    /**
     * Checks to see if the underlying collection is sortable by the given property.
     *
     * @param property The name of the property to sort the underlying collection by.
     * @return true, if the property implements java.lang.Comparable
     */
    @Override
    public boolean isSortable(String property) {
        return true;
    }

    public SearchResult getResult() {
        return result;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public long getElapsedTimeMillis() {
        return elapsedTimeMillis;
    }

    public double getElapsedTimeSecs() {
        return (double) elapsedTimeMillis / 1000;
    }

    private Object key;

    public Object getRowKey() {
        return key;
    }

    public void setRowKey(Object key) {
        this.key = key;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getIndexDirectory() {
        return indexDirectory;
    }
}
