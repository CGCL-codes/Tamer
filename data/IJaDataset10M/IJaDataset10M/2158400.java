package sudoku;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;

/**
 *
 * @author hobiwan
 */
public class CheckRenderer extends JPanel implements TreeCellRenderer {

    private static final long serialVersionUID = 1L;

    private JCheckBox check;

    private JLabel label;

    public CheckRenderer() {
        setLayout(null);
        add(check = new JCheckBox());
        add(label = new JLabel());
        check.setBackground(UIManager.getColor("Tree.textBackground"));
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        String stringValue = tree.convertValueToText(value, isSelected, expanded, leaf, row, hasFocus);
        setEnabled(tree.isEnabled());
        int selectionState = CheckNode.FULL;
        if (value instanceof CheckNode) {
            selectionState = ((CheckNode) value).getSelectionState();
        }
        check.setSelected(selectionState != CheckNode.NONE);
        check.setEnabled(selectionState != CheckNode.HALF);
        label.setFont(tree.getFont());
        label.setText(stringValue);
        if (leaf) {
            label.setIcon(UIManager.getIcon("Tree.leafIcon"));
        } else if (expanded) {
            label.setIcon(UIManager.getIcon("Tree.openIcon"));
        } else {
            label.setIcon(UIManager.getIcon("Tree.closedIcon"));
        }
        if (isSelected) {
            label.setForeground(UIManager.getColor("Tree.selectionForeground"));
            label.setBackground(UIManager.getColor("Tree.selectionBackground"));
            setBackground(UIManager.getColor("Tree.selectionBackground"));
        } else {
            label.setForeground(UIManager.getColor("Tree.textForeground"));
            label.setBackground(UIManager.getColor("Tree.textBackground"));
            setBackground(UIManager.getColor("Tree.textBackground"));
        }
        return this;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension dCheck = check.getPreferredSize();
        Dimension dLabel = label.getPreferredSize();
        return new Dimension(dCheck.width + dLabel.width, (dCheck.height < dLabel.height ? dLabel.height : dCheck.height));
    }

    @Override
    public void doLayout() {
        Dimension dCheck = check.getPreferredSize();
        Dimension dLabel = label.getPreferredSize();
        int yCheck = 0;
        int yLabel = 0;
        if (dCheck.height < dLabel.height) {
            yCheck = (dLabel.height - dCheck.height) / 2;
        } else {
            yLabel = (dCheck.height - dLabel.height) / 2;
        }
        check.setLocation(0, yCheck);
        check.setBounds(0, yCheck, dCheck.width, dCheck.height);
        label.setLocation(dCheck.width, yLabel);
        label.setBounds(dCheck.width, yLabel, dLabel.width, dLabel.height);
    }
}
