package loro.compilacion;

import loro.arbol.*;
import loro.visitante.VisitanteException;
import loro.Rango;
import loro.IUbicable;

/**
 * Excepci�n para errores sem�nticos.
 * @author Carlos Rueda
 * @version $Id: ChequeadorException.java,v 1.3 2004/01/14 09:29:16 carueda Exp $
 */
public class ChequeadorException extends VisitanteException {

    /** Ubicacion de este error. */
    IUbicable u;

    /**
	 * Obtiene el rango asociado a este error.
	 *
	 * @return el rango asociado a este error.
	 */
    public Rango obtRango() {
        return u.obtRango();
    }

    /**
	 * Crea una excepci�n sem�ntica.
	 *
	 * @param u Ubicaci�n del texto implicado en el error.
	 * @param s Mensaje del error.
	 */
    public ChequeadorException(IUbicable u, String s) {
        super(s);
        this.u = u;
        if (u == null) printStackTrace();
    }

    /**
	 * Obtiene la ubicaci�n de este error.
	 *
	 * @return la ubicaci�n de este error.
	 */
    public IUbicable obtUbicable() {
        return u;
    }
}
