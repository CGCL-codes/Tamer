package edu.indiana.extreme.xbaya.component;

import edu.indiana.extreme.xbaya.component.ws.WSComponent;
import edu.indiana.extreme.xbaya.component.ws.WorkflowComponent;
import edu.indiana.extreme.xbaya.graph.Graph;
import edu.indiana.extreme.xbaya.graph.GraphException;
import edu.indiana.extreme.xbaya.graph.subworkflow.SubWorkflowNode;
import edu.indiana.extreme.xbaya.graph.ws.WorkflowNode;
import edu.indiana.extreme.xbaya.ode.ODEClient;
import edu.indiana.extreme.xbaya.wf.Workflow;
import edu.indiana.extreme.xbaya.workflow.WorkflowClient;
import edu.indiana.extreme.xbaya.workflow.WorkflowEngineException;

/**
 * @author Chathura Herath
 */
public class SubWorkflowComponent extends WSComponent {

    private Workflow workflow;

    private SubWorkflowComponent(Workflow workflow) throws ComponentException {
        super(workflow.getWorkflowWSDL());
        this.workflow = workflow;
    }

    public static SubWorkflowComponent getInstance(Workflow workflow) throws ComponentException {
        new ODEClient().getInputs(workflow);
        return new SubWorkflowComponent(workflow);
    }

    /**
	 * @param workflowClient
	 * @return The workflow
	 * @throws ComponentException
	 * @throws WorkflowEngineException
	 * @throws GraphException
	 */
    public Workflow getWorkflow(WorkflowClient workflowClient) throws GraphException, WorkflowEngineException, ComponentException {
        return this.workflow;
    }

    /**
	 * @see edu.indiana.extreme.xbaya.component.ws.WSComponent#createNode(edu.indiana.extreme.xbaya.graph.Graph)
	 */
    @Override
    public SubWorkflowNode createNode(Graph graph) {
        SubWorkflowNode node = new SubWorkflowNode(graph);
        node.setWorkflow(workflow.clone());
        node.setName(getName());
        node.setComponent(this);
        node.createID();
        createPorts(node);
        return node;
    }
}
