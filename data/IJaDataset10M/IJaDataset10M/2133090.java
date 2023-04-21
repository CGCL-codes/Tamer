package org.jrman.sl;

import org.jrman.grid.BooleanGrid;
import org.jrman.grid.Color3fGrid;
import org.jrman.grid.Point3fGrid;
import org.jrman.grid.Tuple3fGrid;
import org.jrman.grid.Vector3fGrid;

public class Tuple3fGridValue extends Value {

    private Tuple3fGrid value;

    public static Tuple3fGridValue cast(FloatGridValue fgv) {
        Point3fGrid p3fg = Point3fGrid.getInstance();
        p3fg.set(fgv.getValue());
        return new Tuple3fGridValue(p3fg);
    }

    public Tuple3fGrid getCompatibleTupleGrid() {
        if (value instanceof Point3fGrid) return Point3fGrid.getInstance();
        if (value instanceof Vector3fGrid) return Vector3fGrid.getInstance();
        return Color3fGrid.getInstance();
    }

    public Tuple3fGridValue(Tuple3fGrid value) {
        this.value = value;
    }

    public Tuple3fGrid getValue() {
        return value;
    }

    public Value add(Value other, BooleanGrid cond) {
        if (other instanceof Tuple3fGridValue) {
            Tuple3fGrid t3fg = getCompatibleTupleGrid();
            t3fg.add(value, ((Tuple3fGridValue) other).value, cond);
            return new Tuple3fGridValue(t3fg);
        }
        if (other instanceof Tuple3fValue) {
        }
        if (other instanceof FloatValue) {
        }
        if (other instanceof FloatGridValue) {
        }
        return other.reverseAdd(this, cond);
    }
}
