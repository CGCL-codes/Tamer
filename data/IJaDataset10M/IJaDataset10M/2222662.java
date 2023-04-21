package org.eoti.rei.cmp;

import org.eoti.io.xls.XLSColumn;
import org.eoti.io.xls.XLSReader;
import org.eoti.rei.NDFBrowser;

public class ValueHtLComparator extends ValueLtHComparator {

    protected NDFBrowser browser;

    protected XLSReader reader;

    protected XLSColumn oweCol, worthCol;

    public ValueHtLComparator(NDFBrowser browser) {
        super(browser);
    }

    public int compare(Integer row1, Integer row2) {
        return super.compare(row2, row1);
    }
}
