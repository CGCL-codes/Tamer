package edu.sharif.ce.dml.mobisim.mobilitygenerator.simulation.model.maps;

import edu.sharif.ce.dml.common.logic.entity.Location;
import edu.sharif.ce.dml.common.parameters.logic.Parameter;
import edu.sharif.ce.dml.common.parameters.logic.primitives.IntegerParameter;
import edu.sharif.ce.dml.common.util.DevelopmentLogger;
import edu.sharif.ce.dml.mobisim.mobilitygenerator.simulation.model.maps.exception.InvalidLocationException;
import java.awt.*;
import static java.lang.Math.*;

/**
 * Created by IntelliJ IDEA.
 * User: Masoud
 * Date: Sep 20, 2007
 * Time: 12:40:38 PM
 */
public class DiskReflectiveMap extends Map implements ReflectiveMap, PassiveMap {

    private Location center;

    private int radius;

    private int borderRadius;

    private double accuracy = 100000;

    public java.util.Map<String, Parameter> getParameters() {
        java.util.Map<String, Parameter> parameters = super.getParameters();
        parameters.put("radius", new IntegerParameter("radius", radius));
        parameters.put("borderradius", new IntegerParameter("borderradius", borderRadius));
        return parameters;
    }

    public void setParameters(java.util.Map<String, Parameter> parameters) {
        super.setParameters(parameters);
        radius = (Integer) parameters.get("radius").getValue();
        borderRadius = (Integer) parameters.get("borderradius").getValue();
        mapOrigin = new Location(-borderRadius, -borderRadius);
        center = new Location(radius + mapOrigin.getX(), radius + mapOrigin.getY());
    }

    public int getBorderWidth() {
        return borderRadius;
    }

    public int getBorderHeight() {
        return borderRadius;
    }

    public int getAreaWidth() {
        return 2 * (radius - borderRadius);
    }

    public int getAreaHeight() {
        return 2 * (radius - borderRadius);
    }

    public double howFarFromBorder(double angle, Location loc) {
        double x2 = loc.getX() + Math.cos(angle) * 2 * radius;
        double y2 = loc.getY() + Math.sin(angle) * 2 * radius;
        Location hit1 = findHitPoint(loc.getX() - center.getX(), loc.getY() - center.getY(), x2 - center.getX(), y2 - center.getY(), radius - borderRadius);
        return loc.getLength(hit1.getX() + center.getX(), hit1.getY() + center.getY());
    }

    public boolean isOnBorder(Location loc) {
        return loc.getLength(center) == radius - borderRadius;
    }

    public int getHeight() {
        return 2 * radius;
    }

    public int getWidth() {
        return 2 * radius;
    }

    public void paint(Graphics2D g) {
        Stroke lastStroke = g.getStroke();
        g.drawOval((int) (mapOrigin.getX()), (int) (mapOrigin.getY()), getWidth(), getHeight());
        g.setStroke(dashed);
        g.drawOval((int) (getOrigin().getX() + borderRadius), (int) (getOrigin().getY() + borderRadius), getAreaWidth(), getAreaHeight());
        g.setStroke(lastStroke);
    }

    private Location findHitPoint(double x1, double y1, double x2, double y2, double r) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dr = sqrt(pow(dx, 2) + pow(dy, 2));
        double dD = x1 * y2 - x2 * y1;
        double temp1 = sqrt(pow(r, 2) * pow(dr, 2) - pow(dD, 2));
        if (pow(r, 2) * pow(dr, 2) - pow(dD, 2) <= 0) {
            DevelopmentLogger.logger.debug(pow(r, 2) * pow(dr, 2) - pow(dD, 2) + " : " + x1 + " : " + y1 + " : " + x2 + " : " + y2);
        }
        assert pow(r, 2) * pow(dr, 2) - pow(dD, 2) > 0 : "less than two intersection!";
        double sign = Math.signum(dy);
        sign = sign == 0 ? 1 : sign;
        double temp2 = sign * dx * temp1;
        double xHit1 = (dD * dy + temp2) / pow(dr, 2);
        double xHit2 = (dD * dy - temp2) / pow(dr, 2);
        temp2 = Math.abs(dy) * temp1;
        double yHit1 = (-dD * dx + temp2) / pow(dr, 2);
        double yHit2 = (-dD * dx - temp2) / pow(dr, 2);
        xHit1 = (int) (xHit1 * accuracy) / accuracy;
        yHit1 = (int) (yHit1 * accuracy) / accuracy;
        xHit2 = (int) (xHit2 * accuracy) / accuracy;
        yHit2 = (int) (yHit2 * accuracy) / accuracy;
        if (xHit1 >= min(x1, x2) && xHit1 <= max(x1, x2) && yHit1 >= min(y1, y2) && yHit1 <= max(y1, y2)) {
            return new Location(xHit1, yHit1);
        }
        if ((xHit2 >= min(x1, x2) && xHit2 <= max(x1, x2) && yHit2 >= min(y1, y2) && yHit2 <= max(y1, y2))) {
            return new Location(xHit2, yHit2);
        }
        Location hit1 = new Location(xHit1, yHit1);
        Location hit2 = new Location(xHit2, yHit2);
        if (hit1.getLength(x2, y2) > hit2.getLength(x2, y2)) {
            return hit2;
        } else {
            return hit1;
        }
    }

    public Location isHittedBorder(Location loc1, Location loc2, Location mirror) {
        if (loc2.getLength(center) <= radius - borderRadius) {
            return null;
        }
        double x1 = loc1.getX() - center.getX();
        double y1 = loc1.getY() - center.getY();
        double x2 = loc2.getX() - center.getX();
        double y2 = loc2.getY() - center.getY();
        x1 = (int) (x1 * accuracy) / accuracy;
        y1 = (int) (y1 * accuracy) / accuracy;
        x2 = Math.ceil(x2 * accuracy) / accuracy;
        y2 = Math.ceil(y2 * accuracy) / accuracy;
        double r = radius - borderRadius;
        Location hit = findHitPoint(x1, y1, x2, y2, r);
        double cosAlpha = hit.getX() / r;
        double sinAlpha = -hit.getY() / r;
        double rotatedX2 = x2 * cosAlpha - y2 * sinAlpha;
        double rotatedY2 = x2 * sinAlpha + y2 * cosAlpha;
        double rotatedXMirror = 2 * r - rotatedX2;
        double rotatedYMirror = rotatedY2;
        sinAlpha = -sinAlpha;
        double xM = rotatedXMirror * cosAlpha - rotatedYMirror * sinAlpha;
        double yM = rotatedXMirror * sinAlpha + rotatedYMirror * cosAlpha;
        if (new Location(xM, yM).getLength(0, 0) >= r) {
            xM = (int) (xM * accuracy) / accuracy;
            yM = (int) (yM * accuracy) / accuracy;
            if (new Location(xM, yM).getLength(0, 0) >= r) {
                xM = Math.floor(xM);
                yM = Math.floor(yM);
                while (new Location(xM, yM).getLength(0, 0) >= r) {
                    xM -= xM;
                    yM -= yM;
                }
            }
        }
        mirror.pasteCoordiantion(xM + center.getX(), yM + center.getY());
        hit.pasteCoordiantion(hit.getX() + center.getX(), hit.getY() + center.getY());
        return hit;
    }

    public void validateDestNode(Location loc) throws InvalidLocationException {
        if (loc.getLength(center) > radius) {
            throw new InvalidLocationException("");
        }
    }

    public void validateNode(Location loc) throws InvalidLocationException {
        if (loc.getLength(center) > radius - borderRadius) {
            throw new InvalidLocationException("");
        }
    }
}
