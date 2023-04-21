package de.abg.jreichert.serviceqos.model.wsdl.xtext.services;

import com.google.inject.Singleton;
import com.google.inject.Inject;
import org.eclipse.xtext.*;
import org.eclipse.xtext.service.GrammarProvider;
import org.eclipse.xtext.service.AbstractElementFinder.*;
import org.eclipse.xtext.common.services.TerminalsGrammarAccess;

@Singleton
public class ServiceQosWsdlGrammarAccess extends AbstractGrammarElementFinder {

    public class WsdlDescriptionElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "WsdlDescription");

        private final Group cGroup = (Group) rule.eContents().get(1);

        private final Assignment cInterfaceAssignment_0 = (Assignment) cGroup.eContents().get(0);

        private final RuleCall cInterfaceInterfaceParserRuleCall_0_0 = (RuleCall) cInterfaceAssignment_0.eContents().get(0);

        private final Assignment cEndpointAssignment_1 = (Assignment) cGroup.eContents().get(1);

        private final RuleCall cEndpointEndpointParserRuleCall_1_0 = (RuleCall) cEndpointAssignment_1.eContents().get(0);

        private final Assignment cServiceAssignment_2 = (Assignment) cGroup.eContents().get(2);

        private final RuleCall cServiceServiceParserRuleCall_2_0 = (RuleCall) cServiceAssignment_2.eContents().get(0);

        public ParserRule getRule() {
            return rule;
        }

        public Group getGroup() {
            return cGroup;
        }

        public Assignment getInterfaceAssignment_0() {
            return cInterfaceAssignment_0;
        }

        public RuleCall getInterfaceInterfaceParserRuleCall_0_0() {
            return cInterfaceInterfaceParserRuleCall_0_0;
        }

        public Assignment getEndpointAssignment_1() {
            return cEndpointAssignment_1;
        }

        public RuleCall getEndpointEndpointParserRuleCall_1_0() {
            return cEndpointEndpointParserRuleCall_1_0;
        }

        public Assignment getServiceAssignment_2() {
            return cServiceAssignment_2;
        }

        public RuleCall getServiceServiceParserRuleCall_2_0() {
            return cServiceServiceParserRuleCall_2_0;
        }
    }

    public class ServiceComponentElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "ServiceComponent");

        private final Alternatives cAlternatives = (Alternatives) rule.eContents().get(1);

        private final RuleCall cServiceParserRuleCall_0 = (RuleCall) cAlternatives.eContents().get(0);

        private final RuleCall cEndpointParserRuleCall_1 = (RuleCall) cAlternatives.eContents().get(1);

        private final RuleCall cInterfaceParserRuleCall_2 = (RuleCall) cAlternatives.eContents().get(2);

        private final RuleCall cOperationParserRuleCall_3 = (RuleCall) cAlternatives.eContents().get(3);

        public ParserRule getRule() {
            return rule;
        }

        public Alternatives getAlternatives() {
            return cAlternatives;
        }

        public RuleCall getServiceParserRuleCall_0() {
            return cServiceParserRuleCall_0;
        }

        public RuleCall getEndpointParserRuleCall_1() {
            return cEndpointParserRuleCall_1;
        }

        public RuleCall getInterfaceParserRuleCall_2() {
            return cInterfaceParserRuleCall_2;
        }

        public RuleCall getOperationParserRuleCall_3() {
            return cOperationParserRuleCall_3;
        }
    }

    public class ServiceElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Service");

        private final Group cGroup = (Group) rule.eContents().get(1);

        private final Keyword cServiceKeyword_0 = (Keyword) cGroup.eContents().get(0);

        private final Assignment cNameAssignment_1 = (Assignment) cGroup.eContents().get(1);

        private final RuleCall cNameIDTerminalRuleCall_1_0 = (RuleCall) cNameAssignment_1.eContents().get(0);

        private final Keyword cLeftCurlyBracketKeyword_2 = (Keyword) cGroup.eContents().get(2);

        private final Assignment cReferencedInterfaceAssignment_3 = (Assignment) cGroup.eContents().get(3);

        private final CrossReference cReferencedInterfaceInterfaceTypeCrossReference_3_0 = (CrossReference) cReferencedInterfaceAssignment_3.eContents().get(0);

        private final RuleCall cReferencedInterfaceInterfaceTypeFQNParserRuleCall_3_0_1 = (RuleCall) cReferencedInterfaceInterfaceTypeCrossReference_3_0.eContents().get(1);

        private final Assignment cEndpointAssignment_4 = (Assignment) cGroup.eContents().get(4);

        private final RuleCall cEndpointEndpointParserRuleCall_4_0 = (RuleCall) cEndpointAssignment_4.eContents().get(0);

        private final Keyword cRightCurlyBracketKeyword_5 = (Keyword) cGroup.eContents().get(5);

        public ParserRule getRule() {
            return rule;
        }

        public Group getGroup() {
            return cGroup;
        }

        public Keyword getServiceKeyword_0() {
            return cServiceKeyword_0;
        }

        public Assignment getNameAssignment_1() {
            return cNameAssignment_1;
        }

        public RuleCall getNameIDTerminalRuleCall_1_0() {
            return cNameIDTerminalRuleCall_1_0;
        }

        public Keyword getLeftCurlyBracketKeyword_2() {
            return cLeftCurlyBracketKeyword_2;
        }

        public Assignment getReferencedInterfaceAssignment_3() {
            return cReferencedInterfaceAssignment_3;
        }

        public CrossReference getReferencedInterfaceInterfaceTypeCrossReference_3_0() {
            return cReferencedInterfaceInterfaceTypeCrossReference_3_0;
        }

        public RuleCall getReferencedInterfaceInterfaceTypeFQNParserRuleCall_3_0_1() {
            return cReferencedInterfaceInterfaceTypeFQNParserRuleCall_3_0_1;
        }

        public Assignment getEndpointAssignment_4() {
            return cEndpointAssignment_4;
        }

        public RuleCall getEndpointEndpointParserRuleCall_4_0() {
            return cEndpointEndpointParserRuleCall_4_0;
        }

        public Keyword getRightCurlyBracketKeyword_5() {
            return cRightCurlyBracketKeyword_5;
        }
    }

    public class InterfaceElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Interface");

        private final Group cGroup = (Group) rule.eContents().get(1);

        private final Keyword cInterfaceKeyword_0 = (Keyword) cGroup.eContents().get(0);

        private final Assignment cNameAssignment_1 = (Assignment) cGroup.eContents().get(1);

        private final RuleCall cNameIDTerminalRuleCall_1_0 = (RuleCall) cNameAssignment_1.eContents().get(0);

        private final Keyword cLeftCurlyBracketKeyword_2 = (Keyword) cGroup.eContents().get(2);

        private final Assignment cOperationAssignment_3 = (Assignment) cGroup.eContents().get(3);

        private final RuleCall cOperationOperationParserRuleCall_3_0 = (RuleCall) cOperationAssignment_3.eContents().get(0);

        private final Keyword cRightCurlyBracketKeyword_4 = (Keyword) cGroup.eContents().get(4);

        public ParserRule getRule() {
            return rule;
        }

        public Group getGroup() {
            return cGroup;
        }

        public Keyword getInterfaceKeyword_0() {
            return cInterfaceKeyword_0;
        }

        public Assignment getNameAssignment_1() {
            return cNameAssignment_1;
        }

        public RuleCall getNameIDTerminalRuleCall_1_0() {
            return cNameIDTerminalRuleCall_1_0;
        }

        public Keyword getLeftCurlyBracketKeyword_2() {
            return cLeftCurlyBracketKeyword_2;
        }

        public Assignment getOperationAssignment_3() {
            return cOperationAssignment_3;
        }

        public RuleCall getOperationOperationParserRuleCall_3_0() {
            return cOperationOperationParserRuleCall_3_0;
        }

        public Keyword getRightCurlyBracketKeyword_4() {
            return cRightCurlyBracketKeyword_4;
        }
    }

    public class OperationElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Operation");

        private final Group cGroup = (Group) rule.eContents().get(1);

        private final Keyword cOperationKeyword_0 = (Keyword) cGroup.eContents().get(0);

        private final Assignment cNameAssignment_1 = (Assignment) cGroup.eContents().get(1);

        private final RuleCall cNameIDTerminalRuleCall_1_0 = (RuleCall) cNameAssignment_1.eContents().get(0);

        public ParserRule getRule() {
            return rule;
        }

        public Group getGroup() {
            return cGroup;
        }

        public Keyword getOperationKeyword_0() {
            return cOperationKeyword_0;
        }

        public Assignment getNameAssignment_1() {
            return cNameAssignment_1;
        }

        public RuleCall getNameIDTerminalRuleCall_1_0() {
            return cNameIDTerminalRuleCall_1_0;
        }
    }

    public class EndpointElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Endpoint");

        private final Group cGroup = (Group) rule.eContents().get(1);

        private final Keyword cEndpointKeyword_0 = (Keyword) cGroup.eContents().get(0);

        private final Assignment cNameAssignment_1 = (Assignment) cGroup.eContents().get(1);

        private final RuleCall cNameIDTerminalRuleCall_1_0 = (RuleCall) cNameAssignment_1.eContents().get(0);

        private final Keyword cUsingInterfaceKeyword_2 = (Keyword) cGroup.eContents().get(2);

        private final Assignment cReferencedInterfaceAssignment_3 = (Assignment) cGroup.eContents().get(3);

        private final CrossReference cReferencedInterfaceInterfaceTypeCrossReference_3_0 = (CrossReference) cReferencedInterfaceAssignment_3.eContents().get(0);

        private final RuleCall cReferencedInterfaceInterfaceTypeFQNParserRuleCall_3_0_1 = (RuleCall) cReferencedInterfaceInterfaceTypeCrossReference_3_0.eContents().get(1);

        public ParserRule getRule() {
            return rule;
        }

        public Group getGroup() {
            return cGroup;
        }

        public Keyword getEndpointKeyword_0() {
            return cEndpointKeyword_0;
        }

        public Assignment getNameAssignment_1() {
            return cNameAssignment_1;
        }

        public RuleCall getNameIDTerminalRuleCall_1_0() {
            return cNameIDTerminalRuleCall_1_0;
        }

        public Keyword getUsingInterfaceKeyword_2() {
            return cUsingInterfaceKeyword_2;
        }

        public Assignment getReferencedInterfaceAssignment_3() {
            return cReferencedInterfaceAssignment_3;
        }

        public CrossReference getReferencedInterfaceInterfaceTypeCrossReference_3_0() {
            return cReferencedInterfaceInterfaceTypeCrossReference_3_0;
        }

        public RuleCall getReferencedInterfaceInterfaceTypeFQNParserRuleCall_3_0_1() {
            return cReferencedInterfaceInterfaceTypeFQNParserRuleCall_3_0_1;
        }
    }

    public class FQNElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "FQN");

        private final Group cGroup = (Group) rule.eContents().get(1);

        private final RuleCall cIDTerminalRuleCall_0 = (RuleCall) cGroup.eContents().get(0);

        private final Group cGroup_1 = (Group) cGroup.eContents().get(1);

        private final Keyword cFullStopKeyword_1_0 = (Keyword) cGroup_1.eContents().get(0);

        private final RuleCall cIDTerminalRuleCall_1_1 = (RuleCall) cGroup_1.eContents().get(1);

        public ParserRule getRule() {
            return rule;
        }

        public Group getGroup() {
            return cGroup;
        }

        public RuleCall getIDTerminalRuleCall_0() {
            return cIDTerminalRuleCall_0;
        }

        public Group getGroup_1() {
            return cGroup_1;
        }

        public Keyword getFullStopKeyword_1_0() {
            return cFullStopKeyword_1_0;
        }

        public RuleCall getIDTerminalRuleCall_1_1() {
            return cIDTerminalRuleCall_1_1;
        }
    }

    private WsdlDescriptionElements pWsdlDescription;

    private ServiceComponentElements pServiceComponent;

    private ServiceElements pService;

    private InterfaceElements pInterface;

    private OperationElements pOperation;

    private EndpointElements pEndpoint;

    private FQNElements pFQN;

    private final GrammarProvider grammarProvider;

    private TerminalsGrammarAccess gaTerminals;

    @Inject
    public ServiceQosWsdlGrammarAccess(GrammarProvider grammarProvider, TerminalsGrammarAccess gaTerminals) {
        this.grammarProvider = grammarProvider;
        this.gaTerminals = gaTerminals;
    }

    public Grammar getGrammar() {
        return grammarProvider.getGrammar(this);
    }

    public TerminalsGrammarAccess getTerminalsGrammarAccess() {
        return gaTerminals;
    }

    public WsdlDescriptionElements getWsdlDescriptionAccess() {
        return (pWsdlDescription != null) ? pWsdlDescription : (pWsdlDescription = new WsdlDescriptionElements());
    }

    public ParserRule getWsdlDescriptionRule() {
        return getWsdlDescriptionAccess().getRule();
    }

    public ServiceComponentElements getServiceComponentAccess() {
        return (pServiceComponent != null) ? pServiceComponent : (pServiceComponent = new ServiceComponentElements());
    }

    public ParserRule getServiceComponentRule() {
        return getServiceComponentAccess().getRule();
    }

    public ServiceElements getServiceAccess() {
        return (pService != null) ? pService : (pService = new ServiceElements());
    }

    public ParserRule getServiceRule() {
        return getServiceAccess().getRule();
    }

    public InterfaceElements getInterfaceAccess() {
        return (pInterface != null) ? pInterface : (pInterface = new InterfaceElements());
    }

    public ParserRule getInterfaceRule() {
        return getInterfaceAccess().getRule();
    }

    public OperationElements getOperationAccess() {
        return (pOperation != null) ? pOperation : (pOperation = new OperationElements());
    }

    public ParserRule getOperationRule() {
        return getOperationAccess().getRule();
    }

    public EndpointElements getEndpointAccess() {
        return (pEndpoint != null) ? pEndpoint : (pEndpoint = new EndpointElements());
    }

    public ParserRule getEndpointRule() {
        return getEndpointAccess().getRule();
    }

    public FQNElements getFQNAccess() {
        return (pFQN != null) ? pFQN : (pFQN = new FQNElements());
    }

    public ParserRule getFQNRule() {
        return getFQNAccess().getRule();
    }

    public TerminalRule getIDRule() {
        return gaTerminals.getIDRule();
    }

    public TerminalRule getINTRule() {
        return gaTerminals.getINTRule();
    }

    public TerminalRule getSTRINGRule() {
        return gaTerminals.getSTRINGRule();
    }

    public TerminalRule getML_COMMENTRule() {
        return gaTerminals.getML_COMMENTRule();
    }

    public TerminalRule getSL_COMMENTRule() {
        return gaTerminals.getSL_COMMENTRule();
    }

    public TerminalRule getWSRule() {
        return gaTerminals.getWSRule();
    }

    public TerminalRule getANY_OTHERRule() {
        return gaTerminals.getANY_OTHERRule();
    }
}
