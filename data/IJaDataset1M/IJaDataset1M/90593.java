package specs;

import java.util.Random;
import java.util.TimeZone;
import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.media.Manager;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.util.Info;

public class Tester extends Thread {

    public static final String CAT_SCREEN = "Screen";

    public static final String CAT_SYSTEM = "System Info";

    public static final String CAT_API = "API";

    public static final String CAT_KEYS = "Keys";

    public static final String CAT_MMEDIA = "Multimedia";

    public static final String CAT_BENCHMARK = "Benchmark";

    public static Vector tests = new Vector();

    private long iResolution = 0;

    private PrecisionThread tSleeper;

    private Canvas canvas;

    private Canvas canvasFull;

    private Display d;

    private static boolean finished = false;

    private static final int NUMBER_OF_OPS = 10000000;

    private final int arrayA[];

    private final int arrayB[];

    private static int staticA;

    private static int staticB;

    private int instanceA;

    private int instanceB;

    public int result;

    private final Random random = new Random();

    public Tester() {
        do {
            instanceA = random.nextInt();
        } while (instanceA == 0);
        do {
            instanceB = random.nextInt();
        } while (instanceB == 0);
        do {
            Tester.staticA = random.nextInt();
        } while (Tester.staticA == 0);
        do {
            Tester.staticB = random.nextInt();
        } while (Tester.staticB == 0);
        arrayA = new int[100];
        arrayB = new int[100];
        final Random r = new Random();
        for (int i = 0; i < 100; i++) {
            do {
                arrayA[i] = r.nextInt();
            } while (arrayA[i] == 0);
            do {
                arrayB[i] = r.nextInt();
            } while (arrayB[i] == 0);
        }
    }

    public void run() {
        tSleeper = new PrecisionThread();
        tSleeper.start();
        canvas = new TestCanvas(false);
        canvasFull = new TestCanvas(true);
        try {
            tSleeper.join();
        } catch (final InterruptedException ie) {
        }
        iResolution = tSleeper.iAfter - tSleeper.iBefore;
        final TimeZone tz = TimeZone.getDefault();
        d = Application.display;
        final String[] timeZoneIDs = java.util.TimeZone.getAvailableIDs();
        final StringBuffer timeZonesBuffer = new StringBuffer();
        for (int i = 0; i < timeZoneIDs.length; i++) {
            if (i > 0) {
                timeZonesBuffer.append(", ");
            }
            timeZonesBuffer.append(timeZoneIDs[i]);
        }
        final Font font1 = Font.getDefaultFont();
        final Font font2 = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        final Font font3 = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        final Font font4 = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE);
        final Font font5 = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
        final Font font6 = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
        final Font font7 = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
        Runtime.getRuntime().gc();
        add(Tester.CAT_SCREEN, "Screen (normal)", canvas.getWidth() + "x" + canvas.getHeight());
        add(Tester.CAT_SCREEN, "Screen (full)", canvasFull.getWidth() + "x" + canvasFull.getHeight());
        add(Tester.CAT_SCREEN, "Colors", d.numColors() + (d.isColor() ? "" : "grays"));
        add(Tester.CAT_SCREEN, "Alpha Levels", Integer.toString(d.numAlphaLevels()));
        add(Tester.CAT_SCREEN, "Screen buffered", (canvas.isDoubleBuffered() ? "yes" : "no"));
        add(Tester.CAT_SCREEN, "Has pointer events", (canvas.hasPointerEvents() ? "yes" : "no"));
        add(Tester.CAT_SCREEN, "Has motion events", (canvas.hasPointerMotionEvents() ? "yes" : "no"));
        add(Tester.CAT_SCREEN, "Has key-held events", (canvas.hasRepeatEvents() ? "yes" : "no"));
        add(Tester.CAT_SCREEN, "Default-Font-Height", Integer.toString(font1.getHeight()));
        add(Tester.CAT_SCREEN, "Small-Font-Height", Integer.toString(font2.getHeight()));
        add(Tester.CAT_SCREEN, "Medium-Font-Height", Integer.toString(font3.getHeight()));
        add(Tester.CAT_SCREEN, "Large-Font-Height", Integer.toString(font4.getHeight()));
        add(Tester.CAT_SCREEN, "Small-Font-Height (bold)", Integer.toString(font5.getHeight()));
        add(Tester.CAT_SCREEN, "Medium-Font-Height (bold)", Integer.toString(font6.getHeight()));
        add(Tester.CAT_SCREEN, "Large-Font-Height (bold)", Integer.toString(font7.getHeight()));
        add(Tester.CAT_SYSTEM, "JTWI (JSR-185)", (System.getProperty("microedition.jtwi.version") == null ? "no" : "yes (ver. " + System.getProperty("microedition.jtwi.version") + ")"));
        add(Tester.CAT_SYSTEM, "Total mem", Long.toString(Runtime.getRuntime().totalMemory()));
        add(Tester.CAT_SYSTEM, "Free mem", Long.toString(Runtime.getRuntime().freeMemory()));
        add(Tester.CAT_SYSTEM, "Configuration", readProperty("microedition.configuration"));
        add(Tester.CAT_SYSTEM, "Profiles", readProperty("microedition.profiles"));
        add(Tester.CAT_SYSTEM, "Locale", readProperty("microedition.locale"));
        add(Tester.CAT_SYSTEM, "Platform", readProperty("microedition.platform"));
        add(Tester.CAT_SYSTEM, "Char encoding", readProperty("microedition.encoding"));
        add(Tester.CAT_SYSTEM, "Comm Ports", readProperty("microedition.commports"));
        add(Tester.CAT_SYSTEM, "Default Time Zone", tz.getID());
        add(Tester.CAT_SYSTEM, "Available Time Zone", timeZonesBuffer.toString());
        add(Tester.CAT_API, "JSR-135 MMAPI - multimedia", (isClass("javax.microedition.media.Manager") ? "yes, " + readProperty("microedition.media.version") : "no"));
        final boolean wmapi2 = isClass("javax.wireless.messaging.MessagePart");
        if (wmapi2) {
            add(Tester.CAT_API, "JSR-205 WMAPI - messaging", "yes, 2.0");
        } else {
            add(Tester.CAT_API, "JSR-120 WMAPI - messaging", (isClass("javax.wireless.messaging.Message") ? "yes, 1.1" : "no"));
        }
        add(Tester.CAT_API, "JSR-082 bluetooth", (isClass("javax.bluetooth.LocalDevice") ? "yes" : "no"));
        add(Tester.CAT_API, "JSR-082 bluetooth-obex", (isClass("javax.obex.HeaderSet") ? "yes" : "no"));
        add(Tester.CAT_API, "JSR-184 M3G - 3D graphics", (isClass("javax.microedition.m3g.Node") ? "yes" : "no"));
        add(Tester.CAT_API, "JSR-118 MIDP2", (isClass("javax.microedition.io.HttpsConnection") ? "yes" : "no"));
        add(Tester.CAT_API, "JSR-135 video", (isClass("javax.microedition.media.TimeBase") ? "yes" : "no"));
        add(Tester.CAT_API, "JSR-172 web services", (isClass("javax.xml.parsers.SAXParser") ? "yes" : "no"));
        add(Tester.CAT_API, "JSR-177 security services", (isClass("java.security.Signature") ? "yes" : "no"));
        add(Tester.CAT_API, "JSR-179 location", (isClass("javax.microedition.location.Location") ? "yes" : "no"));
        add(Tester.CAT_API, "JSR-180 SIP", (isClass("javax.microedition.sip.SipConnection") ? "yes" : "no"));
        add(Tester.CAT_API, "API - PIM", (isClass("javax.microedition.pim.PIM") ? "yes" : "no"));
        add(Tester.CAT_API, "API - FileSystem", (isClass("javax.microedition.io.file.FileSystemRegistry") ? "yes" : "no"));
        add(Tester.CAT_API, "Nokia - UI", (isClass("com.nokia.mid.ui.DeviceControl") ? "yes" : "no"));
        add(Tester.CAT_API, "Nokia - sound", (isClass("com.nokia.mid.sound.Sound") ? "yes" : "no"));
        add(Tester.CAT_API, "Nokia - graphics", (isClass("com.nokia.mid.ui.FullCanvas") ? "yes" : "no"));
        add(Tester.CAT_API, "Siemens - UI", (isClass("com.siemens.mp.MIDlet") ? "yes" : "no"));
        add(Tester.CAT_API, "Siemens - graphics", (isClass("com.siemens.mp.color_game.GameCanvas") ? "yes" : "no"));
        add(Tester.CAT_KEYS, "GAME_A", canvas.getKeyName(canvas.getKeyCode(Canvas.GAME_A)));
        add(Tester.CAT_KEYS, "GAME_B", canvas.getKeyName(canvas.getKeyCode(Canvas.GAME_B)));
        add(Tester.CAT_KEYS, "GAME_C", canvas.getKeyName(canvas.getKeyCode(Canvas.GAME_C)));
        add(Tester.CAT_KEYS, "GAME_D", canvas.getKeyName(canvas.getKeyCode(Canvas.GAME_D)));
        add(Tester.CAT_KEYS, "UP", canvas.getKeyName(canvas.getKeyCode(Canvas.UP)));
        add(Tester.CAT_KEYS, "DOWN", canvas.getKeyName(canvas.getKeyCode(Canvas.DOWN)));
        add(Tester.CAT_KEYS, "FIRE", canvas.getKeyName(canvas.getKeyCode(Canvas.FIRE)));
        add(Tester.CAT_KEYS, "LEFT", canvas.getKeyName(canvas.getKeyCode(Canvas.LEFT)));
        add(Tester.CAT_KEYS, "RIGTH", canvas.getKeyName(canvas.getKeyCode(Canvas.RIGHT)));
        add(Tester.CAT_MMEDIA, "MMAPI - multimedia", (isClass("javax.microedition.media.Manager") ? "yes, " + readProperty("microedition.media.version") : "no"));
        add(Tester.CAT_MMEDIA, "Video capture", readProperty("supports.video.capture"));
        add(Tester.CAT_MMEDIA, "Video encodings", readProperty("video.encodings"));
        add(Tester.CAT_MMEDIA, "Video snapshot encodings", readProperty("video.snapshot.encodings"));
        add(Tester.CAT_MMEDIA, "Audio capture", readProperty("supports.audio.capture"));
        add(Tester.CAT_MMEDIA, "Audio encoding", readProperty("audio.encoding"));
        add(Tester.CAT_MMEDIA, "Supports recording", readProperty("supports.recording"));
        add(Tester.CAT_MMEDIA, "Supports mixing", readProperty("supports.mixing"));
        add(Tester.CAT_MMEDIA, "Streamable contents", readProperty("streamable.contents"));
        final String[] supportedProtocols = getSupportedProtocols(null);
        if (supportedProtocols != null) {
            for (int i = 0; i < supportedProtocols.length; i++) {
                final String protocol = supportedProtocols[i];
                final String[] supportedContentTypes = getSupportedContentTypes(protocol);
                final StringBuffer buffer = new StringBuffer();
                for (int j = 0; j < supportedContentTypes.length; j++) {
                    if (j > 0) {
                        buffer.append(", ");
                    }
                    buffer.append(supportedContentTypes[j]);
                }
                add(Tester.CAT_MMEDIA, "Protocol " + protocol, buffer.toString());
            }
        }
        add(Tester.CAT_BENCHMARK, "Timer res. est.", iResolution + "ms");
        performAdditionBenchmark();
        performDivisionBenchmark();
        performMultiplicationBenchmark();
        Tester.finished = true;
    }

    public void add(final String category, final String name, final String value) {
        Tester.tests.addElement(new Info(category, name, value));
    }

    /**
   * Checks to see if a given class/interface exists in this Java
   * implementation.
   * @param sName the full name of the class
   * @return true if the class/interface exists
   */
    private boolean isClass(final String sName) {
        boolean fFound = false;
        try {
            if (sName != null) {
                Class.forName(sName);
                fFound = true;
            }
        } catch (final ClassNotFoundException cnfe) {
        }
        return fFound;
    }

    /**
   * Retrieves the system property, and returns it, or "unknown" if it is null.
   * @param sName the name of the system property, eg. for System.getProperty
   * @return the contents of the property, never null
   */
    private String readProperty(final String sName) {
        final String sValue = System.getProperty(sName);
        if (sValue == null) {
            return "unknown";
        }
        return sValue;
    }

    public String[] getSupportedProtocols(final String contentType) {
        return Manager.getSupportedProtocols(contentType);
    }

    public String[] getSupportedContentTypes(final String protocol) {
        return Manager.getSupportedContentTypes(protocol);
    }

    public static void export(final Form list, final String category) {
        if (!Tester.finished) {
            list.append("... still working ...\n");
        }
        for (int i = 0; i < Tester.tests.size(); i++) {
            final Info inf = (Info) Tester.tests.elementAt(i);
            if (inf.category == category) {
                list.append(inf.toString() + "\n");
            }
        }
    }

    private void performAdditionBenchmark() {
        long before;
        long after;
        before = System.currentTimeMillis();
        int result = 0;
        for (int i = 0; i < Tester.NUMBER_OF_OPS / 100; i++) {
            for (int j = 0; j < 100; j++) {
                result = arrayA[j] + arrayB[j];
            }
        }
        after = System.currentTimeMillis();
        final long elapsedArray = after - before;
        final int localA = random.nextInt();
        final int localB = random.nextInt();
        before = System.currentTimeMillis();
        for (int i = 0; i < Tester.NUMBER_OF_OPS; i++) {
            result = localA + localB;
        }
        after = System.currentTimeMillis();
        final long elapsedLocal = after - before;
        before = System.currentTimeMillis();
        for (int i = 0; i < Tester.NUMBER_OF_OPS; i++) {
            result = instanceA + instanceB;
        }
        after = System.currentTimeMillis();
        final long elapsedInstance = after - before;
        before = System.currentTimeMillis();
        for (int i = 0; i < Tester.NUMBER_OF_OPS; i++) {
            result = Tester.staticA + Tester.staticB;
        }
        after = System.currentTimeMillis();
        final long elapsedStatic = after - before;
        this.result = result;
        add(Tester.CAT_BENCHMARK, "add of array values", elapsedArray + " ms");
        add(Tester.CAT_BENCHMARK, "add of locals", elapsedLocal + " ms");
        add(Tester.CAT_BENCHMARK, "add of instance variables ", elapsedInstance + " ms");
        add(Tester.CAT_BENCHMARK, "add of static variables ", elapsedStatic + " ms");
    }

    private void performMultiplicationBenchmark() {
        long before;
        long after;
        int result = 0;
        before = System.currentTimeMillis();
        for (int i = 0; i < Tester.NUMBER_OF_OPS / 100; i++) {
            for (int j = 0; j < 100; j++) {
                result = arrayA[j] * arrayB[j];
            }
        }
        after = System.currentTimeMillis();
        final long elapsedArray = after - before;
        final int localA = random.nextInt();
        final int localB = random.nextInt();
        before = System.currentTimeMillis();
        for (int i = 0; i < Tester.NUMBER_OF_OPS; i++) {
            result = localA * localB;
        }
        after = System.currentTimeMillis();
        final long elapsedLocal = after - before;
        before = System.currentTimeMillis();
        for (int i = 0; i < Tester.NUMBER_OF_OPS; i++) {
            result = instanceA * instanceB;
        }
        after = System.currentTimeMillis();
        final long elapsedInstance = after - before;
        before = System.currentTimeMillis();
        for (int i = 0; i < Tester.NUMBER_OF_OPS; i++) {
            result = Tester.staticA * Tester.staticB;
        }
        after = System.currentTimeMillis();
        final long elapsedStatic = after - before;
        this.result = result;
        add(Tester.CAT_BENCHMARK, "mul of array values", elapsedArray + " ms");
        add(Tester.CAT_BENCHMARK, "mul of locals", elapsedLocal + " ms");
        add(Tester.CAT_BENCHMARK, "mul of instance variables ", elapsedInstance + " ms");
        add(Tester.CAT_BENCHMARK, "mul of static variables ", elapsedStatic + " ms");
    }

    private void performDivisionBenchmark() {
        long before;
        long after;
        before = System.currentTimeMillis();
        int result = 0;
        for (int i = 0; i < Tester.NUMBER_OF_OPS / 100; i++) {
            for (int j = 0; j < 100; j++) {
                result = arrayA[j] / arrayB[j];
            }
        }
        after = System.currentTimeMillis();
        final long elapsedArray = after - before;
        int localA, localB;
        do {
            localA = random.nextInt();
        } while (localA == 0);
        do {
            localB = random.nextInt();
        } while (localB == 0);
        before = System.currentTimeMillis();
        for (int i = 0; i < Tester.NUMBER_OF_OPS; i++) {
            result = localA / localB;
        }
        after = System.currentTimeMillis();
        final long elapsedLocal = after - before;
        before = System.currentTimeMillis();
        for (int i = 0; i < Tester.NUMBER_OF_OPS; i++) {
            result = instanceA / instanceB;
        }
        after = System.currentTimeMillis();
        final long elapsedInstance = after - before;
        before = System.currentTimeMillis();
        for (int i = 0; i < Tester.NUMBER_OF_OPS; i++) {
            result = Tester.staticA / Tester.staticB;
        }
        after = System.currentTimeMillis();
        final long elapsedStatic = after - before;
        this.result = result;
        add(Tester.CAT_BENCHMARK, "div of array values", elapsedArray + " ms");
        add(Tester.CAT_BENCHMARK, "div of locals", elapsedLocal + " ms");
        add(Tester.CAT_BENCHMARK, "div of instance variables ", elapsedInstance + " ms");
        add(Tester.CAT_BENCHMARK, "div of static variables ", elapsedStatic + " ms");
    }
}
