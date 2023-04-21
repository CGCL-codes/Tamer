package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

public class UnaryExpression extends OperatorExpression {

    public Expression expression;

    public Constant optimizedBooleanConstant;

    public UnaryExpression(Expression expression, int operator) {
        this.expression = expression;
        this.bits |= operator << OperatorSHIFT;
    }

    public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
        this.expression.checkNPE(currentScope, flowContext, flowInfo);
        if (((this.bits & OperatorMASK) >> OperatorSHIFT) == NOT) {
            return this.expression.analyseCode(currentScope, flowContext, flowInfo).asNegatedCondition();
        } else {
            return this.expression.analyseCode(currentScope, flowContext, flowInfo);
        }
    }

    public Constant optimizedBooleanConstant() {
        return this.optimizedBooleanConstant == null ? this.constant : this.optimizedBooleanConstant;
    }

    /**
	 * Code generation for an unary operation
	 *
	 * @param currentScope org.eclipse.jdt.internal.compiler.lookup.BlockScope
	 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
	 * @param valueRequired boolean
	 */
    public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {
        int pc = codeStream.position;
        BranchLabel falseLabel, endifLabel;
        if (this.constant != Constant.NotAConstant) {
            if (valueRequired) {
                codeStream.generateConstant(this.constant, this.implicitConversion);
            }
            codeStream.recordPositionsFrom(pc, this.sourceStart);
            return;
        }
        switch((this.bits & OperatorMASK) >> OperatorSHIFT) {
            case NOT:
                switch((this.expression.implicitConversion & IMPLICIT_CONVERSION_MASK) >> 4) {
                    case T_boolean:
                        this.expression.generateOptimizedBoolean(currentScope, codeStream, null, (falseLabel = new BranchLabel(codeStream)), valueRequired);
                        if (valueRequired) {
                            codeStream.iconst_0();
                            if (falseLabel.forwardReferenceCount() > 0) {
                                codeStream.goto_(endifLabel = new BranchLabel(codeStream));
                                codeStream.decrStackSize(1);
                                falseLabel.place();
                                codeStream.iconst_1();
                                endifLabel.place();
                            }
                        } else {
                            falseLabel.place();
                        }
                        break;
                }
                break;
            case TWIDDLE:
                switch((this.expression.implicitConversion & IMPLICIT_CONVERSION_MASK) >> 4) {
                    case T_int:
                        this.expression.generateCode(currentScope, codeStream, valueRequired);
                        if (valueRequired) {
                            codeStream.iconst_m1();
                            codeStream.ixor();
                        }
                        break;
                    case T_long:
                        this.expression.generateCode(currentScope, codeStream, valueRequired);
                        if (valueRequired) {
                            codeStream.ldc2_w(-1L);
                            codeStream.lxor();
                        }
                }
                break;
            case MINUS:
                if (this.constant != Constant.NotAConstant) {
                    if (valueRequired) {
                        switch((this.expression.implicitConversion & IMPLICIT_CONVERSION_MASK) >> 4) {
                            case T_int:
                                codeStream.generateInlinedValue(this.constant.intValue() * -1);
                                break;
                            case T_float:
                                codeStream.generateInlinedValue(this.constant.floatValue() * -1.0f);
                                break;
                            case T_long:
                                codeStream.generateInlinedValue(this.constant.longValue() * -1L);
                                break;
                            case T_double:
                                codeStream.generateInlinedValue(this.constant.doubleValue() * -1.0);
                        }
                    }
                } else {
                    this.expression.generateCode(currentScope, codeStream, valueRequired);
                    if (valueRequired) {
                        switch((this.expression.implicitConversion & IMPLICIT_CONVERSION_MASK) >> 4) {
                            case T_int:
                                codeStream.ineg();
                                break;
                            case T_float:
                                codeStream.fneg();
                                break;
                            case T_long:
                                codeStream.lneg();
                                break;
                            case T_double:
                                codeStream.dneg();
                        }
                    }
                }
                break;
            case PLUS:
                this.expression.generateCode(currentScope, codeStream, valueRequired);
        }
        if (valueRequired) {
            codeStream.generateImplicitConversion(this.implicitConversion);
        }
        codeStream.recordPositionsFrom(pc, this.sourceStart);
    }

    /**
	 * Boolean operator code generation
	 *	Optimized operations are: &&, ||, <, <=, >, >=, &, |, ^
	 */
    public void generateOptimizedBoolean(BlockScope currentScope, CodeStream codeStream, BranchLabel trueLabel, BranchLabel falseLabel, boolean valueRequired) {
        if ((this.constant != Constant.NotAConstant) && (this.constant.typeID() == T_boolean)) {
            super.generateOptimizedBoolean(currentScope, codeStream, trueLabel, falseLabel, valueRequired);
            return;
        }
        if (((this.bits & OperatorMASK) >> OperatorSHIFT) == NOT) {
            this.expression.generateOptimizedBoolean(currentScope, codeStream, falseLabel, trueLabel, valueRequired);
        } else {
            super.generateOptimizedBoolean(currentScope, codeStream, trueLabel, falseLabel, valueRequired);
        }
    }

    public StringBuffer printExpressionNoParenthesis(int indent, StringBuffer output) {
        output.append(operatorToString()).append(' ');
        return this.expression.printExpression(0, output);
    }

    public TypeBinding resolveType(BlockScope scope) {
        boolean expressionIsCast;
        if ((expressionIsCast = this.expression instanceof CastExpression) == true) this.expression.bits |= DisableUnnecessaryCastCheck;
        TypeBinding expressionType = this.expression.resolveType(scope);
        if (expressionType == null) {
            this.constant = Constant.NotAConstant;
            return null;
        }
        int expressionTypeID = expressionType.id;
        boolean use15specifics = scope.compilerOptions().sourceLevel >= ClassFileConstants.JDK1_5;
        if (use15specifics) {
            if (!expressionType.isBaseType()) {
                expressionTypeID = scope.environment().computeBoxingType(expressionType).id;
            }
        }
        if (expressionTypeID > 15) {
            this.constant = Constant.NotAConstant;
            scope.problemReporter().invalidOperator(this, expressionType);
            return null;
        }
        int tableId;
        switch((this.bits & OperatorMASK) >> OperatorSHIFT) {
            case NOT:
                tableId = AND_AND;
                break;
            case TWIDDLE:
                tableId = LEFT_SHIFT;
                break;
            default:
                tableId = MINUS;
        }
        int operatorSignature = OperatorSignatures[tableId][(expressionTypeID << 4) + expressionTypeID];
        this.expression.computeConversion(scope, TypeBinding.wellKnownType(scope, (operatorSignature >>> 16) & 0x0000F), expressionType);
        this.bits |= operatorSignature & 0xF;
        switch(operatorSignature & 0xF) {
            case T_boolean:
                this.resolvedType = TypeBinding.BOOLEAN;
                break;
            case T_byte:
                this.resolvedType = TypeBinding.BYTE;
                break;
            case T_char:
                this.resolvedType = TypeBinding.CHAR;
                break;
            case T_double:
                this.resolvedType = TypeBinding.DOUBLE;
                break;
            case T_float:
                this.resolvedType = TypeBinding.FLOAT;
                break;
            case T_int:
                this.resolvedType = TypeBinding.INT;
                break;
            case T_long:
                this.resolvedType = TypeBinding.LONG;
                break;
            default:
                this.constant = Constant.NotAConstant;
                if (expressionTypeID != T_undefined) scope.problemReporter().invalidOperator(this, expressionType);
                return null;
        }
        if (this.expression.constant != Constant.NotAConstant) {
            this.constant = Constant.computeConstantOperation(this.expression.constant, expressionTypeID, (this.bits & OperatorMASK) >> OperatorSHIFT);
        } else {
            this.constant = Constant.NotAConstant;
            if (((this.bits & OperatorMASK) >> OperatorSHIFT) == NOT) {
                Constant cst = this.expression.optimizedBooleanConstant();
                if (cst != Constant.NotAConstant) this.optimizedBooleanConstant = BooleanConstant.fromValue(!cst.booleanValue());
            }
        }
        if (expressionIsCast) {
            CastExpression.checkNeedForArgumentCast(scope, tableId, operatorSignature, this.expression, expressionTypeID);
        }
        return this.resolvedType;
    }

    public void traverse(ASTVisitor visitor, BlockScope blockScope) {
        if (visitor.visit(this, blockScope)) {
            this.expression.traverse(visitor, blockScope);
        }
        visitor.endVisit(this, blockScope);
    }
}
