package net.coljac.zhongwen.reminder;

import net.coljac.zhongwen.CharRecord;
import net.coljac.zhongwen.CharApp;
import net.coljac.zhongwen.CharProps;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This is the main entry point for the "reminder" application. It puts
 * a undecorated ittle window in the corner of the screen and cycles through the
 * records in the database.
 * <p/>
 * JDK 1.5 only.
 */
public class ReminderFrame extends JFrame {

    ReminderPanel panel;

    public static ReminderFrame frame = null;

    JDialog mainWindow;

    public ReminderFrame(String filename) {
        setupSystemTray();
        frame = this;
        CharRecord record = null;
        try {
            String file = CharProps.getStringProperty("record.file", CharApp.RECORD_FILE);
            if (filename != null) {
                file = filename;
            }
            record = new CharRecord(file);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        panel = new ReminderPanel(record);
        mainWindow = new JDialog(this, false);
        final JDialog window = mainWindow;
        setUndecorated(true);
        KeyAdapter keyAdapt = new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                } else if (e.getKeyCode() == KeyEvent.VK_F1) {
                    showSettings();
                } else if (e.getKeyCode() == KeyEvent.VK_F2) {
                    window.setAlwaysOnTop(!window.isAlwaysOnTop());
                } else {
                    panel.keyPress(e.getKeyCode());
                }
            }
        };
        this.addKeyListener(keyAdapt);
        this.pack();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((int) (screen.getWidth() - getWidth()), (int) (screen.getHeight() - getHeight() - 30));
        window.setUndecorated(true);
        window.add(panel);
        window.setAlwaysOnTop(true);
        window.addKeyListener(keyAdapt);
        panel.addKeyListener(keyAdapt);
        window.setSize(50, 100);
        window.setLocation((int) (screen.getWidth() - window.getWidth()), (int) (screen.getHeight() - window.getHeight() - 30));
        window.setVisible(true);
    }

    public void setPosition(int x, int y) {
        mainWindow.setLocation(x, y);
    }

    public Point getPosition() {
        return mainWindow.getLocation();
    }

    public void showSettings() {
        String inputValue = JOptionPane.showInputDialog("Time for cards, in sec (is " + panel.getDelay() + "):");
        if (inputValue == null) {
            return;
        }
        try {
            int secs = Integer.parseInt(inputValue);
            CharProps.getProperties().setProperty("reminder.delay", secs + "");
            CharProps.storeProps();
            panel.setDelay(secs);
        } catch (NumberFormatException n) {
            displayMessage("Please enter a valid number.");
            showSettings();
        }
    }

    public void displayMessage(String message) {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame, message);
        frame.dispose();
    }

    private void setupSystemTray() {
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            new ReminderFrame(args[0]);
        } else {
            new ReminderFrame(null);
        }
    }
}
