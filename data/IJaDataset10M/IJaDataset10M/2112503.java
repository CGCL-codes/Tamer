package be.vds.jtb.taskmanager.view;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

/**
 * This panel is a representation of a task that is currently running.
 * 
 * @author gautier vanderslyen
 * @date 22-mars-08
 */
public class RunningTaskPanel extends JPanel {

    private JProgressBar progressBar;

    private JLabel messageLabel;

    public RunningTaskPanel() {
        init();
    }

    private void init() {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.setBorder(new EmptyBorder(10, 5, 10, 5));
        this.add(createLabel(), BorderLayout.SOUTH);
        this.add(createProgressBar(), BorderLayout.CENTER);
    }

    private JComponent createLabel() {
        messageLabel = new JLabel();
        return messageLabel;
    }

    private JComponent createProgressBar() {
        progressBar = new JProgressBar();
        progressBar.setMaximum(100);
        return progressBar;
    }

    public void setMaxProgress(int total) {
        progressBar.setMaximum(total);
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setToolTipText(message);
    }

    public void setProgress(int progress) {
        progressBar.setValue(progress);
    }
}
