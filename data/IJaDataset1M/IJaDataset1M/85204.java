package com.ilog.translator.java2cs.translation.astrewriter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.internal.corext.dom.ASTNodes;
import org.eclipse.text.edits.TextEditGroup;
import com.ilog.translator.java2cs.translation.ITranslationContext;

/**
 * Prepare instance methods from enumerations to be transformed in extension methods:
 * Replace in the body of the new created methods (see PrepareExtensionMethodsForEnum1) "this" with the added parameter 
 * @author nrrg
 *
 */
@SuppressWarnings("restriction")
public class PrepareExtensionMethodsForEnum2 extends ASTRewriterVisitor {

    public PrepareExtensionMethodsForEnum2(ITranslationContext context) {
        super(context);
        transformerName = "Prepare Extension Methods For Transforming Enums - replace this with parameter";
        description = new TextEditGroup(transformerName);
    }

    @Override
    public void endVisit(ThisExpression node) {
        ASTNode enumParent = ASTNodes.getParent(node, ASTNode.ENUM_DECLARATION);
        if (enumParent == null) return;
        ASTNode methodParent = ASTNodes.getParent(node, ASTNode.METHOD_DECLARATION);
        if (methodParent == null) return;
        MethodDeclaration method = (MethodDeclaration) methodParent;
        if (method.parameters().size() == 0) return;
        if (!(method.parameters().get(0) instanceof SingleVariableDeclaration)) return;
        SingleVariableDeclaration firstParameter = (SingleVariableDeclaration) method.parameters().get(0);
        if (!firstParameter.getName().getIdentifier().equalsIgnoreCase(PrepareExtensionMethodsForEnum1.PARAMETER_NAME)) return;
        context.getLogger().logInfo("[Custom info] Found this in enumeration " + ((EnumDeclaration) enumParent).getName() + " for method :" + ((MethodDeclaration) methodParent).getName());
        SimpleName newExpression = currentRewriter.getAST().newSimpleName(PrepareExtensionMethodsForEnum1.PARAMETER_NAME);
        currentRewriter.replace(node, newExpression, description);
    }
}
