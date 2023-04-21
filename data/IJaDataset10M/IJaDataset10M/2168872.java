package titiritero.vista;

import java.awt.Color;
import titiritero.*;

public abstract class Figura implements Dibujable, MouseClickObservador {

    private Color color;

    private Posicionable posicionable;

    @Override
    public abstract void dibujar(SuperficieDeDibujo superfice);

    public void setColor(Color unColor) {
        this.color = unColor;
    }

    public Color getColor() {
        return this.color;
    }

    @Override
    public Posicionable getPosicionable() {
        return this.posicionable;
    }

    @Override
    public void setPosicionable(Posicionable posicionable) {
        this.posicionable = posicionable;
    }

    @Override
    public void mouseClick(int x, int y) {
        System.out.println("Click;" + x + "," + y);
    }
}
