package modelo.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

/**
 * Clase que nos da una funcionalidad más avanzada, para dibujar figuras
 * complejas, como por ejemplo las flechas.
 */
public class DrawUtils {

    /**
	 * Permite dibujar una flecha a partir de las coordenadas iniciales y las finales.
	 * @param g2d El aparato gráfico en el que se va a dibujar.
	 * @param xOrigen La coordenada x final de la flecha.
	 * @param yOrigen La coordenada y final de la flecha.
	 * @param xDestino La coordeanda x inicial de la flecha.
	 * @param yDestino La coordeanda y inicial de la flecha.
	 * @param stroke Tamaño de la punta de la flecha.
	 * @param patron El patrón a usar en la línea de la flecha. Si es null, se dibujará
	 * una línea normal.
	 * @param color El color con el que se dibujará la flecha.
	 */
    public static void drawArrow(Graphics2D g2d, int xOrigen, int yOrigen, int xDestino, int yDestino, float stroke, float[] patron, Color color) {
        double aDir = Math.atan2(xOrigen - xDestino, yOrigen - yDestino);
        g2d.setColor(color);
        if (patron != null) {
            BasicStroke bs = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, patron, 0);
            g2d.setStroke(bs);
        } else {
            g2d.setStroke(new BasicStroke(1f));
        }
        g2d.drawLine(xDestino, yDestino, xOrigen, yOrigen);
        g2d.setStroke(new BasicStroke(1f));
        Polygon tmpPoly = new Polygon();
        int i1 = 12 + (int) (stroke * 2);
        int i2 = 6 + (int) stroke;
        tmpPoly.addPoint(xDestino, yDestino);
        tmpPoly.addPoint(xDestino + xCor(i1, aDir + .5), yDestino + yCor(i1, aDir + .5));
        tmpPoly.addPoint(xDestino + xCor(i2, aDir), yDestino + yCor(i2, aDir));
        tmpPoly.addPoint(xDestino + xCor(i1, aDir - .5), yDestino + yCor(i1, aDir - .5));
        tmpPoly.addPoint(xDestino, yDestino);
        g2d.drawPolygon(tmpPoly);
        g2d.fillPolygon(tmpPoly);
    }

    /***************************************************************************
	 * Métodos privados para ayuda al dibujo
	 **************************************************************************/
    private static int yCor(int len, double dir) {
        return (int) (len * Math.cos(dir));
    }

    private static int xCor(int len, double dir) {
        return (int) (len * Math.sin(dir));
    }
}
