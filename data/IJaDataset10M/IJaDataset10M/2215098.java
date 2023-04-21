package marten.age.control;

import marten.age.graphics.primitives.Point;

public interface MouseListener extends Listener {

    public abstract void mouseMove(Point coords);

    public abstract void mouseUp(Point coords);

    public abstract void mouseDown(Point coords);

    public abstract void mouseWheelRoll(int delta, Point coords);
}
