package es.realtimesystems.simplemulticast;

import java.util.EventObject;

/**
 * La clase PTMFEventConexion es utilizada por PTMF para notificar informacion
 * relativa a la conexion Multicast
 */
public class PTMFEventConexion extends PTMFEvent {

    /**
   * Constructor PTMFEventConexion
   * @param socket Un objeto SocketPTMFImp
   * @param sInformativa cadena Informativa
   */
    public PTMFEventConexion(SocketPTMFImp socket, String sInformativa) {
        super(socket, EVENTO_CONEXION, sInformativa);
    }
}
