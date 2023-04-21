package nz.net.juju.jaune;

import java.io.*;

/** Main application class that parses the arguments, calls the class
    compiler, and writes out the results.
*/
public class Jaune {

    private static String[][] ARGS = { { "--add-to-cp", "Append the next argument to the class path" }, { "--profile", "Add profiling code" }, { "--debug", "Add debugging traces" }, { "--no-peep", "Don't run the peep hole optimiser" }, { "-m<port>", "Set the port to compile for" } };

    private static void usage() {
        String id = "$Id: Jaune.java,v 1.1.1.1 2002/04/23 03:43:02 michaelh Exp $";
        String spaces = "                                                 ";
        System.out.println("Jaune - an ahead of time compiler for small systems");
        System.out.println(id + "\n");
        System.out.println("Usage: java " + Jaune.class.getName() + " [args...] class-files.class [...]");
        System.out.println("Options:");
        for (int i = 0; i < ARGS.length; i++) {
            String arg = ARGS[i][0];
            System.out.println("  " + arg + spaces.substring(0, 15 - arg.length()) + ARGS[i][1]);
        }
    }

    public static void main(String[] args) throws Exception {
        int ops = 0;
        boolean shortNames = false;
        String outPrefix = null;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("--add-to-cp")) {
                ClassManager.addToClasspath(args[++i]);
            } else if (arg.equals("--short-names")) {
                shortNames = true;
            } else if (arg.equals("--out-prefix")) {
                outPrefix = args[++i];
            } else if (arg.equals("--profile")) {
                Options.profile = true;
            } else if (arg.equals("--debug")) {
                Options.debug = true;
            } else if (arg.equals("--no-peep")) {
                Options.noPeep = true;
            } else if (arg.startsWith("-m")) {
                if (arg.equals("-mgbz80")) {
                    Options.forGB = true;
                } else {
                }
            } else if (arg.endsWith(".class") == false) {
                usage();
            } else {
                int idx = arg.lastIndexOf('.');
                String asmFile;
                String asmBase;
                if (shortNames) {
                    asmBase = Support.getLabel(ClassManager.get(arg)) + ".asm";
                } else {
                    asmBase = arg.substring(0, idx) + ".asm";
                }
                if (outPrefix == null) {
                    asmFile = asmBase;
                } else {
                    asmFile = outPrefix + File.separator + asmBase;
                }
                PrintStream out = new PrintStream(new FileOutputStream(asmFile));
                CompiledClass cl = ClassCompiler.compile(arg);
                cl.emit(out);
                out.close();
                ops++;
            }
        }
        if (ops == 0) {
            usage();
        }
    }
}
