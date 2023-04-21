package jaguar.grammar.structures.exceptions;

/**
  * representa la situacion en la que  
 * 
 * @author Ivan Hern�ndez Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class ProductionSetNotFoundException extends Exception {

    /**
     * Constructor sin par�metros.
     * La excepcion presenta el mensaje por omisi�n, el cual es
     * <code></code>
     */
    public ProductionSetNotFoundException() {
        super();
    }

    /**
     * Constructor.
     * Usa el mensaje especificado como parametro del constructor de la superclase.
     *
     * @param mensaje que contiene esta excepci�n.
     */
    public ProductionSetNotFoundException(String mensaje) {
        super(mensaje);
    }
}
