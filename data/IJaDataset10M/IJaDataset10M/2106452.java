package com.amerilib.states;

import com.amerilib.StateShape;
import com.amerilib.States;
import java.awt.geom.Area;

/**
 * @author Jason Nichols (jason@amerilib.com
 */
public class KSState extends StateShape {

    public KSState() {
        super(States.lookupState("KS"));
        xOffset = 115.216f;
        yOffset = 84.708f;
        path.moveTo(39.272003, 21.010002);
        path.lineTo(35.229004, 21.217003);
        path.lineTo(21.084991, 21.075996);
        path.lineTo(7.3589935, 20.450996);
        path.lineTo(0.0, 20.068);
        path.lineTo(1.2629929, 0.0);
        path.lineTo(7.917, 0.24500275);
        path.lineTo(20.248993, 0.663002);
        path.lineTo(33.696, 0.8010025);
        path.lineTo(35.54399, 0.8010025);
        path.lineTo(36.485, 1.637001);
        path.lineTo(37.179993, 1.7080002);
        path.lineTo(37.59999, 1.9860001);
        path.lineTo(37.59999, 2.8919983);
        path.lineTo(37.041, 3.3789978);
        path.lineTo(36.899994, 4.0759964);
        path.lineTo(37.52899, 5.1200027);
        path.lineTo(38.296997, 6.026001);
        path.lineTo(39.06099, 6.5839996);
        path.lineTo(39.481003, 10.1380005);
        path.lineTo(39.272003, 21.010002);
        path.closePath();
        area = new Area(path);
        updateTranslatedOffsets();
        updateModifiedOffsets();
    }
}
