package de.mse.mogwai.impl.swing.beans;

import javax.swing.JTabbedPane;

/**
 * Implementation of an extended Tabbed Pane.
 *
 * @author  Mirko Sertic
 */
public class ExtendedTabbedPane extends JTabbedPane implements ExtendedLayoutable {

    private int m_align;

    public ExtendedTabbedPane() {
        this.m_align = ExtendedLayoutable.NONE;
        this.setFont(this.getFont().deriveFont(java.awt.Font.PLAIN, 11));
    }

    public void processEvent(java.awt.AWTEvent e) {
        super.processEvent(e);
    }

    public int getAlign() {
        return this.m_align;
    }

    public void setAlign(int iAlign) {
        this.m_align = iAlign;
        this.doLayout();
    }
}
