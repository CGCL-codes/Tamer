package org.arastreju.core.ontology.odql;

/**
 * AST representing the declaration of (default) namespace.
 * 
 * Created: 17.07.2008
 * 
 * @author Oliver Tigges
 */
public class ASTNamespaceDeclaration extends SimpleNode {

    public String defaultNamespace;

    public ASTNamespaceDeclaration(int id) {
        super(id);
    }

    public String getDefaultNamespace() {
        return defaultNamespace;
    }

    @Override
    public String toString() {
        return "NamespaceDeclaration default = " + getDefaultNamespace();
    }
}
