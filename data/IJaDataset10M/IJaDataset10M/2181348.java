package recoder.java.declaration;

import recoder.abstraction.Type;
import recoder.abstraction.Variable;
import recoder.java.Declaration;
import recoder.java.Expression;
import recoder.java.ExpressionContainer;
import recoder.java.Identifier;
import recoder.java.JavaNonTerminalProgramElement;
import recoder.java.NamedProgramElement;
import recoder.java.NonTerminalProgramElement;
import recoder.java.ProgramElement;
import recoder.java.SourceElement;
import recoder.java.SourceVisitor;
import recoder.list.generic.ASTList;
import recoder.service.ProgramModelInfo;
import recoder.service.SourceInfo;

/**
 * Variable specification that defines a variable name. This is a part of a
 * {@link recoder.java.declaration.VariableDeclaration}and does not contain a
 * type reference or own modifiers. Note that calls to modifiers are delegated
 * to the enclosing variable declaration and are therefore discouraged. This was
 * necessary to subtype Declaration as analyses are interested in the exact
 * location of a variable name.
 * 
 * @author AL
 */
public class VariableSpecification extends JavaNonTerminalProgramElement implements Declaration, NamedProgramElement, ExpressionContainer, Variable {

    /**
	 * serialization id 
	 */
    private static final long serialVersionUID = -2190909599303924076L;

    /**
     * Parent.
     */
    VariableDeclaration parent;

    /**
     * Name.
     */
    private Identifier name;

    /**
     * Initializer.
     */
    private Expression initializer;

    /**
     * Dimensions.
     */
    private int dimensions;

    /**
     * Service.
     */
    SourceInfo service;

    /**
     * Variable specification.
     */
    public VariableSpecification() {
    }

    /**
     * Variable specification.
     * 
     * @param name
     *            an identifier.
     */
    public VariableSpecification(Identifier name) {
        setIdentifier(name);
        makeParentRoleValid();
    }

    /**
     * Variable specification.
     * 
     * @param name
     *            an identifier.
     * @param init
     *            an expression.
     */
    public VariableSpecification(Identifier name, Expression init) {
        setParent(parent);
        setIdentifier(name);
        setInitializer(init);
        makeParentRoleValid();
    }

    /**
     * Variable specification.
     * 
     * @param name
     *            an identifier.
     * @param dimensions
     *            an int value.
     * @param init
     *            an expression.
     */
    public VariableSpecification(Identifier name, int dimensions, Expression init) {
        setParent(parent);
        setIdentifier(name);
        setDimensions(dimensions);
        setInitializer(init);
        makeParentRoleValid();
    }

    /**
     * Variable specification.
     * 
     * @param proto
     *            a variable specification.
     */
    protected VariableSpecification(VariableSpecification proto) {
        super(proto);
        if (proto.name != null) {
            name = proto.name.deepClone();
        }
        if (proto.initializer != null) {
            initializer = proto.initializer.deepClone();
        }
        dimensions = proto.dimensions;
        makeParentRoleValid();
    }

    /**
     * Deep clone.
     * 
     * @return the object.
     */
    public VariableSpecification deepClone() {
        return new VariableSpecification(this);
    }

    /**
     * Make parent role valid.
     */
    public void makeParentRoleValid() {
        if (name != null) {
            name.setParent(this);
        }
        if (initializer != null) {
            initializer.setExpressionContainer(this);
        }
    }

    /**
     * Get AST parent.
     * 
     * @return the non terminal program element.
     */
    public NonTerminalProgramElement getASTParent() {
        return parent;
    }

    /**
     * Returns the number of children of this node.
     * 
     * @return an int giving the number of children of this node
     */
    public int getChildCount() {
        int result = 0;
        if (name != null) result++;
        if (initializer != null) result++;
        return result;
    }

    /**
     * Returns the child at the specified index in this node's "virtual" child
     * array
     * 
     * @param index
     *            an index into this node's "virtual" child array
     * @return the program element at the given position
     * @exception ArrayIndexOutOfBoundsException
     *                if <tt>index</tt> is out of bounds
     */
    public ProgramElement getChildAt(int index) {
        if (name != null) {
            if (index == 0) return name;
            index--;
        }
        if (initializer != null) {
            if (index == 0) return initializer;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int getChildPositionCode(ProgramElement child) {
        if (name == child) {
            return 0;
        }
        if (initializer == child) {
            return 1;
        }
        return -1;
    }

    /**
     * Replace a single child in the current node. The child to replace is
     * matched by identity and hence must be known exactly. The replacement
     * element can be null - in that case, the child is effectively removed. The
     * parent role of the new child is validated, while the parent link of the
     * replaced child is left untouched.
     * 
     * @param p
     *            the old child.
     * @param p
     *            the new child.
     * @return true if a replacement has occured, false otherwise.
     * @exception ClassCastException
     *                if the new child cannot take over the role of the old one.
     */
    public boolean replaceChild(ProgramElement p, ProgramElement q) {
        if (p == null) {
            throw new NullPointerException();
        }
        if (name == p) {
            Identifier r = (Identifier) q;
            name = r;
            if (r != null) {
                r.setParent(this);
            }
            return true;
        }
        if (initializer == p) {
            Expression r = (Expression) q;
            initializer = r;
            if (r != null) {
                r.setExpressionContainer(this);
            }
            return true;
        }
        return false;
    }

    /**
     * Set parent.
     * 
     * @param parent
     *            a variable declaration.
     */
    public void setParent(VariableDeclaration parent) {
        this.parent = parent;
    }

    /**
     * Get parent.
     * 
     * @return the variable declaration.
     */
    public VariableDeclaration getParent() {
        return parent;
    }

    /**
     * Get the number of expressions in this container.
     * 
     * @return the number of expressions.
     */
    public int getExpressionCount() {
        return (initializer != null) ? 1 : 0;
    }

    public Expression getExpressionAt(int index) {
        if (initializer != null && index == 0) {
            return initializer;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    /**
     * Get name.
     * 
     * @return the string.
     */
    public final String getName() {
        return (name == null) ? null : name.getText();
    }

    /**
     * Get identifier.
     * 
     * @return the identifier.
     */
    public Identifier getIdentifier() {
        return name;
    }

    /**
     * Set identifier.
     * 
     * @param id
     *            an identifier.
     */
    public void setIdentifier(Identifier id) {
        name = id;
    }

    /**
     * Set dimensions.
     * 
     * @param dimensions
     *            an int value.
     */
    public void setDimensions(int dimensions) {
        if (dimensions < 0) {
            throw new IllegalArgumentException("Negative dimension?");
        }
        this.dimensions = dimensions;
    }

    /**
     * Get dimensions.
     * 
     * @return the int value.
     */
    public int getDimensions() {
        return dimensions;
    }

    /**
     * Set initializer.
     * 
     * @param expr
     *            an expression.
     */
    public void setInitializer(Expression expr) {
        initializer = expr;
    }

    /**
     * Get initializer.
     * 
     * @return the expression.
     */
    public Expression getInitializer() {
        return initializer;
    }

    /**
     * Get modifiers of the enclosing declaration.
     * 
     * @return the modifier mutable list.
     */
    public ASTList<DeclarationSpecifier> getDeclarationSpecifiers() {
        return parent != null ? parent.getDeclarationSpecifiers() : null;
    }

    /**
     * Set modifiers of the enclosing declaration.
     * 
     * @param m
     *            a modifier mutable list.
     */
    public void setDeclarationSpecifiers(ASTList<DeclarationSpecifier> m) {
        if (parent != null) {
            parent.setDeclarationSpecifiers(m);
        }
    }

    /**
     * Test whether the declaration is strictfp.
     */
    public boolean isStrictFp() {
        return parent.isStrictFp();
    }

    /**
     * Test whether the declaration is final.
     */
    public boolean isFinal() {
        return parent.isFinal();
    }

    public ProgramModelInfo getProgramModelInfo() {
        return service;
    }

    public void setProgramModelInfo(ProgramModelInfo service) {
        if (!(service instanceof SourceInfo)) throw new IllegalArgumentException("service for VariableSpecification must be of type SourceInfo.");
        this.service = (SourceInfo) service;
    }

    private void updateModel() {
        getFactory().getServiceConfiguration().getChangeHistory().updateModel();
    }

    public Type getType() {
        if (service == null) {
            updateModel();
        }
        return service.getType(this);
    }

    public String getFullName() {
        return getName();
    }

    public String getBinaryName() {
        return getName();
    }

    public SourceElement getFirstElement() {
        return name;
    }

    public SourceElement getLastElement() {
        if (initializer != null) {
            return initializer.getLastElement();
        } else {
            return name;
        }
    }

    public void accept(SourceVisitor v) {
        v.visitVariableSpecification(this);
    }
}
