package org.jmlspecs.jir.ast.jdt.dom.handler;

import org.eclipse.jdt.core.dom.MethodInvocation;
import org.jmlspecs.jir.binding.JirNaryOp;

public class JirFreshHandler implements IMethodInvocationHandler {

    private IMethodDispatcher md;

    @Override
    public String getHandledMethodName() {
        return "fresh";
    }

    @Override
    public void handle(final MethodInvocation mi) {
        Util.handle(this.md, mi, JirNaryOp.FRESH);
    }

    @Override
    public void setDispatcher(final IMethodDispatcher md) {
        this.md = md;
    }
}
