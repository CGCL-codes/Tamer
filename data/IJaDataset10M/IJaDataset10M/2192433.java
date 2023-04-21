package net.sf.orcc.network.transforms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.sf.orcc.OrccException;
import net.sf.orcc.ir.Expression;
import net.sf.orcc.ir.GlobalVariable;
import net.sf.orcc.ir.Use;
import net.sf.orcc.ir.expr.BinaryExpr;
import net.sf.orcc.ir.expr.UnaryExpr;
import net.sf.orcc.network.Connection;
import net.sf.orcc.network.Instance;
import net.sf.orcc.network.Network;
import net.sf.orcc.network.Vertex;
import net.sf.orcc.network.attributes.IAttribute;
import net.sf.orcc.util.OrderedMap;
import org.jgrapht.DirectedGraph;

/**
 * This class defines a transformation that flattens a given network in-place.
 * 
 * @author Matthieu Wipliez
 * @author Ghislain Roquier
 * 
 */
public class NetworkFlattener implements INetworkTransformation {

    /**
	 * Copies all instances and edges between them of subGraph in graph
	 * 
	 * @throws OrccException
	 */
    private void copySubGraph(HashMap<String, Integer> existingInsts, Map<String, IAttribute> attrs, Network network, Network subNetwork) throws OrccException {
        DirectedGraph<Vertex, Connection> graph = network.getGraph();
        DirectedGraph<Vertex, Connection> subGraph = subNetwork.getGraph();
        Set<Vertex> vertexSet = new HashSet<Vertex>(subGraph.vertexSet());
        Set<Connection> edgeSet = new HashSet<Connection>(subGraph.edgeSet());
        for (Vertex vertex : vertexSet) {
            if (vertex.isInstance()) {
                Map<String, IAttribute> vertexAttrs = vertex.getInstance().getAttributes();
                for (Entry<String, IAttribute> attr : attrs.entrySet()) {
                    if (!vertexAttrs.containsKey(attr.getKey())) {
                        vertexAttrs.put(attr.getKey(), attr.getValue());
                    }
                }
                graph.addVertex(renameInstance(existingInsts, vertex, network));
            }
        }
        for (Connection edge : edgeSet) {
            Vertex srcVertex = subGraph.getEdgeSource(edge);
            Vertex tgtVertex = subGraph.getEdgeTarget(edge);
            if (srcVertex.isInstance() && tgtVertex.isInstance()) {
                graph.addEdge(srcVertex, tgtVertex, edge);
            }
        }
    }

    private void copyVariables(OrderedMap<GlobalVariable> existingVars, Network network, Instance instance) throws OrccException {
        for (GlobalVariable var : instance.getNetwork().getVariables()) {
            OrderedMap<GlobalVariable> parentVars = network.getVariables();
            GlobalVariable newVar = new GlobalVariable(var);
            newVar.setExpression(var.getExpression());
            if (existingVars.contains(var)) {
                newVar.setName(instance.getId() + "_" + newVar.getName());
                for (Use use : var.getUses()) {
                    use.getVariable().setName(newVar.getName());
                }
            }
            if (!existingVars.contains(newVar)) {
                parentVars.add("", newVar.getLocation(), newVar.getName(), newVar);
            }
        }
    }

    /**
	 * Links each predecessor of vertex to the successors of the input port in
	 * subGraph
	 * 
	 * @param vertex
	 *            the parent graph
	 * @param graph
	 *            the parent graph
	 * @param subGraph
	 *            the child graph
	 * @throws OrccException
	 */
    private void linkIncomingConnections(Vertex vertex, DirectedGraph<Vertex, Connection> graph, DirectedGraph<Vertex, Connection> subGraph) throws OrccException {
        Set<Connection> incomingEdgeSet = new HashSet<Connection>(graph.incomingEdgesOf(vertex));
        for (Connection edge : incomingEdgeSet) {
            Set<Connection> outgoingEdgeSet = new HashSet<Connection>();
            for (Vertex v : subGraph.vertexSet()) {
                if (v.isPort()) {
                    if (edge.getTarget().getName().equals(v.getPort().getName())) {
                        outgoingEdgeSet = subGraph.outgoingEdgesOf(v);
                    }
                }
            }
            for (Connection newEdge : outgoingEdgeSet) {
                Connection incoming = new Connection(edge.getSource(), newEdge.getTarget(), edge.getAttributes());
                graph.addEdge(graph.getEdgeSource(edge), subGraph.getEdgeTarget(newEdge), incoming);
            }
        }
    }

    /**
	 * Links each successor of vertex to the predecessors of the output port in
	 * subGraph
	 * 
	 * @param vertex
	 *            the current vertex
	 * @param graph
	 *            the parent graph
	 * @param subGraph
	 *            the child graph
	 * @throws OrccException
	 */
    private void linkOutgoingConnections(Vertex vertex, DirectedGraph<Vertex, Connection> graph, DirectedGraph<Vertex, Connection> subGraph) throws OrccException {
        Set<Connection> outgoingEdgeSet = new HashSet<Connection>(graph.outgoingEdgesOf(vertex));
        for (Connection edge : outgoingEdgeSet) {
            Set<Connection> incomingEdgeSet = new HashSet<Connection>();
            for (Vertex v : subGraph.vertexSet()) {
                if (v.isPort()) {
                    if (edge.getSource().getName().equals(v.getPort().getName())) {
                        incomingEdgeSet = subGraph.incomingEdgesOf(v);
                    }
                }
            }
            for (Connection newEdge : incomingEdgeSet) {
                Connection incoming = new Connection(newEdge.getSource(), edge.getTarget(), edge.getAttributes());
                graph.addEdge(subGraph.getEdgeSource(newEdge), graph.getEdgeTarget(edge), incoming);
            }
        }
    }

    /**
	 * Propagates parameters of instance to the different sub-instances contain
	 * in the attached network of instance
	 * 
	 * @throws OrccException
	 */
    private void propagateParams(Instance instance) throws OrccException {
        HashMap<String, Expression> parentParams = new HashMap<String, Expression>(instance.getParameters());
        Network subNetwork = instance.getNetwork();
        for (GlobalVariable var : subNetwork.getVariables()) {
            Expression expr = resolveExpr(var.getExpression(), parentParams);
            var.setExpression(expr);
        }
        for (Instance inst : subNetwork.getInstances()) {
            Map<String, Expression> instParams = inst.getParameters();
            for (Entry<String, Expression> entry : instParams.entrySet()) {
                Expression expr = resolveExpr(entry.getValue(), parentParams);
                entry.setValue(expr);
            }
        }
    }

    /**
	 * Renames instance if an instance with the same name already exists
	 * 
	 * @throws OrccException
	 */
    private Vertex renameInstance(HashMap<String, Integer> existingInsts, Vertex vertex, Network network) throws OrccException {
        Instance instance = vertex.getInstance();
        String str = instance.getId();
        Integer value = new Integer(0);
        if (existingInsts.containsKey(str)) {
            value = existingInsts.get(str);
            instance.setId(String.format(str + "_%03d", ++value));
            for (Instance inst : network.getInstances()) {
                if (inst.getId().equals(instance.getId())) {
                    instance.setId(String.format(str + "_%03d", ++value));
                }
            }
        }
        existingInsts.put(instance.getId(), value);
        return vertex;
    }

    private Expression resolveExpr(Expression expr, HashMap<String, Expression> parentParams) {
        if (expr.isBinaryExpr()) {
            BinaryExpr bopExpr = (BinaryExpr) expr;
            bopExpr.setE1(resolveExpr(bopExpr.getE1(), parentParams));
            bopExpr.setE2(resolveExpr(bopExpr.getE2(), parentParams));
        } else if (expr.isUnaryExpr()) {
            UnaryExpr uopExpr = (UnaryExpr) expr;
            uopExpr.setExpr(resolveExpr(uopExpr.getExpr(), parentParams));
        } else {
            if (parentParams.containsKey(expr.toString())) {
                expr = parentParams.get(expr.toString());
            }
        }
        return expr;
    }

    @Override
    public void transform(Network network) throws OrccException {
        Set<Vertex> vertexSet = new HashSet<Vertex>(network.getGraph().vertexSet());
        HashMap<String, Integer> existingInsts = new HashMap<String, Integer>();
        OrderedMap<GlobalVariable> ExistingVars = network.getVariables();
        for (Instance instance : network.getInstances()) {
            if (!instance.isNetwork()) {
                existingInsts.put(instance.getId(), new Integer(0));
            }
        }
        for (Vertex vertex : vertexSet) {
            if (vertex.isInstance()) {
                Instance instance = vertex.getInstance();
                if (instance.isNetwork()) {
                    Network subNetwork = instance.getNetwork();
                    transform(subNetwork);
                    propagateParams(instance);
                    copyVariables(ExistingVars, network, instance);
                    copySubGraph(existingInsts, instance.getAttributes(), network, subNetwork);
                    linkOutgoingConnections(vertex, network.getGraph(), subNetwork.getGraph());
                    linkIncomingConnections(vertex, network.getGraph(), subNetwork.getGraph());
                    network.getGraph().removeVertex(vertex);
                }
            }
        }
    }
}
