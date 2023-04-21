package com.shudes.container;

import java.util.*;

public class DataRow extends Container<DataRow.DataRowId, String> {

    protected DataSet dataSet;

    DataRow(DataSet ds) {
        super(new DataRow.DataRowId(ds.nextRowId()));
        this.dataSet = ds;
    }

    protected DataRow(DataSet ds, DataRow.DataRowId id) {
        super(id);
        this.dataSet = ds;
    }

    public Integer getId() {
        return getParent().getId();
    }

    @Override
    public DataRow newContainer() {
        return new DataRow(this.dataSet, this.getParent());
    }

    @Override
    protected Collection<String> newChildrenCollection() {
        ArrayList<String> a;
        a = new ArrayList<String>(dataSet.columns.size());
        for (int i = 0; i < dataSet.columns.size(); i++) {
            this.addChild(null);
        }
        return a;
    }

    static class DataRowId {

        protected Integer id;

        DataRowId(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return id;
        }

        public int hashCode() {
            return id.hashCode();
        }

        public String toString() {
            return id.toString();
        }

        public boolean equals(Object o) {
            return ((o instanceof DataRowId) && (o != null) && (this.hashCode() == o.hashCode()));
        }
    }
}
