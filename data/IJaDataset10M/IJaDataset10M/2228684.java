package ow.tool.util.vizframework.geom;

import java.awt.Shape;
import java.awt.geom.Point2D;
import ow.id.ID;

public class VortexGeometryManager extends AbstractCircleGeometryManager {

    public Shape getShapeForMessage(ID src, ID dest) {
        double srcDbl = src.toBigInteger().doubleValue();
        double destDbl = dest.toBigInteger().doubleValue();
        return this.getShapeForLineBetweenTwoPoints(srcDbl * 2.0, srcDbl / this.idSpaceSize, destDbl * 2.0, destDbl / this.idSpaceSize, 0.0, 0.5);
    }

    public Shape getShapeForConnection(ID from, ID to) {
        double fromDbl = from.toBigInteger().doubleValue();
        double toDbl = to.toBigInteger().doubleValue();
        return this.getShapeForLineBetweenTwoPoints(fromDbl * 2.0, fromDbl / this.idSpaceSize, toDbl * 2.0, toDbl / this.idSpaceSize, 0.7, 1.1);
    }

    public Point2D getNodePoint2D(ID id) {
        double idDbl = id.toBigInteger().doubleValue();
        return this.getNodePoint2D(idDbl * 2.0, idDbl / this.idSpaceSize);
    }
}
