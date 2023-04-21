package org.myrobotlab.service.interfaces;

import java.net.URL;
import org.myrobotlab.framework.Message;

public interface CommunicationInterface {

    public void send(final Message msg);

    public void send(final URL url, final Message msg);

    public void setComm(final Communicator comm);

    public Communicator getComm();
}
