package edu.iu.iv.toolkits.vwtk.properties.assignable;

import java.awt.geom.Point2D;

public interface IPositionAssignable extends IAssignable {

    public void setPosition(Point2D.Float p);

    public Point2D.Float getPosition();
}
