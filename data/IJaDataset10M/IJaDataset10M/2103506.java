package org.rubypeople.rdt.refactoring.core.inlinemethod;

import org.jruby.ast.MethodDefNode;
import org.rubypeople.rdt.refactoring.classnodeprovider.IncludedClassesProvider;
import org.rubypeople.rdt.refactoring.documentprovider.IDocumentProvider;
import org.rubypeople.rdt.refactoring.nodewrapper.MethodNodeWrapper;

public class MethodFinder implements IMethodFinder {

    public MethodDefNode find(String className, String methodName, IDocumentProvider doc) {
        for (MethodNodeWrapper method : new IncludedClassesProvider(doc).getAllMethodsFor(className)) {
            if (method.getName().equals(methodName)) {
                return method.getWrappedNode();
            }
        }
        return null;
    }
}
