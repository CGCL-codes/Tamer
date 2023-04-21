package org.ourgrid.common.ui.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import org.ourgrid.common.interfaces.Constants;

public class BusyDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    public static final String EOS = "EOS";

    private JPanel mainPanel;

    private JScrollPane outputScroll;

    private JTextArea outputText;

    private JLabel pleaseWaitLabel;

    private JButton okButton;

    protected BufferedReader reader;

    public BusyDialog(Frame parentFrame) throws HeadlessException {
        this(parentFrame, null);
    }

    public BusyDialog(Frame parentFrame, PipedOutputStream out) {
        super(parentFrame, "Please wait...");
        this.setLayout(new BorderLayout());
        initComponents();
        this.setLocationRelativeTo(parentFrame);
        this.setComponentOrientation(parentFrame.getComponentOrientation());
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        pack();
        if (out != null) {
            PipedInputStream snk = new PipedInputStream();
            this.reader = new BufferedReader(new InputStreamReader(snk));
            try {
                out.connect(snk);
            } catch (IOException e) {
            }
            new Thread(new PipedStreamGobbler()).start();
        }
    }

    private class PipedStreamGobbler implements Runnable {

        public PipedStreamGobbler() {
            super();
        }

        public void run() {
            String line;
            do {
                line = null;
                try {
                    line = reader.readLine();
                } catch (IOException e) {
                }
                if (line != null && !line.equals(EOS)) {
                    addOutputText(line);
                }
            } while (line == null || !line.equals(EOS));
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }
    }

    private void initComponents() {
        mainPanel = new JPanel();
        outputScroll = new JScrollPane();
        outputText = new JTextArea();
        pleaseWaitLabel = new JLabel();
        okButton = new JButton();
        this.add(mainPanel, BorderLayout.CENTER);
        outputText.setText("");
        outputText.setColumns(30);
        outputText.setRows(30);
        outputScroll.setViewportView(outputText);
        outputScroll.setAutoscrolls(false);
        pleaseWaitLabel.setText("Please wait...");
        okButton.setText("OK");
        okButton.setEnabled(false);
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    public void addOutputText(String text) {
        outputText.setText(outputText.getText() + Constants.LINE_SEPARATOR + text);
        outputText.validate();
    }

    public void error() {
        pleaseWaitLabel.setText("Error");
        allowClose();
    }

    public void allowClose() {
        pleaseWaitLabel.setText("Done");
        okButton.setEnabled(true);
    }

    public JTextArea getTextArea() {
        return outputText;
    }
}
