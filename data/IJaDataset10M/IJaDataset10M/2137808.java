package org.torbs.engine.protocol;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.torbs.util.Console;

public class ServerStatusResp implements MessageInterface {

    public int mNbMaxPlayers;

    public int mNbPlayersConnected;

    public String mRaceName;

    public List<String> mPlayers = new ArrayList<String>();

    public ServerStatusResp(int nbPlayersConnected, int nbMaxPlayers, String race) {
        mNbPlayersConnected = nbPlayersConnected;
        mNbMaxPlayers = nbMaxPlayers;
        mRaceName = race;
    }

    public ServerStatusResp(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        receive(ois);
    }

    public void addPlayer(String player) {
        mPlayers.add(player);
    }

    @Override
    public int getID() {
        return Protocol.SERVER_STATUS_RESP;
    }

    @Override
    public void receive(ObjectInputStream pOis) throws IOException, ClassNotFoundException {
        mNbMaxPlayers = pOis.readInt();
        mNbPlayersConnected = pOis.readInt();
        mRaceName = pOis.readUTF();
        for (int i = 0; i < mNbPlayersConnected; i++) {
            String player = pOis.readUTF();
            addPlayer(player);
        }
        Console.consoleTrace("RECEIVE " + toString(), Console.CTL_DEBUG);
    }

    @Override
    public void send(ObjectOutputStream pOos) throws IOException {
        pOos.writeInt(mNbMaxPlayers);
        pOos.writeInt(mNbPlayersConnected);
        pOos.writeUTF(mRaceName);
        for (String player : mPlayers) {
            pOos.writeUTF(player);
        }
        Console.consoleTrace("SEND " + toString(), Console.CTL_DEBUG);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("ServerStatusResp: ");
        result.append("NbMaxPlayers [");
        result.append(mNbMaxPlayers);
        result.append("] NbPlayersConnected [");
        result.append(mNbPlayersConnected);
        result.append("] RaceName [");
        result.append(mRaceName);
        result.append("] Players: ");
        for (String player : mPlayers) {
            result.append("[");
            result.append(player);
            result.append("]");
        }
        return result.toString();
    }
}
