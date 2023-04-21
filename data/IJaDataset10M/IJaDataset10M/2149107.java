package de.sweetpete.percussionist.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class GuiToolkit {

    private static String iconsDefaultPath = "/gfx/controls/";

    private static String iconsDefaultExtension = ".png";

    public static Icon createIcon(String id) {
        String imgLocation = iconsDefaultPath + id + iconsDefaultExtension;
        URL imageURL = GuiToolkit.class.getResource(imgLocation);
        Icon icon = new ImageIcon(imageURL, id);
        return icon;
    }

    public static JButton createActionButton(AbstractAction abstractAction) {
        JButton button = new JButton();
        button.setAction(abstractAction);
        return button;
    }

    public static JMenuItem createMenuItem(AbstractAction abstractAction) {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setAction(abstractAction);
        return menuItem;
    }

    public static void setColumnWidths(JTable table, int[] widths) {
        TableModel model = table.getModel();
        for (int i = 0; i < model.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            if (i < widths.length) column.setPreferredWidth(widths[i]);
        }
    }

    public static void dumpActionMapKeys(JComponent c) {
        Object[] keys = c.getActionMap().allKeys();
        System.out.println("ActionMap keys of " + c.getClass().getCanonicalName());
        for (int i = 0; i < keys.length; i++) {
            System.out.println(">>>>" + keys[i]);
        }
    }

    public static void fitToScreen(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        Point frameLocation = frame.getLocation();
        if (frameLocation.x + frameSize.width > screenSize.width) frameLocation.x = screenSize.width - frameSize.width;
        if (frameLocation.y + frameSize.height > screenSize.height) frameLocation.y = screenSize.height - frameSize.height;
        frame.setSize(frameSize);
        frame.setLocation(frameLocation);
        frame.validate();
    }

    public static GridBagConstraints getGridBagConstraints(final int gridx, final int gridy) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        return gbc;
    }

    public static GridBagConstraints getGridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill, Insets insets, int ipadx, int ipady) {
        return new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insets, ipadx, ipady);
    }

    public static GridBagConstraints getGridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        return gbc;
    }

    public static GridBagConstraints getGridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight, int fill) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.fill = fill;
        return gbc;
    }
}
