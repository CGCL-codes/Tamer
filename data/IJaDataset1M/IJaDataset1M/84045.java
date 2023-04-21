package com.ilog.translator.java2cs.translation.astrewriter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.text.edits.TextEditGroup;
import com.ilog.translator.java2cs.translation.ITranslationContext;

/**
 * Try to automatically infer generic argument for a given generic type
 * 
 * 
 * @author afau
 *
 */
public class AutomaticInferGenericArgumentVisitor extends ASTRewriterVisitor {

    public AutomaticInferGenericArgumentVisitor(ITranslationContext context) {
        super(context);
        transformerName = "Automatic Infer Generic Argument";
        description = new TextEditGroup(transformerName);
    }

    @Override
    public boolean visit(MethodInvocation node) {
        final IMethodBinding mBinding = node.resolveMethodBinding();
        if (node.typeArguments().size() == 0 && isValidMethod(mBinding)) {
            final ITypeBinding[] typeParams = mBinding.getTypeArguments();
            final ListRewrite lr = currentRewriter.getListRewrite(node, MethodInvocation.TYPE_ARGUMENTS_PROPERTY);
            for (final ITypeBinding tParam : typeParams) {
                lr.insertLast(currentRewriter.createStringPlaceholder(tParam.getQualifiedName(), ASTNode.SIMPLE_TYPE), description);
            }
        }
        return true;
    }

    private boolean isValidMethod(IMethodBinding binding) {
        if (binding == null) return false;
        if (binding.getParameterTypes().length == 0) {
            final ITypeBinding retType = binding.getReturnType();
            if (retType.getTypeArguments().length > 0) {
                return true;
            }
        }
        return false;
    }
}
