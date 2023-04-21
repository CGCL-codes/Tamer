package zuilib.components;

import processing.core.PConstants;
import zuilib.properties.CircleDimension;

public class CircleButton extends button {

    public CircleDimension dimension;

    public CircleButton(String sname, float fx, float fy) {
        super(sname, fx, fy);
        mode = PConstants.CORNER;
        dimension = new CircleDimension();
    }

    public boolean over() {
        dimension.over = overCircle(dimension.size, mode);
        return dimension.over;
    }

    public void draw() {
        super.draw();
        graphic.ellipseMode(mode);
        graphic.ellipse(0, 0, dimension.size, dimension.size);
    }
}
