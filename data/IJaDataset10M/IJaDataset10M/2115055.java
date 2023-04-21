package org.plazmaforge.studio.reportdesigner.figures;

import java.awt.Graphics2D;

public class J2DSubreportFigure extends J2DComponentFigure {

    private java.awt.Image image;

    private static int MARGIN = 10;

    public java.awt.Image getImage() {
        return image;
    }

    public void setImage(java.awt.Image image) {
        this.image = image;
    }

    protected void draw(Graphics2D graphics2d) {
        graphics2d.drawImage(getImage(), MARGIN, MARGIN, null);
    }
}
