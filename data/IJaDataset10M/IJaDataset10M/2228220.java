package org.jostraca.util;

import java.io.PrintStream;

/** Accepts messages for display to user on command line.
 */
public class CommandLineUserMessageHandler extends RecordingUserMessageHandler {

    public static final String CN = CommandLineUserMessageHandler.class.getName();

    private static final PrintStream sSysOut = System.out;

    public void add(int pType, String pMsg) {
        super.add(pType, pMsg);
        if (null != pMsg) {
            if (pType <= iThreshold) {
                sSysOut.println(pMsg);
            }
        }
    }
}
