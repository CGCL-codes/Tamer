package client;

import java.awt.*;
import javax.swing.*;
import common.AccountInfo;

/**
 * Panel displaying the user's disc quota.
 * 
 * @author John �sterlund
 * @version 0.1 2006-05-22
 */
public class QuotaPanel extends JPanel {

    private static final long serialVersionUID = -2961130234594798104L;

    private JProgressBar progressBar;

    private JLabel msg;

    private JLabel label;

    private GUI gui;

    private AccountInfo info;

    private int percent;

    /**
     * Constructs a quota panel.
     * 
     * @param gui Parent GUI
     */
    public QuotaPanel(GUI gui) {
        super(new BorderLayout(0, 0));
        this.gui = gui;
        label = new JLabel("Quota:");
        msg = new JLabel();
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(progressBar);
        panel.add(msg);
        add(panel, BorderLayout.EAST);
    }

    /**
     * Clear the quota panel. Used when disconneting.
     */
    public void clear() {
        progressBar.setValue(progressBar.getMinimum());
        msg.setText("");
    }

    /**
     * Updates the quota panel.
     */
    public void updateQuota() {
        info = gui.getAccountInfo();
        percent = (int) (((float) info.getUsedSpace() / (float) info.getQuota()) * 100);
        progressBar.setValue(percent);
        long free = info.getQuota() - info.getUsedSpace();
        String length = "0";
        if (free < 1024) length = String.valueOf(free) + " B"; else if (free < 1048576) length = String.valueOf(free / 1024) + " KiB"; else length = String.valueOf(free / 1048576) + " MiB";
        msg.setText("Free: " + length);
    }
}
