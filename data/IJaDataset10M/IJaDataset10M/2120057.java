package com.koylu.caffein.query.target;

import java.sql.ResultSet;
import java.util.Map;
import com.koylu.caffein.model.caffein.CaffeinConfig;
import com.koylu.caffein.model.clazz.Clazz;

public class Column extends Target {

    private String alias;

    private String path;

    private com.koylu.caffein.model.clazz.Column column;

    private StringBuffer query = null;

    public Column() {
    }

    public Column(String alias, String path) {
        this.alias = alias;
        this.path = path;
    }

    public Column(com.koylu.caffein.model.clazz.Column column) {
        this.column = column;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public StringBuffer toQuery(CaffeinConfig caffeinConfig, Map<String, Clazz> aliasMap) throws Exception {
        if (query == null) {
            if (column == null) {
                if (aliasMap.containsKey(alias)) {
                    column = aliasMap.get(alias).pathToColumn(caffeinConfig, path);
                    if (column == null) {
                        throw new RuntimeException("invalid clazz path:" + path);
                    }
                } else {
                    throw new RuntimeException("invalid clazz alias:" + alias);
                }
            }
            query = new StringBuffer(alias).append(".").append(column.getColumn()).append(" ").append(column.getColumnAlias());
        }
        return query;
    }

    public Object getCurrentRow(CaffeinConfig caffeinConfig, Map<String, Clazz> aliasMap, ResultSet resultSet) throws Exception {
        return resultSet.getObject(column.getColumnAlias());
    }
}
