package krowdix.control.util;

import krowdix.interfaz.AreaTrabajo;
import krowdix.modelo.objetos.agentes.Nodo;
import org.apache.commons.collections15.Transformer;

/**
 * Transformador usado por {@link AreaTrabajo}. Dado un {@link Nodo}, devuelve
 * un objeto de tipo {@link String} que es el texto (label) que debe aparecer
 * junto al nodo en el gráfico. En esta implementación, dicho texto se
 * corresponde con el Id del Nodo, obtenido usando {@link Nodo#dameId()}.
 * 
 * @author Daniel Alonso Fernández
 */
public class VertexLabelTransformer implements Transformer<Nodo, String> {

    /**
	 * Transforma el nodo un objeto de tipo String.
	 */
    public String transform(Nodo nodo) {
        return String.valueOf(nodo.dameId());
    }
}
