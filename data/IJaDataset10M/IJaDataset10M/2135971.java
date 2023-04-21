package com.jlz.beans.core;

import com.julewa.db.Entity;

public class Dynamic<T> extends Entity<T> {

    private String _table_suffix_ = "asset";

    public void set_table_suffix(String suffix) {
        _table_suffix_ = suffix;
    }

    public String get_table_suffix_() {
        return _table_suffix_;
    }
}
