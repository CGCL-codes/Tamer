package edu.rice.cs.drjava.ui.config;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import edu.rice.cs.drjava.config.*;
import edu.rice.cs.drjava.*;
import edu.rice.cs.util.swing.SwingFrame;

/** Graphical form of a ColorOption.
 *  @version $Id: ColorOptionComponent.java 5232 2010-04-24 00:14:05Z mgricken $
 */
public class ColorOptionComponent extends OptionComponent<Color, JPanel> {

    private JButton _button;

    private JTextField _colorField;

    private JPanel _panel;

    private Color _color;

    private boolean _isBackgroundColor;

    private boolean _isBoldText;

    /** Main constructor for ColorOptionComponent.
   *  @param opt The ColorOption to display
   *  @param text The text to display in the label of the component
   *  @param parent The Frame displaying this component
   */
    public ColorOptionComponent(ColorOption opt, String text, SwingFrame parent) {
        this(opt, text, parent, false);
    }

    /** An alternate constructor, allowing the caller to specify whether this color is a background color.  If so, 
   *  the button will display the color as its background.
   */
    public ColorOptionComponent(ColorOption opt, String text, SwingFrame parent, boolean isBackgroundColor) {
        this(opt, text, parent, isBackgroundColor, false);
    }

    public ColorOptionComponent(ColorOption opt, String text, SwingFrame parent, boolean isBackgroundColor, boolean isBoldText) {
        super(opt, text, parent);
        _isBackgroundColor = isBackgroundColor;
        _isBoldText = isBoldText;
        _button = new JButton();
        _button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                chooseColor();
            }
        });
        _button.setText("...");
        _button.setMaximumSize(new Dimension(10, 10));
        _button.setMinimumSize(new Dimension(10, 10));
        _colorField = new JTextField();
        _colorField.setEditable(false);
        _colorField.setHorizontalAlignment(JTextField.CENTER);
        _panel = new JPanel(new BorderLayout());
        _panel.add(_colorField, BorderLayout.CENTER);
        _panel.add(_button, BorderLayout.EAST);
        if (_isBackgroundColor) {
            _colorField.setForeground(DrJava.getConfig().getSetting(OptionConstants.DEFINITIONS_NORMAL_COLOR));
            DrJava.getConfig().addOptionListener(OptionConstants.DEFINITIONS_NORMAL_COLOR, new OptionListener<Color>() {

                public void optionChanged(OptionEvent<Color> oe) {
                    _colorField.setForeground(oe.value);
                }
            });
        } else {
            _colorField.setBackground(DrJava.getConfig().getSetting(OptionConstants.DEFINITIONS_BACKGROUND_COLOR));
            DrJava.getConfig().addOptionListener(OptionConstants.DEFINITIONS_BACKGROUND_COLOR, new OptionListener<Color>() {

                public void optionChanged(OptionEvent<Color> oe) {
                    _colorField.setBackground(oe.value);
                }
            });
        }
        if (_isBoldText) {
            _colorField.setFont(_colorField.getFont().deriveFont(Font.BOLD));
        }
        _color = DrJava.getConfig().getSetting(_option);
        _updateField(_color);
        setComponent(_panel);
    }

    /** Constructor that allows for a tooltip description. */
    public ColorOptionComponent(ColorOption opt, String text, SwingFrame parent, String description) {
        this(opt, text, parent, description, false);
    }

    /** Constructor that allows for a tooltip description as well as whether or not this is a background color. */
    public ColorOptionComponent(ColorOption opt, String text, SwingFrame parent, String description, boolean isBackgroundColor) {
        this(opt, text, parent, isBackgroundColor);
        setDescription(description);
    }

    /** Constructor that allows for a tooltip description as well as whether or not this is a background color.*/
    public ColorOptionComponent(ColorOption opt, String text, SwingFrame parent, String description, boolean isBackgroundColor, boolean isBoldText) {
        this(opt, text, parent, isBackgroundColor, isBoldText);
        setDescription(description);
    }

    /** Sets the tooltip description text for this option.
   *  @param description the tooltip text
   */
    public void setDescription(String description) {
        _panel.setToolTipText(description);
        _button.setToolTipText(description);
        _colorField.setToolTipText(description);
        _label.setToolTipText(description);
    }

    /** Updates the config object with the new setting.
   *  @return true if the new value is set successfully
   */
    public boolean updateConfig() {
        if (!_color.equals(DrJava.getConfig().getSetting(_option))) {
            DrJava.getConfig().setSetting(_option, _color);
        }
        return true;
    }

    /** Displays the given value. */
    public void setValue(Color value) {
        _color = value;
        _updateField(value);
    }

    /** Updates the component's field to display the given color. */
    private void _updateField(Color c) {
        if (_isBackgroundColor) _colorField.setBackground(c); else _colorField.setForeground(c);
        _colorField.setText(getLabelText() + " (" + _option.format(c) + ")");
    }

    /** Shows a color chooser dialog for picking a new color. */
    public void chooseColor() {
        Color c = JColorChooser.showDialog(_parent, "Choose '" + getLabelText() + "'", _color);
        if (c != null) {
            _color = c;
            notifyChangeListeners();
            _updateField(c);
        }
    }
}
