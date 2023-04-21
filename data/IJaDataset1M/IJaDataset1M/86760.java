package org.eclipse.jdt.core.dom;

import java.util.ArrayList;
import java.util.List;

/**
 * Single member annotation node (added in JLS3 API). The single member annotation 
 * "@foo(bar)" is equivalent to the normal annotation "@foo(value=bar)". 
 * <p>
 * <pre>
 * SingleMemberAnnotation:
 *   <b>@</b> TypeName <b>(</b> Expression  <b>)</b>
 * </pre>
 * Within annotations, only certain kinds of expressions are meaningful,
 * including other annotations.
 * </p>
 * 
 * @since 3.1
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public final class SingleMemberAnnotation extends Annotation {

    /**
	 * The "typeName" structural property of this node type.
	 */
    public static final ChildPropertyDescriptor TYPE_NAME_PROPERTY = internalTypeNamePropertyFactory(SingleMemberAnnotation.class);

    /**
	 * The "value" structural property of this node type.
	 */
    public static final ChildPropertyDescriptor VALUE_PROPERTY = new ChildPropertyDescriptor(SingleMemberAnnotation.class, "value", Expression.class, MANDATORY, CYCLE_RISK);

    /**
	 * A list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
    private static final List PROPERTY_DESCRIPTORS;

    static {
        List propertyList = new ArrayList(3);
        createPropertyList(SingleMemberAnnotation.class, propertyList);
        addProperty(TYPE_NAME_PROPERTY, propertyList);
        addProperty(VALUE_PROPERTY, propertyList);
        PROPERTY_DESCRIPTORS = reapPropertyList(propertyList);
    }

    /**
	 * Returns a list of structural property descriptors for this node type.
	 * Clients must not modify the result.
	 * 
	 * @param apiLevel the API level; one of the AST.JLS* constants
	 * @return a list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor})
	 */
    public static List propertyDescriptors(int apiLevel) {
        return PROPERTY_DESCRIPTORS;
    }

    /**
	 * The value; lazily initialized; defaults to a unspecified, but legal,
	 * expression.
	 */
    private Expression value = null;

    /**
	 * Creates a new unparented normal annotation node owned 
	 * by the given AST.  By default, the annotation has an
	 * unspecified type name and an unspecified value.
	 * <p>
	 * N.B. This constructor is package-private.
	 * </p>
	 * 
	 * @param ast the AST that is to own this node
	 */
    SingleMemberAnnotation(AST ast) {
        super(ast);
        unsupportedIn2();
    }

    final List internalStructuralPropertiesForType(int apiLevel) {
        return propertyDescriptors(apiLevel);
    }

    final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
        if (property == TYPE_NAME_PROPERTY) {
            if (get) {
                return getTypeName();
            } else {
                setTypeName((Name) child);
                return null;
            }
        }
        if (property == VALUE_PROPERTY) {
            if (get) {
                return getValue();
            } else {
                setValue((Expression) child);
                return null;
            }
        }
        return super.internalGetSetChildProperty(property, get, child);
    }

    final ChildPropertyDescriptor internalTypeNameProperty() {
        return TYPE_NAME_PROPERTY;
    }

    final int getNodeType0() {
        return SINGLE_MEMBER_ANNOTATION;
    }

    ASTNode clone0(AST target) {
        SingleMemberAnnotation result = new SingleMemberAnnotation(target);
        result.setSourceRange(this.getStartPosition(), this.getLength());
        result.setTypeName((Name) ASTNode.copySubtree(target, getTypeName()));
        result.setValue((Expression) ASTNode.copySubtree(target, getValue()));
        return result;
    }

    final boolean subtreeMatch0(ASTMatcher matcher, Object other) {
        return matcher.match(this, other);
    }

    void accept0(ASTVisitor visitor) {
        boolean visitChildren = visitor.visit(this);
        if (visitChildren) {
            acceptChild(visitor, getTypeName());
            acceptChild(visitor, getValue());
        }
        visitor.endVisit(this);
    }

    /**
	 * Returns the value of this annotation.
	 * 
	 * @return the value node
	 */
    public Expression getValue() {
        if (this.value == null) {
            synchronized (this) {
                if (this.value == null) {
                    preLazyInit();
                    this.value = new SimpleName(this.ast);
                    postLazyInit(this.value, VALUE_PROPERTY);
                }
            }
        }
        return this.value;
    }

    /**
	 * Sets the value of this annotation.
	 * 
	 * @param value the new value
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */
    public void setValue(Expression value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        ASTNode oldChild = this.value;
        preReplaceChild(oldChild, value, VALUE_PROPERTY);
        this.value = value;
        postReplaceChild(oldChild, value, VALUE_PROPERTY);
    }

    int memSize() {
        return super.memSize() + 1 * 4;
    }

    int treeSize() {
        return memSize() + (this.typeName == null ? 0 : getTypeName().treeSize()) + (this.value == null ? 0 : getValue().treeSize());
    }
}
