package fpuna.ia.othello.algoritmo;

import fpuna.ia.othello.Utils.Tablero;

/**
 *
 * @author gusamasan
 */
public abstract class Algoritmo {

    private int profundidad;

    public Algoritmo() {
    }

    public int getProfundidad() {
        return (this.profundidad);
    }

    public void setProfundidad(int pProfundidad) {
        this.profundidad = pProfundidad;
    }

    public abstract Tablero obtenerNuevaConfiguracionTablero(Tablero tablero, short turno);
}
