package de.fzi.injectj.frontend.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import de.fzi.injectj.backend.ProgressEvent;
import de.fzi.injectj.backend.ProgressListener;
import de.fzi.injectj.language.CodeMapper;

/** A progress indicator. This window displays a progress bar and a short message.
  *
  * @author Volker Kuttruff
  * @author Sven Luzar*/
public class DlgProgress extends JDialog implements ProgressListener {

    JPanel panel = new JPanel();

    JProgressBar progressBar = new JProgressBar();

    JLabel messageLabel = new JLabel();

    String title;

    public DlgProgress(JFrame parent, String title, boolean modal) {
        this(parent, title, modal, false);
    }

    public DlgProgress(JFrame parent, String title, boolean modal, boolean withoutProgressBar) {
        super(parent, title, modal);
        try {
            jbInit();
            if (withoutProgressBar) progressBar.setVisible(false);
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public DlgProgress() {
        this(false);
    }

    public DlgProgress(boolean withoutProgressBar) {
        this(null, null, false, withoutProgressBar);
    }

    public static void main(String[] args) {
        DlgProgress progressFrame = new DlgProgress();
        progressFrame.setVisible(true);
    }

    /** Sets the message text.
    *
    * @param message the message text to display
    */
    public void setMessageText(String message) {
        messageLabel.setText(message);
        show();
    }

    /** Sets the progress bar to the given percentage. The value
    * must be within 0 and 100.
    *
    * @param percantage percanatage displayed in progress bar
    */
    public void setProgressBar(int percentage) {
        if (percentage < 0 | percentage > 100) return;
        progressBar.setValue(percentage);
    }

    /** Sets the progress bar to the given percentage. The value
    * must be within 0.0 and 100.0.
    *
    * @param percantage percanatage displayed in progress bar
    */
    public void setProgressBar(float percentage) {
        setProgressBar((int) percentage);
    }

    /** Sets the progress bar to the given percentage. The value
    * must be within 0.0 and 100.0.
    *
    * @param percantage percanatage displayed in progress bar
    */
    public void setProgressBar(double percentage) {
        setProgressBar((int) percentage);
    }

    private void jbInit() throws Exception {
        int maxWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        this.setTitle(CodeMapper.getText("PROGRESSFRAME_TITLE"));
        panel.setPreferredSize(new Dimension(400, 80));
        panel.setLayout(new BorderLayout(0, 5));
        JPanel barPanel = new JPanel();
        progressBar.setPreferredSize(new Dimension(370, 20));
        barPanel.add(progressBar);
        panel.add(barPanel, BorderLayout.CENTER);
        panel.add(messageLabel, BorderLayout.NORTH);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.getContentPane().add(panel);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = panel.getPreferredSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    /**  Invoked when an event occurs.
   *   @param e The event
   */
    public void progressPerformed(ProgressEvent e) {
        if ((e.getType() & ProgressEvent.OPEN) == ProgressEvent.OPEN) this.setVisible(true);
        messageLabel.setText(e.getMessage());
        setProgressBar(e.getProgress());
        if ((e.getType() & ProgressEvent.CLOSE) == ProgressEvent.CLOSE) this.setVisible(false);
    }
}
