package io;

import world.World;
import java.io.*;

public class DestructableReader {

    public static void readDestructable(World w, DataInputStream dis) throws IOException {
        int slength = dis.readInt();
        String name = new String();
        for (int i = slength - 1; i >= 0; i--) {
            name += dis.readChar();
        }
        double x = dis.readDouble();
        double y = dis.readDouble();
        int life = dis.readInt();
    }
}
