package org.eclipse.ui.internal;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * This class manages the common workbench colors.  
 */
public class WorkbenchColors {

    private static boolean init = false;

    private static Color[] workbenchColors;

    /**
     * Dispose all color pre-allocated by the workbench.
     */
    private static void disposeWorkbenchColors() {
        for (int i = 0; i < workbenchColors.length; i++) {
            workbenchColors[i].dispose();
        }
        workbenchColors = null;
    }

    /**
     * Initialize all colors used in the workbench in case the OS is using
     * a 256 color palette making sure the workbench colors are allocated.
     *
     * This list comes from the designers.
     */
    private static void initWorkbenchColors(Display d) {
        if (workbenchColors != null) {
            return;
        }
        workbenchColors = new Color[] { new Color(d, 255, 255, 255), new Color(d, 255, 251, 240), new Color(d, 223, 223, 191), new Color(d, 223, 191, 191), new Color(d, 192, 220, 192), new Color(d, 192, 192, 192), new Color(d, 191, 191, 191), new Color(d, 191, 191, 159), new Color(d, 191, 159, 191), new Color(d, 160, 160, 164), new Color(d, 159, 159, 191), new Color(d, 159, 159, 159), new Color(d, 159, 159, 127), new Color(d, 159, 127, 159), new Color(d, 159, 127, 127), new Color(d, 128, 128, 128), new Color(d, 127, 159, 159), new Color(d, 127, 159, 127), new Color(d, 127, 127, 159), new Color(d, 127, 127, 127), new Color(d, 127, 127, 95), new Color(d, 127, 95, 127), new Color(d, 127, 95, 95), new Color(d, 95, 127, 127), new Color(d, 95, 127, 95), new Color(d, 95, 95, 127), new Color(d, 95, 95, 95), new Color(d, 95, 95, 63), new Color(d, 95, 63, 95), new Color(d, 95, 63, 63), new Color(d, 63, 95, 95), new Color(d, 63, 95, 63), new Color(d, 63, 63, 95), new Color(d, 0, 0, 0), new Color(d, 195, 204, 224), new Color(d, 214, 221, 235), new Color(d, 149, 168, 199), new Color(d, 128, 148, 178), new Color(d, 106, 128, 158), new Color(d, 255, 255, 255), new Color(d, 0, 0, 0), new Color(d, 0, 0, 0), new Color(d, 132, 130, 132), new Color(d, 143, 141, 138), new Color(d, 171, 168, 165), new Color(d, 230, 226, 221) };
    }

    /**
     * Disposes of the colors. Ignore all
     * system colors as they do not need
     * to be disposed.
     */
    public static void shutdown() {
        if (!init) {
            return;
        }
        disposeWorkbenchColors();
        init = false;
    }

    /**
     * Initializes the colors.
     */
    public static void startup() {
        if (init) {
            return;
        }
        init = true;
        Display display = Display.getDefault();
        initWorkbenchColors(display);
    }
}
