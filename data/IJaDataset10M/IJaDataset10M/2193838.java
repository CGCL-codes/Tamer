package de.jassda.modules.trace.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import de.jassda.csp.Context;
import de.jassda.csp.CspProcess;
import de.jassda.modules.trace.event.EventSet;

/**
 * Class CellRenderer
 *
 *
 * @author Mark Broerkens
 * @version %I%, %G%
 */
public class CellRenderer implements TreeCellRenderer {

    static final String IMAGE_PATH = "de/jassda/modules/trace/resources/images/";

    static HashMap iconCache = new HashMap();

    final File imagePath = new File("de/jassda/modules/trace/resources/images");

    /**
     * Method getTreeCellRendererComponent
     *
     *
     * @param tree
     * @param value
     * @param selected
     * @param expanded
     * @param leaf
     * @param row
     * @param hasFocus
     *
     * @return
     *
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        ImageIcon icon;
        String className = value.getClass().getName();
        int begin = className.lastIndexOf('.') + 1;
        int end = className.length();
        String name = className.substring(begin, end);
        if (value instanceof EventSet) {
            icon = getIcon("EventSet_" + name + ".gif");
            if (icon == null) {
                icon = getIcon("EventSet_Default.gif");
            }
        } else if (value instanceof CspProcess) {
            icon = getIcon("Process_" + name + ".gif");
            if (icon == null) {
                icon = getIcon("Process_Default.gif");
            }
        } else if (value instanceof Context) {
            icon = getIcon("Context.gif");
        } else {
            icon = null;
        }
        JLabel label = new JLabel(value.toString(), icon, JLabel.RIGHT);
        label.setFont(new Font("Serif", Font.PLAIN, 12));
        label.setForeground(Color.black);
        return label;
    }

    /**
     * Method getIcon
     *
     *
     * @param name
     *
     * @return
     *
     */
    private ImageIcon getIcon(String name) {
        ImageIcon image = (ImageIcon) iconCache.get(name);
        if (image == null) {
            URL url = ClassLoader.getSystemResource(IMAGE_PATH + name);
            if (url != null) {
                image = new ImageIcon(url);
                iconCache.put(name, image);
            }
        }
        return image;
    }
}
