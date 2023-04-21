package de.devisnik.shifting.impl;

import org.eclipse.swt.graphics.Point;

class TestShiftable implements IShiftable {

    private final Point itsPosition;

    public TestShiftable(int startX, int startY) {
        itsPosition = new Point(startX, startY);
    }

    public Point getPosition() {
        return itsPosition;
    }

    public boolean isDisposed() {
        return false;
    }

    public void setPosition(int x, int y) {
        itsPosition.x = x;
        itsPosition.y = y;
    }

    public Point getSize() {
        return new Point(10, 10);
    }
}
