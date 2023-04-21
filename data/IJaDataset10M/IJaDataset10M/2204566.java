package org.swtchart;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

/**
 * A title.
 */
public interface ITitle {

    /**
     * Sets the title text.
     * 
     * @param title
     *            the title text
     */
    public void setText(String title);

    /**
     * Gets the title text.
     * 
     * @return the title text
     */
    public String getText();

    /**
     * Sets the font for title text.
     * 
     * @param font
     *            the font for title text
     */
    public void setFont(Font font);

    /**
     * Gets the font for title text.
     * 
     * @return the font size for title text
     */
    public Font getFont();

    /**
     * Sets the foreground color of title.
     * 
     * @param color
     *            the foreground color of title
     */
    public void setForeground(Color color);

    /**
     * Gets the foreground color of title.
     * 
     * @return the foreground color of title
     */
    public Color getForeground();

    /**
     * Sets the visibility state of title.
     * 
     * @param visible
     *            the visibility state
     */
    void setVisible(boolean visible);

    /**
     * Gets the visibility state.
     * 
     * @return true if title is visible
     */
    boolean isVisible();
}
