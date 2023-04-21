package com.citrix.ditaOtGui.gui;

import com.citrix.ditaOtGui.misc.FileReaderWriter;
import com.citrix.ditaOtGui.misc.ErrorMsg;
import com.citrix.ditaOtGui.misc.MyDate;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

public class CommandRunner extends JFrame implements ActionListener {

    private JProgressBar progressBar;

    private Task task;

    private String strCmd;

    private String strFileSep;

    private DitaOtGuiWrapper wrapper;

    private String strLogText = "";

    private String strCmdText = "";

    private boolean isCanceled;

    private File log;

    private JLabel lblDate, lblInfo;

    private JButton btnLog, btnCnacel, btnClose, btnOPFolder;

    private JPanel pnlTop, pnlCenter, pnlBottom;

    public CommandRunner(DitaOtGuiWrapper wrapper, String str) {
        super("Process Status");
        strCmd = str;
        this.wrapper = wrapper;
        wrapper.setVisible(false);
        isCanceled = false;
        strFileSep = System.getProperty("file.separator");
        log = new File("." + strFileSep + "GUI" + strFileSep + "log" + strFileSep + "log.txt");
        progressBar = new JProgressBar(0, 100);
        progressBar.setIndeterminate(true);
        lblDate = new JLabel(new MyDate().getDate(), JLabel.RIGHT);
        lblDate.setForeground(Color.blue);
        lblInfo = new JLabel("<html>" + "<font color=\"#008000\">" + "<b>" + "Starting Process..." + "</b>" + "</font>" + "</html>", JLabel.LEFT);
        btnLog = new JButton("Log File");
        btnLog.setEnabled(false);
        btnLog.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                new LogFile().showText();
            }
        });
        btnOPFolder = new JButton("Output Folder");
        btnOPFolder.setEnabled(false);
        btnOPFolder.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                try {
                    String dir = DitaOtGuiWrapper.outputDir;
                    Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL \"" + dir + "\"");
                } catch (Exception exception) {
                    new ErrorMsg().showErrorMsg("Following error occurred while opening the output folder.\n" + exception.getMessage());
                }
            }
        });
        btnCnacel = new JButton("Cancel");
        btnCnacel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                isCanceled = true;
                Toolkit.getDefaultToolkit().beep();
                String str = "<html>" + "<font color=\"#FF0000\">" + "<b>" + "Process Canceled." + "</b>" + "</font>" + "</html>";
                lblInfo.setText(str);
                progressBar.setIndeterminate(false);
                progressBar.setValue(30);
                new FileReaderWriter().setContents(log, strLogText);
                btnLog.setEnabled(true);
                btnOPFolder.setEnabled(true);
                btnCnacel.setEnabled(false);
                btnClose.setEnabled(true);
            }
        });
        btnClose = new JButton("Close");
        btnClose.setEnabled(false);
        btnClose.addActionListener(this);
        pnlTop = new JPanel();
        pnlTop.add(progressBar);
        pnlTop.add(lblDate);
        pnlCenter = new JPanel();
        pnlCenter.add(lblInfo);
        pnlBottom = new JPanel();
        pnlBottom.setLayout(new FlowLayout());
        pnlBottom.add(btnLog);
        pnlBottom.add(btnOPFolder);
        pnlBottom.add(btnCnacel);
        pnlBottom.add(btnClose);
        setBounds(250, 250, 500, 150);
        setLayout(new GridLayout(3, 1, 5, 5));
        add(pnlTop);
        add(pnlCenter);
        add(pnlBottom);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        task = new Task();
        task.execute();
    }

    class Task extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() {
            try {
                Thread.sleep(1000);
                Runtime rt = Runtime.getRuntime();
                Process pr = rt.exec(strCmd);
                BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                String line = null;
                while ((line = input.readLine()) != null && !isCanceled) {
                    System.out.println(line);
                    strCmdText = line;
                    strLogText += line + "\n";
                    lblInfo.setText("<html>" + "<font color=\"#008000\">" + "<b>" + strCmdText + "</b>" + "</font>" + "</html>");
                }
                int exitVal = pr.waitFor();
                new FileReaderWriter().setContents(log, strLogText);
            } catch (InterruptedException ignore) {
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            progressBar.setIndeterminate(false);
            progressBar.setValue(100);
            lblInfo.setText("<html>" + "<font color=\"#FF0000\">" + "<b>" + "Process completed." + "</b>" + "</font>" + "</html>");
            btnLog.setEnabled(true);
            btnOPFolder.setEnabled(true);
            btnCnacel.setEnabled(false);
            btnClose.setEnabled(true);
        }
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals("Close")) {
            wrapper.setVisible(true);
            setVisible(false);
        }
    }
}
