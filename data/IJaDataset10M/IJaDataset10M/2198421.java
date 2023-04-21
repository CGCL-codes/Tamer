package org.slasoi.gslam.templateregistry.plog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

public class Console implements Runnable {

    /**
	 * Invokes the Zen Prolog console (shell) application
	 * - using the default (in-memory) {@link zen.prolog.Module$Factory$Impl Module.Factory}.
	 */
    public static void main(String args[]) {
        try {
            new Console().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Engine engine;

    private PrintStream output = System.out;

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private static final String EXIT = "exit";

    private static final String QUIT = "quit";

    public Console() {
        this(new Engine());
    }

    public Console(Engine engine) {
        if (engine == null) throw new IllegalArgumentException("No Engine specified");
        this.engine = engine;
    }

    public void setOutput(PrintStream output) {
        if (output != null) this.output = output;
    }

    public void setReader(BufferedReader reader) {
        if (reader != null) this.reader = reader;
    }

    public void run() {
        Session session = null;
        output.print(Engine.VERSION + " (exit|quit)\n");
        while (true) {
            if (session == null) {
                output.print("\n?- ");
            }
            String s = null;
            try {
                s = reader.readLine();
                if (s.equals(EXIT) || s.equals(QUIT)) {
                    output.print("\nGoodbye:)\n");
                    return;
                } else if (s.startsWith("list:")) {
                    String $mod = s.substring(5, s.length());
                    _m_wrapper mod = engine.__GET_MODULE($mod);
                    if (mod != null) try {
                        Clause.Iterator cit = mod.listClauses();
                        Clause c = cit.next();
                        while (c != null) {
                            output.println(c.toString());
                            c = cit.next();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (session != null && s.lastIndexOf('y') > -1) {
                    if (session.solveNext()) {
                        if (!____DISPLAY_SOLUTION(session)) session = null;
                    } else {
                        output.print("(no)\n");
                        session = null;
                    }
                } else if (session != null) {
                    session = null;
                } else {
                    Clause c = engine.user.parse(s);
                    session = engine.session(c);
                    if (session.solveNext()) {
                        if (!____DISPLAY_SOLUTION(session)) session = null;
                    } else {
                        output.print("(no)\n");
                        session = null;
                    }
                }
            } catch (Error e) {
                output.print("ERROR: " + e.error().toString() + "\n");
                session = null;
            } catch (Exception e) {
                output.print("ERROR: " + e.getMessage() + "\n");
                output.print("(no)\n");
                session = null;
            }
        }
    }

    private boolean ____DISPLAY_SOLUTION(Session session) throws Error {
        List<Var> vars = session.variables();
        int count = 0;
        Collections.sort(vars);
        for (Var v : vars) {
            Term value = v.binding();
            if (value instanceof Clause) count++;
            String var = v.name;
            String val = engine.user.write(value, true, false);
            if (!var.equals(val)) output.print("   " + var + " = " + val + "\n");
        }
        if (count > 0) {
            output.print("(yes)");
            output.print(" more y/n? ");
        } else {
            output.print("(yes)\n");
        }
        return count > 0;
    }
}
