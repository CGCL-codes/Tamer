package com.koylu.caffein.query.group;

import java.util.Map;
import com.koylu.caffein.model.caffein.CaffeinConfig;
import com.koylu.caffein.model.clazz.Clazz;
import com.koylu.caffein.model.clazz.Column;

public class Group {

    private Column column;

    private String alias;

    private String path;

    private StringBuffer query = null;

    public Group() {
    }

    public Group(String alias, String path) {
        this.alias = alias;
        this.path = path;
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
            query = new StringBuffer(alias).append(".").append(column.getColumn());
        }
        return query;
    }

    public static Group by(String alias, String path) {
        return new Group(alias, path);
    }
}
