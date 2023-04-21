package com.ilog.translator.java2cs.translation.astrewriter.astchange;

import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.internal.corext.refactoring.structure.CompilationUnitRewrite;

/**
 * Updates references to moved static members. Accepts
 * <code>CompilationUnit</code>s.
 */
public class ReferenceAnalyzer2 extends MoveStaticMemberAnalyzer2 {

    public ReferenceAnalyzer2(CompilationUnitRewrite cuRewrite, IBinding[] members, ITypeBinding target, ITypeBinding source) {
        super(cuRewrite, members, source, target);
    }

    public boolean needsTargetImport() {
        return fNeedsImport;
    }

    @Override
    public boolean visit(TypeDeclaration node) {
        ITypeBinding binding = node.resolveBinding();
        if (binding != null) {
            binding = binding.getTypeDeclaration();
            if (isMovedMember(binding)) {
                return false;
            }
        }
        return super.visit(node);
    }

    @Override
    public boolean visit(VariableDeclarationFragment node) {
        if (isMovedMember(node.resolveBinding())) {
            return false;
        }
        return super.visit(node);
    }

    @Override
    public boolean visit(FieldDeclaration node) {
        final VariableDeclarationFragment singleFragment = (VariableDeclarationFragment) node.fragments().get(0);
        if (isMovedMember(singleFragment.resolveBinding())) {
            return false;
        }
        return super.visit(node);
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        if (isMovedMember(node.resolveBinding())) {
            return false;
        }
        return super.visit(node);
    }

    @Override
    public boolean visit(SimpleName node) {
        if (!node.isDeclaration() && isMovedMember(node.resolveBinding()) && !isProcessed(node)) {
            this.rewrite(node, fTarget);
        }
        return false;
    }

    @Override
    public boolean visit(QualifiedName node) {
        if (isMovedMember(node.resolveBinding())) {
            if (node.getParent() instanceof ImportDeclaration) {
                final ITypeBinding typeBinding = node.resolveTypeBinding();
                if (typeBinding != null) {
                    fCuRewrite.getImportRewrite().removeImport(typeBinding.getQualifiedName());
                }
                final String imp = fCuRewrite.getImportRewrite().addImport(fTarget.getQualifiedName() + '.' + node.getName().getIdentifier());
                fCuRewrite.getImportRemover().registerAddedImport(imp);
            } else {
                this.rewrite(node, fTarget);
            }
            return false;
        } else {
            return super.visit(node);
        }
    }

    @Override
    public boolean visit(FieldAccess node) {
        if (isMovedMember(node.resolveFieldBinding())) {
            this.rewrite(node, fTarget);
        }
        return super.visit(node);
    }

    @Override
    public boolean visit(MethodInvocation node) {
        IMethodBinding binding = node.resolveMethodBinding();
        if (binding != null) {
            binding = binding.getMethodDeclaration();
            if (isMovedMember(binding)) {
                this.rewrite(node, fTarget);
            }
        }
        return super.visit(node);
    }

    @Override
    public boolean visit(MemberRef node) {
        if (isMovedMember(node.resolveBinding())) {
            this.rewrite(node, fTarget);
        }
        return false;
    }

    @Override
    public boolean visit(MethodRef node) {
        if (isMovedMember(node.resolveBinding())) {
            this.rewrite(node, fTarget);
        }
        return false;
    }

    @Override
    public boolean visit(AnnotationTypeDeclaration node) {
        if (isMovedMember(node.resolveBinding())) {
            return false;
        }
        return super.visit(node);
    }

    @Override
    public boolean visit(EnumDeclaration node) {
        if (isMovedMember(node.resolveBinding())) {
            return false;
        }
        return super.visit(node);
    }
}
