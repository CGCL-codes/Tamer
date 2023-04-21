package com.ilog.translator.java2cs.translation.astrewriter;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.text.edits.TextEditGroup;
import com.ilog.translator.java2cs.translation.ITranslationContext;
import com.ilog.translator.java2cs.translation.noderewriter.ElementRewriter;
import com.ilog.translator.java2cs.translation.noderewriter.FieldRewriter;
import com.ilog.translator.java2cs.translation.noderewriter.INodeRewriter;

public class RenameFieldVisitor extends ASTRewriterVisitor {

    public RenameFieldVisitor(ITranslationContext context) {
        super(context);
        transformerName = "Rename Field";
        description = new TextEditGroup(transformerName);
    }

    @Override
    public boolean applyChange(IProgressMonitor pm) throws CoreException {
        final Change change = createChange(pm, null);
        if (change != null) {
            context.addChange(fCu, change);
        }
        return true;
    }

    @Override
    public void endVisit(QualifiedName node) {
        if (node.getParent().getNodeType() == ASTNode.VARIABLE_DECLARATION_EXPRESSION || node.getParent().getNodeType() == ASTNode.VARIABLE_DECLARATION_FRAGMENT) {
            final Name qual = node.getQualifier();
            ITypeBinding itype = null;
            if (qual != null) {
                itype = qual.resolveTypeBinding();
            }
            if (qual == null || !(itype != null && itype.isArray())) {
                final INodeRewriter result = context.getMapper().mapFieldAccess(fCu, node);
                applyNodeRewriter(node, result, description);
            }
            final INodeRewriter result = context.getMapper().mapType2(fCu, node);
            applyNodeRewriter(node, result, description);
        }
    }

    @Override
    public void endVisit(VariableDeclarationFragment node) {
        final INodeRewriter result = context.getMapper().mapFieldAccess(node);
        if (result != null && result instanceof ElementRewriter) ((ElementRewriter) result).setModifyModifiers(true);
        applyNodeRewriter(node, result, description);
    }

    @Override
    public boolean visit(FieldDeclaration node) {
        final FieldRewriter result = (FieldRewriter) context.getMapper().mapFieldDeclaration(node, context.getHandlerFromDoc(node, true));
        if (result != null) {
            result.setModifyModifiers(true);
            applyNodeRewriter(node, result, description);
        }
        return false;
    }

    @Override
    public void endVisit(SingleVariableDeclaration node) {
        final INodeRewriter result = context.getMapper().mapVariableDeclaration(node);
        applyNodeRewriter(node, result, description);
    }

    @Override
    public void endVisit(VariableDeclarationExpression node) {
        final INodeRewriter result = context.getMapper().mapVariableDeclaration(node);
        if (result != null && result instanceof ElementRewriter) ((ElementRewriter) result).setModifyModifiers(true);
        applyNodeRewriter(node, result, description);
    }
}
