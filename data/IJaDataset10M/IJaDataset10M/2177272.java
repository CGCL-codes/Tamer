package de.mguennewig.pobjsearch;

import de.mguennewig.pobjects.metadata.FormEntry;
import de.mguennewig.pobjects.metadata.MapItem;
import de.mguennewig.pobjform.StringElement;

/** Collection of utility methods for the PObjSearch package.
 *
 * @author Michael G�nnewig
 */
final class SearchUtils extends Object {

    static final FormEntry[] FILTER_ENTRIES = { SearchForm.entryPage, SearchForm.entrySort, SearchForm.entryRestrict, SearchForm.entryFilter, SearchForm.entryMode, SearchForm.entryPattern, SearchForm.entrySetFilter, SearchForm.entryAddFilter };

    static final FormEntry[] PAGER_ENTRIES = { SearchForm.entryRows, SearchForm.entryJumpToPage, SearchForm.entryGo };

    static final int SORT_DESCENDING = -1;

    static final int SORT_UNSORTED = 0;

    static final int SORT_ASCENDING = +1;

    static final MapItem[] EMPTY_FILTER = { new MapItem(null, "---") };

    private SearchUtils() {
        super();
    }

    /**
   * Returns the sort column entry for the given column index in the search
   * model or <code>null</code> if it does not exist in the form.
   */
    static SearchForm.Column getSortColumn(final SearchForm form, final int columnIndex) {
        for (final SearchForm.Column column : form.getSort().getColumn()) {
            final Integer nr = column.getNr();
            if (nr != null && nr.intValue() == columnIndex) return column;
        }
        return null;
    }

    /** Returns the sorting status for the given column index.
   *
   * @see #SORT_ASCENDING
   * @see #SORT_DESCENDING
   * @see #SORT_UNSORTED
   */
    static int getSortingStatus(final SearchForm form, final int columnIndex) {
        final SearchForm.Column column = getSortColumn(form, columnIndex);
        if (column != null) {
            if (SearchForm.DIRECTION_ASCENDING.equals(column.getDirection())) return SORT_ASCENDING;
            if (SearchForm.DIRECTION_DESCENDING.equals(column.getDirection())) return SORT_DESCENDING;
        }
        return SORT_UNSORTED;
    }

    /** Sets the sorting status for the given column index.
   *
   * <p>If the column is already sorted, then only the direction will be
   * changed.  New columns will be appended to the list of sorted columns.</p>
   *
   * @return the position within the sort array.
   * @see #getSortingStatus(SearchForm, int)
   */
    static int setSortingStatus(final SearchForm form, final int columnIndex, final int status) {
        final SearchForm.Sort sort = form.getSort();
        for (int i = 0; i < sort.getNumElements(); i++) {
            final SearchForm.Column column = sort.getColumn(i);
            final Integer nr = column.getNr();
            if (nr != null && nr.intValue() == columnIndex) {
                switch(status) {
                    case SORT_ASCENDING:
                        column.setDirection(SearchForm.DIRECTION_ASCENDING);
                        break;
                    case SORT_DESCENDING:
                        column.setDirection(SearchForm.DIRECTION_DESCENDING);
                        break;
                    case SORT_UNSORTED:
                        sort.removeElement(i);
                        break;
                    default:
                        throw new UnsupportedOperationException("Unhandled status " + status);
                }
                return i;
            }
        }
        if (status != SORT_UNSORTED) {
            final SearchForm.Column column = sort.addColumn();
            column.setNr(Integer.valueOf(columnIndex));
            if (status == SORT_ASCENDING) column.setDirection(SearchForm.DIRECTION_ASCENDING); else column.setDirection(SearchForm.DIRECTION_DESCENDING);
            return sort.getNumElements() - 1;
        }
        return -1;
    }

    /** Initializes/restricts the form elements.
   *
   * @see HtmlSearchForm#HtmlSearchForm(de.mguennewig.pobjects.Container)
   * @see SwingSearchForm#SwingSearchForm(de.mguennewig.pobjects.Container)
   */
    static void initialize(final SearchForm form) {
        ((StringElement) form.getElement(SearchForm.entryFilter)).setRestrictTo(EMPTY_FILTER);
        ((StringElement) form.getElement(SearchForm.entryPattern)).setSize(16);
    }

    /** @see SearchForm#initializeForListAll(SearchModel) */
    static void initializeForListAll(final SearchForm form, final SearchModel searchModel) {
        form.getSort().clear();
        form.getRestrict().clear();
        form.setPattern(null);
        form.setMode(SearchForm.MODE_LIST_ALL);
        form.setPage(Integer.valueOf(1));
        if (searchModel != null && searchModel.getColumnCount() > 0) {
            final MapItem[] items = new MapItem[searchModel.getFilterCount()];
            for (int i = 0; i < searchModel.getFilterCount(); i++) {
                final SearchFilter filter = searchModel.getFilter(i);
                items[i] = new MapItem(filter.getIdentifier(), filter.getLabel());
            }
            ((StringElement) form.getElement(SearchForm.entryFilter)).setRestrictTo(items);
            form.setFilter(searchModel.getColumn(0).getIdentifier());
            for (final SearchColumn column : searchModel.getDefaultSortColumns()) {
                setSortingStatus(form, searchModel.getColumnIndex(column), SORT_ASCENDING);
            }
        } else form.setFilter(null);
        form.setRows(SearchForm.ROWS_25);
    }
}
