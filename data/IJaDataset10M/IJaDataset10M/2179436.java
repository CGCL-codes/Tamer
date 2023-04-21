package fullflow;

import java.awt.Point;

/**
 *
 * @author blackmoon
 */
public class JConector extends JFigura {

    private Point inicio;

    private Point fin;

    public JConector(Point inicio, Point fin) {
        super(Math.abs((int) (fin.x - inicio.x)), Math.abs((int) (fin.y - inicio.y)));
        this.inicio = inicio;
        this.fin = fin;
        this.setLocation(Math.abs((int) (inicio.x)), Math.abs((int) (inicio.y)));
        if (inicio.getX() < fin.getX()) {
            figura.moveTo(0, 0);
            figura.lineTo(0, 0);
            figura.lineTo(0, this.getHeight() / 2);
            figura.lineTo(this.getWidth() - 5, this.getHeight() / 2);
            figura.lineTo(this.getWidth() - 5, this.getHeight());
        } else if (inicio.getX() > fin.getX()) {
            this.setLocation(Math.abs((int) (inicio.x)) - this.getWidth() - 5, Math.abs((int) (inicio.y)));
            figura.moveTo(this.getWidth(), 0);
            figura.lineTo(this.getWidth() - 5, 0);
            figura.lineTo(this.getWidth() - 5, this.getHeight() / 2);
            figura.lineTo(10, this.getHeight() / 2);
            figura.lineTo(10, this.getHeight());
        }
    }

    void redimenzionar() {
        figura.reset();
        if (inicio.getX() < fin.getX()) {
            figura.moveTo(0, 0);
            figura.lineTo(0, 0);
            figura.lineTo(0, this.getHeight() / 2);
            figura.lineTo(this.getWidth(), this.getHeight() / 2);
            figura.lineTo(this.getWidth(), this.getHeight());
        } else if (inicio.getX() > fin.getX()) {
            figura.moveTo(this.getWidth(), 0);
            figura.lineTo(this.getWidth() - 5, 0);
            figura.lineTo(this.getWidth() - 5, this.getHeight() / 2);
            figura.lineTo(10, this.getHeight() / 2);
            figura.lineTo(10, this.getHeight());
        }
    }
}
