package com.intel.gpe.client2.expert.workfloweditor.actions;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.List;
import javax.swing.AbstractAction;
import com.intel.gpe.client2.adapters.MessageAdapter;
import com.intel.gpe.client2.expert.i18n.Messages;
import com.intel.gpe.client2.expert.i18n.MessagesKeys;
import com.intel.gpe.clients.api.workflow.BoundVariable;
import com.intel.gpe.clients.api.workflow.VariableValue;
import com.intel.gpe.clients.api.workflow.WorkflowJobClient;

/**
 * @version $Id: PrintWorkflowVariablesAction.java,v 1.4 2006/10/23 12:43:26 dizhigul Exp $
 * @author Valery Shorin
 */
public class PrintWorkflowVariablesAction extends AbstractAction {

    private WorkflowJobClient jobClient;

    private MessageAdapter messageAdapter;

    public PrintWorkflowVariablesAction(WorkflowJobClient client, MessageAdapter messageAdapter) {
        super(Messages.getString(MessagesKeys.expert_workfloweditor_actions_PrintWorkflowVariablesAction_Print_Variables));
        this.jobClient = client;
        this.messageAdapter = messageAdapter;
    }

    public void actionPerformed(ActionEvent ev) {
        try {
            List<BoundVariable<VariableValue>> vars = jobClient.getVariables();
            for (BoundVariable<VariableValue> var : vars) {
                System.out.println(Messages.getString(MessagesKeys.expert_workfloweditor_actions_PrintWorkflowVariablesAction_VARIABLE));
                System.out.println(MessageFormat.format(Messages.getString(MessagesKeys.expert_workfloweditor_actions_PrintWorkflowVariablesAction_name), var.getVariable().getName()));
                System.out.println(MessageFormat.format(Messages.getString(MessagesKeys.expert_workfloweditor_actions_PrintWorkflowVariablesAction_part), var.getVariable().getPart()));
                System.out.println(MessageFormat.format(Messages.getString(MessagesKeys.expert_workfloweditor_actions_PrintWorkflowVariablesAction_value), var.getValue()));
            }
        } catch (Exception e) {
            messageAdapter.showException(Messages.getString(MessagesKeys.expert_workfloweditor_actions_PrintWorkflowVariablesAction_Getting_variables_failed), e);
        }
    }
}
