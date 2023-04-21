package org.jalgo.module.am1simulator.view;

import java.awt.Font;
import java.awt.Frame;
import org.jalgo.main.gui.JAlgoWindow;

/**
 * All constants for GUI elements are defined here.
 * 
 * @author Max Leuth&auml;user
 */
public final class GuiUtilities {

    /** The presentation font */
    public static final Font PRESENTATIONFONT = new Font("Courier", Font.BOLD, 18);

    /** The standard font */
    public static final Font STANDARDFONT = new Font("Courier", Font.PLAIN, 12);

    public static final Font STANDARDEDITORFONT = new Font("Monospaced", Font.PLAIN, 12);

    public static final Font PRESENTATIONEDITORFONT = new Font("Monospaced", Font.PLAIN, 18);

    /**
	 * This method returns the {@link JAlgoWindow} or throw a
	 * {@link RuntimeException} if it was unable to find it.
	 * 
	 * @return the {@link JAlgoWindow}
	 */
    public static JAlgoWindow getJAlgoWindow() {
        for (Frame f : Frame.getFrames()) if (f instanceof JAlgoWindow) return (JAlgoWindow) f;
        throw new RuntimeException("JAlgoWindow not found");
    }
}
