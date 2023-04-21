package com.ilog.translator.java2cs.translation.astrewriter;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.text.edits.TextEditGroup;
import com.ilog.translator.java2cs.translation.ITranslationContext;
import com.ilog.translator.java2cs.translation.noderewriter.BreakAndContinueLabelRewriter;
import com.ilog.translator.java2cs.translation.noderewriter.INodeRewriter;
import com.ilog.translator.java2cs.translation.noderewriter.StringRewriter;
import com.ilog.translator.java2cs.translation.noderewriter.SuperFieldAccessRewriter;
import com.ilog.translator.java2cs.translation.noderewriter.SuperMethodInvocationRewriter;

public class ReplaceKeywordInBodyVisitor extends ASTRewriterVisitor {

    public ReplaceKeywordInBodyVisitor(ITranslationContext context) {
        super(context);
        transformerName = "Replace keyword in body";
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
    public boolean needValidation() {
        return false;
    }

    @Override
    public void endVisit(StringLiteral node) {
        final INodeRewriter result = new StringRewriter(node.getEscapedValue());
        applyNodeRewriter(node, result, description);
    }

    @Override
    public void endVisit(BreakStatement node) {
        final SimpleName label = node.getLabel();
        if (label != null) {
            final INodeRewriter result = new BreakAndContinueLabelRewriter(label.getFullyQualifiedName());
            applyNodeRewriter(node, result, description);
        }
    }

    @Override
    public void endVisit(ContinueStatement node) {
        final SimpleName label = node.getLabel();
        if (label != null) {
            final INodeRewriter result = new BreakAndContinueLabelRewriter(label.getFullyQualifiedName());
            applyNodeRewriter(node, result, description);
        }
    }

    @Override
    public void endVisit(SuperMethodInvocation node) {
        final INodeRewriter result = new SuperMethodInvocationRewriter();
        applyNodeRewriter(node, result, description);
    }

    @Override
    public void endVisit(SuperFieldAccess node) {
        final INodeRewriter result = new SuperFieldAccessRewriter();
        applyNodeRewriter(node, result, description);
    }
}
