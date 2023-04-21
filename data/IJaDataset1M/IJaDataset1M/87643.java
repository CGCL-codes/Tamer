package net.sf.doolin.gui.ext.tabular.csv;

import org.apache.commons.lang.ObjectUtils;

/**
 * Just renders the value by calling the <code>toString()</code> method on it.
 * 
 * @author Damien Coraboeuf
 * @version $Id: PrimitiveCSVAdapter.java,v 1.1 2007/08/14 14:09:22 guinnessman Exp $
 */
public class PrimitiveCSVAdapter implements CSVAdapter {

    /**
	 * Just renders the value by calling the <code>toString()</code> method on
	 * it.
	 * 
	 * @see net.sf.doolin.gui.ext.tabular.csv.CSVAdapter#getCSVValue(java.lang.Object)
	 */
    public String getCSVValue(Object value) {
        return ObjectUtils.toString(value, "");
    }
}
