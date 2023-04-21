package org.tigr.microarray.mev.file;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.BevelBorder;
import org.tigr.microarray.mev.ISlideData;
import org.tigr.microarray.mev.ISlideMetaData;

public class SlideLoaderProgressBar extends JDialog {

    protected static final int BUFFER_SIZE = 1024 * 128;

    private int result = JOptionPane.CANCEL_OPTION;

    private ISlideData[] data;

    protected ISlideMetaData meta;

    private File[] files;

    private boolean stop = false;

    private Exception exception;

    private LoadingPanel loadingPanel = new LoadingPanel();

    private boolean fillMissingSpots = false;

    private boolean fileLoaded = false;

    /**
     * Creates a <code>SlideDataLoader</code> to load data from the
     * specified array of files.
     */
    public SlideLoaderProgressBar(JFrame frame) {
        super(frame, "Slide Data Loading");
        Listener listener = new Listener();
        JPanel btnsPanel = createBtnsPanel(listener);
        Container content = getContentPane();
        content.setLayout(new GridBagLayout());
        content.add(loadingPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        content.add(btnsPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 8, 4, 8), 0, 0));
        addWindowListener(listener);
        pack();
    }

    /**
     * Shows the dialog to load files with specified type of format.
     */
    public int showModal() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - getSize().width) / 2, (screenSize.height - getSize().height) / 2);
        show();
        return result;
    }

    /**
     * Creates a panel with 'cancel' button.
     */
    private JPanel createBtnsPanel(ActionListener listener) {
        JPanel panel = new JPanel(new BorderLayout());
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancel-command");
        cancelButton.addActionListener(listener);
        panel.add(cancelButton, BorderLayout.EAST);
        getRootPane().setDefaultButton(cancelButton);
        return panel;
    }

    /**
         * Sets max value for the 'files' progress bar.
         */
    public void setFilesCount(int count) {
        loadingPanel.setFilesCount(count);
    }

    /**
         * Sets max value for the 'file' progress bar.
         */
    public void setLinesCount(int count) {
        loadingPanel.setLinesCount(count);
    }

    /**
         * Sets current value of the 'files' progress bar.
         */
    public void setFilesProgress(int value) {
        loadingPanel.setFilesProgress(value);
        if (value == loadingPanel.getFilesCount()) {
            fileLoaded = true;
        }
    }

    /**
         * Sets current value of the 'file' progress bar.
         */
    public void setFileProgress(int value) {
        loadingPanel.setFileProgress(value);
    }

    /**
         * Sets name of a loaded file.
         */
    public void setFileName(String filename) {
        loadingPanel.setFileName(filename);
    }

    /**
         * Sets common progress description.
         */
    public void setRemain(int count) {
        loadingPanel.setRemain(count);
    }

    /**
     * The class to listen to window and an action events.
     */
    private class Listener extends WindowAdapter implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            dispose();
            fileLoaded = true;
        }

        public void windowClosing(WindowEvent e) {
            dispose();
            fileLoaded = true;
        }
    }

    /**
     * The panel to display a loading progress.
     */
    private class LoadingPanel extends JPanel {

        private JProgressBar filesProgress = new JProgressBar();

        private JProgressBar fileProgress = new JProgressBar();

        private JLabel filesLabel = new JLabel("Remain: ");

        private JLabel fileLabel = new JLabel("File: ");

        /**
         * Constructs a <code>LoadingPanel</code>.
         */
        public LoadingPanel() {
            setPreferredSize(new Dimension(350, 120));
            setBorder(new BevelBorder(BevelBorder.RAISED));
            setLayout(new GridBagLayout());
            filesProgress.setStringPainted(true);
            fileProgress.setStringPainted(true);
            add(filesLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
            add(filesProgress, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
            add(fileLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
            add(fileProgress, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
        }

        /**
         * Sets max value for the 'files' progress bar.
         */
        public void setFilesCount(int count) {
            filesProgress.setMaximum(count);
        }

        public int getFilesCount() {
            return filesProgress.getMaximum();
        }

        /**
         * Sets max value for the 'file' progress bar.
         */
        public void setLinesCount(int count) {
            fileProgress.setMaximum(count);
        }

        /**
         * Sets current value of the 'files' progress bar.
         */
        public void setFilesProgress(int value) {
            filesProgress.setValue(value);
        }

        /**
         * Sets current value of the 'file' progress bar.
         */
        public void setFileProgress(int value) {
            fileProgress.setValue(value);
        }

        /**
         * Sets name of a loaded file.
         */
        public void setFileName(String filename) {
            fileLabel.setText("File: " + filename);
        }

        /**
         * Sets common progress description.
         */
        public void setRemain(int count) {
            filesLabel.setText(String.valueOf(count) + " file(s) remaining to load.");
        }
    }

    public boolean getFileLoaded() {
        return fileLoaded;
    }
}
