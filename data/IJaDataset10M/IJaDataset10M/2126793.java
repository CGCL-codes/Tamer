package com.tcmj.test.jna.example.tools;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

/**
 *
 * @author tcmj
 */
public class Printf {

    public interface CLibrary extends Library {

        CLibrary INSTANCE = (CLibrary) Native.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"), CLibrary.class);

        void printf(String format, Object... args);
    }

    public static void main(String[] args) {
        CLibrary.INSTANCE.printf("Hello, World\n");
        CLibrary.INSTANCE.printf("Das ist Text, und er wird als solcher ausgegeben. \n");
        CLibrary.INSTANCE.printf("Der Wert der Variablen Zahl1 ist: %d \n", 12);
        Object[] obj = new Object[] { 'A' };
        CLibrary.INSTANCE.printf("Der Wert der Variablen Zeichen1 ist: %c \n", obj);
        CLibrary.INSTANCE.printf("Der Wert der Variablen Zeichen1 ist: %d \n", obj);
        for (int i = 0; i < args.length; i++) {
            CLibrary.INSTANCE.printf("Argument %d: %s\n", i, args[i]);
        }
    }
}
