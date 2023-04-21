package br.usp.ime.origami.model;

import br.usp.ime.origami.algorithms.Baskara;
import static java.lang.Math.*;

public class MovingTriangle {

    private MovingPoint p1, p2, p3;

    private boolean edge12, edge23, edge31;

    public MovingTriangle(MovingPoint p1, MovingPoint p2, MovingPoint p3) {
        super();
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public EventType getEventType() {
        double time = getCollapsingTime();
        if (p1.getPositionAt(time).almostEquals(p2.getPositionAt(time))) {
        }
        return null;
    }

    public double getCollapsingTime() {
        double c = p1.getPoint().getX() * p2.getPoint().getY() + p1.getPoint().getY() * p3.getPoint().getX() + p2.getPoint().getX() * p3.getPoint().getY() - (p2.getPoint().getY() * p3.getPoint().getX() + p1.getPoint().getY() * p2.getPoint().getX() + p1.getPoint().getX() * p3.getPoint().getY());
        double a1 = p1.getSpeed() * p2.getSpeed() * cos(p1.getDirectionAngle()) * sin(p2.getDirectionAngle());
        double a2 = p1.getSpeed() * p3.getSpeed() * sin(p1.getDirectionAngle()) * cos(p3.getDirectionAngle());
        double a3 = p2.getSpeed() * p3.getSpeed() * cos(p2.getDirectionAngle()) * sin(p3.getDirectionAngle());
        double a4 = p2.getSpeed() * p3.getSpeed() * sin(p2.getDirectionAngle()) * cos(p3.getDirectionAngle());
        double a5 = p1.getSpeed() * p2.getSpeed() * sin(p1.getDirectionAngle()) * cos(p2.getDirectionAngle());
        double a6 = p1.getSpeed() * p3.getSpeed() * cos(p1.getDirectionAngle()) * sin(p3.getDirectionAngle());
        double a = a1 + a2 + a3 - (a4 + a5 + a6);
        double b1 = p1.getPoint().getX() * p2.getSpeed() * sin(p2.getDirectionAngle()) + p2.getPoint().getY() * p1.getSpeed() * cos(p1.getDirectionAngle());
        double b2 = p1.getPoint().getY() * p3.getSpeed() * cos(p3.getDirectionAngle()) + p3.getPoint().getX() * p1.getSpeed() * sin(p1.getDirectionAngle());
        double b3 = p2.getPoint().getX() * p3.getSpeed() * sin(p3.getDirectionAngle()) + p3.getPoint().getY() * p2.getSpeed() * cos(p2.getDirectionAngle());
        double b4 = p2.getPoint().getY() * p3.getSpeed() * cos(p3.getDirectionAngle()) + p3.getPoint().getX() * p2.getSpeed() * sin(p2.getDirectionAngle());
        double b5 = p1.getPoint().getY() * p2.getSpeed() * cos(p2.getDirectionAngle()) + p2.getPoint().getX() * p1.getSpeed() * sin(p1.getDirectionAngle());
        double b6 = p1.getPoint().getX() * p3.getSpeed() * sin(p3.getDirectionAngle()) + p3.getPoint().getY() * p1.getSpeed() * cos(p1.getDirectionAngle());
        double b = b1 + b2 + b3 - (b4 + b5 + b6);
        Baskara baskara = new Baskara(a, b, c);
        if (baskara.hasRealRoots()) {
            if (baskara.getFirstRoot() > 0 && baskara.getSecondRoot() > 0) {
                return Math.min(baskara.getSecondRoot(), baskara.getFirstRoot());
            } else {
                if (baskara.getFirstRoot() > 0) {
                    return baskara.getFirstRoot();
                }
                if (baskara.getSecondRoot() > 0) {
                    return baskara.getSecondRoot();
                }
            }
        }
        return Double.MAX_VALUE;
    }

    public Triangle getPositionAt(double time) {
        return new Triangle(p1.getPositionAt(time), p2.getPositionAt(time), p3.getPositionAt(time));
    }

    @Override
    public String toString() {
        return String.format("[MovingTriangle %s %s %s]", p1, p2, p3);
    }

    public boolean isEdge12() {
        return edge12;
    }

    public boolean isEdge23() {
        return edge23;
    }

    public boolean isEdge31() {
        return edge31;
    }
}
