package jaque.jpa;

import jaque.expressions.*;
import java.util.*;

final class DbExpressionType {

    private DbExpressionType() {
    }

    public static String toString(int expressionType) {
        switch(expressionType) {
            case ExpressionType.Add:
                return "+";
            case ExpressionType.LogicalAnd:
                return "AND";
            case ExpressionType.Convert:
                return "";
            case ExpressionType.Divide:
                return "/";
            case ExpressionType.Equal:
                return "=";
            case ExpressionType.GreaterThan:
                return ">";
            case ExpressionType.GreaterThanOrEqual:
                return ">=";
            case ExpressionType.LessThan:
                return "<";
            case ExpressionType.LessThanOrEqual:
                return "<=";
            case ExpressionType.Negate:
                return "-";
            case ExpressionType.LogicalNot:
                return "~";
            case ExpressionType.Multiply:
                return "*";
            case ExpressionType.NotEqual:
                return "<>";
            case ExpressionType.LogicalOr:
                return "OR";
            case ExpressionType.Subtract:
                return "-";
            default:
                throw new IllegalArgumentException("Unsupported expression type:" + ExpressionType.toString(expressionType));
        }
    }

    public static final int Select = ExpressionType.MaxExpressionTypeValue + 1;

    public static final int Join = Select + 1;

    public static final int Aggregate = Join + 1;

    public static final int Scalar = Aggregate + 1;

    public static final int Exists = Scalar + 1;

    public static final int In = Exists + 1;

    public static final int Grouping = In + 1;

    public static final int AggregateSubquery = Grouping + 1;

    public static final int Between = AggregateSubquery + 1;

    public static final int RowCount = Between + 1;

    public static final int NamedValue = RowCount + 1;
}

final class OrderExpression {

    private final boolean _isAscending;

    private final Expression _expression;

    OrderExpression(boolean isAscending, Expression expression) {
        _isAscending = isAscending;
        _expression = expression;
    }

    boolean isAscending() {
        return _isAscending;
    }

    Expression getExpression() {
        return _expression;
    }
}

final class SelectExpression extends Expression {

    private final Expression _source;

    private final Expression _selector;

    private final boolean _isDistinct;

    private final Expression _where;

    private final List<OrderExpression> _orderBy;

    private final List<Expression> _groupBy;

    SelectExpression(Expression from, Expression selector, Expression where, List<OrderExpression> orderBy, List<Expression> groupBy, boolean isDistinct) {
        super(DbExpressionType.Select, selector != null ? selector.getResultType() : where.getResultType());
        _source = from;
        _selector = selector;
        _isDistinct = isDistinct;
        _where = where;
        _orderBy = orderBy;
        _groupBy = groupBy;
    }

    SelectExpression(Expression from, Expression selector, Expression where, List<OrderExpression> orderBy, List<Expression> groupBy) {
        this(from, selector, where, orderBy, groupBy, false);
    }

    SelectExpression(Expression from, Expression selector, Expression where) {
        this(from, selector, where, null, null);
    }

    @Override
    protected <T> T visit(ExpressionVisitor<T> v) {
        return ((DbExpressionVisitor<T>) v).visit(this);
    }

    Expression getFrom() {
        return _source;
    }

    Expression getSelector() {
        return _selector;
    }

    Expression getWhere() {
        return _where;
    }

    List<OrderExpression> getOrderBy() {
        return _orderBy;
    }

    List<Expression> getGroupBy() {
        return _groupBy;
    }

    boolean isDistinct() {
        return _isDistinct;
    }

    @Override
    public String toString() {
        QueryFormatter f = new QueryFormatter();
        apply(f);
        return f.toString();
    }
}

enum JoinType {

    CrossJoin, InnerJoin, LeftOuter
}

final class JoinExpression extends Expression {

    private final JoinType _joinType;

    private final Expression _left;

    private final Expression _right;

    private final Expression _condition;

    JoinExpression(Class<?> type, JoinType joinType, Expression left, Expression right, Expression condition) {
        super(DbExpressionType.Join, type);
        _joinType = joinType;
        _left = left;
        _right = right;
        _condition = condition;
    }

    JoinType getJoin() {
        return _joinType;
    }

    Expression getLeft() {
        return _left;
    }

    Expression getRight() {
        return _right;
    }

    Expression getCondition() {
        return _condition;
    }

    @Override
    protected <T> T visit(ExpressionVisitor<T> v) {
        return ((DbExpressionVisitor<T>) v).visit(this);
    }
}

abstract class SubqueryExpression extends Expression {

    private final SelectExpression _select;

    protected SubqueryExpression(int eType, Class<?> type, SelectExpression select) {
        super(eType, type);
        assert (eType == DbExpressionType.Scalar || eType == DbExpressionType.Exists || eType == DbExpressionType.In);
        _select = select;
    }

    SelectExpression getSelect() {
        return _select;
    }
}

final class ScalarExpression extends SubqueryExpression {

    ScalarExpression(Class<?> type, SelectExpression select) {
        super(DbExpressionType.Scalar, type, select);
    }

    @Override
    protected <T> T visit(ExpressionVisitor<T> v) {
        return ((DbExpressionVisitor<T>) v).visit(this);
    }
}

final class ExistsExpression extends SubqueryExpression {

    ExistsExpression(SelectExpression select) {
        super(DbExpressionType.Exists, Boolean.TYPE, select);
    }

    @Override
    protected <T> T visit(ExpressionVisitor<T> v) {
        return ((DbExpressionVisitor<T>) v).visit(this);
    }
}

final class InExpression extends SubqueryExpression {

    private final Expression _expression;

    private final List<Expression> _values;

    InExpression(Expression expression, SelectExpression select) {
        this(select, expression, null);
    }

    InExpression(Expression expression, List<Expression> values) {
        this(null, expression, values);
    }

    private InExpression(SelectExpression select, Expression expression, List<Expression> values) {
        super(DbExpressionType.In, Boolean.TYPE, select);
        _expression = expression;
        _values = values;
    }

    Expression getExpression() {
        return _expression;
    }

    List<Expression> getValues() {
        return _values;
    }

    @Override
    protected <T> T visit(ExpressionVisitor<T> v) {
        return ((DbExpressionVisitor<T>) v).visit(this);
    }
}

enum AggregateType {

    Count {

        @Override
        public String toString() {
            return "COUNT";
        }
    }
    , Min {

        @Override
        public String toString() {
            return "MIN";
        }
    }
    , Max {

        @Override
        public String toString() {
            return "MAX";
        }
    }
    , Sum {

        @Override
        public String toString() {
            return "SUM";
        }
    }
    , Average {

        @Override
        public String toString() {
            return "AVG";
        }
    }

}

final class AggregateExpression extends Expression {

    private final AggregateType _aggType;

    private final Expression _argument;

    private final boolean _isDistinct;

    AggregateExpression(Class<?> type, AggregateType aggType, Expression argument, boolean isDistinct) {
        super(DbExpressionType.Aggregate, type);
        _aggType = aggType;
        _argument = argument;
        _isDistinct = isDistinct;
    }

    AggregateType getAggregateType() {
        return _aggType;
    }

    Expression getArgument() {
        return _argument;
    }

    boolean isDistinct() {
        return _isDistinct;
    }

    @Override
    protected <T> T visit(ExpressionVisitor<T> v) {
        return ((DbExpressionVisitor<T>) v).visit(this);
    }
}

final class AggregateSubqueryExpression extends Expression {

    private final String _groupByAlias;

    private final Expression _aggregateInGroupSelect;

    private final ScalarExpression _aggregateAsSubquery;

    AggregateSubqueryExpression(String groupByAlias, Expression aggregateInGroupSelect, ScalarExpression aggregateAsSubquery) {
        super(DbExpressionType.AggregateSubquery, aggregateAsSubquery.getResultType());
        _aggregateInGroupSelect = aggregateInGroupSelect;
        _groupByAlias = groupByAlias;
        _aggregateAsSubquery = aggregateAsSubquery;
    }

    String getGroupByAlias() {
        return _groupByAlias;
    }

    Expression getAggregateInGroupSelect() {
        return _aggregateInGroupSelect;
    }

    ScalarExpression getAggregateAsSubquery() {
        return _aggregateAsSubquery;
    }

    @Override
    protected <T> T visit(ExpressionVisitor<T> v) {
        return ((DbExpressionVisitor<T>) v).visit(this);
    }
}

final class BetweenExpression extends Expression {

    private final Expression _expression;

    private final Expression _lower;

    private final Expression _upper;

    BetweenExpression(Expression expression, Expression lower, Expression upper) {
        super(DbExpressionType.Between, expression.getResultType());
        _expression = expression;
        _lower = lower;
        _upper = upper;
    }

    Expression getExpression() {
        return _expression;
    }

    Expression getLower() {
        return _lower;
    }

    Expression getUpper() {
        return _upper;
    }

    @Override
    protected <T> T visit(ExpressionVisitor<T> v) {
        return ((DbExpressionVisitor<T>) v).visit(this);
    }
}

final class RowNumberExpression extends Expression {

    private final List<OrderExpression> _orderBy;

    RowNumberExpression(List<OrderExpression> orderBy) {
        super(DbExpressionType.RowCount, Integer.TYPE);
        _orderBy = orderBy;
    }

    List<OrderExpression> getOrderBy() {
        return _orderBy;
    }

    @Override
    protected <T> T visit(ExpressionVisitor<T> v) {
        return ((DbExpressionVisitor<T>) v).visit(this);
    }
}

final class NamedValueExpression extends Expression {

    private final String _name;

    private final Expression _value;

    NamedValueExpression(String name, Expression value) {
        super(DbExpressionType.NamedValue, value.getResultType());
        _name = name;
        _value = value;
    }

    String getName() {
        return _name;
    }

    Expression getValue() {
        return _value;
    }

    @Override
    protected <T> T visit(ExpressionVisitor<T> v) {
        if (v instanceof DbExpressionVisitor) return ((DbExpressionVisitor<T>) v).visit(this);
        return (T) this;
    }
}

interface DbExpressionVisitor<T> extends ExpressionVisitor<T> {

    T visit(SelectExpression e);

    T visit(JoinExpression e);

    T visit(ScalarExpression e);

    T visit(ExistsExpression e);

    T visit(InExpression e);

    T visit(AggregateExpression e);

    T visit(AggregateSubqueryExpression e);

    T visit(BetweenExpression e);

    T visit(RowNumberExpression e);

    T visit(NamedValueExpression e);
}
