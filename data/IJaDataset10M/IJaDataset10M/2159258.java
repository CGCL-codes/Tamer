package org.stjs.generator;

import japa.parser.ast.Node;
import org.stjs.generator.scope.QualifiedName;
import org.stjs.generator.scope.TypeScope;

public class ASTNodeData {

    private QualifiedName<?> qualifiedName;

    private TypeScope typeScope;

    private Node parent;

    public ASTNodeData() {
    }

    public ASTNodeData(Node parent) {
        this.parent = parent;
    }

    public QualifiedName<?> getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(QualifiedName<?> qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public TypeScope getTypeScope() {
        return typeScope;
    }

    public void setTypeScope(TypeScope typeScope) {
        this.typeScope = typeScope;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}
