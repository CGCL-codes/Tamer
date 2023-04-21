package gui;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Fenetre extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = -4533201738865348721L;

    public Fenetre() {
        super();
        this.setTitle("Galerie");
        this.setSize(300, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(buildPanel());
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setVisible(true);
    }

    private JPanel buildPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBackground(Color.white);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Fenetre frame = new Fenetre();
            }
        });
    }
}
