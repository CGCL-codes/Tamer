package net.sf.josceleton.prototype.console;

import net.sf.josceleton.core.api.entity.location.Coordinate;
import net.sf.josceleton.core.api.entity.location.Direction;

public class SomeUtil {

    public static int prettyPrint(final Coordinate coordinate, final Direction direction) {
        final float rawValue;
        if (direction == Direction.X) {
            rawValue = coordinate.x();
        } else if (direction == Direction.Y) {
            rawValue = coordinate.y();
        } else {
            rawValue = coordinate.z();
        }
        return Math.round(rawValue * 100);
    }
}
