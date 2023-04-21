package tesis.extensiones;

public class Transicion {

    State estadoDesde;

    State estadoHacia;

    Evento evento;

    public Transicion(State estD, State estH, Evento evt) {
        estadoDesde = estD;
        estadoHacia = estH;
        evento = evt;
    }

    /**
	 * @return the estadoDesde
	 */
    public State estadoDesde() {
        return estadoDesde;
    }

    /**
	 * @return the estadoHacia
	 */
    public State estadoHacia() {
        return estadoHacia;
    }

    /**
	 * @return the evento
	 */
    public Evento evento() {
        return evento;
    }

    @Override
    public String toString() {
        return "Trns: " + estadoDesde + " " + estadoHacia + " " + evento;
    }
}
