package org.jazzteam.edu.lang.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;

public class MenuLayoutDemo {

    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.PAGE_AXIS));
        menuBar.add(createMenu("Menu 1"));
        menuBar.add(createMenu("Menu 2"));
        menuBar.add(createMenu("Menu 3"));
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
        return menuBar;
    }

    public JMenu createMenu(String title) {
        JMenu m = new HorizontalMenu(title);
        m.add("Menu item #1 in " + title);
        m.add("Menu item #2 in " + title);
        m.add("Menu item #3 in " + title);
        JMenu submenu = new HorizontalMenu("Submenu");
        submenu.add("Submenu item #1");
        submenu.add("Submenu item #2");
        m.add(submenu);
        return m;
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("MenuLayoutDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MenuLayoutDemo demo = new MenuLayoutDemo();
        Container contentPane = frame.getContentPane();
        contentPane.setBackground(Color.WHITE);
        contentPane.add(demo.createMenuBar(), BorderLayout.LINE_START);
        frame.setSize(300, 150);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    class HorizontalMenu extends JMenu {

        HorizontalMenu(String label) {
            super(label);
            JPopupMenu pm = getPopupMenu();
            pm.setLayout(new BoxLayout(pm, BoxLayout.LINE_AXIS));
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        @Override
        public void setPopupMenuVisible(boolean b) {
            boolean isVisible = isPopupMenuVisible();
            if (b != isVisible) {
                if ((b == true) && isShowing()) {
                    int x = 0;
                    int y = 0;
                    Container parent = getParent();
                    if (parent instanceof JPopupMenu) {
                        x = 0;
                        y = getHeight();
                    } else {
                        x = getWidth();
                        y = 0;
                    }
                    getPopupMenu().show(this, x, y);
                } else {
                    getPopupMenu().setVisible(false);
                }
            }
        }
    }
}
