package com.chauhai.sequencelogvisualizer.display.colorchooser;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import com.chauhai.sequencelogvisualizer.display.Utils;

/**
 * Display some color to be selected.
 */
class ColorSelector extends JPanel implements MouseListener {

    private ActionListener actionListener;

    private Color color;

    /**
	 * Constructor.
	 * @param title
	 * @param initialColor
	 */
    public ColorSelector(String title, Color initialColor) {
        setBorder(title);
        addColorItems();
        color = initialColor != null ? initialColor : Color.BLACK;
    }

    /**
	 * Get current selected color.
	 * @return
	 */
    public Color getColor() {
        return color;
    }

    /**
	 * Set the border of the color selector.
	 * @param title
	 */
    private void setBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(title);
        Utils.setFontBold(this, false);
        setBorder(border);
    }

    /**
	 * Color that can be used.
	 */
    private void addColorItems() {
        add(createColorItem(0x000000, "Black"));
        add(createColorItem(0x808080, "Gray"));
        add(createColorItem(0xC0C0C0, "Silver"));
        add(createColorItem(0xFFFFFF, "White"));
        add(createColorItem(0xFF0000, "Red"));
        add(createColorItem(0xFFFF00, "Yellow"));
        add(createColorItem(0x00FF00, "Lime"));
        add(createColorItem(0x00FFFF, "Aqua"));
        add(createColorItem(0x0000FF, "Blue"));
        add(createColorItem(0xFF00FF, "Fuchsia"));
        add(createColorItem(0x800000, "Maroon"));
        add(createColorItem(0x808000, "Olive"));
        add(createColorItem(0x008000, "Green"));
        add(createColorItem(0x008080, "Teal"));
        add(createColorItem(0x000080, "Navy"));
        add(createColorItem(0x800080, "Purple"));
    }

    /**
	 * Create a item on the color selector to select colors.
	 * @param rgb color value.
	 * @param hint
	 * @return
	 */
    private JButton createColorItem(int rgb, String hint) {
        JButton item = new JButton("■");
        item.setForeground(new Color(rgb));
        item.setToolTipText(hint);
        item.setBorder(null);
        item.addMouseListener(this);
        return item;
    }

    public void addActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    /**
	 * Process when the mouse is moved on an color items.
	 * This will highlight the item.
	 */
    @Override
    public void mouseEntered(MouseEvent e) {
        JComponent item = (JComponent) e.getSource();
        item.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    /**
	 * Unhighlight a color item when the mouse is moved over it. 
	 */
    @Override
    public void mouseExited(MouseEvent e) {
        JComponent item = (JComponent) e.getSource();
        item.setBorder(null);
    }

    /**
	 * Select the color when the mouse is pressed on a color item.
	 */
    @Override
    public void mousePressed(MouseEvent e) {
        JComponent item = (JComponent) e.getSource();
        color = item.getForeground();
        actionListener.actionPerformed(new ActionEvent(this, 0, null));
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }
}
