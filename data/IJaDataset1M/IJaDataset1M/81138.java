package jolie.xtext.ui.contentassist;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.*;
import org.eclipse.xtext.ui.editor.contentassist.AbstractJavaBasedContentProposalProvider;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;

/**
 * Represents a generated, default implementation of interface {@link IProposalProvider}.
 * Methods are dynamically dispatched on the first parameter, i.e., you can override them 
 * with a more concrete subtype. 
 */
@SuppressWarnings("all")
public class AbstractJolieProposalProvider extends AbstractJavaBasedContentProposalProvider {

    public void completeProgram_Constants(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeProgram_Include(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeProgram_Ports(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeProgram_Interface(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeProgram_Types(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeProgram_Init(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeProgram_Execution(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeProgram_Define(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeProgram_Embedded(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeProgram_Main(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeConstant_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeInclude_ImportURI(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeFileName_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeType_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeType_Native_type_sub(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeType_Typedef(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeTypedef_Subtypes(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeSubtypes_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeSubtypes_Native_type(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeSubtypes_Typedef(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeNative_type_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void completeNative_type_Type(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        lookupCrossReference(((CrossReference) assignment.getTerminal()), context, acceptor);
    }

    public void completeEmbedded_String(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeEmbedded_In(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeEmbedded_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeDefine_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeDefine_Mainrocess(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeInit_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void completeInit_Mainrocess(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeMain_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void completeMain_Mainrocess(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeMainProcess_ParallelStatement(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeProcess_ParallelStatement(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeParallelStatement_SequenceStatement(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeSequenceStatement_BasicStatement(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeNDChoiceStatement_Operation(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        lookupCrossReference(((CrossReference) assignment.getTerminal()), context, acceptor);
    }

    public void completeNDChoiceStatement_Op(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeNDChoiceStatement_MainProcess(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeBasicStatement_Process(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeBasicStatement_AssignStatementOrPostIncrementDecrement(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeBasicStatement_NDChoiceStatement(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeBasicStatement_PreIncrementDecrement(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeBasicStatement_Call(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        lookupCrossReference(((CrossReference) assignment.getTerminal()), context, acceptor);
    }

    public void completeBasicStatement_Operation(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        lookupCrossReference(((CrossReference) assignment.getTerminal()), context, acceptor);
    }

    public void completeBasicStatement_Op(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeBasicStatement_Scope(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeBasicStatement_Compensate(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeBasicStatement_Throw(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeBasicStatement_Install(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeIs_function_VariablePath(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeInstall_InstallFunction(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeThrow_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeThrow_Espression(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeCompensate_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeCompensate_MainProcess(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeScope_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeScope_MainProcess(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeInputOrOutputOperationDefOrCall_VariablePath(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeInputOrOutputOperationDefOrCall_InputOperation(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeInputOrOutputOperationDefOrCall_OutputPortCall(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeLinkIn_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeLinkOut_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeAssignStatementOrPostIncrementDecrementOrInputOperation_VariablePath(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeAssignStatementOrPostIncrementDecrementOrInputOperation_RightSide(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeRightSide_Expression(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeRightSide_VariablePath(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeSynchronized_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeSynchronized_MainProcess(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeUndef_VariablePath(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeOutputPortCall_Port(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        lookupCrossReference(((CrossReference) assignment.getTerminal()), context, acceptor);
    }

    public void completeOutputPortCall_Expression(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeOutputPortCall_VariablePath(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeOutputPortCall_InstallFunction(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeInstallFunciton_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeInstallFunciton_ParallelStatement(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeInputOperation_Expression(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeInputOperation_MainProcess(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completePreIncrementDecrement_VariablePath(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeIf_Condition(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeIf_IfProcess(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeIf_ElseProcess(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeIf_IfNasted(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeFor_ParallelStatement(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeFor_Condition(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeFor_Body(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeCondition_Condition(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeCondition_VariablePath(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeCondition_IsF(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeCondition_RightCondition(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeRightCondition_Expression(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeForeach_Var1(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeForeach_Var2(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeForeach_Body(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeWhile_Condition(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeWhile_MainProcess(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeExpression_Op(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) ((Alternatives) assignment.getTerminal()).getElements().get(0)), context, acceptor);
        completeRuleCall(((RuleCall) ((Alternatives) assignment.getTerminal()).getElements().get(1)), context, acceptor);
        completeRuleCall(((RuleCall) ((Alternatives) assignment.getTerminal()).getElements().get(2)), context, acceptor);
        completeRuleCall(((RuleCall) ((Alternatives) assignment.getTerminal()).getElements().get(3)), context, acceptor);
    }

    public void completeExpression_Right(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeTerminalExpression_Value(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeTerminalExpression_VariablePath(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeVariablePath_Dot(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeVariablePath_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeVariablePath_Children(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeWith_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeWith_Mainrocess(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeInterface_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeInterface_RequestResponseOperation(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeInterface_OneWayOperation(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completePort_InputPortStatement(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completePort_OutputPortStatement(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeInputPortStatement_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeInputPortStatement_Location(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeInputPortStatement_Protocol(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeInputPortStatement_OneWayOperation(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeInputPortStatement_RequestResponseOperation(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeInputPortStatement_Redirects(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeInputPortStatement_Aggregates(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeInputPortStatement_Intefaces(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeOutputPortStatement_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeOutputPortStatement_Location(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeOutputPortStatement_Protocol(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeOutputPortStatement_OneWayOperation(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeOutputPortStatement_RequestResponseOperation(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeOutputPortStatement_Intefaces(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeOneWayOperation_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeRequestResponseOperation_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeOneWayOperationSignature_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeOneWayOperationSignature_TypeOfThrow(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeOneWayOperationSignature_Op(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeRequestResponseSignature_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeRequestResponseSignature_TypeOfThrow(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeRequestResponseSignature_Faults(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeRequestResponseSignature_Op(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeThrowsClause_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeThrowsClause_Type(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeTypeOfThrow_Type(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        lookupCrossReference(((CrossReference) assignment.getTerminal()), context, acceptor);
    }

    public void completeTypeOfThrow_NaiveType(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeLocation_Uri(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeUri_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeInterfaces_Interface(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        lookupCrossReference(((CrossReference) assignment.getTerminal()), context, acceptor);
    }

    public void completeProtocol_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeProtocol_ProtocolConfiguration(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeProtocolConfiguration_MainProcess(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeRedirects_RedRef(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeRedirectDef_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeRedirectDef_OutputPortIdentifier(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void completeAggregates_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        completeRuleCall(((RuleCall) assignment.getTerminal()), context, acceptor);
    }

    public void complete_Program(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Constant(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Execution(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Include(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_FileName(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Type(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Typedef(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Subtypes(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Cardinality(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Native_type(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Embedded(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Define(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Init(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Main(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_MainProcess(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Process(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_ParallelStatement(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_SequenceStatement(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_NDChoiceStatement(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_BasicStatement(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Is_function(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Install(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Throw(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Compensate(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Scope(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_InputOrOutputOperationDefOrCall(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_linkIn(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_linkOut(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_cH(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Exit(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_AssignStatementOrPostIncrementDecrementOrInputOperation(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_RightSide(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Synchronized(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Undef(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_OutputPortCall(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_InstallFunciton(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_InputOperation(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_PreIncrementDecrement(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_If(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_For(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Body(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Condition(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_RightCondition(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Foreach(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_While(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Expression(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_TerminalExpression(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_VariablePath(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_With(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Interface(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Port(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_InputPortStatement(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_OutputPortStatement(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_OneWayOperation(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_RequestResponseOperation(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_OneWayOperationSignature(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_RequestResponseSignature(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_ThrowsClause(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_TypeOfThrow(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Location(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Uri(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Interfaces(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Protocol(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_ProtocolConfiguration(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Redirects(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_RedirectDef(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_Aggregates(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_CONCURRENT(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_SEQUENTIAL(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_SEMICOLON(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_COLON(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_PLUS(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_VERT(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_ASSIGN(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_DOT(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_COMMA(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_AT(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_CHOICE(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_DECREMENT(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_ASTERISK(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_QUESTION(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_DIVIDE(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_POINTSTO(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_DEEPCOPYLEFT(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_MINUS(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_PERCENT_SIGN(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_EQUAL(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_LANGLE(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_RANGLE(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_HASH(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_MAJOR_OR_EQUAL(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_MINOR_OR_EQUAL(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_NOT_EQUAL(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_NOT(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_REAL(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_ID(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_INT(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_STRING(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_ML_COMMENT(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_SL_COMMENT(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_WS(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }

    public void complete_ANY_OTHER(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    }
}
