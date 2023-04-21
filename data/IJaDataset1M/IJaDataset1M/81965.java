package com.frinika.swing;

import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * Dialog for monitoring task progress while it's running
 * @author Peter Johan Salomonsen
 *
 */
public class ProgressBarDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    JProgressBar progressBar;

    public ProgressBarDialog(JFrame frame, String labelText, int completeCount) {
        super(frame, true);
        this.setResizable(false);
        this.setUndecorated(true);
        this.setAlwaysOnTop(true);
        try {
            progressBar = new JProgressBar(0, completeCount);
            progressBar.setStringPainted(true);
            setLayout(new GridLayout(0, 1));
            JLabel lb = new JLabel(labelText);
            lb.setFont(new Font(lb.getFont().getName(), Font.BOLD, lb.getFont().getSize() * 2));
            add(lb);
            add(progressBar);
            this.setSize(getPreferredSize());
            this.setLocationRelativeTo(frame);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setString(String str) {
        progressBar.setString(str);
    }

    public void setProgressValue(int value) {
        progressBar.setValue(value);
    }
}
