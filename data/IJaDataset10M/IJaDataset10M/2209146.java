package com.volantis.mcs.cli;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Implementation of UserInterface which directs IO to the console
 */
class ConsoleUserInterface implements UserInterface {

    public void reportError(String message) {
        System.err.println(message);
        System.err.flush();
    }

    public void reportException(Throwable throwable) {
        throwable.printStackTrace(System.err);
        System.err.flush();
    }

    public void reportStatus(String message) {
        System.out.println(message);
        System.out.flush();
    }

    public String getInputLine() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return line;
    }
}
