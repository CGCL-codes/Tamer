package uk.co.mauerkinder.ehaase.jstubgen;

import java.io.PrintStream;

public class JStubGen extends Inspect {

    /**
	 *  Prints out all methods and field of a class using reflection.
	 *
	 *  The option "-s" generates a stub file in the current directory,
	 *  i.e. a java class with the same signature but empty implementation.
	 *
	 *  Same with option "-snp", only that "private" members are not
	 *  generated.
	 *  The file should be valid java source code.
	 *
	 */
    public static void main(String[] argv) {
        main(argv, System.out);
    }

    /**
	 *
	 * @param argv
	 * @param out
	 */
    public static void main(String[] argv, PrintStream out) {
        Class clazz;
        if (argv.length < 1) {
            usage();
            return;
        }
        try {
            stubs = (argv.length > 1 && argv[0].equals("-s"));
            noprivate = (argv.length > 1 && argv[0].equals("-snp"));
            if (noprivate) stubs = true;
            System.err.println("Processing class '" + argv[argv.length - 1] + "'.");
            clazz = Class.forName(argv[argv.length - 1]);
        } catch (ClassNotFoundException ex) {
            System.err.println("Class '" + argv[argv.length - 1] + "' could not be found.");
            usage();
            return;
        }
        if (stubs) {
            generateStub(out, clazz);
        } else {
            System.out.println("Inspecting " + clazz.getName() + ":");
            if (clazz.getSuperclass() != null) System.out.println("Superclass: " + clazz.getSuperclass().getName());
            System.out.println("Implemented interfaces:");
            iterateAndPrint(clazz.getInterfaces());
            System.out.println("--- Fields --------------------------------");
            iterateAndPrint(clazz.getDeclaredFields());
            System.out.println("--- Constructors --------------------------");
            iterateAndPrint(clazz.getDeclaredConstructors());
            System.out.println("--- Methods -------------------------------");
            iterateAndPrint(clazz.getDeclaredMethods());
        }
    }

    /** print usage */
    static void usage() {
        System.err.println("java Inspect [-s] <classname>");
        System.err.println("             where");
        System.err.println("             -s      generate stub class");
        System.err.println("             -snp    generate stub class, don't include 'package private' and 'private' members");
    }

    /** Generate stubs, but no private members */
    static boolean noprivate;

    /** generate stubs */
    static boolean stubs;
}
