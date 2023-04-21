package vistas;

import java.awt.Color;
import titiritero.vista.Cuadrado;

public class VistaArma extends Cuadrado {

    private static int TAMANIO_ARMA = 30;

    public VistaArma() {
        super(TAMANIO_ARMA, TAMANIO_ARMA);
        setColor(Color.GREEN);
    }
}
