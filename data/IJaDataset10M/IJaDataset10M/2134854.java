package com.global360.sketchpadbpmn.propertiespanel;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import com.global360.sketchpadbpmn.SketchpadApplication;
import com.global360.sketchpadbpmn.Utility;
import com.global360.sketchpadbpmn.i18n.Messages;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ColorTableCell extends EditAndButtonTableCell {

    private static final long serialVersionUID = 1L;

    private ColorPropertyValue currentValue = null;

    public ColorTableCell() {
        super();
    }

    public ColorTableCell(Color initialColor) {
        super(BorderLayout.EAST, TableCell.DELETE_PROPERTY_BUTTON, TableCell.DELETE_PROPERTY_BUTTON_SELECTED, "X", Messages.getString("ColorTableCell.ClearColor"));
        updateColor(new ColorPropertyValue(initialColor));
    }

    /**
   * @param initialColor
   */
    private void updateColor(ColorPropertyValue newColor) {
        if (newColor == null) {
            this.panel.getEdit().setBackground(Color.WHITE);
            this.panel.getEdit().setText("---");
            Font font = this.panel.getEdit().getFont();
            this.panel.getEdit().setFont(font.deriveFont(Font.ITALIC));
        } else if (newColor.isEmpty()) {
            this.panel.getEdit().setBackground(Color.WHITE);
            this.panel.getEdit().setText(Messages.getString("ColorTableCell.None"));
            Font font = this.panel.getEdit().getFont();
            this.panel.getEdit().setFont(font.deriveFont(Font.ITALIC));
        } else {
            this.panel.getEdit().setBackground(newColor.getColor());
            this.panel.getEdit().setText("");
        }
        this.currentValue = new ColorPropertyValue(newColor);
    }

    /**
   * Invoked when an action occurs.
   *
   * @param e ActionEvent
   * @todo Implement this java.awt.event.ActionListener method
   */
    public void actionPerformed(ActionEvent e) {
        Color startingColor = Color.white;
        Color newColor = null;
        if (e.getActionCommand().equals(EditAndButtonPanel.PRESS_IN_BUTTON)) {
            newColor = null;
        } else if (e.getActionCommand().equals(EditAndButtonPanel.PRESS_IN_EDIT)) {
            startingColor = Color.white;
            if ((this.currentValue == null) && !this.currentValue.isEmpty()) {
                startingColor = this.currentValue.getColor();
            }
            JColorChooser colorChooser = new JColorChooser(startingColor);
            JDialog dialog = JColorChooser.createDialog(this.panel.getEdit(), Messages.getString("ColorTableCell.SelectAColor"), true, colorChooser, new ColorChooserOkListener(this, colorChooser), null);
            Rectangle editBounds = this.panel.getEdit().getBounds();
            Point editScreenLocation = this.panel.getEdit().getLocationOnScreen();
            Point mainFrameLocation = SketchpadApplication.getMainFrame().getLocationOnScreen();
            dialog.setLocation(editScreenLocation.x + editBounds.width, mainFrameLocation.y + (SketchpadApplication.getMainFrame().getHeight() - dialog.getHeight()) / 2);
            dialog.setVisible(true);
            newColor = colorChooser.getColor();
        }
        if (!Utility.areEqual(startingColor, newColor)) {
            this.currentValue = new ColorPropertyValue(newColor);
            this.stopCellEditing();
        }
    }

    /**
   * getComponent
   *
   * @param value Object
   * @return Component
   * @todo Implement this com.global360.sketchpadbpmn.propertiespanel.TableCell method
   */
    protected Component getComponent(Object value, boolean isEnabled) {
        super.getComponent(value, isEnabled);
        this.panel.getEdit().setText("");
        ColorPropertyValue colorValue = new ColorPropertyValue(Color.DARK_GRAY);
        if (value instanceof Color) {
            colorValue = new ColorPropertyValue((Color) value);
        } else if (value instanceof ColorProperty) {
            colorValue = (ColorPropertyValue) ((ColorProperty) value).getContents();
        }
        updateColor(colorValue);
        this.panel.getEdit().setEnabled(isEnabled);
        return this.panel;
    }

    /**
   * Returns the value contained in the editor.
   *
   * @return the value contained in the editor
   * @todo Implement this javax.swing.CellEditor method
   */
    public Object getCellEditorValue() {
        return this.currentValue;
    }

    /**
   * setColor
   *
   * @param color Color
   */
    public void setColor(Color color) {
        this.panel.getEdit().setBackground(color);
    }
}

class ColorChooserOkListener implements ActionListener {

    ColorTableCell parent = null;

    JColorChooser chooser = null;

    public ColorChooserOkListener(ColorTableCell parent, JColorChooser chooser) {
        this.parent = parent;
        this.chooser = chooser;
    }

    public void actionPerformed(ActionEvent e) {
        this.parent.setColor(chooser.getColor());
    }
}
