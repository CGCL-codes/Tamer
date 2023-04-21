package org.teambeans.sodbeans.projects;

import java.util.Properties;
import org.openide.filesystems.FileObject;
import org.teambeans.sodbeans.api.hop.MainFileProvider;

/**
 *
 * @author Andrew
 */
public class MainFileProviderImpl extends MainFileProvider {

    private final HopProject proj;

    private FileObject mainFile = null;

    private boolean checked = false;

    MainFileProviderImpl(HopProject proj) {
        this.proj = proj;
    }

    public FileObject getMainFile() {
        if (mainFile == null && !checked) {
            checked = true;
            Properties props = (Properties) proj.getLookup().lookup(Properties.class);
            String path = props.getProperty(HopProject.KEY_MAINFILE);
            if (path != null) {
                FileObject projectDir = proj.getProjectDirectory();
                mainFile = projectDir.getFileObject(path);
            }
        }
        if (mainFile != null && !mainFile.isValid()) {
            return null;
        }
        return mainFile;
    }

    public FileObject[] getSourceFiles() {
        FileObject[] files = proj.getProjectDirectory().getChildren();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().compareToIgnoreCase("src") == 0) {
                FileObject src = files[i];
                FileObject[] srcFiles = src.getChildren();
                return srcFiles;
            }
        }
        return null;
    }

    public void setMainFile(FileObject file) {
        String projPath = proj.getProjectDirectory().getPath();
        assert file == null || file.getPath().startsWith(projPath) : "Main file not under project";
        boolean change = ((mainFile == null) != (file == null)) || (mainFile != null && !mainFile.equals(file));
        if (change) {
            mainFile = file;
            Properties props = (Properties) proj.getLookup().lookup(Properties.class);
            String relPath = file.getPath().substring(projPath.length());
            props.put(HopProject.KEY_MAINFILE, relPath);
        }
    }
}
