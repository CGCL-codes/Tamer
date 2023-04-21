package genj.app;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.reflect.Method;
import javax.swing.*;
import javax.swing.event.*;
import genj.gedcom.*;
import genj.util.ImgIcon;
import genj.util.swing.ImgIconConverter;

/**
 * A component that shows a list of TagPaths
 */
public class TagPathList extends JComponent {

    /** members */
    private JList lChoose;

    private Vector vPaths;

    /**
   * Tag List Cell Renderer
   */
    class TagPathRenderer implements ListCellRenderer {

        /** members */
        private JLabel label = new JLabel();

        private boolean selected;

        /** return component for rendering list element */
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            TagPath path = (TagPath) value;
            label.setText(path.toString());
            label.setIcon(ImgIconConverter.get(Property.getDefaultImage(path.getLast())));
            if (isSelected) label.setBackground(lChoose.getSelectionBackground()); else label.setBackground(lChoose.getBackground());
            label.setOpaque(isSelected);
            return label;
        }
    }

    /**
   * Constructor
   */
    public TagPathList() {
        lChoose = new JList();
        lChoose.setCellRenderer(new TagPathRenderer());
        lChoose.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setLayout(new BorderLayout());
        add(new JScrollPane(lChoose), "Center");
    }

    /**
   * Adds a path to this list
   */
    public void addPath(TagPath path) {
        if (vPaths == null) vPaths = new Vector();
        vPaths.addElement(path);
        lChoose.setListData(vPaths);
    }

    /**
   * Moves currently selected paths down
   */
    public void down() {
        int row = lChoose.getSelectedIndex();
        if ((row == -1) || (row == vPaths.size() - 1)) return;
        Object o = vPaths.elementAt(row);
        vPaths.setElementAt(vPaths.elementAt(row + 1), row);
        vPaths.setElementAt(o, row + 1);
        lChoose.setListData(vPaths);
        lChoose.setSelectedIndex(row + 1);
    }

    /**
   * Returns the paths used by this list
   */
    public TagPath[] getPaths() {
        if (vPaths == null) return new TagPath[0];
        TagPath[] result = new TagPath[vPaths.size()];
        vPaths.copyInto(result);
        return result;
    }

    /**
   * Returns the preferred size of this component
   */
    public Dimension getPreferredSize() {
        return new Dimension(64, 64);
    }

    /**
   * Removes a path from this list
   */
    public void removePath(TagPath path) {
        if (vPaths == null) return;
        vPaths.removeElement(path);
        lChoose.setListData(vPaths);
    }

    /**
   * Sets the paths used by this list
   */
    public void setPaths(TagPath[] paths) {
        vPaths = new Vector(paths.length);
        for (int p = 0; p < paths.length; p++) {
            vPaths.addElement(paths[p]);
        }
        lChoose.setListData(vPaths);
    }

    /**
   * Moves currently selected paths up
   */
    public void up() {
        int row = lChoose.getSelectedIndex();
        if ((row == -1) || (row == 0)) {
            return;
        }
        Object o = vPaths.elementAt(row);
        vPaths.setElementAt(vPaths.elementAt(row - 1), row);
        vPaths.setElementAt(o, row - 1);
        lChoose.setListData(vPaths);
        lChoose.setSelectedIndex(row - 1);
    }
}
