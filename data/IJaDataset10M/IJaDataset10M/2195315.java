package com.teletalk.jadmin.gui.overview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class implements the GUI component of each element in the server component tree. This component 
 * can hold a string for the title (or key) of the element and a string representing a value. It can also be fitted with 
 * an icon.
 * 
 * @author Tobias L�fstrand
 * 
 * @since Alpha
 */
public final class KeyValueLabel extends JPanel {

    /**
    * 
    */
    private static final long serialVersionUID = 1L;

    private JLabel keyLabel;

    private JLabel valueLabel;

    private Icon icon;

    private Font keyFont;

    private Font valueFont;

    private int minWidth = 100;

    /**
	 * Creates a new KeyValueLabel.
	 */
    public KeyValueLabel() {
        super();
        initLayout();
    }

    /**
	 * Initializes the layout of this KeyValueLabel.
	 */
    private void initLayout() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        icon = null;
        keyFont = null;
        valueFont = null;
        keyLabel = new JLabel("");
        valueLabel = new JLabel("");
        keyLabel.setOpaque(false);
        keyLabel.setForeground(Color.black);
        add(keyLabel);
        add(Box.createHorizontalStrut(10));
        valueLabel.setOpaque(false);
        add(valueLabel);
        add(Box.createHorizontalStrut(10));
    }

    /**
	 * Destroys this KeyValueLabel.
	 */
    public void destroy() {
        remove(keyLabel);
        remove(valueLabel);
        keyLabel = null;
        valueLabel = null;
    }

    /**
	 * Sets the flag indicating if this KeyValueLabel is selected or not.
	 */
    public void setSelected(boolean selected) {
    }

    /**
	 * Sets the icon of this KeyValueLabel.
	 */
    public void setIcon(Icon icon) {
        if (icon != this.icon) {
            this.keyLabel.setIcon(icon);
            this.icon = icon;
        }
    }

    /**
	 * Sets the key text.
	 */
    public void setKeyText(final String text) {
        this.keyLabel.setText(" " + ((text != null) ? text : ""));
    }

    /**
	 * Sets the value text.
	 */
    public void setValueText(String text) {
        this.valueLabel.setText(" " + ((text != null) ? text : ""));
    }

    /**
	 * Sets the key font.
	 */
    public void setKeyFont(final Font font) {
        if (font != this.keyFont) {
            this.keyLabel.setFont(font);
            this.keyFont = font;
        }
    }

    /**
	 * Sets the value font.
	 */
    public void setValueFont(Font font) {
        if (font != this.valueFont) {
            this.valueLabel.setFont(font);
            this.valueFont = font;
        }
    }

    /**
	 * Calculates the component size to reduce layout problems (Swing).
	 */
    public void calculateComponentSize() {
        int width = this.keyLabel.getPreferredSize().width + this.valueLabel.getPreferredSize().width + 20;
        if (width < minWidth) width = minWidth; else minWidth = width;
        this.setPreferredSize(new Dimension(width, (int) this.getPreferredSize().getHeight()));
    }
}
