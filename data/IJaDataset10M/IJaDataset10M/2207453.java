package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.parser.Parser;

public class AnnotationMethodDeclaration extends MethodDeclaration {

    public Expression defaultValue;

    public int extendedDimensions;

    /**
	 * MethodDeclaration constructor comment.
	 */
    public AnnotationMethodDeclaration(CompilationResult compilationResult) {
        super(compilationResult);
    }

    public void generateCode(ClassFile classFile) {
        classFile.generateMethodInfoHeader(this.binding);
        int methodAttributeOffset = classFile.contentsOffset;
        int attributeNumber = classFile.generateMethodInfoAttribute(this.binding, this);
        classFile.completeMethodInfo(methodAttributeOffset, attributeNumber);
    }

    public boolean isAnnotationMethod() {
        return true;
    }

    public boolean isMethod() {
        return false;
    }

    public void parseStatements(Parser parser, CompilationUnitDeclaration unit) {
    }

    public StringBuffer print(int tab, StringBuffer output) {
        printIndent(tab, output);
        printModifiers(this.modifiers, output);
        if (this.annotations != null) printAnnotations(this.annotations, output);
        TypeParameter[] typeParams = typeParameters();
        if (typeParams != null) {
            output.append('<');
            int max = typeParams.length - 1;
            for (int j = 0; j < max; j++) {
                typeParams[j].print(0, output);
                output.append(", ");
            }
            typeParams[max].print(0, output);
            output.append('>');
        }
        printReturnType(0, output).append(this.selector).append('(');
        if (this.arguments != null) {
            for (int i = 0; i < this.arguments.length; i++) {
                if (i > 0) output.append(", ");
                this.arguments[i].print(0, output);
            }
        }
        output.append(')');
        if (this.thrownExceptions != null) {
            output.append(" throws ");
            for (int i = 0; i < this.thrownExceptions.length; i++) {
                if (i > 0) output.append(", ");
                this.thrownExceptions[i].print(0, output);
            }
        }
        if (this.defaultValue != null) {
            output.append(" default ");
            this.defaultValue.print(0, output);
        }
        printBody(tab + 1, output);
        return output;
    }

    public void resolveStatements() {
        super.resolveStatements();
        if (this.arguments != null) {
            this.scope.problemReporter().annotationMembersCannotHaveParameters(this);
        }
        if (this.typeParameters != null) {
            this.scope.problemReporter().annotationMembersCannotHaveTypeParameters(this);
        }
        if (this.extendedDimensions != 0) {
            this.scope.problemReporter().illegalExtendedDimensions(this);
        }
        if (this.binding == null) return;
        TypeBinding returnTypeBinding = this.binding.returnType;
        if (returnTypeBinding != null) {
            checkAnnotationMethodType: {
                TypeBinding leafReturnType = returnTypeBinding.leafComponentType();
                if (returnTypeBinding.dimensions() <= 1) {
                    switch(leafReturnType.erasure().id) {
                        case T_byte:
                        case T_short:
                        case T_char:
                        case T_int:
                        case T_long:
                        case T_float:
                        case T_double:
                        case T_boolean:
                        case T_JavaLangString:
                        case T_JavaLangClass:
                            break checkAnnotationMethodType;
                    }
                    if (leafReturnType.isEnum() || leafReturnType.isAnnotationType()) break checkAnnotationMethodType;
                }
                this.scope.problemReporter().invalidAnnotationMemberType(this);
            }
            if (this.defaultValue != null) {
                MemberValuePair pair = new MemberValuePair(this.selector, this.sourceStart, this.sourceEnd, this.defaultValue);
                pair.binding = this.binding;
                pair.resolveTypeExpecting(this.scope, returnTypeBinding);
                this.binding.setDefaultValue(org.eclipse.jdt.internal.compiler.lookup.ElementValuePair.getValue(this.defaultValue));
            } else {
                this.binding.setDefaultValue(null);
            }
        }
    }

    public void traverse(ASTVisitor visitor, ClassScope classScope) {
        if (visitor.visit(this, classScope)) {
            if (this.annotations != null) {
                int annotationsLength = this.annotations.length;
                for (int i = 0; i < annotationsLength; i++) this.annotations[i].traverse(visitor, this.scope);
            }
            if (this.returnType != null) {
                this.returnType.traverse(visitor, this.scope);
            }
            if (this.defaultValue != null) {
                this.defaultValue.traverse(visitor, this.scope);
            }
        }
        visitor.endVisit(this, classScope);
    }
}
