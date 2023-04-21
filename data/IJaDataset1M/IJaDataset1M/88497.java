package loro.arbol;

import loro.visitante.IVisitante;
import loro.visitante.VisitanteException;
import loro.Rango;

/**
 * Nodo para una afirmaci�n.
 *
 * @version 2002-10-06
 */
public class NAfirmacion extends NAccion {

    /**
	 * La expresi�n para la afirmaci�n. 
	 * Puede ser null si s�lo se indic� una cadena de descripci�n.
	 */
    NExpresion expresion;

    /**
	 * Cadena de descripci�n cuando no hay expresi�n 
	 */
    String docText;

    /** Segmento de texto abarcado por toda la afirmaci�n. */
    String cadena;

    /**
	 * Crea un nodo afirmacion.
	 */
    public NAfirmacion(Rango rango, String cadena, NExpresion e) {
        super(rango);
        expresion = e;
        this.cadena = cadena;
    }

    /**
	 * Crea un nodo afirmacion.
	 */
    public NAfirmacion(Rango rango, String cadena, String docText) {
        super(rango);
        this.docText = docText;
        this.cadena = cadena;
    }

    /**
	 * Retorna la descripcion literal dada por el programador.
	 * Casos:
	 *   Ha sido dada una expresi�n y �sta es de tipo NLiteralCadena. 
	 *   ha sido dado una cadena de documentaci�n.
	 *   En otros casos retorna null.
	 * La idea es mantener las comillas de esta descripcion, que no se
	 * tienen a disposicion con obtCadena().
	 */
    public String getLiteralDescription() {
        if (docText != null) {
            return docText;
        } else if (expresion instanceof NLiteralCadena) {
            return ((NLiteralCadena) expresion).obtImagen();
        } else {
            return null;
        }
    }

    /**
	 * Acepta al visitante.
	 */
    public void aceptar(IVisitante v) throws VisitanteException {
        v.visitar(this);
    }

    /**
	 * Obtiene el segmento de texto abarcado por toda la afirmaci�n.
	 */
    public String obtCadena() {
        return cadena;
    }

    /**
	 * Obtiene la expresion de esta afirmacion.
	 * Puede ser null cuando lo que se suministr� es una cadena
	 * de documentaci�n.
	 */
    public NExpresion obtExpresion() {
        return expresion;
    }
}
