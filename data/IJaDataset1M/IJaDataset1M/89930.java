package modelo.herramientas.dibujo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;
import modelo.Elemento;
import modelo.utils.DrawUtils;

/**
 * El dibujo de una flecha.
 */
public class Flecha implements Elemento, Serializable {

    private static final long serialVersionUID = 1L;

    private Point destino;

    private Point origen;

    private float[] patron;

    private Color color;

    private float sizePunta;

    /**
	 * @param destino El punto al que apunta la flecha.
	 * @param origen El origen de la flecha, desde donde sale.
	 * @param patron El patrón que tiene la línea de la flecha.
	 * Puede ser null.
	 * @param color El color con el que se dibujará la flecha.
	 * @param sizePunta El tamaño de la punta.
	 */
    public Flecha(Point origen, Point destino, float[] patron, Color color, float sizePunta) {
        super();
        this.destino = destino;
        this.origen = origen;
        if (patron.length <= 0) {
            this.patron = null;
        } else {
            this.patron = patron;
        }
        this.color = color;
        this.sizePunta = sizePunta;
    }

    public void paint(Graphics g) {
        DrawUtils.drawArrow((Graphics2D) g, origen.x, origen.y, destino.x, destino.y, this.sizePunta, patron, this.color);
    }
}
