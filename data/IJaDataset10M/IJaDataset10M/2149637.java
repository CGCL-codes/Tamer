package de.cinek.rssview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JWindow;
import de.cinek.rssview.images.IconContainer;
import de.cinek.rssview.images.IconSet;

/**
 * TODO: Fix issues where 2 Channels are update in a quick interval (Window pops
 * up and is closed by the first Thread after some seconds)
 * @author christoph
 */
public class VisualNotification implements Notificator {

    private static final int CLOSE_TIMEOUT = 5000;

    private JWindow windowInstance;

    private JLabel textLabel;

    private final ResourceBundle rb = ResourceBundle.getBundle("rssview");

    /**
	 * @see de.cinek.rssview.Notificator#notifyUser(de.cinek.rssview.Channel, int,
	 *      int)
	 */
    public void notifyUser(Channel channel, int startIndex, int endIndex) {
        synchronized (this) {
            if (windowInstance == null) {
                this.windowInstance = createWindow();
            }
        }
        textLabel.setText(channel.getName() + " " + rb.getString("visual_notification_updated"));
        showWindow(windowInstance);
        new Thread("NotificationCloser") {

            /**
			 * @see java.lang.Thread#run()
			 */
            public void run() {
                try {
                    sleep(CLOSE_TIMEOUT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                windowInstance.dispose();
            }
        }.start();
    }

    private JWindow createWindow() {
        JWindow window = new JWindow();
        this.textLabel = new JLabel();
        this.textLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        window.getContentPane().add(new JLabel(IconContainer.getIconSet().getIcon(IconSet.ICON_APP)), BorderLayout.WEST);
        window.getContentPane().add(this.textLabel);
        return window;
    }

    private void showWindow(JWindow window) {
        window.pack();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int y = d.height - window.getHeight();
        int x = d.width - window.getWidth();
        window.setLocation(x, y);
        window.show();
    }
}
