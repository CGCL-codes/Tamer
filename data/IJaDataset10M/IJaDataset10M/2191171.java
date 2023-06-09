package jmri.jmrix;

import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.text.*;
import jmri.util.JmriJFrame;

/**
 * Abstact base class for Frames displaying communications monitor information
 * @author	Bob Jacobsen   Copyright (C) 2001, 2003
 * @version	$Revision: 1.25 $
 */
public abstract class AbstractMonFrame extends JmriJFrame {

    protected abstract String title();

    /**
     * Initialize the data source.
     * <P>
     * This is invoked at the end of the GUI initialization phase.
     * Subclass implementations should connect to their data source here.
     */
    protected abstract void init();

    public void dispose() {
        super.dispose();
    }

    protected JButton clearButton = new JButton();

    protected JToggleButton freezeButton = new JToggleButton();

    protected JScrollPane jScrollPane1 = new JScrollPane();

    protected JTextArea monTextPane = new JTextArea();

    protected JButton startLogButton = new JButton();

    protected JButton stopLogButton = new JButton();

    protected JCheckBox rawCheckBox = new JCheckBox();

    protected JCheckBox timeCheckBox = new JCheckBox();

    protected JButton openFileChooserButton = new JButton();

    protected JTextField entryField = new JTextField();

    protected JButton enterButton = new JButton();

    AbstractMonFrame self;

    final javax.swing.JFileChooser logFileChooser = new JFileChooser(jmri.jmrit.XmlFile.userFileLocationDefault());

    public AbstractMonFrame() {
        super();
        self = this;
    }

    public void initComponents() throws Exception {
        clearButton.setText("Clear screen");
        clearButton.setVisible(true);
        clearButton.setToolTipText("Clear monitoring history");
        freezeButton.setText("Freeze screen");
        freezeButton.setVisible(true);
        freezeButton.setToolTipText("Stop display scrolling");
        enterButton.setText("Add Message");
        enterButton.setVisible(true);
        enterButton.setToolTipText("Add a text message to the log");
        monTextPane.setVisible(true);
        monTextPane.setToolTipText("Command and reply monitoring information appears here");
        monTextPane.setEditable(false);
        entryField.setToolTipText("Enter text here, then click button to include it in log");
        JTextField t = new JTextField(80);
        int x = jScrollPane1.getPreferredSize().width + t.getPreferredSize().width;
        int y = jScrollPane1.getPreferredSize().height + 10 * t.getPreferredSize().height;
        jScrollPane1.getViewport().add(monTextPane);
        jScrollPane1.setPreferredSize(new Dimension(x, y));
        jScrollPane1.setVisible(true);
        startLogButton.setText("Start logging");
        startLogButton.setVisible(true);
        startLogButton.setToolTipText("start logging to file");
        stopLogButton.setText("Stop logging");
        stopLogButton.setVisible(true);
        stopLogButton.setToolTipText("Stop logging to file");
        rawCheckBox.setText("Show raw data");
        rawCheckBox.setVisible(true);
        rawCheckBox.setToolTipText("If checked, show the raw traffic in hex");
        timeCheckBox.setText("Show timestamps");
        timeCheckBox.setVisible(true);
        timeCheckBox.setToolTipText("If checked, show timestamps before each message");
        openFileChooserButton.setText("Choose log file");
        openFileChooserButton.setVisible(true);
        openFileChooserButton.setToolTipText("Click here to select a new output log file");
        setTitle(title());
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(jScrollPane1);
        JPanel paneA = new JPanel();
        paneA.setLayout(new BoxLayout(paneA, BoxLayout.Y_AXIS));
        JPanel pane1 = new JPanel();
        pane1.setLayout(new BoxLayout(pane1, BoxLayout.X_AXIS));
        pane1.add(clearButton);
        pane1.add(freezeButton);
        pane1.add(rawCheckBox);
        pane1.add(timeCheckBox);
        paneA.add(pane1);
        JPanel pane2 = new JPanel();
        pane2.setLayout(new BoxLayout(pane2, BoxLayout.X_AXIS));
        pane2.add(openFileChooserButton);
        pane2.add(startLogButton);
        pane2.add(stopLogButton);
        paneA.add(pane2);
        JPanel pane3 = new JPanel();
        pane3.setLayout(new BoxLayout(pane3, BoxLayout.X_AXIS));
        pane3.add(enterButton);
        pane3.add(entryField);
        paneA.add(pane3);
        getContentPane().add(paneA);
        clearButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                clearButtonActionPerformed(e);
            }
        });
        startLogButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                startLogButtonActionPerformed(e);
            }
        });
        stopLogButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                stopLogButtonActionPerformed(e);
            }
        });
        openFileChooserButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                openFileChooserButtonActionPerformed(e);
            }
        });
        enterButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                enterButtonActionPerformed(e);
            }
        });
        logFileChooser.setSelectedFile(new File("monitorLog.txt"));
        init();
        addHelpMenu();
        pack();
        paneA.setMaximumSize(paneA.getSize());
        pack();
    }

    /**
     * Define help menu for this window.
     * <p>
     * By default, provides a generic help page
     * that covers general features.  Specific
     * implementations can override this to 
     * show their own help page if desired.
     */
    protected void addHelpMenu() {
        addHelpMenu("package.jmri.jmrix.AbstractMonFrame", true);
    }

    public void nextLine(String line, String raw) {
        StringBuffer sb = new StringBuffer(120);
        if (timeCheckBox.isSelected()) {
            sb.append(df.format(new Date())).append(": ");
        }
        if (rawCheckBox.isSelected()) {
            sb.append('[').append(raw).append("]  ");
        }
        sb.append(line);
        synchronized (self) {
            linesBuffer.append(sb.toString());
        }
        if (!freezeButton.isSelected()) {
            Runnable r = new Runnable() {

                public void run() {
                    synchronized (self) {
                        monTextPane.append(linesBuffer.toString());
                        int LineCount = monTextPane.getLineCount();
                        if (LineCount > MAX_LINES) {
                            LineCount -= MAX_LINES;
                            try {
                                int offset = monTextPane.getLineStartOffset(LineCount);
                                monTextPane.getDocument().remove(0, offset);
                            } catch (BadLocationException ex) {
                            }
                        }
                        linesBuffer.setLength(0);
                    }
                }
            };
            javax.swing.SwingUtilities.invokeLater(r);
        }
        if (logStream != null) {
            synchronized (logStream) {
                String logLine = sb.toString();
                if (!newline.equals("\n")) {
                    int i = 0;
                    int lim = sb.length();
                    StringBuffer out = new StringBuffer(sb.length() + 10);
                    for (i = 0; i < lim; i++) {
                        if (sb.charAt(i) == '\n') out.append(newline); else out.append(sb.charAt(i));
                    }
                    logLine = out.toString();
                }
                logStream.print(logLine);
            }
        }
    }

    String newline = System.getProperty("line.separator");

    public synchronized void clearButtonActionPerformed(java.awt.event.ActionEvent e) {
        synchronized (linesBuffer) {
            linesBuffer.setLength(0);
            monTextPane.setText("");
        }
    }

    public synchronized void startLogButtonActionPerformed(java.awt.event.ActionEvent e) {
        if (logStream == null) {
            try {
                logStream = new PrintStream(new FileOutputStream(logFileChooser.getSelectedFile()));
            } catch (Exception ex) {
                log.error("exception " + ex);
            }
        }
    }

    public synchronized void stopLogButtonActionPerformed(java.awt.event.ActionEvent e) {
        if (logStream != null) {
            synchronized (logStream) {
                logStream.flush();
                logStream.close();
            }
            logStream = null;
        }
    }

    public void openFileChooserButtonActionPerformed(java.awt.event.ActionEvent e) {
        int retVal = logFileChooser.showSaveDialog(this);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            boolean loggingNow = (logStream != null);
            stopLogButtonActionPerformed(e);
            if (loggingNow) startLogButtonActionPerformed(e);
        }
    }

    public void enterButtonActionPerformed(java.awt.event.ActionEvent e) {
        nextLine(entryField.getText() + "\n", "");
    }

    public synchronized String getFrameText() {
        return linesBuffer.toString();
    }

    volatile PrintStream logStream = null;

    DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");

    StringBuffer linesBuffer = new StringBuffer();

    private static int MAX_LINES = 500;

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AbstractMonFrame.class.getName());
}
