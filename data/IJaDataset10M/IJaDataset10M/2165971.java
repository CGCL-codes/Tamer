package br.jabuti.util;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.StringTokenizer;
import javax.swing.JButton;
import br.jabuti.gui.*;
import java.awt.Font;
import java.io.*;
import java.net.*;

public class ToolConstants {

    public static final String toolName = "JaBUTi";

    public static final String toolDescription = "Java Bytecode Understanding and Testing";

    public static final String toolVersion = "1.0";

    public static final String mainLogo = "jabuti.gif";

    public static final String aboutLogo = "jabuti-about128x128.gif";

    public static final String blankSpace = "blank.gif";

    public static final String projectExtension = ".jbt";

    public static final String traceExtension = ".trc";

    public static final String instExtension = "_instr.jar";

    public static final Font normalFont = (new JButton()).getFont();

    public static final Font titleFont = normalFont.deriveFont(normalFont.getSize2D() + 1.0F);

    public static PrintStream STDERR = System.err;

    public static int sourceFontSize = 14;

    public static final String LABEL_COLOR = new String("Color");

    public static final String LABEL_DOMINATOR = new String("Dominator");

    public static final String LABEL_IDOMINATOR = new String("IDominator");

    public static final String LABEL_LIVE_DEFINITIONS = new String("Alive definitions");

    public static final String LABEL_WEIGHT = new String("Weight");

    public static final int COLOR_0 = 0;

    public static final int COLOR_1 = 1;

    public static final int COLOR_2 = 2;

    public static final int COLOR_3 = 3;

    public static final int COLOR_4 = 4;

    public static final int COLOR_5 = 5;

    public static final int COLOR_6 = 6;

    public static final int COLOR_7 = 7;

    public static final int COLOR_8 = 8;

    public static final int COLOR_9 = 9;

    public static final int SELECTED = 30;

    public static final int NUM_COLORS = 10;

    public static final Color[] CFGColors = new Color[NUM_COLORS];

    private static final Color selectedColor = new Color(128, 128, 255);

    public static Color getColor(int c) {
        if (CFGColors[0] == null) {
            CFGColors[COLOR_0] = new Color(192, 192, 192);
            CFGColors[COLOR_1] = new Color(255, 255, 255);
            CFGColors[COLOR_2] = new Color(000, 255, 255);
            CFGColors[COLOR_3] = new Color(064, 224, 208);
            CFGColors[COLOR_4] = new Color(000, 255, 127);
            CFGColors[COLOR_5] = new Color(173, 255, 047);
            CFGColors[COLOR_6] = new Color(255, 255, 000);
            CFGColors[COLOR_7] = new Color(255, 215, 000);
            CFGColors[COLOR_8] = new Color(255, 140, 000);
            CFGColors[COLOR_9] = new Color(255, 000, 000);
        }
        if (c == SELECTED) {
            return selectedColor;
        }
        if (c >= 1 && c < NUM_COLORS) {
            return CFGColors[c];
        }
        return CFGColors[COLOR_0];
    }

    public static String getFourDigitNumber(int num) {
        DecimalFormat formatter = new DecimalFormat("0000");
        return formatter.format(num);
    }

    public static void setSTDERR(PrintStream ps) {
        STDERR = ps;
    }

    public static void setSTDERR(String name) {
        try {
            STDERR = new PrintStream(new FileOutputStream(name));
        } catch (FileNotFoundException fnfe) {
            STDERR = System.err;
        }
    }

    public static void reportException(Throwable e, PrintStream out) {
        System.err.println("AN EXCEPTIONS WAS GENERETED...");
        out.println("==> " + e.getClass().getName());
        out.println("Message:   " + e.getMessage());
        out.println("Stack trace: ");
        e.printStackTrace(out);
        out.println("");
        out.flush();
    }

    public static URL getToolBaseResource(String filename) {
        URL toolBaseDirectory = JabutiGUI.class.getResource(filename);
        return toolBaseDirectory;
    }

    public static Class getClassFromClasspath(String cName, boolean initialize, String classpath) throws MalformedURLException, ClassNotFoundException {
        StringTokenizer st = new StringTokenizer(classpath, File.pathSeparator);
        URL[] arrUrl = new URL[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++) {
            arrUrl[i] = new File(st.nextToken()).toURI().toURL();
        }
        ClassLoader cLoader = new URLClassLoader(arrUrl);
        return Class.forName(cName, initialize, cLoader);
    }
}
