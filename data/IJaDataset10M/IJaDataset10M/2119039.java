package org.micthemodel.gui.windows;

import org.micthemodel.factory.Parameters;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author sbishnoi
 */
public class JInternalFrameProcessMonitor extends JInternalFrame {

    JPanel mainPanel;

    JScrollPane mainScrollPane;

    Process process;

    String inputFilePath;

    JLabel jLabelProcessStatus;

    boolean showingOutput = false;

    String outFile;

    URL outputURL;

    JEditorPane outputPane;

    public JInternalFrameProcessMonitor(Process process, String inputFilePath, String outFile) {
        this.process = process;
        this.inputFilePath = inputFilePath;
        this.outFile = outFile;
        this.initComponents();
        this.createView();
        this.pack();
        this.monitor();
    }

    private void initComponents() {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.mainPanel = new JPanel();
        this.mainScrollPane = new JScrollPane(this.mainPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.setContentPane(mainPanel);
        this.setTitle("Process Monitor");
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setResizable(true);
        this.setClosable(true);
    }

    private void createView() {
        this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
        JLabel processLabel = new JLabel("Process for file:" + this.inputFilePath);
        this.mainPanel.add(processLabel);
        JLabel timeStartedLabel = new JLabel("Started at " + Parameters.whatTimeIsIt());
        this.mainPanel.add(timeStartedLabel);
        this.jLabelProcessStatus = new JLabel("Process running...");
        this.mainPanel.add(jLabelProcessStatus);
        JButton jButtonKill = new JButton("Kill process");
        jButtonKill.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                killProcess();
            }
        });
        this.mainPanel.add(jButtonKill);
        JButton jButtonShow = new JButton("Show/Update output");
        jButtonShow.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showOutput();
            }
        });
        this.mainPanel.add(jButtonShow);
    }

    private void killProcess() {
        try {
            this.process.exitValue();
            JOptionPane.showMessageDialog(this, "This process has already terminated.");
        } catch (IllegalThreadStateException ex) {
            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to kill this process?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                this.process.destroy();
            }
        }
    }

    private void showOutput() {
        if (!this.showingOutput) {
            try {
                this.outputURL = new File(this.outFile).toURI().toURL();
                this.outputPane = new JEditorPane(this.outputURL);
            } catch (MalformedURLException ex) {
                this.outputPane = new JEditorPane("txt", "Cannot find the output file.");
            } catch (IOException ex) {
                this.outputPane = new JEditorPane("txt", "Cannot find the output file.");
            }
            this.outputPane.setEditable(false);
            JScrollPane outputScrollPane = new JScrollPane(this.outputPane);
            outputScrollPane.setPreferredSize(new Dimension(300, 200));
            this.mainPanel.add(outputScrollPane);
            this.pack();
            this.showingOutput = true;
        }
        if (this.outputURL != null) {
            try {
                this.outputPane.read(new FileInputStream(this.outFile), this);
            } catch (IOException ex) {
                this.outputPane.setText("Cannot read the output file.");
            }
        }
    }

    private void monitor() {
        Thread monitorThread = new Thread(new MonitorRunnable());
        monitorThread.start();
    }

    private class MonitorRunnable implements Runnable {

        @Override
        public void run() {
            try {
                Parameters.getOut().println("New process started at " + Parameters.whatTimeIsIt() + ". Started monitoring.");
                process.waitFor();
                int status = process.exitValue();
                if (status == 0) {
                    jLabelProcessStatus.setText("Process completed at " + Parameters.whatTimeIsIt());
                } else {
                    jLabelProcessStatus.setText("Process exited with value " + status + " at " + Parameters.whatTimeIsIt());
                }
            } catch (InterruptedException ex) {
                System.out.println("monitoring terminated!");
            }
        }
    }
}
