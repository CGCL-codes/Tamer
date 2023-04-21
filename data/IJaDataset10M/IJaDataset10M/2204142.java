package net.sf.swarmnet.common.message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;

public interface IMessage {

    MessageType getType();

    Properties getProperties();

    void write(ObjectOutputStream stream) throws IOException;

    void read(ObjectInputStream stream) throws IOException;
}
