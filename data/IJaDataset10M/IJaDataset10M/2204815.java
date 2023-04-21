package javaframework.capadeaccesoadatos.io.streams.texto;

import java.io.BufferedWriter;

/**
 * Miembros públicos para implementar un escritor de texto con buffer sobre un stream
 *
 * <br/><br/>
 *
 * <b><u>Notas de diseño</u></b><br/>
 * <b>· Fecha de creación:</b> 01/01/2007<br/>
 * <b>· Revisiones:</b> 16/12/2009, 03/05/2010<br/><br/>
 * <b><u>Estado</u></b><br/>
 * <b>· Depurado:</b> -<br/>
 * <b>· Pruebas estructurales:</b> -<br/>
 * <b>· Pruebas funcionales:</b> -<br/>
 *
 * @author Francisco Pérez R. de V. (franjfw@yahoo.es) (c) 2011
 * @version JavaFramework.0.0.1.desktop-web.es
 * @version InterfazEscritor.0.0.1
 * @since JavaFramework.0.0.1.desktop-web.es
 * @see <a href=””></a>
 *
 */
public interface InterfazEscritor {

    /**
	 * @return	Devuelve un objeto Java nativo del tipo <code>BufferedWriter</code> que representa
	 *			un escritor de texto con buffer. Este objeto permite escribir texto al stream.
	 */
    BufferedWriter getEscritorConBuffer();

    /**
	 * Envía al stream el array de caracteres que recibe como parámetro.
	 *
	 * @param caracteres	Array de caracteres a escribir en el stream.
	 */
    void escribir(final char[] caracteres);

    /**
	 * Envía al stream la cadena que recibe como parámetro.
	 *
	 * @param cadena	Ccadena a escribir en el stream.
	 */
    void escribir(final String cadena);

    /**
	 * Envia al stream un salto de línea
	 */
    void escribirSaltoDeLínea();

    /**
	 * Vacía el stream en el destino al que se encuentre conectado.
	 */
    void vaciarStream();
}
