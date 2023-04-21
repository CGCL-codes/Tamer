package net.sf.matrixappender.ui;

import java.awt.Color;
import net.sf.matrixappender.Config;

public class ColorScheme {

    public final Color start;

    public final Color step;

    public final Color residue;

    private ColorScheme(Color start, Color step, Color residue) {
        this.start = start;
        this.step = step;
        this.residue = residue;
    }

    public static void reset() {
        fatal = new ColorScheme(new Color(Config.colorFull, Config.colorFull, Config.colorFull), new Color(Config.colorStep, Config.colorStep, Config.colorStep), new Color(200, 200, 200));
        error = new ColorScheme(new Color(Config.colorFull, Config.colorFlash, Config.colorFlash), new Color(Config.colorStep, Config.colorFlashStep, Config.colorFlash), new Color(Config.colorResidue, 0, 0));
        warn = new ColorScheme(new Color(Config.colorFull, Config.colorFull, Config.colorFlash), new Color(Config.colorStep, Config.colorStep, Config.colorFlashStep), new Color(Config.colorResidue, Config.colorResidue, 0));
        info = new ColorScheme(new Color(Config.colorFlash, Config.colorFull, Config.colorFlash), new Color(Config.colorFlashStep, Config.colorStep, Config.colorFlashStep), new Color(0, Config.colorResidue, 0));
        debug = new ColorScheme(new Color(Config.colorFlash, Config.colorFlash, Config.colorFull), new Color(Config.colorFlashStep, Config.colorFlashStep, Config.colorStep), new Color(0, 0, Config.colorResidue));
        trace = new ColorScheme(new Color(Config.colorFlash, Config.colorFlash, Config.colorFlash), new Color(Config.colorStep, Config.colorStep, Config.colorStep), new Color(Config.colorResidue, Config.colorResidue, Config.colorResidue));
    }

    static {
        reset();
    }

    public static ColorScheme fatal;

    public static ColorScheme error;

    public static ColorScheme warn;

    public static ColorScheme info;

    public static ColorScheme debug;

    public static ColorScheme trace;
}
