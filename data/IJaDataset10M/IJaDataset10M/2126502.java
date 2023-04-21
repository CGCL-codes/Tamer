package edu.kds.circuit.wireShapeCreators.boundingBoxCreator.lines;

import java.awt.geom.Rectangle2D;
import edu.kds.circuit.*;
import edu.kds.circuit.visitors.CircuitVisitor;
import edu.kds.circuit.wireShapeCreators.boundingBoxCreator.BoundingBoxCreator;
import edu.kds.circuit.wireShapeCreators.boundingBoxCreator.dataStructure.*;

public class SimpleBoxStrategy implements BoxStrategy {

    public static final double componentMarginFact = 0.1;

    public static final double wireMargin = 0.05;

    public BoundingBox getBox(Connector start, Connector end) {
        BoundingBox box = createBox(start, end);
        AddLineVisitor lineAdder = new AddLineVisitor(box);
        try {
            end.getEndCConnector().getParent().getParent().accept(lineAdder);
        } catch (WireTraversalException exc) {
            try {
                end.getStartCConnector().getParent().getParent().accept(lineAdder);
            } catch (WireTraversalException exc1) {
            }
        }
        return box;
    }

    protected BoundingBox createBox(Connector start, Connector end) {
        return new BoundingBox(start, end);
    }

    protected static class AddLineVisitor implements CircuitVisitor {

        private final BoundingBox box;

        AddLineVisitor(BoundingBox box) {
            this.box = box;
        }

        public void visit(Circuit c) {
        }

        public void visit(CComponent c) {
            if (c instanceof CCompositeInner) return;
            if (box.intersects(c)) {
                box.includedComponents.add(c);
                for (BoxLine line : box.getTravelingLines()) {
                    if (line.intersects(c)) createLinesAround(c, line.axis);
                }
            }
        }

        public void visit(CConnector c) {
        }

        public void visit(WireEdge e) {
            if (BoundingBoxCreator.ignoreThese.contains(e)) return;
            if (box.intersects(e)) {
                box.includedEdges.add(e);
                for (BoxLine line : box.getTravelingLines()) {
                    if (line.intersects(e)) createLinesAround(e, line.axis);
                }
            }
        }

        public void visit(WireJoint j) {
        }

        public void visit(CLabel l) {
        }

        private void createLinesAround(WireEdge e, Axis axis) {
            if (!e.isHorizontal() && !e.isVertical()) return;
            if (axis == Axis.X) {
                box.addLine(e.getStartPoint().x + wireMargin, Axis.Y);
                box.addLine(e.getStartPoint().x - wireMargin, Axis.Y);
            } else {
                box.addLine(e.getStartPoint().y + wireMargin, Axis.X);
                box.addLine(e.getStartPoint().y - wireMargin, Axis.X);
            }
        }

        private void createLinesAround(CComponent c, Axis axis) {
            Rectangle2D.Double cRect = c.getBounds();
            double xMargin = cRect.width * componentMarginFact;
            double yMargin = cRect.height * componentMarginFact;
            if (axis == Axis.X) {
                box.addLine(cRect.y - yMargin, axis);
                box.addLine(cRect.y + cRect.height + yMargin, axis);
            } else {
                box.addLine(cRect.x - xMargin, axis);
                box.addLine(cRect.x + cRect.width + xMargin, axis);
            }
        }
    }
}
