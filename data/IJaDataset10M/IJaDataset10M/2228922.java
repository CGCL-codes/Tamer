package JavaOrc.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * @class ColorTile
 *
 * @date 08-20-2001
 * @author Eric Crahen
 * @version 1.0
 *
 * Colored button that will display the color of given UIManager property and
 * display a JColorChooser that will edit that color when it is pressed.
 */
public class ColorTile extends JButton {

    private String title;

    private String colorProperty;

    /**
   * Create a new ColorTilefor teh given UIManager property.
   *
   * @param String property
   */
    public ColorTile(String colorProperty) {
        this(colorProperty, "Choose a Color");
    }

    /**
   * Create a new ColorTilefor teh given UIManager property and the given title
   * on the color chooser.
   *
   * @param String property
   * @param String chooser title
   */
    public ColorTile(String colorProperty, String title) {
        this.colorProperty = colorProperty;
        this.title = title;
        Dimension sz = new Dimension(32, 32);
        setMinimumSize(sz);
        setMaximumSize(sz);
        setBackground(UIManager.getColor(colorProperty));
    }

    /**
   * Listen for press event
   */
    protected void fireActionPerformed(ActionEvent event) {
        Color c = JColorChooser.showDialog(this, title, UIManager.getColor(colorProperty));
        if (c != null) UIManager.put(colorProperty, c);
    }
}
