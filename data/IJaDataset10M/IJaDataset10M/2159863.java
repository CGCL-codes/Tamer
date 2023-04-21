package de.suse.swamp.core.workflow;

import java.text.*;
import java.util.*;
import de.suse.swamp.core.actions.*;
import de.suse.swamp.core.container.*;
import de.suse.swamp.core.data.*;
import de.suse.swamp.core.data.datatypes.*;
import de.suse.swamp.core.notification.*;
import de.suse.swamp.core.security.*;
import de.suse.swamp.core.tasks.*;
import de.suse.swamp.core.util.*;
import de.suse.swamp.util.*;

public class Node extends Persistant {

    private String name;

    private ArrayList edges;

    private boolean isActive = false;

    private MileStone mileStone = null;

    private boolean mileStoneLoaded = false;

    private NodeTemplate nodeTempl;

    private int workflowId;

    /**
     * used to create a new node from a node template. This is the way
     * to create a node when creating a new workflow. Note that the template
     * is stored for to be present in createEdges.
     * 
     * To create Nodes when restoring from database, we try to be template
     * independant and take the other constructor.
     *
     * @param nodeTempl - the template to create the node from
     * @param actions  - a list of actions for the node.
     */
    public Node(NodeTemplate nodeTempl) {
        this.name = nodeTempl.getName();
        this.nodeTempl = nodeTempl;
        setModified(true);
    }

    /**
     * Constructor called from StorageManager
     */
    public Node(int id, String name, boolean isActive) {
        setId(id);
        this.name = name;
        this.nodeTempl = null;
        this.isActive = isActive;
        setModified(true);
    }

    /**
     * returns the name of the template from which this node was created.
     * 
     * @return name as string
     */
    public String getName() {
        return name;
    }

    /**
     * @return Returns the longDescription replaced with Datapack values.
     */
    public String getReplacedDescription() {
        if (getTemplate() != null) {
            return NotificationTools.workflowDataReplace(getTemplate().getDescription(), WorkflowManager.getInstance().getWorkflow(workflowId));
        } else {
            return "";
        }
    }

    /**
	 * Set this node active, 
	 * generate Tasks and send them to TaskManager
	 *  
	 */
    public void activate(String username, ResultList history) throws Exception {
        if (this.isActive()) {
            Logger.DEBUG("Tried to activate already active node " + this.getName() + ", skipping.");
        } else {
            for (Iterator it = getEdges().iterator(); it.hasNext(); ) {
                ((Edge) it.next()).reset();
            }
            HistoryManager.create("NODE_ENTER", this.getId(), getWorkflowId(), username, null);
            WorkflowManager wfMan = WorkflowManager.getInstance();
            isActive = true;
            if (getMileStone() != null) {
                getMileStone().setDisplayed(true);
                history.addResult(ResultList.MESSAGE, "Milestone \"" + this.mileStone.getDescription() + "\" has been reached.");
            }
            setModified(true);
            if (getTemplate().getActionTemplates() != null && getTemplate().getActionTemplates().size() > 0) {
                for (Iterator iter = getTemplate().getActionTemplates().iterator(); iter.hasNext(); ) {
                    ActionTemplate action = (ActionTemplate) iter.next();
                    WorkflowTask task = (WorkflowTask) action.createTask(this.workflowId);
                    TaskManager.addTask(task, username, history);
                }
            }
            if (this.isEndNode()) {
                TaskManager.cancelTasks(workflowId, username);
                Workflow wf = wfMan.getWorkflow(this.getWorkflowId());
                wf.deactivateNonMileStoneNodes();
                if (wf.isSubWorkflow()) {
                    Event ev = new Event(Event.SUBWORKFLOW_FINISHED, wf.getId(), wf.getParentwfid());
                    EventManager.handleWorkflowEvent(ev, username, history);
                }
                if (wf.getSubWfIds().size() > 0) {
                    for (Iterator it = wf.getSubWorkflows(true).iterator(); it.hasNext(); ) {
                        Workflow subWf = (Workflow) it.next();
                        Event ev = new Event(Event.PARENTWORKFLOW_FINISHED, wf.getId(), subWf.getId());
                        EventManager.handleWorkflowEvent(ev, username, history);
                        if (subWf.isRunning()) {
                            Logger.ERROR("Wf: " + wf.getName() + " finished, but Subwf: " + subWf.getName() + " still running. Please add event handling for PARENTWORKFLOW_FINISHED.");
                        }
                    }
                }
                history.addResult(ResultList.MESSAGE, "Workflow \"" + wf.getReplacedDescription() + "\" was finished.");
                HistoryManager.create("TASK_WORKFLOWCLOSE", wf.getId(), wf.getId(), username, "");
            }
        }
    }

    /**
     * set this node inactive, and deactivates all its outgoing edges. 
     * That means, that all Conditions are set to "false" which allows 
     * a second iteration through this workflow-part.
     */
    public void deactivate() throws StorageException {
        if (getEdges() != null) {
            for (Iterator iter = getEdges().iterator(); iter.hasNext(); ) {
                Edge edge = (Edge) iter.next();
                edge.reset();
            }
        }
        TaskManager.cancelTasksForNode(workflowId, this, SWAMPUser.SYSTEMUSERNAME);
        isActive = false;
        setModified(true);
    }

    /**
     * @return An ArrayList containing all Edges leaving this node. 
     */
    public ArrayList getEdges() {
        if (edges == null) {
            edges = WorkflowManager.loadEdges(this);
        }
        return edges;
    }

    /**
     * create all the Edges that leave this Node. This is called once all
     * the nodes have been created.
     *
     * @param nodeLookup a HashMap containing all nodes defined in the
     * parent workflow
     */
    public void createEdges(HashMap nodeLookup) {
        if (nodeTempl != null) {
            this.edges = nodeTempl.createEdges(nodeLookup);
            setModified(true);
        }
    }

    /**
     * add a new edge to list of edges of this node. Note that this may only be
     * called from a method that reconstructs edges for nodes from a storage like
     * the database. <b>Never call this method</b>.
     */
    public void addEdge(Edge e) {
        if (edges == null) {
            edges = new ArrayList();
        }
        if (e != null) {
            edges.add(e);
        }
        setModified(true);
    }

    /**
     *  Set the workflow this node belongs to. Only wants to be called once. We
     *  need this so that all elements of the construct "workflow object" have
     *  access to common data, like the datapack.
     */
    public void setWorkflowId(int workflowId) {
        if (this.workflowId == 0) {
            this.workflowId = workflowId;
            setModified(true);
        }
    }

    /**
     * @return ArrayList of nodes to be entered next. if !empty, current node is
     * left and new node(s) entered, if empty, nothing will happen
     */
    public ArrayList handleEvent(Event event) {
        ArrayList newNodes = new ArrayList();
        if (getEdges() != null) {
            for (Iterator i = getEdges().iterator(); i.hasNext(); ) {
                Edge e = (Edge) i.next();
                Node newNode = e.handleEvent(event);
                if (newNode != null) {
                    newNodes.add(newNode);
                }
            }
        }
        return newNodes;
    }

    /**
     * Returns a string representing the node object
     * 
     * @return a multiline string showing node data.
     */
    public String toString() {
        StringBuffer desc = new StringBuffer();
        desc.append("Node " + this.getName() + " active=" + isActive());
        return new String(desc);
    }

    /**
     * Returns a XML representing the node object
     * 
     * @return a multiline XML showing node data.
     */
    public String toXML() {
        StringBuffer sb = new StringBuffer();
        return new String(sb);
    }

    /**
     * @return an array list of all associated actions of this node.
     */
    public ArrayList getActionsTemplates() {
        if (getTemplate() != null) return nodeTempl.getActionTemplates(); else return null;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean equals(Object o) {
        if (o instanceof Node) {
            int otherId = ((Node) o).getId();
            if (otherId == getId()) {
                return true;
            }
        }
        return super.equals(o);
    }

    /**
     * @return Returns the longDescription replaced with Datapack values.
     */
    public String getReplacedLongDescription() {
        return NotificationTools.workflowDataReplace(getTemplate().getLongDescription(), WorkflowManager.getInstance().getWorkflow(workflowId));
    }

    public boolean equals(Node node) {
        return (getId() == node.getId());
    }

    /**
     * @return Returns the mileStone.
     */
    public MileStone getMileStone() {
        if (!mileStoneLoaded) {
            this.mileStone = WorkflowManager.loadNodeMileStone(this);
            mileStoneLoaded = true;
        }
        return this.mileStone;
    }

    /**
     * @param mileStone The mileStone to set.
     */
    public void setMileStone(MileStone mileStone) {
        if (getMileStone() != null) {
            Logger.ERROR("Tried to set milestone twice on node: " + this.getName());
        } else {
            this.mileStone = mileStone;
            this.mileStoneLoaded = true;
            setModified(true);
        }
    }

    public NodeTemplate getTemplate() {
        if (nodeTempl == null) {
            WorkflowManager wfMan = WorkflowManager.getInstance();
            WorkflowTemplate wftemp = wfMan.getWorkflow(workflowId).getTemplate();
            nodeTempl = wftemp.getNodeTemplate(this.name);
        }
        return nodeTempl;
    }

    /**
     * @return Returns the workflowId.
     */
    public int getWorkflowId() {
        return this.workflowId;
    }

    public boolean hasDueDate() {
        Workflow wf = WorkflowManager.getInstance().getWorkflow(this.getWorkflowId());
        if (getTemplate() != null && getTemplate().getDueDateReference() != null && !wf.getDatabit(getTemplate().getDueDateReference()).equals("")) return true; else return false;
    }

    /**
     * @return Returns the dueDate.
     */
    public Date getDueDate() {
        String databit = getTemplate().getDueDateReference();
        Logger.LOG("getDueDate: databit: " + databit);
        if (databit == null || databit.length() == 0) {
            return null;
        } else {
            Workflow wf = WorkflowManager.getInstance().getWorkflow(getWorkflowId());
            Date dueDate = new Date();
            try {
                Databit data = wf.getDatabit(databit);
            } catch (Exception e) {
                Logger.ERROR("Unable to get due date: " + e.getMessage());
            }
            return dueDate;
        }
    }

    /**
     * @return Returns the dueDate.
     */
    public String getDueDateString() {
        DateFormat df = new SimpleDateFormat(dateDatabit.dateFormat);
        String result = df.format(getDueDate());
        Logger.DEBUG("STRING RESULT " + result);
        return result;
    }

    /**
     * @param dueDate The dueDate to set.
     */
    public void setDueDate(Date dueDate) {
        String databit = getTemplate().getDueDateReference();
        if (databit.length() == 0) {
            return;
        } else {
            Workflow wf = WorkflowManager.getInstance().getWorkflow(getWorkflowId());
            Databit data = wf.getDatabit(databit);
            try {
            } catch (Exception e) {
                Logger.ERROR("Unable to set due date: " + e.getMessage());
            }
        }
    }

    /**
     * @return Returns the type.
     */
    public String getType() {
        return getTemplate().getType();
    }

    public boolean isStartNode() {
        return (this.getTemplate().getType().equalsIgnoreCase("start"));
    }

    public boolean isEndNode() {
        return (this.getTemplate().getType().equalsIgnoreCase("end"));
    }

    /**
     * Return all history entries for this node that start with $prefix
     */
    public ArrayList getHistoryEntries(int wfid, String prefix) {
        ArrayList entries = new ArrayList();
        entries.addAll(HistoryManager.getHistoryEntries(wfid, this.getId(), prefix));
        return entries;
    }
}
