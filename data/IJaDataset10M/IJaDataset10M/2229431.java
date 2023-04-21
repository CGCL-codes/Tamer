package com.esotericsoftware.kryonet.examples.chatrmi;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;

public class Network {

    public static final int port = 54777;

    public static final short PLAYER = 1;

    public static final short CHAT_FRAME = 2;

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        ObjectSpace.registerClasses(kryo);
        kryo.register(IPlayer.class);
        kryo.register(IChatFrame.class);
        kryo.register(String[].class);
    }
}
