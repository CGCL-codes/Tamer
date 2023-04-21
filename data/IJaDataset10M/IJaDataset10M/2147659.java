package intellibitz.sted.widgets;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Displays the About message.
 */
public class AboutDialog extends JDialog implements KeyListener, MouseListener {

    private final JButton ok;

    public AboutDialog(String title, Component aboutDescriptor) {
        super();
        setTitle(title);
        getContentPane().add(aboutDescriptor);
        ok = new JButton("ok");
        ok.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent evt) {
                AboutDialog.this.setVisible(false);
            }
        });
        ok.addKeyListener(this);
        ok.setFocusable(true);
        final JPanel _pane = new JPanel();
        _pane.add(ok);
        getContentPane().add(BorderLayout.SOUTH, _pane);
        getRootPane().setDefaultButton(ok);
        setResizable(false);
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        pack();
        ok.requestFocus();
        setVisible(false);
    }

    public void setOKText(String okTitle) {
        ok.setText(okTitle);
    }

    /**
     * Invoked when a key has been pressed. See the class description for {@link
     * java.awt.event.KeyEvent} for a definition of a key pressed event.
     */
    public void keyPressed(KeyEvent e) {
        setVisible(false);
    }

    /**
     * Invoked when a key has been released. See the class description for
     * {@link KeyEvent} for a definition of a key released event.
     */
    public void keyReleased(KeyEvent e) {
        setVisible(false);
    }

    /**
     * Invoked when a key has been typed. See the class description for {@link
     * KeyEvent} for a definition of a key typed event.
     */
    public void keyTyped(KeyEvent e) {
        setVisible(false);
    }

    /**
     * Invoked when the mouse button has been clicked (pressed and released) on
     * a component.
     */
    public void mouseClicked(MouseEvent e) {
        setVisible(false);
    }

    /**
     * Invoked when the mouse enters a component.
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Invoked when the mouse exits a component.
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    public void mousePressed(MouseEvent e) {
        setVisible(false);
    }

    /**
     * Invoked when a mouse button has been released on a component.
     */
    public void mouseReleased(MouseEvent e) {
        setVisible(false);
    }
}
