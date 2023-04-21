package org.pointrel.pointrel20090201.synchronizing;

import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.pointrel.pointrel20090201.configuration.FileUtilities;

public class SimpleRemoteDirectoryRepositorySynchronizerApplication extends JFrame {

    class LoggingOutputStream extends OutputStream {

        @Override
        public void write(int b) throws IOException {
            byte data[] = { (byte) b };
            logTextArea.append(new String(data));
        }

        public void write(byte[] bytes) throws IOException {
            logTextArea.append(new String(bytes));
        }
    }

    private static final long serialVersionUID = 1L;

    private JPanel mainContentPane = null;

    private JPanel topPanel = null;

    private JLabel localRepositoryLabel = null;

    private JTextField localRepositoryPathTextField = null;

    private JLabel remoteRepositoryLabel = null;

    private JTextField remoteRepositoryPathTextField = null;

    private JButton synchronizeButton = null;

    private JScrollPane logTextAreaScrollPane = null;

    private JTextArea logTextArea = null;

    private JLabel logLabel = null;

    private JButton chooseLocalButton = null;

    private JButton chooseRemoteButton = null;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                SimpleRemoteDirectoryRepositorySynchronizerApplication thisClass = new SimpleRemoteDirectoryRepositorySynchronizerApplication();
                thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                thisClass.setVisible(true);
            }
        });
    }

    public SimpleRemoteDirectoryRepositorySynchronizerApplication() {
        super();
        initialize();
    }

    private void initialize() {
        this.setSize(735, 389);
        this.setContentPane(getMainContentPane());
        this.setTitle("Remote Directory Repository Synchronizer");
    }

    private JPanel getMainContentPane() {
        if (mainContentPane == null) {
            mainContentPane = new JPanel();
            mainContentPane.setLayout(new BorderLayout());
            mainContentPane.add(getTopPanel(), BorderLayout.NORTH);
            mainContentPane.add(getLogTextAreaScrollPane(), BorderLayout.CENTER);
        }
        return mainContentPane;
    }

    private JPanel getTopPanel() {
        if (topPanel == null) {
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.gridx = 2;
            gridBagConstraints21.gridy = 1;
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.gridx = 2;
            gridBagConstraints12.gridy = 0;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 0;
            gridBagConstraints11.gridy = 5;
            logLabel = new JLabel();
            logLabel.setText("Log");
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 1;
            gridBagConstraints8.gridy = 4;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.anchor = GridBagConstraints.EAST;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = GridBagConstraints.BOTH;
            gridBagConstraints2.gridy = 1;
            gridBagConstraints2.weightx = 1.0;
            gridBagConstraints2.gridx = 1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.anchor = GridBagConstraints.EAST;
            gridBagConstraints1.gridy = 1;
            remoteRepositoryLabel = new JLabel();
            remoteRepositoryLabel.setText("Remote Repository");
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.gridx = 1;
            localRepositoryLabel = new JLabel();
            localRepositoryLabel.setText("Local Repository");
            topPanel = new JPanel();
            topPanel.setLayout(new GridBagLayout());
            topPanel.add(localRepositoryLabel, gridBagConstraints7);
            topPanel.add(getLocalRepositoryPathTextField(), gridBagConstraints);
            topPanel.add(remoteRepositoryLabel, gridBagConstraints1);
            topPanel.add(getRemoteRepositoryPathTextField(), gridBagConstraints2);
            topPanel.add(getSynchronizeButton(), gridBagConstraints8);
            topPanel.add(logLabel, gridBagConstraints11);
            topPanel.add(getChooseLocalButton(), gridBagConstraints12);
            topPanel.add(getChooseRemoteButton(), gridBagConstraints21);
        }
        return topPanel;
    }

    private JTextField getLocalRepositoryPathTextField() {
        if (localRepositoryPathTextField == null) {
            localRepositoryPathTextField = new JTextField();
            localRepositoryPathTextField.setText("TestRepositories/TestRepository1/");
        }
        return localRepositoryPathTextField;
    }

    private JTextField getRemoteRepositoryPathTextField() {
        if (remoteRepositoryPathTextField == null) {
            remoteRepositoryPathTextField = new JTextField();
            remoteRepositoryPathTextField.setText("TestRepositories/TestRepositoryRemote/");
        }
        return remoteRepositoryPathTextField;
    }

    private JButton getChooseLocalButton() {
        if (chooseLocalButton == null) {
            chooseLocalButton = new JButton();
            chooseLocalButton.setText("Choose local...");
            chooseLocalButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    chooseLocalPressed();
                }
            });
        }
        return chooseLocalButton;
    }

    protected void chooseLocalPressed() {
        String repositoryPath = FileUtilities.ensureRepositoryPathIsValid(null, this, true, true);
        if (repositoryPath != null) this.localRepositoryPathTextField.setText(repositoryPath);
    }

    private JButton getChooseRemoteButton() {
        if (chooseRemoteButton == null) {
            chooseRemoteButton = new JButton();
            chooseRemoteButton.setText("Choose remote...");
            chooseRemoteButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    chooseRemotePressed();
                }
            });
        }
        return chooseRemoteButton;
    }

    protected void chooseRemotePressed() {
        String repositoryPath = FileUtilities.ensureRepositoryPathIsValid(null, this, true, true);
        if (repositoryPath != null) this.remoteRepositoryPathTextField.setText(repositoryPath);
    }

    private JButton getSynchronizeButton() {
        if (synchronizeButton == null) {
            synchronizeButton = new JButton();
            synchronizeButton.setText("Synchronize");
            synchronizeButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    synchronize();
                }
            });
        }
        return synchronizeButton;
    }

    protected void synchronize() {
        String localRepositoryPath = this.localRepositoryPathTextField.getText();
        String remoteRepositoryPath = this.remoteRepositoryPathTextField.getText();
        OutputStream logger = new LoggingOutputStream();
        logTextArea.setText("");
        SimpleRemoteDirectoryRepositorySynchronizer synchronizer = new SimpleRemoteDirectoryRepositorySynchronizer(localRepositoryPath, remoteRepositoryPath, logger);
        boolean result = synchronizer.synchronize();
        if (result) {
            JOptionPane.showMessageDialog(this, "Synchronization completed OK.\nResource files downloaded: " + synchronizer.remoteFilesCopiedToLocal.size() + "\nResource files uploaded: " + synchronizer.localFilesCopiedToRemote.size());
            for (String fileName : synchronizer.remoteFilesCopiedToLocal) {
                try {
                    logger.write(("File downloaded: " + fileName + "\n").getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (String fileName : synchronizer.localFilesCopiedToRemote) {
                try {
                    logger.write(("File uploaded: " + fileName + "\n").getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else JOptionPane.showMessageDialog(this, "Synchronization failed; see log for details.");
    }

    private JScrollPane getLogTextAreaScrollPane() {
        if (logTextAreaScrollPane == null) {
            logTextAreaScrollPane = new JScrollPane();
            logTextAreaScrollPane.setViewportView(getLogTextArea());
        }
        return logTextAreaScrollPane;
    }

    private JTextArea getLogTextArea() {
        if (logTextArea == null) {
            logTextArea = new JTextArea();
        }
        return logTextArea;
    }
}
