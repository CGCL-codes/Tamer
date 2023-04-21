package excepciones;

/**
 * Excepci&oacute;n que se lanza cuando no se admiten nuevos jugadores a un
 * juego
 * 
 * @author Agust&iacute;n Villafa&ntilde;e
 * 
 */
public class ExNoAdmiteJugadores extends ExEscoba {

    static final long serialVersionUID = 108;

    /**
	 * Genera excepci&oacute;n, mensaje:
	 * <i>"Este juego no admite nuevos jugadores"</i>
	 */
    public ExNoAdmiteJugadores() {
        super("Este juego no admite nuevos jugadores");
    }
}
