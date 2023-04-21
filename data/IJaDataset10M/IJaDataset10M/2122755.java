package com.trentech.billcalc.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import com.trentech.billcalc.event.EventFactory;

/**
 * ToolTipManager controls display of a single tooltip that exists outside of 
 * a frame.  This tooltip is meant for long explanations of a graphical item
 * while still being readable.
 * 
 * @author Trent Hoeppner
 */
public class ToolTipManager {

    /**
     * The component that will display the help text.
     */
    private JTextArea helpLabel;

    /**
     * The size of the area (width * height) that should be used for the 
     * tooltip.
     */
    private int helpAreaSize;

    /**
     * The JFrame to anchor the tooltip to.
     */
    private JFrame base;

    /**
     * The tooltip.
     */
    private JWindow tooltip;

    /**
     * An enumeration of the edges for a frame or component.  The edges are 
     * ordered to match the preferences of the tooltip placement.
     */
    private enum Edge {

        /**
         * The right of the container.
         */
        Right, /**
         * The bottom of the container.
         */
        Bottom, /**
         * The left of the container.
         */
        Left, /**
         * The top of the container.
         */
        Top
    }

    /**
     * Constructor for ContextHelpListener.
     * 
     * @param   base    the frame that will serve as the anchor for the tooltip.
     *                  Cannot be null.
     */
    public ToolTipManager(JFrame base) {
        this.base = base;
        estimateHelpTipMaxArea();
        tooltip = new JWindow();
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        tooltip.setContentPane(contentPane);
        contentPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        helpLabel = new JTextArea("");
        helpLabel.setEditable(false);
        helpLabel.setLineWrap(true);
        helpLabel.setWrapStyleWord(true);
        contentPane.add(helpLabel, BorderLayout.CENTER);
        base.addComponentListener(new WindowMoveListener());
    }

    /**
     * Sets the text for the tooltip and repositions the tooltip based on the
     * location of the frame.
     * 
     * @param   text    the text to set.  If null or empty, the tooltip will be 
     *                  hidden.
     */
    public void setText(String text) {
        if (text == null || text.length() == 0) {
            tooltip.setVisible(false);
            return;
        }
        helpLabel.setText(text);
        Rectangle bounds = findBounds();
        if (bounds != null) {
            tooltip.setBounds(bounds);
            tooltip.setVisible(true);
        } else {
            tooltip.setVisible(false);
            System.out.println("no suitable bounds found");
        }
    }

    /**
     * Finds the best location for the for tooltip.
     * 
     * @return  the new bounds for the tooltip.  Will be null if there is no 
     *          location that can hold the tooltip.
     */
    private Rectangle findBounds() {
        GraphicsConfiguration currentConfig = findCurrentConfig();
        Rectangle screenBounds = currentConfig.getBounds();
        for (Edge edge : Edge.values()) {
            Rectangle bounds = isEdgeSuitable(screenBounds, edge);
            if (bounds != null) {
                return bounds;
            }
        }
        return null;
    }

    /**
     * Returns the bounds of the tooltip if the edge is suitable, or null if 
     * the edge is not suitable.
     * 
     * @param   screenBounds    the bounds for the current screen.  Cannot be 
     *                          null.
     * @param   edge            the edge to check for suitability.  Cannot be 
     *                          null.
     *                          
     * @return  the bounds of the tooltip if edge is suitable, null otherwise.
     */
    private Rectangle isEdgeSuitable(Rectangle screenBounds, Edge edge) {
        Rectangle bounds = createBounds(edge);
        if (screenBounds.contains(bounds)) {
            return bounds;
        }
        return null;
    }

    /**
     * Creates the bounds of the tooltip based on which edge of the base frame 
     * it will appear on.  The tooltip will be on the outside of the frame.
     * 
     * @param   edge    the edge to create the bounds for.  Cannot be null.
     * 
     * @return  the bounds for the tooltip.  Will not be null.
     */
    private Rectangle createBounds(Edge edge) {
        int heightForFixedWidth = helpAreaSize / base.getWidth();
        int widthForFixedHeight = helpAreaSize / base.getHeight();
        Rectangle rectangle;
        switch(edge) {
            case Top:
                rectangle = new Rectangle(base.getX(), base.getY() - heightForFixedWidth, base.getWidth(), heightForFixedWidth);
                break;
            case Bottom:
                rectangle = new Rectangle(base.getX(), base.getY() + base.getHeight(), base.getWidth(), heightForFixedWidth);
                break;
            case Right:
                rectangle = new Rectangle(base.getX() + base.getWidth(), base.getY(), widthForFixedHeight, base.getHeight());
                break;
            case Left:
                rectangle = new Rectangle(base.getX() - widthForFixedHeight, base.getY(), widthForFixedHeight, base.getHeight());
                break;
            default:
                rectangle = null;
        }
        return rectangle;
    }

    /**
     * Finds the configuration which represents the screen that the base frame 
     * is currently in.
     * 
     * @return  the configuration for the current screen.  Will not be null.
     */
    private GraphicsConfiguration findCurrentConfig() {
        GraphicsConfiguration found = null;
        GraphicsEnvironment environ = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = environ.getScreenDevices();
        for (GraphicsDevice device : devices) {
            GraphicsConfiguration config = device.getDefaultConfiguration();
            if (config.getBounds().contains(base.getLocation())) {
                found = config;
                break;
            }
        }
        if (found == null) {
            found = environ.getDefaultScreenDevice().getDefaultConfiguration();
        }
        return found;
    }

    /**
     * Estimates how much area will be required for the tooltip based on the
     * size of the largest tooltip text, and sets {@link #helpAreaSize}.
     */
    private void estimateHelpTipMaxArea() {
        String largestText = "";
        for (EventFactory factory : EventFactory.values()) {
            String description = factory.getLocalizedDescription();
            if (description.length() > largestText.length()) {
                largestText = description;
            }
            for (String propertyName : factory.getPropertyNames()) {
                String propertyDescription = factory.getLocalizedDescription(propertyName);
                if (propertyDescription.length() > largestText.length()) {
                    largestText = propertyDescription;
                }
            }
        }
        JPanel panel = new JPanel();
        JTextArea textArea = new JTextArea();
        FlowLayout layout = new FlowLayout();
        panel.setLayout(layout);
        panel.add(textArea);
        panel.setPreferredSize(new Dimension(1000, 1000));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText(largestText);
        layout.layoutContainer(panel);
        Dimension size = textArea.getPreferredSize();
        helpAreaSize = (int) ((size.width * size.height) * 1.1);
    }

    /**
     * WindowMoveListener is listener for moves and resizes of the base frame
     * so that the tooltip location and size can be adjusted accordingly. 
     */
    private class WindowMoveListener extends ComponentAdapter {

        /**
         * Constructor for WindowMoveListener.
         */
        public WindowMoveListener() {
        }

        /**
         * {@inheritDoc}
         */
        public void componentMoved(ComponentEvent e) {
            setText(helpLabel.getText());
        }

        /**
         * {@inheritDoc}
         */
        public void componentResized(ComponentEvent e) {
            setText(helpLabel.getText());
        }
    }
}
