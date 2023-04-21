package com.ilog.translator.java2cs.translation.astrewriter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.corext.dom.ASTNodes;
import org.eclipse.text.edits.TextEditGroup;
import com.ilog.translator.java2cs.translation.ITranslationContext;

public class MoveClassInMethodToInnerVisitor extends ASTRewriterVisitor {

    public MoveClassInMethodToInnerVisitor(ITranslationContext context) {
        super(context);
        transformerName = "Move Class In Method To Inner";
        description = new TextEditGroup(transformerName);
    }

    @Override
    public boolean visit(TypeDeclaration node) {
        if (ASTNodes.getParent(node, ASTNode.METHOD_DECLARATION) != null) {
            final TypeDeclaration newType = (TypeDeclaration) currentRewriter.createMoveTarget(node);
            currentRewriter.remove(node, description);
            final TypeDeclaration enclosingTypeOfMethod = (TypeDeclaration) ASTNodes.getParent(node, ASTNode.TYPE_DECLARATION);
            currentRewriter.getListRewrite(enclosingTypeOfMethod, enclosingTypeOfMethod.getBodyDeclarationsProperty()).insertLast(newType, description);
        }
        context.autoPropertiesSearch(node);
        return true;
    }
}
