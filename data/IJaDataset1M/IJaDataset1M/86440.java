package co.fxl.gui.swing;

import java.awt.Color;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.impl.ColorTemplate;

public abstract class SwingColor extends ColorTemplate implements IColor {

    protected abstract void setColor(Color color);

    @Override
    public IColor setRGB(int r, int g, int b) {
        assertRange(r);
        assertRange(g);
        assertRange(b);
        setColor(new Color(r, g, b));
        return this;
    }

    private void assertRange(int r) {
        assert r >= 0 && r <= 255 : "color out of range: " + r;
    }

    @Override
    public IColor remove() {
        setColor(null);
        return this;
    }
}
