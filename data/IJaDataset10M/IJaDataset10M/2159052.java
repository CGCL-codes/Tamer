package vista;

import java.awt.Color;
import logica.entidades.NoJugador;
import logica.escenario.Fruta;
import ar.uba.fi.algo3.titiritero.SuperficieDeDibujo;

public class VistaFruta extends ar.uba.fi.algo3.titiritero.vista.Circulo {

    private static int radio = 8;

    private Fruta fruta;

    public VistaFruta(NoJugador unaFruta, EscalaYPosicion escalayPos) {
        super(radio, escalayPos, 0, 0);
        this.fruta = (Fruta) unaFruta;
        this.setPosicionable(this.fruta);
        setColor(Color.RED);
    }

    public void dibujar(SuperficieDeDibujo superficie) {
        if (this.fruta.estaVivo()) this.setColor(Color.RED); else this.setColor(Color.BLACK);
        super.dibujar(superficie);
    }
}
