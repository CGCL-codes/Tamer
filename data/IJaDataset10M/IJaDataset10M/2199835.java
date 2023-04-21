package net.sourceforge.jasymcaandroid.jasymca;

import java.util.*;
import java.io.*;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.util.Log;

public class Jasymca implements Runnable {

    static InputStream getFileInputStream(String fname) throws IOException {
        if (fname.startsWith("m/") && fname.endsWith(".m")) {
            int l = fname.length();
            String name = fname.substring(2, l - 2);
            Resources R = context.getResources();
            int id = R.getIdentifier(name, "raw", context.getPackageName());
            if (id != 0) {
                return R.openRawResource(id);
            }
        }
        if (!fname.startsWith("/")) {
            File file = new File(context.getFilesDir(), fname);
            return new java.io.FileInputStream(file);
        }
        return new java.io.FileInputStream(fname);
    }

    public static ContextWrapper context;

    static OutputStream getFileOutputStream(String fname, boolean append) throws IOException {
        if (fname.indexOf('/') < 0) {
            int mode = Context.MODE_WORLD_WRITEABLE;
            if (append) mode |= Context.MODE_APPEND;
            return context.openFileOutput(fname, mode);
        } else if (!fname.startsWith("/")) {
            fname = context.getFileStreamPath(fname).getAbsolutePath();
        }
        return new java.io.FileOutputStream(fname, append);
    }

    public void readFile(InputStream in) throws JasymcaException {
        LambdaLOADFILE.readFile(in);
    }

    static String JasymcaRC = "Jasymca.";

    public Environment env;

    public Processor proc = null;

    public Parser pars;

    String ui = "Octave";

    PrintStream ps;

    InputStream is;

    public void interrupt() {
        if (proc != null) proc.set_interrupt(true);
    }

    public boolean isAlive() {
        return evalLoop != null && evalLoop.isAlive();
    }

    private String welcome = "Jasymca	- Java Symbolic Calculator\n" + "version 2.1\n" + "Copyright (C) 2006, 2009 - Helmut Dersch\n" + "der@hs-furtwangen.de\n\n";

    static NumFmt fmt = new NumFmtVar(10, 5);

    Thread evalLoop = null;

    public Jasymca() {
        this("Octave");
    }

    public Jasymca(String ui) {
        setup_ui(ui, true);
        welcome += "Executing in " + ui + "-Mode.\n";
        welcome += "Welcome and have fun!\n";
    }

    public void setup_ui(String ui, boolean clear_env) {
        if (clear_env) {
            env = new Environment();
        }
        if (ui != null) this.ui = ui;
        if (this.ui.equals("Maxima")) {
            proc = new XProcessor(env);
            pars = new MaximaParser(env);
        } else if (this.ui.equals("Octave")) {
            proc = new Processor(env);
            pars = new OctaveParser(env);
        } else if (this.ui.equals("OpenMath")) {
            proc = new OMProcessor(env);
            pars = new OMParser(env);
        } else if (this.ui.equals("Popcorn")) {
            proc = new OMProcessor(env);
            pars = new PopcornParser(env);
        } else {
            System.out.println("Mode " + this.ui + " not available.");
            System.exit(0);
        }
    }

    public void start(InputStream is, PrintStream ps) {
        this.is = is;
        this.ps = ps;
        try {
            String fname = JasymcaRC + ui + ".rc";
            InputStream file = getFileInputStream(fname);
            LambdaLOADFILE.readFile(file);
        } catch (Exception e) {
        }
        ps.print(welcome);
        proc.setPrintStream(ps);
        evalLoop = new Thread(this);
        evalLoop.start();
    }

    public void run() {
        while (true) {
            ps.print(pars.prompt());
            try {
                proc.set_interrupt(false);
                List<?> code = pars.compile(is, ps);
                if (code == null) {
                    ps.println("");
                    continue;
                }
                if (proc.process_list(code, false) == Processor.EXIT) {
                    ps.println("\nGoodbye.");
                    return;
                }
                proc.printStack();
            } catch (Exception e) {
                ps.println("\n" + e);
                proc.clearStack();
                Log.w("Jasymca", e);
            }
        }
    }

    /**
	 * @param welcome the welcome to set
	 */
    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    /**
	 * @return the welcome
	 */
    public String getWelcome() {
        return welcome;
    }
}
