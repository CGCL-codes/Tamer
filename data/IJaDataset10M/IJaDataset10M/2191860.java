package org.eclipse.jdt.internal.compiler.ast;

import java.util.ArrayList;
import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.parser.*;
import org.eclipse.jdt.internal.compiler.problem.*;
import org.eclipse.jdt.internal.compiler.util.Util;

public class ConstructorDeclaration extends AbstractMethodDeclaration {

    public ExplicitConstructorCall constructorCall;

    public TypeParameter[] typeParameters;

    public ConstructorDeclaration(CompilationResult compilationResult) {
        super(compilationResult);
    }

    /**
 * @see org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration#analyseCode(org.eclipse.jdt.internal.compiler.lookup.ClassScope, org.eclipse.jdt.internal.compiler.flow.InitializationFlowContext, org.eclipse.jdt.internal.compiler.flow.FlowInfo)
 * @deprecated use instead {@link #analyseCode(ClassScope, InitializationFlowContext, FlowInfo, int)}
 */
    public void analyseCode(ClassScope classScope, InitializationFlowContext initializerFlowContext, FlowInfo flowInfo) {
        analyseCode(classScope, initializerFlowContext, flowInfo, FlowInfo.REACHABLE);
    }

    /**
 * The flowInfo corresponds to non-static field initialization infos. It may be unreachable (155423), but still the explicit constructor call must be
 * analysed as reachable, since it will be generated in the end.
 */
    public void analyseCode(ClassScope classScope, InitializationFlowContext initializerFlowContext, FlowInfo flowInfo, int initialReachMode) {
        if (this.ignoreFurtherInvestigation) return;
        int nonStaticFieldInfoReachMode = flowInfo.reachMode();
        flowInfo.setReachMode(initialReachMode);
        checkUnused: {
            MethodBinding constructorBinding;
            if ((constructorBinding = this.binding) == null) break checkUnused;
            if ((this.bits & ASTNode.IsDefaultConstructor) != 0) break checkUnused;
            if (constructorBinding.isUsed()) break checkUnused;
            if (constructorBinding.isPrivate()) {
                if ((this.binding.declaringClass.tagBits & TagBits.HasNonPrivateConstructor) == 0) break checkUnused;
            } else if (!constructorBinding.isOrEnclosedByPrivateType()) {
                break checkUnused;
            }
            if (this.constructorCall == null) break checkUnused;
            if (this.constructorCall.accessMode != ExplicitConstructorCall.This) {
                ReferenceBinding superClass = constructorBinding.declaringClass.superclass();
                if (superClass == null) break checkUnused;
                MethodBinding methodBinding = superClass.getExactConstructor(Binding.NO_PARAMETERS);
                if (methodBinding == null) break checkUnused;
                if (!methodBinding.canBeSeenBy(SuperReference.implicitSuperConstructorCall(), this.scope)) break checkUnused;
            }
            this.scope.problemReporter().unusedPrivateConstructor(this);
        }
        if (isRecursive(null)) {
            this.scope.problemReporter().recursiveConstructorInvocation(this.constructorCall);
        }
        try {
            ExceptionHandlingFlowContext constructorContext = new ExceptionHandlingFlowContext(initializerFlowContext.parent, this, this.binding.thrownExceptions, initializerFlowContext, this.scope, FlowInfo.DEAD_END);
            initializerFlowContext.checkInitializerExceptions(this.scope, constructorContext, flowInfo);
            if (this.binding.declaringClass.isAnonymousType()) {
                ArrayList computedExceptions = constructorContext.extendedExceptions;
                if (computedExceptions != null) {
                    int size;
                    if ((size = computedExceptions.size()) > 0) {
                        ReferenceBinding[] actuallyThrownExceptions;
                        computedExceptions.toArray(actuallyThrownExceptions = new ReferenceBinding[size]);
                        this.binding.thrownExceptions = actuallyThrownExceptions;
                    }
                }
            }
            if (this.arguments != null) {
                for (int i = 0, count = this.arguments.length; i < count; i++) {
                    flowInfo.markAsDefinitelyAssigned(this.arguments[i].binding);
                }
            }
            if (this.constructorCall != null) {
                if (this.constructorCall.accessMode == ExplicitConstructorCall.This) {
                    FieldBinding[] fields = this.binding.declaringClass.fields();
                    for (int i = 0, count = fields.length; i < count; i++) {
                        FieldBinding field;
                        if (!(field = fields[i]).isStatic()) {
                            flowInfo.markAsDefinitelyAssigned(field);
                        }
                    }
                }
                flowInfo = this.constructorCall.analyseCode(this.scope, constructorContext, flowInfo);
            }
            flowInfo.setReachMode(nonStaticFieldInfoReachMode);
            if (this.statements != null) {
                int complaintLevel = (nonStaticFieldInfoReachMode & FlowInfo.UNREACHABLE) == 0 ? Statement.NOT_COMPLAINED : Statement.COMPLAINED_FAKE_REACHABLE;
                for (int i = 0, count = this.statements.length; i < count; i++) {
                    Statement stat = this.statements[i];
                    if ((complaintLevel = stat.complainIfUnreachable(flowInfo, this.scope, complaintLevel)) < Statement.COMPLAINED_UNREACHABLE) {
                        flowInfo = stat.analyseCode(this.scope, constructorContext, flowInfo);
                    }
                }
            }
            if ((flowInfo.tagBits & FlowInfo.UNREACHABLE) == 0) {
                this.bits |= ASTNode.NeedFreeReturn;
            }
            flowInfo.setReachMode(initialReachMode);
            if ((this.constructorCall != null) && (this.constructorCall.accessMode != ExplicitConstructorCall.This)) {
                flowInfo = flowInfo.mergedWith(constructorContext.initsOnReturn);
                FieldBinding[] fields = this.binding.declaringClass.fields();
                for (int i = 0, count = fields.length; i < count; i++) {
                    FieldBinding field;
                    if ((!(field = fields[i]).isStatic()) && field.isFinal() && (!flowInfo.isDefinitelyAssigned(fields[i]))) {
                        this.scope.problemReporter().uninitializedBlankFinalField(field, ((this.bits & ASTNode.IsDefaultConstructor) != 0) ? (ASTNode) this.scope.referenceType() : this);
                    }
                }
            }
            constructorContext.complainIfUnusedExceptionHandlers(this);
            this.scope.checkUnusedParameters(this.binding);
        } catch (AbortMethod e) {
            this.ignoreFurtherInvestigation = true;
        }
    }

    /**
 * Bytecode generation for a constructor
 *
 * @param classScope org.eclipse.jdt.internal.compiler.lookup.ClassScope
 * @param classFile org.eclipse.jdt.internal.compiler.codegen.ClassFile
 */
    public void generateCode(ClassScope classScope, ClassFile classFile) {
        int problemResetPC = 0;
        if (this.ignoreFurtherInvestigation) {
            if (this.binding == null) return;
            int problemsLength;
            CategorizedProblem[] problems = this.scope.referenceCompilationUnit().compilationResult.getProblems();
            CategorizedProblem[] problemsCopy = new CategorizedProblem[problemsLength = problems.length];
            System.arraycopy(problems, 0, problemsCopy, 0, problemsLength);
            classFile.addProblemConstructor(this, this.binding, problemsCopy);
            return;
        }
        try {
            problemResetPC = classFile.contentsOffset;
            internalGenerateCode(classScope, classFile);
        } catch (AbortMethod e) {
            if (e.compilationResult == CodeStream.RESTART_IN_WIDE_MODE) {
                try {
                    classFile.contentsOffset = problemResetPC;
                    classFile.methodCount--;
                    classFile.codeStream.resetInWideMode();
                    internalGenerateCode(classScope, classFile);
                } catch (AbortMethod e2) {
                    int problemsLength;
                    CategorizedProblem[] problems = this.scope.referenceCompilationUnit().compilationResult.getAllProblems();
                    CategorizedProblem[] problemsCopy = new CategorizedProblem[problemsLength = problems.length];
                    System.arraycopy(problems, 0, problemsCopy, 0, problemsLength);
                    classFile.addProblemConstructor(this, this.binding, problemsCopy, problemResetPC);
                }
            } else {
                int problemsLength;
                CategorizedProblem[] problems = this.scope.referenceCompilationUnit().compilationResult.getAllProblems();
                CategorizedProblem[] problemsCopy = new CategorizedProblem[problemsLength = problems.length];
                System.arraycopy(problems, 0, problemsCopy, 0, problemsLength);
                classFile.addProblemConstructor(this, this.binding, problemsCopy, problemResetPC);
            }
        }
    }

    public void generateSyntheticFieldInitializationsIfNecessary(MethodScope methodScope, CodeStream codeStream, ReferenceBinding declaringClass) {
        if (!declaringClass.isNestedType()) return;
        NestedTypeBinding nestedType = (NestedTypeBinding) declaringClass;
        SyntheticArgumentBinding[] syntheticArgs = nestedType.syntheticEnclosingInstances();
        for (int i = 0, max = syntheticArgs == null ? 0 : syntheticArgs.length; i < max; i++) {
            SyntheticArgumentBinding syntheticArg;
            if ((syntheticArg = syntheticArgs[i]).matchingField != null) {
                codeStream.aload_0();
                codeStream.load(syntheticArg);
                codeStream.fieldAccess(Opcodes.OPC_putfield, syntheticArg.matchingField, null);
            }
        }
        syntheticArgs = nestedType.syntheticOuterLocalVariables();
        for (int i = 0, max = syntheticArgs == null ? 0 : syntheticArgs.length; i < max; i++) {
            SyntheticArgumentBinding syntheticArg;
            if ((syntheticArg = syntheticArgs[i]).matchingField != null) {
                codeStream.aload_0();
                codeStream.load(syntheticArg);
                codeStream.fieldAccess(Opcodes.OPC_putfield, syntheticArg.matchingField, null);
            }
        }
    }

    private void internalGenerateCode(ClassScope classScope, ClassFile classFile) {
        classFile.generateMethodInfoHeader(this.binding);
        int methodAttributeOffset = classFile.contentsOffset;
        int attributeNumber = classFile.generateMethodInfoAttribute(this.binding);
        if ((!this.binding.isNative()) && (!this.binding.isAbstract())) {
            TypeDeclaration declaringType = classScope.referenceContext;
            int codeAttributeOffset = classFile.contentsOffset;
            classFile.generateCodeAttributeHeader();
            CodeStream codeStream = classFile.codeStream;
            codeStream.reset(this, classFile);
            ReferenceBinding declaringClass = this.binding.declaringClass;
            int enumOffset = declaringClass.isEnum() ? 2 : 0;
            int argSlotSize = 1 + enumOffset;
            if (declaringClass.isNestedType()) {
                this.scope.extraSyntheticArguments = declaringClass.syntheticOuterLocalVariables();
                this.scope.computeLocalVariablePositions(declaringClass.getEnclosingInstancesSlotSize() + 1 + enumOffset, codeStream);
                argSlotSize += declaringClass.getEnclosingInstancesSlotSize();
                argSlotSize += declaringClass.getOuterLocalVariablesSlotSize();
            } else {
                this.scope.computeLocalVariablePositions(1 + enumOffset, codeStream);
            }
            if (this.arguments != null) {
                for (int i = 0, max = this.arguments.length; i < max; i++) {
                    LocalVariableBinding argBinding;
                    codeStream.addVisibleLocalVariable(argBinding = this.arguments[i].binding);
                    argBinding.recordInitializationStartPC(0);
                    switch(argBinding.type.id) {
                        case TypeIds.T_long:
                        case TypeIds.T_double:
                            argSlotSize += 2;
                            break;
                        default:
                            argSlotSize++;
                            break;
                    }
                }
            }
            MethodScope initializerScope = declaringType.initializerScope;
            initializerScope.computeLocalVariablePositions(argSlotSize, codeStream);
            boolean needFieldInitializations = this.constructorCall == null || this.constructorCall.accessMode != ExplicitConstructorCall.This;
            boolean preInitSyntheticFields = this.scope.compilerOptions().targetJDK >= ClassFileConstants.JDK1_4;
            if (needFieldInitializations && preInitSyntheticFields) {
                generateSyntheticFieldInitializationsIfNecessary(this.scope, codeStream, declaringClass);
            }
            if (this.constructorCall != null) {
                this.constructorCall.generateCode(this.scope, codeStream);
            }
            if (needFieldInitializations) {
                if (!preInitSyntheticFields) {
                    generateSyntheticFieldInitializationsIfNecessary(this.scope, codeStream, declaringClass);
                }
                if (declaringType.fields != null) {
                    for (int i = 0, max = declaringType.fields.length; i < max; i++) {
                        FieldDeclaration fieldDecl;
                        if (!(fieldDecl = declaringType.fields[i]).isStatic()) {
                            fieldDecl.generateCode(initializerScope, codeStream);
                        }
                    }
                }
            }
            if (this.statements != null) {
                for (int i = 0, max = this.statements.length; i < max; i++) {
                    this.statements[i].generateCode(this.scope, codeStream);
                }
            }
            if (this.ignoreFurtherInvestigation) {
                throw new AbortMethod(this.scope.referenceCompilationUnit().compilationResult, null);
            }
            if ((this.bits & ASTNode.NeedFreeReturn) != 0) {
                codeStream.return_();
            }
            codeStream.exitUserScope(this.scope);
            codeStream.recordPositionsFrom(0, this.bodyEnd);
            classFile.completeCodeAttribute(codeAttributeOffset);
            attributeNumber++;
            if ((codeStream instanceof StackMapFrameCodeStream) && needFieldInitializations && declaringType.fields != null) {
                ((StackMapFrameCodeStream) codeStream).resetSecretLocals();
            }
        }
        classFile.completeMethodInfo(methodAttributeOffset, attributeNumber);
    }

    public boolean isConstructor() {
        return true;
    }

    public boolean isDefaultConstructor() {
        return (this.bits & ASTNode.IsDefaultConstructor) != 0;
    }

    public boolean isInitializationMethod() {
        return true;
    }

    public boolean isRecursive(ArrayList visited) {
        if (this.binding == null || this.constructorCall == null || this.constructorCall.binding == null || this.constructorCall.isSuperAccess() || !this.constructorCall.binding.isValidBinding()) {
            return false;
        }
        ConstructorDeclaration targetConstructor = ((ConstructorDeclaration) this.scope.referenceType().declarationOf(this.constructorCall.binding.original()));
        if (this == targetConstructor) return true;
        if (visited == null) {
            visited = new ArrayList(1);
        } else {
            int index = visited.indexOf(this);
            if (index >= 0) return index == 0;
        }
        visited.add(this);
        return targetConstructor.isRecursive(visited);
    }

    public void parseStatements(Parser parser, CompilationUnitDeclaration unit) {
        if (((this.bits & ASTNode.IsDefaultConstructor) != 0) && this.constructorCall == null) {
            this.constructorCall = SuperReference.implicitSuperConstructorCall();
            this.constructorCall.sourceStart = this.sourceStart;
            this.constructorCall.sourceEnd = this.sourceEnd;
            return;
        }
        parser.parse(this, unit, false);
    }

    public StringBuffer printBody(int indent, StringBuffer output) {
        output.append(" {");
        if (this.constructorCall != null) {
            output.append('\n');
            this.constructorCall.printStatement(indent, output);
        }
        if (this.statements != null) {
            for (int i = 0; i < this.statements.length; i++) {
                output.append('\n');
                this.statements[i].printStatement(indent, output);
            }
        }
        output.append('\n');
        printIndent(indent == 0 ? 0 : indent - 1, output).append('}');
        return output;
    }

    public void resolveJavadoc() {
        if (this.binding == null || this.javadoc != null) {
            super.resolveJavadoc();
        } else if ((this.bits & ASTNode.IsDefaultConstructor) == 0) {
            if (this.binding.declaringClass != null && !this.binding.declaringClass.isLocalType()) {
                int javadocVisibility = this.binding.modifiers & ExtraCompilerModifiers.AccVisibilityMASK;
                ClassScope classScope = this.scope.classScope();
                ProblemReporter reporter = this.scope.problemReporter();
                int severity = reporter.computeSeverity(IProblem.JavadocMissing);
                if (severity != ProblemSeverities.Ignore) {
                    if (classScope != null) {
                        javadocVisibility = Util.computeOuterMostVisibility(classScope.referenceType(), javadocVisibility);
                    }
                    int javadocModifiers = (this.binding.modifiers & ~ExtraCompilerModifiers.AccVisibilityMASK) | javadocVisibility;
                    reporter.javadocMissing(this.sourceStart, this.sourceEnd, severity, javadocModifiers);
                }
            }
        }
    }

    public void resolveStatements() {
        SourceTypeBinding sourceType = this.scope.enclosingSourceType();
        if (!CharOperation.equals(sourceType.sourceName, this.selector)) {
            this.scope.problemReporter().missingReturnType(this);
        }
        if (this.typeParameters != null) {
            for (int i = 0, length = this.typeParameters.length; i < length; i++) {
                this.typeParameters[i].resolve(this.scope);
            }
        }
        if (this.binding != null && !this.binding.isPrivate()) {
            sourceType.tagBits |= TagBits.HasNonPrivateConstructor;
        }
        if (this.constructorCall != null) {
            if (sourceType.id == TypeIds.T_JavaLangObject && this.constructorCall.accessMode != ExplicitConstructorCall.This) {
                if (this.constructorCall.accessMode == ExplicitConstructorCall.Super) {
                    this.scope.problemReporter().cannotUseSuperInJavaLangObject(this.constructorCall);
                }
                this.constructorCall = null;
            } else {
                this.constructorCall.resolve(this.scope);
            }
        }
        if ((this.modifiers & ExtraCompilerModifiers.AccSemicolonBody) != 0) {
            this.scope.problemReporter().methodNeedBody(this);
        }
        super.resolveStatements();
    }

    public void traverse(ASTVisitor visitor, ClassScope classScope) {
        if (visitor.visit(this, classScope)) {
            if (this.javadoc != null) {
                this.javadoc.traverse(visitor, this.scope);
            }
            if (this.annotations != null) {
                int annotationsLength = this.annotations.length;
                for (int i = 0; i < annotationsLength; i++) this.annotations[i].traverse(visitor, this.scope);
            }
            if (this.typeParameters != null) {
                int typeParametersLength = this.typeParameters.length;
                for (int i = 0; i < typeParametersLength; i++) {
                    this.typeParameters[i].traverse(visitor, this.scope);
                }
            }
            if (this.arguments != null) {
                int argumentLength = this.arguments.length;
                for (int i = 0; i < argumentLength; i++) this.arguments[i].traverse(visitor, this.scope);
            }
            if (this.thrownExceptions != null) {
                int thrownExceptionsLength = this.thrownExceptions.length;
                for (int i = 0; i < thrownExceptionsLength; i++) this.thrownExceptions[i].traverse(visitor, this.scope);
            }
            if (this.constructorCall != null) this.constructorCall.traverse(visitor, this.scope);
            if (this.statements != null) {
                int statementsLength = this.statements.length;
                for (int i = 0; i < statementsLength; i++) this.statements[i].traverse(visitor, this.scope);
            }
        }
        visitor.endVisit(this, classScope);
    }

    public TypeParameter[] typeParameters() {
        return this.typeParameters;
    }
}
