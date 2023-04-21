package javaframework.capadeaplicación.mensajes.rastreo.mensajero;

import javaframework.capadeaplicación.mensajes.rastreo.MensajeEstadoDeEjecución;
import javaframework.capadeaplicación.mensajes.rastreo.rastreador.InterfazRastreador.DestinoLog;

/**
 *
 * <b><u>Notas de diseño</u></b><br/>
 * <b>· Fecha de creación:</b> 21/03/2011<br/>
 * <b>· Revisiones:</b><br/><br/>
 * <b><u>Estado</u></b><br/>
 * <b>· Depurado:</b> -<br/>
 * <b>· Pruebas estructurales:</b> -<br/>
 * <b>· Pruebas funcionales:</b> -<br/>
 *
 * @author Francisco Pérez R. de V. (franjfw@yahoo.es) (c) 2011
 * @version JavaFramework.0.0.1.desktop-web.es
 * @version NombreDeClase.0.0.1
 * @since InterfazMensajero.0.0.1.desktop-web.es
 * @see <a href=””></a>
 *
 */
interface InterfazMensajero {

    /**
	 * Efectúa la entrega de un objeto <code>MensajeEstadoDeEjecución</code> en un conjunto de destinos
	 *
	 * <br/><br/>
	 *
	 * @param mensaje El objeto mensaje a entregar.
	 * @param destinosLog Array de objetos del tipo <code>DestinoLog</code> que especifican el destino de la entrega.
	 *
	 */
    void entregarMensaje(final MensajeEstadoDeEjecución mensaje, final DestinoLog[] destinosLog);
}
