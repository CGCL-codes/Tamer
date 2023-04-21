package x.sql2.filter;

import x.sql2.SQLUtils;

public class SQLFilterEntryBetween implements ISQLFilterEntry {

    private String field;

    private Object from, to;

    public SQLFilterEntryBetween(String fieldname, Object from, Object to) {
        this.field = fieldname;
        this.from = from;
        this.to = to;
    }

    @Override
    public String toQuery() {
        return field + " BETWEEN " + SQLUtils.formatColumnValue(from) + " AND " + SQLUtils.formatColumnValue(to);
    }

    @Override
    public ISQLFilterEntry clone() {
        return new SQLFilterEntryBetween(field, from, to);
    }
}
