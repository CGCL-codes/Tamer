package org.eclipse.jdt.core.dom;

import java.util.ArrayList;
import java.util.List;

/**
 * Array access expression AST node type.
 *
 * <pre>
 * ArrayAccess:
 *    Expression <b>[</b> Expression <b>]</b>
 * </pre>
 * 
 * @since 2.0
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public class ArrayAccess extends Expression {

    /**
	 * The "array" structural property of this node type.
	 * @since 3.0
	 */
    public static final ChildPropertyDescriptor ARRAY_PROPERTY = new ChildPropertyDescriptor(ArrayAccess.class, "array", Expression.class, MANDATORY, CYCLE_RISK);

    /**
	 * The "index" structural property of this node type.
	 * @since 3.0
	 */
    public static final ChildPropertyDescriptor INDEX_PROPERTY = new ChildPropertyDescriptor(ArrayAccess.class, "index", Expression.class, MANDATORY, CYCLE_RISK);

    /**
	 * A list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
    private static final List PROPERTY_DESCRIPTORS;

    static {
        List properyList = new ArrayList(3);
        createPropertyList(ArrayAccess.class, properyList);
        addProperty(ARRAY_PROPERTY, properyList);
        addProperty(INDEX_PROPERTY, properyList);
        PROPERTY_DESCRIPTORS = reapPropertyList(properyList);
    }

    /**
	 * Returns a list of structural property descriptors for this node type.
	 * Clients must not modify the result.
	 * 
	 * @param apiLevel the API level; one of the
	 * <code>AST.JLS*</code> constants

	 * @return a list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor})
	 * @since 3.0
	 */
    public static List propertyDescriptors(int apiLevel) {
        return PROPERTY_DESCRIPTORS;
    }

    /**
	 * The array expression; lazily initialized; defaults to an unspecified,
	 * but legal, expression.
	 */
    private Expression arrayExpression = null;

    /**
	 * The index expression; lazily initialized; defaults to an unspecified,
	 * but legal, expression.
	 */
    private Expression indexExpression = null;

    /**
	 * Creates a new unparented array access expression node owned by the given 
	 * AST. By default, the array and index expresssions are unspecified, 
	 * but legal.
	 * <p>
	 * N.B. This constructor is package-private.
	 * </p>
	 * 
	 * @param ast the AST that is to own this node
	 */
    ArrayAccess(AST ast) {
        super(ast);
    }

    final List internalStructuralPropertiesForType(int apiLevel) {
        return propertyDescriptors(apiLevel);
    }

    final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
        if (property == ARRAY_PROPERTY) {
            if (get) {
                return getArray();
            } else {
                setArray((Expression) child);
                return null;
            }
        }
        if (property == INDEX_PROPERTY) {
            if (get) {
                return getIndex();
            } else {
                setIndex((Expression) child);
                return null;
            }
        }
        return super.internalGetSetChildProperty(property, get, child);
    }

    final int getNodeType0() {
        return ARRAY_ACCESS;
    }

    ASTNode clone0(AST target) {
        ArrayAccess result = new ArrayAccess(target);
        result.setSourceRange(this.getStartPosition(), this.getLength());
        result.setArray((Expression) getArray().clone(target));
        result.setIndex((Expression) getIndex().clone(target));
        return result;
    }

    final boolean subtreeMatch0(ASTMatcher matcher, Object other) {
        return matcher.match(this, other);
    }

    void accept0(ASTVisitor visitor) {
        boolean visitChildren = visitor.visit(this);
        if (visitChildren) {
            acceptChild(visitor, getArray());
            acceptChild(visitor, getIndex());
        }
        visitor.endVisit(this);
    }

    /**
	 * Returns the array expression of this array access expression.
	 * 
	 * @return the array expression node
	 */
    public Expression getArray() {
        if (this.arrayExpression == null) {
            synchronized (this) {
                if (this.arrayExpression == null) {
                    preLazyInit();
                    this.arrayExpression = new SimpleName(this.ast);
                    postLazyInit(this.arrayExpression, ARRAY_PROPERTY);
                }
            }
        }
        return this.arrayExpression;
    }

    /**
	 * Sets the array expression of this array access expression.
	 * 
	 * @param expression the array expression node
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */
    public void setArray(Expression expression) {
        if (expression == null) {
            throw new IllegalArgumentException();
        }
        ASTNode oldChild = this.arrayExpression;
        preReplaceChild(oldChild, expression, ARRAY_PROPERTY);
        this.arrayExpression = expression;
        postReplaceChild(oldChild, expression, ARRAY_PROPERTY);
    }

    /**
	 * Returns the index expression of this array access expression.
	 * 
	 * @return the index expression node
	 */
    public Expression getIndex() {
        if (this.indexExpression == null) {
            synchronized (this) {
                if (this.indexExpression == null) {
                    preLazyInit();
                    this.indexExpression = new SimpleName(this.ast);
                    postLazyInit(this.indexExpression, INDEX_PROPERTY);
                }
            }
        }
        return this.indexExpression;
    }

    /**
	 * Sets the index expression of this array access expression.
	 * 
	 * @param expression the index expression node
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */
    public void setIndex(Expression expression) {
        if (expression == null) {
            throw new IllegalArgumentException();
        }
        ASTNode oldChild = this.indexExpression;
        preReplaceChild(oldChild, expression, INDEX_PROPERTY);
        this.indexExpression = expression;
        postReplaceChild(oldChild, expression, INDEX_PROPERTY);
    }

    int memSize() {
        return BASE_NODE_SIZE + 2 * 4;
    }

    int treeSize() {
        return memSize() + (this.arrayExpression == null ? 0 : getArray().treeSize()) + (this.indexExpression == null ? 0 : getIndex().treeSize());
    }
}
