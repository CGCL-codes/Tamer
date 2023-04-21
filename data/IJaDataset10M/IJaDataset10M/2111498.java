package org.jbudget.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JColorChooser;
import org.jbudget.gui.BasicDialog;

/**
 * @author petrov
 */
public class ColorChooserDialog extends BasicDialog {

    /** Color chooser component. */
    private final JColorChooser colorChooser = new JColorChooser();

    /** A flag that is set to true when the OK button is pressed. This
   * indicates to the client that we hava a valid color. */
    private boolean okPressed = false;

    /** Creates a new instance of ColorChooserDialog */
    public ColorChooserDialog(java.awt.Frame parent, boolean modular, boolean alwaysDispose) {
        super(parent, modular, alwaysDispose);
        inputPanel.add(colorChooser, BorderLayout.CENTER);
        setTitle("Choose Color");
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    /** Creates a new instance of ColorChooserDialog */
    public ColorChooserDialog(java.awt.Frame parent, boolean modular, boolean alwaysDispose, Color initialColor) {
        super(parent, modular, alwaysDispose);
        colorChooser.setColor(initialColor);
        inputPanel.add(colorChooser, BorderLayout.CENTER);
        setTitle("Choose Color");
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    /** Always returns true. Has to be implemented to use the BasicDialog. */
    public boolean okAction() {
        okPressed = true;
        return true;
    }

    /** Returns the color selected in the chooser component. */
    public Color getColor() {
        return colorChooser.getColor();
    }

    /** Returns true if the component was closed by pressing the OK button. */
    public boolean haveNewColor() {
        return okPressed;
    }
}
