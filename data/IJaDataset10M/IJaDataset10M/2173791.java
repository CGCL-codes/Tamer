package org.das2.util;

import java.util.logging.*;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 *
 * @author  jbf
 */
public class Splash extends JWindow {

    public static Splash instance = null;

    private Handler handler;

    private JLabel messageLabel;

    public static String getVersion() {
        String cvsTagName = "$Name$";
        String version;
        if (cvsTagName.length() <= 9) {
            version = "untagged_version";
        } else {
            version = cvsTagName.substring(6, cvsTagName.length() - 2);
        }
        return version;
    }

    public Handler getLogHandler() {
        if (handler == null) {
            handler = createhandler();
        }
        return handler;
    }

    private Handler createhandler() {
        Handler result = new Handler() {

            Handler handler;

            public void publish(LogRecord logRecord) {
                System.out.println(logRecord.getMessage());
                messageLabel.setText(logRecord.getMessage());
            }

            public void flush() {
            }

            public void close() {
            }
        };
        return result;
    }

    private static ImageIcon getSplashImage() {
        URL url = Splash.class.getResource("/images/dasSplash.gif");
        if (url == null) return null;
        return new ImageIcon(url);
    }

    public static Splash getInstance() {
        if (instance == null) {
            instance = new Splash();
        }
        return instance;
    }

    public static void showSplash() {
        getInstance();
        instance.setVisible(true);
    }

    public static void hideSplash() {
        getInstance();
        instance.setVisible(false);
    }

    /** Creates a new instance of Splash */
    public Splash() {
        super();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(getSplashImage()), BorderLayout.CENTER);
        Box bottomPanel = Box.createHorizontalBox();
        messageLabel = new JLabel("");
        messageLabel.setMinimumSize(new Dimension(200, 10));
        bottomPanel.add(messageLabel);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(new JLabel("version " + getVersion() + "   ", JLabel.RIGHT));
        panel.add(bottomPanel, BorderLayout.SOUTH);
        this.setContentPane(panel);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        System.out.println("This is das2 version " + getVersion());
        Splash.showSplash();
        Logger.getLogger("").addHandler(Splash.getInstance().getLogHandler());
        try {
            for (int i = 0; i < 6; i++) {
                Thread.sleep(500);
                Logger.getLogger("").info("i=" + i);
            }
        } catch (java.lang.InterruptedException e) {
        }
        Splash.hideSplash();
    }
}
