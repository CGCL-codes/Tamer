package rvsnoop.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;
import rvsnoop.SubjectElement;

/**
 * A custom renderer for nodes in the subject explorer tree.
 * <p>
 * Based on <a href="http://wiki.apache.org/logging-log4j/LogFactor5">Log Factor 5</a>.
 *
 * @author <a href="mailto:lundberg@home.se">Örjan Lundberg</a>
 * @author <a href="mailto:ianp@ianp.org">Ian Phillips</a>
 * @version $Revision: 393 $, $Date: 2008-06-02 10:22:38 -0400 (Mon, 02 Jun 2008) $
 */
public class SubjectExplorerRenderer extends JPanel implements TreeCellRenderer {

    private static final long serialVersionUID = 5695225783544033925L;

    private final StringBuffer buffer = new StringBuffer();

    final JCheckBox checkbox = new JCheckBox();

    private final JLabel label = new JLabel();

    public SubjectExplorerRenderer() {
        super(new FlowLayout(FlowLayout.LEADING, 0, 0));
        setBackground(UIManager.getColor("Tree.textBackground"));
        checkbox.setOpaque(false);
        label.setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder());
        add(checkbox);
        add(label);
    }

    @Override
    protected final void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if ("text".equals(propertyName) || "selected".equals(propertyName)) super.firePropertyChange(propertyName, oldValue, newValue);
    }

    public final Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean focus) {
        final SubjectElement element = (SubjectElement) value;
        label.setText((String) element.getUserObject());
        checkbox.setSelected(element.isSelected());
        buffer.append("Records at this node: ").append(element.getNumRecordsHere());
        buffer.append("\nRecords under this node: ").append(element.getNumRecordsUnder());
        if (element.isErrorHere()) {
            buffer.append("\nThere is an error at this node.");
            label.setForeground(Color.RED);
        } else if (element.isErrorUnder()) {
            buffer.append("\nThere is an error under this node.");
            label.setForeground(Color.ORANGE);
        } else {
            label.setForeground(Color.BLACK);
        }
        setToolTipText(buffer.toString());
        buffer.setLength(0);
        return this;
    }
}
