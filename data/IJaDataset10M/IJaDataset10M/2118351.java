package it.chesslab.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**  */
public abstract class KeyboardLib {

    /**  */
    public static final String prompt(String prompt) {
        System.out.print(prompt);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String line = reader.readLine();
            if (line == null) {
                line = StringLib.EMPTY_STRING;
            }
            return line;
        } catch (IOException e) {
            return StringLib.EMPTY_STRING;
        }
    }

    /**  */
    public static final String prompt() {
        return KeyboardLib.prompt(StringLib.EMPTY_STRING);
    }
}
