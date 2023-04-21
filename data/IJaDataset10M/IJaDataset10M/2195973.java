package hu.cubussapiens.modembed.notation.operation.parseTreeConstruction;

import org.eclipse.emf.ecore.*;
import org.eclipse.xtext.*;
import org.eclipse.xtext.parsetree.reconstr.IEObjectConsumer;
import org.eclipse.xtext.parsetree.reconstr.impl.AbstractParseTreeConstructor;
import hu.cubussapiens.modembed.notation.operation.services.OperationNotationGrammarAccess;
import com.google.inject.Inject;

@SuppressWarnings("all")
public class OperationNotationParsetreeConstructor extends AbstractParseTreeConstructor {

    @Inject
    private OperationNotationGrammarAccess grammarAccess;

    @Override
    protected AbstractToken getRootToken(IEObjectConsumer inst) {
        return new ThisRootNode(inst);
    }

    protected class ThisRootNode extends RootToken {

        public ThisRootNode(IEObjectConsumer inst) {
            super(inst);
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Operations_Group(this, this, 0, inst);
                case 1:
                    return new Operation_Group(this, this, 1, inst);
                case 2:
                    return new OperationParameter_Alternatives(this, this, 2, inst);
                case 3:
                    return new LiteralParameter_Group(this, this, 3, inst);
                case 4:
                    return new VariableParameter_Group(this, this, 4, inst);
                case 5:
                    return new OperationStep_Alternatives(this, this, 5, inst);
                case 6:
                    return new OperationLabel_Group(this, this, 6, inst);
                case 7:
                    return new OperationWord_Group(this, this, 7, inst);
                case 8:
                    return new Value_Alternatives(this, this, 8, inst);
                case 9:
                    return new LiteralValue_ValueAssignment(this, this, 9, inst);
                case 10:
                    return new LiteralParamValue_LiteralAssignment(this, this, 10, inst);
                case 11:
                    return new LabelValue_Group(this, this, 11, inst);
                case 12:
                    return new VariableValue_Group(this, this, 12, inst);
                case 13:
                    return new VariableIndex_Group(this, this, 13, inst);
                default:
                    return null;
            }
        }
    }

    protected class Operations_Group extends GroupToken {

        public Operations_Group(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Group getGrammarElement() {
            return grammarAccess.getOperationsAccess().getGroup();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Operations_OperationsAssignment_2(lastRuleCallOrigin, this, 0, inst);
                case 1:
                    return new Operations_InstructionsetAssignment_1(lastRuleCallOrigin, this, 1, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getOperationsRule().getType().getClassifier()) return null;
            return eObjectConsumer;
        }
    }

    protected class Operations_InstructionsKeyword_0 extends KeywordToken {

        public Operations_InstructionsKeyword_0(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Keyword getGrammarElement() {
            return grammarAccess.getOperationsAccess().getInstructionsKeyword_0();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(this, index, index, inst);
            }
        }
    }

    protected class Operations_InstructionsetAssignment_1 extends AssignmentToken {

        public Operations_InstructionsetAssignment_1(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getOperationsAccess().getInstructionsetAssignment_1();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Operations_InstructionsKeyword_0(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("instructionset", true)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("instructionset");
            if (valueSerializer.isValid(obj.getEObject(), grammarAccess.getOperationsAccess().getInstructionsetSTRINGTerminalRuleCall_1_0(), value, null)) {
                type = AssignmentType.TERMINAL_RULE_CALL;
                element = grammarAccess.getOperationsAccess().getInstructionsetSTRINGTerminalRuleCall_1_0();
                return obj;
            }
            return null;
        }
    }

    protected class Operations_OperationsAssignment_2 extends AssignmentToken {

        public Operations_OperationsAssignment_2(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getOperationsAccess().getOperationsAssignment_2();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Operation_Group(this, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("operations", false)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("operations");
            if (value instanceof EObject) {
                IEObjectConsumer param = createEObjectConsumer((EObject) value);
                if (param.isInstanceOf(grammarAccess.getOperationRule().getType().getClassifier())) {
                    type = AssignmentType.PARSER_RULE_CALL;
                    element = grammarAccess.getOperationsAccess().getOperationsOperationParserRuleCall_2_0();
                    consumed = obj;
                    return param;
                }
            }
            return null;
        }

        @Override
        public AbstractToken createFollowerAfterReturn(AbstractToken next, int actIndex, int index, IEObjectConsumer inst) {
            if (value == inst.getEObject() && !inst.isConsumed()) return null;
            switch(index) {
                case 0:
                    return new Operations_OperationsAssignment_2(lastRuleCallOrigin, next, actIndex, consumed);
                case 1:
                    return new Operations_InstructionsetAssignment_1(lastRuleCallOrigin, next, actIndex, consumed);
                default:
                    return null;
            }
        }
    }

    protected class Operation_Group extends GroupToken {

        public Operation_Group(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Group getGrammarElement() {
            return grammarAccess.getOperationAccess().getGroup();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Operation_RightCurlyBracketKeyword_7(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getOperationRule().getType().getClassifier()) return null;
            return eObjectConsumer;
        }
    }

    protected class Operation_OperationKeyword_0 extends KeywordToken {

        public Operation_OperationKeyword_0(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Keyword getGrammarElement() {
            return grammarAccess.getOperationAccess().getOperationKeyword_0();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(this, index, index, inst);
            }
        }
    }

    protected class Operation_NameAssignment_1 extends AssignmentToken {

        public Operation_NameAssignment_1(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getOperationAccess().getNameAssignment_1();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Operation_OperationKeyword_0(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("name", true)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("name");
            if (valueSerializer.isValid(obj.getEObject(), grammarAccess.getOperationAccess().getNameIDTerminalRuleCall_1_0(), value, null)) {
                type = AssignmentType.TERMINAL_RULE_CALL;
                element = grammarAccess.getOperationAccess().getNameIDTerminalRuleCall_1_0();
                return obj;
            }
            return null;
        }
    }

    protected class Operation_LeftParenthesisKeyword_2 extends KeywordToken {

        public Operation_LeftParenthesisKeyword_2(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Keyword getGrammarElement() {
            return grammarAccess.getOperationAccess().getLeftParenthesisKeyword_2();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Operation_NameAssignment_1(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }
    }

    protected class Operation_ParamsAssignment_3 extends AssignmentToken {

        public Operation_ParamsAssignment_3(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getOperationAccess().getParamsAssignment_3();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new OperationParameter_Alternatives(this, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("params", false)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("params");
            if (value instanceof EObject) {
                IEObjectConsumer param = createEObjectConsumer((EObject) value);
                if (param.isInstanceOf(grammarAccess.getOperationParameterRule().getType().getClassifier())) {
                    type = AssignmentType.PARSER_RULE_CALL;
                    element = grammarAccess.getOperationAccess().getParamsOperationParameterParserRuleCall_3_0();
                    consumed = obj;
                    return param;
                }
            }
            return null;
        }

        @Override
        public AbstractToken createFollowerAfterReturn(AbstractToken next, int actIndex, int index, IEObjectConsumer inst) {
            if (value == inst.getEObject() && !inst.isConsumed()) return null;
            switch(index) {
                case 0:
                    return new Operation_ParamsAssignment_3(lastRuleCallOrigin, next, actIndex, consumed);
                case 1:
                    return new Operation_LeftParenthesisKeyword_2(lastRuleCallOrigin, next, actIndex, consumed);
                default:
                    return null;
            }
        }
    }

    protected class Operation_RightParenthesisKeyword_4 extends KeywordToken {

        public Operation_RightParenthesisKeyword_4(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Keyword getGrammarElement() {
            return grammarAccess.getOperationAccess().getRightParenthesisKeyword_4();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Operation_ParamsAssignment_3(lastRuleCallOrigin, this, 0, inst);
                case 1:
                    return new Operation_LeftParenthesisKeyword_2(lastRuleCallOrigin, this, 1, inst);
                default:
                    return null;
            }
        }
    }

    protected class Operation_LeftCurlyBracketKeyword_5 extends KeywordToken {

        public Operation_LeftCurlyBracketKeyword_5(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Keyword getGrammarElement() {
            return grammarAccess.getOperationAccess().getLeftCurlyBracketKeyword_5();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Operation_RightParenthesisKeyword_4(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }
    }

    protected class Operation_StepsAssignment_6 extends AssignmentToken {

        public Operation_StepsAssignment_6(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getOperationAccess().getStepsAssignment_6();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new OperationStep_Alternatives(this, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("steps", false)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("steps");
            if (value instanceof EObject) {
                IEObjectConsumer param = createEObjectConsumer((EObject) value);
                if (param.isInstanceOf(grammarAccess.getOperationStepRule().getType().getClassifier())) {
                    type = AssignmentType.PARSER_RULE_CALL;
                    element = grammarAccess.getOperationAccess().getStepsOperationStepParserRuleCall_6_0();
                    consumed = obj;
                    return param;
                }
            }
            return null;
        }

        @Override
        public AbstractToken createFollowerAfterReturn(AbstractToken next, int actIndex, int index, IEObjectConsumer inst) {
            if (value == inst.getEObject() && !inst.isConsumed()) return null;
            switch(index) {
                case 0:
                    return new Operation_StepsAssignment_6(lastRuleCallOrigin, next, actIndex, consumed);
                case 1:
                    return new Operation_LeftCurlyBracketKeyword_5(lastRuleCallOrigin, next, actIndex, consumed);
                default:
                    return null;
            }
        }
    }

    protected class Operation_RightCurlyBracketKeyword_7 extends KeywordToken {

        public Operation_RightCurlyBracketKeyword_7(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Keyword getGrammarElement() {
            return grammarAccess.getOperationAccess().getRightCurlyBracketKeyword_7();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Operation_StepsAssignment_6(lastRuleCallOrigin, this, 0, inst);
                case 1:
                    return new Operation_LeftCurlyBracketKeyword_5(lastRuleCallOrigin, this, 1, inst);
                default:
                    return null;
            }
        }
    }

    protected class OperationParameter_Alternatives extends AlternativesToken {

        public OperationParameter_Alternatives(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Alternatives getGrammarElement() {
            return grammarAccess.getOperationParameterAccess().getAlternatives();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new OperationParameter_LiteralParameterParserRuleCall_0(lastRuleCallOrigin, this, 0, inst);
                case 1:
                    return new OperationParameter_VariableParameterParserRuleCall_1(lastRuleCallOrigin, this, 1, inst);
                case 2:
                    return new OperationParameter_OperationLabelParserRuleCall_2(lastRuleCallOrigin, this, 2, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getLiteralParameterRule().getType().getClassifier() && getEObject().eClass() != grammarAccess.getOperationLabelRule().getType().getClassifier() && getEObject().eClass() != grammarAccess.getVariableParameterRule().getType().getClassifier()) return null;
            return eObjectConsumer;
        }
    }

    protected class OperationParameter_LiteralParameterParserRuleCall_0 extends RuleCallToken {

        public OperationParameter_LiteralParameterParserRuleCall_0(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public RuleCall getGrammarElement() {
            return grammarAccess.getOperationParameterAccess().getLiteralParameterParserRuleCall_0();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new LiteralParameter_Group(this, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getLiteralParameterRule().getType().getClassifier()) return null;
            if (checkForRecursion(LiteralParameter_Group.class, eObjectConsumer)) return null;
            return eObjectConsumer;
        }

        @Override
        public AbstractToken createFollowerAfterReturn(AbstractToken next, int actIndex, int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(next, actIndex, index, inst);
            }
        }
    }

    protected class OperationParameter_VariableParameterParserRuleCall_1 extends RuleCallToken {

        public OperationParameter_VariableParameterParserRuleCall_1(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public RuleCall getGrammarElement() {
            return grammarAccess.getOperationParameterAccess().getVariableParameterParserRuleCall_1();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new VariableParameter_Group(this, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getVariableParameterRule().getType().getClassifier()) return null;
            if (checkForRecursion(VariableParameter_Group.class, eObjectConsumer)) return null;
            return eObjectConsumer;
        }

        @Override
        public AbstractToken createFollowerAfterReturn(AbstractToken next, int actIndex, int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(next, actIndex, index, inst);
            }
        }
    }

    protected class OperationParameter_OperationLabelParserRuleCall_2 extends RuleCallToken {

        public OperationParameter_OperationLabelParserRuleCall_2(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public RuleCall getGrammarElement() {
            return grammarAccess.getOperationParameterAccess().getOperationLabelParserRuleCall_2();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new OperationLabel_Group(this, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getOperationLabelRule().getType().getClassifier()) return null;
            if (checkForRecursion(OperationLabel_Group.class, eObjectConsumer)) return null;
            return eObjectConsumer;
        }

        @Override
        public AbstractToken createFollowerAfterReturn(AbstractToken next, int actIndex, int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(next, actIndex, index, inst);
            }
        }
    }

    protected class LiteralParameter_Group extends GroupToken {

        public LiteralParameter_Group(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Group getGrammarElement() {
            return grammarAccess.getLiteralParameterAccess().getGroup();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new LiteralParameter_NameAssignment_1(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getLiteralParameterRule().getType().getClassifier()) return null;
            return eObjectConsumer;
        }
    }

    protected class LiteralParameter_IntKeyword_0 extends KeywordToken {

        public LiteralParameter_IntKeyword_0(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Keyword getGrammarElement() {
            return grammarAccess.getLiteralParameterAccess().getIntKeyword_0();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(this, index, index, inst);
            }
        }
    }

    protected class LiteralParameter_NameAssignment_1 extends AssignmentToken {

        public LiteralParameter_NameAssignment_1(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getLiteralParameterAccess().getNameAssignment_1();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new LiteralParameter_IntKeyword_0(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("name", true)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("name");
            if (valueSerializer.isValid(obj.getEObject(), grammarAccess.getLiteralParameterAccess().getNameIDTerminalRuleCall_1_0(), value, null)) {
                type = AssignmentType.TERMINAL_RULE_CALL;
                element = grammarAccess.getLiteralParameterAccess().getNameIDTerminalRuleCall_1_0();
                return obj;
            }
            return null;
        }
    }

    protected class VariableParameter_Group extends GroupToken {

        public VariableParameter_Group(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Group getGrammarElement() {
            return grammarAccess.getVariableParameterAccess().getGroup();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new VariableParameter_NameAssignment_1(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getVariableParameterRule().getType().getClassifier()) return null;
            return eObjectConsumer;
        }
    }

    protected class VariableParameter_TypeAssignment_0 extends AssignmentToken {

        public VariableParameter_TypeAssignment_0(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getVariableParameterAccess().getTypeAssignment_0();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(this, index, index, inst);
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("type", true)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("type");
            if (valueSerializer.isValid(obj.getEObject(), grammarAccess.getVariableParameterAccess().getTypeIDTerminalRuleCall_0_0(), value, null)) {
                type = AssignmentType.TERMINAL_RULE_CALL;
                element = grammarAccess.getVariableParameterAccess().getTypeIDTerminalRuleCall_0_0();
                return obj;
            }
            return null;
        }
    }

    protected class VariableParameter_NameAssignment_1 extends AssignmentToken {

        public VariableParameter_NameAssignment_1(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getVariableParameterAccess().getNameAssignment_1();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new VariableParameter_TypeAssignment_0(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("name", true)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("name");
            if (valueSerializer.isValid(obj.getEObject(), grammarAccess.getVariableParameterAccess().getNameIDTerminalRuleCall_1_0(), value, null)) {
                type = AssignmentType.TERMINAL_RULE_CALL;
                element = grammarAccess.getVariableParameterAccess().getNameIDTerminalRuleCall_1_0();
                return obj;
            }
            return null;
        }
    }

    protected class OperationStep_Alternatives extends AlternativesToken {

        public OperationStep_Alternatives(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Alternatives getGrammarElement() {
            return grammarAccess.getOperationStepAccess().getAlternatives();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new OperationStep_OperationLabelParserRuleCall_0(lastRuleCallOrigin, this, 0, inst);
                case 1:
                    return new OperationStep_OperationWordParserRuleCall_1(lastRuleCallOrigin, this, 1, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getOperationLabelRule().getType().getClassifier() && getEObject().eClass() != grammarAccess.getOperationWordRule().getType().getClassifier()) return null;
            return eObjectConsumer;
        }
    }

    protected class OperationStep_OperationLabelParserRuleCall_0 extends RuleCallToken {

        public OperationStep_OperationLabelParserRuleCall_0(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public RuleCall getGrammarElement() {
            return grammarAccess.getOperationStepAccess().getOperationLabelParserRuleCall_0();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new OperationLabel_Group(this, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getOperationLabelRule().getType().getClassifier()) return null;
            if (checkForRecursion(OperationLabel_Group.class, eObjectConsumer)) return null;
            return eObjectConsumer;
        }

        @Override
        public AbstractToken createFollowerAfterReturn(AbstractToken next, int actIndex, int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(next, actIndex, index, inst);
            }
        }
    }

    protected class OperationStep_OperationWordParserRuleCall_1 extends RuleCallToken {

        public OperationStep_OperationWordParserRuleCall_1(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public RuleCall getGrammarElement() {
            return grammarAccess.getOperationStepAccess().getOperationWordParserRuleCall_1();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new OperationWord_Group(this, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getOperationWordRule().getType().getClassifier()) return null;
            if (checkForRecursion(OperationWord_Group.class, eObjectConsumer)) return null;
            return eObjectConsumer;
        }

        @Override
        public AbstractToken createFollowerAfterReturn(AbstractToken next, int actIndex, int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(next, actIndex, index, inst);
            }
        }
    }

    protected class OperationLabel_Group extends GroupToken {

        public OperationLabel_Group(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Group getGrammarElement() {
            return grammarAccess.getOperationLabelAccess().getGroup();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new OperationLabel_NameAssignment_1(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getOperationLabelRule().getType().getClassifier()) return null;
            return eObjectConsumer;
        }
    }

    protected class OperationLabel_LabelKeyword_0 extends KeywordToken {

        public OperationLabel_LabelKeyword_0(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Keyword getGrammarElement() {
            return grammarAccess.getOperationLabelAccess().getLabelKeyword_0();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(this, index, index, inst);
            }
        }
    }

    protected class OperationLabel_NameAssignment_1 extends AssignmentToken {

        public OperationLabel_NameAssignment_1(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getOperationLabelAccess().getNameAssignment_1();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new OperationLabel_LabelKeyword_0(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("name", true)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("name");
            if (valueSerializer.isValid(obj.getEObject(), grammarAccess.getOperationLabelAccess().getNameIDTerminalRuleCall_1_0(), value, null)) {
                type = AssignmentType.TERMINAL_RULE_CALL;
                element = grammarAccess.getOperationLabelAccess().getNameIDTerminalRuleCall_1_0();
                return obj;
            }
            return null;
        }
    }

    protected class OperationWord_Group extends GroupToken {

        public OperationWord_Group(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Group getGrammarElement() {
            return grammarAccess.getOperationWordAccess().getGroup();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new OperationWord_SemicolonKeyword_2(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getOperationWordRule().getType().getClassifier()) return null;
            return eObjectConsumer;
        }
    }

    protected class OperationWord_InstructionAssignment_0 extends AssignmentToken {

        public OperationWord_InstructionAssignment_0(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getOperationWordAccess().getInstructionAssignment_0();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(this, index, index, inst);
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("instruction", true)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("instruction");
            if (valueSerializer.isValid(obj.getEObject(), grammarAccess.getOperationWordAccess().getInstructionIDTerminalRuleCall_0_0(), value, null)) {
                type = AssignmentType.TERMINAL_RULE_CALL;
                element = grammarAccess.getOperationWordAccess().getInstructionIDTerminalRuleCall_0_0();
                return obj;
            }
            return null;
        }
    }

    protected class OperationWord_ValuesAssignment_1 extends AssignmentToken {

        public OperationWord_ValuesAssignment_1(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getOperationWordAccess().getValuesAssignment_1();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Value_Alternatives(this, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("values", false)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("values");
            if (value instanceof EObject) {
                IEObjectConsumer param = createEObjectConsumer((EObject) value);
                if (param.isInstanceOf(grammarAccess.getValueRule().getType().getClassifier())) {
                    type = AssignmentType.PARSER_RULE_CALL;
                    element = grammarAccess.getOperationWordAccess().getValuesValueParserRuleCall_1_0();
                    consumed = obj;
                    return param;
                }
            }
            return null;
        }

        @Override
        public AbstractToken createFollowerAfterReturn(AbstractToken next, int actIndex, int index, IEObjectConsumer inst) {
            if (value == inst.getEObject() && !inst.isConsumed()) return null;
            switch(index) {
                case 0:
                    return new OperationWord_ValuesAssignment_1(lastRuleCallOrigin, next, actIndex, consumed);
                case 1:
                    return new OperationWord_InstructionAssignment_0(lastRuleCallOrigin, next, actIndex, consumed);
                default:
                    return null;
            }
        }
    }

    protected class OperationWord_SemicolonKeyword_2 extends KeywordToken {

        public OperationWord_SemicolonKeyword_2(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Keyword getGrammarElement() {
            return grammarAccess.getOperationWordAccess().getSemicolonKeyword_2();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new OperationWord_ValuesAssignment_1(lastRuleCallOrigin, this, 0, inst);
                case 1:
                    return new OperationWord_InstructionAssignment_0(lastRuleCallOrigin, this, 1, inst);
                default:
                    return null;
            }
        }
    }

    protected class Value_Alternatives extends AlternativesToken {

        public Value_Alternatives(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Alternatives getGrammarElement() {
            return grammarAccess.getValueAccess().getAlternatives();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Value_LiteralValueParserRuleCall_0(lastRuleCallOrigin, this, 0, inst);
                case 1:
                    return new Value_LabelValueParserRuleCall_1(lastRuleCallOrigin, this, 1, inst);
                case 2:
                    return new Value_VariableValueParserRuleCall_2(lastRuleCallOrigin, this, 2, inst);
                case 3:
                    return new Value_LiteralParamValueParserRuleCall_3(lastRuleCallOrigin, this, 3, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getLabelValueRule().getType().getClassifier() && getEObject().eClass() != grammarAccess.getLiteralParamValueRule().getType().getClassifier() && getEObject().eClass() != grammarAccess.getLiteralValueRule().getType().getClassifier() && getEObject().eClass() != grammarAccess.getVariableValueRule().getType().getClassifier()) return null;
            return eObjectConsumer;
        }
    }

    protected class Value_LiteralValueParserRuleCall_0 extends RuleCallToken {

        public Value_LiteralValueParserRuleCall_0(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public RuleCall getGrammarElement() {
            return grammarAccess.getValueAccess().getLiteralValueParserRuleCall_0();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new LiteralValue_ValueAssignment(this, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getLiteralValueRule().getType().getClassifier()) return null;
            if (checkForRecursion(LiteralValue_ValueAssignment.class, eObjectConsumer)) return null;
            return eObjectConsumer;
        }

        @Override
        public AbstractToken createFollowerAfterReturn(AbstractToken next, int actIndex, int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(next, actIndex, index, inst);
            }
        }
    }

    protected class Value_LabelValueParserRuleCall_1 extends RuleCallToken {

        public Value_LabelValueParserRuleCall_1(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public RuleCall getGrammarElement() {
            return grammarAccess.getValueAccess().getLabelValueParserRuleCall_1();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new LabelValue_Group(this, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getLabelValueRule().getType().getClassifier()) return null;
            if (checkForRecursion(LabelValue_Group.class, eObjectConsumer)) return null;
            return eObjectConsumer;
        }

        @Override
        public AbstractToken createFollowerAfterReturn(AbstractToken next, int actIndex, int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(next, actIndex, index, inst);
            }
        }
    }

    protected class Value_VariableValueParserRuleCall_2 extends RuleCallToken {

        public Value_VariableValueParserRuleCall_2(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public RuleCall getGrammarElement() {
            return grammarAccess.getValueAccess().getVariableValueParserRuleCall_2();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new VariableValue_Group(this, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getVariableValueRule().getType().getClassifier()) return null;
            if (checkForRecursion(VariableValue_Group.class, eObjectConsumer)) return null;
            return eObjectConsumer;
        }

        @Override
        public AbstractToken createFollowerAfterReturn(AbstractToken next, int actIndex, int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(next, actIndex, index, inst);
            }
        }
    }

    protected class Value_LiteralParamValueParserRuleCall_3 extends RuleCallToken {

        public Value_LiteralParamValueParserRuleCall_3(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public RuleCall getGrammarElement() {
            return grammarAccess.getValueAccess().getLiteralParamValueParserRuleCall_3();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new LiteralParamValue_LiteralAssignment(this, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getLiteralParamValueRule().getType().getClassifier()) return null;
            if (checkForRecursion(LiteralParamValue_LiteralAssignment.class, eObjectConsumer)) return null;
            return eObjectConsumer;
        }

        @Override
        public AbstractToken createFollowerAfterReturn(AbstractToken next, int actIndex, int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(next, actIndex, index, inst);
            }
        }
    }

    protected class LiteralValue_ValueAssignment extends AssignmentToken {

        public LiteralValue_ValueAssignment(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getLiteralValueAccess().getValueAssignment();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(this, index, index, inst);
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getLiteralValueRule().getType().getClassifier()) return null;
            if ((value = eObjectConsumer.getConsumable("value", true)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("value");
            if (valueSerializer.isValid(obj.getEObject(), grammarAccess.getLiteralValueAccess().getValueINTTerminalRuleCall_0(), value, null)) {
                type = AssignmentType.TERMINAL_RULE_CALL;
                element = grammarAccess.getLiteralValueAccess().getValueINTTerminalRuleCall_0();
                return obj;
            }
            return null;
        }
    }

    protected class LiteralParamValue_LiteralAssignment extends AssignmentToken {

        public LiteralParamValue_LiteralAssignment(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getLiteralParamValueAccess().getLiteralAssignment();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(this, index, index, inst);
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getLiteralParamValueRule().getType().getClassifier()) return null;
            if ((value = eObjectConsumer.getConsumable("literal", true)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("literal");
            if (value instanceof EObject) {
                IEObjectConsumer param = createEObjectConsumer((EObject) value);
                if (param.isInstanceOf(grammarAccess.getLiteralParamValueAccess().getLiteralLiteralParameterCrossReference_0().getType().getClassifier())) {
                    type = AssignmentType.CROSS_REFERENCE;
                    element = grammarAccess.getLiteralParamValueAccess().getLiteralLiteralParameterCrossReference_0();
                    return obj;
                }
            }
            return null;
        }
    }

    protected class LabelValue_Group extends GroupToken {

        public LabelValue_Group(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Group getGrammarElement() {
            return grammarAccess.getLabelValueAccess().getGroup();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new LabelValue_LabelAssignment_1(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getLabelValueRule().getType().getClassifier()) return null;
            return eObjectConsumer;
        }
    }

    protected class LabelValue_LabelKeyword_0 extends KeywordToken {

        public LabelValue_LabelKeyword_0(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Keyword getGrammarElement() {
            return grammarAccess.getLabelValueAccess().getLabelKeyword_0();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(this, index, index, inst);
            }
        }
    }

    protected class LabelValue_LabelAssignment_1 extends AssignmentToken {

        public LabelValue_LabelAssignment_1(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getLabelValueAccess().getLabelAssignment_1();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new LabelValue_LabelKeyword_0(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("label", true)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("label");
            if (value instanceof EObject) {
                IEObjectConsumer param = createEObjectConsumer((EObject) value);
                if (param.isInstanceOf(grammarAccess.getLabelValueAccess().getLabelOperationLabelCrossReference_1_0().getType().getClassifier())) {
                    type = AssignmentType.CROSS_REFERENCE;
                    element = grammarAccess.getLabelValueAccess().getLabelOperationLabelCrossReference_1_0();
                    return obj;
                }
            }
            return null;
        }
    }

    protected class VariableValue_Group extends GroupToken {

        public VariableValue_Group(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Group getGrammarElement() {
            return grammarAccess.getVariableValueAccess().getGroup();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new VariableValue_IndexingAssignment_3(lastRuleCallOrigin, this, 0, inst);
                case 1:
                    return new VariableValue_ItemAssignment_2(lastRuleCallOrigin, this, 1, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getVariableValueRule().getType().getClassifier()) return null;
            return eObjectConsumer;
        }
    }

    protected class VariableValue_VariableAssignment_0 extends AssignmentToken {

        public VariableValue_VariableAssignment_0(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getVariableValueAccess().getVariableAssignment_0();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(this, index, index, inst);
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("variable", true)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("variable");
            if (value instanceof EObject) {
                IEObjectConsumer param = createEObjectConsumer((EObject) value);
                if (param.isInstanceOf(grammarAccess.getVariableValueAccess().getVariableVariableParameterCrossReference_0_0().getType().getClassifier())) {
                    type = AssignmentType.CROSS_REFERENCE;
                    element = grammarAccess.getVariableValueAccess().getVariableVariableParameterCrossReference_0_0();
                    return obj;
                }
            }
            return null;
        }
    }

    protected class VariableValue_FullStopKeyword_1 extends KeywordToken {

        public VariableValue_FullStopKeyword_1(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Keyword getGrammarElement() {
            return grammarAccess.getVariableValueAccess().getFullStopKeyword_1();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new VariableValue_VariableAssignment_0(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }
    }

    protected class VariableValue_ItemAssignment_2 extends AssignmentToken {

        public VariableValue_ItemAssignment_2(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getVariableValueAccess().getItemAssignment_2();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new VariableValue_FullStopKeyword_1(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("item", true)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("item");
            if (valueSerializer.isValid(obj.getEObject(), grammarAccess.getVariableValueAccess().getItemIDTerminalRuleCall_2_0(), value, null)) {
                type = AssignmentType.TERMINAL_RULE_CALL;
                element = grammarAccess.getVariableValueAccess().getItemIDTerminalRuleCall_2_0();
                return obj;
            }
            return null;
        }
    }

    protected class VariableValue_IndexingAssignment_3 extends AssignmentToken {

        public VariableValue_IndexingAssignment_3(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getVariableValueAccess().getIndexingAssignment_3();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new VariableIndex_Group(this, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("indexing", false)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("indexing");
            if (value instanceof EObject) {
                IEObjectConsumer param = createEObjectConsumer((EObject) value);
                if (param.isInstanceOf(grammarAccess.getVariableIndexRule().getType().getClassifier())) {
                    type = AssignmentType.PARSER_RULE_CALL;
                    element = grammarAccess.getVariableValueAccess().getIndexingVariableIndexParserRuleCall_3_0();
                    consumed = obj;
                    return param;
                }
            }
            return null;
        }

        @Override
        public AbstractToken createFollowerAfterReturn(AbstractToken next, int actIndex, int index, IEObjectConsumer inst) {
            if (value == inst.getEObject() && !inst.isConsumed()) return null;
            switch(index) {
                case 0:
                    return new VariableValue_ItemAssignment_2(lastRuleCallOrigin, next, actIndex, consumed);
                default:
                    return null;
            }
        }
    }

    protected class VariableIndex_Group extends GroupToken {

        public VariableIndex_Group(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Group getGrammarElement() {
            return grammarAccess.getVariableIndexAccess().getGroup();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new VariableIndex_RightSquareBracketKeyword_2(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getVariableIndexRule().getType().getClassifier()) return null;
            return eObjectConsumer;
        }
    }

    protected class VariableIndex_LeftSquareBracketKeyword_0 extends KeywordToken {

        public VariableIndex_LeftSquareBracketKeyword_0(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Keyword getGrammarElement() {
            return grammarAccess.getVariableIndexAccess().getLeftSquareBracketKeyword_0();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(this, index, index, inst);
            }
        }
    }

    protected class VariableIndex_IndexAssignment_1 extends AssignmentToken {

        public VariableIndex_IndexAssignment_1(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getVariableIndexAccess().getIndexAssignment_1();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new VariableIndex_LeftSquareBracketKeyword_0(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("index", true)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("index");
            if (valueSerializer.isValid(obj.getEObject(), grammarAccess.getVariableIndexAccess().getIndexINTTerminalRuleCall_1_0(), value, null)) {
                type = AssignmentType.TERMINAL_RULE_CALL;
                element = grammarAccess.getVariableIndexAccess().getIndexINTTerminalRuleCall_1_0();
                return obj;
            }
            return null;
        }
    }

    protected class VariableIndex_RightSquareBracketKeyword_2 extends KeywordToken {

        public VariableIndex_RightSquareBracketKeyword_2(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Keyword getGrammarElement() {
            return grammarAccess.getVariableIndexAccess().getRightSquareBracketKeyword_2();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new VariableIndex_IndexAssignment_1(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }
    }
}
