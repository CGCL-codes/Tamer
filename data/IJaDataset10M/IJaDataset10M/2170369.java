package com.cell.xls;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;

public class XLSFullRow extends XLSRow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
	 * String[0] = value <br>
	 * String[1] = desc <br>
	 */
    LinkedHashMap<String, String[]> full_columns = new LinkedHashMap<String, String[]>();

    public XLSFullRow(XLSFile file, String id, String desc) {
        super(file, id, desc);
    }

    public void put(String name, String value, String desc) {
        full_columns.put(name, new String[] { value, desc });
    }

    public Collection<String> getColumns() {
        return full_columns.keySet();
    }

    public String getValue(String column_name) {
        String[] c = full_columns.get(column_name);
        if (c != null && c.length == 2) {
            return c[0];
        }
        return null;
    }

    public String getDesc(String column_name) {
        String[] c = full_columns.get(column_name);
        if (c != null && c.length == 2) {
            return c[1];
        }
        return null;
    }
}
