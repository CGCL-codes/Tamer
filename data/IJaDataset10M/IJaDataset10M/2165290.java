package edu.stanford.genetics.treeview.plugin.karyoview;

import edu.stanford.genetics.treeview.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
* This class allows editing of a file set...
*/
public class FileSetEditor extends JPanel {

    private FileSet fileSet;

    /** Setter for fileSet */
    public void setFileSet(FileSet fileSet) {
        this.fileSet = fileSet;
    }

    /** Getter for fileSet */
    public FileSet getFileSet() {
        return fileSet;
    }

    public FileSetEditor(FileSet fileSet, Window jFrame) {
        setFileSet(fileSet);
        final JLabel desc = new JLabel(fileSet.toString());
        add(desc);
        final Window frame = jFrame;
        JButton pushButton = new JButton("Find...");
        pushButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser fileDialog = new JFileChooser();
                CdtFilter ff = new CdtFilter();
                fileDialog.setFileFilter(ff);
                String string = getFileSet().getDir();
                if (string != null) {
                    fileDialog.setCurrentDirectory(new File(string));
                }
                int retVal = fileDialog.showOpenDialog(frame);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    File chosen = fileDialog.getSelectedFile();
                    FileSet fileSet1 = new FileSet(chosen.getName(), chosen.getParent() + File.separator);
                    fileSet1.setName(getFileSet().getName());
                    getFileSet().copyState(fileSet1);
                }
                desc.setText(getFileSet().toString());
                desc.revalidate();
                desc.repaint();
            }
        });
        add(pushButton);
    }

    public static final void main(String[] argv) {
        FileSet temp = new FileSet(new DummyConfigNode("DummyFileSet"));
        JFrame frame = new JFrame("FileSetEditor Test");
        FileSetEditor cse = new FileSetEditor(temp, frame);
        frame.getContentPane().add(cse);
        frame.pack();
        frame.setVisible(true);
    }
}
