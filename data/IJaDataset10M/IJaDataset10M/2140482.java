package de.uni_trier.st.nevada.layout.orthogonal.Orthogonalization;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import de.uni_trier.st.nevada.layout.orthogonal.CyclicListIterator;
import de.uni_trier.st.nevada.layout.orthogonal.Dart;
import de.uni_trier.st.nevada.layout.orthogonal.Face;
import de.uni_trier.st.nevada.layout.orthogonal.OEdge;
import de.uni_trier.st.nevada.layout.orthogonal.ONode;
import de.uni_trier.st.nevada.layout.orthogonal.PlanarGraph;

public class SketchedSimpleShape extends QuasiOrthShape {

    private Map lookupBends, shapeEdges;

    private Map angleConstraints, bendConstraints;

    public SketchedSimpleShape(PlanarGraph planarGraph, Set acSet, Set bcSet, int alpha, int beta, int gamma) {
        super(planarGraph);
        lookupBends = new Hashtable();
        shapeEdges = new Hashtable();
        Map references = new Hashtable();
        NetEdge zeroCapacity = new NetEdge(new NetNode("dummy1", 0), new NetNode("dummy2", 0), 0, 0);
        angleConstraints = new Hashtable();
        for (Iterator i = acSet.iterator(); i.hasNext(); ) {
            AngleConstraint ac = (AngleConstraint) i.next();
            int supply = ac.getAngle() - ac.getDarts().size();
            NetNode angleNode = createNode(references, ac, ac.getAngle() - ac.getDarts().size());
            ONode node = ac.getNode();
            NetNode vertexNode = createNode(references, node, 0);
            ac.constraintVertexEdge = new NetEdge(angleNode, vertexNode, alpha, Integer.MAX_VALUE);
            ac.vertexConstraintEdge = new NetEdge(vertexNode, angleNode, alpha, Integer.MAX_VALUE);
            networkEdges.add(ac.constraintVertexEdge);
            networkEdges.add(ac.vertexConstraintEdge);
            for (Iterator j = ac.getDarts().iterator(); j.hasNext(); ) {
                angleConstraints.put(j.next(), ac);
            }
        }
        bendConstraints = new Hashtable();
        for (Iterator i = bcSet.iterator(); i.hasNext(); ) {
            BendConstraint bc = (BendConstraint) i.next();
            int length = bc.getLength();
            for (int j = 0; j < length; j++) networkNodes.add(bc.getNode(j));
            bendConstraints.put(bc.getEdge(), bc);
        }
        for (Iterator it = planarGraph.embeddedNodes().iterator(); it.hasNext(); ) {
            ONode node = (ONode) it.next();
            int degree = planarGraph.getDegree(node);
            if (degree == 0) continue;
            Dart dart = planarGraph.getDarts(node).head(), prev = null;
            if (angleConstraints.containsKey(dart) && !node.isCrossing()) {
                for (int i = 0; i < degree; i++) {
                    prev = dart;
                    dart = planarGraph.next(dart);
                    addSketchedDart(references, dart, prev, zeroCapacity, Math.max(1, beta + gamma));
                }
            } else {
                for (int i = 0; i < degree; i++) {
                    prev = dart;
                    dart = planarGraph.next(dart);
                    addKandinskyDart(references, dart, prev, zeroCapacity);
                }
            }
        }
        int betaMinGamma = Math.max(0, beta - gamma);
        for (Iterator it = planarGraph.embeddedEdges().iterator(); it.hasNext(); ) {
            OEdge edge = (OEdge) it.next();
            if (bendConstraints.containsKey(edge)) {
                BendConstraint bc = (BendConstraint) bendConstraints.get(edge);
                String bends = bc.getBends();
                int length = bc.getLength();
                Dart regdart = edge.regularDart();
                Face regFace = planarGraph.getFace(regdart);
                Face revFace = planarGraph.getFace(edge.reverseDart());
                NetNode regFaceNode = createFaceNode(references, regFace, bendConstraints);
                NetNode revFaceNode = createFaceNode(references, revFace, bendConstraints);
                for (int i = 0; i < length; i++) {
                    if (bends.charAt(i) == '0') {
                        NetEdge netedge = new NetEdge(bc.getNode(i), regFaceNode, betaMinGamma, 1);
                        networkEdges.add(netedge);
                        bc.setCostEdge(i, netedge);
                        netedge = new NetEdge(bc.getNode(i), revFaceNode, 0, 2);
                        networkEdges.add(netedge);
                        bc.setBendEdge(i, netedge);
                    } else {
                        NetEdge netedge = new NetEdge(bc.getNode(i), revFaceNode, betaMinGamma, 1);
                        networkEdges.add(netedge);
                        bc.setCostEdge(i, netedge);
                        netedge = new NetEdge(bc.getNode(i), regFaceNode, 0, 2);
                        networkEdges.add(netedge);
                        bc.setBendEdge(i, netedge);
                    }
                }
            }
        }
        references.clear();
        recomputeShape();
    }

    protected void addSketchedDart(Map references, Dart dart, Dart prev, NetEdge zeroCapacity, int betaMinGamma) {
        if (!shapeEdges.containsKey(dart)) shapeEdges.put(dart, new NetEdge[4]);
        if (!shapeEdges.containsKey(prev)) shapeEdges.put(prev, new NetEdge[4]);
        NetEdge dartEdges[] = (NetEdge[]) shapeEdges.get(dart);
        NetEdge prevEdges[] = (NetEdge[]) shapeEdges.get(prev);
        AngleConstraint dartAC = (AngleConstraint) angleConstraints.get(dart);
        AngleConstraint prevAC = (AngleConstraint) angleConstraints.get(prev);
        NetNode dartACNode = createNode(references, dartAC, 0);
        NetNode prevACNode = createNode(references, prevAC, 0);
        Face dartFace = planarGraph.getFace(dart);
        Face prevFace = planarGraph.getFace(prev);
        NetNode dartFaceNode = createFaceNode(references, dartFace, bendConstraints);
        NetNode prevFaceNode = createFaceNode(references, prevFace, bendConstraints);
        if (dartFace.equals(prevFace)) dartEdges[2] = zeroCapacity; else {
            dartEdges[2] = new NetEdge(dartFaceNode, prevFaceNode, betaMinGamma, Integer.MAX_VALUE);
            networkEdges.add(dartEdges[2]);
        }
        dartEdges[0] = new NetEdge(dartACNode, dartFaceNode, 0, 3);
        networkEdges.add(dartEdges[0]);
        prevEdges[1] = new NetEdge(dartFaceNode, prevACNode, betaMinGamma, 1);
        networkEdges.add(prevEdges[1]);
        dartEdges[3] = prevEdges[1];
    }

    protected void addKandinskyDart(Map references, Dart dart, Dart prev, NetEdge zeroCapacity) {
        if (!shapeEdges.containsKey(dart)) shapeEdges.put(dart, new NetEdge[4]);
        if (!shapeEdges.containsKey(prev)) shapeEdges.put(prev, new NetEdge[4]);
        NetEdge dartEdges[] = (NetEdge[]) shapeEdges.get(dart);
        NetEdge prevEdges[] = (NetEdge[]) shapeEdges.get(prev);
        ONode node = dart.target();
        int degree = planarGraph.getDegree(node);
        NetNode vertexNode = createNode(references, node, 4 - degree);
        Face dartFace = planarGraph.getFace(dart);
        Face prevFace = planarGraph.getFace(prev);
        NetNode dartFaceNode = createFaceNode(references, dartFace, bendConstraints);
        NetNode prevFaceNode = createFaceNode(references, prevFace, bendConstraints);
        if (dartFace.equals(prevFace)) dartEdges[2] = zeroCapacity; else {
            dartEdges[2] = new NetEdge(dartFaceNode, prevFaceNode, 1, Integer.MAX_VALUE);
            networkEdges.add(dartEdges[2]);
        }
        if (degree < 4) {
            dartEdges[0] = new NetEdge(vertexNode, dartFaceNode, 0, 3);
            networkEdges.add(dartEdges[0]);
            prevEdges[1] = zeroCapacity;
        } else if (degree == 4) {
            dartEdges[0] = prevEdges[1] = zeroCapacity;
        } else {
            dartEdges[0] = zeroCapacity;
            prevEdges[1] = new NetEdge(dartFaceNode, vertexNode, 1, 1);
            networkEdges.add(prevEdges[1]);
        }
        dartEdges[3] = prevEdges[1];
    }

    @Override
    public void clear() {
        networkNodes.clear();
        networkEdges.clear();
        lookupBends.clear();
        shapeEdges.clear();
        angleConstraints.clear();
    }

    protected NetNode createFaceNode(Map references, Face face, Map bendConstraints) {
        NetNode value = (NetNode) references.get(face);
        if (value == null) {
            int supply = face.isOuter() ? -face.size() - 4 : 4 - face.size();
            for (CyclicListIterator it = face.getIterator(); it.hasNext(); ) {
                Dart dart = (Dart) it.next();
                if (bendConstraints.containsKey(dart.edge())) {
                    BendConstraint bc = (BendConstraint) bendConstraints.get(dart.edge());
                    supply -= bc.getLength();
                }
            }
            value = new NetNode(face.toDebug(), supply);
            networkNodes.add(value);
            references.put(face, value);
        }
        return value;
    }

    @Override
    public int getAngle(Dart dart) {
        NetEdge dartEdges[] = (NetEdge[]) shapeEdges.get(dart);
        return dartEdges[0].getFlow() + 1 - dartEdges[1].getFlow();
    }

    @Override
    public int getAngle(ONode node, OEdge edge) {
        return getAngle(edge.getDart(node));
    }

    @Override
    public String getBends(Dart dart) {
        StringBuffer resBuffer = (StringBuffer) lookupBends.get(dart);
        if (resBuffer == null) {
            resBuffer = new StringBuffer();
            NetEdge dualDarts[] = (NetEdge[]) shapeEdges.get(dart.dual());
            if (!dart.source().isCrossing() && dualDarts[3].getFlow() > 0) resBuffer.append("2");
            int flow = dualDarts[2].getFlow();
            for (int i = 0; i < flow; i++) resBuffer.append("1");
            NetEdge argDarts[] = (NetEdge[]) shapeEdges.get(dart);
            if (bendConstraints.containsKey(dart.edge())) {
                BendConstraint bc = (BendConstraint) bendConstraints.get(dart.edge());
                String bends = bc.getBends();
                int length = bc.getLength();
                if (dart.target().equals(dart.edge().getTarget())) {
                    for (int i = 0; i < length; i++) {
                        if (bc.getBendedge(i).getFlow() > 1) resBuffer.append(bends.charAt(i));
                    }
                } else {
                    for (int i = length - 1; i >= 0; i--) {
                        if (bc.getBendedge(i).getFlow() > 1) {
                            if (bends.charAt(i) == '0') resBuffer.append("1"); else resBuffer.append("0");
                        }
                    }
                }
            }
            flow = argDarts[2].getFlow();
            for (int i = 0; i < flow; i++) resBuffer.append("0");
            if (argDarts[3].getFlow() > 0) resBuffer.append("0");
            lookupBends.put(dart, resBuffer);
        }
        return resBuffer.toString();
    }

    @Override
    public String getBends(ONode node, OEdge edge) {
        return getBends(edge.getDart(node));
    }

    @Override
    public void recomputeShape() {
        new Network().maxFlowWithMinCost(networkNodes, networkEdges);
    }
}
