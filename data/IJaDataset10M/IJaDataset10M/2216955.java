package com.enerjy.analyzer.java.rules;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;

/**
 * JAVA0085 Use of sun.* class ''{0}''
 */
public class T0085 extends RuleBase {

    @Override
    public boolean visit(CompilationUnit unit) {
        NodeLookup nodes = getNodeLookup(unit);
        for (ITypeBinding ref : nodes.getReferencedTypes()) {
            String name = ref.getQualifiedName();
            if (name.startsWith("sun.")) {
                for (ASTNode reference : nodes.getNonDocReferenceNodes(ref)) {
                    addProblem(reference, name);
                }
            }
        }
        return false;
    }
}
