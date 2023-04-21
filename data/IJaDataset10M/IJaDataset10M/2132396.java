package com.amerilib.states;

import com.amerilib.StateShape;
import com.amerilib.States;
import java.awt.geom.Area;

/**
 * @author Jason Nichols (jason@amerilib.com
 */
public class RIState extends StateShape {

    public RIState() {
        super(States.lookupState("RI"));
        xOffset = 266.257f;
        yOffset = 56.358f;
        path.moveTo(0.91000366, 4.8779984);
        path.lineTo(0.7620239, 3.5959969);
        path.lineTo(0.5180054, 2.2659988);
        path.lineTo(0.0, 0.46699905);
        path.lineTo(1.7480164, 0.0);
        path.lineTo(2.2400208, 0.34399796);
        path.lineTo(3.2750244, 1.6749992);
        path.lineTo(4.162018, 3.0289993);
        path.lineTo(3.2750244, 3.4979973);
        path.lineTo(2.8789978, 3.4489975);
        path.lineTo(2.5350037, 3.9919968);
        path.lineTo(1.7950134, 4.581997);
        path.lineTo(0.91000366, 4.8779984);
        path.closePath();
        area = new Area(path);
        updateTranslatedOffsets();
        updateModifiedOffsets();
    }
}
