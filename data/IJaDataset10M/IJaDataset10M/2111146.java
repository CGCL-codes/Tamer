package org.jdu.dao.config;

import java.io.Serializable;
import java.util.List;

public class Table implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4971907913911950439L;

    private String name;

    private String className = null;

    private List columns;

    public void addColumns(Column column) {
        columns.add(column);
    }

    public List getColumns() {
        return columns;
    }

    public void setColumns(List columns) {
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
