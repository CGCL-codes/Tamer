package com.enerjy.analyzer.java.rules;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;

/**
 * JAVA0279 Serialization method ''{0}'' calls overridable method ''{1}''
 */
public class T0279 extends RuleBase {

    @Override
    public boolean visit(CompilationUnit unit) {
        Set<IMethodBinding> readObjects = new HashSet<IMethodBinding>();
        NodeLookup nodes = getNodeLookup(unit);
        for (MethodDeclaration node : nodes.getNodes(MethodDeclaration.class)) {
            IMethodBinding method = node.resolveBinding();
            if ((null == method) || Modifier.isFinal(method.getDeclaringClass().getModifiers())) {
                continue;
            }
            if (!method.getName().startsWith("readObject") || !isSerializationMethod(method)) {
                continue;
            }
            readObjects.add(method);
        }
        if (readObjects.isEmpty()) {
            return false;
        }
        for (MethodInvocation node : nodes.getNodes(MethodInvocation.class)) {
            IMethodBinding method = node.resolveMethodBinding();
            if (null == method) {
                continue;
            }
            int mods = method.getModifiers();
            if (Modifier.isFinal(mods) || Modifier.isStatic(mods) || Modifier.isPrivate(mods)) {
                continue;
            }
            MethodDeclaration decl = RuleUtils.findMethodDecl(node);
            if (null == decl) {
                continue;
            }
            IMethodBinding declBinding = decl.resolveBinding();
            if (!readObjects.contains(declBinding)) {
                continue;
            }
            if (method.getDeclaringClass().isAssignmentCompatible(declBinding.getDeclaringClass())) {
                addProblem(node.getName(), decl.getName(), method.getName());
            }
        }
        return false;
    }
}
