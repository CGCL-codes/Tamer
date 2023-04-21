package net.cevn.gui;

import net.cevn.gui.layout.BoxLayout;

/**
 * The <code>ToolBar</code> class represents a horizontal container with buttons. 
 * 
 * @author Christopher Field <cfield2@gmail.com>
 * @version
 * @since 0.0.1
 */
public class ToolBar extends AbstractContainer {

    /**
	 * The spacing in pixels between buttons in the horizontal direction.
	 */
    private int spacing = 0;

    /**
	 * Creates a new <code>ToolBar</code> instance.
	 */
    public ToolBar() {
        super();
        setBackground(ToolBar.ALL_STATE, null);
        setBorder(ToolBar.ALL_STATE, null);
        setLayout(BoxLayout.createHorizontalBox());
    }

    /**
	 * Sets the spacing in pixels. The distance between buttons in horizontal
	 * direction.
	 * 
	 * @param spacing The spacing.
	 */
    public void setSpacing(final int spacing) {
        this.spacing = spacing;
    }

    /**
	 * Gets the spacing in pixels.
	 * 
	 * @return
	 */
    public int getSpacing() {
        return spacing;
    }

    /**
	 * Add button.
	 * 
	 * @param button The button.
	 */
    public void addButton(final Button button) {
        addWidget(button);
        addWidget(BoxLayout.createHorizontalStrut(spacing));
    }

    /**
	 * Adds a window. This takes the tile button and adds it to the toolbar. When
	 * the button is clicked, the window is restored and brought to the front. If
	 * it was already restored, it is just brought to the front.
	 * 
	 * @param window The window.
	 */
    public void addWindow(final Window window) {
        addButton(window.getTileButton());
    }
}
