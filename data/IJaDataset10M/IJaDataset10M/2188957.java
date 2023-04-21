package org.streets.workflow.engine.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.streets.workflow.engine.IEdge;
import org.streets.workflow.engine.INode;
import org.streets.workflow.engine.WorkflowException;
import org.streets.workflow.engine.mem.Activity;
import org.streets.workflow.engine.mem.EndNode;
import org.streets.workflow.engine.mem.Loop;
import org.streets.workflow.engine.mem.StartNode;
import org.streets.workflow.engine.mem.SyncRouter;
import org.streets.workflow.engine.mem.Transition;
import org.streets.workflow.engine.plugin.INodeNetExtension;
import org.streets.workflow.model.WFProcess;
import org.streets.workflow.model.net.WFActivity;
import org.streets.workflow.model.net.WFEndNode;
import org.streets.workflow.model.net.WFLoop;
import org.streets.workflow.model.net.WFStartNode;
import org.streets.workflow.model.net.WFSyncRouter;
import org.streets.workflow.model.net.WFTransition;

/**
 *
 */
public class NodeNet {

    private final WFProcess processModel;

    private final StartNode startNode;

    private final Map<String, INode> nodeMap = new HashMap<String, INode>();

    private final Map<String, IEdge> edgeMap = new HashMap<String, IEdge>();

    /**
     * wangmj  初始化一个工作流网实例,将引擎的扩展属性，注入到对应的工作流元素中
     * @param process
     * @param netExtensions
     * @throws KernelException
     */
    public NodeNet(WFProcess process, final Map<String, List<INodeNetExtension>> netExtensions) throws WorkflowException {
        this.processModel = process;
        WFStartNode wf_start = processModel.getStartNode();
        startNode = new StartNode(wf_start);
        List<INodeNetExtension> extensionList = netExtensions.get(startNode.getExtensionTargetName());
        for (int i = 0; extensionList != null && i < extensionList.size(); i++) {
            INodeNetExtension extension = extensionList.get(i);
            startNode.register(extension);
        }
        nodeMap.put(wf_start.getId(), startNode);
        List<WFActivity> activities = processModel.getActivities();
        for (int i = 0; i < activities.size(); i++) {
            WFActivity activity = activities.get(i);
            Activity activityInstance = new Activity(activity);
            extensionList = netExtensions.get(activityInstance.getExtensionTargetName());
            for (int j = 0; extensionList != null && j < extensionList.size(); j++) {
                INodeNetExtension extension = extensionList.get(j);
                activityInstance.register(extension);
            }
            nodeMap.put(activity.getId(), activityInstance);
        }
        List<WFSyncRouter> synchronizers = processModel.getSynchronizers();
        for (int i = 0; i < synchronizers.size(); i++) {
            WFSyncRouter synchronizer = synchronizers.get(i);
            SyncRouter synchronizerInstance = new SyncRouter(synchronizer);
            extensionList = netExtensions.get(synchronizerInstance.getExtensionTargetName());
            for (int j = 0; extensionList != null && j < extensionList.size(); j++) {
                INodeNetExtension extension = extensionList.get(j);
                synchronizerInstance.register(extension);
            }
            nodeMap.put(synchronizer.getId(), synchronizerInstance);
        }
        List<WFEndNode> endNodes = processModel.getEndNodes();
        for (int i = 0; i < endNodes.size(); i++) {
            WFEndNode endNode = endNodes.get(i);
            EndNode endNodeInstance = new EndNode(endNode);
            extensionList = netExtensions.get(endNodeInstance.getExtensionTargetName());
            for (int j = 0; extensionList != null && j < extensionList.size(); j++) {
                INodeNetExtension extension = extensionList.get(j);
                endNodeInstance.register(extension);
            }
            nodeMap.put(endNode.getId(), endNodeInstance);
        }
        List<WFTransition> transitions = processModel.getTransitions();
        for (int i = 0; i < transitions.size(); i++) {
            WFTransition transition = transitions.get(i);
            Transition transitionInstance = new Transition(transition);
            String fromNodeId = transition.getFromNode().getId();
            if (fromNodeId != null) {
                INode enteringNodeInstance = nodeMap.get(fromNodeId);
                if (enteringNodeInstance != null) {
                    enteringNodeInstance.addLeavingTransition(transitionInstance);
                    transitionInstance.setEnteringNode(enteringNodeInstance);
                }
            }
            String toNodeId = transition.getToNode().getId();
            if (toNodeId != null) {
                INode leavingNodeInstance = nodeMap.get(toNodeId);
                if (leavingNodeInstance != null) {
                    leavingNodeInstance.addEnteringTransition(transitionInstance);
                    transitionInstance.setLeavingNode(leavingNodeInstance);
                }
            }
            extensionList = netExtensions.get(transitionInstance.getExtensionTargetName());
            for (int j = 0; extensionList != null && j < extensionList.size(); j++) {
                INodeNetExtension extension = extensionList.get(j);
                transitionInstance.register(extension);
            }
            edgeMap.put(transitionInstance.getEdgeId(), transitionInstance);
        }
        List<WFLoop> loops = processModel.getLoops();
        for (int i = 0; i < loops.size(); i++) {
            WFLoop loop = loops.get(i);
            Loop loopInstance = new Loop(loop);
            String fromNodeId = loop.getFromNode().getId();
            if (fromNodeId != null) {
                INode enteringNodeInstance = nodeMap.get(fromNodeId);
                if (enteringNodeInstance != null) {
                    enteringNodeInstance.addLeavingLoop(loopInstance);
                    loopInstance.setEnteringNode(enteringNodeInstance);
                }
            }
            String toNodeId = loop.getToNode().getId();
            if (toNodeId != null) {
                INode leavingNodeInstance = nodeMap.get(toNodeId);
                if (leavingNodeInstance != null) {
                    leavingNodeInstance.addEnteringLoop(loopInstance);
                    loopInstance.setLeavingNode(leavingNodeInstance);
                }
            }
            extensionList = netExtensions.get(loopInstance.getExtensionTargetName());
            for (int j = 0; extensionList != null && j < extensionList.size(); j++) {
                INodeNetExtension extension = extensionList.get(j);
                loopInstance.register(extension);
            }
            edgeMap.put(loopInstance.getEdgeId(), loopInstance);
        }
    }

    public WFProcess getProcessModel() {
        return processModel;
    }

    public StartNode getStartNode() {
        return startNode;
    }

    public <T extends INode> T getNode(String nodeId) {
        return (T) nodeMap.get(nodeId);
    }

    public <T extends IEdge> T getEdge(String edgeId) {
        return (T) edgeMap.get(edgeId);
    }
}
