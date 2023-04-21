package com.dukesoftware.ongakumusou.canvasdraw;

import java.awt.Graphics2D;

/**
 * 
 * <p></p>
 *
 * <h5>update history</h5> 
 * <p>2007/07/18 This file was created.</p>
 *
 * @author 
 * @since 2007/07/18
 * @version last update 2007/07/18
 */
public class NormalDraw extends Draw {

    public NormalDraw(Drawer drawer) {
        super(drawer);
    }

    @Override
    public void draw(Graphics2D g) {
        drawer.updateAndDrawElements(g);
    }
}
