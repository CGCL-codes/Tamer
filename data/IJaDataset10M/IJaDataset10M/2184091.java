package org.ximtec.igesture.app.showcaseapp;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

/**
 * @version 1.0 Nov 2006
 * @author Ueli Kurmann, igesture@uelikurmann.ch
 * @author Beat Signer, signer@inf.ethz.ch
 */
public class Style {

    private Color color;

    private Stroke stroke;

    public Style() {
        color = Color.BLACK;
        stroke = new BasicStroke(1.0f);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    public Stroke getStroke() {
        return stroke;
    }
}
