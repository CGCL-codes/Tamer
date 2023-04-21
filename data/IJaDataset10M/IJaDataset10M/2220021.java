package electrode;

import electrode.GameWindow.LayerType;
import electrode.api.ScriptEnvironment;
import electrode.gel.Gel;
import electrode.gfx.DialougeLayer;
import jargs.gnu.CmdLineParser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipException;
import org.lwjgl.LWJGLException;
import org.python.util.PythonInterpreter;

/**
 *
 * @author cgranade
 */
public class Main {

    /**
     * Name of the engine project.
     */
    public static final String PROJECT_NAME = "ElectroDE";

    /**
     * Path at which all Electrode API scripts are kept, relative to the root
     * of the main Electrode JAR.
     */
    public static final String API_PATH = "/electrode/api";

    /**
     * Version number for the engine.
     */
    public static final Version ELECTRODE_VERSION = new Version(0, 1, 0);

    /**
     * Set to true if the engine should dump extended debug information.
     */
    private static boolean debug = false;

    /**
     * Set to true if the engine should attempt to enter fullscreen mode.
     */
    private static boolean fullscreen = false;

    /**
     * Path to the currently executing game.
     */
    private static String gamePath;

    /**
     * Intepreter context for the Jython backend.
     */
    private static PythonInterpreter interp;

    /**
     * Main OpenGL window.
     */
    private static GameWindow win = null;

    /**
     * Rendering layer to manage all in-game dialouge.
     */
    private static DialougeLayer dialougeLayer = null;

    /**
     * Separate thread for game scripts.
     */
    private static Thread scriptThread;

    /**
     * Object encapsulating the currently executing gel.
     */
    private static Gel runningGel = null;

    /**
     * Dumps an exception to stderr.
     * 
     * @param e The exception to be uncerimoniously dumped.
     */
    public static void dumpException(Exception e) {
        System.err.println(e.getMessage());
        if (debug) {
            e.printStackTrace();
        }
    }

    /**
     * Prints a string to stderr if debug information is enabled.
     * @param str String to be written to stderr.
     */
    public static void debugString(String str) {
        if (debug) {
            System.err.println("[DEBUG] " + str);
        }
    }

    /**
     * Prints a string to stderr if debug information is enabled.
     * 
     * @param str Format string, as specified for {@link String.format}.
     * @param args Values to be substituted into {@code str} for formatting.
     */
    public static void debugString(String str, Object... args) {
        debugString(String.format(str, args));
    }

    /**
     * Returns the current game window, or {@code null} if the game window has
     * not yet been constructed.
     * 
     * @return The current game window.
     */
    public static GameWindow getGameWindow() {
        return win;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option versionOpt = parser.addBooleanOption("version");
        CmdLineParser.Option debugOpt = parser.addBooleanOption("debug");
        CmdLineParser.Option fullscreenOpt = parser.addBooleanOption("fullscreen");
        try {
            parser.parse(args);
        } catch (CmdLineParser.OptionException e) {
            Main.dumpException(e);
            System.exit(2);
        }
        if ((Boolean) parser.getOptionValue(versionOpt, Boolean.FALSE)) {
            System.out.println("ElectroDE version " + ELECTRODE_VERSION + ".");
            System.exit(0);
        }
        debug = (Boolean) parser.getOptionValue(debugOpt, Boolean.FALSE);
        fullscreen = (Boolean) parser.getOptionValue(fullscreenOpt, Boolean.FALSE);
        String[] otherArgs = parser.getRemainingArgs();
        if (otherArgs.length != 1) {
            System.exit(2);
        }
        gamePath = otherArgs[0];
        File gelFile = new File(gamePath);
        try {
            runningGel = new Gel(gelFile);
        } catch (ZipException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        interp = new PythonInterpreter();
        ScriptEnvironment env = new ScriptEnvironment(interp);
        try {
            interp.execfile(runningGel.getScriptStream(runningGel.getManifest().getGame().getInit()));
        } catch (IOException ex) {
            System.err.println("Main script for game not found. Quitting.");
            System.exit(2);
        }
        try {
            win = new GameWindow(runningGel.getManifest().getName(), fullscreen);
        } catch (LWJGLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(2);
        }
        dialougeLayer = new DialougeLayer();
        win.setLayer(LayerType.DIALOUGE, dialougeLayer);
        env.setAPIVar("dialouge", dialougeLayer);
        final String titleScreenPath = runningGel.getManifest().getGame().getTitleScreen();
        scriptThread = new Thread(new Runnable() {

            public void run() {
                debugString("Starting new thread: %s", Thread.currentThread().getName());
                try {
                    interp.execfile(runningGel.getScriptStream(titleScreenPath));
                } catch (IOException ex) {
                    System.err.println("Title screen script for game not found. Quitting.");
                    System.exit(2);
                }
            }
        });
        scriptThread.start();
        try {
            win.run();
        } finally {
            if (win != null) {
                win.doCleanup();
            }
        }
    }

    /**
     * Gets the input stream for an API script.
     * 
     * @param path Path to the API script to be loaded, relative to the
     *     root API script path.
     * @return {@link InputStream} object corresponding to the requested API
     *     script.
     */
    public static InputStream getAPIScriptAsStream(String path) {
        String fullPath = pathJoin(API_PATH, path);
        debugString("Loading classpath resource (api script): %s.", fullPath);
        return Main.class.getResourceAsStream(fullPath);
    }

    /**
     * Joins multiple path elements in an OS-agnostic manner.
     * 
     * @param segments Path segments to be joined.
     * @return A single path composed of the given path segments.
     */
    public static String pathJoin(String... segments) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < segments.length - 1; i++) {
            if (segments[i].endsWith(File.separator) && segments[i + 1].startsWith(File.separator)) {
                buf.append(segments[i].substring(0, segments[i].length() - File.separator.length()));
            } else {
                buf.append(segments[i]);
            }
            if (!(segments[i].endsWith(File.separator) || segments[i + 1].startsWith(File.separator))) {
                buf.append(File.separator);
            }
        }
        buf.append(segments[segments.length - 1]);
        return buf.toString();
    }

    /**
     * Fetches the currently running game's gel.
     * 
     * @return The {@link Gel} for the currently running game.
     */
    public static Gel getRunningGel() {
        return runningGel;
    }
}
