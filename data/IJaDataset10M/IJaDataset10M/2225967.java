package de.abg.jreichert.serviceqos.model.wsdl.xtext.parseTreeConstruction;

import org.eclipse.emf.ecore.*;
import org.eclipse.xtext.*;
import org.eclipse.xtext.parsetree.reconstr.IEObjectConsumer;
import org.eclipse.xtext.parsetree.reconstr.impl.AbstractParseTreeConstructor;
import de.abg.jreichert.serviceqos.model.wsdl.xtext.services.ServiceQosWsdlGrammarAccess;
import com.google.inject.Inject;

@SuppressWarnings("all")
public class ServiceQosWsdlParsetreeConstructor extends AbstractParseTreeConstructor {

    @Inject
    private ServiceQosWsdlGrammarAccess grammarAccess;

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
                    return new WsdlDescription_Group(this, this, 0, inst);
                case 1:
                    return new ServiceComponent_Alternatives(this, this, 1, inst);
                case 2:
                    return new Service_Group(this, this, 2, inst);
                case 3:
                    return new Interface_Group(this, this, 3, inst);
                case 4:
                    return new Operation_Group(this, this, 4, inst);
                case 5:
                    return new Endpoint_Group(this, this, 5, inst);
                default:
                    return null;
            }
        }
    }

    protected class WsdlDescription_Group extends GroupToken {

        public WsdlDescription_Group(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Group getGrammarElement() {
            return grammarAccess.getWsdlDescriptionAccess().getGroup();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new WsdlDescription_ServiceAssignment_2(lastRuleCallOrigin, this, 0, inst);
                case 1:
                    return new WsdlDescription_EndpointAssignment_1(lastRuleCallOrigin, this, 1, inst);
                case 2:
                    return new WsdlDescription_InterfaceAssignment_0(lastRuleCallOrigin, this, 2, inst);
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(this, index, index - 3, inst);
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getWsdlDescriptionRule().getType().getClassifier()) return null;
            return eObjectConsumer;
        }
    }

    protected class WsdlDescription_InterfaceAssignment_0 extends AssignmentToken {

        public WsdlDescription_InterfaceAssignment_0(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getWsdlDescriptionAccess().getInterfaceAssignment_0();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Interface_Group(this, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("interface", false)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("interface");
            if (value instanceof EObject) {
                IEObjectConsumer param = createEObjectConsumer((EObject) value);
                if (param.isInstanceOf(grammarAccess.getInterfaceRule().getType().getClassifier())) {
                    type = AssignmentType.PARSER_RULE_CALL;
                    element = grammarAccess.getWsdlDescriptionAccess().getInterfaceInterfaceParserRuleCall_0_0();
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
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(next, actIndex, index, consumed);
            }
        }
    }

    protected class WsdlDescription_EndpointAssignment_1 extends AssignmentToken {

        public WsdlDescription_EndpointAssignment_1(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getWsdlDescriptionAccess().getEndpointAssignment_1();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Endpoint_Group(this, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("endpoint", false)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("endpoint");
            if (value instanceof EObject) {
                IEObjectConsumer param = createEObjectConsumer((EObject) value);
                if (param.isInstanceOf(grammarAccess.getEndpointRule().getType().getClassifier())) {
                    type = AssignmentType.PARSER_RULE_CALL;
                    element = grammarAccess.getWsdlDescriptionAccess().getEndpointEndpointParserRuleCall_1_0();
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
                    return new WsdlDescription_InterfaceAssignment_0(lastRuleCallOrigin, next, actIndex, consumed);
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(next, actIndex, index - 1, consumed);
            }
        }
    }

    protected class WsdlDescription_ServiceAssignment_2 extends AssignmentToken {

        public WsdlDescription_ServiceAssignment_2(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getWsdlDescriptionAccess().getServiceAssignment_2();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Service_Group(this, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("service", false)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("service");
            if (value instanceof EObject) {
                IEObjectConsumer param = createEObjectConsumer((EObject) value);
                if (param.isInstanceOf(grammarAccess.getServiceRule().getType().getClassifier())) {
                    type = AssignmentType.PARSER_RULE_CALL;
                    element = grammarAccess.getWsdlDescriptionAccess().getServiceServiceParserRuleCall_2_0();
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
                    return new WsdlDescription_EndpointAssignment_1(lastRuleCallOrigin, next, actIndex, consumed);
                case 1:
                    return new WsdlDescription_InterfaceAssignment_0(lastRuleCallOrigin, next, actIndex, consumed);
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(next, actIndex, index - 2, consumed);
            }
        }
    }

    protected class ServiceComponent_Alternatives extends AlternativesToken {

        public ServiceComponent_Alternatives(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Alternatives getGrammarElement() {
            return grammarAccess.getServiceComponentAccess().getAlternatives();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new ServiceComponent_ServiceParserRuleCall_0(lastRuleCallOrigin, this, 0, inst);
                case 1:
                    return new ServiceComponent_EndpointParserRuleCall_1(lastRuleCallOrigin, this, 1, inst);
                case 2:
                    return new ServiceComponent_InterfaceParserRuleCall_2(lastRuleCallOrigin, this, 2, inst);
                case 3:
                    return new ServiceComponent_OperationParserRuleCall_3(lastRuleCallOrigin, this, 3, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getEndpointRule().getType().getClassifier() && getEObject().eClass() != grammarAccess.getOperationRule().getType().getClassifier() && getEObject().eClass() != grammarAccess.getInterfaceRule().getType().getClassifier() && getEObject().eClass() != grammarAccess.getServiceRule().getType().getClassifier()) return null;
            return eObjectConsumer;
        }
    }

    protected class ServiceComponent_ServiceParserRuleCall_0 extends RuleCallToken {

        public ServiceComponent_ServiceParserRuleCall_0(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public RuleCall getGrammarElement() {
            return grammarAccess.getServiceComponentAccess().getServiceParserRuleCall_0();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Service_Group(this, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getServiceRule().getType().getClassifier()) return null;
            if (checkForRecursion(Service_Group.class, eObjectConsumer)) return null;
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

    protected class ServiceComponent_EndpointParserRuleCall_1 extends RuleCallToken {

        public ServiceComponent_EndpointParserRuleCall_1(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public RuleCall getGrammarElement() {
            return grammarAccess.getServiceComponentAccess().getEndpointParserRuleCall_1();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Endpoint_Group(this, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getEndpointRule().getType().getClassifier()) return null;
            if (checkForRecursion(Endpoint_Group.class, eObjectConsumer)) return null;
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

    protected class ServiceComponent_InterfaceParserRuleCall_2 extends RuleCallToken {

        public ServiceComponent_InterfaceParserRuleCall_2(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public RuleCall getGrammarElement() {
            return grammarAccess.getServiceComponentAccess().getInterfaceParserRuleCall_2();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Interface_Group(this, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getInterfaceRule().getType().getClassifier()) return null;
            if (checkForRecursion(Interface_Group.class, eObjectConsumer)) return null;
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

    protected class ServiceComponent_OperationParserRuleCall_3 extends RuleCallToken {

        public ServiceComponent_OperationParserRuleCall_3(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public RuleCall getGrammarElement() {
            return grammarAccess.getServiceComponentAccess().getOperationParserRuleCall_3();
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
            if (getEObject().eClass() != grammarAccess.getOperationRule().getType().getClassifier()) return null;
            if (checkForRecursion(Operation_Group.class, eObjectConsumer)) return null;
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

    protected class Service_Group extends GroupToken {

        public Service_Group(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Group getGrammarElement() {
            return grammarAccess.getServiceAccess().getGroup();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Service_RightCurlyBracketKeyword_5(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getServiceRule().getType().getClassifier()) return null;
            return eObjectConsumer;
        }
    }

    protected class Service_ServiceKeyword_0 extends KeywordToken {

        public Service_ServiceKeyword_0(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Keyword getGrammarElement() {
            return grammarAccess.getServiceAccess().getServiceKeyword_0();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(this, index, index, inst);
            }
        }
    }

    protected class Service_NameAssignment_1 extends AssignmentToken {

        public Service_NameAssignment_1(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getServiceAccess().getNameAssignment_1();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Service_ServiceKeyword_0(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("name", true)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("name");
            if (valueSerializer.isValid(obj.getEObject(), grammarAccess.getServiceAccess().getNameIDTerminalRuleCall_1_0(), value, null)) {
                type = AssignmentType.TERMINAL_RULE_CALL;
                element = grammarAccess.getServiceAccess().getNameIDTerminalRuleCall_1_0();
                return obj;
            }
            return null;
        }
    }

    protected class Service_LeftCurlyBracketKeyword_2 extends KeywordToken {

        public Service_LeftCurlyBracketKeyword_2(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Keyword getGrammarElement() {
            return grammarAccess.getServiceAccess().getLeftCurlyBracketKeyword_2();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Service_NameAssignment_1(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }
    }

    protected class Service_ReferencedInterfaceAssignment_3 extends AssignmentToken {

        public Service_ReferencedInterfaceAssignment_3(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getServiceAccess().getReferencedInterfaceAssignment_3();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Service_LeftCurlyBracketKeyword_2(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("referencedInterface", false)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("referencedInterface");
            if (value instanceof EObject) {
                IEObjectConsumer param = createEObjectConsumer((EObject) value);
                if (param.isInstanceOf(grammarAccess.getServiceAccess().getReferencedInterfaceInterfaceTypeCrossReference_3_0().getType().getClassifier())) {
                    type = AssignmentType.CROSS_REFERENCE;
                    element = grammarAccess.getServiceAccess().getReferencedInterfaceInterfaceTypeCrossReference_3_0();
                    return obj;
                }
            }
            return null;
        }
    }

    protected class Service_EndpointAssignment_4 extends AssignmentToken {

        public Service_EndpointAssignment_4(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getServiceAccess().getEndpointAssignment_4();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Endpoint_Group(this, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("endpoint", true)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("endpoint");
            if (value instanceof EObject) {
                IEObjectConsumer param = createEObjectConsumer((EObject) value);
                if (param.isInstanceOf(grammarAccess.getEndpointRule().getType().getClassifier())) {
                    type = AssignmentType.PARSER_RULE_CALL;
                    element = grammarAccess.getServiceAccess().getEndpointEndpointParserRuleCall_4_0();
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
                    return new Service_EndpointAssignment_4(lastRuleCallOrigin, next, actIndex, consumed);
                case 1:
                    return new Service_ReferencedInterfaceAssignment_3(lastRuleCallOrigin, next, actIndex, consumed);
                case 2:
                    return new Service_LeftCurlyBracketKeyword_2(lastRuleCallOrigin, next, actIndex, consumed);
                default:
                    return null;
            }
        }
    }

    protected class Service_RightCurlyBracketKeyword_5 extends KeywordToken {

        public Service_RightCurlyBracketKeyword_5(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Keyword getGrammarElement() {
            return grammarAccess.getServiceAccess().getRightCurlyBracketKeyword_5();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Service_EndpointAssignment_4(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }
    }

    protected class Interface_Group extends GroupToken {

        public Interface_Group(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Group getGrammarElement() {
            return grammarAccess.getInterfaceAccess().getGroup();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Interface_RightCurlyBracketKeyword_4(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getInterfaceRule().getType().getClassifier()) return null;
            return eObjectConsumer;
        }
    }

    protected class Interface_InterfaceKeyword_0 extends KeywordToken {

        public Interface_InterfaceKeyword_0(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Keyword getGrammarElement() {
            return grammarAccess.getInterfaceAccess().getInterfaceKeyword_0();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(this, index, index, inst);
            }
        }
    }

    protected class Interface_NameAssignment_1 extends AssignmentToken {

        public Interface_NameAssignment_1(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getInterfaceAccess().getNameAssignment_1();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Interface_InterfaceKeyword_0(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("name", true)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("name");
            if (valueSerializer.isValid(obj.getEObject(), grammarAccess.getInterfaceAccess().getNameIDTerminalRuleCall_1_0(), value, null)) {
                type = AssignmentType.TERMINAL_RULE_CALL;
                element = grammarAccess.getInterfaceAccess().getNameIDTerminalRuleCall_1_0();
                return obj;
            }
            return null;
        }
    }

    protected class Interface_LeftCurlyBracketKeyword_2 extends KeywordToken {

        public Interface_LeftCurlyBracketKeyword_2(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Keyword getGrammarElement() {
            return grammarAccess.getInterfaceAccess().getLeftCurlyBracketKeyword_2();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Interface_NameAssignment_1(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }
    }

    protected class Interface_OperationAssignment_3 extends AssignmentToken {

        public Interface_OperationAssignment_3(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getInterfaceAccess().getOperationAssignment_3();
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
            if ((value = eObjectConsumer.getConsumable("operation", true)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("operation");
            if (value instanceof EObject) {
                IEObjectConsumer param = createEObjectConsumer((EObject) value);
                if (param.isInstanceOf(grammarAccess.getOperationRule().getType().getClassifier())) {
                    type = AssignmentType.PARSER_RULE_CALL;
                    element = grammarAccess.getInterfaceAccess().getOperationOperationParserRuleCall_3_0();
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
                    return new Interface_LeftCurlyBracketKeyword_2(lastRuleCallOrigin, next, actIndex, consumed);
                default:
                    return null;
            }
        }
    }

    protected class Interface_RightCurlyBracketKeyword_4 extends KeywordToken {

        public Interface_RightCurlyBracketKeyword_4(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Keyword getGrammarElement() {
            return grammarAccess.getInterfaceAccess().getRightCurlyBracketKeyword_4();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Interface_OperationAssignment_3(lastRuleCallOrigin, this, 0, inst);
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
                    return new Operation_NameAssignment_1(lastRuleCallOrigin, this, 0, inst);
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

    protected class Endpoint_Group extends GroupToken {

        public Endpoint_Group(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Group getGrammarElement() {
            return grammarAccess.getEndpointAccess().getGroup();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Endpoint_ReferencedInterfaceAssignment_3(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if (getEObject().eClass() != grammarAccess.getEndpointRule().getType().getClassifier()) return null;
            return eObjectConsumer;
        }
    }

    protected class Endpoint_EndpointKeyword_0 extends KeywordToken {

        public Endpoint_EndpointKeyword_0(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Keyword getGrammarElement() {
            return grammarAccess.getEndpointAccess().getEndpointKeyword_0();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                default:
                    return lastRuleCallOrigin.createFollowerAfterReturn(this, index, index, inst);
            }
        }
    }

    protected class Endpoint_NameAssignment_1 extends AssignmentToken {

        public Endpoint_NameAssignment_1(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getEndpointAccess().getNameAssignment_1();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Endpoint_EndpointKeyword_0(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("name", true)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("name");
            if (valueSerializer.isValid(obj.getEObject(), grammarAccess.getEndpointAccess().getNameIDTerminalRuleCall_1_0(), value, null)) {
                type = AssignmentType.TERMINAL_RULE_CALL;
                element = grammarAccess.getEndpointAccess().getNameIDTerminalRuleCall_1_0();
                return obj;
            }
            return null;
        }
    }

    protected class Endpoint_UsingInterfaceKeyword_2 extends KeywordToken {

        public Endpoint_UsingInterfaceKeyword_2(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Keyword getGrammarElement() {
            return grammarAccess.getEndpointAccess().getUsingInterfaceKeyword_2();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Endpoint_NameAssignment_1(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }
    }

    protected class Endpoint_ReferencedInterfaceAssignment_3 extends AssignmentToken {

        public Endpoint_ReferencedInterfaceAssignment_3(AbstractToken lastRuleCallOrigin, AbstractToken next, int transitionIndex, IEObjectConsumer eObjectConsumer) {
            super(lastRuleCallOrigin, next, transitionIndex, eObjectConsumer);
        }

        @Override
        public Assignment getGrammarElement() {
            return grammarAccess.getEndpointAccess().getReferencedInterfaceAssignment_3();
        }

        @Override
        public AbstractToken createFollower(int index, IEObjectConsumer inst) {
            switch(index) {
                case 0:
                    return new Endpoint_UsingInterfaceKeyword_2(lastRuleCallOrigin, this, 0, inst);
                default:
                    return null;
            }
        }

        @Override
        public IEObjectConsumer tryConsume() {
            if ((value = eObjectConsumer.getConsumable("referencedInterface", true)) == null) return null;
            IEObjectConsumer obj = eObjectConsumer.cloneAndConsume("referencedInterface");
            if (value instanceof EObject) {
                IEObjectConsumer param = createEObjectConsumer((EObject) value);
                if (param.isInstanceOf(grammarAccess.getEndpointAccess().getReferencedInterfaceInterfaceTypeCrossReference_3_0().getType().getClassifier())) {
                    type = AssignmentType.CROSS_REFERENCE;
                    element = grammarAccess.getEndpointAccess().getReferencedInterfaceInterfaceTypeCrossReference_3_0();
                    return obj;
                }
            }
            return null;
        }
    }
}
