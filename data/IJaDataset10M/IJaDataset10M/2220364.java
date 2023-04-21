package net.sf.doolin.gui.ext.tabular.csv;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Just renders the value by calling the <code>toString()</code> method on it
 * and escapes special characters.
 * 
 * @author Damien Coraboeuf
 * @version $Id: StringCSVAdapter.java,v 1.1 2007/08/14 14:09:22 guinnessman Exp $
 */
public class StringCSVAdapter implements CSVAdapter {

    /**
	 * Just renders the value by calling the <code>toString()</code> method on
	 * it and escapes special characters.
	 * 
	 * @see net.sf.doolin.gui.ext.tabular.csv.CSVAdapter#getCSVValue(java.lang.Object)
	 */
    public String getCSVValue(Object value) {
        String string = ObjectUtils.toString(value, "");
        string = StringUtils.replace(string, "\"", "\"\"");
        return "\"" + value + "\"";
    }
}
