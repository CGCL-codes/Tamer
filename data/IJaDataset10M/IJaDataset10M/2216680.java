package com.rapidminer.gui.tools;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A (modal) progress monitor dialog which is also able to show state texts and
 * also provides an interemediate mode.
 *
 * @author Santhosh Kumar, Ingo Mierswa
 */
public class ProgressDialog extends JDialog implements ChangeListener {

    private static final long serialVersionUID = -8792339176006884719L;

    private JLabel statusLabel = new JLabel();

    private JProgressBar progressBar;

    private transient ProgressMonitor monitor;

    public ProgressDialog(Frame owner, String title, ProgressMonitor monitor, boolean modal) throws HeadlessException {
        super(owner, title, modal);
        init(monitor);
    }

    public ProgressDialog(Dialog owner, String title, ProgressMonitor monitor, boolean modal) throws HeadlessException {
        super(owner, title, modal);
        init(monitor);
    }

    private void init(ProgressMonitor monitor) {
        this.monitor = monitor;
        progressBar = new JProgressBar(0, monitor.getTotal());
        if (monitor.isIndeterminate()) {
            progressBar.setIndeterminate(true);
        } else {
            progressBar.setValue(monitor.getCurrent());
        }
        statusLabel.setText(monitor.getStatus());
        JPanel contents = (JPanel) getContentPane();
        contents.setBorder(BorderFactory.createEmptyBorder(11, 11, 11, 11));
        contents.add(statusLabel, BorderLayout.NORTH);
        contents.add(progressBar);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        monitor.addChangeListener(this);
    }

    public void stateChanged(final ChangeEvent ce) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    stateChanged(ce);
                }
            });
            return;
        }
        statusLabel.setText(monitor.getStatus());
        if (!monitor.isIndeterminate()) progressBar.setValue(monitor.getCurrent());
        if (monitor.getCurrent() >= monitor.getTotal()) {
            dispose();
        }
    }
}
