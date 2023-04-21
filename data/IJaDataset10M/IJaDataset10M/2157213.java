package net.sf.beezle.sushi.cli;

import net.sf.beezle.sushi.fs.World;
import net.sf.beezle.sushi.io.InputLogStream;
import net.sf.beezle.sushi.io.MultiOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Configurable replacement for System.out, System.err and System.in. 
 * TODO: name clash with java.world.Console in Java 6.
 */
public class Console {

    public static Console create(World world) {
        return new Console(world, System.out, System.err, System.in);
    }

    public static Console create(World world, final OutputStream log) {
        return new Console(world, new PrintStream(MultiOutputStream.createTeeStream(System.out, log), true), new PrintStream(MultiOutputStream.createTeeStream(System.err, log), true), new InputLogStream(System.in, log));
    }

    public final World world;

    public final PrintStream info;

    public final PrintStream verbose;

    public final PrintStream error;

    public final Scanner input;

    private final MultiOutputStream verboseSwitch;

    public Console(World world, PrintStream info, PrintStream error, InputStream in) {
        this.world = world;
        this.info = info;
        this.verboseSwitch = MultiOutputStream.createNullStream();
        this.verbose = new PrintStream(verboseSwitch);
        this.error = error;
        this.input = new Scanner(in);
    }

    public boolean getVerbose() {
        return verboseSwitch.dests().size() == 1;
    }

    public void setVerbose(boolean verbose) {
        verboseSwitch.dests().clear();
        if (verbose) {
            verboseSwitch.dests().add(info);
        }
    }

    public void pressReturn() {
        readline("Press return to continue, ctrl-C to abort.\n");
    }

    public String readline(String message) {
        return readline(message, "");
    }

    public String readline(String message, String dflt) {
        String str;
        info.print(message);
        str = input.nextLine();
        if (str.length() == 0) {
            return dflt;
        } else {
            return str;
        }
    }
}
