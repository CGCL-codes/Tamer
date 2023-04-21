package org.nakedobjects.ide.eclipse.core.util;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.nakedobjects.ide.core.Constants;

public class MethodNameUtils {

    private MethodNameUtils() {
    }

    public static final String[] RESERVED_METHOD_NAMES = new String[] { "title", "iconName", "creating", "created", "loading", "loaded", "updating", "updated", "deleting", "deleted", "validate" };

    public static String asReservedMethodName(MethodDeclaration methodDeclaration) {
        if (!MethodUtils.isPublic(methodDeclaration)) {
            return null;
        }
        int numParameters = methodDeclaration.parameters().size();
        if (numParameters != 0) {
            return null;
        }
        String methodName = methodDeclaration.getName().getFullyQualifiedName();
        for (String reservedMethodName : RESERVED_METHOD_NAMES) {
            if (reservedMethodName.equals(methodName)) {
                return methodName;
            }
        }
        return null;
    }

    /**
     * Returns the property name for the method declaration, or (more likely
     * to be the case) <tt>null</tt> if the method doesn't represent the accessor of a property.
     *  
     * @param methodDeclaration
     * @return
     */
    public static String asPropertyName(MethodDeclaration methodDeclaration) {
        SimpleName name = methodDeclaration.getName();
        String methodName = name.getFullyQualifiedName();
        String candidatePropertyName = MethodUtils.unprefixed(methodName, Constants.PREFIX_GET);
        if (candidatePropertyName == null) {
            return null;
        }
        if (!MethodUtils.isPublic(methodDeclaration)) {
            return null;
        }
        if (methodDeclaration.parameters().size() > 0) {
            return null;
        }
        if (MethodNameUtils.asCollectionName(methodDeclaration) != null) {
            return null;
        }
        if (MethodNameUtils.isLog4jLogger(methodDeclaration)) {
            return null;
        }
        return candidatePropertyName;
    }

    /**
     * Returns the property name for the method declaration, or (more likely
     * to be the case) <tt>null</tt> if the method doesn't represent the accessor of a property.
     *  
     * @param methodDeclaration
     * @return
     */
    public static String asCollectionName(MethodDeclaration methodDeclaration) {
        IMethodBinding methodBinding = methodDeclaration.resolveBinding();
        if (methodBinding == null) {
            return "";
        }
        String methodName = methodBinding.getName();
        String candidateCollectionName = MethodUtils.unprefixed(methodName, Constants.PREFIX_GET);
        if (candidateCollectionName == null) {
            return null;
        }
        if (!MethodUtils.isPublic(methodDeclaration)) {
            return null;
        }
        if (methodDeclaration.parameters().size() > 0) {
            return null;
        }
        ITypeBinding returnType = methodBinding.getReturnType();
        ITypeBinding accessorReturnCollectionType = returnType.getTypeDeclaration();
        String qualifiedName = accessorReturnCollectionType.getQualifiedName();
        if (!MethodUtils.isCollectionType(qualifiedName)) {
            return null;
        }
        return candidateCollectionName;
    }

    /**
     * Returns the action name for the method declaration, or (more likely
     * to be the case) <tt>null</tt> if the method doesn't represent the action method
     * of an action.
     *  
     * @param methodDeclaration
     * @return
     */
    public static String asActionName(MethodDeclaration methodDeclaration) {
        if (!MethodUtils.isPublic(methodDeclaration)) {
            return null;
        }
        String methodName = methodDeclaration.getName().getFullyQualifiedName();
        String anyPrefix = MethodUtils.getPrefix(methodName);
        if (anyPrefix != null) {
            return null;
        }
        if (asReservedMethodName(methodDeclaration) != null) {
            return null;
        }
        return methodName;
    }

    public static boolean isLog4jLogger(MethodDeclaration methodDeclaration) {
        IMethodBinding methodBinding = methodDeclaration.resolveBinding();
        if (methodBinding == null) {
            return false;
        }
        ITypeBinding returnType = methodBinding.getReturnType();
        String qualifiedName = returnType.getQualifiedName();
        return "org.apache.log4j.Logger".equals(qualifiedName);
    }
}
