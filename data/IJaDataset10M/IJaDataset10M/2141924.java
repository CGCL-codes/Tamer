package com.iver.cit.gvsig.project.documents.view.toc.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

/**
 * DOCUMENT ME!
 *
 * @author vcn To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */
public class PanelColor extends JPanel {

    public Color[] m_colores = new Color[100];

    public Color m_color;

    private Integer numReg;

    private int numreg;

    public int a = 155;

    public int b = 205;

    public int c = 255;

    /**
     * Creates a new PanelColor object.
     *
     * @param numReg DOCUMENT ME!
     */
    public PanelColor(int numReg) {
        setLayout(new BorderLayout());
        setMaximumSize(new Dimension(12, 12));
        setMinimumSize(new Dimension(12, 12));
        setPreferredSize(new Dimension(12, 12));
    }

    public void setColor(Color color) {
        m_color = color;
        a = color.getRed();
        System.out.println("a=color.getRed();" + a);
        b = color.getGreen();
        System.out.println("b=color.getGreen();" + b);
        c = color.getBlue();
        System.out.println("c=color.getBlue();" + c);
        this.setBackground(color);
    }

    /**
     * DOCUMENT ME!
     */
    public void setColorinicial() {
        a = 155;
        b = 205;
        c = 255;
        m_color = new Color(a, b, c);
        this.setBackground(m_color);
    }

    /**
     * DOCUMENT ME!
     */
    public void setColorfin() {
        this.m_color = new Color(45, 95, 145);
        a = 45;
        b = 95;
        c = 145;
        this.setBackground(m_color);
    }
}
