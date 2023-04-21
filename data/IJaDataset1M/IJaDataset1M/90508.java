package com.memoire.bu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

/**
 * A small window with a title.
 */
public class BuPopupWindow extends JPopupMenu {

    public BuPopupWindow() {
        BuBorderLayout lo = new BuBorderLayout();
        lo.setVgap(2);
        setLayout(lo);
        setBorder(new CompoundBorder(getBorder(), BuBorders.EMPTY1212));
    }

    static class Title extends JPanel {

        Title(String _title) {
            JLabel t = new JLabel(_title);
            t.setFont(BuLib.deriveFont("InternalFrameTitlePane", Font.PLAIN, -2));
            t.setForeground(Color.white);
            t.setBorder(new EmptyBorder(1, 2, 0, 2));
            JButton b = new JButton("-");
            b.setFont(BuLib.deriveFont("InternalFrameTitlePane", Font.BOLD, -2));
            b.setBorder(new EmptyBorder(0, 2, 0, 2));
            b.setRequestFocusEnabled(false);
            b.setActionCommand("LINIFY");
            this.setBackground(this.getBackground().darker());
            this.setOpaque(true);
            this.setLayout(new BuBorderLayout());
            this.add(t, BuBorderLayout.CENTER);
            this.add(b, BuBorderLayout.EAST);
        }
    }

    private String title_;

    public String getTitle() {
        return title_;
    }

    JPanel bt_;

    public void setTitle(String _title) {
        title_ = _title;
        if (bt_ != null) {
            remove(bt_);
        }
        if (_title == null) bt_ = new JPanel(); else {
            bt_ = new Title(_title);
        }
        add(bt_, BuBorderLayout.NORTH);
        revalidate();
    }

    private JComponent content_;

    public void setContent(JComponent _content) {
        content_ = _content;
        add(content_, BuBorderLayout.CENTER);
        revalidate();
    }

    public static void main(String[] _args) {
        JFrame f = new JFrame();
        BuPopupWindow w = new BuPopupWindow();
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(100, 100));
        p.setBackground(Color.white);
        w.setTitle("Hello");
        w.add(p, BuBorderLayout.CENTER);
        w.setInvoker(f);
        w.setVisible(true);
    }
}
