package myComponents.myGUI.myJTreeTable;

import java.awt.Color;
import myComponents.myTreeCellRenderers.*;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import myComponents.myGUI.MyFont;

/**
 *
 * @author ΔΙΟΝΥΣΗΣ
 */
public class SeriesTreeCellRenderer extends DefaultTreeCellRenderer implements TreeCellRenderer {

    private static final long serialVersionUID = 534646765786987L;

    public SeriesTreeCellRenderer() {
        setLeafIcon(new ImageIcon(getClass().getResource("/images/small_watch.png")));
        setClosedIcon(new ImageIcon(getClass().getResource("/images/small_watch_off.png")));
        setOpenIcon(new ImageIcon(getClass().getResource("/images/small_watch_off.png")));
    }

    @Override
    public Font getFont() {
        Font font = new Font(MyFont.mySmallerFont.getFontName(), Font.PLAIN, MyFont.mySmallerFont.getSize());
        return font;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        return this;
    }
}
