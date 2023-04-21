package org.jmlspecs.jir.util;

import java.util.List;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Type;
import org.jmlspecs.jir.JirExpressionTypeVisitor;
import org.jmlspecs.jir.ast.jdt.dom.JdtJirBindingManager;
import org.jmlspecs.jir.binding.JirBindingVisitor;
import org.jmlspecs.jir.binding.JirElementTypeOpBinding;
import org.jmlspecs.jir.binding.JirFieldBinding;
import org.jmlspecs.jir.binding.JirGenericTypeConversionOpBinding;
import org.jmlspecs.jir.binding.JirInstanceMethodOpBinding;
import org.jmlspecs.jir.binding.JirInvariantForOpBinding;
import org.jmlspecs.jir.binding.JirIsInitializedOpBinding;
import org.jmlspecs.jir.binding.JirLabelOpBinding;
import org.jmlspecs.jir.binding.JirLocalBinding;
import org.jmlspecs.jir.binding.JirMethodBinding;
import org.jmlspecs.jir.binding.JirNaryOpBinding;
import org.jmlspecs.jir.binding.JirOnlyCalledOpBinding;
import org.jmlspecs.jir.binding.JirQuantificationOpBinding;
import org.jmlspecs.jir.binding.JirQuantifiedVariableBinding;
import org.jmlspecs.jir.binding.JirSetComprehensionOpBinding;
import org.jmlspecs.jir.binding.JirSpaceOpBinding;
import org.jmlspecs.jir.binding.JirStaticMethodOpBinding;
import org.jmlspecs.jir.binding.JirTypeConversionOpBinding;
import org.jmlspecs.jir.binding.JirTypeOfOpBinding;
import org.jmlspecs.jir.binding.JirTypeOpBinding;

public class JirExpressionTypeBindingVisitor extends JirExpressionTypeVisitor {

    protected JirBindingVisitor<JdtJirBindingManager, Type, Expression> bndVis;

    public JirExpressionTypeBindingVisitor(final JdtJirBindingManager bindingManager) {
        super(bindingManager);
        this.bndVis = new JirBindingVisitor<JdtJirBindingManager, Type, Expression>(bindingManager);
    }

    public JirExpressionTypeBindingVisitor(final JdtJirBindingManager bindingManager, final JirBindingVisitor<JdtJirBindingManager, Type, Expression> bindingVisitor) {
        super(bindingManager);
        this.bndVis = bindingVisitor;
    }

    public void setBindingVisitor(final JirBindingVisitor<JdtJirBindingManager, Type, Expression> bndVis) {
        this.bndVis = bndVis;
    }

    @Override
    public boolean visit(final JirElementTypeOpBinding<Expression, Type> op) {
        this.bndVis.visit(op);
        return super.visit(op);
    }

    @Override
    public boolean visit(final JirFieldBinding<Type> bnd) {
        this.bndVis.visit(bnd);
        return super.visit(bnd);
    }

    @Override
    public boolean visit(final JirGenericTypeConversionOpBinding<Expression, Type> op) {
        this.bndVis.visit(op);
        return super.visit(op);
    }

    @Override
    public boolean visit(final JirInstanceMethodOpBinding<Expression> op) {
        this.bndVis.visit(op);
        return super.visit(op);
    }

    @Override
    public boolean visit(final JirInvariantForOpBinding<Expression> op) {
        this.bndVis.visit(op);
        return super.visit(op);
    }

    @Override
    public boolean visit(final JirIsInitializedOpBinding op) {
        this.bndVis.visit(op);
        return super.visit(op);
    }

    @Override
    public boolean visit(final JirLabelOpBinding<Expression> op) {
        this.bndVis.visit(op);
        return super.visit(op);
    }

    @Override
    public boolean visit(final JirLocalBinding<Type> bnd) {
        this.bndVis.visit(bnd);
        return super.visit(bnd);
    }

    @Override
    public boolean visit(final JirNaryOpBinding<Expression> op) {
        this.bndVis.visit(op);
        return super.visit(op);
    }

    @Override
    public boolean visit(final JirOnlyCalledOpBinding op) {
        this.bndVis.visit(op);
        return super.visit(op);
    }

    @Override
    public boolean visit(final JirQuantificationOpBinding<Expression, Type> op) {
        this.bndVis.visit(op);
        return super.visit(op);
    }

    @Override
    public boolean visit(final JirQuantifiedVariableBinding<Type> bnd) {
        this.bndVis.visit(bnd);
        return super.visit(bnd);
    }

    @Override
    public boolean visit(final JirSetComprehensionOpBinding<Expression, Type> op) {
        this.bndVis.visit(op);
        return super.visit(op);
    }

    @Override
    public boolean visit(final JirSpaceOpBinding op) {
        this.bndVis.visit(op);
        return super.visit(op);
    }

    @Override
    public boolean visit(final JirStaticMethodOpBinding<Expression> op) {
        this.bndVis.visit(op);
        return super.visit(op);
    }

    @Override
    public boolean visit(final JirTypeConversionOpBinding<Expression> op) {
        this.bndVis.visit(op);
        return super.visit(op);
    }

    @Override
    public boolean visit(final JirTypeOfOpBinding<Expression, Type> op) {
        this.bndVis.visit(op);
        return super.visit(op);
    }

    @Override
    public boolean visit(final JirTypeOpBinding<Type> op) {
        this.bndVis.visit(op);
        return super.visit(op);
    }

    @Override
    public boolean visit(final MethodInvocation node) {
        final JirMethodBinding mBnd = this.bindingManager.getMethodBinding(node);
        if (mBnd != null) {
            this.bndVis.visit(mBnd);
        }
        return super.visit(node);
    }

    @Override
    public boolean visitEquiv(final JirNaryOpBinding<Expression> op, final Expression e1, final Expression e2) {
        this.bndVis.visit(op);
        return super.visitEquiv(op, e1, e2);
    }

    @Override
    public boolean visitEverything(final JirNaryOpBinding<Expression> op) {
        this.bndVis.visit(op);
        return super.visitEverything(op);
    }

    @Override
    public boolean visitFresh(final JirNaryOpBinding<Expression> op, final Expression e) {
        this.bndVis.visit(op);
        return super.visitFresh(op, e);
    }

    @Override
    public boolean visitImplies(final JirNaryOpBinding<Expression> op, final Expression e1, final Expression e2) {
        this.bndVis.visit(op);
        return super.visitImplies(op, e1, e2);
    }

    @Override
    public boolean visitLockOrder(final JirNaryOpBinding<Expression> op, final Expression e1, final Expression e2) {
        this.bndVis.visit(op);
        return super.visitLockOrder(op, e1, e2);
    }

    @Override
    public boolean visitMethodInvocation(final MethodInvocation node) {
        final JirMethodBinding mBnd = this.bindingManager.getMethodBinding(node);
        if (mBnd != null) {
            this.bndVis.visit(mBnd);
        }
        return super.visitMethodInvocation(node);
    }

    @Override
    public boolean visitNonNullElements(final JirNaryOpBinding<Expression> op, final Expression e) {
        this.bndVis.visit(op);
        return super.visitNonNullElements(op, e);
    }

    @Override
    public boolean visitNotAssigned(final JirNaryOpBinding<Expression> op, final List<Expression> es) {
        this.bndVis.visit(op);
        return super.visitNotAssigned(op, es);
    }

    @Override
    public boolean visitNotEquiv(final JirNaryOpBinding<Expression> op, final Expression e1, final Expression e2) {
        this.bndVis.visit(op);
        return super.visitNotEquiv(op, e1, e2);
    }

    @Override
    public boolean visitNothing(final JirNaryOpBinding<Expression> op) {
        this.bndVis.visit(op);
        return super.visitNothing(op);
    }

    @Override
    public boolean visitNotModified(final JirNaryOpBinding<Expression> op, final List<Expression> es) {
        this.bndVis.visit(op);
        return super.visitNotModified(op, es);
    }

    @Override
    public boolean visitNotSpecified(final JirNaryOpBinding<Expression> op) {
        this.bndVis.visit(op);
        return super.visitNotSpecified(op);
    }

    @Override
    public boolean visitOld(final JirNaryOpBinding<Expression> op, final Expression expression) {
        this.bndVis.visit(op);
        return super.visitOld(op, expression);
    }

    @Override
    public boolean visitOnlyAccessed(final JirNaryOpBinding<Expression> op, final List<Expression> es) {
        this.bndVis.visit(op);
        return super.visitOnlyAccessed(op, es);
    }

    @Override
    public boolean visitOnlyAssigned(final JirNaryOpBinding<Expression> op, final List<Expression> es) {
        this.bndVis.visit(op);
        return super.visitOnlyAssigned(op, es);
    }

    @Override
    public boolean visitOnlyCaptured(final JirNaryOpBinding<Expression> op, final List<Expression> es) {
        this.bndVis.visit(op);
        return super.visitOnlyCaptured(op, es);
    }

    @Override
    public boolean visitReach(final JirNaryOpBinding<Expression> op, final Expression e) {
        this.bndVis.visit(op);
        return super.visitReach(op, e);
    }

    @Override
    public boolean visitResult(final JirNaryOpBinding<Expression> op) {
        this.bndVis.visit(op);
        return super.visitResult(op);
    }

    @Override
    public boolean visitRevImplies(final JirNaryOpBinding<Expression> op, final Expression e1, final Expression e2) {
        this.bndVis.visit(op);
        return super.visitRevImplies(op, e1, e2);
    }

    @Override
    public boolean visitSame(final JirNaryOpBinding<Expression> op) {
        this.bndVis.visit(op);
        return super.visitSame(op);
    }

    @Override
    public boolean visitSetComprehension(final JirNaryOpBinding<Expression> op, final Expression e1, final Expression e2, final Expression e3, final Expression e4) {
        this.bndVis.visit(op);
        return super.visitSetComprehension(op, e1, e2, e3, e4);
    }

    @Override
    public boolean visitStrictLockOrder(final JirNaryOpBinding<Expression> op, final Expression e1, final Expression e2) {
        this.bndVis.visit(op);
        return super.visitStrictLockOrder(op, e1, e2);
    }

    @Override
    public boolean visitSubtype(final JirNaryOpBinding<Expression> op, final Expression e1, final Expression e2) {
        this.bndVis.visit(op);
        return super.visitSubtype(op, e1, e2);
    }
}
