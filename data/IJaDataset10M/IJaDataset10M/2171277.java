package com.trapezium.chisel.gui;

import java.awt.*;
import java.awt.event.*;

/** A title bar control for a movable window.  The title bar displays a title,
 *  moves the window which owns it.  By default it assumes its parent is the
 *  window to move, but any ancestor can be specified.  The title bar can also
 *  contain up to three buttons (minimize, maximize and close) as specified
 *  in the style parameter.  The default is a maximize and a close button.
 */
public class TitleBar extends ChiselAWTPane implements ActionListener {

    public static final int CLOSEBUTTON = 1;

    public static final int MINBUTTON = 2;

    public static final int MAXBUTTON = 4;

    String title;

    int style;

    GlyphButton minButton = null;

    GlyphButton maxButton = null;

    GlyphButton closeButton = null;

    boolean activated = false;

    public TitleBar(String title) {
        this(title, CLOSEBUTTON | MAXBUTTON, 1);
    }

    public TitleBar(String title, int style) {
        this(title, style, 1);
    }

    public TitleBar(String title, int style, int ancestor) {
        super(NO_BORDER);
        this.title = title;
        this.style = style;
        setBackground(DEFAULT_INACTIVECOLOR);
        setForeground(DEFAULT_TITLETEXTCOLOR);
        setFont(new Font("Dialog", Font.BOLD, 12));
        setLayout(null);
        if ((style & MINBUTTON) != 0) {
            minButton = new GlyphButton(GlyphButton.MINIMIZE);
            minButton.setForeground(Color.blue);
            minButton.addActionListener(this);
            add(minButton);
        }
        if ((style & MAXBUTTON) != 0) {
            maxButton = new GlyphButton(GlyphButton.MAXIMIZE);
            maxButton.setForeground(Color.black);
            maxButton.addActionListener(this);
            add(maxButton);
        }
        if ((style & CLOSEBUTTON) != 0) {
            closeButton = new GlyphButton(GlyphButton.CLOSE);
            closeButton.setForeground(Color.red);
            closeButton.addActionListener(this);
            add(closeButton);
        }
        Mover mover = new Mover(this, ancestor);
        addMouseListener(mover);
        addMouseMotionListener(mover);
    }

    /** one of the window buttons fired */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        ChiselAWTViewer viewer = (ChiselAWTViewer) getParent();
        if (source == minButton) {
            viewer.minimize();
        } else if (source == maxButton) {
            viewer.maximize();
        } else if (source == closeButton) {
            viewer.close();
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (title != null && title.length() > 0) {
            g.setColor(getForeground());
            Font font = getFont();
            FontMetrics fm = getFontMetrics(font);
            g.setFont(font);
            g.drawString(title, 4, 2 + fm.getAscent());
        }
    }

    public void doLayout() {
        Dimension size = getSize();
        int hgap = 2;
        Dimension buttonsize;
        int x = size.width;
        int y;
        if (closeButton != null) {
            buttonsize = closeButton.getPreferredSize();
            x -= hgap + buttonsize.width;
            y = (size.height - buttonsize.height) / 2;
            closeButton.setBounds(x, y, buttonsize.width, buttonsize.height);
        }
        if (maxButton != null) {
            buttonsize = maxButton.getPreferredSize();
            x -= hgap + buttonsize.width;
            y = (size.height - buttonsize.height) / 2;
            maxButton.setBounds(x, y, buttonsize.width, buttonsize.height);
        }
        if (minButton != null) {
            buttonsize = minButton.getPreferredSize();
            x -= hgap + buttonsize.width;
            y = (size.height - buttonsize.height) / 2;
            minButton.setBounds(x, y, buttonsize.width, buttonsize.height);
        }
    }

    public Dimension getPreferredSize() {
        Dimension size = (closeButton != null ? closeButton.getPreferredSize() : new Dimension(12, 12));
        size.width *= 7;
        size.height += 4;
        return size;
    }

    public void setText(String text) {
        title = text;
        repaint();
    }

    public String getText() {
        return (title);
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
        setBackground(activated ? DEFAULT_ACTIVECOLOR : DEFAULT_INACTIVECOLOR);
        repaint();
    }
}
