package org.kumenya.ui.chart.drawing;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.KeyListener;

/**
 * @author Jean Morissette
 */
public interface Tool extends MouseInputListener, KeyListener {

    /**
     * @return a name for this tool.
     */
    String getName();

    /**
     * @return an icon for this tool.
     */
    Icon getIcon();

    /**
     * If the given tool manager is not null, the tool is added to the
     * ToolManager as the current editing one.
     * Otherwise the tool is reset.
     *
     * @param tm
     */
    void setToolManager(ToolManager tm);

    /**
     * @return the Cursor instance for this tool.
     */
    Cursor getCursor();

    /**
     * Paints the tools.
     *
     * @param g
     */
    void paint(Graphics2D g);
}
