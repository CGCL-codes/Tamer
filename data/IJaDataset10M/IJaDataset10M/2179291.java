package deesel.parser.com.nodes;

import deesel.parser.ASTNode;
import deesel.parser.com.COMNode;
import deesel.parser.com.visitor.COMVisitor;
import deesel.parser.com.visitor.VisitorContext;
import deesel.util.logging.Logger;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author <a href="mailto:neil.ellis@mangala.co.uk>Neil Ellis</a>
 * @version $Revision: 1.1 $
 */
public class CompiledField implements Variable, DeeselField {

    private static Logger log = Logger.getLogger(CompiledField.class);

    private Field field;

    public CompiledField(Field field) {
        this.field = field;
    }

    public String getName() {
        return field.getName();
    }

    public boolean isPrivate() {
        return Modifier.isPrivate(field.getModifiers());
    }

    public boolean isProtected() {
        return Modifier.isProtected(field.getModifiers());
    }

    public boolean isPublic() {
        return Modifier.isPublic(field.getModifiers());
    }

    public boolean isStatic() {
        return Modifier.isStatic(field.getModifiers());
    }

    public boolean isFinal() {
        return Modifier.isFinal(field.getModifiers());
    }

    public boolean isVolatile() {
        return Modifier.isVolatile(field.getModifiers());
    }

    public boolean isTransient() {
        return Modifier.isTransient(field.getModifiers());
    }

    public Expression getInitialValue() {
        throw new IllegalArgumentException("Cannot call getInitialValue on a compiled field.");
    }

    public void accept(COMVisitor visitor, VisitorContext context) {
        throw new IllegalArgumentException("Cannot call accept on a compiled field.");
    }

    public DeeselClass getType() {
        return new CompiledClass(field.getType());
    }

    public void setParent(COMNode node) {
        throw new IllegalArgumentException("Cannot call setParent on a compiled field.");
    }

    public COMNode getParent() {
        throw new IllegalArgumentException("Cannot call getParent on a compiled field.");
    }

    public ASTNode getASTNode() {
        throw new IllegalArgumentException("Cannot call getASTNode on a compiled field.");
    }

    public void setASTNode(ASTNode node) {
        throw new IllegalArgumentException("Cannot call setASTNode on a compiled field.");
    }

    public DeeselMethod getParentMethod() {
        throw new IllegalArgumentException("Cannot call getParentMethod on a compiled field.");
    }

    public DeeselClass getParentClass() {
        throw new IllegalArgumentException("Cannot call getParentClass on a compiled field.");
    }

    public CompilationUnit getCompilationUnit() {
        throw new IllegalArgumentException("Cannot call getCompilationUnit on a compiled field.");
    }

    public DeeselClass getClassDecleration(Type name) {
        throw new IllegalArgumentException("Cannot call getClassDecleration on a compiled field.");
    }

    public Type[] getResolvedClassNames(Type name) {
        throw new IllegalArgumentException("Cannot call getResolvedClassNames on a compiled field.");
    }

    public COMNode[] getAllChildren() {
        return new COMNode[0];
    }

    public Variable getVariableDeclaration(String name) {
        return null;
    }

    public void validate() {
    }

    public void resolve() {
    }

    public boolean isValidated() {
        return false;
    }

    public boolean isField() {
        return true;
    }

    public boolean isLocal() {
        return false;
    }
}
