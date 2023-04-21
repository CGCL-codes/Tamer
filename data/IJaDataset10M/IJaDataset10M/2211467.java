package es.aeat.eett.rubik.treeNavi;

import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import com.tonbeller.jpivot.olap.model.Displayable;
import com.tonbeller.jpivot.olap.model.Hierarchy;
import com.tonbeller.jpivot.olap.model.Level;
import com.tonbeller.jpivot.olap.model.Member;
import swingUtil.Tamemasa.tree.CheckRenderer;

public class TreeCellRenderer extends CheckRenderer {

    private static final long serialVersionUID = -1L;

    protected Color cSel = UIManager.getColor("Tree.selectionForeground");

    protected Color cNor = UIManager.getColor("Tree.textForeground");

    private Icon closeIcon = UIManager.getIcon("Tree.closedIcon");

    private ImageIcon iconHierarchy = null;

    private ImageIcon iconLevel = null;

    private ImageIcon iconMember = null;

    private ImageIcon iconCalMember = null;

    private BasicCellRenderer basicCellRenderer = new BasicCellRenderer();

    private OptionRender optionRender = new OptionRender();

    TreeCellRenderer() {
        super();
        if (iconHierarchy == null) {
            iconHierarchy = new ImageIcon(this.getClass().getResource("/icons/class_hi.gif"));
            iconLevel = new ImageIcon(this.getClass().getResource("/icons/lrun_obj.gif"));
            iconMember = new ImageIcon(this.getClass().getResource("/icons/public_co.gif"));
            iconCalMember = new ImageIcon(this.getClass().getResource("/icons/calculateMember.png"));
        }
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        String stringValue = tree.convertValueToText(value, isSelected, expanded, leaf, row, hasFocus);
        Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
        if (((DefaultMutableTreeNode) value).getLevel() < 3) {
            return basicCellRenderer.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);
        } else if (userObject instanceof Displayable) {
            if (value instanceof MemberTreeNode) {
                MemberTreeNode nAxis = ((MemberTreeNode) value).getParentAxis();
                if (nAxis != null && nAxis.getUserObject().equals(TreeNavi.NAME_FILTER)) {
                    return optionRender.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);
                }
            }
            setEnabled(tree.isEnabled());
            Displayable displayable = (Displayable) userObject;
            if (displayable instanceof Hierarchy) {
                label.setIcon(iconHierarchy);
            } else {
                check.setSelected(((CheckNode) value).isSelected());
                if (displayable instanceof Hierarchy) {
                    label.setIcon(iconHierarchy);
                } else if (displayable instanceof Level) {
                    label.setIcon(iconLevel);
                } else if (displayable instanceof Member) {
                    if (((Member) displayable).isCalculated()) {
                        label.setIcon(iconCalMember);
                    } else {
                        label.setIcon(iconMember);
                    }
                }
            }
        } else {
            label.setIcon(UIManager.getIcon("Tree.leafIcon"));
        }
        label.setFont(tree.getFont());
        label.setText(stringValue);
        label.setSelected(isSelected);
        label.setFocus(hasFocus);
        if (isSelected) {
            label.setForeground(cSel);
        } else {
            label.setForeground(cNor);
        }
        return this;
    }

    class BasicCellRenderer extends DefaultTreeCellRenderer {

        private static final long serialVersionUID = -1L;

        BasicCellRenderer() {
            super();
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            String stringValue = tree.convertValueToText(value, isSelected, expanded, leaf, row, hasFocus);
            this.hasFocus = hasFocus;
            setText(stringValue);
            if (isSelected) setForeground(getTextSelectionColor()); else setForeground(getTextNonSelectionColor());
            if (!tree.isEnabled()) {
                setEnabled(false);
            } else {
                setEnabled(true);
            }
            switch(((DefaultMutableTreeNode) value).getLevel()) {
                case 0:
                case 1:
                    if (leaf) {
                        setIcon(leafIcon);
                    } else if (expanded) {
                        setIcon(openIcon);
                    } else {
                        setIcon(closeIcon);
                    }
                    break;
                case 2:
                    setIcon(iconHierarchy);
            }
            setComponentOrientation(tree.getComponentOrientation());
            selected = isSelected;
            return this;
        }
    }
}
