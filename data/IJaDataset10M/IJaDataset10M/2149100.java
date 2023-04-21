package com.amerilib.states;

import com.amerilib.StateShape;
import com.amerilib.States;
import java.awt.geom.Area;

/**
 * @author Jason Nichols (jason@amerilib.com
 */
public class CTState extends StateShape {

    public CTState() {
        super(States.lookupState("CT"));
        xOffset = 258.055f;
        yOffset = 56.802f;
        path.moveTo(9.261017, 4.434002);
        path.lineTo(8.96402, 3.1520004);
        path.lineTo(8.720001, 1.8220024);
        path.lineTo(8.227997, 0.0);
        path.lineTo(6.649994, 0.34500122);
        path.lineTo(0.0, 1.7980003);
        path.lineTo(0.19500732, 2.8080025);
        path.lineTo(0.64001465, 5.026001);
        path.lineTo(0.64001465, 7.489002);
        path.lineTo(0.29400635, 8.176998);
        path.lineTo(0.8529968, 8.821003);
        path.lineTo(2.3650208, 7.785);
        path.lineTo(3.4470215, 6.7990036);
        path.lineTo(4.0390015, 6.1590004);
        path.lineTo(4.2850037, 6.356003);
        path.lineTo(5.1230164, 5.913002);
        path.lineTo(6.699005, 5.5670013);
        path.lineTo(9.261017, 4.434002);
        path.closePath();
        area = new Area(path);
        updateTranslatedOffsets();
        updateModifiedOffsets();
    }
}
