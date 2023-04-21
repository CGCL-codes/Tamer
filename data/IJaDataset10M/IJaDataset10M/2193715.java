package es.usc.citius.servando.android.communications;

/**
 * Interfaz que deberán implementar los servicios que deseen atender las peticiones
 * remotas realizadas desde los dispositivos móviles.
 * @author Tomás Teijeiro campo
 */
public interface CommunicableService {

    /**
     * Obtiene el identificador del servicio.
     * @return Identificador único de este servicio.
     */
    public String getId();

    /**
     * Procesa una petición de tipo "Send" enviada por un cliente remoto.
     * @param obj Objeto enviado
     * @param client Dirección IP del cliente.
     * @return true si la entrega ha sido correcta, false en caso contrario.
     */
    public boolean processSend(Object obj, String client);

    /**
     * Procesa una petición de tipo "ISend" enviada por un cliente remoto.
     * @param obj Objeto enviado
     * @param client Dirección IP del cliente.
     */
    public void processISend(Object obj, String client);

    /**
     * Procesa una petición de tipo "SendReceive" enviada por un cliente remoto.
     * @param obj Objeto enviado
     * @param client Dirección IP del cliente.
     * @return Objeto respuesta.
     */
    public Object processSendReceive(Object obj, String client);
}
