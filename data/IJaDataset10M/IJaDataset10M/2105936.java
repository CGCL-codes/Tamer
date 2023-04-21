package org.hawksee.javase;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import org.hawksee.core.tools.Manager;
import org.hawksee.javase.menus.MainMenu;

public class MainFrame extends JFrame {

    public static final long serialVersionUID = 0;

    private JFileChooser fileChooser;

    private StitcherPanel stitcherPanel;

    private JTextField messageField;

    private Manager manager;

    public MainFrame() {
        setTitle("HawkEYE");
        this.fileChooser = new JFileChooser(new File("."));
        this.manager = new Manager();
        this.stitcherPanel = new StitcherPanel(this.manager);
        this.messageField = new JTextField();
        this.setLayout(new java.awt.BorderLayout());
        this.add(this.stitcherPanel, BorderLayout.CENTER);
        this.add(this.messageField, BorderLayout.SOUTH);
        setJMenuBar(new MainMenu(this));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 500);
    }

    public Manager getManager() {
        return this.manager;
    }

    public void openImage() {
        int result = this.fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            String fileName = this.fileChooser.getSelectedFile().getPath();
            try {
                BufferedMonochromeImage image = new BufferedMonochromeImage(ImageIO.read(new File(fileName)));
                image.moveTo(0, 0);
                this.manager.addImage(image);
                this.stitcherPanel.repaint();
            } catch (IOException ioe) {
                System.err.println(ioe.toString());
            }
        }
    }

    public void quit() {
        System.exit(0);
    }

    public void setMessage(String text) {
        this.messageField.setText(text);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
