package filtros;

import ficherosMusicales.EnumeracionDeExtensiones;

/**
 * Filtra un directorio con ficheros cuya extension este en la enumeracion 
 * de extensiones.
 * 
 * @author Rodrigo Villamil Perez
 */
public class FiltroFicherosMusicales extends FiltroFichero {

    public FiltroFicherosMusicales() {
        super();
        for (EnumeracionDeExtensiones ext : EnumeracionDeExtensiones.values()) {
            this.aniade(ext.getDato());
        }
    }

    public String getDescription() {
        return "Ficheros musicales";
    }
}
