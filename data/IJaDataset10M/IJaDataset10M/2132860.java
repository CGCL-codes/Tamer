package net.sourceforge.originalsynth.tab;

import java.awt.Component;
import javax.swing.JPanel;

/**
 * This is the JPanel which holds the controls at the top of each tab (e.g.
 * play, amplitude, duration, pitch)
 * 
 * 
 */
public class ControlsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * Duration controller.
     */
    public static final int WIDGET_DURATION = 0;

    /**
     * Frequency controller.
     */
    public static final int WIDGET_FREQ = 1;

    /**
     * Amplitude controller.
     */
    public static final int WIDGET_AMP = 2;

    /**
     * Play button.
     */
    public static final int WIDGET_PLAY = 3;

    private Component[] widgets = new Component[4];

    /**
     * Constructor.
     * 
     */
    public ControlsPanel() {
    }

    /**
     * Adds a "widget" such as a JSpinner.
     * 
     * @param comp
     *            The widget
     * @param widgetType
     *            The enumerated widget type
     */
    public void addWidget(Component comp, int widgetType) {
        super.add(comp);
        if (widgetType < widgets.length) {
            widgets[widgetType] = comp;
        }
    }

    /**
     * 
     * @param widgetType Specifies which widget you want.
     * @return The widget.
     */
    public Component getWidget(int widgetType) {
        if (widgetType < widgets.length) {
            return widgets[widgetType];
        }
        return null;
    }

    public void remove(int x) {
        super.remove(widgets[x]);
    }
}
