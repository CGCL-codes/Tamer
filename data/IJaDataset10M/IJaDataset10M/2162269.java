package javaframework.capadeaccesoadatos.io.streams.bytes;

/**
 *  Miembros públicos para realizar operaciones de lectura sobre streams a nivel de bytes.
 *
 * <br/><br/>
 *
 * <b><u>Notas de diseño</u></b><br/>
 * <b>· Fecha de creación:</b> 01/01/2007<br/>
 * <b>· Revisiones:</b><br/><br/>
 * <b><u>Estado</u></b><br/>
 * <b>· Depurado:</b> -<br/>
 * <b>· Pruebas estructurales:</b> -<br/>
 * <b>· Pruebas funcionales:</b> -<br/>
 *
 * @author Francisco Pérez R. de V. (franjfw@yahoo.es) (c) 2011
 * @version JavaFramework.0.0.1.desktop-web.es
 * @version InterfazStreamLecturaBytes.0.0.1
 * @since JavaFramework.0.0.1.desktop-web.es
 * @see <a href=””></a>
 *
 */
public interface InterfazStreamLecturaBytes {

    /**
	 * Devuelve un array de bytes con los datos disponibles que se han podido leer.
	 * Se intentará leer tantos bytes como indica el parámetro <code>bytesALeer</code>. Si hay menos
	 * bytes para leer, se devuelve el array con los bytes que se han podido leer y el resto
	 * de posiciones quedan seteadas a cero.
	 *
	 * Cada vez que se realiza una lectura, se setea el atributo <code>bytesLeídosEnÚltimaLectura</code>.
	 *
	 * Cuando se realiza una lectura que sobrepasa el final del stream, el valor de
	 * <code>bytesLeídosEnÚltimaLectura</code> es de <code>-1</code>
	 *
	 * <br/><br/>
	 * Leer un stream hasta el final: <br/>
	 *
	 * si en un stream hay <code>n</code> bytes y se realiza una lectura con <code>bytesALeer = n</code>:
	 *
	 * 1)	Se devuelve un array de n bytes. <br/>
	 * 2)	<code>bytesLeídosEnÚltimaLectura</code> vale <code>0</code> (no -1).<br/>
	 * 3)	para obtener el valor <code>bytesLeídosEnÚltimaLectura = -1</code> es necesario realizar
	 * una nueva lectura en el stream de al menos 1 byte (es decir, hay que leer más allá del final del stream
	 * para obtener un valor -1.<br/>
	 *
	 * El método <code>read</code> de la clase nativa <code>InputStream</code> bloquea hasta que haya datos disponibles.
	 * Para evitar el bloqueo puede comprobarse antes de invocar a este método si
	 * hay datos disponibles. De hecho, para optimizar el uso de la memoria debe invocarse a
	 * este método con el argumento <code>bytesALeer = this.getDatosDisponibles()</code>. El valor de
	 * <code>bytesALeer</code> nunca puede superar el valor máximo definido en la constante
	 * <code>BUFFER_ARCHIVO_GRANDE</code> de la InterfazStreamLectura. Si esto ocurre, se lanza una excepción.<br/>
	 *
	 * Si se especifica un valor <code>númeroBytesALeer</code> superior a los datos que quedan por leer
	 * en el stream, se creará un array cuyas primeras posiciones ocuparán los bytes que quedan
	 * en el stream y las restantes posiciones quedarán con el valor 0 (valor de inicialización
	 * por defecto del array). El valor de la constante <code>NÚMERO_BYTES_LEÍDOS</code> (y el de
	 * <code>getBytesLeídosEnÚltimaLectura()</code>) refleja el número real de bytes leídos (bytes que no están
	 * a cero en el array de bytes leídos). Este valor puede utilizarse para leer los valores
	 * no-cero del array que devuelve este método.<br/>
	 *
	 * @param númeroBytesALeer	número de bytes que serán leídos en la operación de lectura.
	 * @return	Devuelve un array con los bytes leídos del stream. Si el array tiene más posiciones que
	 *			bytes leídos, las posiciones no leídas se devuelven con el valor cero.
	 */
    byte[] leer(final int bytesALeer);
}
