package entornoAntares.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DialogoMensaje extends JDialog implements WindowListener {

    private static final long serialVersionUID = 1L;

    private static final int TAM_X = 300;

    private static final int TAM_Y = 100;

    private JLabel labelMensaje;

    private Frame owner;

    public DialogoMensaje(Frame owner, GraphicsConfiguration gc) {
        super(owner, "Main menu", false, gc);
        setUndecorated(true);
        setPreferredSize(new Dimension(TAM_X, TAM_Y));
        labelMensaje = new JLabel();
        this.getContentPane().add(labelMensaje);
        labelMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        pack();
        setResizable(false);
        setLocation(owner.getWidth() / 2 - TAM_X / 2, owner.getHeight() / 2 - TAM_Y / 2);
        this.owner = owner;
        this.addWindowListener(this);
    }

    public void mostrarMensaje(String m) {
        labelMensaje.setText(m);
        setLocation(owner.getWidth() - TAM_X / 2, owner.getHeight() - TAM_Y / 2);
        this.setVisible(true);
    }

    public void ocultar() {
        this.setVisible(false);
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }
}
