package edu.kds.gui.editor;

import java.util.*;
import edu.kds.circuit.*;
import edu.kds.circuit.geom.*;
import edu.kds.circuit.wireShapeCreators.WireShapeCreator;
import edu.kds.circuit.wireShapeCreators.boundingBoxCreator.BoundingBoxCreator;
import edu.kds.gui.editor.component.SComponent;

public class WireShapeUpdater {

    private static final boolean SHAPE_UPDATE_DISABLED = true;

    private static final WireShapeCreator creator = new BoundingBoxCreator();

    public static void layoutWires(SComponent sComp) {
        layoutWires(sComp.getCComponent());
    }

    public static void layoutWires(CComponent cComp) {
        if (SHAPE_UPDATE_DISABLED) return;
        (new RelayoutThread(cComp)).start();
    }

    private static class RelayoutThread extends Thread {

        private final CComponent relayoutThis;

        private static final int ITERATIONS = 2;

        public RelayoutThread(CComponent comp) {
            relayoutThis = comp;
        }

        public void run() {
            for (int i = 0; i < ITERATIONS; i++) relayout(relayoutThis);
        }
    }

    private static void relayout(CComponent comp) {
        for (CConnector con : comp.connectors) {
            Collection<WireEdge> tmp = con.getEdges();
            for (WireEdge edge : tmp) {
                cleanupWire(edge);
            }
        }
    }

    public static synchronized void cleanupWire(WireEdge edge) {
        List<WireEdge> existingWire = edge.getWire();
        int joints = existingWire.size() - 1;
        Connector start = existingWire.get(0).start;
        Connector end = existingWire.get(existingWire.size() - 1).end;
        for (WireEdge e : existingWire) creator.ignoreThis(e);
        List<InchPoint> path = creator.getWireShape(start, end);
        creator.clearIgnored();
        while (joints < path.size()) {
            InchPoint p = edge.start.getPos().clone();
            WireEdge newEdge = existingWire.get(0).split(p);
            existingWire = newEdge.getWire();
            joints = existingWire.size() - 1;
        }
        while (joints > path.size()) {
            WireEdge newEdge = ((WireJoint) existingWire.get(0).end).deleteJoint();
            if (newEdge == null) {
                System.err.println("Internal error. Sorry");
                return;
            }
            existingWire = newEdge.getWire();
            joints = existingWire.size() - 1;
        }
        for (int i = 0; i < joints; i++) {
            InchVector v = existingWire.get(i).end.getPos().vectorTo(path.get(i));
            ((WireJoint) existingWire.get(i).end).moveJoint(v);
        }
        if (existingWire.get(0).start instanceof CConnector) {
            ((CConnector) existingWire.get(0).start).sanitizeEdges(null);
        } else if (existingWire.get(0).end instanceof CConnector) {
            ((CConnector) existingWire.get(0).end).sanitizeEdges(null);
        }
        if (existingWire.get(existingWire.size() - 1).start instanceof CConnector) {
            ((CConnector) existingWire.get(existingWire.size() - 1).start).sanitizeEdges(null);
        } else if (existingWire.get(existingWire.size() - 1).end instanceof CConnector) {
            ((CConnector) existingWire.get(existingWire.size() - 1).end).sanitizeEdges(null);
        }
    }
}
