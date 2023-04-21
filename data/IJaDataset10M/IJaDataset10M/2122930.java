package de.devisnik.eidle.flashlight;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;

public class Spot implements ISpot {

    private int itsRadius;

    private final Point itsCenter;

    private Point itsDirection;

    private Region itsRegion;

    private IShape itsShape;

    private Point itsSpeed;

    private final IShapeFactory itsShapeFactory;

    private boolean itsIsShapeUpdated;

    private Point itsRegionCenter;

    public Spot(final int radius, final Point startPosition, final int directionX, final int directionY, final int speedX, final int speedY, final IShapeFactory shapeFactory) {
        itsRadius = radius;
        itsCenter = startPosition;
        itsSpeed = new Point(speedX, speedY);
        itsShapeFactory = shapeFactory;
        itsDirection = new Point(directionX, directionY);
        itsShape = createShape();
    }

    private IShape createShape() {
        itsIsShapeUpdated = true;
        return itsShapeFactory.createCircle(itsRadius);
    }

    public Region getRegion() {
        updateRegion();
        return itsRegion;
    }

    public Rectangle move(final Rectangle rectangle) {
        return updateFocusPoint(rectangle);
    }

    private Region createRegion(final Point center) {
        final Region newRegion = new Region();
        newRegion.add(itsShape.getPointArray(center.x, center.y));
        return newRegion;
    }

    private void updateRegion() {
        if (itsIsShapeUpdated) {
            if (itsRegion != null) {
                itsRegion.dispose();
            }
            itsRegion = createRegion(itsCenter);
        } else {
            itsRegion.translate(itsCenter.x - itsRegionCenter.x, itsCenter.y - itsRegionCenter.y);
        }
        itsRegionCenter = new Point(itsCenter.x, itsCenter.y);
    }

    private Rectangle updateFocusPoint(final Rectangle rectangle) {
        if (rectangle.width == 0 || rectangle.height == 0) {
            return new Rectangle(0, 0, 0, 0);
        }
        final Rectangle oldBoundingBox = new Rectangle(itsCenter.x - itsRadius - 1, itsCenter.y - itsRadius - 1, 2 * (itsRadius + 1), 2 * (itsRadius + 1));
        if (itsCenter.x >= rectangle.width - itsRadius) {
            itsCenter.x = rectangle.width - itsRadius;
            itsDirection.x = -1;
        }
        if (itsCenter.x <= itsRadius) {
            itsCenter.x = itsRadius;
            itsDirection.x = 1;
        }
        if (itsCenter.y >= rectangle.height - itsRadius) {
            itsCenter.y = rectangle.height - itsRadius;
            itsDirection.y = -1;
        }
        if (itsCenter.y <= itsRadius) {
            itsCenter.y = itsRadius;
            itsDirection.y = 1;
        }
        itsCenter.x += itsDirection.x * itsSpeed.x;
        itsCenter.y += itsDirection.y * itsSpeed.y;
        final Rectangle newBoundingBox = new Rectangle(itsCenter.x - itsRadius - 1, itsCenter.y - itsRadius - 1, 2 * (itsRadius + 1), 2 * (itsRadius + 1));
        return oldBoundingBox.union(newBoundingBox);
    }

    public void dispose() {
        if (itsRegion != null) {
            itsRegion.dispose();
        }
    }

    public void setRadius(final int radius) {
        itsRadius = radius;
        itsShape = createShape();
    }

    public void setSpeed(final int speedX, final int speedY) {
        itsSpeed = new Point(speedX, speedY);
    }

    public Point getSpeed() {
        return itsSpeed;
    }

    public void setDirection(final int dirX, final int dirY) {
        itsDirection = new Point(dirX, dirY);
    }

    public Point getDirection() {
        return itsDirection;
    }
}
