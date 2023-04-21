package com.memoire.yapod;

import java.util.Vector;

public class YapodInQuery extends YapodAbstractTestQuery {

    private String field_;

    private Vector values_;

    public YapodInQuery(String _field, Vector _values, YapodQuery _previous) {
        super(_previous);
        if (_field == null) throw new IllegalArgumentException("_field is null");
        if (_values == null) throw new IllegalArgumentException("_values is null");
        field_ = _field;
        values_ = _values;
    }

    protected boolean test(Object o) {
        Object v = YapodLib.getValue(o, field_);
        if (v == FAKE) v = field_;
        if (v == null) v = "";
        return values_.contains(v);
    }

    public String toString() {
        return "in(\"" + field_ + "\"," + values_ + "," + getPrevious() + ")";
    }
}
