package com.epicsagaonline.bukkit.SolarRedstoneTorch.utility;

public class Log {

    public static void Write(String message) {
        if (message != null) {
            System.out.println("[SolarRedstoneTorch] " + message.trim());
        }
    }
}
