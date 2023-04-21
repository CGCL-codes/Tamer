package org.drftpd.tools.installer.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import org.drftpd.tools.installer.FileLogger;
import org.drftpd.tools.installer.InstallerConfig;
import org.drftpd.tools.installer.UserFileLocator;

/**
 * @author djb61
 * @version $Id: LogWindow.java 1883 2008-02-24 19:30:17Z djb61 $
 */
public class LogWindow extends JFrame implements UserFileLocator {

    private boolean _fileLogEnabled;

    private boolean _suppressLog;

    private FileLogger _fileLog;

    private JButton _buildButton;

    private JButton _exitButton;

    private JButton _okButton;

    private JButton _selectAllButton;

    private JTextArea _logArea;

    private BufferedReader _logReader;

    private PipedInputStream _logInput;

    public LogWindow(PipedInputStream logInput, JButton buildButton, JButton selectAllButton, JButton exitButton, InstallerConfig config) {
        super("Build Log");
        _fileLogEnabled = config.getFileLogging();
        _suppressLog = config.getSuppressLog();
        _logInput = logInput;
        _buildButton = buildButton;
        _selectAllButton = selectAllButton;
        _exitButton = exitButton;
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        JPanel centerPanel = new JPanel();
        BorderLayout centerLayout = new BorderLayout();
        centerPanel.setLayout(centerLayout);
        _logArea = new JTextArea();
        _logArea.setLineWrap(true);
        _logArea.setEditable(false);
        if (_suppressLog) {
            _logArea.setText("LOGGING SUPPRESSED");
        } else {
            _logArea.setText("");
        }
        JScrollPane logPane = new JScrollPane(_logArea);
        logPane.setBorder(new TitledBorder(new EtchedBorder(), "Plugin Build Log"));
        centerPanel.add(logPane, BorderLayout.CENTER);
        contentPane.add(centerPanel, BorderLayout.CENTER);
        JPanel southPanel = new JPanel();
        FlowLayout southLayout = new FlowLayout(FlowLayout.CENTER);
        southPanel.setLayout(southLayout);
        _okButton = new JButton();
        _okButton.setText("OK");
        _okButton.setEnabled(false);
        _okButton.setPreferredSize(new Dimension(100, 25));
        _okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });
        southPanel.add(_okButton);
        contentPane.add(southPanel, BorderLayout.SOUTH);
        setSize(500, 400);
        validate();
        setVisible(true);
    }

    public void init() throws IOException {
        if (_fileLogEnabled) {
            _fileLog = new FileLogger();
            _fileLog.init();
        }
        _logReader = new BufferedReader(new InputStreamReader(_logInput));
        _buildButton.setEnabled(false);
        _selectAllButton.setEnabled(false);
        _exitButton.setEnabled(false);
        new Thread(new ReadingThread()).start();
    }

    public String getUserDir() {
        JFileChooser userFileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
        userFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        userFileChooser.setDialogTitle("Select directory DrFTPd 2.0 is installed in");
        int result = userFileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            return userFileChooser.getSelectedFile().getPath();
        } else {
            return null;
        }
    }

    private class ReadingThread implements Runnable {

        public void run() {
            try {
                String logLine = null;
                do {
                    logLine = _logReader.readLine();
                    if (logLine != null) {
                        if (_fileLogEnabled) {
                            _fileLog.writeLog(logLine);
                        }
                        if (!_suppressLog) {
                            _logArea.append(logLine + "\n");
                            _logArea.setCaretPosition(_logArea.getText().length());
                        }
                    }
                } while (logLine != null);
            } catch (Exception e) {
            } finally {
                if (_fileLogEnabled) {
                    _fileLog.cleanup();
                }
                try {
                    _logReader.close();
                } catch (IOException e) {
                }
                try {
                    _logInput.close();
                } catch (IOException e) {
                }
                _okButton.setEnabled(true);
                _buildButton.setEnabled(true);
                _selectAllButton.setEnabled(true);
                _exitButton.setEnabled(true);
            }
        }
    }
}
