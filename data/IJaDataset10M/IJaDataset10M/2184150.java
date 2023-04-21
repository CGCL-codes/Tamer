package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.*;

public class TryStatement extends SubRoutineStatement {

    private static final char[] SECRET_RETURN_ADDRESS_NAME = " returnAddress".toCharArray();

    private static final char[] SECRET_ANY_HANDLER_NAME = " anyExceptionHandler".toCharArray();

    private static final char[] SECRET_RETURN_VALUE_NAME = " returnValue".toCharArray();

    public Block tryBlock;

    public Block[] catchBlocks;

    public Argument[] catchArguments;

    public Block finallyBlock;

    BlockScope scope;

    public UnconditionalFlowInfo subRoutineInits;

    ReferenceBinding[] caughtExceptionTypes;

    boolean[] catchExits;

    BranchLabel subRoutineStartLabel;

    public LocalVariableBinding anyExceptionVariable, returnAddressVariable, secretReturnValue;

    ExceptionLabel[] declaredExceptionLabels;

    private Object[] reusableJSRTargets;

    private BranchLabel[] reusableJSRSequenceStartLabels;

    private int[] reusableJSRStateIndexes;

    private int reusableJSRTargetsCount = 0;

    private static final int NO_FINALLY = 0;

    private static final int FINALLY_SUBROUTINE = 1;

    private static final int FINALLY_DOES_NOT_COMPLETE = 2;

    private static final int FINALLY_INLINE = 3;

    int mergedInitStateIndex = -1;

    int preTryInitStateIndex = -1;

    int naturalExitMergeInitStateIndex = -1;

    int[] catchExitInitStateIndexes;

    public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
        this.preTryInitStateIndex = currentScope.methodScope().recordInitializationStates(flowInfo);
        if (this.anyExceptionVariable != null) {
            this.anyExceptionVariable.useFlag = LocalVariableBinding.USED;
        }
        if (this.returnAddressVariable != null) {
            this.returnAddressVariable.useFlag = LocalVariableBinding.USED;
        }
        if (this.subRoutineStartLabel == null) {
            ExceptionHandlingFlowContext handlingContext = new ExceptionHandlingFlowContext(flowContext, this, this.caughtExceptionTypes, null, this.scope, flowInfo.unconditionalInits());
            handlingContext.initsOnFinally = new NullInfoRegistry(flowInfo.unconditionalInits());
            FlowInfo tryInfo;
            if (this.tryBlock.isEmptyBlock()) {
                tryInfo = flowInfo;
            } else {
                tryInfo = this.tryBlock.analyseCode(currentScope, handlingContext, flowInfo.copy());
                if ((tryInfo.tagBits & FlowInfo.UNREACHABLE) != 0) this.bits |= ASTNode.IsTryBlockExiting;
            }
            handlingContext.complainIfUnusedExceptionHandlers(this.scope, this);
            if (this.catchArguments != null) {
                int catchCount;
                this.catchExits = new boolean[catchCount = this.catchBlocks.length];
                this.catchExitInitStateIndexes = new int[catchCount];
                for (int i = 0; i < catchCount; i++) {
                    FlowInfo catchInfo;
                    if (this.caughtExceptionTypes[i].isUncheckedException(true)) {
                        catchInfo = handlingContext.initsOnFinally.mitigateNullInfoOf(flowInfo.unconditionalCopy().addPotentialInitializationsFrom(handlingContext.initsOnException(this.caughtExceptionTypes[i])).addPotentialInitializationsFrom(tryInfo).addPotentialInitializationsFrom(handlingContext.initsOnReturn));
                    } else {
                        catchInfo = flowInfo.unconditionalCopy().addPotentialInitializationsFrom(handlingContext.initsOnException(this.caughtExceptionTypes[i])).addPotentialInitializationsFrom(tryInfo.nullInfoLessUnconditionalCopy()).addPotentialInitializationsFrom(handlingContext.initsOnReturn.nullInfoLessUnconditionalCopy());
                    }
                    LocalVariableBinding catchArg = this.catchArguments[i].binding;
                    catchInfo.markAsDefinitelyAssigned(catchArg);
                    catchInfo.markAsDefinitelyNonNull(catchArg);
                    if (this.tryBlock.statements == null) {
                        catchInfo.setReachMode(FlowInfo.UNREACHABLE);
                    }
                    catchInfo = this.catchBlocks[i].analyseCode(currentScope, flowContext, catchInfo);
                    this.catchExitInitStateIndexes[i] = currentScope.methodScope().recordInitializationStates(catchInfo);
                    this.catchExits[i] = (catchInfo.tagBits & FlowInfo.UNREACHABLE) != 0;
                    tryInfo = tryInfo.mergedWith(catchInfo.unconditionalInits());
                }
            }
            this.mergedInitStateIndex = currentScope.methodScope().recordInitializationStates(tryInfo);
            if (flowContext.initsOnFinally != null) {
                flowContext.initsOnFinally.add(handlingContext.initsOnFinally);
            }
            return tryInfo;
        } else {
            InsideSubRoutineFlowContext insideSubContext;
            FinallyFlowContext finallyContext;
            UnconditionalFlowInfo subInfo;
            insideSubContext = new InsideSubRoutineFlowContext(flowContext, this);
            subInfo = this.finallyBlock.analyseCode(currentScope, finallyContext = new FinallyFlowContext(flowContext, this.finallyBlock), flowInfo.nullInfoLessUnconditionalCopy()).unconditionalInits();
            if (subInfo == FlowInfo.DEAD_END) {
                this.bits |= ASTNode.IsSubRoutineEscaping;
                this.scope.problemReporter().finallyMustCompleteNormally(this.finallyBlock);
            }
            this.subRoutineInits = subInfo;
            ExceptionHandlingFlowContext handlingContext = new ExceptionHandlingFlowContext(insideSubContext, this, this.caughtExceptionTypes, null, this.scope, flowInfo.unconditionalInits());
            handlingContext.initsOnFinally = new NullInfoRegistry(flowInfo.unconditionalInits());
            FlowInfo tryInfo;
            if (this.tryBlock.isEmptyBlock()) {
                tryInfo = flowInfo;
            } else {
                tryInfo = this.tryBlock.analyseCode(currentScope, handlingContext, flowInfo.copy());
                if ((tryInfo.tagBits & FlowInfo.UNREACHABLE) != 0) this.bits |= ASTNode.IsTryBlockExiting;
            }
            handlingContext.complainIfUnusedExceptionHandlers(this.scope, this);
            if (this.catchArguments != null) {
                int catchCount;
                this.catchExits = new boolean[catchCount = this.catchBlocks.length];
                this.catchExitInitStateIndexes = new int[catchCount];
                for (int i = 0; i < catchCount; i++) {
                    FlowInfo catchInfo;
                    if (this.caughtExceptionTypes[i].isUncheckedException(true)) {
                        catchInfo = handlingContext.initsOnFinally.mitigateNullInfoOf(flowInfo.unconditionalCopy().addPotentialInitializationsFrom(handlingContext.initsOnException(this.caughtExceptionTypes[i])).addPotentialInitializationsFrom(tryInfo).addPotentialInitializationsFrom(handlingContext.initsOnReturn));
                    } else {
                        catchInfo = flowInfo.unconditionalCopy().addPotentialInitializationsFrom(handlingContext.initsOnException(this.caughtExceptionTypes[i])).addPotentialInitializationsFrom(tryInfo.nullInfoLessUnconditionalCopy()).addPotentialInitializationsFrom(handlingContext.initsOnReturn.nullInfoLessUnconditionalCopy());
                    }
                    LocalVariableBinding catchArg = this.catchArguments[i].binding;
                    catchInfo.markAsDefinitelyAssigned(catchArg);
                    catchInfo.markAsDefinitelyNonNull(catchArg);
                    if (this.tryBlock.statements == null) {
                        catchInfo.setReachMode(FlowInfo.UNREACHABLE);
                    }
                    catchInfo = this.catchBlocks[i].analyseCode(currentScope, insideSubContext, catchInfo);
                    this.catchExitInitStateIndexes[i] = currentScope.methodScope().recordInitializationStates(catchInfo);
                    this.catchExits[i] = (catchInfo.tagBits & FlowInfo.UNREACHABLE) != 0;
                    tryInfo = tryInfo.mergedWith(catchInfo.unconditionalInits());
                }
            }
            finallyContext.complainOnDeferredChecks(handlingContext.initsOnFinally.mitigateNullInfoOf((tryInfo.tagBits & FlowInfo.UNREACHABLE) == 0 ? flowInfo.unconditionalCopy().addPotentialInitializationsFrom(tryInfo).addPotentialInitializationsFrom(insideSubContext.initsOnReturn) : insideSubContext.initsOnReturn), currentScope);
            if (flowContext.initsOnFinally != null) {
                flowContext.initsOnFinally.add(handlingContext.initsOnFinally);
            }
            this.naturalExitMergeInitStateIndex = currentScope.methodScope().recordInitializationStates(tryInfo);
            if (subInfo == FlowInfo.DEAD_END) {
                this.mergedInitStateIndex = currentScope.methodScope().recordInitializationStates(subInfo);
                return subInfo;
            } else {
                FlowInfo mergedInfo = tryInfo.addInitializationsFrom(subInfo);
                this.mergedInitStateIndex = currentScope.methodScope().recordInitializationStates(mergedInfo);
                return mergedInfo;
            }
        }
    }

    public ExceptionLabel enterAnyExceptionHandler(CodeStream codeStream) {
        if (this.subRoutineStartLabel == null) return null;
        return super.enterAnyExceptionHandler(codeStream);
    }

    public void enterDeclaredExceptionHandlers(CodeStream codeStream) {
        for (int i = 0, length = this.declaredExceptionLabels == null ? 0 : this.declaredExceptionLabels.length; i < length; i++) {
            this.declaredExceptionLabels[i].placeStart();
        }
    }

    public void exitAnyExceptionHandler() {
        if (this.subRoutineStartLabel == null) return;
        super.exitAnyExceptionHandler();
    }

    public void exitDeclaredExceptionHandlers(CodeStream codeStream) {
        for (int i = 0, length = this.declaredExceptionLabels == null ? 0 : this.declaredExceptionLabels.length; i < length; i++) {
            this.declaredExceptionLabels[i].placeEnd();
        }
    }

    private int finallyMode() {
        if (this.subRoutineStartLabel == null) {
            return NO_FINALLY;
        } else if (isSubRoutineEscaping()) {
            return FINALLY_DOES_NOT_COMPLETE;
        } else if (this.scope.compilerOptions().inlineJsrBytecode) {
            return FINALLY_INLINE;
        } else {
            return FINALLY_SUBROUTINE;
        }
    }

    /**
 * Try statement code generation with or without jsr bytecode use
 *	post 1.5 target level, cannot use jsr bytecode, must instead inline finally block
 * returnAddress is only allocated if jsr is allowed
 */
    public void generateCode(BlockScope currentScope, CodeStream codeStream) {
        if ((this.bits & ASTNode.IsReachable) == 0) {
            return;
        }
        boolean isStackMapFrameCodeStream = codeStream instanceof StackMapFrameCodeStream;
        this.anyExceptionLabel = null;
        this.reusableJSRTargets = null;
        this.reusableJSRSequenceStartLabels = null;
        this.reusableJSRTargetsCount = 0;
        int pc = codeStream.position;
        int finallyMode = finallyMode();
        boolean requiresNaturalExit = false;
        int maxCatches = this.catchArguments == null ? 0 : this.catchArguments.length;
        ExceptionLabel[] exceptionLabels;
        if (maxCatches > 0) {
            exceptionLabels = new ExceptionLabel[maxCatches];
            for (int i = 0; i < maxCatches; i++) {
                ExceptionLabel exceptionLabel = new ExceptionLabel(codeStream, this.catchArguments[i].binding.type);
                exceptionLabel.placeStart();
                exceptionLabels[i] = exceptionLabel;
            }
        } else {
            exceptionLabels = null;
        }
        if (this.subRoutineStartLabel != null) {
            this.subRoutineStartLabel.initialize(codeStream);
            enterAnyExceptionHandler(codeStream);
        }
        try {
            this.declaredExceptionLabels = exceptionLabels;
            this.tryBlock.generateCode(this.scope, codeStream);
        } finally {
            this.declaredExceptionLabels = null;
        }
        boolean tryBlockHasSomeCode = codeStream.position != pc;
        if (tryBlockHasSomeCode) {
            BranchLabel naturalExitLabel = new BranchLabel(codeStream);
            BranchLabel postCatchesFinallyLabel = null;
            for (int i = 0; i < maxCatches; i++) {
                exceptionLabels[i].placeEnd();
            }
            if ((this.bits & ASTNode.IsTryBlockExiting) == 0) {
                int position = codeStream.position;
                switch(finallyMode) {
                    case FINALLY_SUBROUTINE:
                    case FINALLY_INLINE:
                        requiresNaturalExit = true;
                        if (this.naturalExitMergeInitStateIndex != -1) {
                            codeStream.removeNotDefinitelyAssignedVariables(currentScope, this.naturalExitMergeInitStateIndex);
                            codeStream.addDefinitelyAssignedVariables(currentScope, this.naturalExitMergeInitStateIndex);
                        }
                        codeStream.goto_(naturalExitLabel);
                        break;
                    case NO_FINALLY:
                        if (this.naturalExitMergeInitStateIndex != -1) {
                            codeStream.removeNotDefinitelyAssignedVariables(currentScope, this.naturalExitMergeInitStateIndex);
                            codeStream.addDefinitelyAssignedVariables(currentScope, this.naturalExitMergeInitStateIndex);
                        }
                        codeStream.goto_(naturalExitLabel);
                        break;
                    case FINALLY_DOES_NOT_COMPLETE:
                        codeStream.goto_(this.subRoutineStartLabel);
                        break;
                }
                codeStream.updateLastRecordedEndPC(this.tryBlock.scope, position);
            }
            exitAnyExceptionHandler();
            if (this.catchArguments != null) {
                postCatchesFinallyLabel = new BranchLabel(codeStream);
                for (int i = 0; i < maxCatches; i++) {
                    if (exceptionLabels[i].count == 0) continue;
                    enterAnyExceptionHandler(codeStream);
                    if (this.preTryInitStateIndex != -1) {
                        codeStream.removeNotDefinitelyAssignedVariables(currentScope, this.preTryInitStateIndex);
                        codeStream.addDefinitelyAssignedVariables(currentScope, this.preTryInitStateIndex);
                    }
                    codeStream.pushExceptionOnStack(exceptionLabels[i].exceptionType);
                    exceptionLabels[i].place();
                    LocalVariableBinding catchVar;
                    int varPC = codeStream.position;
                    if ((catchVar = this.catchArguments[i].binding).resolvedPosition != -1) {
                        codeStream.store(catchVar, false);
                        catchVar.recordInitializationStartPC(codeStream.position);
                        codeStream.addVisibleLocalVariable(catchVar);
                    } else {
                        codeStream.pop();
                    }
                    codeStream.recordPositionsFrom(varPC, this.catchArguments[i].sourceStart);
                    this.catchBlocks[i].generateCode(this.scope, codeStream);
                    exitAnyExceptionHandler();
                    if (!this.catchExits[i]) {
                        switch(finallyMode) {
                            case FINALLY_INLINE:
                                if (isStackMapFrameCodeStream) {
                                    ((StackMapFrameCodeStream) codeStream).pushStateIndex(this.naturalExitMergeInitStateIndex);
                                }
                                if (this.catchExitInitStateIndexes[i] != -1) {
                                    codeStream.removeNotDefinitelyAssignedVariables(currentScope, this.catchExitInitStateIndexes[i]);
                                    codeStream.addDefinitelyAssignedVariables(currentScope, this.catchExitInitStateIndexes[i]);
                                }
                                this.finallyBlock.generateCode(this.scope, codeStream);
                                codeStream.goto_(postCatchesFinallyLabel);
                                if (isStackMapFrameCodeStream) {
                                    ((StackMapFrameCodeStream) codeStream).popStateIndex();
                                }
                                break;
                            case FINALLY_SUBROUTINE:
                                requiresNaturalExit = true;
                            case NO_FINALLY:
                                if (this.naturalExitMergeInitStateIndex != -1) {
                                    codeStream.removeNotDefinitelyAssignedVariables(currentScope, this.naturalExitMergeInitStateIndex);
                                    codeStream.addDefinitelyAssignedVariables(currentScope, this.naturalExitMergeInitStateIndex);
                                }
                                codeStream.goto_(naturalExitLabel);
                                break;
                            case FINALLY_DOES_NOT_COMPLETE:
                                codeStream.goto_(this.subRoutineStartLabel);
                                break;
                        }
                    }
                }
            }
            ExceptionLabel naturalExitExceptionHandler = requiresNaturalExit && (finallyMode == FINALLY_SUBROUTINE) ? new ExceptionLabel(codeStream, null) : null;
            int finallySequenceStartPC = codeStream.position;
            if (this.subRoutineStartLabel != null && this.anyExceptionLabel.count != 0) {
                codeStream.pushExceptionOnStack(this.scope.getJavaLangThrowable());
                if (this.preTryInitStateIndex != -1) {
                    codeStream.removeNotDefinitelyAssignedVariables(currentScope, this.preTryInitStateIndex);
                    codeStream.addDefinitelyAssignedVariables(currentScope, this.preTryInitStateIndex);
                }
                placeAllAnyExceptionHandler();
                if (naturalExitExceptionHandler != null) naturalExitExceptionHandler.place();
                switch(finallyMode) {
                    case FINALLY_SUBROUTINE:
                        codeStream.store(this.anyExceptionVariable, false);
                        codeStream.jsr(this.subRoutineStartLabel);
                        codeStream.recordPositionsFrom(finallySequenceStartPC, this.finallyBlock.sourceStart);
                        int position = codeStream.position;
                        codeStream.throwAnyException(this.anyExceptionVariable);
                        codeStream.recordPositionsFrom(position, this.finallyBlock.sourceEnd);
                        this.subRoutineStartLabel.place();
                        codeStream.pushExceptionOnStack(this.scope.getJavaLangThrowable());
                        position = codeStream.position;
                        codeStream.store(this.returnAddressVariable, false);
                        codeStream.recordPositionsFrom(position, this.finallyBlock.sourceStart);
                        this.finallyBlock.generateCode(this.scope, codeStream);
                        position = codeStream.position;
                        codeStream.ret(this.returnAddressVariable.resolvedPosition);
                        codeStream.recordPositionsFrom(position, this.finallyBlock.sourceEnd);
                        break;
                    case FINALLY_INLINE:
                        codeStream.store(this.anyExceptionVariable, false);
                        codeStream.addVariable(this.anyExceptionVariable);
                        codeStream.recordPositionsFrom(finallySequenceStartPC, this.finallyBlock.sourceStart);
                        this.finallyBlock.generateCode(currentScope, codeStream);
                        position = codeStream.position;
                        codeStream.throwAnyException(this.anyExceptionVariable);
                        codeStream.removeVariable(this.anyExceptionVariable);
                        if (this.preTryInitStateIndex != -1) {
                            codeStream.removeNotDefinitelyAssignedVariables(currentScope, this.preTryInitStateIndex);
                        }
                        this.subRoutineStartLabel.place();
                        codeStream.recordPositionsFrom(position, this.finallyBlock.sourceEnd);
                        break;
                    case FINALLY_DOES_NOT_COMPLETE:
                        codeStream.pop();
                        this.subRoutineStartLabel.place();
                        codeStream.recordPositionsFrom(finallySequenceStartPC, this.finallyBlock.sourceStart);
                        this.finallyBlock.generateCode(this.scope, codeStream);
                        break;
                }
                if (requiresNaturalExit) {
                    switch(finallyMode) {
                        case FINALLY_SUBROUTINE:
                            naturalExitLabel.place();
                            int position = codeStream.position;
                            naturalExitExceptionHandler.placeStart();
                            codeStream.jsr(this.subRoutineStartLabel);
                            naturalExitExceptionHandler.placeEnd();
                            codeStream.recordPositionsFrom(position, this.finallyBlock.sourceEnd);
                            break;
                        case FINALLY_INLINE:
                            if (isStackMapFrameCodeStream) {
                                ((StackMapFrameCodeStream) codeStream).pushStateIndex(this.naturalExitMergeInitStateIndex);
                            }
                            if (this.naturalExitMergeInitStateIndex != -1) {
                                codeStream.removeNotDefinitelyAssignedVariables(currentScope, this.naturalExitMergeInitStateIndex);
                                codeStream.addDefinitelyAssignedVariables(currentScope, this.naturalExitMergeInitStateIndex);
                            }
                            naturalExitLabel.place();
                            this.finallyBlock.generateCode(this.scope, codeStream);
                            if (postCatchesFinallyLabel != null) {
                                position = codeStream.position;
                                codeStream.goto_(postCatchesFinallyLabel);
                                codeStream.recordPositionsFrom(position, this.finallyBlock.sourceEnd);
                            }
                            if (isStackMapFrameCodeStream) {
                                ((StackMapFrameCodeStream) codeStream).popStateIndex();
                            }
                            break;
                        case FINALLY_DOES_NOT_COMPLETE:
                            break;
                        default:
                            naturalExitLabel.place();
                            break;
                    }
                }
                if (postCatchesFinallyLabel != null) {
                    postCatchesFinallyLabel.place();
                }
            } else {
                naturalExitLabel.place();
            }
        } else {
            if (this.subRoutineStartLabel != null) {
                this.finallyBlock.generateCode(this.scope, codeStream);
            }
        }
        if (this.mergedInitStateIndex != -1) {
            codeStream.removeNotDefinitelyAssignedVariables(currentScope, this.mergedInitStateIndex);
            codeStream.addDefinitelyAssignedVariables(currentScope, this.mergedInitStateIndex);
        }
        codeStream.recordPositionsFrom(pc, this.sourceStart);
    }

    /**
 * @see SubRoutineStatement#generateSubRoutineInvocation(BlockScope, CodeStream, Object, int, LocalVariableBinding)
 */
    public boolean generateSubRoutineInvocation(BlockScope currentScope, CodeStream codeStream, Object targetLocation, int stateIndex, LocalVariableBinding secretLocal) {
        boolean isStackMapFrameCodeStream = codeStream instanceof StackMapFrameCodeStream;
        int finallyMode = finallyMode();
        switch(finallyMode) {
            case FINALLY_DOES_NOT_COMPLETE:
                codeStream.goto_(this.subRoutineStartLabel);
                return true;
            case NO_FINALLY:
                exitDeclaredExceptionHandlers(codeStream);
                return false;
        }
        if (targetLocation != null) {
            boolean reuseTargetLocation = true;
            if (this.reusableJSRTargetsCount > 0) {
                nextReusableTarget: for (int i = 0, count = this.reusableJSRTargetsCount; i < count; i++) {
                    Object reusableJSRTarget = this.reusableJSRTargets[i];
                    differentTarget: {
                        if (targetLocation == reusableJSRTarget) break differentTarget;
                        if (targetLocation instanceof Constant && reusableJSRTarget instanceof Constant && ((Constant) targetLocation).hasSameValue((Constant) reusableJSRTarget)) {
                            break differentTarget;
                        }
                        continue nextReusableTarget;
                    }
                    if ((this.reusableJSRStateIndexes[i] != stateIndex) && finallyMode == FINALLY_INLINE) {
                        reuseTargetLocation = false;
                        break nextReusableTarget;
                    } else {
                        codeStream.goto_(this.reusableJSRSequenceStartLabels[i]);
                        return true;
                    }
                }
            } else {
                this.reusableJSRTargets = new Object[3];
                this.reusableJSRSequenceStartLabels = new BranchLabel[3];
                this.reusableJSRStateIndexes = new int[3];
            }
            if (reuseTargetLocation) {
                if (this.reusableJSRTargetsCount == this.reusableJSRTargets.length) {
                    System.arraycopy(this.reusableJSRTargets, 0, this.reusableJSRTargets = new Object[2 * this.reusableJSRTargetsCount], 0, this.reusableJSRTargetsCount);
                    System.arraycopy(this.reusableJSRSequenceStartLabels, 0, this.reusableJSRSequenceStartLabels = new BranchLabel[2 * this.reusableJSRTargetsCount], 0, this.reusableJSRTargetsCount);
                    System.arraycopy(this.reusableJSRStateIndexes, 0, this.reusableJSRStateIndexes = new int[2 * this.reusableJSRTargetsCount], 0, this.reusableJSRTargetsCount);
                }
                this.reusableJSRTargets[this.reusableJSRTargetsCount] = targetLocation;
                BranchLabel reusableJSRSequenceStartLabel = new BranchLabel(codeStream);
                reusableJSRSequenceStartLabel.place();
                this.reusableJSRStateIndexes[this.reusableJSRTargetsCount] = stateIndex;
                this.reusableJSRSequenceStartLabels[this.reusableJSRTargetsCount++] = reusableJSRSequenceStartLabel;
            }
        }
        if (finallyMode == FINALLY_INLINE) {
            if (isStackMapFrameCodeStream) {
                ((StackMapFrameCodeStream) codeStream).pushStateIndex(stateIndex);
                if (this.naturalExitMergeInitStateIndex != -1 || stateIndex != -1) {
                    codeStream.removeNotDefinitelyAssignedVariables(currentScope, this.naturalExitMergeInitStateIndex);
                    codeStream.addDefinitelyAssignedVariables(currentScope, this.naturalExitMergeInitStateIndex);
                }
            } else {
                if (this.naturalExitMergeInitStateIndex != -1) {
                    codeStream.removeNotDefinitelyAssignedVariables(currentScope, this.naturalExitMergeInitStateIndex);
                    codeStream.addDefinitelyAssignedVariables(currentScope, this.naturalExitMergeInitStateIndex);
                }
            }
            if (secretLocal != null) {
                codeStream.addVariable(secretLocal);
            }
            exitAnyExceptionHandler();
            exitDeclaredExceptionHandlers(codeStream);
            this.finallyBlock.generateCode(currentScope, codeStream);
            if (isStackMapFrameCodeStream) {
                ((StackMapFrameCodeStream) codeStream).popStateIndex();
            }
        } else {
            codeStream.jsr(this.subRoutineStartLabel);
            exitAnyExceptionHandler();
            exitDeclaredExceptionHandlers(codeStream);
        }
        return false;
    }

    public boolean isSubRoutineEscaping() {
        return (this.bits & ASTNode.IsSubRoutineEscaping) != 0;
    }

    public StringBuffer printStatement(int indent, StringBuffer output) {
        printIndent(indent, output).append("try \n");
        this.tryBlock.printStatement(indent + 1, output);
        if (this.catchBlocks != null) for (int i = 0; i < this.catchBlocks.length; i++) {
            output.append('\n');
            printIndent(indent, output).append("catch (");
            this.catchArguments[i].print(0, output).append(") ");
            this.catchBlocks[i].printStatement(indent + 1, output);
        }
        if (this.finallyBlock != null) {
            output.append('\n');
            printIndent(indent, output).append("finally\n");
            this.finallyBlock.printStatement(indent + 1, output);
        }
        return output;
    }

    public void resolve(BlockScope upperScope) {
        this.scope = new BlockScope(upperScope);
        BlockScope tryScope = new BlockScope(this.scope);
        BlockScope finallyScope = null;
        if (this.finallyBlock != null) {
            if (this.finallyBlock.isEmptyBlock()) {
                if ((this.finallyBlock.bits & ASTNode.UndocumentedEmptyBlock) != 0) {
                    this.scope.problemReporter().undocumentedEmptyBlock(this.finallyBlock.sourceStart, this.finallyBlock.sourceEnd);
                }
            } else {
                finallyScope = new BlockScope(this.scope, false);
                MethodScope methodScope = this.scope.methodScope();
                if (!upperScope.compilerOptions().inlineJsrBytecode) {
                    this.returnAddressVariable = new LocalVariableBinding(TryStatement.SECRET_RETURN_ADDRESS_NAME, upperScope.getJavaLangObject(), ClassFileConstants.AccDefault, false);
                    finallyScope.addLocalVariable(this.returnAddressVariable);
                    this.returnAddressVariable.setConstant(Constant.NotAConstant);
                }
                this.subRoutineStartLabel = new BranchLabel();
                this.anyExceptionVariable = new LocalVariableBinding(TryStatement.SECRET_ANY_HANDLER_NAME, this.scope.getJavaLangThrowable(), ClassFileConstants.AccDefault, false);
                finallyScope.addLocalVariable(this.anyExceptionVariable);
                this.anyExceptionVariable.setConstant(Constant.NotAConstant);
                if (!methodScope.isInsideInitializer()) {
                    MethodBinding methodBinding = ((AbstractMethodDeclaration) methodScope.referenceContext).binding;
                    if (methodBinding != null) {
                        TypeBinding methodReturnType = methodBinding.returnType;
                        if (methodReturnType.id != TypeIds.T_void) {
                            this.secretReturnValue = new LocalVariableBinding(TryStatement.SECRET_RETURN_VALUE_NAME, methodReturnType, ClassFileConstants.AccDefault, false);
                            finallyScope.addLocalVariable(this.secretReturnValue);
                            this.secretReturnValue.setConstant(Constant.NotAConstant);
                        }
                    }
                }
                this.finallyBlock.resolveUsing(finallyScope);
                finallyScope.shiftScopes = new BlockScope[this.catchArguments == null ? 1 : this.catchArguments.length + 1];
                finallyScope.shiftScopes[0] = tryScope;
            }
        }
        this.tryBlock.resolveUsing(tryScope);
        if (this.catchBlocks != null) {
            int length = this.catchArguments.length;
            TypeBinding[] argumentTypes = new TypeBinding[length];
            boolean catchHasError = false;
            for (int i = 0; i < length; i++) {
                BlockScope catchScope = new BlockScope(this.scope);
                if (finallyScope != null) {
                    finallyScope.shiftScopes[i + 1] = catchScope;
                }
                if ((argumentTypes[i] = this.catchArguments[i].resolveForCatch(catchScope)) == null) {
                    catchHasError = true;
                }
                this.catchBlocks[i].resolveUsing(catchScope);
            }
            if (catchHasError) {
                return;
            }
            this.caughtExceptionTypes = new ReferenceBinding[length];
            for (int i = 0; i < length; i++) {
                this.caughtExceptionTypes[i] = (ReferenceBinding) argumentTypes[i];
                for (int j = 0; j < i; j++) {
                    if (this.caughtExceptionTypes[i].isCompatibleWith(argumentTypes[j])) {
                        this.scope.problemReporter().wrongSequenceOfExceptionTypesError(this, this.caughtExceptionTypes[i], i, argumentTypes[j]);
                    }
                }
            }
        } else {
            this.caughtExceptionTypes = new ReferenceBinding[0];
        }
        if (finallyScope != null) {
            this.scope.addSubscope(finallyScope);
        }
    }

    public void traverse(ASTVisitor visitor, BlockScope blockScope) {
        if (visitor.visit(this, blockScope)) {
            this.tryBlock.traverse(visitor, this.scope);
            if (this.catchArguments != null) {
                for (int i = 0, max = this.catchBlocks.length; i < max; i++) {
                    this.catchArguments[i].traverse(visitor, this.scope);
                    this.catchBlocks[i].traverse(visitor, this.scope);
                }
            }
            if (this.finallyBlock != null) this.finallyBlock.traverse(visitor, this.scope);
        }
        visitor.endVisit(this, blockScope);
    }
}
