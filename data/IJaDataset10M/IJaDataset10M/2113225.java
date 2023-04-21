package kabalpackage.info;

import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import kabalpackage.*;

/**
 * Displays information on the GPL license under which this application has
 * been released.
 */
@SuppressWarnings({ "serial", "unused" })
public class LicenseWindow extends JFrame {

    public LicenseWindow() {
        try {
            String GTK = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
            UIManager.setLookAndFeel(GTK);
            UIManager.installLookAndFeel("GTK", GTK);
        } catch (Exception e) {
            System.err.println("Could not install GTK");
        }
        try {
            Image iconImage = ImageIO.read(getClass().getResourceAsStream("../images/icon.gif"));
            setIconImage(iconImage);
        } catch (Exception e) {
            System.err.println("Could not load icon.");
        }
        setTitle("License information");
        setLayout(new BorderLayout(7, 7));
        setSize(new Dimension(500, 300));
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel appTitle = new JLabel(" License information");
        appTitle.setFont(new Font("Sans", Font.BOLD, 24));
        add(appTitle, BorderLayout.NORTH);
        JTextArea textArea = new TextPanel();
        textArea.setEditable(false);
        textArea.setCaretPosition(0);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setAutoscrolls(false);
        add(scrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new ButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class TextPanel extends JTextArea {

        public TextPanel() {
            String licenseText = "";
            try {
                InputStreamReader fileReader = new InputStreamReader(getClass().getResourceAsStream("license.txt"));
                BufferedReader reader = new BufferedReader(fileReader);
                String lineRead;
                while ((lineRead = reader.readLine()) != null) {
                    licenseText += lineRead + "\n";
                }
            } catch (Exception e) {
                licenseText = "Could not read license text file!";
                System.err.println(licenseText);
            }
            this.setText(licenseText);
        }
    }

    private class ButtonPanel extends JPanel {

        public ButtonPanel() {
            setLayout(new FlowLayout());
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    LicenseWindow.this.dispose();
                }
            });
            add(closeButton);
        }
    }

    public static void main(String[] args) {
        new LicenseWindow();
    }
}
