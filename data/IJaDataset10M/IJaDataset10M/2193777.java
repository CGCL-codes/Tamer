package org.gvsig.raster.beans.canvas.layers;

import java.awt.Color;
import java.awt.Graphics;
import org.gvsig.raster.beans.canvas.DrawableElement;

/**
 * Gr�fico que representa un borde para el canvas. Dibuja un cuadrado sin relleno en el perimetro m�ximo de
 * dibujo del canvas. 
 *
 * 14-oct-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class Border extends DrawableElement {

    public static final int BORDER = 5;

    /**
	 * Constructor. Asigna el color
	 * @param c
	 */
    public Border(Color c) {
        setColor(c);
    }

    /**
	 * Dibujado de la l�nea de incremento exponencial sobre el canvas.
	 */
    public void paint(Graphics g) {
        g.setColor(color);
        g.drawRect(canvas.getCanvasMinX(), canvas.getCanvasMinY(), canvas.getCanvasWidth(), canvas.getCanvasHeight());
    }

    public void firstActions() {
        canvas.addBorder(BORDER, BORDER, BORDER, BORDER);
    }

    public void firstDrawActions() {
    }
}
