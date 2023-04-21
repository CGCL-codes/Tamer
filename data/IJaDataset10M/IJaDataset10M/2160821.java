package com.healthmarketscience.sqlbuilder;

import java.io.IOException;
import com.healthmarketscience.common.util.AppendableExt;
import java.util.ArrayList;

/**
 * An object representing a clause which generally expects to be
 * combined/nested with other clauses.  Implementations are expected to manage
 * to delimit themselves so that they do not interfere with other clauses at a
 * peer level.  Also, implementations which may contain a variable number of
 * expressions should override the {@link #isEmpty} appropriately.
 *
 * @author James Ahlborn
 */
abstract class NestableClause extends SqlObject {

    private boolean _disableParens;

    protected NestableClause() {
    }

    /**
   * Returns whether or not wrapping parentheses are disabled for this clause
   * (for clauses which utilize wrapping parentheses).  Defaults to {@code
   * false}.
   */
    public boolean isDisableParens() {
        return _disableParens;
    }

    /**
   * Controls whether or not this clause will wrap itself in parentheses (for
   * relevant clauses).
   * <p/>
   * Warning: you should generally <b>not</b> disable parentheses as this may
   * change the meaning of the SQL query.  However, sometimes non-standard SQL
   * queries may require direct control over the wrapping parentheses.
   */
    public NestableClause setDisableParens(boolean disableParens) {
        _disableParens = disableParens;
        return this;
    }

    /**
   * Returns <code>true</code> iff the output of this instance would be an
   * empty expression, <code>false</code> otherwise.
   * <p>
   * Default implementation returns {@code false}.
   */
    public boolean isEmpty() {
        return false;
    }

    /**
   * Returns {@code true} iff the output of this instance would include
   * surrounding parentheses, {@code false} otherwise.
   * <p>
   * Default implementation returns {@code !isEmpty() && !isDisableParens()}.
   */
    public boolean hasParens() {
        return !isEmpty() && !isDisableParens();
    }

    /**
   * Determines if any of the given clauses are non-empty.
   * @return {@code false} if at least one clause is non-empty, {@code true}
   *         otherwise
   */
    protected static boolean areEmpty(SqlObjectList<? extends NestableClause> nestedClauses) {
        for (NestableClause nc : nestedClauses) {
            if (!nc.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
   * Determines if any of the given clauses are non-empty.
   * @return {@code false} if at least one clause is non-empty, {@code true}
   *         otherwise
   */
    protected static boolean hasParens(SqlObjectList<? extends NestableClause> nestedClauses) {
        int nonEmptyClauses = 0;
        for (NestableClause nc : nestedClauses) {
            if (nc.hasParens()) {
                return true;
            }
            if (!nc.isEmpty()) {
                ++nonEmptyClauses;
            }
        }
        if (nonEmptyClauses > 1) {
            return true;
        }
        return false;
    }

    /**
   * Appends an open parenthesis to the given AppendableExt if disableParens is
   * {@code true}, otherwise does nothing.
   */
    protected void openParen(AppendableExt app) throws IOException {
        if (!isDisableParens()) {
            app.append("(");
        }
    }

    /**
   * Appends a close parenthesis to the given AppendableExt if disableParens is
   * {@code true}, otherwise does nothing.
   */
    protected void closeParen(AppendableExt app) throws IOException {
        if (!isDisableParens()) {
            app.append(")");
        }
    }

    /**
   * Appends the given custom clause to the given AppendableExt, handling
   * {@code null} and enclosing parens.
   */
    protected void appendCustomIfNotNull(AppendableExt app, SqlObject obj) throws IOException {
        if (obj != null) {
            openParen(app);
            app.append(obj);
            closeParen(app);
        }
    }

    /**
   * Appends the given nested clauses to the given AppendableExt, handling
   * empty nested clauses and enclosing parens.
   */
    protected void appendNestedClauses(AppendableExt app, SqlObjectList<? extends NestableClause> nestedClauses) throws IOException {
        boolean hasEmptyNestedClause = false;
        for (NestableClause nestedClause : nestedClauses) {
            if (nestedClause.isEmpty()) {
                hasEmptyNestedClause = true;
                break;
            }
        }
        SqlObjectList<? extends NestableClause> tmpNestedClauses;
        if (!hasEmptyNestedClause) {
            tmpNestedClauses = nestedClauses;
        } else {
            SqlObjectList<NestableClause> nonEmptyNestableClauses = new SqlObjectList<NestableClause>(nestedClauses.getDelimiter(), new ArrayList<NestableClause>(nestedClauses.size()));
            for (NestableClause nestedClause : nestedClauses) {
                if (!nestedClause.isEmpty()) {
                    nonEmptyNestableClauses.addObject(nestedClause);
                }
            }
            tmpNestedClauses = nonEmptyNestableClauses;
        }
        if ((tmpNestedClauses.size() > 1) && !isDisableParens()) {
            app.append("(").append(tmpNestedClauses).append(")");
        } else {
            app.append(tmpNestedClauses);
        }
    }
}
