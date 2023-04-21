package com.mockturtlesolutions.snifflib.invprobs;

import javax.swing.*;
import java.awt.*;
import javax.swing.tree.*;
import com.mockturtlesolutions.snifflib.guitools.components.IconServer;
import com.mockturtlesolutions.snifflib.datatypes.DblMatrix;
import com.mockturtlesolutions.snifflib.guitools.components.DomainNameNode;
import com.mockturtlesolutions.snifflib.guitools.components.NamedParameterNode;

public class HierarchicalCellRenderer extends JLabel implements TreeCellRenderer {

    private static final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);

    private Icon openIcon, closedIcon, leafIcon;

    public HierarchicalCellRenderer() {
        DefaultTreeCellRenderer rend1 = new DefaultTreeCellRenderer();
        this.openIcon = rend1.getOpenIcon();
        this.closedIcon = rend1.getClosedIcon();
        this.leafIcon = rend1.getLeafIcon();
        this.setOpaque(true);
        this.setIconTextGap(12);
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        leaf = false;
        this.setIcon(null);
        this.setText(null);
        Color BACKGROUND = Color.white;
        Color FOREGROUND = Color.black;
        if (value instanceof DefaultMutableTreeNode) {
            Object uo = ((DefaultMutableTreeNode) value).getUserObject();
            if (uo instanceof DblMatrix) {
                this.setText(uo.toString());
                this.setToolTipText("Parameter value");
            } else {
                if (uo instanceof DomainNameNode) {
                    if (uo instanceof NamedParameterNode) {
                        NamedParameterNode entry = (NamedParameterNode) uo;
                        String lastpart = entry.getDomain();
                        if (lastpart != null) {
                            String[] parts = lastpart.split("\\.");
                            if (parts.length > 1) {
                                lastpart = parts[parts.length - 1];
                            }
                        }
                        if (entry.hasParameterValue()) {
                            DblMatrix paramVal = entry.getParameterValue();
                            this.setText(lastpart + "=" + paramVal.getDoubleAt(0).toString());
                            this.setToolTipText(entry.getDomain());
                        } else {
                            ImageIcon img = entry.getImage();
                            if (img == null) {
                                this.setText(lastpart);
                            } else {
                                this.setIcon(entry.getImage());
                            }
                            this.setToolTipText(entry.getDomain());
                        }
                    } else {
                        DomainNameNode entry = (DomainNameNode) uo;
                        String lastpart = entry.getDomain();
                        if (lastpart != null) {
                            String[] parts = lastpart.split("\\.");
                            if (parts.length > 1) {
                                lastpart = parts[parts.length - 1];
                            }
                        }
                        ImageIcon img = entry.getImage();
                        if (img == null) {
                            this.setText(lastpart);
                        } else {
                            this.setIcon(entry.getImage());
                        }
                        this.setToolTipText(entry.getDomain());
                    }
                } else {
                    if (uo instanceof HierarchyTreeNode) {
                        Object k = ((HierarchyTreeNode) value).getUserObject();
                    } else {
                        if (uo instanceof String) {
                            this.setText((String) uo);
                            FOREGROUND = Color.blue;
                            this.setToolTipText("Parameter name");
                        } else {
                            throw new RuntimeException("Unexpected node of type:" + uo.getClass().toString());
                        }
                    }
                }
            }
        } else {
            throw new RuntimeException("Unexpected value class:" + value.getClass().toString());
        }
        if (isSelected) {
            this.setOpaque(true);
            this.setBackground(FOREGROUND);
            this.setForeground(BACKGROUND);
        } else {
            this.setOpaque(false);
            this.setBackground(BACKGROUND);
            this.setForeground(FOREGROUND);
        }
        return (this);
    }
}
