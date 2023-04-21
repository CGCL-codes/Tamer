package hu.cubussapiens.modembed.notation.implementation.ui.contentassist;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.*;
import org.eclipse.xtext.common.ui.contentassist.TerminalsProposalProvider;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;

/**
 * Represents a generated, default implementation of interface {@link IProposalProvider}.
 * Methods are dynamically dispatched on the first parameter, i.e., you can override them 
 * with a more concrete subtype. 
 */
@SuppressWarnings("all")
public class AbstractAtomicImplementationNotationProposalProvider extends TerminalsProposalProvider {

    public void completeNImplementation_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeNImplementation_For(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeNImplementation_Platform(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeNImplementation_Variables(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeNImplementation_Impls(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeNVariable_Type(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeNVariable_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeNInterfaceImpl_Role(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeNInterfaceImpl_Parameters(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeNInterfaceImpl_Steps(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeNImplLabel_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeNSet_Target(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        lookupCrossReference(((CrossReference) assignment.getTerminal()), context, acceptor);
    }

    public void completeNSet_Exp(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeNECall_Op(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeNECall_E(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeNELiteral_Value(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeNEVariable_Var(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        lookupCrossReference(((CrossReference) assignment.getTerminal()), context, acceptor);
    }

    public void completeNELabel_Label(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        lookupCrossReference(((CrossReference) assignment.getTerminal()), context, acceptor);
    }

    public void completeNEAddition_E1(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeNEAddition_E2(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeNEMultiplication_E1(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeNEMultiplication_E2(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void complete_NImplementation(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_NVariable(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_NInterfaceImpl(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_NImplStep(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_NImplLabel(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_NSet(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_NExpression(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_NECall(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_NELiteral(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_NEVariable(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_NELabel(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_NEAddition(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_NEMultiplication(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }
}
