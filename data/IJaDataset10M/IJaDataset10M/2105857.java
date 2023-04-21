package com.itbs.aimcer.gui.userlist;

import com.itbs.aimcer.bean.Renderable;
import com.itbs.aimcer.bean.GroupWrapper;
import com.itbs.aimcer.bean.ClientProperties;
import javax.swing.*;
import java.awt.*;

/**
 * Used to display the group
 * @author Alex Rass
 * @since Jul 28, 2008 1:47:35 PM
 */
public class GroupLabel extends JLabel implements Renderable {

    public static final Font NORM = new Font("sansserif", Font.BOLD, ClientProperties.INSTANCE.getFontSize() + 1);

    public static final Color COLOR_NORM = new Color(102, 102, 102);

    public static final Color COLOR_SPECIAL = new Color(255, 153, 0);

    public static final Color COLOR_WEATHER = new Color(50, 86, 154);

    /** Color of the selected Item */
    private static final Color COLOR_SELECTED = new Color(127, 127, 240);

    GroupWrapper group;

    public GroupLabel(GroupWrapper group) {
        this.group = group;
        setOpaque(false);
        setFont(NORM);
        setForeground(Color.WHITE);
        update();
    }

    private void update() {
        if (group.getName().equalsIgnoreCase("Weather")) {
            setBackground(COLOR_WEATHER);
        } else {
            setBackground(COLOR_NORM);
        }
    }

    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public JComponent getDisplayComponent(boolean isSelected, boolean cellHasFocus) {
        update();
        setSelected(isSelected);
        setText(group.getName() + " - " + group.sizeOnline() + "/" + group.size());
        return this;
    }

    /**
     * Up the width.
     * @return new width.
     */
    public Dimension getPreferredSize() {
        Dimension dim = super.getPreferredSize();
        dim.width += ContactLabel.SIZE_WIDTH_INC;
        dim.height += ContactLabel.SIZE_HEIGHT_INC + 2;
        return dim;
    }

    public static final int B_WIDTH = 11;

    public static final int B_HEIGHT = 6;

    public static final int B_CLOSE_SHIFT = 2;

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(selected ? COLOR_SELECTED : getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setPaint(Color.WHITE);
        g2d.drawLine(0, 0, getWidth(), 0);
        g2d.setPaint(getBackground().brighter());
        g2d.drawLine(0, 1, getWidth(), 1);
        g2d.setPaint(Color.BLACK);
        g2d.drawLine(2, getHeight() - 1, getWidth(), getHeight() - 1);
        g.translate(ContactLabel.SIZE_WIDTH_INC / 2, ContactLabel.SIZE_HEIGHT_INC / 2);
        Graphics2D g2 = (Graphics2D) g.create();
        super.paintComponent(g);
        g2.translate(getWidth() - B_WIDTH - ContactLabel.SIZE_WIDTH_INC, 5);
        g2.setStroke(new BasicStroke(2));
        g2.setColor(getForeground());
        if (group.isShrunk()) {
            g2.fillPolygon(new int[] { B_CLOSE_SHIFT, B_CLOSE_SHIFT + B_HEIGHT, B_CLOSE_SHIFT }, new int[] { 0, B_WIDTH / 2, B_WIDTH }, 3);
        } else {
            g2.fillPolygon(new int[] { 0, B_WIDTH, B_WIDTH / 2 }, new int[] { B_CLOSE_SHIFT, B_CLOSE_SHIFT, B_CLOSE_SHIFT + B_HEIGHT }, 3);
        }
        g2.dispose();
    }
}
