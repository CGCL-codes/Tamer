package lcm.painters;

import java.awt.Color;
import java.awt.Graphics2D;
import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.textarea.Gutter;

public class ColoredRectDirtyMarkPainter implements DirtyMarkPainter {

    static final int WIDTH = 12;

    protected int width = WIDTH;

    protected Color color = null;

    public void setWidth(int width) {
        this.width = width;
    }

    public void setColor(Color c) {
        color = c;
    }

    public void paint(Graphics2D gfx, Gutter gutter, int y, int height, Buffer buffer, int physicalLine) {
        Color c = gfx.getColor();
        gfx.setColor(color);
        gfx.fillRect(gutter.getWidth() - width, y, width, height);
        gfx.setColor(c);
    }
}
