package org.perfectday.communication.model.plugcommunication;

import org.perfectday.message.model.Message;

/**
 *
 * @author Lobo <inmortalland83@gmail.com>
 */
public abstract class PlugCommunication {

    public abstract void exposeService();

    /**
     *  Este método sirve para conectarse al servidor de la partida 
     * @param destiny: it´s the name of the queue or topic destiny
     * @param typeDestiny: it´s the type of the Destiny: Topic or Quee
     */
    public abstract void connect(String destiny, String typeDestiny);

    public abstract void disconnect();

    public abstract void sendMessage(Object message);

    public abstract void startUp();

    public abstract Message receiveMessage();
}
