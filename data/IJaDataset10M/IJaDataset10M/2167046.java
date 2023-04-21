package jaguar.machine.stack.structures.exceptions;

/**
 * representa la situacion en la que  no encontramos un QxGammaStarSet v�lido
 * 
 * @author Ivan Hern�ndez Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class QxGammaStarSetNotFoundException extends Exception {

    /**
     * Constructor sin par�metros.
     * La excepcion presenta el mensaje por omisi�n, el cual es
     * <code>none</code>
     */
    public QxGammaStarSetNotFoundException() {
        super();
    }

    /**
     * Constructor.
     * Usa el mensaje especificado como parametro del constructor de la superclase.
     *
     * @param mensaje que contiene esta excepci�n.
     */
    public QxGammaStarSetNotFoundException(String mensaje) {
        super(mensaje);
    }
}
