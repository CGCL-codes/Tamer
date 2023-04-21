package net.sf.elbe.ui.editors.searchresult;

import java.util.ArrayList;
import net.sf.elbe.core.model.AttributeHierachie;
import net.sf.elbe.core.model.IAttribute;
import net.sf.elbe.core.model.ISearch;
import net.sf.elbe.core.model.ISearchResult;
import net.sf.elbe.core.model.IValue;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class SearchResultEditorFilter extends ViewerFilter {

    protected SearchResultEditorContentProvider contentProvider;

    protected String quickFilterValue;

    private boolean showDn;

    public SearchResultEditorFilter() {
        this.quickFilterValue = "";
    }

    public void connect(SearchResultEditorContentProvider contentProvider) {
        this.contentProvider = contentProvider;
    }

    public void inputChanged(ISearch newSearch, boolean showDn) {
        this.showDn = showDn;
    }

    public boolean isFiltered() {
        return this.quickFilterValue != null && !"".equals(quickFilterValue);
    }

    public Object[] filter(Viewer viewer, Object parent, Object[] elements) {
        if (isFiltered()) {
            int size = elements.length;
            ArrayList out = new ArrayList(size);
            for (int i = 0; i < size; ++i) {
                Object element = elements[i];
                if (select(viewer, parent, element)) out.add(element);
            }
            return out.toArray();
        } else {
            return elements;
        }
    }

    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (element instanceof ISearchResult) {
            ISearchResult searchResult = (ISearchResult) element;
            String[] returningAttributes = searchResult.getSearch().getReturningAttributes();
            for (int r = 0; r < returningAttributes.length; r++) {
                String ra = returningAttributes[r];
                AttributeHierachie ah = searchResult.getAttributeWithSubtypes(ra);
                if (ah != null) {
                    IAttribute[] attributes = ah.getAttributes();
                    for (int i = 0; i < attributes.length; i++) {
                        IValue[] values = attributes[i].getValues();
                        for (int k = 0; k < values.length; k++) {
                            if (this.goesThroughQuickFilter(values[k])) {
                                return true;
                            }
                        }
                    }
                }
            }
            if (this.showDn && searchResult.getDn().toString().toUpperCase().indexOf(this.quickFilterValue.toUpperCase()) > -1) {
                return true;
            }
            return false;
        } else {
            return true;
        }
    }

    private boolean goesThroughQuickFilter(IValue value) {
        if (value.isString() && value.getStringValue().toUpperCase().indexOf(this.quickFilterValue.toUpperCase()) == -1) {
            return false;
        } else if (value.isBinary()) {
            return false;
        }
        return true;
    }

    public void dispose() {
        this.contentProvider = null;
    }

    public String getQuickFilterValue() {
        return quickFilterValue;
    }

    public void setQuickFilterValue(String quickFilterValue) {
        if (!this.quickFilterValue.equals(quickFilterValue)) {
            this.quickFilterValue = quickFilterValue;
            if (this.contentProvider != null) this.contentProvider.refresh();
        }
    }
}
