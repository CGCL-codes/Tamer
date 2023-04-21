package de.proteinms.xtandemparser.viewer;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * This dialog show information about the progress of the file parsing.
 *
 * @author Thilo Muth
 */
public class ProgressDialog extends JDialog {

    /**
     * Holds an instance of the progress bar.
     */
    private JProgressBar progressBar;

    /**
     * Constructor for a progress dialog with a frame as parent.
     *
     * @param aParent The parent frame.
     */
    public ProgressDialog(Frame aParent) {
        super(aParent);
        initComponents();
        setLocationRelativeTo(aParent);
    }

    /**
     * Initializes the components for the progress dialog.
     */
    private void initComponents() {
        progressBar = new JProgressBar();
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Please Wait...");
        setResizable(false);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent evt) {
                closeDialog(evt);
            }
        });
        progressBar.setFont(progressBar.getFont().deriveFont(progressBar.getFont().getSize() - 1f));
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(progressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        pack();
    }

    /**
     * Sets the progress bar value.
     *
     * @param value
     */
    public void setValue(final int value) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                progressBar.setValue(value);
            }
        });
    }

    /**
     * Sets the maximum value of the progress bar
     *
     * @param maxvalue
     */
    public void setMaxValue(final int maxvalue) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                progressBar.setMaximum(maxvalue);
            }
        });
    }

    /**
     * Sets the dialog indeterminate.
     *
     * @param indeterminate
     */
    public void setIndeterminate(final boolean indeterminate) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                progressBar.setStringPainted(!indeterminate);
                progressBar.setIndeterminate(indeterminate);
            }
        });
    }

    /**
     * Sets the filename string in the progressbar.
     *
     * @param fileName
     */
    public void setFileName(final String fileName) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                progressBar.setStringPainted(fileName != null);
                progressBar.setString(fileName);
            }
        });
    }

    /**
     * This method closes the dialog, if possible.
     *
     * @param event
     */
    private void closeDialog(WindowEvent event) {
        int option = JOptionPane.showConfirmDialog(this.getParent(), "Closing the progress bar will close XTandem Viewer.\n" + "Do you still want to close the progress bar?", "Close XTandem Viewer?", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
