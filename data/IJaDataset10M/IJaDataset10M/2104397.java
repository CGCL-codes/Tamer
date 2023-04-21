package jung;

import java.awt.Shape;
import java.awt.geom.GeneralPath;

public class BentLine<V, E> extends edu.uci.ics.jung.visualization.decorators.EdgeShape.BentLine<V, E> implements EdgeShaper {

    public BentLine() {
        super();
    }

    @Override
    public Shape toShape() {
        final float controlY = 0.5f;
        final GeneralPath thisPath = new GeneralPath();
        thisPath.moveTo(0.0f, 0.0f);
        thisPath.lineTo(0.5f, controlY);
        thisPath.lineTo(1.0f, 0.0f);
        return thisPath;
    }
}
