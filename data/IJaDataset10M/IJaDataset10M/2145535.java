package osj.appframework;

import java.awt.*;
import javax.swing.*;

public class SplashWindow extends JWindow {

    private Image theImage;

    private JPanel swContentPane;

    private JLabel messageLabel;

    private String message = "loading...";

    private LabelUpdater theLabelUpdater;

    private boolean messageChanged = false;

    public SplashWindow(ImageIcon theImage) {
        swContentPane = new JPanel();
        swContentPane.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        swContentPane.setLayout(new BorderLayout());
        messageLabel = new JLabel(message, theImage, JLabel.CENTER);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setVerticalTextPosition(JLabel.BOTTOM);
        messageLabel.setHorizontalTextPosition(JLabel.CENTER);
        messageLabel.setIconTextGap(10);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        swContentPane.add(messageLabel);
        this.setContentPane(swContentPane);
        setBounds(50, 50, 400, 250);
        swContentPane.setBackground(Color.white);
        swContentPane.setOpaque(true);
    }

    public void setMessage(String message) {
        this.message = message;
        messageChanged = true;
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            theLabelUpdater = new LabelUpdater();
            (new Thread(theLabelUpdater)).start();
        } else {
            theLabelUpdater.stop();
        }
    }

    public void centerOnScreen() {
        int x, y;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        x = (screenSize.width / 2) - (this.getWidth() / 2);
        y = (screenSize.height / 2) - (this.getHeight() / 2);
        this.setLocation(x, y);
    }

    class LabelUpdater implements Runnable {

        boolean run = true;

        String oldMessage;

        public void stop() {
            run = false;
        }

        public void run() {
            oldMessage = message;
            while (run) {
                if (messageChanged) {
                    messageLabel.setText(message);
                    oldMessage = message;
                    messageChanged = false;
                }
            }
        }
    }

    public static void main(String[] args) {
        ImageIcon theSplashImage = new ImageIcon(SplashWindow.class.getResource("images/osjperiodictable.png"));
        SplashWindow sw = new SplashWindow(theSplashImage);
        sw.centerOnScreen();
        sw.setVisible(true);
    }
}
