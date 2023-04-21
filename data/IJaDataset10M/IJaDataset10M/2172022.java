package de.bioutils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Alexander Kerner
 * @deprecated
 *
 */
public class RowImpl implements Row {

    private final ArrayList<String> elements;

    private final String delim;

    public RowImpl(String delim, List<String> elements) {
        this.elements = new ArrayList<String>(elements);
        this.delim = delim;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (String s : elements) {
            sb.append(s);
            sb.append(getDelim());
        }
        return sb.toString();
    }

    public String getAtIndex(int i) {
        return elements.get(i);
    }

    public Iterator<String> iterator() {
        return elements.iterator();
    }

    public String getDelim() {
        return delim;
    }
}
