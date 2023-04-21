package de.mindcrimeilab.swing.util;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

/**
 * Extended SwingWorker providing a progress dialog.
 * 
 * @author Michael Engelhardt<me@mindcrime-ilab.de>
 * @author $Author:me $
 * @version $Revision:62 $
 * 
 */
public abstract class VisualProgressWorker<T, V> extends SwingWorker<T, V> {

    private final InternalProgressDialog dialog;

    private volatile boolean stopWorker;

    public VisualProgressWorker(Component component) {
        super();
        dialog = new InternalProgressDialog(SwingUtilities.windowForComponent(component));
        stopWorker = false;
    }

    public void setStopWorker(boolean stop) {
        stopWorker = stop;
    }

    public boolean hasStopWorker() {
        return stopWorker;
    }

    public void showProgressDialog() {
        dialog.setLocationRelativeTo(dialog.getParent());
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.pack();
        dialog.setVisible(true);
        dialog.toFront();
        dialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    public void hideProgressDialog() {
        dialog.setVisible(false);
        dialog.dispose();
        dialog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void setDetailMessage(String message) {
        dialog.setDetailMessage(message);
        dialog.pack();
    }

    public void setActionText(String text) {
        dialog.setActionText(text);
        dialog.pack();
    }

    public void setTitle(String title) {
        dialog.setTitle(title);
        dialog.pack();
    }

    @Override
    protected void done() {
        super.done();
        if (dialog.isVisible()) {
            this.hideProgressDialog();
        }
    }
}

/**
 * Internal view class
 * 
 * @author Michael Engelhardt<me@mindcrime-ilab.de>
 * @author $Author:me $
 * @version $Revision:62 $
 * 
 */
final class InternalProgressDialog extends JDialog {

    /**
     * serial version uid
     */
    private static final long serialVersionUID = -418787593671991025L;

    private JProgressBar jpbProgessBar = null;

    private JLabel jlDetailMessage = null;

    private JLabel jlAction = null;

    public InternalProgressDialog(Window owner) {
        super(owner);
        initializeGui();
    }

    public void setDetailMessage(String name) {
        getJlDetailMessage().setText(name);
    }

    public void setActionText(String text) {
        getJlAction().setText(text);
    }

    private void initializeGui() {
        setTitle("Operation in progress...");
        setModal(false);
        setResizable(false);
        final JComponent contentPane = (JComponent) getContentPane();
        contentPane.setLayout(new GridBagLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        addActionLabelTo(contentPane);
        addActionTextTo(contentPane);
        addLabelProgressTo(contentPane);
        addProgressBarTo(contentPane);
        addDetailMessageTo(contentPane);
        pack();
        getJlAction().setText("");
        getJlDetailMessage().setText("");
    }

    private void addActionLabelTo(JComponent component) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 5, 10);
        component.add(new JLabel("Action:"), gbc);
    }

    private void addActionTextTo(JComponent component) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 5, 10);
        component.add(getJlAction(), gbc);
    }

    private void addLabelProgressTo(JComponent component) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 5, 0);
        component.add(new JLabel("Progress:"), gbc);
    }

    private void addProgressBarTo(JComponent component) {
        GridBagConstraints gbcJpbProgressBar = new GridBagConstraints();
        gbcJpbProgressBar.anchor = GridBagConstraints.WEST;
        gbcJpbProgressBar.gridx = 1;
        gbcJpbProgressBar.gridy = 1;
        gbcJpbProgressBar.gridwidth = 1;
        gbcJpbProgressBar.gridheight = 1;
        gbcJpbProgressBar.fill = GridBagConstraints.BOTH;
        gbcJpbProgressBar.weightx = 1.0;
        gbcJpbProgressBar.weighty = 1.0;
        gbcJpbProgressBar.insets = new Insets(0, 0, 5, 0);
        component.add(getJpbProgressBar(), gbcJpbProgressBar);
    }

    private void addDetailMessageTo(JComponent component) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 5, 10);
        component.add(getJlDetailMessage(), gbc);
    }

    private JProgressBar getJpbProgressBar() {
        if (null == jpbProgessBar) {
            jpbProgessBar = new JProgressBar(SwingConstants.HORIZONTAL);
            jpbProgessBar.setIndeterminate(true);
        }
        return jpbProgessBar;
    }

    private JLabel getJlDetailMessage() {
        if (null == jlDetailMessage) {
            jlDetailMessage = new JLabel("X");
        }
        return jlDetailMessage;
    }

    private JLabel getJlAction() {
        if (null == jlAction) {
            jlAction = new JLabel("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        }
        return jlAction;
    }
}
