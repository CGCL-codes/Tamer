package org.chess.quasimodo.util;

import java.io.File;

public class SystemUtils {

    public static File getUserHome() {
        return new File(System.getProperty("user.home"));
    }
}
