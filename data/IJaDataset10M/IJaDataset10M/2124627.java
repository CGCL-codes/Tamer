package co.edu.javeriana.hci.logica.general;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *
 * @author Administrador
 */
@XStreamAlias("punto")
public class Punto {

    @XStreamAlias("x")
    private int X;

    @XStreamAlias("y")
    private int Y;

    public Punto() {
        X = 0;
        Y = 0;
    }

    public Punto(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public Punto(Punto punto) {
        this.setX(punto.getX());
        this.setY(punto.getY());
    }

    public void sumarPunto(Punto punto) {
        this.setX(this.getX() + punto.getX());
        this.setY(this.getY() + punto.getY());
    }

    public void restarPunto(Punto punto) {
        this.setX(this.getX() - punto.getX());
        this.setY(this.getY() - punto.getY());
    }

    public boolean equals(Punto punto) {
        boolean retorno;
        retorno = false;
        if ((this.getX() == punto.getX()) || (this.getY() == punto.getY())) {
            retorno = true;
        }
        return retorno;
    }

    public int getX() {
        return X;
    }

    public void setX(int X) {
        this.X = X;
    }

    public int getY() {
        return Y;
    }

    public void setY(int Y) {
        this.Y = Y;
    }
}
