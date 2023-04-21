package de.mse.mogwai.impl.swing.beans;

import javax.swing.ButtonGroup;
import javax.swing.AbstractButton;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Component;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Implementation of an GroupBox panel.
 *
 * @author  Mirko Sertic
 */
public class ExtendedGroupBox extends ExtendedPanel {

    private ButtonGroup m_buttongroup;

    private String m_text;

    public ExtendedGroupBox() {
        this.m_buttongroup = new ButtonGroup();
        super.setBorder(javax.swing.BorderFactory.createLineBorder(Color.black, 1));
    }

    public void setText(String text) {
        this.m_text = text;
        this.repaint();
    }

    public String getText() {
        return this.m_text;
    }

    public void setBorder(javax.swing.border.Border border) {
    }

    public void paint(Graphics g) {
        super.paint(g);
        if ((this.m_text != null) && (this.m_text.length() > 0)) {
            java.awt.FontMetrics fm = g.getFontMetrics();
            int length = fm.stringWidth(this.m_text) + 10;
            int height = fm.getHeight();
            g.setColor(Color.black);
            int x[] = new int[4];
            int y[] = new int[4];
            x[0] = 0;
            y[0] = 0;
            x[1] = length + height;
            y[1] = 0;
            x[2] = length;
            y[2] = height;
            x[3] = 0;
            y[3] = height;
            g.setColor(Color.lightGray);
            g.fillPolygon(x, y, 4);
            g.setColor(Color.black);
            g.drawPolygon(x, y, 4);
            g.drawString(this.m_text, 5, fm.getAscent());
        }
    }

    public Component add(Component what) {
        if (what instanceof ExtendedAbstractButton) {
            this.m_buttongroup.add(((ExtendedAbstractButton) what).getDelegateAsAbstractButton());
            if (this.m_buttongroup.getButtonCount() == 1) ((ExtendedAbstractButton) what).getDelegateAsAbstractButton().setSelected(true);
        }
        return super.add(what);
    }
}
