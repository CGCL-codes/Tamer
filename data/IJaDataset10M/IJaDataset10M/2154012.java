package uk.ekiwi.mq;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Bryce
 */
public abstract class AbstractValidator extends InputVerifier implements KeyListener {

    private JDialog popup;

    private Object parent;

    private JLabel messageLabel;

    private Point point;

    private Dimension cDim;

    private Color color;

    private JComponent component;

    private AbstractValidator() {
        color = new Color(243, 255, 159);
    }

    private AbstractValidator(JComponent c, String message) {
        this();
        c.addKeyListener(this);
        messageLabel = new JLabel(message + " ");
        component = c;
    }

    public AbstractValidator(JDialog parent, JComponent c, String message) {
        this(c, message);
        this.parent = parent;
        popup = new JDialog(parent);
        initComponents();
    }

    public AbstractValidator(JInternalFrame parent, JComponent c, String message) {
        this(c, message);
        this.parent = parent;
        popup = new JDialog(javax.swing.JOptionPane.getFrameForComponent(parent));
        initComponents();
    }

    public AbstractValidator(JLabel lblStatus, JComponent c, String message) {
        this(c, message);
        this.parent = parent;
        lblStatus.setText(message);
    }

    protected abstract boolean validationCriteria(JComponent c);

    public boolean verify(JComponent c) {
        if (!validationCriteria(c)) {
            if (parent instanceof WantsValidationStatus) ((WantsValidationStatus) parent).validateFailed();
            c.setBackground(Color.PINK);
            popup.setSize(0, 0);
            popup.setLocationRelativeTo(c.getParent());
            point = popup.getLocation();
            cDim = c.getSize();
            popup.setLocation(point.x - (int) cDim.getWidth() / 2, point.y + (int) cDim.getHeight() / 2);
            popup.pack();
            popup.setVisible(true);
            return false;
        }
        c.setBackground(Color.WHITE);
        if (parent instanceof WantsValidationStatus) ((WantsValidationStatus) parent).validatePassed();
        return true;
    }

    protected void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void keyPressed(KeyEvent e) {
        popup.setVisible(false);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    private void initComponents() {
        popup.getContentPane().setLayout(new FlowLayout());
        popup.setUndecorated(true);
        popup.getContentPane().setBackground(color);
        popup.getContentPane().add(messageLabel);
        popup.setFocusableWindowState(false);
    }
}
