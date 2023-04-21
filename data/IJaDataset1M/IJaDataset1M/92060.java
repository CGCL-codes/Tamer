package gui;

import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

/**
 *
 * @author Augusto
 */
public class popupAutoCompletar extends JTextField {

    public popupAutoCompletar(JFrame w) {
        JPopupMenu menuAutoCompletar = new JPopupMenu();
        JMenuItem itemDefault = new JMenuItem();
        JTextField fieldAutoCompletar = new JTextField();
        itemDefault.setText("jMenuItem1");
        menuAutoCompletar.add(itemDefault);
        fieldAutoCompletar.setComponentPopupMenu(menuAutoCompletar);
        fieldAutoCompletar.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                System.out.println("asd");
            }
        });
    }
}
