package org.jmlspecs.jir.ast.jdt.dom.handler;

import org.eclipse.jdt.core.dom.MethodInvocation;
import org.jmlspecs.jir.binding.JirNaryOp;

public class JirOnlyCapturedHandler implements IMethodInvocationHandler {

    private IMethodDispatcher md;

    @Override
    public String getHandledMethodName() {
        return "onlyCaptured";
    }

    @Override
    public void handle(final MethodInvocation mi) {
        Util.handle(this.md, mi, JirNaryOp.ONLY_CAPTURED);
    }

    @Override
    public void setDispatcher(final IMethodDispatcher md) {
        this.md = md;
    }
}
