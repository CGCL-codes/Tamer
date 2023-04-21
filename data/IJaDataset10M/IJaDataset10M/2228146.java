package loro.arbol;

import loro.visitante.IVisitante;
import loro.visitante.VisitanteException;
import loro.Rango;

/**
 * Nodo de creacion de arreglo.
 * @author Carlos Rueda
 * @version 2002-03-27 creado.
 */
public class NExpresionArreglo extends NExpresion {

    /**
	 * La lista de expresiones de este arreglo.
	*/
    NExpresion[] exprs;

    public NExpresionArreglo(Rango rango, NExpresion[] exprs) {
        super(rango);
        this.exprs = exprs;
    }

    /**
	 * Obtiene la lista de expresiones.
	 */
    public NExpresion[] obtExpresiones() {
        return exprs;
    }

    /**
	 * Acepta al visitante.
	 */
    public void aceptar(IVisitante v) throws VisitanteException {
        v.visitar(this);
    }
}
