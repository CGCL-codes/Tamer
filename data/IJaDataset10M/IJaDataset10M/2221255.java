package zuilib.components;

import processing.core.PConstants;

public class CircleHandle_Rect extends circlehandle {

    private float boxsize;

    public CircleHandle_Rect(String sname, float fx, float fy, float fsize, float fboxsize, float flength) {
        super(sname, fx, fy, fsize, fboxsize, flength);
        box = new RectButton(Name.get() + "_Rect", 0, 0);
        box.setMode(PConstants.CENTER);
        boxsize = fboxsize;
    }

    public void setup() {
        super.setup();
        ((RectButton) box).dimension.set(boxsize, boxsize);
        setValuePercent(0);
    }
}
