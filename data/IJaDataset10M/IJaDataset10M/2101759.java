package org.cresques.px;

import org.cresques.geo.ViewPortData;
import java.awt.Graphics2D;

public interface Drawable {

    public void draw(Graphics2D g, ViewPortData vp);
}
