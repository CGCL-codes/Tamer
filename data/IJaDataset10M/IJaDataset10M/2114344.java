package org.jmlspecs.eclipse.refactor.dom;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeParameter;

/**
 * ASTVisitor that lists the imports required for an Ast sub tree.
 * 
 * @author iain
 */
public class ImportLister extends ASTVisitor {

    private Set<String> imports = new HashSet<String>();

    /**
     * List the imports required by the Ast sub tree.
     * 
     * @param node
     *            the root of the sub tree.
     * @return the set of imports required.
     */
    public static Set<String> list(ASTNode node) {
        ImportLister lister = new ImportLister();
        node.accept(lister);
        return lister.getImports();
    }

    /**
     * @return the discovered imports.
     */
    public Set<String> getImports() {
        return imports;
    }

    @Override
    public boolean visit(SingleVariableDeclaration node) {
        addType(node.getType());
        return super.visit(node);
    }

    @Override
    public boolean visit(MarkerAnnotation node) {
        imports.add(node.resolveAnnotationBinding().getAnnotationType().getQualifiedName());
        return super.visit(node);
    }

    @Override
    public boolean visit(ClassInstanceCreation node) {
        addType(node.getType());
        return super.visit(node);
    }

    @Override
    public boolean visit(CastExpression node) {
        addType(node.getType());
        return super.visit(node);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean visit(MethodDeclaration node) {
        addType(node.getReturnType2());
        List<SimpleName> exceptionNames = node.thrownExceptions();
        for (SimpleName name : exceptionNames) {
            imports.add(name.resolveTypeBinding().getQualifiedName());
        }
        return super.visit(node);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean visit(TypeParameter node) {
        List<Type> types = node.typeBounds();
        for (Type type : types) {
            addType(type);
        }
        return super.visit(node);
    }

    @Override
    public boolean visit(MethodInvocation node) {
        if (Flags.isStatic(node.resolveMethodBinding().getModifiers())) {
            imports.add(node.resolveMethodBinding().getDeclaringClass().getQualifiedName());
        }
        return super.visit(node);
    }

    @SuppressWarnings("unchecked")
    private void addType(Type type) {
        if (!type.isPrimitiveType()) {
            if (type.isArrayType()) {
                addType(((ArrayType) type).getComponentType());
            } else {
                imports.add(type.resolveBinding().getQualifiedName());
            }
        }
        if (type.isParameterizedType()) {
            List<Type> args = ((ParameterizedType) type).typeArguments();
            for (Type argType : args) {
                addType(argType);
            }
        }
    }
}
