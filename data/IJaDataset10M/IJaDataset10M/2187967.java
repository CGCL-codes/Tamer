package com.fairbait.sybase.model;

import java.util.*;
import com.fairbait.sybase.model.visitor.*;

public final class SqlType extends AbstractSqlItem {

    private String name;

    private List<SqlSimpleExpression> parameters;

    public SqlType(final String name) {
        setName(name);
        parameters = new ArrayList<SqlSimpleExpression>();
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<SqlSimpleExpression> getParametersReadonly() {
        return Collections.unmodifiableList(parameters);
    }

    public boolean addParameter(final SqlSimpleExpression parameter) {
        parameter.setParent(this);
        return parameters.add(parameter);
    }

    public boolean setSqlText(final String sqlText) {
        final SqlType i = parseType(sqlText);
        if (i == null) return false;
        return copyFrom(i);
    }

    public boolean copyFrom(final ISqlItem item) {
        if (!(item instanceof SqlType)) return false;
        final SqlType from = (SqlType) item;
        name = from.name;
        parameters = from.parameters;
        return true;
    }

    public static SqlType parseType(final String sqlText) {
        return new SqlItemParserVisitor(sqlText).parseType();
    }

    public void accept(final ISqlItemVisitor visitor) {
        visitor.visit(this);
    }

    public SqlItemGroupType getSqlItemGroupType() {
        return ISqlItem.SqlItemGroupType.SQL_UNCLASSIFIED;
    }

    public SqlItemType getSqlItemType() {
        return ISqlItem.SqlItemType.SqlType;
    }
}
