package org.processmining.framework.models.bpmn;

import java.util.ArrayList;
import java.util.HashMap;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author JianHong.YE, collaborate with LiJie.WEN and Feng This class represent
 *         one level of process, a process is the main diagram of defined in a
 *         SubProcess node
 */
public class BpmnProcessModel {

    protected HashMap<String, BpmnSubProcess> subProcesses;

    protected HashMap<String, BpmnObject> nodes;

    protected HashMap<String, BpmnEdge> edges;

    protected String parentId;

    protected BpmnEvent start;

    protected BpmnEvent end;

    protected HashMap<String, BpmnGraphVertex> vids = new HashMap<String, BpmnGraphVertex>();

    /**
	 * Constructor from XML document
	 * 
	 */
    public BpmnProcessModel(String parentId, Element element) {
        subProcesses = new HashMap<String, BpmnSubProcess>();
        nodes = new HashMap<String, BpmnObject>();
        edges = new HashMap<String, BpmnEdge>();
        this.parentId = parentId;
        parseCurrentLevel(element);
    }

    public BpmnProcessModel(String parentId) {
        subProcesses = new HashMap<String, BpmnSubProcess>();
        nodes = new HashMap<String, BpmnObject>();
        edges = new HashMap<String, BpmnEdge>();
        this.parentId = parentId;
    }

    /**
	 * @return the end
	 */
    public BpmnEvent getEnd() {
        return end;
    }

    /**
	 * @param end
	 *            the end to set
	 */
    public void setEnd(BpmnEvent end) {
        this.end = end;
    }

    /**
	 * @return the start
	 */
    public BpmnEvent getStart() {
        return start;
    }

    /**
	 * @param start
	 *            the start to set
	 */
    public void setStart(BpmnEvent start) {
        this.start = start;
    }

    public BpmnSubProcess[] getBpmnSubProcesses() {
        int size = subProcesses.size();
        return subProcesses.values().toArray(new BpmnSubProcess[size]);
    }

    public BpmnObject[] getBpmnNodes() {
        int size = nodes.size();
        return nodes.values().toArray(new BpmnObject[size]);
    }

    public BpmnEdge[] getBpmnEdges() {
        int size = edges.size();
        return edges.values().toArray(new BpmnEdge[size]);
    }

    public BpmnEdge getEdge(String edgeId) {
        return edges.get(edgeId);
    }

    public BpmnObject getNode(String nodeId) {
        if (nodes.get(nodeId) != null) return nodes.get(nodeId);
        return subProcesses.get(nodeId);
    }

    public String getNameAndId(String nodeId) {
        BpmnObject node = nodes.get(nodeId);
        if (node == null) node = subProcesses.get(nodeId);
        return node.getName() + "\t" + node.getId();
    }

    /**
	 * If this is a sub process, get the Id of node to which the incoming edge
	 * should point
	 * 
	 * @return
	 */
    public String getInEdgeHeadId() {
        String idStr = "";
        if (this.start != null) {
            idStr = this.start.getId();
        } else if (this.nodes.size() > 0) {
            idStr = (this.getBpmnNodes()[0]).getId();
        }
        return idStr;
    }

    /**
	 * If this is a sub process, get the Id of node from which the outgoing edge
	 * should point
	 * 
	 * @return
	 */
    public String getOutEdgeTailId() {
        String idStr = "";
        if (this.end != null) {
            idStr = this.end.getId();
        } else if (this.nodes.size() > 0) {
            int lastIdx = this.nodes.size() - 1;
            idStr = (this.getBpmnNodes()[lastIdx]).getId();
        }
        return idStr;
    }

    protected void parseCurrentLevel(Element element) {
        NodeList childNodes = element.getChildNodes();
        int childrenNum = childNodes.getLength();
        for (int i = 0; i < childrenNum; i++) {
            Node nd = childNodes.item(i);
            if (nd instanceof Element) {
                parseElement((Element) nd);
            }
        }
    }

    public void addNode(BpmnObject node) {
        if (node instanceof BpmnSubProcess) {
            subProcesses.put(node.getId(), (BpmnSubProcess) node);
        } else {
            nodes.put(node.getId(), node);
        }
    }

    protected BpmnElement parseElement(Element element) {
        String tag = element.getTagName();
        if (tag.equals(BpmnXmlTags.BPMN_START) || tag.equals(BpmnXmlTags.BPMN_INTERMEDIATE) || tag.equals(BpmnXmlTags.BPMN_END)) {
            BpmnEvent event = new BpmnEvent(element);
            String id = event.getId();
            event.setTypeTag(BpmnEventType.valueOf(tag));
            event.setpid(this.parentId);
            nodes.put(id, event);
            if (tag.equals(BpmnXmlTags.BPMN_START)) {
                this.start = event;
            } else if (tag.equals(BpmnXmlTags.BPMN_END)) {
                this.end = event;
            }
            return event;
        } else if (tag.equals(BpmnXmlTags.BPMN_TASK)) {
            BpmnTask task = new BpmnTask(element);
            String id = task.getId();
            task.setTypeTag(BpmnTaskType.valueOf(tag));
            task.setpid(this.parentId);
            nodes.put(id, task);
            NodeList childNodes = element.getElementsByTagName(BpmnXmlTags.BPMN_INTERMEDIATE);
            int childrenNum = childNodes.getLength();
            for (int i = 0; i < childrenNum; i++) {
                Node nd = childNodes.item(i);
                if (nd instanceof Element) {
                    BpmnEvent event = (BpmnEvent) parseElement((Element) nd);
                    event.setLane(task.getLane());
                    if (event != null) {
                        String edgeId = event.getId() + "_" + id;
                        BpmnEdge edge = new BpmnEdge(event.getId(), id);
                        edge.setType(BpmnEdgeType.Flow);
                        edge.setpid(this.parentId);
                        edge.setId(edgeId);
                        edges.put(edgeId, edge);
                    }
                }
            }
            return task;
        } else if (tag.equals(BpmnXmlTags.BPMN_SUBPROCESS)) {
            BpmnSubProcess sub = new BpmnSubProcess(element);
            String id = sub.getId();
            sub.setTypeTag(BpmnTaskType.valueOf(tag));
            sub.setpid(this.parentId);
            subProcesses.put(id, sub);
            return sub;
        } else if (tag.equals(BpmnXmlTags.BPMN_GATEWAY)) {
            BpmnGateway gw = new BpmnGateway(element);
            String id = gw.getId();
            gw.setpid(this.parentId);
            nodes.put(id, gw);
            return gw;
        } else if (tag.equals(BpmnXmlTags.BPMN_POOL)) {
            BpmnSwimPool pool = new BpmnSwimPool(element);
            pool.setType(BpmnSwimType.valueOf(tag));
            String poolid = pool.getId();
            pool.setpid(this.parentId);
            nodes.put(poolid, pool);
            return pool;
        } else if (tag.equals(BpmnXmlTags.BPMN_LANE)) {
            BpmnSwimLane lane = new BpmnSwimLane(element);
            lane.setType(BpmnSwimType.valueOf(tag));
            String laneid = lane.getId();
            lane.setpid(this.parentId);
            nodes.put(laneid, lane);
            return lane;
        } else if (tag.equals(BpmnXmlTags.BPMN_FLOW) || tag.equals(BpmnXmlTags.BPMN_MESSAGE)) {
            BpmnEdge edge = new BpmnEdge(element);
            edge.setType(BpmnEdgeType.valueOf(tag));
            String id = edge.getId();
            edge.setpid(this.parentId);
            edges.put(id, edge);
            return edge;
        }
        return null;
    }

    public void addEdge(BpmnEdge edge) {
        if (edges.get(edge.getId()) == null) edges.put(edge.getId(), edge);
    }

    public void removeEdge(String id) {
        edges.remove(id);
    }
}
