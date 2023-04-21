package es.realtimesystems.simplemulticast;

import java.io.*;
import java.util.*;

/**
 * Interfaz que proporciona un m�todo com�n receiveCallback() para la
 * notificaci�n de la recepci�n de datos sobre el socket PTMF.
 * @version  1.0
 * @author M. Alejandro Garc�a Dom�nguez
 * <A HREF="mailto:garcia@arconet.es">(garcia@arconet.es)</A><p>
 *			   Antonio Berrocal Piris
 */
public interface ReceiveHandler {

    /**
   * El m�todo com�n receiveCallback
   * @param arg El argumento del m�todo callback
   */
    public void receiveCallback(int arg);
}
