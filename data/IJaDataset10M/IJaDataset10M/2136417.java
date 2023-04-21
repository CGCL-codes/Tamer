package com.jes.classfinder.gui.window.splashwindow;

import com.jes.classfinder.gui.internationalization.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.util.Date;
import javax.swing.JWindow;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import com.jes.classfinder.gui.panel.splash.SplashPanel;
import com.jes.classfinder.listeners.progress.CancelListener;
import com.jes.classfinder.listeners.progress.ProgressEvent;
import com.jes.classfinder.listeners.progress.ProgressListener;

/**
 * @author John Dickerson
 *
 */
public class SplashWindow extends JWindow implements ProgressListener {

    private StringBuffer messageSB;

    private SplashPanel splashPanel;

    private Color mainColor;

    private Color borderColor;

    protected Logger logger = Logger.getLogger(SplashWindow.class);

    public SplashWindow(Frame frame, Color mainColor, Color borderColor) {
        super(frame);
        Date dateBefore = new Date();
        this.mainColor = mainColor;
        this.borderColor = borderColor;
        initialize();
        Date dateAfter = new Date();
        long timeTakeToInitialize = dateAfter.getTime() - dateBefore.getTime();
        logger.debug("Splash Window initialized in " + timeTakeToInitialize + " milliseconds");
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Color getMainColor() {
        return mainColor;
    }

    public void setMainColor(Color mainColor) {
        this.mainColor = mainColor;
    }

    private void initialize() {
        Container contentPane = this.getContentPane();
        splashPanel = createSplashPanel();
        contentPane.add(BorderLayout.CENTER, splashPanel);
        messageSB = new StringBuffer();
    }

    private SplashPanel createSplashPanel() {
        SplashPanel splashPanel = new SplashPanel(this);
        setSizeSplashWindow();
        return splashPanel;
    }

    private void setSizeSplashWindow() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSize.width / 2, screenSize.height / 2);
        this.setLocation(screenSize.width / 2 - screenSize.width / 4, screenSize.height / 2 - screenSize.height / 4);
    }

    public void display() {
        this.setVisible(true);
    }

    private String formatMessageToAdd(String messageToAdd) {
        int maxLengthBeforePerformingLineBreak = 60;
        int length = messageToAdd.length();
        String textUpToSpace;
        String messageToReturn = messageToAdd;
        if (length > maxLengthBeforePerformingLineBreak) {
            int indexOfSpace;
            int fromIndex = 0;
            while ((indexOfSpace = messageToAdd.indexOf(" ", fromIndex)) != -1) {
                fromIndex = indexOfSpace + 1;
                if (indexOfSpace > maxLengthBeforePerformingLineBreak) {
                    textUpToSpace = messageToAdd.substring(0, indexOfSpace);
                    messageToReturn = messageToAdd.replaceAll(textUpToSpace + " ", textUpToSpace + "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                    break;
                }
            }
        }
        return messageToReturn;
    }

    private String buildMessage(String messageToAdd) {
        String formattedMessageToAdd = formatMessageToAdd(messageToAdd);
        messageSB.append("<br><br>......");
        messageSB.append("<font size='3'>");
        messageSB.append(formattedMessageToAdd);
        messageSB.append("</font>");
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        sb.append("<body>");
        sb.append(messageSB.toString());
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

    private String buildErrorMessage(String messageToAdd) {
        messageSB.append("<br><br>......");
        messageSB.append("<font color='red' size='5'>");
        messageSB.append(messageToAdd);
        messageSB.append("</font>");
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        sb.append("<body>");
        sb.append(messageSB.toString());
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

    private void displayMessage(ProgressEvent progressEvent) {
        String messageToAdd = progressEvent.getMessage();
        String message = buildMessage(messageToAdd);
        splashPanel.displayMessage(message);
    }

    public void addCancelListener(CancelListener cancelListener) {
    }

    public void cancel() {
    }

    public void displayMessage(final String message) {
        if (!EventQueue.isDispatchThread()) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    String messageToAdd = buildMessage(message);
                    splashPanel.displayMessage(messageToAdd);
                }
            });
        } else {
            String messageToAdd = buildMessage(message);
            splashPanel.displayMessage(messageToAdd);
        }
    }

    public void displayErrorMessage(final String message) {
        if (!EventQueue.isDispatchThread()) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    String messageToAdd = buildErrorMessage(message);
                    splashPanel.displayMessage(messageToAdd);
                }
            });
        } else {
            String messageToAdd = buildMessage(message);
            splashPanel.displayMessage(messageToAdd);
        }
    }

    public void progressPerformed(ProgressEvent progressEvent) {
        final ProgressEvent progressEventFinal = progressEvent;
        if (!EventQueue.isDispatchThread()) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    displayMessage(progressEventFinal);
                }
            });
        } else {
            displayMessage(progressEvent);
        }
    }

    public void closeSplashWindow() {
        setVisible(false);
        dispose();
    }

    public void scrollToEnd() {
        splashPanel.scrollToEnd();
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
    }
}
