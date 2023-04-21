package org.drarch.engine.jdt;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.search.MethodReferenceMatch;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.internal.core.SourceMethod;

public class DrarchSearchRequestor extends SearchRequestor {

    private static final Logger logger = Logger.getLogger(DrarchSearchRequestor.class.getName());

    @Override
    public void acceptSearchMatch(SearchMatch match) throws CoreException {
        if (match instanceof MethodReferenceMatch) {
            MethodReferenceMatch methodReference = (MethodReferenceMatch) match;
            if (methodReference.getElement() instanceof SourceMethod) {
                SourceMethod source = (SourceMethod) methodReference.getElement();
                String methodName = source.getElementName();
                IJavaElement javaElement = source.getParent();
                String className = javaElement.getElementName();
                IJavaElement javaE = javaElement.getParent().getParent();
                String packageName = javaE.getElementName();
                String qualifiedMethodName = packageName + "." + className + "." + methodName;
                org.eclipse.jdt.internal.core.CompilationUnit cu = (org.eclipse.jdt.internal.core.CompilationUnit) source.getParent().getParent();
                ASTNode astNode = source.findNode(parse(cu.getCompilationUnit()));
                MethodDeclaration md = (MethodDeclaration) astNode;
                md.accept(new MethodInvocationVisitor(qualifiedMethodName));
            }
        }
    }

    private CompilationUnit parse(ICompilationUnit unit) {
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setSource(unit);
        parser.setResolveBindings(true);
        return (CompilationUnit) parser.createAST(null);
    }
}
