package servidor.util;

/**
 * Interfaz para poder acceder a las tablas o paginas iterando sobre los elementos de las mismas.
 * Se diferencia de la interfaz Iterator de Java que esta no permite remover elementos y tiene un metodo cerrar
 * para liberar recursos.
 */
public interface Iterador<E> {

    /**
	 * Este metodo debe ser llamado antes de obtener el proximo elemento.
	 * @return true si hay un proximo elemento para iterar. False sino.
	 * 
	 */
    boolean hayProximo();

    /**
	 * @return el elemento donde se encuentra posicionado el cursor en la iteracion.
	 */
    E proximo();

    /**
	 * Metodo para liberar los recursos obtenidos en la iteracion. (Ej, paginas)
	 */
    void cerrar();
}
