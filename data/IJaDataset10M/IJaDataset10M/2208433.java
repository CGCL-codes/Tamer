package fiuba.algo3.juego.modelo;

import java.io.Serializable;
import fiuba.algo3.titiritero.ObjetoVivo;
import fiuba.algo3.titiritero.Posicionable;

public abstract class ObjetoUbicable implements ObjetoVivo, Posicionable, Serializable {

    private static final long serialVersionUID = 8478744294538118063L;

    Rectangulo rectangulo;

    Plano plano;

    public void cambiarPosicion(Punto punto) {
        rectangulo.cambiarPosicion(punto);
    }

    public Punto devolverPunto() {
        return rectangulo.devolverPuntoIzquierdoInferior();
    }

    public boolean coincidePosicionCon(Rectangulo figuraRectangulo) {
        return (this.rectangulo.coincideConPosicionDe(figuraRectangulo));
    }

    public Plano devolverPlano() {
        return this.plano;
    }

    public void determinarPlano(Plano planoDelObjeto) {
        this.plano = planoDelObjeto;
    }

    public int devolverAltura() {
        return rectangulo.altura;
    }

    public int devolverAncho() {
        return rectangulo.ancho;
    }

    public int getY() {
        return (int) (plano.devolverAltura() - devolverPunto().getY() - this.rectangulo.devolverAltura());
    }

    public int getX() {
        return (int) (devolverPunto().getX());
    }
}
