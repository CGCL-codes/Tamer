package org.storrow.jdbc4d.parser;

import java.util.List;
import java.util.ArrayList;

public class Insert implements ParserStatement {

    public Insert(String s) {
        table_ = new String(s);
    }

    public void addColumns(List vector) {
        columns_ = vector;
    }

    public void addValueSpec(Exp zexp) {
        valueSpec_ = zexp;
    }

    public List getColumns() {
        return columns_;
    }

    public String getTable() {
        return table_;
    }

    public List getValues() {
        if (!(valueSpec_ instanceof Expression)) return null; else return ((Expression) valueSpec_).getOperands();
    }

    public Query getQuery() {
        if (!(valueSpec_ instanceof Query)) return null; else return (Query) valueSpec_;
    }

    public String toString() {
        StringBuffer stringbuffer = new StringBuffer("insert into " + table_);
        if (columns_ != null && columns_.size() > 0) {
            stringbuffer.append("(" + columns_.get(0));
            for (int i = 1; i < columns_.size(); i++) stringbuffer.append("," + columns_.get(i));
            stringbuffer.append(")");
        }
        String s = valueSpec_.toString();
        stringbuffer.append(" ");
        if (getValues() != null) stringbuffer.append("values ");
        if (s.startsWith("(")) stringbuffer.append(s); else stringbuffer.append(" (" + s + ")");
        return stringbuffer.toString();
    }

    String table_;

    List columns_;

    Exp valueSpec_;
}
