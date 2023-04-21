package com.openbravo.data.model;

import com.openbravo.data.loader.Datas;
import com.openbravo.format.Formats;

/**
 *
 * @author adrian
 */
public class Field {

    private String label;

    private Datas data;

    private Formats format;

    private boolean searchable;

    private boolean comparable;

    private boolean title;

    public Field(String label, Datas data, Formats format, boolean title, boolean searchable, boolean comparable) {
        this.label = label;
        this.data = data;
        this.format = format;
        this.title = title;
        this.searchable = searchable;
        this.comparable = comparable;
    }

    public Field(String label, Datas data, Formats format) {
        this(label, data, format, false, false, false);
    }

    public String getLabel() {
        return label;
    }

    public Formats getFormat() {
        return format;
    }

    public Datas getData() {
        return data;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public boolean isComparable() {
        return comparable;
    }

    public boolean isTitle() {
        return title;
    }
}
