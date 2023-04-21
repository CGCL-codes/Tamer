package org.plazmaforge.framework.client.swing.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 * @author Oleh Hapon
 *
 */
public class GridBagPanel extends JPanel {

    GridBagConstraints gridBagConstraints = new GridBagConstraints();

    public GridBagPanel() {
        super(new GridBagLayout());
        initialize();
    }

    private void initialize() {
        setBorder(GUIEnvironment.createEmptyBorder());
        gridBagConstraints.anchor = GUIEnvironment.getGridLayoutLabelAnchor();
        gridBagConstraints.insets = GUIEnvironment.createGridLayoutInsets();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
    }

    public GridBagConstraints getGridBagConstraints() {
        return gridBagConstraints;
    }

    public Component add(Component comp) {
        super.add(comp, gridBagConstraints);
        return comp;
    }

    public void addByX(Component comp) {
        gridBagConstraints.gridx++;
        add(comp);
    }

    public void addByY(Component comp) {
        gridBagConstraints.gridy++;
        add(comp);
    }

    public void addSeparator(int width) {
        addSeparator(0, width);
    }

    public void addSeparator(int x, int width) {
        int oldX = gridBagConstraints.gridx;
        int oldWidth = gridBagConstraints.gridwidth;
        int oldFill = gridBagConstraints.fill;
        gridBagConstraints.gridx = x;
        gridBagConstraints.gridwidth = width;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        addByY(new JSeparator());
        gridBagConstraints.gridx = oldX;
        gridBagConstraints.gridwidth = oldWidth;
        gridBagConstraints.fill = oldFill;
    }
}
