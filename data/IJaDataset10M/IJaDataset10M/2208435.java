package org.eclipse.jdt.internal.core.search;

import org.eclipse.jdt.internal.compiler.env.AccessRestriction;

/**
 * A <code>IRestrictedAccessConstructorRequestor</code> collects search results from a <code>searchAllConstructorDeclarations</code>
 * query to a <code>SearchBasicEngine</code> providing restricted access information of declaring type when a constructor is accepted.
 */
public interface IRestrictedAccessConstructorRequestor {

    public void acceptConstructor(int modifiers, char[] simpleTypeName, int parameterCount, char[] signature, char[][] parameterTypes, char[][] parameterNames, int typeModifiers, char[] packageName, int extraFlags, String path, AccessRestriction access);
}
