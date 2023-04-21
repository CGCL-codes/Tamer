package net.obsearch.filter;

import java.util.LinkedList;
import java.util.List;
import net.obsearch.OB;

/**
 * FilterAnd returns true if and only if each if its
 * filters return true.
 * 
 * @author Arnoldo Jose Muller Molina
 */
public final class FilterAnd<O extends OB> implements Filter<O> {

    private List<Filter<O>> andList;

    public FilterAnd() {
        andList = new LinkedList<Filter<O>>();
    }

    public boolean accept(O dbObject, O query) {
        for (Filter<O> f : andList) {
            if (!f.accept(dbObject, query)) {
                return false;
            }
        }
        return true;
    }

    public void addFilter(Filter<O> f) {
        andList.add(f);
    }
}
