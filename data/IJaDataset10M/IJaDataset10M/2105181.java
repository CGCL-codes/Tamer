package net.sf.archimede.jsf.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.apache.commons.beanutils.PropertyUtils;

public class SortableList {

    private String sortColumn;

    private boolean ascending = true;

    private List list;

    private DataModel dataModel = new ListDataModel();

    public SortableList(String defaultSortColumn, List list) {
        this.sortColumn = defaultSortColumn;
        this.list = list;
        dataModel.setWrappedData(list);
    }

    public SortableList(String defaultSortColumn) {
        this.sortColumn = defaultSortColumn;
    }

    public void setList(List list) {
        this.list = list;
        dataModel.setWrappedData(list);
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        System.out.println("setAscending() param: " + ascending);
        this.ascending = ascending;
    }

    public String getSortColumn() {
        System.out.println("getSortColumn() returns: " + sortColumn);
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        System.out.println("getSortColumn() returns: " + sortColumn);
        this.sortColumn = sortColumn;
    }

    public DataModel getDataModel() {
        sort(this.sortColumn);
        return dataModel;
    }

    private void sort(String column) {
        Comparator comparator = new Comparator() {

            public int compare(Object o1, Object o2) {
                Comparable property1;
                Comparable property2;
                try {
                    property1 = (Comparable) PropertyUtils.getProperty(o1, sortColumn);
                    property2 = (Comparable) PropertyUtils.getProperty(o2, sortColumn);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error while sorting columns. The expression for column: \"" + sortColumn + "\" is invalid.");
                }
                if (property1 == null && property2 == null) {
                    return 0;
                }
                if (property1 == null && property2 != null) {
                    return ascending ? 1 : -1;
                } else if (property2 == null && property1 != null) {
                    return ascending ? -1 : 1;
                }
                property1 = convert(property1);
                property2 = convert(property2);
                return ascending ? property1.compareTo(property2) : property2.compareTo(property1);
            }
        };
        Collections.sort(list, comparator);
    }

    private Comparable convert(Comparable arg) {
        if (arg instanceof String) {
            String string = (String) arg;
            return string.toLowerCase();
        }
        return arg;
    }
}
