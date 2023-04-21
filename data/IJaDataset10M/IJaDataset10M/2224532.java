package acciones;

import jade.content.AgentAction;
import conceptos.Jugador;

public class CartasJugadores implements AgentAction {

    private Jugador jugador1;

    private Jugador jugador2;

    private Jugador jugador3;

    public void setJugador(Jugador jugador, int num) {
        if (num == 1) this.jugador1 = jugador;
        if (num == 2) this.jugador2 = jugador;
        if (num == 3) this.jugador3 = jugador;
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public void setJugador1(Jugador jugador1) {
        this.jugador1 = jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    public void setJugador2(Jugador jugador2) {
        this.jugador2 = jugador2;
    }

    public Jugador getJugador3() {
        return jugador3;
    }

    public void setJugador3(Jugador jugador3) {
        this.jugador3 = jugador3;
    }
}
