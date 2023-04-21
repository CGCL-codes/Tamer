package aplicacion.fisica.eventos;

import Deventos.DEvent;
import aplicacion.fisica.*;
import aplicacion.fisica.documentos.FicheroBD;

/**
 * Modificar la BD
 * @author anab, carlos
 */
public class DFileEvent extends DEvent {

    public static final Integer NOTIFICAR_INSERTAR_FICHERO = new Integer(1);

    public static final Integer NOTIFICAR_MODIFICACION_FICHERO = new Integer(2);

    public static final Integer NOTIFICAR_ELIMINAR_FICHERO = new Integer(3);

    public static final Integer RESPUESTA_INSERTAR_FICHERO = new Integer(4);

    public static final Integer RESPUESTA_MODIFICACION_FICHERO = new Integer(5);

    public static final Integer RESPUESTA_ELIMINAR_FICHERO = new Integer(6);

    public static final Integer SINCRONIZACION = new Integer(100);

    public static final Integer RESPUESTA_SINCRONIZACION = new Integer(200);

    public FicheroBD fichero = null;

    public DFileEvent() {
    }
}
