package com.enerjy.analyzer.java.rules;

import java.util.Collection;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * JAVA0017 Class name ''{0}'' does not have required form: {1}
 */
public class T0017 extends PatternRuleBase {

    @Override
    public boolean visit(CompilationUnit unit) {
        NodeLookup nodes = getNodeLookup(unit);
        Collection<TypeDeclaration> types = nodes.getNodes(ASTNode.TYPE_DECLARATION);
        for (TypeDeclaration type : types) {
            if (!type.isInterface()) {
                verify(type.getName());
            }
        }
        return false;
    }
}
