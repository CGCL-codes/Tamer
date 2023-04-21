package edu.udo.scaffoldhunter.gui.util;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * A base class for custom ComboBox cell renderers that should be used instead
 * of deriving from DefaultListCellRenderer. This works around Java bug 6505565
 * which causes ComboBoxes with custom cell renderers to be drawn incorrectly
 * by the GTK look and feel.
 * <p>
 * The return value of getListCellRendererComponent() can be assumed to be a
 * JLabel.
 * 
 * @author Dominic Sacré
 */
public class CustomComboBoxRenderer implements ListCellRenderer {

    private final ListCellRenderer defaultRenderer;

    /**
     * Creates a new custom cell renderer.
     */
    public CustomComboBoxRenderer() {
        if (LookAndFeel.isGTKLookAndFeel()) {
            defaultRenderer = (new JComboBox()).getRenderer();
        } else {
            defaultRenderer = new DefaultListCellRenderer();
        }
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        return defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
