package uk.org.ogsadai.dqp.lqp.optimiser.join;

import java.util.List;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpressionVisitor;
import uk.org.ogsadai.expression.arithmetic.ColumnNameAmbiguousException;
import uk.org.ogsadai.expression.arithmetic.Constant;
import uk.org.ogsadai.expression.arithmetic.Div;
import uk.org.ogsadai.expression.arithmetic.ExecutableFunctionExpression;
import uk.org.ogsadai.expression.arithmetic.Minus;
import uk.org.ogsadai.expression.arithmetic.Mult;
import uk.org.ogsadai.expression.arithmetic.Plus;
import uk.org.ogsadai.expression.arithmetic.Star;
import uk.org.ogsadai.expression.arithmetic.TableColumn;
import uk.org.ogsadai.tuple.ColumnNotFoundException;

/**
 * Extracts the attributes used in an expression.
 *
 * @author The OGSA-DAI Project Team.
 */
public class AttributeExtractor implements ArithmeticExpressionVisitor {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008-2012";

    /** List of attributes that are available. */
    private List<Attribute> mAttributes;

    /**
     * Private constructor, only used by static method of this class.
     * 
     * @param attributes
     *            the list of available attributes
     */
    private AttributeExtractor(List<Attribute> attributes) {
        mAttributes = attributes;
    }

    /**
     * Checks whether the given arithmetic expression contains only attributes
     * from the specified list.
     * 
     * @param expression
     *            expression
     * @param attributes
     *            attributes 
     * @return the indexes of used attributes in the tuple metadata
     */
    public static boolean containsAttributes(ArithmeticExpression expression, List<Attribute> attributes) {
        AttributeExtractor extractor = new AttributeExtractor(attributes);
        try {
            expression.accept(extractor);
            return true;
        } catch (ColumnNotFoundException e) {
            return false;
        }
    }

    @Override
    public void visitConstant(Constant expression) {
    }

    @Override
    public void visitDiv(Div expression) {
        for (ArithmeticExpression child : expression.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visitFunction(ExecutableFunctionExpression function) {
        for (ArithmeticExpression child : function.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visitMinus(Minus expression) {
        for (ArithmeticExpression child : expression.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visitMult(Mult expression) {
        for (ArithmeticExpression child : expression.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visitPlus(Plus expression) {
        for (ArithmeticExpression child : expression.getChildren()) {
            child.accept(this);
        }
    }

    /**
    * @throws ColumnNotFoundException
    *             if the table column doesn't exist in the attribute list.
    */
    @Override
    public void visitTableColumn(TableColumn tableColumn) {
        String name = tableColumn.getName();
        String source = tableColumn.getSource();
        int index = -1;
        int i = 0;
        for (Attribute attribute : mAttributes) {
            String attrName = attribute.getName();
            if (source == null) {
                if (attrName.equals(name)) {
                    if (index != -1) {
                        throw new ColumnNameAmbiguousException(name);
                    }
                    index = i;
                }
            } else {
                if (attrName.equals(name) && source.equals(attribute.getSource())) {
                    return;
                }
            }
            i++;
        }
        if (index == -1) {
            throw new ColumnNotFoundException(name);
        }
    }

    @Override
    public void visitStar(Star expression) {
    }
}
