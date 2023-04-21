package org.maestroframework.db.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.maestroframework.db.ClassMapper;

public class OrderBy implements Expression {

    public enum DIR {

        ASC, DESC
    }

    private List<ExprDir> fieldDirs = new ArrayList<ExprDir>();

    public OrderBy(Expression expression) {
        this(expression, DIR.ASC);
    }

    public OrderBy(String field, OrderBy.DIR direction) {
        this(new Field(field), direction);
    }

    public OrderBy(Expression expression, OrderBy.DIR direction) {
        this.andThen(expression, direction);
    }

    public OrderBy andThen(String fieldName) {
        return this.andThen(new Field(fieldName), DIR.ASC);
    }

    public OrderBy andThen(Expression expression) {
        return this.andThen(expression, DIR.ASC);
    }

    public OrderBy andThen(Expression expression, OrderBy.DIR direction) {
        this.fieldDirs.add(new ExprDir(expression, direction));
        return this;
    }

    public OrderBy andThen(String fieldName, OrderBy.DIR direction) {
        return this.andThen(new Field(fieldName), direction);
    }

    @Override
    public String render(ClassMapper classMapper) {
        StringBuilder orderBy = new StringBuilder();
        orderBy.append(" ORDER BY ");
        Iterator<ExprDir> fieldDirIter = fieldDirs.iterator();
        while (fieldDirIter.hasNext()) {
            orderBy.append(fieldDirIter.next().render(classMapper));
            if (fieldDirIter.hasNext()) orderBy.append(", ");
        }
        return orderBy.toString();
    }

    @Override
    public List<Object> values() {
        List<Object> values = new ArrayList<Object>();
        for (ExprDir e : this.fieldDirs) {
            values.addAll(e.values());
        }
        return values;
    }

    private class ExprDir implements Expression {

        Expression expression;

        DIR dir;

        public ExprDir(Expression expression, DIR dir) {
            this.expression = expression;
            this.dir = dir;
        }

        @Override
        public String render(ClassMapper classMapper) {
            return expression.render(classMapper) + " " + dir.toString();
        }

        @Override
        public List<Object> values() {
            return expression.values();
        }
    }
}
