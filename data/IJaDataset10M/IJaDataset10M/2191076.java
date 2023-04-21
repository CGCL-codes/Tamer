package com.ilog.translator.java2cs.translation.astrewriter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.WildcardType;
import org.eclipse.jdt.internal.corext.dom.ASTNodes;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.text.edits.TextEditGroup;
import com.ilog.translator.java2cs.translation.ITranslationContext;
import com.ilog.translator.java2cs.translation.noderewriter.INodeRewriter;
import com.ilog.translator.java2cs.translation.noderewriter.SynchronizedKeywordRewriter;
import com.ilog.translator.java2cs.translation.util.TranslationUtils;
import com.ilog.translator.java2cs.util.TranslationModelUtil;

/**
 * Replace type and method declaration
 * 
 * @author afau
 *
 */
public class ComputeTypeAndMethodDeclVisitor extends ASTRewriterVisitor {

    public ComputeTypeAndMethodDeclVisitor(ITranslationContext context) {
        super(context);
        transformerName = "Compute Types And Methods Declaration";
        description = new TextEditGroup(transformerName);
    }

    @Override
    public boolean applyChange(IProgressMonitor pm) throws CoreException {
        final Change change = createChange(pm, null);
        context.addChange(fCu, change);
        return true;
    }

    @Override
    public boolean needRecovery() {
        return true;
    }

    @Override
    public void endVisit(AssertStatement node) {
        final Expression expr = node.getExpression();
        final String classname = context.getModel().getAssertReplacementClassName();
        final String newValueOfExpr = TranslationUtils.replaceByNewValue(expr, currentRewriter, fCu, context.getLogger());
        currentRewriter.replace(node, currentRewriter.createStringPlaceholder(classname + ".Assert(" + newValueOfExpr + ");", node.getNodeType()), description);
    }

    @Override
    public void endVisit(SynchronizedStatement node) {
        final Expression expr = node.getExpression();
        final NullProgressMonitor pm = new NullProgressMonitor();
        final SynchronizedKeywordRewriter result = new SynchronizedKeywordRewriter();
        final ComputeFieldAccessAndMethodInvocationVisitor nv = new ComputeFieldAccessAndMethodInvocationVisitor(context);
        nv.setCompilationUnit(fCu);
        nv.transform(pm, expr);
        result.setICompilationUnit(fCu);
        result.process(context, node, currentRewriter, nv.getRewriter(), description);
    }

    @Override
    public void endVisit(SimpleType node) {
        if (node.getParent().getParent().getNodeType() != ASTNode.ARRAY_CREATION) {
            final INodeRewriter result = context.getMapper().mapSimpleType(fCu, node);
            final ASTNode cic = ASTNodes.getParent(node, ASTNode.CLASS_INSTANCE_CREATION);
            if (cic != null) {
                final String comment = TranslationUtils.getBeforeComments(cic, fCu, context);
                if (comment != null && comment.trim().equals("/* j2cs:done */")) {
                    return;
                }
            }
            applyNodeRewriter(node, result, description);
        }
    }

    @Override
    public void endVisit(WildcardType node) {
        final Type bound = node.getBound();
        if (bound == null) {
            if (isALocalVariableDeclarationType(node) || isAMethodReturnType(node)) {
                final ASTNode replacement = currentRewriter.createStringPlaceholder("object", node.getNodeType());
                currentRewriter.replace(node, replacement, description);
            }
            return;
        }
        final ITypeBinding typeB = bound.resolveBinding();
        if (typeB == null) return;
        if (TranslationUtils.isObjectType(typeB)) {
            currentRewriter.replace(node, ASTNode.copySubtree(node.getAST(), bound), description);
        }
        if ((ASTNodes.getParent(node, ASTNode.BLOCK) != null) || isInAReturnType(node) || isAFieldDeclaration(node)) {
            final Object nv = currentRewriter.getEventStore().getNewValue(bound.getParent(), bound.getLocationInParent());
            if (nv != null) currentRewriter.replace(node, (ASTNode) nv, description); else currentRewriter.replace(node, currentRewriter.createCopyTarget(bound), description);
        }
    }

    private boolean isAMethodReturnType(WildcardType node) {
        return ASTNodes.getParent(node, ASTNode.VARIABLE_DECLARATION_STATEMENT) != null;
    }

    private boolean isAFieldDeclaration(WildcardType node) {
        return ASTNodes.getParent(node, ASTNode.FIELD_DECLARATION) != null;
    }

    private boolean isALocalVariableDeclarationType(WildcardType node) {
        return node.getParent().getParent().getNodeType() == ASTNode.METHOD_DECLARATION;
    }

    @Override
    public void endVisit(ParameterizedType node) {
        final INodeRewriter result = context.getMapper().mapParameterizedType(fCu, node);
        final ASTNode cic = ASTNodes.getParent(node, ASTNode.CLASS_INSTANCE_CREATION);
        if (cic != null) {
            final String comment = TranslationUtils.getBeforeComments(cic, fCu, context);
            if (comment != null && comment.trim().equals("/* j2cs:done */")) {
                return;
            }
        }
        final ITypeBinding enclosingType = ASTNodes.getEnclosingType(node);
        final ITypeBinding[] tArgs = enclosingType.getTypeArguments();
        final ITypeBinding[] tPars = enclosingType.getTypeParameters();
        if (enclosingType != null && (tArgs.length > 0 || tPars.length > 0)) {
            final ITypeBinding innerParametrizedType = node.resolveBinding();
            if (innerParametrizedType != null && innerParametrizedType.isMember()) {
                ITypeBinding declaringType = innerParametrizedType.getDeclaringClass();
                if (declaringType != null) {
                    if (declaringType.isRawType()) {
                        declaringType = declaringType.getErasure();
                        final String declName = replaceEnclosingTypeParam(declaringType, innerParametrizedType);
                        if (declName != null) {
                            final String replacement = "/* insert_here:" + declName + ". */" + innerParametrizedType.getName();
                            final ASTNode replNode = currentRewriter.createStringPlaceholder(replacement, node.getNodeType());
                            currentRewriter.replace(node, replNode, description);
                        }
                    } else {
                        declaringType = declaringType.getErasure();
                        final String declName = replaceEnclosingTypeParam(declaringType, innerParametrizedType);
                        if (declName != null) {
                            final String replacement = "/* insert_here:" + declName + ". */" + innerParametrizedType.getName();
                            final ASTNode replNode = currentRewriter.createStringPlaceholder(replacement, node.getNodeType());
                            currentRewriter.replace(node, replNode, description);
                        }
                    }
                }
            }
        }
        applyNodeRewriter(node, result, description);
    }

    @Override
    public boolean visit(QualifiedName node) {
        final IBinding binding = node.resolveBinding();
        if ((binding != null) && (node.resolveBinding().getKind() == IBinding.TYPE)) {
            INodeRewriter result = context.getMapper().mapType2(fCu, node);
            if (result != null) {
                result.setICompilationUnit(fCu);
                result.process(context, node, currentRewriter, null, description);
            }
            result = context.getMapper().mapPackageAccess(node, binding.getJavaElement().getJavaProject().getProject());
            if (result != null) {
                result.setICompilationUnit(fCu);
                result.process(context, node, currentRewriter, null, description);
                return false;
            }
        }
        return true;
    }

    @Override
    public void endVisit(PrimitiveType node) {
        final INodeRewriter result = context.getMapper().mapPrimitiveType(node);
        applyNodeRewriter(node, result, description);
    }

    @Override
    public void endVisit(TypeDeclaration node) {
        if (implementsSerializable(node.resolveBinding())) {
            if (!node.resolveBinding().isInterface()) context.addSerializable(node);
            removeSerializable(node);
        }
        final INodeRewriter result = context.getMapper().mapTypeDeclaration(node.resolveBinding(), context.getHandlerFromDoc(node, false));
        applyNodeRewriter(node, result, description);
    }

    @Override
    public void endVisit(MethodDeclaration node) {
        if (Modifier.isSynchronized(node.getModifiers())) {
            context.addSynchronized(node);
        }
        final INodeRewriter result = context.getMapper().mapMethodDeclaration(fCu.getElementName(), context.getSignatureFromDoc(node, false), context.getHandlerFromDoc(node, true), false, false, true);
        if (result != null) {
            result.setICompilationUnit(fCu);
            result.process(context, node, currentRewriter, null, description);
        }
    }

    @Override
    public void endVisit(Initializer node) {
        if (Modifier.isStatic(node.getModifiers())) {
            final INodeRewriter result = context.getMapper().mapInitializer(node);
            applyNodeRewriter(node, result, description);
        }
    }

    private String replaceEnclosingTypeParam(ITypeBinding enclosingType, ITypeBinding nestedarametrizedType) {
        final ITypeBinding[] typeParamForEnclosing = enclosingType.getTypeParameters();
        final ITypeBinding[] typeParamForNested = nestedarametrizedType.getTypeArguments();
        final List<String> typeParamNameForNested = getNames(typeParamForNested);
        if (typeParamForEnclosing.length == 0) return null;
        String fName = "<";
        for (int i = 0; i < typeParamForEnclosing.length; i++) {
            final ITypeBinding currentTB = typeParamForEnclosing[i];
            if (typeParamNameForNested.contains(currentTB.getName() + "_1")) fName += currentTB.getName() + "_1"; else fName += currentTB.getName();
            if (i < typeParamForEnclosing.length - 1) fName += ",";
        }
        fName += ">";
        return enclosingType.getName() + fName;
    }

    private List<String> getNames(ITypeBinding[] typeParamForEnclosing) {
        final List<String> res = new ArrayList<String>();
        for (final ITypeBinding current : typeParamForEnclosing) {
            res.add(current.getName());
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    private void removeSerializable(TypeDeclaration node) {
        final List<Type> interfaces = node.superInterfaceTypes();
        for (final Type type : interfaces) {
            final ITypeBinding typeBinding = type.resolveBinding();
            if (typeBinding.getQualifiedName().equals(Serializable.class.getName())) {
                currentRewriter.remove(type, description);
            }
        }
    }

    private boolean implementsSerializable(ITypeBinding typeBinding) {
        if (typeBinding.getQualifiedName().equals(Serializable.class.getName())) {
            return true;
        }
        for (final ITypeBinding currentInterface : typeBinding.getInterfaces()) {
            if (implementsSerializable(currentInterface)) {
                return true;
            }
        }
        if (typeBinding.getSuperclass() != null) {
            return implementsSerializable(typeBinding.getSuperclass());
        }
        return false;
    }

    private boolean isInAReturnType(ASTNode node) {
        if (node == null || node.getParent() == null || node.getNodeType() == ASTNode.METHOD_DECLARATION) return false;
        if (node.getLocationInParent().equals(MethodDeclaration.RETURN_TYPE2_PROPERTY)) {
            final MethodDeclaration methDecl = (MethodDeclaration) node.getParent();
            if (TranslationUtils.containsTag(methDecl, TranslationModelUtil.GENERIC_METHOD_WITHCONSTRAINT_TAG) || TranslationUtils.containsTag(methDecl, TranslationModelUtil.GENERIC_WILDCARD_TAG)) return false;
            return true;
        }
        return isInAReturnType(node.getParent());
    }
}
