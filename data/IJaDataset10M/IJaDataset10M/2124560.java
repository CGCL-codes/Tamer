package ch.netcetera.wikisearch.lookup;

/**
 * A list that holds a large bunch of data and a filtered version of it. The
 * elements are sorted alphabetically. Optionally, the elements can be filtered
 * and duplicate entries can be hidden (folding).
 * 
 * @since 2.0
 */
public class FilteredList {

    private String[] fFoldedLabels;

    private ILabelProvider fLabelProvider = new DefaultLabelProvider();

    private boolean fMatchEmptyString = false;

    private boolean fIgnoreCase = true;

    private boolean fAllowDuplicates = false;

    private String fFilter = "";

    private TwoArrayQuickSorter fSorter = new TwoArrayQuickSorter(fIgnoreCase);

    String[] fElements = new String[0];

    String[] fLabels;

    int[] fFoldedIndices;

    int fFoldedCount;

    int[] fFilteredIndices;

    int fFilteredCount;

    private IFilterMatcher fFilterMatcher = new DefaultFilterMatcher();

    /**
	 * Constructs a default FilteredList that doesn't match 
	 * empty strings, ignores case, does not allow duplicates
	 * (i.e. does folding), uses a DefaultLabelProvider and
	 * DefaultFilterMatcher. Pretty much what you want ;-)
	 */
    public FilteredList() {
        fMatchEmptyString = false;
        fIgnoreCase = true;
        fAllowDuplicates = false;
        fLabelProvider = new DefaultLabelProvider();
        fFilterMatcher = new DefaultFilterMatcher();
    }

    /**
     * Constructs a new filtered list.
     * 
     * @param labelProvider
     *            the label renderer
     * @param ignoreCase
     *            specifies whether sorting and folding is case sensitive
     * @param allowDuplicates
     *            specifies whether folding of duplicates is desired
     * @param matchEmptyString
     *            specifies whether empty filter strings should filter
     *            everything or nothing
     */
    public FilteredList(ILabelProvider labelProvider, boolean ignoreCase, boolean allowDuplicates, boolean matchEmptyString) {
        fLabelProvider = labelProvider;
        fIgnoreCase = ignoreCase;
        fSorter = new TwoArrayQuickSorter(ignoreCase);
        fAllowDuplicates = allowDuplicates;
        fMatchEmptyString = matchEmptyString;
    }

    /**
     * Sets the list of elements.
     * 
     * @param elements
     *            the elements to be shown in the list.
     */
    public void setData(String[] elements) {
        if (elements == null) {
            fElements = new String[0];
        } else {
            fElements = new String[elements.length];
            System.arraycopy(elements, 0, fElements, 0, elements.length);
        }
        int length = fElements.length;
        fLabels = new String[length];
        for (int i = 0; i != length; i++) {
            String text = fLabelProvider.getText(fElements[i]);
            fLabels[i] = text;
        }
        fSorter.sort(fLabels, fElements);
        fFilteredIndices = new int[length];
        fFoldedIndices = new int[length];
        updateList();
    }

    /**
     * Tests if the list (before folding and filtering) is empty.
     * 
     * @return returns <code>true</code> if the list is empty,
     *         <code>false</code> otherwise.
     */
    public boolean isEmpty() {
        return (fElements == null) || (fElements.length == 0);
    }

    /**
     * Sets the filter matcher.
     * 
     * @param filterMatcher
     */
    public void setFilterMatcher(IFilterMatcher filterMatcher) {
        if (filterMatcher == null) {
            throw new IllegalArgumentException("FilterMatcher must not be null.");
        }
        fFilterMatcher = filterMatcher;
    }

    /**
     * Returns an array of the selected elements. The type of the elements
     * returned in the list are the same as the ones passed with
     * <code>setElements</code>. The array does not contain the rendered
     * strings.
     * 
     * @return returns the array of selected elements.
     */
    public String[] getSelection() {
        return fFoldedLabels;
    }

    /**
     * Sets the filter pattern. Current only prefix filter patterns are
     * supported.
     * 
     * @param filter
     *            the filter pattern.
     */
    public void setFilter(String filter) {
        fFilter = (filter == null) ? "" : filter;
        updateList();
    }

    private void updateList() {
        fFilteredCount = filter();
        fFoldedCount = fold();
        fFoldedLabels = new String[fFoldedCount];
        int currentIndex = 0;
        for (int i = 0; i < fFoldedCount; i++) {
            fFoldedLabels[i] = fLabels[fFilteredIndices[fFoldedIndices[i]]];
        }
    }

    public void runInUIThread() {
    }

    /**
     * Returns the filter pattern.
     * 
     * @return returns the filter pattern.
     */
    public String getFilter() {
        return fFilter;
    }

    /**
     * Returns all elements which are folded together to one entry in the list.
     * 
     * @param index
     *            the index selecting the entry in the list.
     * @return returns an array of elements folded together, <code>null</code>
     *         if index is out of range.
     */
    public String[] getFoldedElements(int index) {
        if ((index < 0) || (index >= fFoldedCount)) return null;
        int start = fFoldedIndices[index];
        int count = (index == fFoldedCount - 1) ? fFilteredCount - start : fFoldedIndices[index + 1] - start;
        String[] elements = new String[count];
        for (int i = 0; i != count; i++) elements[i] = fElements[fFilteredIndices[start + i]];
        return elements;
    }

    private int fold() {
        if (fAllowDuplicates) {
            for (int i = 0; i != fFilteredCount; i++) fFoldedIndices[i] = i;
            return fFilteredCount;
        } else {
            int k = 0;
            String last = null;
            for (int i = 0; i != fFilteredCount; i++) {
                int j = fFilteredIndices[i];
                String current = fLabels[j];
                if (!current.equals(last)) {
                    fFoldedIndices[k] = i;
                    k++;
                    last = current;
                }
            }
            return k;
        }
    }

    private int filter() {
        if (((fFilter == null) || (fFilter.length() == 0)) && !fMatchEmptyString) return 0;
        fFilterMatcher.setFilter(fFilter.trim(), fIgnoreCase, false);
        int k = 0;
        for (int i = 0; i != fElements.length; i++) {
            if (fFilterMatcher.match(fLabelProvider.getText(fElements[i]))) fFilteredIndices[k++] = i;
        }
        return k;
    }

    /**
     * Returns whether or not duplicates are allowed.
     * 
     * @return <code>true</code> indicates duplicates are allowed
     */
    public boolean getAllowDuplicates() {
        return fAllowDuplicates;
    }

    /**
     * Sets whether or not duplicates are allowed. If this value is set the
     * items should be set again for this value to take effect.
     * 
     * @param allowDuplicates
     *            <code>true</code> indicates duplicates are allowed
     */
    public void setAllowDuplicates(boolean allowDuplicates) {
        this.fAllowDuplicates = allowDuplicates;
    }

    /**
     * Returns whether or not case should be ignored.
     * 
     * @return <code>true</code> if case should be ignored
     */
    public boolean getIgnoreCase() {
        return fIgnoreCase;
    }

    /**
     * Sets whether or not case should be ignored If this value is set the items
     * should be set again for this value to take effect.
     * 
     * @param ignoreCase
     *            <code>true</code> if case should be ignored
     */
    public void setIgnoreCase(boolean ignoreCase) {
        this.fIgnoreCase = ignoreCase;
    }

    /**
     * Returns whether empty filter strings should filter everything or nothing.
     * 
     * @return <code>true</code> for the empty string to match all items,
     *         <code>false</code> to match none
     */
    public boolean getMatchEmptyString() {
        return fMatchEmptyString;
    }

    /**
     * Sets whether empty filter strings should filter everything or nothing. If
     * this value is set the items should be set again for this value to take
     * effect.
     * 
     * @param matchEmptyString
     *            <code>true</code> for the empty string to match all items,
     *            <code>false</code> to match none
     */
    public void setMatchEmptyString(boolean matchEmptyString) {
        this.fMatchEmptyString = matchEmptyString;
    }

    /**
     * Returns the label provider for the items.
     * 
     * @return the label provider
     */
    public ILabelProvider getLabelProvider() {
        return fLabelProvider;
    }

    /**
     * Sets the label provider. If this value is set the items should be set
     * again for this value to take effect.
     * 
     * @param labelProvider
     *            the label provider
     */
    public void setLabelProvider(ILabelProvider labelProvider) {
        this.fLabelProvider = labelProvider;
    }
}
