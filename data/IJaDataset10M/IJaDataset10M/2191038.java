package acciones;

import jade.content.AgentAction;
import conceptos.Distrito;
import conceptos.Jugador;

public class DestruirDistrito implements AgentAction {

    private Jugador jugador;

    private Distrito distrito;

    private Integer pago;

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Distrito getDistrito() {
        return distrito;
    }

    public void setDistrito(Distrito distrito) {
        this.distrito = distrito;
    }

    public Integer getPago() {
        return pago;
    }

    public void setPago(Integer pago) {
        this.pago = pago;
    }
}
