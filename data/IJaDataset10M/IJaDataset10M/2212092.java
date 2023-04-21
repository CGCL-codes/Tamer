package javaframework.capadeaplicación.mensajes.swing;

import java.awt.Component;
import javaframework.capadeaplicación.mensajes.swing.InterfazMensajeSwing.OpciónSeleccionada;
import javaframework.capadeaplicación.mensajes.swing.InterfazMensajeSwing.TiposDeDiálogo;
import javaframework.capadeaplicación.mensajes.swing.InterfazMensajeSwing.TiposDeMensaje;
import javaframework.capadepresentación.imagen.Icono;

/**
 * Recoge el conjunto de miembros públicos sobre los que puede operar el programador
 * para realizar operaciones con dialogos emergentes de confirmación.
 *
 * <br/><br/>
 *
 * <b><u>Notas de diseño</u></b><br/>
 * <b>· Fecha de creación:</b> 19/02/2011, 20/02/2011<br/>
 * <b>· Revisiones:</b><br/><br/>
 * <b><u>Estado</u></b><br/>
 * <b>· Depurado:</b> -<br/>
 * <b>· Pruebas estructurales:</b> -<br/>
 * <b>· Pruebas funcionales:</b> -<br/>
 *
 * @author Francisco Pérez R. de V. (franjfw@yahoo.es) (c) 2011
 * @version JavaFramework.0.0.1.desktop-web.es
 * @version InterfazMensajeConfirmación.0.0.1
 * @since JavaFramework.0.0.1.desktop-web.es
 * @see <a href=””></a>
 *
 */
public interface InterfazMensajeConfirmación extends InterfazMensajeSwing {

    /**
	 * @return	Devuelve un objeto <code>OpciónSeleccionada</code> que representa la opción
	 *			seleccionada por el usuario en el diálogo emergente de confirmación.
	 */
    OpciónSeleccionada getOpciónSeleccionada();

    /**
	 * Este método actúa como un proxy, estableciendo los parámetros de entrada para poder invocar al
	 * método protegido sobrecargado <code>componerMensaje(...)</code>, pasándole los parámetros necesarios
	 * para construir un diálogo emergente.
	 *
	 * @param ventanaAsociada	Objeto <code>Component</code> que representa el componente padre asociado al diálogo
	 *							emergente (normalmente es un objeto JFrame) o <code>null</code> si no se quiere
	 *							especificar ninguno.
	 * @param título			Título de la ventana del diálogo emergente.
	 * @param mensaje			Texto del mensaje.
	 * @param tipoDeMensaje		Especifica el formato del mensaje.
	 * @param tipoDeDiálogo		Especifica las opciones que se mostrarán el diálogo emergente.
	 * @param icono				Icono que se mostrará en el diálogo emergente o <code>null</code> para mostrar
	 *							el icono por defecto.
	 */
    void componerMensaje(final Component ventanaAsociada, final String título, final String mensaje, final TiposDeMensaje tipoDeMensaje, final TiposDeDiálogo tipoDeDiálogo, final Icono icono);

    /**
	 * @return	Devuelve un objeto <code>OpciónSeleccionada</code> que representa la información
	 *			introducida por el usuario en el diálogo emergente. 
	 */
    @Override
    OpciónSeleccionada abrirMensaje();
}
