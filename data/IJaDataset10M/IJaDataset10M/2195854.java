package JavaOrc.diagram;

import diagram.figures.*;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @class DeviceFigure
 *
 * @date 12-16-2004
 * @author Eric Crahen, krcpa
 * @version 1.1
 *
 * Stores the display information for a particular DeviceItem
 */
public class DeviceFigure extends RectangularFigure {

    private int lastDividerLocation = -1;

    public DeviceFigure() {
        super(100, 75);
    }

    public int getDividerLocation() {
        return lastDividerLocation;
    }

    public void setDividerLocation(int lastDividerLocation) {
        this.lastDividerLocation = lastDividerLocation;
    }
}
