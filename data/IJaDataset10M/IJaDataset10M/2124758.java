package freets.tools;

import freets.data.settings.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

/** ProgressMonitor for various use.
 *
 * @version  1.0
 * @author   W. Sauter
 */
public class FreeTsProgressMonitor extends Thread {

    protected JFrame progressFrame;

    protected JPanel progressPanel;

    protected int currentProgressValue;

    protected int newProgressValue;

    protected JProgressBar progressBar;

    protected Runnable setValue;

    protected InputStream inputStream;

    /** stop the thread. */
    protected boolean stop = false;

    protected static final String INITIAL_BUNDLE = Settings.PROPERTIES_PATH + "tools/FreeTsProgressMonitor";

    /** Resource-Bundle with default values for the progress monitor. */
    protected static ResourceBundle resources;

    /** Button to stop the progress. The user has to enable and add the 
     *  ActionListener for ths button. */
    public JButton cancelButton;

    /** The message of the progress monitor that is displayed. */
    protected JLabel label;

    /** Text area to display messages. */
    protected JTextArea messageArea;

    /** Scrollpane for the text area. */
    protected JScrollPane scrollPane;

    static {
        resources = ResourceBundle.getBundle(INITIAL_BUNDLE, Options.defaultOptions.getFreeTsLocale(), freets.tools.FreeTsClassLoader.loader);
    }

    /** Constructor for the FreeTsProgressMonitor
     *
     * @param title:       title of the Frame
     *        label:       progress monitor label
     *        inputStream: Stream that is monitored.
     *        maxValue     the maximum value of the progressbar
     */
    public FreeTsProgressMonitor(String title, JLabel label, InputStream inputStream, int maxValue) {
        this.inputStream = inputStream;
        this.label = label;
        progressPanel = new JPanel();
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        progressFrame = new JFrame(title);
        progressFrame.getContentPane().add(progressPanel, BorderLayout.CENTER);
        progressPanel.add(label);
        progressPanel.add(Box.createRigidArea(new Dimension(1, 20)));
        int maxProgressValue = maxValue;
        if (inputStream != null) {
            try {
                maxProgressValue = inputStream.available();
            } catch (Exception e) {
                LocalizedError.display(e);
            }
        }
        progressBar = new JProgressBar(0, maxProgressValue);
        progressBar.setStringPainted(true);
        label.setLabelFor(progressBar);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        progressPanel.add(getMessagePanel(), BorderLayout.CENTER);
        progressPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        currentProgressValue = progressBar.getValue();
        setValue = new Runnable() {

            public void run() {
                progressBar.setValue(currentProgressValue);
            }
        };
        cancelButton = new JButton(resources.getString("progmon.button.cancel"));
        cancelButton.setEnabled(false);
        progressFrame.getContentPane().add(cancelButton, BorderLayout.SOUTH);
        progressFrame.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        progressFrame.setLocation(screenSize.width / 2 - progressFrame.getWidth() / 2, screenSize.height / 2 - progressFrame.getHeight() / 2);
        progressFrame.show();
        progressFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        progressFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    /** Constructor for the FreeTsProgressMonitor
     *
     * @param inputStream: Stream that is monitored.
     */
    public FreeTsProgressMonitor(InputStream inputStream) {
        this(resources.getString("progmon.title"), new JLabel(resources.getString("progmon.label")), inputStream, 0);
    }

    /**
     * Default constructor for the FreeTsProgressMonitor
     */
    public FreeTsProgressMonitor(int maxValue) {
        this(resources.getString("progmon.title"), new JLabel(resources.getString("progmon.label")), null, maxValue);
    }

    /** Function to increase progress value and update the frame.
     */
    public void updateProgressBar() {
        ++currentProgressValue;
        SwingUtilities.invokeLater(setValue);
    }

    /** Function to increase progress value and update the frame.
     */
    public void updateProgressBar(int updateValue) {
        currentProgressValue = updateValue;
        SwingUtilities.invokeLater(setValue);
    }

    /** Function to dispose progress monitor frame.
     */
    public void dispose() {
        destroy();
        progressFrame.dispose();
    }

    /**
     * Cause the Thrad to stop.
     */
    public void destroy() {
        stop = true;
    }

    /**
     * Set the text of the progressbar label. 
     *
     * @param text The displyed text.
     */
    public void setText(String text) {
        label.setText(text);
    }

    /**
     * Set warning/error message.
     *
     * @param message The error/warning message.
     */
    public void setMessage(String message) {
        messageArea.append(message);
        JViewport viewport = scrollPane.getViewport();
        viewport.scrollRectToVisible(new Rectangle(20, 30, 0, messageArea.getLineCount() + messageArea.getRows()));
    }

    /**
     * Methode to set new progress value.
     */
    public void setNewProgressValue(int progressValue) {
        this.newProgressValue = progressValue;
        currentProgressValue = newProgressValue;
        updateProgressBar(currentProgressValue);
    }

    /**
     * Gets the Frame of this ProgressMonitor.
     *
     * @return the frame of this progress monitor.
     */
    public JFrame getFrame() {
        return progressFrame;
    }

    /**
     * Creates a panel where errors displayed.
     *
     * @return panel where error/message panels displayed.
     */
    public JPanel getMessagePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(resources.getString("progmon.title.message")));
        messageArea = new JTextArea(20, 30);
        messageArea.setEditable(false);
        scrollPane = new JScrollPane(messageArea);
        panel.add(scrollPane);
        return panel;
    }

    /**
     * Body of this Thread;
     */
    public void run() {
        try {
            while (!stop) {
                if (inputStream != null) {
                    newProgressValue = inputStream.available();
                    currentProgressValue = progressBar.getMaximum() - newProgressValue;
                }
                updateProgressBar(currentProgressValue);
                this.sleep(100);
            }
        } catch (IOException ioe) {
            LocalizedError.display(ioe);
        } catch (InterruptedException ie) {
            LocalizedError.display(ie);
        }
    }
}
