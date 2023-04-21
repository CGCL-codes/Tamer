package org.pointrel.pointrel20090201.utilities;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.pointrel.pointrel20090201.ArchiveFileSupport;

public class FileUtilities {

    public static String exampleArchivePath = "trunk/Pointrel20090201/TestArchiveExamples/";

    public static String ensureArchivePathIsValid(String archivePath, Component parent, boolean checkForTransactions) {
        while (true) {
            if (archivePath != null) {
                File file = new File(archivePath);
                if (!file.exists()) {
                    System.out.println("Archive can not be found: " + archivePath);
                    archivePath = null;
                } else if (!file.isDirectory()) {
                    System.out.println("Archive is not a directory: " + archivePath);
                    archivePath = null;
                }
                if (archivePath != null && checkForTransactions) {
                    File[] files = ArchiveFileSupport.getTransactionFiles(archivePath);
                    if (files == null || files.length == 0) {
                        if (parent == null) {
                            archivePath = null;
                        } else {
                            int confirmResult = JOptionPane.showConfirmDialog(parent, "Archive: " + archivePath + "\nThere are no transactions in this directory yet.\nDo you wish to use it as an archive anyway?");
                            if (confirmResult != JOptionPane.OK_OPTION) archivePath = null;
                        }
                    }
                }
                if (archivePath != null) {
                    System.out.println("Archive directory: " + archivePath);
                    return archivePath;
                }
                if (parent == null) return null;
            }
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Choose an archive directory");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnValue = chooser.showOpenDialog(parent);
            if (returnValue != JFileChooser.APPROVE_OPTION) return null;
            archivePath = chooser.getSelectedFile().getAbsolutePath();
        }
    }

    public static String checkArchivePathAndExitIfNoValidSelection(String archivePath, Component parent) {
        archivePath = FileUtilities.ensureArchivePathIsValid(archivePath, parent, true);
        if (archivePath == null) {
            System.out.println("No archive selected; Exiting");
            System.exit(0);
        }
        return archivePath;
    }
}
