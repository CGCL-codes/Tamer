package org.bpaul.rtalk.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Entry point for RTalk.
 * @author BPaul
 *
 */
public class RTalkUI {

    public static final String version = "RTalk v0.0.2";

    public static ResourceBundle messages;

    public static Properties params;

    public static JFrame appframe;

    public static List<EmoteImageIcon> emoticons;

    /**
	 * creates the window and displays it.
	 */
    private static void createAndShowGUI() {
        appframe = new JFrame("RTalk");
        appframe.setResizable(true);
        appframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel loginPanel = new LoginPanel();
        appframe.getContentPane().add(loginPanel);
        appframe.pack();
        appframe.setLocationRelativeTo(null);
        appframe.setVisible(true);
    }

    public static void main(String[] args) {
        emoticons = EmotIcons.getIconList();
        Locale locale = Locale.getDefault();
        messages = ResourceBundle.getBundle("messages", locale);
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        InputStream is = loader.getResourceAsStream("params.properties");
        params = new Properties();
        try {
            params.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });
    }
}
