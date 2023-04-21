package br.usp.iterador.model;

import java.awt.Color;

/**
 * Color painter
 * 
 * @author Guilherme Silveira
 */
public class PaintColor {

    private Color c = Color.YELLOW;

    public Color getColor() {
        return c;
    }

    /**
	 * Changes the color using r,g,b
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
    public void change(double r, double g, double b) {
        r = Math.min(Math.round(r), 255);
        g = Math.min(Math.round(g), 255);
        b = Math.min(Math.round(b), 255);
        c = new Color((int) r, (int) g, (int) b);
    }

    /**
	 * Changes the color
	 * 
	 * @param color
	 */
    public void change(Color color) {
        this.c = color;
    }
}
