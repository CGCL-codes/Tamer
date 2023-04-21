package de.flaxen.jdvdslideshow.io;

public class Logger {

    public static void print(Object obj, Object text) {
        if (text == null) {
            text = "null";
        }
        System.out.println(obj.getClass().getName() + ": " + text.toString());
    }
}
