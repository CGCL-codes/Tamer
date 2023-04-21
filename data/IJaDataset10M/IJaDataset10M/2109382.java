package org.jmlspecs.jir.jdt.jc.handler;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.jmlspecs.jir.JIRWriter;

public interface IMethodDispatcher {

    boolean dispatch(String methodName, MethodInvocation mi);

    JIRWriter getJIRWriter();

    MethodDeclaration getMethodDeclaration();

    void handle(ASTNode node);

    boolean registerHandler(Class<? extends IMethodInvocationHandler> handlerClass);

    void setMethodDeclaration(MethodDeclaration node);
}
