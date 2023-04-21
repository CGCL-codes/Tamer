package org.progeeks.sm;

import java.util.*;
import com.phoenixst.plexus.*;
import com.phoenixst.plexus.traversals.*;
import org.progeeks.graph.*;
import org.progeeks.util.*;
import org.progeeks.util.log.*;

/**
 *  Manages execution state for a StateMachine by keeping
 *  the tasks, subtasks, and state linkages in a graph.
 *  This also acts as a factory for the runtime Task objects.
 *
 *  @version   $Revision: 3431 $
 *  @author    Paul Speed
 */
public class GraphExecutionData implements ExecutionData {

    static Log log = Log.getLog();

    private Graph graph;

    private boolean sharedStates;

    private TraverserPredicate atStatePredicate;

    private TraverserPredicate parentTaskPredicate;

    private TraverserPredicate childTaskPredicate;

    public GraphExecutionData() {
        this(new EdgeIdentityGraph());
    }

    public GraphExecutionData(Graph g) {
        this(g, false);
    }

    /**
     *  Creates a new graph-based ExecutionData implementation
     *  wrapping the specified graph.  If sharedStates is true then
     *  this wrapper assumes that the State objects are already part
     *  of the specified graph.  Otherwise, it will add them as needed.
     */
    public GraphExecutionData(Graph g, boolean sharedStates) {
        this.graph = g;
        this.sharedStates = sharedStates;
        atStatePredicate = TraverserPredicateFactory.createEqualsUser(getAtStateObject(), GraphUtils.DIRECTED_OUT_MASK);
        parentTaskPredicate = TraverserPredicateFactory.createEqualsUser(getChildTaskObject(), GraphUtils.DIRECTED_IN_MASK);
        childTaskPredicate = TraverserPredicateFactory.createEqualsUser(getChildTaskObject(), GraphUtils.DIRECTED_OUT_MASK);
    }

    public Task createTask(State startState, String taskName) {
        DefaultTask task = new DefaultTask(taskName);
        graph.addNode(task);
        moveTaskState(task, null, startState);
        return (task);
    }

    protected Object getAtStateObject() {
        return ("atState");
    }

    protected Object getChildTaskObject() {
        return ("taskChild");
    }

    public void moveTaskState(Task task, State oldState, State state) {
        Graph.Edge oldEdge = null;
        if (oldState != null) {
            oldEdge = graph.getIncidentEdge(task, atStatePredicate);
            if (oldEdge != null) {
                if (!ObjectUtils.areEqual(oldState, oldEdge.getHead())) {
                    throw new RuntimeException("Previous state[" + oldState + "] does not match current:" + oldEdge.getHead());
                }
            }
        }
        if (!sharedStates) graph.addNode(state);
        Graph.Edge newEdge = graph.addEdge(getAtStateObject(), task, state, true);
        if (newEdge != null && oldEdge != null) graph.removeEdge(oldEdge);
    }

    public Task getRootTask(Task task) {
        Task parent;
        while ((parent = getParentTask(task)) != null) {
            task = parent;
        }
        return (task);
    }

    public Task getParentTask(Task child) {
        return ((Task) graph.getAdjacentNode(child, parentTaskPredicate));
    }

    public Collection<Task> getChildTasks(Task parent) {
        return (graph.adjacentNodes(parent, childTaskPredicate));
    }

    public Task createChildTask(Task parent) {
        DefaultTask child = new DefaultTask(parent.getName());
        graph.addNode(child);
        graph.addEdge(getChildTaskObject(), parent, child, true);
        return (child);
    }

    public void mergeChildTasks(Task task) {
        for (Task child : getChildTasks(task)) {
            removeTaskTree(child);
        }
    }

    protected void removeTask(Task task) {
        graph.removeNode(task);
    }

    public void removeRootTask(Task task) {
        removeTaskTree(getRootTask(task));
    }

    public boolean isLeafTask(Task task) {
        return (graph.getIncidentEdge(task, childTaskPredicate) == null);
    }

    protected void removeTaskTree(Task task) {
        for (Traverser t = new PostOrderTraverser(task, graph, childTaskPredicate); t.hasNext(); ) {
            removeTask((Task) t.next());
        }
    }

    /**
     *  Removes the task, it's children, and all of its ancestors
     *  up to the point there are sibling branches.  Returns the
     *  ancestor that had sibling branches or null if the entire
     *  tree was removed.
     */
    public Task collapseTaskBranch(Task task) {
        Task parent = getParentTask(task);
        removeTaskTree(task);
        while (parent != null && isLeafTask(parent)) {
            Task next = getParentTask(parent);
            if (log.isDebugEnabled()) log.debug("Removing ancestor:" + parent);
            removeTask(parent);
            parent = next;
        }
        return (parent);
    }

    public State getState(Task task) {
        if (!graph.containsNode(task)) return (null);
        return ((State) graph.getAdjacentNode(task, atStatePredicate));
    }

    public void setTransitionValue(Task task, String transitionValue) {
        ((DefaultTask) task).setTransitionValue(transitionValue);
    }

    public String getTransitionValue(Task task) {
        return (((DefaultTask) task).getTransitionValue());
    }

    protected static class DefaultTask implements Task {

        private String name;

        private String transitionValue;

        public DefaultTask(String name) {
            this.name = name;
        }

        protected String getTransitionValue() {
            return (transitionValue);
        }

        protected void setTransitionValue(String value) {
            this.transitionValue = value;
        }

        public String getName() {
            return (name);
        }

        public String toString() {
            return ("Task[" + name + ", " + transitionValue + "]");
        }
    }
}
