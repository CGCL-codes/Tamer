package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.PackageBinding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;

public class JavadocQualifiedTypeReference extends QualifiedTypeReference {

    public int tagSourceStart, tagSourceEnd;

    public PackageBinding packageBinding;

    public JavadocQualifiedTypeReference(char[][] sources, long[] pos, int tagStart, int tagEnd) {
        super(sources, pos);
        this.tagSourceStart = tagStart;
        this.tagSourceEnd = tagEnd;
        this.bits |= ASTNode.InsideJavadoc;
    }

    private TypeBinding internalResolveType(Scope scope, boolean checkBounds) {
        this.constant = Constant.NotAConstant;
        if (this.resolvedType != null) return this.resolvedType.isValidBinding() ? this.resolvedType : this.resolvedType.closestMatch();
        TypeBinding type = this.resolvedType = getTypeBinding(scope);
        if (type == null) return null;
        if (!type.isValidBinding()) {
            Binding binding = scope.getTypeOrPackage(this.tokens);
            if (binding instanceof PackageBinding) {
                this.packageBinding = (PackageBinding) binding;
                if (scope.compilationUnitScope().fPackage != this.packageBinding) {
                    scope.problemReporter().javadocInvalidReference(this.sourceStart, this.sourceEnd);
                }
            } else {
                reportInvalidType(scope);
            }
            return null;
        }
        if (isTypeUseDeprecated(type, scope)) reportDeprecatedType(type, scope);
        if (type.isGenericType() || type.isParameterizedType()) {
            this.resolvedType = scope.environment().convertToRawType(type, true);
        }
        return this.resolvedType;
    }

    protected void reportDeprecatedType(TypeBinding type, Scope scope) {
        scope.problemReporter().javadocDeprecatedType(type, this, scope.getDeclarationModifiers());
    }

    protected void reportInvalidType(Scope scope) {
        scope.problemReporter().javadocInvalidType(this, this.resolvedType, scope.getDeclarationModifiers());
    }

    public TypeBinding resolveType(BlockScope blockScope, boolean checkBounds) {
        return internalResolveType(blockScope, checkBounds);
    }

    public TypeBinding resolveType(ClassScope classScope) {
        return internalResolveType(classScope, false);
    }

    public void traverse(ASTVisitor visitor, BlockScope scope) {
        visitor.visit(this, scope);
        visitor.endVisit(this, scope);
    }

    public void traverse(ASTVisitor visitor, ClassScope scope) {
        visitor.visit(this, scope);
        visitor.endVisit(this, scope);
    }
}
