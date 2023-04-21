package org.nomadpim.core.ui.viewers;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class InfixFilter extends ViewerFilter {

    private String infix;

    private String infixUpper;

    private final ISearchableValueAccessor valueAccessor;

    public InfixFilter(ISearchableValueAccessor valueAccessor) {
        super();
        this.valueAccessor = valueAccessor;
    }

    public String getInfix() {
        return this.infix;
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (infix == null) {
            return true;
        }
        String[] searchableValues = this.valueAccessor.getSearchableValues(element);
        for (String value : searchableValues) {
            if (value.toUpperCase().contains(infixUpper)) {
                return true;
            }
        }
        return false;
    }

    public void setInfix(String infix) {
        this.infix = infix;
        this.infixUpper = infix.toUpperCase();
    }
}
