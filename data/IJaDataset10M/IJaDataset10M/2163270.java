package net.sourceforge.jisocreator.model.osexplorer;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import org.eclipse.swt.program.Program;

public class OSExplorer {

    private File[] roots;

    private static OSExplorer instance;

    public static OSExplorer getInstance() {
        if (instance == null) {
            instance = new OSExplorer();
        }
        return instance;
    }

    public OSExplorer(File[] roots) {
        this.setRoots(roots);
    }

    public OSExplorer() {
        this(File.listRoots());
    }

    public boolean launch(File file) {
        return Program.launch(file.getAbsolutePath());
    }

    public String getName(File file) {
        return file.getName();
    }

    public String getAbsolutePath(File file) {
        return file.getAbsolutePath();
    }

    public String length(File file) {
        return Long.toString(file.length());
    }

    public String lastModified(File file) {
        return DateFormat.getDateTimeInstance().format(new Date(file.lastModified()));
    }

    public String getFileType(File file) {
        String extension = getExtension(file);
        if (extension.equals("Folder")) {
            return "Folder";
        } else if (extension.equals("")) {
            return "File";
        } else {
            Program program = Program.findProgram(extension);
            if (program == null) {
                return "File " + extension;
            } else {
                return program.getName();
            }
        }
    }

    public void setRoots(File[] roots) {
        this.roots = roots;
    }

    public File[] getRoots() {
        return roots;
    }

    public boolean isRoot(File file) {
        for (File root : roots) {
            if (root.equals(file)) {
                return true;
            }
        }
        return false;
    }

    public String getExtension(File file) {
        if (file.isDirectory()) {
            return "Folder";
        } else {
            int dot = file.getName().lastIndexOf('.');
            switch(dot) {
                case -1:
                    return "";
                default:
                    return file.getName().substring(dot);
            }
        }
    }
}
