package com.cosylab.vdct.graphics;

import java.awt.*;

/**
 * Insert the type's description here.
 * Creation date: (10.12.2000 13:07:53)
 * @author 
 */
public class BorderDecorator extends Decorator {

    /**
 * Insert the method's description here.
 * Creation date: (10.12.2000 13:28:06)
 */
    public BorderDecorator() {
    }

    /**
 * Default implementation
 * Creation date: (10.12.2000 11:25:20)
 */
    public void draw(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(0, 0, getComponentWidth() - 1, getComponentHeight() - 1);
        getComponent().draw(g);
        g.setColor(Color.black);
        g.drawRect(0, 0, getComponentWidth() - 1, getComponentHeight() - 1);
    }

    /**
 * Insert the method's description here.
 * Creation date: (11.12.2000 16:23:31)
 */
    public int getComponentHeight() {
        if (getComponent() == null) return 0; else return getComponent().getComponentHeight() + 10;
    }

    /**
 * Insert the method's description here.
 * Creation date: (11.12.2000 16:23:02)
 * @return int
 */
    public int getComponentWidth() {
        if (getComponent() == null) return 0; else return getComponent().getComponentWidth() + 10;
    }

    /**
 * Default implementation
 * Creation date: (10.12.2000 11:26:54)
 */
    public void resize(int x0, int y0, int width, int height) {
        getComponent().resize(x0 + 5, y0 + 5, width - 10, height - 10);
    }
}
