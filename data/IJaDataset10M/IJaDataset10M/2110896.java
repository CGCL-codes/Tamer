package javaframework.capadeaplicación.utilidades.configuración;

import java.util.HashMap;

public interface InterfazLectorDeConfiguraciónXMLConEtiquetasClave {

    /**
	 * {@inheritDoc}
	 */
    HashMap<String, String> cargarConfiguraciónXML(final String rutaYNombreFicheroXML);
}
