package uk.org.ogsadai.tuple.join;

import java.util.Iterator;
import uk.org.ogsadai.expression.AndExpression;
import uk.org.ogsadai.expression.EqualExpression;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionVisitor;
import uk.org.ogsadai.expression.GreaterThanExpression;
import uk.org.ogsadai.expression.GreaterThanOrEqualExpression;
import uk.org.ogsadai.expression.InExpression;
import uk.org.ogsadai.expression.IsNullExpression;
import uk.org.ogsadai.expression.LessThanExpression;
import uk.org.ogsadai.expression.LessThanOrEqualExpression;
import uk.org.ogsadai.expression.LikeExpression;
import uk.org.ogsadai.expression.NotEqualExpression;
import uk.org.ogsadai.expression.NotExpression;
import uk.org.ogsadai.expression.OrExpression;

/**
 * Selects ranges of values held within the tree map. The operator to use is
 * specified by a comparison expression.
 * <p>
 * This class makes use of the Visitor pattern, see the Design Patterns book for
 * more details.
 * 
 * @author The OGSA-DAI Project Team.
 * @param <T>
 *            Key type
 * @param <U>
 *            Value type
 */
public class MultiValueTreeMapQuerier<T, U> implements ExpressionVisitor {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008";

    /** The multi value tree map to query. */
    private final MultiValueTreeMap<T, U> mTreeMap;

    /** The key value used to specify the range to select. */
    private T mKeyValue;

    /** The result to return. */
    private Iterator<U> mResult;

    /**
     * Constructor.  The instance produced may be queried multiple times.
     * 
     * @param treeMap  the tree map to query.
     */
    public MultiValueTreeMapQuerier(final MultiValueTreeMap<T, U> treeMap) {
        mTreeMap = treeMap;
    }

    /**
     * Queries the multi-value tree map using a comparison expression and a
     * key value.  For example, if the comparison expression is a 'less than'
     * expression and the key value if 10 then the iterator returned will give
     * access to those values whose key is less than 10.
     * 
     * @param expression  the comparison expression. Only the operator will be
     *                    use, the operands are ignored.
     * 
     * @param keyValue    key value
     * 
     * @return iterator to the selected range of values.
     */
    public Iterator<U> query(final Expression expression, final T keyValue) {
        mKeyValue = keyValue;
        expression.accept(this);
        return mResult;
    }

    /**
     * {@inheritDoc}
     */
    public void visitAndExpression(AndExpression expression) {
    }

    /**
     * {@inheritDoc}
     */
    public void visitOrExpression(OrExpression expression) {
    }

    /**
     * {@inheritDoc}
     */
    public void visitLessThanExpression(LessThanExpression expression) {
        mResult = mTreeMap.getLessThan(mKeyValue);
    }

    /**
     * {@inheritDoc}
     */
    public void visitLessThanOrEqualExpression(LessThanOrEqualExpression expression) {
        mResult = mTreeMap.getLessThanOrEqual(mKeyValue);
    }

    /**
     * {@inheritDoc}
     */
    public void visitGreaterThanExpression(GreaterThanExpression expression) {
        mResult = mTreeMap.getGreaterThan(mKeyValue);
    }

    /**
     * {@inheritDoc}
     */
    public void visitGreaterThanOrEqualExpression(GreaterThanOrEqualExpression expression) {
        mResult = mTreeMap.getGreaterThanOrEqual(mKeyValue);
    }

    /**
     * {@inheritDoc}
     */
    public void visitEqualExpression(EqualExpression expression) {
        mResult = mTreeMap.getEqual(mKeyValue);
    }

    /**
     * {@inheritDoc}
     */
    public void visitIsNullExpression(IsNullExpression expression) {
        mResult = mTreeMap.getEqual(null);
    }

    /**
     * {@inheritDoc}
     */
    public void visitNotEqualExpression(NotEqualExpression expression) {
        mResult = mTreeMap.getNotEqual(mKeyValue);
    }

    /**
     * {@inheritDoc}
     */
    public void visitNotExpression(NotExpression expression) {
    }

    /**
     * {@inheritDoc}
     */
    public void visitLikeExpression(LikeExpression expression) {
    }

    /**
     * {@inheritDoc}
     */
    public void visitInExpression(InExpression expression) {
    }
}
