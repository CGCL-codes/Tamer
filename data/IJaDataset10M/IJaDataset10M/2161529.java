package ca.evanjones;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.sun.tools.attach.VirtualMachine;

/** Attaches to a running JVM and starts profiling. */
public class JSampAttach {

    private static final String CLASSPATH_PROPERTY = "java.class.path";

    /** Splits the classpath into Files. */
    private static File[] classPathParts() {
        String classpath = System.getProperty(CLASSPATH_PROPERTY);
        String separator = System.getProperty("path.separator");
        String[] parts = classpath.split(separator);
        File[] files = new File[parts.length];
        for (int i = 0; i < parts.length; ++i) {
            files[i] = new File(parts[i]);
        }
        return files;
    }

    private static final String JAR_NAME = "jsamp.jar";

    public static void perrorQuit(String message, Exception e) {
        System.err.println("Error: " + message + ": " + e.getMessage());
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length != 4) {
            System.err.println("Usage: JSampAttach <JVM pid> <interval> <port> <output file>");
            System.exit(1);
        }
        File jarPath = null;
        for (File path : classPathParts()) {
            if (path.isDirectory()) {
                File jar = new File(path, JAR_NAME);
                if (jar.canRead()) {
                    jarPath = jar;
                    break;
                }
            } else if (path.getName().equals(JAR_NAME) && path.canRead()) {
                jarPath = path;
                break;
            }
        }
        if (jarPath == null) {
            System.err.println("Error: Could not find " + JAR_NAME + " by searching the classpath");
            System.err.println("CLASSPATH = " + System.getProperty(CLASSPATH_PROPERTY));
            System.err.println("Searched:");
            for (File path : classPathParts()) {
                System.err.println("\t" + path.getAbsolutePath());
            }
            System.exit(1);
        }
        String pid = args[0];
        String interval = args[1];
        String port = args[2];
        String output = args[3];
        try {
            FileOutputStream out = new FileOutputStream(output);
            out.close();
            File outFile = new File(output);
            outFile.delete();
            output = outFile.getAbsolutePath();
        } catch (java.io.FileNotFoundException e) {
            perrorQuit("Cannot write to output file", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        VirtualMachine vm;
        try {
            vm = VirtualMachine.attach(pid);
        } catch (com.sun.tools.attach.AttachNotSupportedException e) {
            perrorQuit("Attach to pid " + pid + " failed", e);
            throw new RuntimeException("should not get here");
        } catch (IOException e) {
            perrorQuit("Attach to pid " + pid + " failed", e);
            throw new RuntimeException("should not get here");
        }
        try {
            vm.loadAgent(jarPath.getAbsolutePath(), interval + ";" + port + ";" + output);
        } catch (com.sun.tools.attach.AgentLoadException e) {
            throw new RuntimeException(e);
        } catch (com.sun.tools.attach.AgentInitializationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
