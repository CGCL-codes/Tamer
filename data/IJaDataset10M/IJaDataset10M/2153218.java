package org.f2o.absurdum.puck.util.swing;

import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultEditorKit;

public class EnhancedJTextArea extends JTextArea {

    private static JPopupMenu popupMenu;

    static {
        popupMenu = new JPopupMenu("Edit");
        JMenuItem menuItem = new JMenuItem(new DefaultEditorKit.CutAction());
        menuItem.setText("Cut");
        menuItem.setMnemonic(KeyEvent.VK_T);
        popupMenu.add(menuItem);
        menuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
        menuItem.setText("Copy");
        menuItem.setMnemonic(KeyEvent.VK_C);
        popupMenu.add(menuItem);
        menuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
        menuItem.setText("Paste");
        menuItem.setMnemonic(KeyEvent.VK_P);
        popupMenu.add(menuItem);
    }

    public EnhancedJTextArea() {
        super();
        init();
    }

    public EnhancedJTextArea(int rows, int cols) {
        super(rows, cols);
        init();
    }

    public EnhancedJTextArea(int rows, int cols, String content) {
        super(content, rows, cols);
        init();
    }

    public void init() {
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        this.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
        this.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                processMouseEvent(e);
            }

            public void mouseReleased(MouseEvent e) {
                processMouseEvent(e);
            }

            private void processMouseEvent(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    popupMenu.setInvoker(EnhancedJTextArea.this);
                }
            }
        });
    }
}
