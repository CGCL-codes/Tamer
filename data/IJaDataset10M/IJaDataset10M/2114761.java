package org.gerhardb.jibs.util;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.io.*;
import java.util.Arrays;
import java.util.prefs.Preferences;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import org.gerhardb.jibs.Jibs;
import org.gerhardb.lib.io.DirectoriesOnlyFileFilter;
import org.gerhardb.lib.io.FilesOnlyFileFilter;
import org.gerhardb.lib.swing.JFileChooserExtra;
import org.gerhardb.lib.swing.JPanelRows;
import org.gerhardb.lib.swing.SwingUtils;
import org.gerhardb.lib.util.Icons;
import org.gerhardb.lib.util.StopCheck;

/**
 */
public class ListDirectoryContents extends JFrame implements StopCheck {

    private static final String LAST_FILE = "LastFile";

    private static final String LAST_ROOT = "LastRoot";

    private static final String APP_NAME = "ListDirectoryContents";

    private static final Preferences clsPrefs = Preferences.userRoot().node("/org/gerhardb/jibs/util/ListDirectoryContents");

    JTextField myFileName = new JTextField(60);

    JTextField myRootDir = new JTextField(60);

    JLabel myProgress = new JLabel("                                                                                       ");

    boolean iStop = false;

    JButton myStopBtn = new JButton(Jibs.getString("ListDirectoryContents.4"));

    String myRootString;

    public ListDirectoryContents() {
        super(Jibs.getString("ListDirectoryContents.5"));
        layoutComponents();
        this.myFileName.setText(clsPrefs.get(LAST_FILE, null));
        this.myRootDir.setText(clsPrefs.get(LAST_ROOT, null));
        this.setIconImage(Icons.getIcon(Icons.JIBS_16).getImage());
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                ListDirectoryContents.this.pack();
                SwingUtils.centerOnScreen(ListDirectoryContents.this);
                ListDirectoryContents.this.setVisible(true);
            }
        });
    }

    @Override
    public boolean isStopped() {
        return this.iStop;
    }

    private void layoutComponents() {
        this.setSize(new Dimension(600, 600));
        JButton goBtn = new JButton(Jibs.getString("ListDirectoryContents.6"));
        goBtn.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                go();
            }
        });
        this.myStopBtn.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ListDirectoryContents.this.iStop = true;
            }
        });
        JButton fileBtn = new JButton("...");
        fileBtn.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectFile();
            }
        });
        JButton rootBtn = new JButton("...");
        rootBtn.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectRoot();
            }
        });
        JPanelRows topPanel = new JPanelRows(FlowLayout.CENTER);
        JPanel aRow = topPanel.topRow();
        aRow.add(new JLabel(Jibs.getString("ListDirectoryContents.9") + ": "));
        aRow.add(this.myRootDir);
        aRow.add(rootBtn);
        aRow = topPanel.nextRow();
        aRow.add(new JLabel(" "));
        aRow = topPanel.nextRow();
        aRow.add(new JLabel(Jibs.getString("ListDirectoryContents.12")));
        aRow = topPanel.nextRow();
        aRow.add(new JLabel(Jibs.getString("ListDirectoryContents.13") + ": "));
        aRow.add(this.myFileName);
        aRow.add(fileBtn);
        aRow = topPanel.nextRow();
        aRow.add(goBtn);
        aRow.add(this.myStopBtn);
        aRow = topPanel.nextRow();
        aRow.add(this.myProgress);
        this.setContentPane(topPanel);
    }

    void selectFile() {
        JFileChooserExtra chooser = new JFileChooserExtra(clsPrefs.get(LAST_FILE, null));
        chooser.setDialogTitle(Jibs.getString("ListDirectoryContents.15"));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.addChoosableFileFilter(new JibsTreeFileFilter());
        chooser.setSaveName(APP_NAME, "DirectoryTreeList.txt");
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File picked = chooser.getSelectedFile();
            if (picked != null) {
                String fileName = picked.toString();
                if (!fileName.contains(".")) {
                    fileName = fileName + ".txt";
                }
                this.myFileName.setText(fileName);
                try {
                    clsPrefs.put(LAST_FILE, picked.toString());
                    clsPrefs.flush();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    void selectRoot() {
        JFileChooserExtra chooser = new JFileChooserExtra(clsPrefs.get(LAST_ROOT, null));
        chooser.setSaveName(APP_NAME, Jibs.getString("ListDirectoryContents.19"));
        chooser.setApproveButtonText(Jibs.getString("ListDirectoryContents.20"));
        chooser.setDialogTitle(Jibs.getString("ListDirectoryContents.21"));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File picked = chooser.getSelectedFile();
            if (picked != null) {
                this.myRootDir.setText(picked.toString());
                try {
                    clsPrefs.put(LAST_ROOT, picked.toString());
                    clsPrefs.flush();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    void go() {
        String fileName = this.myFileName.getText();
        File treeFile = new File(fileName);
        if (treeFile.exists()) {
            JOptionPane.showMessageDialog(this, Jibs.getString("ListDirectoryContents.22"), Jibs.getString("ListDirectoryContents.23"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.myRootString = this.myRootDir.getText();
        File rootFile = new File(this.myRootString);
        if (!rootFile.exists()) {
            JOptionPane.showMessageDialog(this, Jibs.getString("ListDirectoryContents.24"), Jibs.getString("ListDirectoryContents.25"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (this.myRootString.endsWith("/") || this.myRootString.endsWith("\\")) {
            this.myRootString = this.myRootString.substring(0, this.myRootString.length() - 1);
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(treeFile), true);
            try {
                writer.println(Jibs.getString("ListDirectoryContents.3"));
                writer.println(Jibs.getString("ListDirectoryContents.2") + rootFile.getAbsolutePath());
                writer.println(Jibs.getString("ListDirectoryContents.30"));
                writer.flush();
                printFiles(writer, rootFile);
                writer.println(Jibs.getString("ListDirectoryContents.31"));
                writer.flush();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), Jibs.getString("ListDirectoryContents.32"), JOptionPane.ERROR_MESSAGE);
            }
            writer.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), Jibs.getString("ListDirectoryContents.33"), JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        this.myProgress.setText(Jibs.getString("ListDirectoryContents.34"));
    }

    void printFiles(PrintWriter writer, File file) throws Exception {
        if (this.iStop) {
            return;
        }
        File[] files = file.listFiles(FilesOnlyFileFilter.FILES_ONLY);
        Arrays.sort(files);
        for (int i = 0; i < files.length; i++) {
            writer.println(files[i].getAbsolutePath());
        }
        writer.flush();
        File[] dirs = file.listFiles(DirectoriesOnlyFileFilter.DIRECTORIES_ONLY);
        Arrays.sort(dirs);
        for (int i = 0; i < dirs.length; i++) {
            printFiles(writer, dirs[i]);
        }
        writer.flush();
    }

    class JibsTreeFileFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            if (f == null) {
                return false;
            }
            if (f.isDirectory()) {
                return true;
            }
            String name = f.getName();
            if (name.endsWith(".txt")) {
                return true;
            }
            return false;
        }

        @Override
        public String getDescription() {
            return Jibs.getString("ListDirectoryContents.36");
        }
    }

    public static void main(String[] args) {
        new ListDirectoryContents();
    }
}
