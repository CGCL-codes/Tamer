package prisms.osql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Represents a filter for SQL statements, determining which rows in a table structure are affected
 * or returned
 */
public interface WhereExpression {

    /** @return Whether this expression can be printed to SQL instead of preparing a statement */
    boolean isStringable();

    /**
	 * Writes this expression to SQL
	 * 
	 * @param ret The string builder to write the SQL to
	 */
    void toSQL(StringBuilder ret);

    /**
	 * Fills a prepared statement with the data in this expression
	 * 
	 * @param stmt The prepared statement to set the data in
	 * @param paramIdx The starting index to insert this expression's data into
	 * @return The next prepared statement index that should be used after this call finishes
	 * @throws PrismsSqlException If an error occurs setting the data
	 */
    int fillPrepared(java.sql.PreparedStatement stmt, int paramIdx) throws PrismsSqlException;

    /** Utilities going along with WhereExpressions */
    public static final class Util {

        private Util() {
        }

        /**
		 * Combines expressions logically with AND
		 * 
		 * @param exprs The expressions to combine
		 * @return The combined boolean expression
		 */
        public static BooleanExpression and(WhereExpression... exprs) {
            return new BooleanExpression(true, exprs);
        }

        /**
		 * Combines expressions logically with OR
		 * 
		 * @param exprs The expressions to combine
		 * @return The combined boolean expression
		 */
        public static BooleanExpression or(WhereExpression... exprs) {
            return new BooleanExpression(false, exprs);
        }

        /**
		 * Quick creator for a value comparison that compares a column with a value for equality
		 * 
		 * @param <T> The type of the column
		 * @param column The column to compare the values of
		 * @param value The value to compare against
		 * @return The value comparison
		 */
        public static <T> ValueComparison<T> valEqual(Column<T> column, T value) {
            try {
                return valCompare(column, CompareOperator.EQUAL, value);
            } catch (PrismsSqlException e) {
                throw new IllegalStateException("Should not throw exception for equal operator", e);
            }
        }

        /**
		 * Quick creator for a value comparison that compares a column with a value
		 * 
		 * @param <T> The type of the column
		 * @param column The column to compare the values of
		 * @param op The operator to use for comparison
		 * @param value The value to compare against
		 * @return The value comparison
		 * @throws PrismsSqlException If the operator is not compatible with the column type
		 */
        public static <T> ValueComparison<T> valCompare(Column<T> column, CompareOperator op, T value) throws PrismsSqlException {
            return new ValueComparison<T>(column, op, value);
        }
    }

    /** Represents a logical combination of other clauses (AND or OR) */
    public static class BooleanExpression implements WhereExpression {

        private final boolean isAnd;

        private WhereExpression[] theChildren;

        /**
		 * Creates a new boolean expression
		 * 
		 * @param and Whether the expressions should be AND'ed or OR'ed
		 * @param children The expressions to combine logically
		 */
        public BooleanExpression(boolean and, WhereExpression... children) {
            isAnd = and;
            theChildren = children;
        }

        /** @return Whether this expression is AND-type or OR-type */
        public boolean isAnd() {
            return isAnd;
        }

        /** @return The expressions combined logically in this expression */
        public WhereExpression[] getChildren() {
            return theChildren;
        }

        /**
		 * Simplifies this expression
		 * 
		 * @return The simplified expression
		 */
        public WhereExpression simplify() {
            if (theChildren.length == 0) return null; else if (theChildren.length == 1) return theChildren[0];
            for (int c = 0; c < theChildren.length; c++) {
                if (theChildren[c] == null) {
                    theChildren = prisms.util.ArrayUtils.remove(theChildren, c);
                    c--;
                } else if (theChildren[c] instanceof BooleanExpression) {
                    theChildren[c] = ((BooleanExpression) theChildren[c]).simplify();
                    if (theChildren[c] == null) {
                        theChildren = prisms.util.ArrayUtils.remove(theChildren, c);
                        c--;
                    } else if (theChildren[c] instanceof BooleanExpression && ((BooleanExpression) theChildren[c]).isAnd == isAnd) {
                        BooleanExpression b = (BooleanExpression) theChildren[c];
                        theChildren = prisms.util.ArrayUtils.remove(theChildren, c);
                        c--;
                        theChildren = prisms.util.ArrayUtils.addAll(theChildren, b.theChildren);
                    }
                }
            }
            return this;
        }

        public boolean isStringable() {
            for (WhereExpression exp : theChildren) if (!exp.isStringable()) return false;
            return true;
        }

        public void toSQL(StringBuilder ret) {
            toSQL(ret, false);
        }

        private void toSQL(StringBuilder ret, boolean prepare) {
            ret.append('(');
            for (int c = 0; c < theChildren.length; c++) {
                if (c > 0) ret.append(isAnd ? " AND " : " OR ");
                theChildren[c].toSQL(ret);
            }
            ret.append(')');
        }

        public int fillPrepared(PreparedStatement stmt, int paramIdx) throws PrismsSqlException {
            for (WhereExpression child : theChildren) paramIdx = child.fillPrepared(stmt, paramIdx);
            return paramIdx;
        }
    }

    /**
	 * Tests a column against a value or set of values
	 * 
	 * @param <T> The type of the column to test against
	 */
    public abstract static class ValueTest<T> implements WhereExpression {

        private final Column<T> theColumn;

        ValueTest(Column<T> column) {
            theColumn = column;
        }

        /** @return The column to test against */
        public Column<T> getColumn() {
            return theColumn;
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            toSQL(ret);
            return ret.toString();
        }
    }

    /** Operators by which a column value can be compared with another value */
    public static enum CompareOperator {

        /** Compares two values for equality */
        EQUAL("="), /** Compares two values for inequality */
        NOT_EQUAL("<>"), /** Compares two values for the first being greater than the second */
        GREATER(">"), /** Compares two values for the first being less than the second */
        LESS("<"), /** Compares two values for the first being greater than or equal to the second */
        GREATER_EQUAL(">="), /** Compares two values for the first being less than or equal to the second */
        LESS_EQUAL("<="), /** Compares a string with a pattern */
        LIKE("LIKE");

        private final String theSqlName;

        CompareOperator(String sql) {
            theSqlName = sql;
        }

        @Override
        public String toString() {
            return theSqlName;
        }
    }

    /**
	 * Tests a column against a single value
	 * 
	 * @param <T> The type of the column to test against
	 */
    public static class ValueComparison<T> extends ValueTest<T> {

        private final CompareOperator theOperator;

        private boolean isPlaceholder;

        private T theValue;

        /**
		 * Creates a value comparison
		 * 
		 * @param col The column to compare the values in
		 * @param op The operator to use to compare the values
		 * @param value The value to compare the column's data with
		 * @throws PrismsSqlException If the operator is incompatible with the column's value type
		 */
        public ValueComparison(Column<T> col, CompareOperator op, T value) throws PrismsSqlException {
            super(col);
            theOperator = op;
            switch(op) {
                case EQUAL:
                case NOT_EQUAL:
                    break;
                case LIKE:
                    if (!CharSequence.class.isAssignableFrom(col.getDataType().getJavaType())) throw new PrismsSqlException("Operator " + op + " may only be used with a character sequence type");
                    break;
                default:
                    if (value instanceof String) throw new PrismsSqlException("Operator " + op + " not valid with string data type");
            }
            theValue = value;
        }

        /**
		 * Sets this comparison's value as a placeholder or not
		 * 
		 * @param placeholder Whether this comparison's value should be a placeholder
		 */
        public void setPlaceholder(boolean placeholder) {
            isPlaceholder = placeholder;
        }

        /** @param value The value to test against */
        public void setValue(T value) {
            switch(theOperator) {
                default:
                    break;
            }
            theValue = value;
        }

        public boolean isStringable() {
            return !isPlaceholder && getColumn().getDataType().isStringable(theValue, getColumn());
        }

        public void toSQL(StringBuilder ret) {
            getColumn().toSQL(ret);
            if (theValue == null && !isPlaceholder) switch(theOperator) {
                case EQUAL:
                    ret.append(" IS NULL");
                    break;
                case NOT_EQUAL:
                    ret.append(" IS NOT NULL");
                    break;
                default:
                    throw new IllegalStateException("Operator " + theOperator + " not valid with null value");
            } else {
                ret.append(theOperator.toString());
                if (isPlaceholder) ret.append('?'); else getColumn().getDataType().toSQL(theValue, ret, getColumn());
            }
        }

        public int fillPrepared(java.sql.PreparedStatement stmt, int paramIdx) throws PrismsSqlException {
            try {
                switch(theOperator) {
                    case LIKE:
                        stmt.setString(paramIdx++, (String) theValue);
                        break;
                    default:
                        getColumn().getDataType().setParam(theValue, stmt, paramIdx, getColumn());
                        break;
                }
            } catch (SQLException e) {
                throw new PrismsSqlException("Could not fill prepared statement: " + this, e);
            }
            return paramIdx;
        }
    }

    /**
	 * Represents a clause using the "IN" keyword (e.g. value IN (value1, value2,...)
	 * 
	 * @param <T> The type of the column to test against
	 */
    public static class ValueContainment<T> extends ValueTest<T> {

        private final java.util.ArrayList<T> theValueSet;

        private final java.util.BitSet thePlaceholders;

        private boolean isNot;

        /**
		 * @param col The column to test against
		 * @param not Whether this containment is to exclude rather than include
		 * @param values The values to test against
		 */
        public ValueContainment(Column<T> col, boolean not, java.util.Collection<T> values) {
            super(col);
            isNot = not;
            theValueSet = new java.util.ArrayList<T>();
            thePlaceholders = new java.util.BitSet();
        }

        /** @return Whether this containment excludes rather than includes */
        public boolean isNot() {
            return isNot;
        }

        /** @param values The values to test against */
        public void addValues(java.util.Collection<T> values) {
            theValueSet.addAll(values);
        }

        /** @param values The values to test against */
        public void addValues(T... values) {
            for (T val : values) theValueSet.add(val);
        }

        /** @param value Another value to include in the filter */
        public void addValue(T value) {
            if (!theValueSet.contains(value)) theValueSet.add(value);
        }

        /** @return The number of values or placeholders are in this containment expression */
        public int getValueCount() {
            return theValueSet.size() > thePlaceholders.length() ? theValueSet.size() : thePlaceholders.length();
        }

        /**
		 * Gets one of the values stored in this containment expression
		 * 
		 * @param index The index to get the value at
		 * @return The value set for the given index
		 */
        public T getValue(int index) {
            if (index >= theValueSet.size()) return null;
            return theValueSet.get(index);
        }

        /** Adds a placeholder to the end of this value set */
        public void addPlaceholder() {
            thePlaceholders.set(theValueSet.size());
        }

        /**
		 * Sets an element as a placeholder or not
		 * 
		 * @param index The index to set
		 * @param ph Whether the given element should be a placeholder
		 */
        public void setPlaceholder(int index, boolean ph) {
            thePlaceholders.set(index, ph);
        }

        /**
		 * Checks an index to see if element at that index is a placeholder
		 * 
		 * @param index The index to check
		 * @return Whether the given element is a placeholder
		 */
        public boolean isPlaceholder(int index) {
            return thePlaceholders.get(index);
        }

        public boolean isStringable() {
            if (!thePlaceholders.isEmpty()) return false;
            boolean ret = true;
            for (T val : theValueSet) if (!getColumn().getDataType().isStringable(val, getColumn())) {
                ret = false;
                break;
            }
            return ret;
        }

        public void toSQL(StringBuilder ret) {
            getColumn().toSQL(ret);
            if (isNot) ret.append(" NOT");
            ret.append(" IN (");
            boolean first = true;
            for (T val : theValueSet) {
                if (!first) ret.append(", ");
                first = false;
                getColumn().getDataType().toSQL(val, ret, getColumn());
            }
            ret.append(')');
        }

        public int fillPrepared(PreparedStatement stmt, int paramIdx) throws PrismsSqlException {
            try {
                for (T value : theValueSet) getColumn().getDataType().setParam(value, stmt, paramIdx++, getColumn());
            } catch (SQLException e) {
                throw new PrismsSqlException("Could not fill prepared statement: " + this, e);
            }
            return paramIdx;
        }
    }
}
