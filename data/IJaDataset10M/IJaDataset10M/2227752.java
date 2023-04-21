package com.pf.mf.ui;

import java.awt.event.*;
import java.io.*;
import java.nio.channels.*;
import java.util.Properties;
import javax.swing.*;
import com.pf.mf.core.*;

public class MFCmd implements ActionListener {

    private JButton bUpload, bDownload, bConfig;

    private Properties m_props = new Properties();

    private Properties m_hist = new Properties();

    private static void copyFile(File in, File out) throws Exception {
        FileChannel sourceChannel = new FileInputStream(in).getChannel();
        FileChannel destinationChannel = new FileOutputStream(out).getChannel();
        sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
        sourceChannel.close();
        destinationChannel.close();
    }

    public MFCmd() throws IOException {
        m_props.load(new FileInputStream("config.props"));
        try {
            FileInputStream fis = new FileInputStream("hist.props");
            m_hist.load(fis);
            fis.close();
        } catch (IOException e) {
        }
    }

    private void createAndShowGUI() {
        String lookAndFeel = null;
        lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Mail Photo System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createButtons();
        JPanel pane = new JPanel();
        pane.setOpaque(true);
        pane.add(bUpload);
        pane.add(bDownload);
        pane.add(bConfig);
        frame.setContentPane(pane);
        frame.pack();
        frame.setVisible(true);
    }

    private void createButtons() {
        bUpload = new JButton("Upload");
        bUpload.setActionCommand("Upload");
        bUpload.setEnabled(true);
        bDownload = new JButton("Download");
        bDownload.setActionCommand("Download");
        bDownload.setEnabled(true);
        bConfig = new JButton("Resume Upload");
        bConfig.setActionCommand("ResumeUpload");
        bConfig.setEnabled(true);
        bUpload.addActionListener(this);
        bDownload.addActionListener(this);
        bConfig.addActionListener(this);
        bUpload.setToolTipText("Click this button to upload");
        bDownload.setToolTipText("Clike this button to download");
        bConfig.setToolTipText("Click this button to resume upload");
    }

    public void actionPerformed(ActionEvent e) {
        if ("Upload".equals(e.getActionCommand())) {
            String upFolder = m_hist.getProperty("folder.upload", "D:\\");
            JFileChooser chooser = new JFileChooser(new File(upFolder));
            chooser.setMultiSelectionEnabled(true);
            int returnVal = chooser.showOpenDialog(bUpload);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File[] list = chooser.getSelectedFiles();
                if (list.length > 0) {
                    File file1 = list[0];
                    String parent = file1.getParentFile().getAbsolutePath();
                    if (parent != null) m_hist.setProperty("folder.upload", parent);
                    try {
                        FileOutputStream fos = new FileOutputStream("hist.props");
                        m_hist.store(fos, "History -- do not modify");
                        fos.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                try {
                    File tmpFolder = new File("_upload");
                    if (!tmpFolder.exists()) tmpFolder.mkdir();
                    for (int i = 0; i < list.length; i++) {
                        File fi = list[i];
                        File fo = new File(tmpFolder, fi.getName());
                        copyFile(fi, fo);
                    }
                    Sender sd = new Sender(m_props);
                    sd.up(tmpFolder);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else if ("Download".equals(e.getActionCommand())) {
            String downFolder = m_hist.getProperty("folder.download", "D:\\");
            JFileChooser chooser = new JFileChooser(new File(downFolder));
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = chooser.showOpenDialog(bUpload);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File folder = chooser.getSelectedFile();
                m_hist.setProperty("folder.download", folder.getAbsolutePath());
                try {
                    FileOutputStream fos = new FileOutputStream("hist.props");
                    m_hist.store(fos, "History -- do not modify");
                    fos.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Receiver rc = new Receiver(m_props);
                rc.down(folder);
            }
        } else if ("ResumeUpload".equals(e.getActionCommand())) {
            try {
                File tmpFolder = new File("_upload");
                if (!tmpFolder.exists()) tmpFolder.mkdir();
                Sender sd = new Sender(m_props);
                sd.up(tmpFolder);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        final MFCmd mf = new MFCmd();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                mf.createAndShowGUI();
            }
        });
    }
}
