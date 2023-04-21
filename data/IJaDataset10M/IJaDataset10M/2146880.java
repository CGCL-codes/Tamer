package modelo;

import ar.uba.fi.algo3.titiritero.vista.ObjetoDeTexto;

public class PuntosSimples implements Puntos, ObjetoDeTexto {

    private int puntos;

    public PuntosSimples(int valor) {
        puntos = valor;
    }

    public int getPuntos(int valor) {
        return puntos;
    }

    public int getPuntos() {
        return puntos;
    }

    public void sumarPuntos(int valor) {
        puntos += valor;
        if (puntos < 0) puntos = 0;
    }

    public String getTexto() {
        return "Puntos: " + Integer.toString(puntos);
    }
}
