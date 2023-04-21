package fr.itris.glips.svgeditor.visualresources;

import java.awt.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the widgets that will be displayed in the properties dialog
 * 
 * @author ITRIS, Jordi SUC
 */
public class SVGVisualResourceWidget {

    /**
	 * a small font
	 */
    protected static final Font smallFont = new Font("smallFont", Font.ROMAN_BASELINE, 9);

    /**
	 * the font
	 */
    protected static final Font theFont = new Font("theFont", Font.ROMAN_BASELINE, 10);

    /**
     * the runnable used to dispose the widget
     */
    protected Runnable disposer = null;

    /**
	 * used to convert numbers into a string
	 */
    protected static DecimalFormat format;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        format = new DecimalFormat("######.#", symbols);
    }

    /**
     * the bundle used to get labels
     */
    protected ResourceBundle bundle = ResourcesManager.bundle;

    /**
     * the component
     */
    protected JComponent component = null;

    /**
     * the label associated with the panel
     */
    protected String label = "";

    /**
     * the constructor of the class
     */
    public SVGVisualResourceWidget() {
    }

    /**
     * @return the component
     */
    protected JComponent getComponent() {
        return component;
    }

    /**
     * @return the label
     */
    protected String getLabel() {
        return label;
    }

    /**
     * disposes the widget
     */
    public void dispose() {
        if (disposer != null) {
            disposer.run();
            disposer = null;
            component = null;
        }
    }
}
