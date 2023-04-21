package zuilib.components;

import zuilib.properties.AutoSizeController;
import zuilib.properties.ColorController;
import zuilib.properties.RectDimension;
import zuilib.utils.vector;

public class RectGroupControler extends GroupControler {

    public RectDimension dimension;

    public ColorController Color;

    public AutoSizeController autoSize;

    public RectGroupControler(String sname, vector pos, float fwidth, float fheight) {
        super(sname, pos);
        dimension = new RectDimension(fwidth, fheight);
        Color = new ColorController();
        autoSize = new AutoSizeController();
    }

    public RectGroupControler(String sname, float fx, float fy, float fwidth, float fheight) {
        super(sname, fx, fy);
        dimension = new RectDimension(fwidth, fheight);
        Color = new ColorController();
        autoSize = new AutoSizeController();
    }

    public RectGroupControler(String sname, vector pos) {
        super(sname, pos);
        dimension = new RectDimension();
        Color = new ColorController();
        autoSize = new AutoSizeController();
    }

    public RectGroupControler(String sname, float fx, float fy) {
        super(sname, fx, fy);
        dimension = new RectDimension();
        Color = new ColorController();
        autoSize = new AutoSizeController();
    }

    public void setup() {
        super.setup();
        Color.setup(this);
        autoSize.setup(this);
    }

    public void draw() {
        super.draw();
        graphic.rectMode(mode);
        graphic.rect(0, 0, dimension.width, dimension.height);
    }

    public boolean over() {
        dimension.over = overRect(dimension.width, dimension.height, mode);
        return dimension.over;
    }
}
