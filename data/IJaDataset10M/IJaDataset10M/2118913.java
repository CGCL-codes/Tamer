package org.javizy.ui.widget.table;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class JCellPanel extends JPanel {

    private boolean selected = false;

    private boolean enabled = true;

    private int margin = 0;

    private Color selectedBackground;

    private Color unSelectedBackground;

    public JCellPanel(int margin, Color selectedBackground, Color unSelectedBackground) {
        this.margin = margin;
        this.selectedBackground = selectedBackground;
        this.unSelectedBackground = unSelectedBackground;
    }

    private void update() {
        if (selected && enabled) {
            super.setBackground(this.selectedBackground);
            super.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(), BorderFactory.createEmptyBorder(this.margin - 2, this.margin - 2, this.margin - 2, this.margin - 2)));
        } else {
            super.setBackground(this.unSelectedBackground);
            super.setBorder(BorderFactory.createEmptyBorder(this.margin, this.margin, this.margin, this.margin));
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        update();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        update();
    }
}
