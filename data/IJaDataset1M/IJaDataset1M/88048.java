package edu.kds.circuit.wireShapeCreators;

import edu.kds.circuit.*;
import edu.kds.circuit.geom.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleAlignedWireShapeCreator extends WireShapeCreator {

    public List<InchPoint> getWireShape(Connector start, Connector end) {
        List<InchPoint> ret = new ArrayList<InchPoint>(0);
        double xDif = end.getEdgePoint(null).getX() - start.getEdgePoint(null).getX();
        InchPoint p1 = new InchPoint(start.getEdgePoint(null).getX() + xDif / 2, start.getEdgePoint(null).getY());
        InchPoint p2 = new InchPoint(start.getEdgePoint(null).getX() + xDif / 2, end.getEdgePoint(null).getY());
        ret.add(p1);
        ret.add(p2);
        return ret;
    }
}
