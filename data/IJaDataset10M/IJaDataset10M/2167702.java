package com.loribel.commons.swing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * Panel to facilitate the build of a line with components.
 * Simply use the method addCol(JComponent)
 *
 * @author Gregory Borelli
 */
public class GB_PanelLine extends GB_Panel {

    /**
     * Index of the last col.
     */
    private int colIndex = -1;

    /**
     * The width to use between components.
     */
    private int separatorWidth = 7;

    /**
     * The layout used in this panel.
     */
    private GridBagLayout gridBagLayout;

    /**
     * Constructor of GB_PanelLine without parameter.
     */
    public GB_PanelLine() {
        super();
        gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);
    }

    /**
     * Get The width to use between components.
     *
     * @return int - <tt>separatorWidth</tt>
     */
    public int getSeparatorWidth() {
        return separatorWidth;
    }

    /**
     * Set The width to use between components.
     *
     * @param a_separatorWidth int - <tt>separatorWidth</tt>
     */
    public void setSeparatorWidth(int a_separatorWidth) {
        separatorWidth = a_separatorWidth;
    }

    /**
     * Method to really add to panel.
     * This is the unique method to add component to panel use in the method
     * addCol().
     *
     * @param a_comp JComponent -
     * @param c GridBagConstraints -
     */
    protected void addToMainPanel(JComponent a_comp, GridBagConstraints c) {
        if (a_comp == null) {
            return;
        }
        gridBagLayout.setConstraints(a_comp, c);
        this.add(a_comp);
    }

    public void addCol(String a_label) {
        addCol(new JLabel(a_label));
    }

    /**
     * Add a component as a col.
     *
     * @param a_comp JComponent - a_comp
     */
    public void addCol(JComponent a_comp) {
        if (a_comp == null) {
            return;
        }
        addSeparator();
        colIndex++;
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = colIndex;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        this.addToMainPanel(a_comp, c);
    }

    public void addSeparator(int a_width) {
        JLabel l_label = new JLabel();
        l_label.setPreferredSize(new Dimension(a_width, 2));
        colIndex++;
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = colIndex;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        this.addToMainPanel(l_label, c);
    }

    /**
     * Add a component witch accept horizontal filling.
     *
     * @param a_comp JComponent - a_comp
     */
    public void addColFill(JComponent a_comp) {
        if (a_comp == null) {
            return;
        }
        addSeparator();
        colIndex++;
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = colIndex;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.addToMainPanel(a_comp, c);
    }

    /**
     * Add a component witch accept horizontal and vertical filling.
     *
     * @param a_comp JComponent - a_comp
     */
    public void addColFill2(JComponent a_comp) {
        if (a_comp == null) {
            return;
        }
        addSeparator();
        colIndex++;
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = colIndex;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        this.addToMainPanel(a_comp, c);
    }

    /**
     * Add a separator between col. Use the separatorWitdth.
     */
    public void addSeparator() {
        if (colIndex == -1) {
            return;
        }
        addSeparator(separatorWidth);
    }
}
