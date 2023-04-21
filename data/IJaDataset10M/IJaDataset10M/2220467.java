package org.rubypeople.rdt.internal.ui.search;

import org.eclipse.jface.viewers.LabelProvider;
import org.rubypeople.rdt.internal.core.symbols.SearchResult;

public class RubySearchLabelProvider extends LabelProvider {

    public String getText(Object element) {
        if (element instanceof SearchResult) {
            SearchResult searchResult = (SearchResult) element;
            return searchResult.getSymbol().toString();
        }
        if (element instanceof Scope) {
            Scope scope = (Scope) element;
            return scope.getName();
        }
        return super.getText(element);
    }
}
