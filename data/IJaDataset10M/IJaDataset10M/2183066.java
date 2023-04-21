package org.jmlspecs.jir.ast.jdt.dom.handler;

import java.util.List;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.jmlspecs.jir.binding.BindingFactory;
import org.jmlspecs.jir.binding.IBindingManager;
import org.jmlspecs.jir.binding.JirTypeBinding;

public class JirLocalHandler implements IMethodInvocationHandler {

    private IMethodDispatcher md;

    @Override
    public String getHandledMethodName() {
        return "local";
    }

    @Override
    public void handle(final MethodInvocation mi) {
        final List<Expression> args = Util.args(mi);
        final String sourceName = ((StringLiteral) args.get(0)).getLiteralValue();
        final String binaryName = ((StringLiteral) args.get(1)).getLiteralValue();
        this.md.handle(args.get(2));
        final JirTypeBinding<Type> localType = Util.processTypeLiteral(this.md, (TypeLiteral) args.get(2));
        final boolean isParam = ((BooleanLiteral) args.get(3)).booleanValue();
        final int localSlotIndex = Integer.parseInt(((NumberLiteral) args.get(4)).getToken());
        final Name n = mi.getAST().newName(sourceName);
        Util.replace(this.md, mi, n);
        final IBindingManager<Expression, Type> bindingManager = this.md.getBindingManager();
        bindingManager.setVariableBinding(n, BindingFactory.getLocalBinding(sourceName, binaryName, localType, isParam, localSlotIndex));
        bindingManager.setTypeBinding(n, localType);
    }

    @Override
    public void setDispatcher(final IMethodDispatcher md) {
        this.md = md;
    }
}
