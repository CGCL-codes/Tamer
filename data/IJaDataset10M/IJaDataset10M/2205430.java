package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.Arrays;

/**
 * @author Anna R. Zhukova
 */
public class FileChoosers {

    private static Main main;

    public static final String[] ACCEPTED_FORMATS = new String[] { ".owl", ".rdf", ".rdfs", ".obo", ".n3" };

    private static final JFileChooser fileChooser = new JFileChooser("./resources/ontologyexamples");

    private static final FileFilter filter = new FileFilter() {

        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String path = f.getAbsolutePath();
            for (String format : ACCEPTED_FORMATS) {
                if (path.endsWith(format)) {
                    return true;
                }
            }
            return false;
        }

        public String getDescription() {
            return null;
        }
    };

    static {
        FileChoosers.fileChooser.addChoosableFileFilter(FileChoosers.filter);
    }

    public static void setMain(Main main) {
        FileChoosers.main = main;
    }

    public static File[] getOpenFileChooser(String title, boolean multiSelectionEnabled) {
        FileChoosers.fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        FileChoosers.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileChoosers.fileChooser.setMultiSelectionEnabled(multiSelectionEnabled);
        int value = FileChoosers.fileChooser.showDialog(main.getFrame(), title);
        if (value == JFileChooser.APPROVE_OPTION) {
            return multiSelectionEnabled ? FileChoosers.fileChooser.getSelectedFiles() : new File[] { FileChoosers.fileChooser.getSelectedFile() };
        }
        return null;
    }

    public static File getSaveFileChooser() {
        FileChoosers.fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        FileChoosers.fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int value = FileChoosers.fileChooser.showDialog(main.getFrame(), "Select Existing File Or Type a New File Name");
        if (value == JFileChooser.APPROVE_OPTION) {
            File file = FileChoosers.fileChooser.getSelectedFile();
            if (file != null) {
                try {
                    if (file.isFile()) {
                        int replace = JOptionPane.showConfirmDialog(FileChoosers.fileChooser, "File " + file.getName() + " already exists. Replace it?");
                        if (replace == JOptionPane.OK_OPTION) {
                            return file;
                        }
                    } else if (file.createNewFile()) {
                        return file;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(FileChoosers.fileChooser, "Cannot save");
                }
            }
        }
        return null;
    }
}
