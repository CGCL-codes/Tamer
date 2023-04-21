package net.sf.devtool.uwi.contentassist;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.*;
import org.eclipse.xtext.common.contentassist.TerminalsProposalProvider;
import org.eclipse.xtext.ui.core.editor.contentassist.ICompletionProposalAcceptor;
import org.eclipse.xtext.ui.core.editor.contentassist.ContentAssistContext;

/**
 * Represents a generated, default implementation of interface {@link IProposalProvider}.
 * Methods are dynamically dispatched on the first parameter, i.e., you can override them 
 * with a more concrete subtype. 
 */
public class AbstractInstallDSLProposalProvider extends TerminalsProposalProvider {

    private static final Logger logger = Logger.getLogger(AbstractInstallDSLProposalProvider.class);

    public void completeModel_Version(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        if (logger.isDebugEnabled()) {
            logger.debug("completeModel_Version feature '" + assignment.getFeature() + "' terminal '" + assignment.getTerminal() + "' cardinality '" + assignment.getCardinality() + "' and prefix '" + context.getPrefix() + "'");
        }
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeModel_Body(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        if (logger.isDebugEnabled()) {
            logger.debug("completeModel_Body feature '" + assignment.getFeature() + "' terminal '" + assignment.getTerminal() + "' cardinality '" + assignment.getCardinality() + "' and prefix '" + context.getPrefix() + "'");
        }
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeVersion_Version(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        if (logger.isDebugEnabled()) {
            logger.debug("completeVersion_Version feature '" + assignment.getFeature() + "' terminal '" + assignment.getTerminal() + "' cardinality '" + assignment.getCardinality() + "' and prefix '" + context.getPrefix() + "'");
        }
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeParallelAction_Actions(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        if (logger.isDebugEnabled()) {
            logger.debug("completeParallelAction_Actions feature '" + assignment.getFeature() + "' terminal '" + assignment.getTerminal() + "' cardinality '" + assignment.getCardinality() + "' and prefix '" + context.getPrefix() + "'");
        }
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeDownload_NameURI(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        if (logger.isDebugEnabled()) {
            logger.debug("completeDownload_NameURI feature '" + assignment.getFeature() + "' terminal '" + assignment.getTerminal() + "' cardinality '" + assignment.getCardinality() + "' and prefix '" + context.getPrefix() + "'");
        }
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeRun_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        if (logger.isDebugEnabled()) {
            logger.debug("completeRun_Name feature '" + assignment.getFeature() + "' terminal '" + assignment.getTerminal() + "' cardinality '" + assignment.getCardinality() + "' and prefix '" + context.getPrefix() + "'");
        }
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeRun_ParameterList(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        if (logger.isDebugEnabled()) {
            logger.debug("completeRun_ParameterList feature '" + assignment.getFeature() + "' terminal '" + assignment.getTerminal() + "' cardinality '" + assignment.getCardinality() + "' and prefix '" + context.getPrefix() + "'");
        }
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeParameterList_Parameters(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        if (logger.isDebugEnabled()) {
            logger.debug("completeParameterList_Parameters feature '" + assignment.getFeature() + "' terminal '" + assignment.getTerminal() + "' cardinality '" + assignment.getCardinality() + "' and prefix '" + context.getPrefix() + "'");
        }
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeParameter_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        if (logger.isDebugEnabled()) {
            logger.debug("completeParameter_Name feature '" + assignment.getFeature() + "' terminal '" + assignment.getTerminal() + "' cardinality '" + assignment.getCardinality() + "' and prefix '" + context.getPrefix() + "'");
        }
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeParameter_Value(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        if (logger.isDebugEnabled()) {
            logger.debug("completeParameter_Value feature '" + assignment.getFeature() + "' terminal '" + assignment.getTerminal() + "' cardinality '" + assignment.getCardinality() + "' and prefix '" + context.getPrefix() + "'");
        }
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeParameter_Parameter(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        if (logger.isDebugEnabled()) {
            logger.debug("completeParameter_Parameter feature '" + assignment.getFeature() + "' terminal '" + assignment.getTerminal() + "' cardinality '" + assignment.getCardinality() + "' and prefix '" + context.getPrefix() + "'");
        }
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void complete_Model(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        if (logger.isDebugEnabled()) {
            logger.debug("complete_Model '" + ruleCall.getRule().getName() + "' cardinality '" + ruleCall.getCardinality() + "' for model '" + context.getCurrentModel() + "' and prefix '" + context.getPrefix() + "'");
        }
    }

    public void complete_Version(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        if (logger.isDebugEnabled()) {
            logger.debug("complete_Version '" + ruleCall.getRule().getName() + "' cardinality '" + ruleCall.getCardinality() + "' for model '" + context.getCurrentModel() + "' and prefix '" + context.getPrefix() + "'");
        }
    }

    public void complete_Body(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        if (logger.isDebugEnabled()) {
            logger.debug("complete_Body '" + ruleCall.getRule().getName() + "' cardinality '" + ruleCall.getCardinality() + "' for model '" + context.getCurrentModel() + "' and prefix '" + context.getPrefix() + "'");
        }
    }

    public void complete_ParallelAction(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        if (logger.isDebugEnabled()) {
            logger.debug("complete_ParallelAction '" + ruleCall.getRule().getName() + "' cardinality '" + ruleCall.getCardinality() + "' for model '" + context.getCurrentModel() + "' and prefix '" + context.getPrefix() + "'");
        }
    }

    public void complete_SingleAction(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        if (logger.isDebugEnabled()) {
            logger.debug("complete_SingleAction '" + ruleCall.getRule().getName() + "' cardinality '" + ruleCall.getCardinality() + "' for model '" + context.getCurrentModel() + "' and prefix '" + context.getPrefix() + "'");
        }
    }

    public void complete_Download(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        if (logger.isDebugEnabled()) {
            logger.debug("complete_Download '" + ruleCall.getRule().getName() + "' cardinality '" + ruleCall.getCardinality() + "' for model '" + context.getCurrentModel() + "' and prefix '" + context.getPrefix() + "'");
        }
    }

    public void complete_Run(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        if (logger.isDebugEnabled()) {
            logger.debug("complete_Run '" + ruleCall.getRule().getName() + "' cardinality '" + ruleCall.getCardinality() + "' for model '" + context.getCurrentModel() + "' and prefix '" + context.getPrefix() + "'");
        }
    }

    public void complete_ParameterList(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        if (logger.isDebugEnabled()) {
            logger.debug("complete_ParameterList '" + ruleCall.getRule().getName() + "' cardinality '" + ruleCall.getCardinality() + "' for model '" + context.getCurrentModel() + "' and prefix '" + context.getPrefix() + "'");
        }
    }

    public void complete_Parameter(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        if (logger.isDebugEnabled()) {
            logger.debug("complete_Parameter '" + ruleCall.getRule().getName() + "' cardinality '" + ruleCall.getCardinality() + "' for model '" + context.getCurrentModel() + "' and prefix '" + context.getPrefix() + "'");
        }
    }

    public void complete_QualifiedName(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        if (logger.isDebugEnabled()) {
            logger.debug("complete_QualifiedName '" + ruleCall.getRule().getName() + "' cardinality '" + ruleCall.getCardinality() + "' for model '" + context.getCurrentModel() + "' and prefix '" + context.getPrefix() + "'");
        }
    }
}
