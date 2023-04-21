package com.dukesoftware.utils.awt.graphics;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class BasicInteractionTool {

    private final AffineTransform aff = new AffineTransform();

    private double origOffsetX, origOffsetY;

    private double scale;

    public BasicInteractionTool() {
        reset();
    }

    public void rotate(double theta) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void translate(double userOffsetX, double userOffsetY) {
        Point2D.Double userOffset = new Point2D.Double(userOffsetX, userOffsetY);
        AffineTransform inv;
        try {
            inv = new AffineTransform();
            inv.scale(scale, scale);
            inv = inv.createInverse();
            inv.transform(userOffset, userOffset);
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
        origOffsetX += userOffset.x;
        origOffsetY += userOffset.y;
        updateAffineTransform(origOffsetX * scale, origOffsetY * scale);
    }

    public void magnify(Point2D.Double usernewCenter, double displayCenterX, double displayCenterY, double newScale) {
        final Point2D.Double origCenter = new Point2D.Double();
        this.scale = newScale;
        AffineTransform inv;
        try {
            inv = aff.createInverse();
            inv.transform(usernewCenter, origCenter);
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
        double dx = displayCenterX - origCenter.x * scale;
        double dy = displayCenterY - origCenter.y * scale;
        updateAffineTransform(dx, dy);
        origOffsetX = dx / scale;
        origOffsetY = dy / scale;
    }

    private void updateAffineTransform(double dx, double dy) {
        aff.setToIdentity();
        aff.translate(dx, dy);
        aff.scale(scale, scale);
    }

    public final void transform(Graphics2D g2d) {
        g2d.transform(aff);
    }

    public void reset() {
        aff.setToIdentity();
        scale = 1.0;
    }
}
