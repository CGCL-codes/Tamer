package de.odysseus.el;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.FunctionMapper;
import javax.el.MethodInfo;
import javax.el.VariableMapper;
import de.odysseus.el.misc.LocalMessages;
import de.odysseus.el.misc.TypeConverter;
import de.odysseus.el.tree.Bindings;
import de.odysseus.el.tree.ExpressionNode;
import de.odysseus.el.tree.Tree;
import de.odysseus.el.tree.TreeBuilder;
import de.odysseus.el.tree.TreeStore;
import de.odysseus.el.tree.NodePrinter;

/**
 * A method expression is ready to be evaluated (by calling either
 * {@link #invoke(ELContext, Object[])} or {@link #getMethodInfo(ELContext)}).
 *
 * Instances of this class are usually created using an {@link ExpressionFactoryImpl}.
 * 
 * @author Christoph Beck
 */
public final class TreeMethodExpression extends javax.el.MethodExpression {

    private static final long serialVersionUID = 1L;

    private final TreeBuilder builder;

    private final Bindings bindings;

    private final String expr;

    private final Class<?> type;

    private final Class<?>[] types;

    private final boolean deferred;

    private final boolean is_void;

    private transient ExpressionNode node;

    private String structure;

    /**
	 * Create a new method expression.
	 * The expression must be an lvalue expression or literal text.
	 * The expected return type may be <code>null</code>, meaning "don't care".
	 * If it is an lvalue expression, the parameter types must not be <code>null</code>.
	 * If it is literal text, the expected return type must not be <code>void</code>.
	 * @param store used to get the parse tree from.
	 * @param functions the function mapper used to bind functions
	 * @param variables the variable mapper used to bind variables
	 * @param expr the expression string
	 * @param returnType the expected return type (may be <code>null</code>)
	 * @param paramTypes the expected parameter types (must not be <code>null</code> for lvalues)
	 */
    public TreeMethodExpression(TreeStore store, FunctionMapper functions, VariableMapper variables, TypeConverter converter, String expr, Class<?> returnType, Class<?>[] paramTypes) {
        super();
        Tree tree = store.get(expr);
        this.builder = store.getBuilder();
        this.bindings = tree.bind(functions, variables, converter);
        this.expr = expr;
        this.type = returnType == void.class ? null : returnType;
        this.types = paramTypes;
        this.node = tree.getRoot();
        this.deferred = tree.isDeferred();
        this.is_void = returnType == void.class;
        if (node.isLiteralText()) {
            if (returnType == void.class) {
                throw new ELException(LocalMessages.get("error.method.literal.void", expr));
            }
        } else {
            if (!node.isLeftValue()) {
                throw new ELException(LocalMessages.get("error.method.invalid", expr));
            }
            if (paramTypes == null) {
                throw new ELException(LocalMessages.get("error.method.notypes"));
            }
        }
    }

    private String getStructuralId() {
        if (structure == null) {
            structure = node.getStructuralId(bindings);
        }
        return structure;
    }

    /**
   * Evaluates the expression and answers information about the method
   * @param context used to resolve properties (<code>base.property</code> and <code>base[property]</code>)
   * @return method information or <code>null</code> for literal expressions
   * @throws ELException if evaluation fails (e.g. suitable method not found)
   */
    @Override
    public MethodInfo getMethodInfo(ELContext context) throws ELException {
        return node.getMethodInfo(bindings, context, is_void ? void.class : type, types);
    }

    @Override
    public String getExpressionString() {
        return expr;
    }

    /**
   * Evaluates the expression and invokes the method.
   * @param context used to resolve properties (<code>base.property</code> and <code>base[property]</code>)
   * @return method result or <code>null</code> if this is a literal text expression
   * @throws ELException if evaluation fails (e.g. suitable method not found)
   */
    @Override
    public Object invoke(ELContext context, Object[] paramValues) throws ELException {
        return node.invoke(bindings, context, is_void ? void.class : type, types, paramValues);
    }

    /**
	 * @return <code>true</code> if this is a literal text expression
	 */
    @Override
    public boolean isLiteralText() {
        return node.isLiteralText();
    }

    /**
	 * Answer <code>true</code> if this is a deferred expression (starting with <code>#{</code>)
	 */
    public boolean isDeferred() {
        return deferred;
    }

    /**
	 * Expressions are compared using the concept of a <em>structural id</em>:
   * variable and function names are anonymized such that two expressions with
   * same tree structure will also have the same structural id and vice versa.
	 * Two method expressions are equal if
	 * <ol>
	 * <li>their builders are equal</li>
	 * <li>their structural id's are equal</li>
	 * <li>their bindings are equal</li>
	 * <li>their expected types match (see below)</li>
	 * <li>their parameter types are equal (no-text method expressions only)</li>
	 * </ol>
	 * For text method expressions, the two expected types match if both types are the
	 * same or one of them is <code>null</code> and the other is <code>Object.class</code>.
	 * For non-text method expressions, the expected types match if both types are the same
	 * or one of them is <code>null</code>.
	 */
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == getClass()) {
            TreeMethodExpression other = (TreeMethodExpression) obj;
            if (!builder.equals(other.builder)) {
                return false;
            }
            if (type != other.type) {
                if (type != null && other.type != null) {
                    return false;
                }
                if (isLiteralText()) {
                    if (type != Object.class && other.type != Object.class) {
                        return false;
                    }
                } else {
                    if (type != null && other.is_void || other.type != null && is_void) {
                        return false;
                    }
                }
            }
            if (!isLiteralText() && !Arrays.equals(types, other.types)) {
                return false;
            }
            return getStructuralId().equals(other.getStructuralId()) && bindings.equals(other.bindings);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getStructuralId().hashCode();
    }

    @Override
    public String toString() {
        return "TreeMethodExpression(" + expr + ")";
    }

    /**
	 * Print the parse tree.
	 * @param writer
	 */
    public void dump(PrintWriter writer) {
        NodePrinter.dump(writer, node);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        try {
            node = builder.build(expr).getRoot();
        } catch (ELException e) {
            throw new IOException(e.getMessage());
        }
    }
}
