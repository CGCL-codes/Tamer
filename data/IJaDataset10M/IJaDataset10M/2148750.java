package servidor.tabla;

import servidor.buffer.Bloque;
import servidor.buffer.BufferManager;
import servidor.tabla.impl.PaginaImpl;

/**
 * Fabrica que crea una implementacion de una Pagina.
 */
public final class FabricaPagina {

    /**
	 * Constructor privado para evitar instanciamiento.
	 */
    private FabricaPagina() {
    }

    ;

    /**
	 * Metodo para obtener una Pagina
	 * @param bufferManager el Administrador que maneja el bloque de la pagina.
	 * @param columnasTabla un arreglo ordenado con las columnas de la tabla a la que pertenece esta pagina.
	 * @param idPagina el identificador de la pagina.
	 * @param bloque el bloque con los datos de esta pagina.
	 * @return una implementacion de una Pagina o NULL si el bloque es NULL.
	 */
    public static Pagina damePagina(BufferManager bufferManager, Columna[] columnasTabla, Pagina.ID idPagina, Bloque bloque) {
        if (bloque == null) {
            return null;
        }
        return new PaginaImpl(bufferManager, columnasTabla, idPagina, bloque);
    }
}
