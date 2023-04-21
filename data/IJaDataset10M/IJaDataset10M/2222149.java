package magicstudio.flattendir;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import magicstudio.util.DirTraversal;

public class FlattenDir {

    private static char separator = '_';

    private static char sysSeparator = System.getProperty("file.separator").charAt(0);

    public static void main(String args[]) {
        assert System.getProperty("file.separator").length() == 1;
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        MainFrame frame = new MainFrame();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private static class MainFrame extends JFrame {

        private JFrame frame = this;

        private JPanel pane = (JPanel) getContentPane();

        private JTextField fileText = new JTextField(30);

        private int prefixLength;

        private String prefix;

        private static int count = 0;

        MainFrame() {
            setTitle("FlattenDir");
            pane.setLayout(new GridLayout(0, 1));
            JPanel upperPane = new JPanel(new FlowLayout());
            java.net.URL imgURL = this.getClass().getResource("Open16.gif");
            JButton browseButton = new JButton("Browse...", new ImageIcon(imgURL));
            fileText.addActionListener(startAction);
            final JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            browseButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                        fileText.setText(fc.getSelectedFile().toString());
                    }
                }
            });
            upperPane.add(fileText);
            upperPane.add(browseButton);
            pane.add(upperPane);
            JPanel lowerPane = new JPanel(new FlowLayout());
            JButton aboutButton = new JButton("About");
            aboutButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(frame, "Developed by Cobra@2003\nUsage: select directory to be flatten.");
                }
            });
            JButton startButton = new JButton("Start");
            startButton.addActionListener(startAction);
            lowerPane.add(aboutButton);
            lowerPane.add(startButton);
            pane.add(lowerPane);
        }

        private ActionListener startAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                File file = new File(fileText.getText());
                if (!file.exists() || file.isFile()) {
                    JOptionPane.showMessageDialog(frame, file.toString() + "\ndoes not exists or\nis not a valid directory!", "Error!", JOptionPane.ERROR_MESSAGE);
                } else {
                    File[] subfiles = file.listFiles();
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < 10 && i < subfiles.length; i++) {
                        sb.append("-- " + subfiles[i].toString() + "\n");
                    }
                    if (subfiles.length > 10) sb.append("......\n");
                    if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(frame, file.toString() + " =>\n" + sb.toString() + "Do you wish to continue?", "Continue?", JOptionPane.YES_NO_OPTION)) {
                        prefix = file.toString() + FlattenDir.sysSeparator;
                        prefixLength = prefix.toString().length();
                        DirAccess da = new DirAccess(file);
                        da.start();
                    }
                }
            }
        };

        private class DirAccess extends DirTraversal {

            public DirAccess(File file) {
                super(file);
            }

            public void run() {
                super.run();
                JOptionPane.showMessageDialog(frame, "Operation Successful!");
            }

            protected void enterDir(File file) {
            }

            protected void leaveDir(File dir) {
                if (currentDepth != 1) {
                    System.err.println("Delete " + dir);
                    boolean flag = dir.delete();
                    if (!flag) throw new RuntimeException();
                }
            }

            protected void handleFile(File file) {
                File dir = file.getParentFile();
                if (prefix.equals(dir.toString() + FlattenDir.sysSeparator)) return;
                String filename = file.toString().substring(prefixLength);
                filename = filename.replace(FlattenDir.sysSeparator, FlattenDir.separator);
                File newFile = new File(prefix + filename);
                while (newFile.exists()) {
                    count++;
                    String surfix = "" + FlattenDir.separator + count;
                    int dotPos = filename.lastIndexOf('.');
                    if (dotPos == -1) {
                        newFile = new File(prefix + filename + surfix);
                    } else {
                        StringBuffer sb = new StringBuffer(filename);
                        sb.insert(dotPos, surfix);
                        newFile = new File(prefix + sb.toString());
                    }
                }
                System.err.println(newFile.toString());
                boolean flag = file.renameTo(newFile);
                if (!flag) throw new RuntimeException();
            }
        }
    }
}
