package org.vastenhouw.swing.fld;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.JTextComponent;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.vastenhouw.swing.SwingUtil;
import org.vastenhouw.swing.resources.DefUIResources;
import org.vastenhouw.util.FileUtil;

public class FileField extends DataField {

    /** Instruction to display only files. */
    public static final int FILES_ONLY = JFileChooser.FILES_ONLY;

    /** Instruction to display only directories. */
    public static final int DIRECTORIES_ONLY = JFileChooser.DIRECTORIES_ONLY;

    /** Instruction to display both files and directories. */
    public static final int FILES_AND_DIRECTORIES = JFileChooser.FILES_AND_DIRECTORIES;

    private ExampleFileFilter filter = null;

    private boolean multiSelectionEnabled = false;

    private int fileSelectionMode = FILES_ONLY;

    private File file;

    private File baseDirectory;

    public FileField(String labelText) {
        this(labelText, null);
    }

    public FileField(String labelText, File file) {
        super(labelText, new JTextField(file == null ? null : file.getAbsolutePath()), DefUIResources.getIcon(DefUIResources.ICON_FILE_CHOOSER));
        this.file = file;
    }

    public void setRelativeTo(File baseDir) {
        this.baseDirectory = baseDir;
    }

    public void addExtension(String ext) {
        if (filter == null) {
            filter = new ExampleFileFilter();
        }
        filter.addExtension(ext);
    }

    public void setDescription(String desc) {
        if (filter == null) {
            filter = new ExampleFileFilter();
        }
        filter.setDescription(desc);
    }

    public void setMultiSelectionEnabled(boolean b) {
        multiSelectionEnabled = b;
    }

    public void setFileSelectionMode(int mode) {
        fileSelectionMode = mode;
    }

    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(baseDirectory, ((JTextComponent) getData()).getText()));
        if (filter != null) {
            chooser.setFileFilter(filter);
        }
        chooser.setMultiSelectionEnabled(multiSelectionEnabled);
        chooser.setFileSelectionMode(fileSelectionMode);
        int returnVal = chooser.showOpenDialog(SwingUtil.getWindowForComponent(data));
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            ((JTextComponent) getData()).setText(getRelativePart(file));
        }
    }

    private String getRelativePart(File f) {
        return FileUtil.getRelativePathFromTo(baseDirectory, f);
    }
}
