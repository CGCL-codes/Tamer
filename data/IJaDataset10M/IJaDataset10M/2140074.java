package org.torbs.engine.protocol;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** All the messages implement this interface
 */
public interface MessageInterface {

    /** Get the message ID
   * @return the message ID
   */
    public int getID();

    /** Receive a network message
   * @param ois
   * @throws IOException
   * @throws ClassNotFoundException
   */
    public void receive(ObjectInputStream ois) throws IOException, ClassNotFoundException;

    /** Send a network message
   * @param oos
   * @throws IOException
   */
    public void send(ObjectOutputStream oos) throws IOException;
}
