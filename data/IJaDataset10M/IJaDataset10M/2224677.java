package de.sljngshot.network.datagrams;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import de.sljngshot.network.Protocol;

public class GameInitNack implements INetworkPacket {

    String reason;

    public GameInitNack(ObjectInputStream ois) {
        System.out.println("Game Init Nack");
        try {
            fillFromStream(ois);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GameInitNack(String reason) {
        this.reason = reason;
    }

    @Override
    public void writeToStream(ObjectOutputStream oos) throws IOException {
        oos.writeByte(Protocol.INITNACK.getId());
        oos.writeObject(reason);
    }

    @Override
    public void fillFromStream(ObjectInputStream ois) throws IOException {
        try {
            reason = (String) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
