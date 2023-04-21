package de.oliverfrietsch.truesimplecrypt.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author of
 * User helper functions.
 */
public class UserUtils {

    public static void ensureRoot() throws SecurityException {
        String user = System.getenv("USER");
        if ((user == null) || (!user.equals("root"))) throw new SecurityException("Sorry, current user does not seem to be root.");
    }

    public static void reRunAsRoot(Class cls, String description) throws FileNotFoundException, IOException {
        String sudoBinary = null;
        boolean kdesu = false;
        try {
            sudoBinary = ProcessUtils.runAndReadAll(false, "which", "gksudo").trim();
            if (sudoBinary.length() == 0) {
                sudoBinary = ProcessUtils.runAndReadAll(false, "which", "kdesu").trim();
                kdesu = true;
            }
            if (sudoBinary.length() == 0) sudoBinary = null;
        } catch (Exception e) {
        }
        if (sudoBinary == null) throw new FileNotFoundException("Could not locate gksudo or kdesu binary.");
        String javaBinary = System.getProperties().get("java.home") + "/bin/java";
        if (!new File(javaBinary).exists()) throw new FileNotFoundException("Couldn't locate Java VM binary.");
        String className = cls.getName();
        String classPath = System.getProperty("java.class.path");
        String[] params = new String[4];
        if (kdesu) {
            params[1] = "-d";
            params[2] = "--noignorebutton";
        } else {
            params[1] = "--description";
            params[2] = description;
        }
        params[0] = sudoBinary;
        params[params.length - 1] = "'" + javaBinary + "' " + "-classpath '" + classPath + "' " + className;
        new ProcessBuilder(params).start();
    }
}
