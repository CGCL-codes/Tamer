package org.jmlspecs.jir.ast.jdt.dom.handler;

import java.util.List;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.jmlspecs.jir.binding.IBindingManager;
import org.jmlspecs.jir.binding.JirTypeBinding;
import org.sireum.util.IProcedure;

public class JirThizHandler implements IMethodInvocationHandler {

    private IMethodDispatcher md;

    @Override
    public String getHandledMethodName() {
        return "thiz";
    }

    @Override
    public void handle(final MethodInvocation mi) {
        final List<Expression> args = Util.args(mi);
        final boolean isImplicit = ((BooleanLiteral) args.get(1)).booleanValue();
        final AST ast = mi.getAST();
        final IBindingManager<Expression, Type> bndMan = this.md.getBindingManager();
        this.md.handle(args.get(0));
        final TypeLiteral tl = (TypeLiteral) args.get(0);
        final JirTypeBinding<Type> type = Util.processTypeLiteral(this.md, tl);
        final ThisExpression thisExp = new IProcedure<ThisExpression>() {

            @Override
            public ThisExpression proceed() {
                final ThisExpression result = ast.newThisExpression();
                if (!isImplicit) {
                    result.setQualifier(ast.newName(type.getTypeName().getOptionalSourceFullyQualifiedName()));
                }
                return result;
            }
        }.proceed();
        bndMan.setTypeBinding(thisExp, type);
        Util.replace(this.md, mi, thisExp);
    }

    @Override
    public void setDispatcher(final IMethodDispatcher md) {
        this.md = md;
    }
}
