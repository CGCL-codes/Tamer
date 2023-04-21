package servidor.catalog;

import servidor.tabla.Columna;
import servidor.tabla.Tabla;

/**
 * Interfaz con los metodos del catalogo.
 * Las tablas no tienen claves.
 * Las tablas no soportan nulls.
 */
public interface Catalogo {

    /**
     * Nombre de la tabla donde se guardan los nombres de las tablas de usuario.
     */
    String NOMBRE_TABLA_DE_TABLAS = "sys_table";

    /**
     * Nombre de la tabla donde se guardan los datos de las columnas de cada tabla de usuario.
     */
    String NOMBRE_TABLA_DE_COLUMNAS = "sys_column";

    /**
     * Nombre de la tabla que contiene la cantidad de paginas de cada tabla del usuario.
     */
    String NOMBRE_TABLA_DE_PAGINAS = "sys_page";

    /**
     * La longitud maxima que puede tener el nombre de una tabla.
     */
    int LONGITUD_CAMPO_NOMBRE_TABLA = 32;

    /**
     * La longitud maxima que puede tener el nombre de una columna.
     */
    int LONGITUD_CAMPO_NOMBRE_COLUMNA = 32;

    /**
     * La longitud maxima que puede tener el nombre de un tipo.
     */
    int LONGITUD_CAMPO_TIPO_COLUMNA = 32;

    /**
	 * Longitud en bytes de un entero.
	 */
    int LONGITUD_INT = Integer.SIZE / Byte.SIZE;

    /**
	 * Longitud en bytes de un entero largo.
	 */
    int LONGITUD_LONG = Long.SIZE / Byte.SIZE;

    /**
     * Metodo que obtiene una tabla a partir del nombre de la misma.
     * @param nombreTabla el nombre de la tabla deseada.
     * @return una implementacion de la tabla, o NULL si no existe ninguna tabla con ese nombre.
     */
    Tabla dameTabla(String nombreTabla);

    /**
     * Metodo para saber si existe una tabla con un nombre determinado.
     * @param nombreTabla el nombre de la tabla que se desea conocer su existencia.
     * @return true si existe una tabla con dicho nombre en el catalogo.
     */
    boolean existeTabla(String nombreTabla);

    /**
     * Metodo para saber si el nombre de una tabla corresponde con una de las tablas reservadas del sistema.
     * @param nombreTabla el nombre de la tabla que se desea conocer si es del sistema.
     * @return true si existe una tabla del sistema con dicho nombre.
     */
    boolean tablaDelSistema(String nombreTabla);

    /**
     * Metodo para borrar una tabla por su nombre.
     * @param nombreTabla el nombre de la tabla a borrar.
     * @throws RuntimeException si no existe ninguna tabla con ese nombre.
     */
    void borrarTabla(String nombreTabla);

    /**
     * Crea una tabla con un nombre y columnas especificado.
     * @param nombreTabla el nombre de la tabla a crear.
     * @param columnas las columnas de la tabla.
     * @throws RuntimeException si ya existe una tabla en el catalogo con ese nombre.
     * @see Columna
     */
    void crearTabla(String nombreTabla, Columna... columnas);

    /**
     * Obtiene los datos de las columnas de una determinada tabla.
     * @param nombreTabla el nombre de la tabla que se desean obtener las columnas.
     * @return un arreglo con las columnas de la tabla.
     * @throws RuntimeException si no existe ninguna tabla con ese nombre.
     * @see Columna
     */
    Columna[] columnasDeTabla(String nombreTabla);

    /**
     * Obtiene los datos de una columna de una determinada tabla.
     * @param nombreTabla el nombre de la tabla a la que pertenece la columna.
     * @param posicion la posicion de la columna dentro de la tabla.
     * @return una implementacion con los datos de la columna deseada.
     * @throws RuntimeException si no existe ninguna tabla con ese nombre.
     * @throws RuntimeException si no existe la columna dentro de la tabla.
     * @see Columna
     */
    Columna dameColumnaEnPosicion(String nombreTabla, int posicion);

    /**
	 * Actualiza la tabla de paginas con la cantidad de paginas de una tabla del usuario.
	 * @param nombreTabla la tabla de usuario a actualizar en el catalogo.
	 * @param nroPagina la cantidad de paginas a establecer
	 * @throws RuntimeException si no existe ninguna tabla con ese nombre.
	 */
    void actualizarTablaPaginas(String nombreTabla, int nroPagina);
}
