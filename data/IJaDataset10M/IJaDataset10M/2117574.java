package com.flyox.game.militarychess.util;

public class SysUtil {

    private static long playerID = 10000;

    public static synchronized long genID() {
        return playerID++;
    }
}
