package com.intel.gpe.client2.expert.explorer;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.w3c.dom.Element;
import com.intel.gpe.client2.AsyncClient;
import com.intel.gpe.client2.adapters.MessageAdapter;
import com.intel.gpe.client2.common.clientwrapper.ClientWrapper;
import com.intel.gpe.client2.common.panels.targetsystemchooser.TargetSystemChooserPanel;
import com.intel.gpe.client2.common.panels.wrappers.OKCancelWrapper;
import com.intel.gpe.client2.common.requests.ExceptionObserver;
import com.intel.gpe.client2.common.requests.JobClientCache;
import com.intel.gpe.client2.defaults.IPreferences;
import com.intel.gpe.client2.expert.ExpertNodes;
import com.intel.gpe.client2.expert.i18n.Messages;
import com.intel.gpe.client2.expert.i18n.MessagesKeys;
import com.intel.gpe.client2.expert.panels.JobsPanel;
import com.intel.gpe.client2.expert.workfloweditor.MainWorkflow;
import com.intel.gpe.client2.expert.workfloweditor.actions.PrintWorkflowVariablesAction;
import com.intel.gpe.client2.expert.workfloweditor.panels.WorkflowEditorPanel;
import com.intel.gpe.client2.panels.GPEPanelResult;
import com.intel.gpe.client2.providers.FileProvider;
import com.intel.gpe.client2.providers.GridBeanProvider;
import com.intel.gpe.client2.providers.OutcomeProvider;
import com.intel.gpe.client2.requests.BaseRequest;
import com.intel.gpe.client2.security.GPESecurityManager;
import com.intel.gpe.clients.api.JobClient;
import com.intel.gpe.clients.api.WSRPClient;
import com.intel.gpe.clients.api.workflow.BoundVariable;
import com.intel.gpe.clients.api.workflow.VariableValue;
import com.intel.gpe.clients.api.workflow.WorkflowJobClient;
import com.intel.gui.controls2.configurable.IConfigurable;
import com.intel.util.observer.IObserver;

/**
 * @version $Id: WorkflowTreeNode.java,v 1.35 2007/02/22 13:02:29 vashorin Exp $
 * @author Valery Shorin
 */
public class WorkflowTreeNode extends JobNode {

    private JAXBContext jc;

    public WorkflowTreeNode(String name, JobExplorerTree tree, DefaultTreeModel model, IConfigurable parent, AsyncClient asyncClient, IPreferences userDefaults, GPESecurityManager securityManager, MessageAdapter messageAdapter, JobsPanel jobsPanel, OutcomeProvider outcomeProvider, FileProvider fileProvider, GridBeanProvider gridBeanProvider, JAXBContext jc) {
        super(name, tree, model, parent, asyncClient, userDefaults, securityManager, messageAdapter, jobsPanel, outcomeProvider, fileProvider, gridBeanProvider);
        setUserObject(getDisplayName());
        model.reload(this);
        this.jc = jc;
    }

    public WorkflowTreeNode(ClientWrapper<JobClient, JobClientCache> client, JobExplorerTree tree, DefaultTreeModel model, IConfigurable parent, AsyncClient asyncClient, IPreferences userDefaults, GPESecurityManager securityManager, MessageAdapter messageAdapter, JobsPanel jobsPanel, OutcomeProvider outcomeProvider, FileProvider fileProvider, GridBeanProvider gridBeanProvider, JAXBContext jc) {
        super(client, tree, model, parent, asyncClient, userDefaults, securityManager, messageAdapter, jobsPanel, outcomeProvider, fileProvider, gridBeanProvider);
        this.jc = jc;
    }

    public WorkflowTreeNode(WorkflowTargetSystemNode parentNode, MainWorkflow wf, JobExplorerTree tree, DefaultTreeModel model, IConfigurable parent, AsyncClient asyncClient, IPreferences userDefaults, GPESecurityManager securityManager, MessageAdapter messageAdapter, JobsPanel jobsPanel, OutcomeProvider outcomeProvider, FileProvider fileProvider, GridBeanProvider gridBeanProvider, JAXBContext jc) throws Exception {
        super(Messages.getString(MessagesKeys.expert_explorer_WorkflowTreeNode_Loading), tree, model, parent, asyncClient, userDefaults, securityManager, messageAdapter, jobsPanel, outcomeProvider, fileProvider, gridBeanProvider);
        parentNode.add(this);
        this.jc = jc;
        panel = new WorkflowEditorPanel(parent, ExpertNodes.WorkflowEditorPanel, this, asyncClient, messageAdapter, userDefaults, fileProvider, securityManager, outcomeProvider, wf, gridBeanProvider, jc);
        name = panel.getJobName();
        jobsPanel.addPanel(name, panel);
        setUserObject(getDisplayName());
    }

    @Override
    public void showPanel() {
        if (panel == null) {
            panel = new WorkflowEditorPanel(this.parent, name, this, asyncClient, messageAdapter, userDefaults, fileProvider, securityManager, outcomeProvider, gridBeanProvider, jc);
            jobsPanel.addPanel(name, panel);
        }
        jobsPanel.activatePanel(panel);
    }

    @Override
    public JPopupMenu getPopupMenu() {
        JPopupMenu menu = new JPopupMenu();
        if (getClientItem().getClient() != null && !isClientValid()) {
            menu.add(actionDelete);
            menu.add(panel.getSaveAction(new SaveObserver()));
            return menu;
        }
        menu.add(new AbstractAction(Messages.getString(MessagesKeys.expert_explorer_WorkflowTreeNode_Refresh)) {

            public void actionPerformed(ActionEvent e) {
                if (panel != null) {
                    asyncClient.executeRequest(new RefreshWorkflowRequest(), new ExceptionObserver(Messages.getString(MessagesKeys.expert_explorer_WorkflowTreeNode_Cannot_refresh_workflow_status), messageAdapter));
                }
                refreshAction.actionPerformed(e);
            }
        });
        if (panel != null) {
            menu.add(panel.getSaveAction(new SaveObserver()));
            menu.add(new AbstractAction(Messages.getString(MessagesKeys.expert_explorer_WorkflowTreeNode_Copy_to)) {

                public void actionPerformed(ActionEvent e) {
                    final TargetSystemChooserPanel tssChooser = new TargetSystemChooserPanel(WorkflowTreeNode.this.parent, ExpertNodes.TargetSystemChooserPanel, asyncClient, ((WorkflowEditorPanel) panel).getClient().getRegistryClientWrapperList(), messageAdapter);
                    final OKCancelWrapper targetSystemChooserWrapper = new OKCancelWrapper(tssChooser, Messages.getString(MessagesKeys.expert_explorer_WorkflowTreeNode_Select_target_system), tssChooser);
                    messageAdapter.show(targetSystemChooserWrapper);
                    if (targetSystemChooserWrapper.getResult() == GPEPanelResult.OK) {
                        RootNode root = (RootNode) getParent().getParent().getParent();
                        JobExplorerNode<?, ?, ?, ?> target = root.findNode(tssChooser.getTargetSystem().getClient());
                        AbstractAction action = ((WorkflowEditorPanel) panel).getCopyAction(null, target);
                        action.actionPerformed(null);
                    }
                }
            });
        }
        if (getClientItem().getClient() == null) {
            menu.add(panel.getSubmitAction(new SubmitObserver()));
            menu.add(actionRename);
            menu.add(actionDelete);
        } else {
            menu.add(actionStart);
            menu.add(actionAbort);
            menu.add(actionHold);
            menu.add(actionResume);
            menu.add(actionChangeTT);
            menu.add(actionDestroy);
            menu.add(actionDisplayRP);
            menu.add(new PrintWorkflowVariablesAction((WorkflowJobClient) (getClientItem().getClient()), messageAdapter));
            if (panel != null) {
            } else {
                menu.add(new AbstractAction(Messages.getString(MessagesKeys.expert_explorer_WorkflowTreeNode_Reconstruct)) {

                    public void actionPerformed(ActionEvent e) {
                        asyncClient.executeRequest(new ReconstrucInputRequest(), null);
                    }
                });
            }
        }
        return menu;
    }

    @Override
    public void doubleClick() {
        if (panel != null) {
            jobsPanel.activatePanel(panel);
        }
    }

    @Override
    protected String getDisplayName() {
        String result = Messages.getString(MessagesKeys.expert_explorer_WorkflowTreeNode_workflow);
        if (getClientItem().getClient() != null) {
            if (isClientValid()) {
                result = result + " " + getClientItem().getCache().getId() + " " + getClientItem().getCache().getStatusString();
            } else {
                result = result + " " + Messages.getString(MessagesKeys.expert_explorer_WorkflowTreeNode_Destroyed);
            }
        } else {
            result = result + " " + name;
        }
        return result;
    }

    @Override
    public JobExplorerNode<WSRPClient, Object, ?, ?> createChildNode(ClientWrapper<WSRPClient, Object> client) {
        return null;
    }

    @Override
    protected String getIconPath() {
        if (!isClientValid()) {
            return "images/workflow-job-inv.gif";
        }
        return "images/workflow-job.gif";
    }

    @Override
    public String getToolTipText() {
        return null;
    }

    private class SubmitObserver implements IObserver {

        public void observableUpdate(Object theObserved, Object changeCode) {
            if (changeCode instanceof Exception) {
                messageAdapter.showException(Messages.getString(MessagesKeys.expert_explorer_WorkflowTreeNode_Job_not_submitted), (Exception) changeCode);
                ((WorkflowEditorPanel) panel).setMode(WorkflowEditorPanel.MODE_EDIT);
            } else if (changeCode instanceof JobClient) {
                ((WorkflowEditorPanel) panel).setMode(WorkflowEditorPanel.MODE_VIEW);
                ((WorkflowEditorPanel) panel).getWorkflow().setJobClient((WorkflowJobClient) changeCode);
                setClientItem(new ClientWrapper<JobClient, JobClientCache>((JobClient) changeCode, new JobClientCache()));
                getClientItem().addUpdateListener(WorkflowTreeNode.this);
                reload();
                model.reload(WorkflowTreeNode.this);
            }
        }
    }

    private class SaveObserver implements IObserver {

        public void observableUpdate(Object theObserved, Object changeCode) {
            if (changeCode instanceof Exception) {
                messageAdapter.showException(Messages.getString(MessagesKeys.expert_explorer_WorkflowTreeNode_Job_not_saved), (Exception) changeCode);
            }
        }
    }

    private class ReconstrucInputRequest extends BaseRequest {

        public ReconstrucInputRequest() {
            super(Messages.getString(MessagesKeys.expert_explorer_WorkflowTreeNode_Workflow_reconstructing));
        }

        public Object perform() throws Throwable {
            WorkflowJobClient cl = ((WorkflowJobClient) (getClientItem().getClient()));
            try {
                List<BoundVariable<VariableValue>> y = cl.getVariables();
                for (BoundVariable<VariableValue> var : y) {
                    if (var.getVariable().getName().equals(Messages.getString(MessagesKeys.expert_explorer_WorkflowTreeNode_savedJob))) {
                        Element el = var.getValue().getElementVariableValue();
                        if (el == null) {
                            messageAdapter.showMessage(Messages.getString(MessagesKeys.expert_explorer_WorkflowTreeNode_Cannot_reconstruct_input__saved_job_variable_is_empty));
                            break;
                        }
                        Unmarshaller m = jc.createUnmarshaller();
                        MainWorkflow wf = (MainWorkflow) m.unmarshal(el.getFirstChild());
                        panel = new WorkflowEditorPanel(WorkflowTreeNode.this.parent, ExpertNodes.WorkflowEditorPanel, WorkflowTreeNode.this, asyncClient, messageAdapter, userDefaults, fileProvider, securityManager, outcomeProvider, wf, gridBeanProvider, jc);
                        ((WorkflowEditorPanel) panel).getWorkflow().setJobClient(cl);
                        ((WorkflowEditorPanel) panel).setMode(WorkflowEditorPanel.MODE_VIEW);
                        name = panel.getJobName();
                        jobsPanel.addPanel(name, panel);
                        setUserObject(getDisplayName());
                        break;
                    }
                }
                if (panel == null) {
                    messageAdapter.showMessage(Messages.getString(MessagesKeys.expert_explorer_WorkflowTreeNode_Cannot_reconstruct_input__saved_job_variable_not_found));
                }
            } catch (Exception ex) {
                messageAdapter.showException(Messages.getString(MessagesKeys.expert_explorer_WorkflowTreeNode_Cannot_reconstruct_input), ex);
            }
            reload();
            model.reload(WorkflowTreeNode.this);
            return null;
        }
    }

    private class RefreshWorkflowRequest extends BaseRequest {

        private RefreshWorkflowRequest() {
            super(Messages.getString(MessagesKeys.expert_explorer_Workflow_Refreshing));
        }

        public Object perform() throws Throwable {
            ((WorkflowEditorPanel) panel).getWorkflow().checkStatuses();
            ((WorkflowEditorPanel) panel).getWorkflow().refresh();
            return null;
        }
    }
}
