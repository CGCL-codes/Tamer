package freestyleLearningGroup.independent.gui;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class FLGSelectableFileChooser extends JFileChooser {

    private String typeDescription;

    public FLGSelectableFileChooser(String typeDescription, String[] selectableFileExtensions) {
        this.typeDescription = typeDescription;
        setFileFilter(new SelectableFileFilter(selectableFileExtensions));
    }

    public FLGSelectableFileChooser(String typeDescription, String selectableFileExtension) {
        this.typeDescription = typeDescription;
        setFileFilter(new SelectableFileFilter(selectableFileExtension));
    }

    public boolean showDialog() {
        int returnValue = showOpenDialog(FLGUIUtilities.getMainFrame());
        return returnValue == APPROVE_OPTION;
    }

    public boolean showDialog(File currentDirectory) {
        if (currentDirectory != null) this.setCurrentDirectory(currentDirectory);
        int returnValue = showOpenDialog(FLGUIUtilities.getMainFrame());
        return returnValue == APPROVE_OPTION;
    }

    public String getFileExtension() {
        String fileExtension = null;
        File f = getSelectedFile();
        if (f != null) {
            String fileName = f.getName().toLowerCase();
            int lastDotIndex = fileName.lastIndexOf(".");
            if (lastDotIndex > 2) {
                fileExtension = fileName.substring(lastDotIndex + 1);
            }
        }
        return fileExtension;
    }

    class SelectableFileFilter extends FileFilter {

        private boolean showAllFiles = false;

        private String[] selectableFileExtensions;

        public SelectableFileFilter(String[] selectableFileExtensions) {
            this.selectableFileExtensions = selectableFileExtensions;
            init();
        }

        public SelectableFileFilter(String selectableExtension) {
            selectableFileExtensions = new String[1];
            selectableFileExtensions[0] = selectableExtension;
            init();
        }

        private void init() {
            if (selectableFileExtensions == null) showAllFiles = true; else if (selectableFileExtensions[0].equals(".*")) showAllFiles = true;
        }

        public boolean accept(File f) {
            if (showAllFiles || f.isDirectory()) return true; else {
                String fileName = f.getName().toLowerCase();
                for (int i = 0; i < selectableFileExtensions.length; i++) {
                    if (fileName.endsWith(selectableFileExtensions[i])) return true;
                }
                return false;
            }
        }

        public String getDescription() {
            return typeDescription;
        }
    }
}
