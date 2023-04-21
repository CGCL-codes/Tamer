package com.enerjy.analyzer.java.rules;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * JAVA0260 Class ''{0}'': use ''enum'' instead of Enumerated Type pattern
 */
public class T0260 extends VersionedRuleBase {

    @Override
    protected String getMinimumVersion() {
        return VERSION_1_5;
    }

    @Override
    public boolean visit(CompilationUnit unit) {
        NodeLookup nodes = getNodeLookup(unit);
        for (TypeDeclaration node : nodes.getNodes(TypeDeclaration.class)) {
            ITypeBinding type = node.resolveBinding();
            if (null == type) {
                continue;
            }
            boolean hasPublicCtor = false;
            boolean hasPrivateCtor = false;
            for (IMethodBinding method : type.getDeclaredMethods()) {
                if (!method.isConstructor()) {
                    continue;
                }
                int mods = method.getModifiers();
                if (Modifier.isPublic(mods)) {
                    hasPublicCtor = true;
                } else if (Modifier.isPrivate(mods)) {
                    hasPrivateCtor = true;
                }
            }
            if (hasPublicCtor || !hasPrivateCtor) {
                continue;
            }
            int fieldCount = 0;
            for (IVariableBinding field : type.getDeclaredFields()) {
                int mods = field.getModifiers();
                if (!Modifier.isStatic(mods) || Modifier.isPrivate(mods)) {
                    continue;
                }
                ITypeBinding check = field.getType();
                if ((null == check) || (check != type)) {
                    continue;
                }
                ++fieldCount;
            }
            if (fieldCount > 1) {
                addProblem(node.getName(), node.getName().getIdentifier());
            }
        }
        return false;
    }
}
