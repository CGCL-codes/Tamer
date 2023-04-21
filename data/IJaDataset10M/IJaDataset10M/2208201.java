package flanagan.io;

import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.swing.filechooser.*;
import flanagan.io.FileInput;

public class MultipleFilesChooser {

    private String[] fileNames = null;

    private String[] pathNames = null;

    private String[] dirNames = null;

    private String[] stemNames = null;

    private FileInput[] fileObjects = null;

    private int nFiles = 0;

    private String path = null;

    private String extn = null;

    public MultipleFilesChooser() {
    }

    public MultipleFilesChooser(String path) {
        this.path = path;
    }

    public FileInput[] selectFiles() {
        return this.selectFiles("Select File");
    }

    public FileInput[] selectFiles(String prompt) {
        JFileChooser chooser = new JFileChooser(this.path);
        chooser.setMultiSelectionEnabled(true);
        if (this.extn != null) {
            FileTypeFilter f = new FileTypeFilter();
            f.addExtension(extn);
            f.setDescription(extn + " files");
            chooser.setFileFilter(f);
        } else {
            chooser.setAcceptAllFileFilterUsed(true);
        }
        chooser.setDialogTitle(prompt);
        chooser.showOpenDialog(null);
        File[] files = chooser.getSelectedFiles();
        this.nFiles = files.length;
        this.fileObjects = new FileInput[nFiles];
        this.fileNames = new String[nFiles];
        this.stemNames = new String[nFiles];
        this.pathNames = new String[nFiles];
        this.dirNames = new String[nFiles];
        for (int i = 0; i < nFiles; i++) {
            this.fileNames[i] = files[i].getName();
            this.pathNames[i] = files[i].toString();
            this.dirNames[i] = (files[i].getParentFile()).toString();
            this.fileObjects[i] = new FileInput(this.pathNames[i]);
            int posDot = this.fileNames[i].indexOf('.');
            if (posDot == -1) {
                this.stemNames[i] = this.fileNames[i];
            } else {
                this.stemNames[i] = this.fileNames[i].substring(0, posDot);
            }
        }
        return this.fileObjects;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    public void setExtension(String extn) {
        this.extn = extn;
    }

    public void setAllExtensions() {
        this.extn = null;
    }

    public String getExtension() {
        return this.extn;
    }

    public int getNumberOfFiles() {
        return this.nFiles;
    }

    public String[] getFileNames() {
        return this.fileNames;
    }

    public String[] getStemNames() {
        return this.stemNames;
    }

    public String[] getPathNames() {
        return this.pathNames;
    }

    public String[] getDirPaths() {
        return this.dirNames;
    }

    public final synchronized void close() {
        for (int i = 0; i < this.nFiles; i++) {
            this.fileObjects[i].close();
        }
    }

    public static final synchronized void endProgram() {
        int ans = JOptionPane.showConfirmDialog(null, "Do you wish to end the program", "End Program", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (ans == 0) {
            System.exit(0);
        } else {
            JOptionPane.showMessageDialog(null, "Now you must press the appropriate escape key/s, e.g. Ctrl C, to exit this program");
        }
    }
}
