package test;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.JOptionPane;

public class FullScreenDemo extends JFrame {

    private static final long serialVersionUID = 1L;

    private GraphicsEnvironment setup;

    private GraphicsDevice device;

    private Label label3;

    public FullScreenDemo() {
        setup = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = setup.getDefaultScreenDevice();
        Label label1 = new Label("Press CTRL+N to Start");
        Label label2 = new Label("Press CTRL+X to Exit");
        label3 = new Label("");
        Panel panel1 = new Panel();
        Panel panel2 = new Panel();
        panel1.add(label1);
        panel1.add(label2);
        panel2.setLayout(new GridLayout(1, 1));
        panel2.add(label3);
        this.addKeyListener(new KeyListener());
        this.add(panel1, BorderLayout.NORTH);
        this.add(panel2, BorderLayout.CENTER);
        this.setUndecorated(true);
        try {
            device.setFullScreenWindow(this);
            this.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class KeyListener extends KeyAdapter {

        public void keyPressed(KeyEvent evt) {
            if (evt.getKeyCode() == KeyEvent.VK_X) {
                int exit = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit ?", "Exit . . .", JOptionPane.YES_NO_OPTION);
                if (exit == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
            if (evt.getKeyCode() == KeyEvent.VK_N) {
                String getUser = JOptionPane.showInputDialog(null, "Enter your name:", "User . . .", JOptionPane.QUESTION_MESSAGE);
                label3.setText(" Welcome : " + getUser);
                label3.setFont(new Font("arial", Font.BOLD, 14));
            }
        }
    }

    public static void main(String[] args) {
        new FullScreenDemo();
    }
}
