package Figuras;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * Clase Ovalo que extiende de la clase Rectangulo y se pueden manipular
 * objetos ovalo.
 * @since 1.6
 */
public class Ovalo extends Rectangulo {

    /**
     * Construye un ovalo.
     *
     * @param punto La coordenada inicial (x, y)
     * @param ancho El ancho
     * @param alto El alto
     * @param colorBorde El color del borde
     * @param colorRelleno El color del relleno
     * @param tamanhio El tamañio del borde
     * @since 1.6
     */
    public Ovalo(Point2D punto, int ancho, int alto, Color colorBorde, Color colorRelleno, int tamanhio) {
        super(punto, ancho, alto, colorBorde, colorRelleno, tamanhio);
    }

    /**
     * Construye un ovalo.
     *
     * @param x La coordenada x
     * @param y La coordenada y
     * @param ancho El ancho
     * @param alto El alto
     * @param colorBorde El color del borde
     * @param colorRelleno El color del relleno
     * @param tamanhio El tamañio del borde
     * @since 1.6
     */
    public Ovalo(int x, int y, int ancho, int alto, Color colorBorde, Color colorRelleno, int tamanhio) {
        super(x, y, ancho, alto, colorBorde, colorRelleno, tamanhio);
    }

    /**
     * Dibuja el ovalo.
     * @param g El objeto Graphics
     * @since 1.6
     */
    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2;
        Ellipse2D e2;
        Stroke bordeFigura;
        if (getColorRelleno() != null) {
            g.setColor(getColorRelleno());
            g.fillOval((int) getInicio().getX(), (int) getInicio().getY(), getAncho(), getAlto());
        }
        e2 = new Ellipse2D.Float((int) getInicio().getX(), (int) getInicio().getY(), getAncho(), getAlto());
        g2 = (Graphics2D) g;
        bordeFigura = new BasicStroke(getTamanhio(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        g2.setColor(getColorBorde());
        g2.setStroke(bordeFigura);
        g2.draw(e2);
    }
}
