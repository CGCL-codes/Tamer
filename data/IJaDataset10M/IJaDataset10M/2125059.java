package org.opendte.controller.gui;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class DeployTestcaseDialog extends JDialog implements ActionListener {

    public DeployTestcaseDialog(Frame owner, boolean modal) {
        super(owner, modal);
        setTitle("Deploy Testcase");
        createDialog();
    }

    private JTextField mSpecPath;

    private JTextField mZipPath;

    private boolean mCancelled = false;

    private void createDialog() {
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JPanel p = Util.createTitledPanel("Path to spec file");
        mSpecPath = new JTextField(30);
        p.add(mSpecPath);
        JButton fileChooseBtn = new JButton("...");
        fileChooseBtn.setActionCommand("CHOOSE_SPEC_PATH");
        fileChooseBtn.addActionListener(this);
        p.add(fileChooseBtn);
        JPanel p2 = new JPanel();
        p2.setBorder(new EmptyBorder(5, 5, 5, 5));
        p2.add(p);
        getContentPane().add(p2);
        p = Util.createTitledPanel("Path to zip archive");
        mZipPath = new JTextField(30);
        p.add(mZipPath);
        fileChooseBtn = new JButton("...");
        fileChooseBtn.setActionCommand("CHOOSE_ZIP_PATH");
        fileChooseBtn.addActionListener(this);
        p.add(fileChooseBtn);
        p2 = new JPanel();
        p2.setBorder(new EmptyBorder(5, 5, 5, 5));
        p2.add(p);
        getContentPane().add(p2);
        JButton ok = new JButton("Ok");
        ok.setActionCommand("OK");
        ok.addActionListener(this);
        JButton cancel = new JButton("Cancel");
        cancel.setActionCommand("CANCEL");
        cancel.addActionListener(this);
        p = new JPanel();
        p.setLayout(new GridLayout(1, 2));
        p.add(ok);
        p.add(cancel);
        p2 = new JPanel();
        p2.setBorder(new EmptyBorder(5, 5, 5, 5));
        p2.add(p);
        getContentPane().add(p2);
        getContentPane().setSize(new Dimension(250, 200));
        pack();
        show();
    }

    public String getSpecPath() {
        if (mCancelled) return null;
        return mSpecPath.getText();
    }

    public String getZipPath() {
        if (mCancelled) return null;
        return mZipPath.getText();
    }

    public boolean wasCancelled() {
        return mCancelled;
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("[DeployTestcaseDialog] got action " + e.getActionCommand());
        if (e.getActionCommand().equals("OK")) {
            mCancelled = false;
            dispose();
        } else if (e.getActionCommand().equals("CANCEL")) {
            mCancelled = true;
            dispose();
        } else if (e.getActionCommand().equals("CHOOSE_SPEC_PATH")) {
            JFileChooser chooser = getFileChooser(getSpecPath());
            int retVal = chooser.showOpenDialog(this);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                System.out.println("Opening " + f);
                String path = f.getAbsolutePath();
                mSpecPath.setText(path);
                File tcDir = f.getParentFile();
                String name = f.getName();
                int dotIndex = name.lastIndexOf(".");
                if (dotIndex != -1) {
                    String basename = name.substring(0, name.lastIndexOf("."));
                    String zipfileName = basename + ".zip";
                    File zipFile = new File(tcDir, zipfileName);
                    if (zipFile.exists()) {
                        mZipPath.setText(zipFile.getAbsolutePath());
                    }
                }
            }
        } else if (e.getActionCommand().equals("CHOOSE_ZIP_PATH")) {
            JFileChooser chooser = getFileChooser(getZipPath());
            int retVal = chooser.showOpenDialog(this);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                mZipPath.setText(f.getAbsolutePath());
            }
        } else {
            System.out.println("Got unknown command " + e.getActionCommand());
        }
    }

    private JFileChooser getFileChooser(String filename) {
        if (filename == null || filename.equals("")) {
            return new JFileChooser(System.getProperty("user.dir"));
        } else {
            File f = new File(filename);
            String parent = f.getParent();
            return new JFileChooser(parent);
        }
    }
}
