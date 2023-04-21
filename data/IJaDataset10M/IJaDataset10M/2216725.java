package org.qsardb.conversion.table;

import java.util.*;

public class Row {

    private String id = null;

    private Map<Column, Cell> values = null;

    private Set<String> labels = null;

    public Row(String id, Map<Column, Cell> values) {
        this(id, values, Collections.<String>emptySet());
    }

    public Row(String id, Map<Column, Cell> values, Set<String> labels) {
        setId(id);
        setValues(values);
        setLabels(labels);
    }

    public String getId() {
        return this.id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public Map<Column, Cell> getValues() {
        return this.values;
    }

    private void setValues(Map<Column, Cell> values) {
        this.values = values;
    }

    public Set<String> getLabels() {
        return this.labels;
    }

    private void setLabels(Set<String> labels) {
        this.labels = labels;
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Row) {
            Row that = (Row) object;
            return (this.getId()).equals(that.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        return getId();
    }
}
