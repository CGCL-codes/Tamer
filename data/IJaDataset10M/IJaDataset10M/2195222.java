package palindrume.bd;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe
 *
 * @author  Tonio Rush
 * @version 0.0 21/10/2007
 * @see
 */
public class Tracer {

    private static Tracer _instance;

    private FileWriter fileW;

    private static boolean _traces_on = true;

    public static void activate(boolean b) {
        _traces_on = b;
    }

    public static void t(Object appelant, String msg) {
        append(appelant.toString() + " : " + msg + "\n");
    }

    public static void t(String appelant, String msg) {
        append(appelant + " : " + msg + "\n");
    }

    public static void t(String msg) {
        append(msg + "\n");
    }

    public static void t(Object appelant, Throwable e) {
        e.printStackTrace();
        append(appelant.toString() + " : exception\n");
        append(" --> Name = [" + e.getClass().getName() + "]\n");
        append(" --> Message = [" + e.getMessage() + "]\n");
        append(" --> Stack trace :\n");
        StackTraceElement[] stack = e.getStackTrace();
        for (int i = 0; i < stack.length; ++i) {
            append("    --- at " + stack[i].getClassName() + "." + stack[i].getMethodName() + "(" + stack[i].getFileName() + ":" + stack[i].getLineNumber() + ")\n");
        }
    }

    private static void append(String ligne) {
        if (!_traces_on) return;
        System.out.print(ligne);
        if (_instance == null || _instance.fileW == null) {
            return;
        }
        try {
            _instance.fileW.write(DateFormateur.getTimeHHMMSS() + " " + ligne);
            _instance.fileW.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private Tracer() {
    }

    public static void shutdown() {
        append("Tracer shuting down...");
        if (_instance == null || _instance.fileW == null) {
            append("no file open... bye");
            return;
        }
        try {
            _instance.fileW.close();
            _instance.fileW = null;
            _instance = null;
            append("file closed...bye");
        } catch (IOException e) {
            _instance.fileW = null;
            _instance = null;
            append("impossible to close file...bye");
        }
    }

    public static void initialize(String file) {
        _instance = new Tracer();
        if (file == null) {
            _instance.fileW = null;
            System.out.println("Tracer : logging to console only...");
            return;
        }
        String fileName = file + DateFormateur.getDateForFile() + ".log";
        System.out.println("Tracer : logging to " + fileName);
        try {
            new File(fileName).createNewFile();
        } catch (IOException e1) {
            System.out.println("Tracer : cant't create " + fileName);
            e1.printStackTrace();
            return;
        }
        try {
            _instance.fileW = new FileWriter(fileName);
        } catch (IOException e) {
            System.out.println("Tracer : cannot open " + fileName);
            _instance.fileW = null;
            e.printStackTrace();
        }
        System.out.println("Tracer : on");
    }
}
