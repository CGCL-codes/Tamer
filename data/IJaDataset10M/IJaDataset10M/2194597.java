package com.jpmorrsn.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.jpmorrsn.graphics.DrawFBP.FontType;

public class MyFontChooser implements ListSelectionListener, WindowListener {

    private static final long serialVersionUID = 1L;

    JList jl = null;

    Dialog popup;

    JFrame frame;

    String result;

    Graphics g = null;

    Dimension minSize;

    Dimension prefSize;

    Dimension maxSize;

    Graphics2D osg;

    String[] selFonts;

    String fontDesc;

    DrawFBP driver;

    String title = "";

    FontType ft = null;

    MyFontChooser(FontType ftyp, JFrame f, Graphics2D og, DrawFBP drawFBP) {
        frame = f;
        osg = og;
        driver = drawFBP;
        ft = ftyp;
    }

    String getResult() {
        popup = new Dialog(frame, Dialog.ModalityType.APPLICATION_MODAL);
        popup.setFocusable(true);
        popup.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent ev) {
                if (ev.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    popup.dispose();
                }
            }
        });
        fontDesc = (ft == FontType.GENERAL) ? "general text" : "fixed width";
        JPanel p = new JPanel();
        title = "Select font for " + fontDesc + " characters";
        popup.setTitle(title);
        minSize = new Dimension(200, 20);
        prefSize = new Dimension(200, 20);
        maxSize = new Dimension(Short.MAX_VALUE, 20);
        g = frame.getGraphics();
        p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
        popup.add(p);
        jl = new JList((Object[]) selFonts);
        jl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jl.setLayoutOrientation(JList.VERTICAL_WRAP);
        jl.setVisibleRowCount(40);
        CellRenderer cr = new CellRenderer();
        jl.setCellRenderer(cr);
        JScrollPane listScroller = new JScrollPane(jl);
        p.add(listScroller);
        popup.setBounds(100, 100, 600, 600);
        jl.addListSelectionListener(this);
        popup.addWindowListener(this);
        popup.setVisible(true);
        frame.update(g);
        return result;
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            if (jl.getSelectedIndex() > -1) {
                result = selFonts[jl.getSelectedIndex()];
                JOptionPane.showMessageDialog(frame, "Font '" + result + "' selected as font for " + fontDesc + " characters.");
                popup.setVisible(false);
            }
        }
    }

    void buildFontList() {
        Font[] allfonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        LinkedList<String> ll = new LinkedList<String>();
        for (int j = 0; j < allfonts.length; j++) {
            FontMetrics fontMetrics = osg.getFontMetrics(allfonts[j]);
            if (ft == FontType.GENERAL || (fontMetrics.charWidth('i') == fontMetrics.charWidth('m'))) ll.add(allfonts[j].getName());
        }
        selFonts = new String[ll.size()];
        ll.toArray(selFonts);
        return;
    }

    class CellRenderer implements ListCellRenderer {

        private static final long serialVersionUID = 1L;

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JPanel jp = new JPanel();
            BoxLayout gb = new BoxLayout(jp, BoxLayout.X_AXIS);
            jp.setLayout(gb);
            jp.setBackground(Color.WHITE);
            Dimension minSize2;
            Dimension prefSize2;
            Dimension maxSize2;
            minSize = new Dimension(150, 15);
            prefSize = new Dimension(150, 15);
            maxSize = new Dimension(Short.MAX_VALUE, 15);
            JLabel lab1 = new JLabel((String) value);
            lab1.setFont(new Font("Arial", Font.PLAIN, driver.defaultFontSize));
            lab1.setMinimumSize(minSize);
            lab1.setMaximumSize(maxSize);
            lab1.setPreferredSize(prefSize);
            jp.add(lab1);
            minSize2 = new Dimension(20, 15);
            prefSize2 = new Dimension(20, 15);
            maxSize2 = new Dimension(Short.MAX_VALUE, 15);
            jp.add(new Box.Filler(minSize2, prefSize2, maxSize2));
            JLabel lab2 = new JLabel("Sample English Text \\");
            lab2.setFont(new Font((String) value, Font.PLAIN, driver.defaultFontSize));
            minSize = new Dimension(150, 15);
            prefSize = new Dimension(150, 15);
            lab2.setMinimumSize(minSize);
            lab2.setMaximumSize(maxSize);
            lab2.setPreferredSize(prefSize);
            jp.add(lab2);
            jp.add(new Box.Filler(minSize2, prefSize2, maxSize2));
            JLabel lab3 = new JLabel("中文文本示例");
            lab3.setFont(new Font((String) value, Font.PLAIN, driver.defaultFontSize));
            minSize = new Dimension(100, 15);
            prefSize = new Dimension(100, 15);
            lab3.setMinimumSize(minSize);
            lab3.setMaximumSize(maxSize);
            lab3.setPreferredSize(prefSize);
            jp.add(lab3);
            return jp;
        }
    }

    @Override
    public void windowClosed(WindowEvent arg0) {
        popup.setVisible(false);
    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
    }

    @Override
    public void windowIconified(WindowEvent arg0) {
    }

    @Override
    public void windowOpened(WindowEvent arg0) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        popup.setVisible(false);
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
