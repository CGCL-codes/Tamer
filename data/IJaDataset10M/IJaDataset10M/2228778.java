package org.sdf4j.model.sdf.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import org.sdf4j.demo.SDFAdapterDemo;
import org.sdf4j.generator.SDFRandomGraph;
import org.sdf4j.model.parameters.ExpressionValue;
import org.sdf4j.model.parameters.InvalidExpressionException;
import org.sdf4j.model.parameters.Variable;
import org.sdf4j.model.sdf.SDFAbstractVertex;
import org.sdf4j.model.sdf.SDFEdge;
import org.sdf4j.model.sdf.SDFGraph;
import org.sdf4j.model.sdf.SDFInterfaceVertex;
import org.sdf4j.model.sdf.SDFVertex;
import org.sdf4j.model.sdf.esdf.SDFBroadcastVertex;
import org.sdf4j.model.sdf.esdf.SDFForkVertex;
import org.sdf4j.model.sdf.esdf.SDFJoinVertex;
import org.sdf4j.model.sdf.esdf.SDFRoundBufferVertex;
import org.sdf4j.model.sdf.types.SDFExpressionEdgePropertyType;
import org.sdf4j.model.sdf.types.SDFIntEdgePropertyType;
import org.sdf4j.model.visitors.SDF4JException;
import org.sdf4j.model.visitors.VisitorOutput;

/**
 * Visitor used to transform an SDF into an Homogeneous SDF (for all edges :
 * prod = cons)
 * 
 * @author jpiat
 * 
 */
public class ToHSDFVisitor implements GraphVisitor<SDFGraph, SDFAbstractVertex, SDFEdge> {

    /**
	 * Test the visitor
	 * 
	 * @param args
	 * @throws InvalidExpressionException
	 */
    public static void main(String[] args) throws InvalidExpressionException {
        @SuppressWarnings("unused") int nbVertex = 3, minInDegree = 1, maxInDegree = 5, minOutDegree = 1, maxOutDegree = 2;
        @SuppressWarnings("unused") int minrate = 1, maxrate = 4;
        @SuppressWarnings("unused") SDFRandomGraph test = new SDFRandomGraph();
        SDFAdapterDemo applet = new SDFAdapterDemo();
        SDFGraph demoGraph = createTestComGraph();
        SDFAdapterDemo applet2 = new SDFAdapterDemo();
        ToHSDFVisitor visitor = new ToHSDFVisitor();
        try {
            demoGraph.accept(visitor);
            applet2.init(demoGraph);
            applet.init(visitor.getOutput());
        } catch (SDF4JException e) {
            e.printStackTrace();
        }
    }

    private SDFGraph outputGraph;

    /**
	 * GIves this visitor output
	 * 
	 * @return The output of the visitor
	 */
    public SDFGraph getOutput() {
        return outputGraph;
    }

    private void linkVerticesTop(SDFGraph sdf, HashMap<SDFAbstractVertex, Vector<SDFAbstractVertex>> matchCopies, SDFGraph output) throws InvalidExpressionException {
        for (SDFEdge edge : sdf.edgeSet()) {
            sdf.getEdgeSource(edge);
            sdf.getEdgeTarget(edge);
            SDFInterfaceVertex inputVertex = null;
            SDFInterfaceVertex outputVertex = null;
            Vector<SDFAbstractVertex> sourceCopies = matchCopies.get(sdf.getEdgeSource(edge));
            Vector<SDFAbstractVertex> targetCopies = matchCopies.get(sdf.getEdgeTarget(edge));
            int nbDelays = edge.getDelay().intValue();
            int totProd = 0;
            int sourceProd = 0;
            int targetCons = 0;
            int targetIndex = (nbDelays / edge.getCons().intValue()) % targetCopies.size(), sourceIndex = 0;
            int rest = Math.min(edge.getProd().intValue(), edge.getCons().intValue());
            int startIndex = targetIndex;
            while (totProd < (edge.getCons().intValue() * targetCopies.size())) {
                if (rest < edge.getProd().intValue() && !(sourceCopies.get(sourceIndex) instanceof SDFForkVertex) && !(sourceCopies.get(sourceIndex) instanceof SDFBroadcastVertex)) {
                    SDFAbstractVertex explodeVertex = new SDFForkVertex();
                    output.addVertex(explodeVertex);
                    SDFAbstractVertex originVertex = (SDFAbstractVertex) sourceCopies.get(sourceIndex);
                    explodeVertex.setName("explode_" + originVertex.getName() + "_" + edge.getSourceInterface().getName());
                    sourceCopies.set(sourceIndex, explodeVertex);
                    SDFEdge newEdge = output.addEdge(originVertex, explodeVertex);
                    newEdge.setDelay(new SDFIntEdgePropertyType(0));
                    newEdge.setProd(new SDFIntEdgePropertyType(edge.getProd().intValue()));
                    newEdge.setCons(new SDFIntEdgePropertyType(edge.getProd().intValue()));
                    newEdge.setSourceInterface(edge.getSourceInterface());
                    newEdge.setTargetInterface(edge.getTargetInterface());
                }
                if (rest < edge.getCons().intValue() && !(targetCopies.get(targetIndex) instanceof SDFJoinVertex) && !(targetCopies.get(targetIndex) instanceof SDFRoundBufferVertex)) {
                    SDFAbstractVertex implodeVertex = new SDFJoinVertex();
                    output.addVertex(implodeVertex);
                    SDFAbstractVertex originVertex = (SDFAbstractVertex) targetCopies.get(targetIndex);
                    implodeVertex.setName("implode_" + originVertex.getName() + "_" + edge.getTargetInterface().getName());
                    targetCopies.set(targetIndex, implodeVertex);
                    SDFEdge newEdge = output.addEdge(implodeVertex, originVertex);
                    newEdge.setDelay(new SDFIntEdgePropertyType(0));
                    newEdge.setProd(new SDFIntEdgePropertyType(edge.getCons().intValue()));
                    newEdge.setCons(new SDFIntEdgePropertyType(edge.getCons().intValue()));
                    newEdge.setSourceInterface(edge.getSourceInterface());
                    newEdge.setTargetInterface(edge.getTargetInterface());
                }
                SDFEdge newEdge = output.addEdge(sourceCopies.get(sourceIndex), targetCopies.get(targetIndex));
                if (sourceCopies.get(sourceIndex).getSink(edge.getSourceInterface().getName()) != null) {
                    newEdge.setSourceInterface(sourceCopies.get(sourceIndex).getSink(edge.getSourceInterface().getName()));
                } else {
                    newEdge.setSourceInterface(edge.getSourceInterface().clone());
                }
                if (targetCopies.get(targetIndex).getSource(edge.getTargetInterface().getName()) != null) {
                    newEdge.setTargetInterface(targetCopies.get(targetIndex).getSource(edge.getTargetInterface().getName()));
                } else {
                    newEdge.setTargetInterface(edge.getTargetInterface().clone());
                }
                newEdge.setTargetInterface(edge.getTargetInterface().clone());
                if (targetCopies.get(targetIndex) instanceof SDFVertex) {
                    if (((SDFVertex) targetCopies.get(targetIndex)).getAssociatedInterface(edge) != null) {
                        inputVertex = ((SDFVertex) targetCopies.get(targetIndex)).getAssociatedInterface(edge);
                        ((SDFVertex) targetCopies.get(targetIndex)).setInterfaceVertexExternalLink(newEdge, inputVertex);
                    }
                }
                if (sourceCopies.get(sourceIndex) instanceof SDFVertex) {
                    if (((SDFVertex) sourceCopies.get(sourceIndex)).getAssociatedInterface(edge) != null) {
                        outputVertex = ((SDFVertex) sourceCopies.get(sourceIndex)).getAssociatedInterface(edge);
                        ((SDFVertex) sourceCopies.get(sourceIndex)).setInterfaceVertexExternalLink(newEdge, outputVertex);
                    }
                }
                newEdge.copyProperties(edge);
                newEdge.setProd(new SDFIntEdgePropertyType(rest));
                newEdge.setCons(new SDFIntEdgePropertyType(rest));
                if ((targetIndex == 0 || targetIndex < startIndex) && nbDelays > 0) {
                    newEdge.setDelay(new SDFIntEdgePropertyType(edge.getCons().intValue()));
                    nbDelays = nbDelays - edge.getCons().intValue();
                } else {
                    newEdge.setDelay(new SDFIntEdgePropertyType(0));
                }
                sourceProd += rest;
                targetCons += rest;
                totProd += rest;
                if (sourceProd == edge.getProd().intValue() && targetCons == edge.getCons().intValue()) {
                    sourceIndex = (sourceIndex + 1) % sourceCopies.size();
                    sourceProd = 0;
                    targetIndex = (targetIndex + 1) % targetCopies.size();
                    targetCons = 0;
                    rest = Math.min(edge.getProd().intValue(), edge.getCons().intValue());
                } else if (sourceProd == edge.getProd().intValue()) {
                    sourceIndex = (sourceIndex + 1) % sourceCopies.size();
                    sourceProd = 0;
                    rest = Math.min(edge.getCons().intValue() - targetCons, edge.getProd().intValue());
                } else if (targetCons == edge.getCons().intValue()) {
                    targetIndex = (targetIndex + 1) % targetCopies.size();
                    targetCons = 0;
                    rest = Math.min(edge.getProd().intValue() - sourceProd, edge.getCons().intValue());
                }
                if ((totProd == (edge.getCons().intValue() * targetCopies.size())) && targetCopies.get(0) instanceof SDFInterfaceVertex && sourceIndex < sourceCopies.size()) {
                    totProd = 0;
                }
            }
            for (int i = 0; i < sourceCopies.size(); i++) {
                if (sourceCopies.get(i) instanceof SDFForkVertex && !sourceCopies.get(i).equals(edge.getSource())) {
                    SDFAbstractVertex trueSource = null;
                    for (SDFEdge inEdge : output.incomingEdgesOf(sourceCopies.get(i))) {
                        trueSource = inEdge.getSource();
                    }
                    sourceCopies.set(i, trueSource);
                }
            }
            for (int i = 0; i < targetCopies.size(); i++) {
                if (targetCopies.get(i) instanceof SDFJoinVertex && !targetCopies.get(i).equals(edge.getTarget())) {
                    SDFAbstractVertex trueTarget = null;
                    for (SDFEdge inEdge : output.outgoingEdgesOf(targetCopies.get(i))) {
                        trueTarget = inEdge.getTarget();
                    }
                    targetCopies.set(i, trueTarget);
                }
            }
        }
    }

    private void transformsTop(SDFGraph graph, SDFGraph output) throws SDF4JException, InvalidExpressionException {
        HashMap<SDFAbstractVertex, Vector<SDFAbstractVertex>> matchCopies = new HashMap<SDFAbstractVertex, Vector<SDFAbstractVertex>>();
        if (graph.isSchedulable()) {
            for (SDFAbstractVertex vertex : graph.vertexSet()) {
                Vector<SDFAbstractVertex> copies = new Vector<SDFAbstractVertex>();
                matchCopies.put(vertex, copies);
                if (vertex instanceof SDFInterfaceVertex) {
                    SDFAbstractVertex copy = ((SDFAbstractVertex) vertex).clone();
                    copies.add(copy);
                    output.addVertex(copy);
                } else {
                    VisitorOutput.getLogger().log(Level.INFO, vertex.getName() + " x" + vertex.getNbRepeat());
                    if (vertex.getNbRepeatAsInteger() == 1) {
                        SDFAbstractVertex copy = ((SDFAbstractVertex) vertex).clone();
                        copy.setName(copy.getName());
                        output.addVertex(copy);
                        copies.add(copy);
                    } else {
                        for (int i = 0; i < vertex.getNbRepeatAsInteger(); i++) {
                            SDFAbstractVertex copy = ((SDFAbstractVertex) vertex).clone();
                            copy.setName(copy.getName() + "_" + i);
                            copy.setNbRepeat(1);
                            output.addVertex(copy);
                            copies.add(copy);
                        }
                    }
                }
            }
            linkVerticesTop(graph, matchCopies, output);
            output.getPropertyBean().setValue("schedulable", true);
        } else {
            VisitorOutput.getLogger().log(Level.SEVERE, "graph " + graph.getName() + " is not schedulable");
            throw (new SDF4JException("Graph " + graph.getName() + " is not schedulable"));
        }
    }

    public void visit(SDFEdge sdfEdge) {
    }

    public void visit(SDFGraph sdf) throws SDF4JException {
        outputGraph = sdf.clone();
        boolean isHSDF = true;
        for (SDFAbstractVertex vertex : outputGraph.vertexSet()) {
            try {
                if (vertex instanceof SDFVertex && vertex.getNbRepeatAsInteger() > 1) {
                    isHSDF = false;
                    break;
                }
            } catch (InvalidExpressionException e) {
                e.printStackTrace();
                throw (new SDF4JException(e.getMessage()));
            }
        }
        if (isHSDF) {
            return;
        }
        outputGraph.clean();
        ArrayList<SDFAbstractVertex> vertices = new ArrayList<SDFAbstractVertex>(sdf.vertexSet());
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i) instanceof SDFVertex) {
                vertices.get(i).accept(this);
            }
        }
        try {
            transformsTop(sdf, outputGraph);
        } catch (InvalidExpressionException e) {
            e.printStackTrace();
            throw (new SDF4JException(e.getMessage()));
        }
    }

    public void visit(SDFAbstractVertex sdfVertex) {
    }

    private static SDFGraph createTestComGraph() {
        SDFGraph graph = new SDFGraph();
        SDFVertex sensorInt = new SDFVertex();
        sensorInt.setName("sensor_Int");
        graph.addVertex(sensorInt);
        SDFVertex gen5 = new SDFVertex();
        gen5.setName("Gen5");
        graph.addVertex(gen5);
        SDFVertex recopie5 = new SDFVertex();
        recopie5.setName("recopie_5");
        graph.addVertex(recopie5);
        SDFVertex acqData = new SDFVertex();
        acqData.setName("acq_data");
        graph.addVertex(acqData);
        SDFEdge sensGen = graph.addEdge(sensorInt, gen5);
        sensGen.setProd(new SDFIntEdgePropertyType(1));
        sensGen.setCons(new SDFIntEdgePropertyType(1));
        SDFEdge genRec = graph.addEdge(gen5, recopie5);
        genRec.setProd(new SDFExpressionEdgePropertyType(new ExpressionValue("SIZE")));
        genRec.setCons(new SDFExpressionEdgePropertyType(new ExpressionValue("1+2")));
        SDFEdge genAcq = graph.addEdge(gen5, acqData);
        genAcq.setProd(new SDFIntEdgePropertyType(1));
        genAcq.setCons(new SDFIntEdgePropertyType(1));
        SDFEdge recAcq = graph.addEdgeWithInterfaces(recopie5, acqData);
        recAcq.setProd(new SDFIntEdgePropertyType(3));
        recAcq.setCons(new SDFIntEdgePropertyType(2));
        graph.addVariable(new Variable("a", "5"));
        graph.addVariable(new Variable("b", "10"));
        return graph;
    }
}
