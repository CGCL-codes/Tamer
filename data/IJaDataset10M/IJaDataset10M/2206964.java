package javaframework.capadeaccesoadatos.io.streams.bytes.ficheros;

import java.io.FileOutputStream;
import java.util.Date;
import javaframework.capadeaccesoadatos.io.disco.sistemadearchivos.Fichero;
import javaframework.capadeaccesoadatos.io.streams.bytes.ClaseAbstractaStreamEscrituraBytes;
import javaframework.capadeaplicación.mensajes.rastreo.rastreador.Rastreador;

/**
 * Respresenta un stream de escritura en modo binario conectado a fichero.
 *
 * <br/><br/>
 *
 * <b><u>Notas de diseño</u></b><br/>
 *
 * Si el fichero ya existe, al realizar una operación de escritura se sobreescribe el contenido
 * del fichero, comenzando por la primera posición.
 *
 * <br/><br/>
 *
 * <b>· Fecha de creación:</b> 01/01/2007<br/>
 * <b>· Revisiones:</b> 16/12/2009, 03/05/2010<br/><br/>
 * <b><u>Estado</u></b><br/>
 * <b>· Depurado:</b> -<br/>
 * <b>· Pruebas estructurales:</b> -<br/>
 * <b>· Pruebas funcionales:</b> -<br/>
 *
 * @author Francisco Pérez R. de V. (franjfw@yahoo.es) (c) 2011
 * @version JavaFramework.0.0.1.desktop-web.es
 * @version StreamEscrituraFichero.0.0.1
 * @since JavaFramework.0.0.1.desktop-web.es
 * @see <a href=""></a>
 *
 */
public final class StreamEscrituraFichero extends ClaseAbstractaStreamEscrituraBytes {

    /**
	 * Instancia un objeto <code>StreamEscrituraFichero</code>
	 *
	 * <br/><br/>
	 *
	 * @param stream		Objeto del tipo <code>Fichero</code> que representa el fichero al cual se
	 *						conecta este objeto.
	 *
	 * @param rastreador	Objeto <code>Rastreador</code> que efectuará el seguimiento del objeto. Si
	 *						se especifica un valor <code>null</code>, no se efectúa el seguimiento.
	 */
    public StreamEscrituraFichero(final Fichero fichero, final Rastreador rastreador) {
        super(null, rastreador);
        try {
            final String RUTA_ABSOLUTA = fichero.getRutaCanónicaAbsoluta();
            final FileOutputStream FOS = new FileOutputStream(RUTA_ABSOLUTA);
            super.setStreamEscritura(FOS);
            super.registrarEstado(new Date(), this.getClass().getCanonicalName(), "StreamEscrituraFichero", null, null, fichero, rastreador);
        } catch (Exception e) {
            super.registrarEstado(new Date(), this.getClass().getCanonicalName(), "StreamEscrituraFichero", e, null, fichero, rastreador);
        }
    }
}
