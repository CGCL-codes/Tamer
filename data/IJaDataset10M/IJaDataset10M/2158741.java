package debug.javaframework.capadeaplicación.mensajes.rastreo.mensajero;

import java.util.Date;
import javaframework.capadeaplicación.mensajes.rastreo.MensajeEstadoDeEjecución;
import javaframework.capadeaplicación.mensajes.rastreo.mensajero.Mensajero;
import javaframework.capadeaplicación.mensajes.rastreo.rastreador.InterfazRastreador.DestinoLog;

/**
 *
 * @author curro
 */
public class Test_Mensajero {

    /**
	 *
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            final MensajeEstadoDeEjecución MEE = new MensajeEstadoDeEjecución();
            final Date MARCA_DE_TIEMPO = new java.util.Date();
            final String NOMBRE_CLASE = "Test_MensajeEstadoDeEjecución";
            final String NOMBRE_MÉTODO = "main";
            final Exception E = null;
            final String TRAZA = "Esto es una prueba";
            final String[] PARAMS = new String[2];
            PARAMS[0] = "hola";
            PARAMS[1] = "mundo";
            final DestinoLog[] DESTINOS = new DestinoLog[2];
            DESTINOS[0] = DestinoLog.POPUP;
            DESTINOS[1] = DestinoLog.POPUP;
            MEE.componerMensaje(MARCA_DE_TIEMPO, NOMBRE_CLASE, NOMBRE_MÉTODO, E, TRAZA, (Object) PARAMS);
            final Mensajero M = new Mensajero(null);
            M.entregarMensaje(MEE, DESTINOS);
            M.liberarRecursos();
        } catch (Exception e) {
        }
    }
}
