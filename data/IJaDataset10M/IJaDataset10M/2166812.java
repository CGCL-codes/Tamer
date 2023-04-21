package fr.x9c.cadmium.support;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import javax.swing.JFrame;
import fr.x9c.cadmium.gui.InOutPanel;
import fr.x9c.cadmium.kernel.ByteCodeParameters;
import fr.x9c.cadmium.kernel.CadmiumException;
import fr.x9c.cadmium.kernel.Fail;
import fr.x9c.cadmium.kernel.Fatal;
import fr.x9c.cadmium.kernel.Interpreter;
import fr.x9c.cadmium.util.RandomAccessInputStream;

/**
 * This class implements a <tt>main</tt> method that looks up for the OCaml
 * toplevel asembedded and launches it, interaction with the toplevel being
 * done using a GUI.
 *
 * @author <a href="mailto:cadmium@x9c.fr">Xavier Clerc</a>
 * @version 1.1
 * @since 1.0
 */
public final class EmbeddedTopLevel {

    /**  Frame title. */
    private static final String TITLE = "OCaml toplevel";

    /**  Frame width. */
    private static final int WIDTH = 600;

    /**  Frame height. */
    private static final int HEIGHT = 400;

    /**
     * No instance of this class.
     */
    private EmbeddedTopLevel() {
    }

    /**
     * Looks up for the OCaml toplevel executable in the system path and launch
     * it, interaction with the toplevel being done using a UI.
     * @param args passed to toplevel
     */
    public static void main(final String[] args) {
        final PipedInputStream in = new PipedInputStream();
        final PipedOutputStream out = new PipedOutputStream();
        final PipedOutputStream err = new PipedOutputStream();
        InOutPanel top = null;
        try {
            final ByteCodeParameters params = new ByteCodeParameters(args, false, true, in, new PrintStream(out, true), new PrintStream(err, true), false, false, false, false, "Unix", false, "/usr/local/bin/ocaml", true, null, false, false, false, false, 64 * 1024, 64 * 1024, new String[0], true);
            final RandomAccessInputStream source = new RandomAccessInputStream(EmbeddedTopLevel.class.getResourceAsStream("/usr/local/bin/ocaml"));
            final Interpreter interp = new Interpreter(params, new File("."), source);
            top = new InOutPanel(interp, new PrintStream(new PipedOutputStream(in), true), new PipedInputStream(out), new PipedInputStream(err));
        } catch (final CadmiumException ie) {
            final Throwable t = ie.getCause();
            if (!(t instanceof Fail.Exception) && !(t instanceof Fatal.Exception)) {
                System.out.println("error: " + ie.getMessage());
            }
            System.exit(1);
        } catch (final Fatal.Exception fe) {
            System.out.println("fatal error: " + fe.getMessage());
            System.exit(1);
        } catch (final IOException ioe) {
            System.out.println("i/o error: " + ioe.getMessage());
            System.exit(1);
        } catch (final Exception e) {
            System.out.println("internal error: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        } catch (final Throwable t) {
            System.out.println("Java error: " + t.getMessage());
            t.printStackTrace(System.err);
            System.exit(1);
        }
        final JFrame frame = new JFrame(EmbeddedTopLevel.TITLE);
        frame.getContentPane().add(top, BorderLayout.CENTER);
        frame.setSize(EmbeddedTopLevel.WIDTH, EmbeddedTopLevel.HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
