package de.mpiwg.vspace.workspace.template.slides.provider;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import de.mpiwg.vspace.common.project.ProjectObservable;
import de.mpiwg.vspace.common.project.ProjectManager;
import de.mpiwg.vspace.generation.copier.IFilePathProvider;
import de.mpiwg.vspace.generation.copier.IFileToCopyInfo;
import de.mpiwg.vspace.templates.provider.CommonFileInfo;
import de.mpiwg.vspace.workspace.template.slides.internal.Constants;

/**
 * This class plugs in in generation plugin to provide FileInfos that
 * are used during generation plugin for copying neccessary files.
 * 
 * @author Julia Damerow
 *
 */
public class CSSFilePathProvider implements IFilePathProvider, Observer {

    private File cssFolder = null;

    public CSSFilePathProvider() {
        checkTemplateFolder();
        ProjectObservable.INSTANCE.addObserver(this);
    }

    public List<IFileToCopyInfo> getFilesToCopyInfos() {
        checkTemplateFolder();
        if (cssFolder == null) return new ArrayList<IFileToCopyInfo>();
        File[] cssFiles = cssFolder.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                if (pathname.getName().endsWith("." + Constants.CSS_EXTENSION)) return true;
                return false;
            }
        });
        List<IFileToCopyInfo> infos = new ArrayList<IFileToCopyInfo>();
        if ((cssFiles != null) && (cssFiles.length != 0)) {
            for (File cssFile : cssFiles) infos.add(new CommonFileInfo(null, cssFile.getAbsolutePath(), cssFile.getName()));
        }
        return infos;
    }

    public List<String> getFoldersToRegister() {
        return new ArrayList<String>();
    }

    private void checkTemplateFolder() {
        if (cssFolder != null) return;
        cssFolder = ProjectManager.getInstance().getFolder(Constants.FOLDER_NAME + File.separator + Constants.CSS_FOLDER_NAME);
    }

    public void update(Observable arg0, Object arg1) {
        if (arg0 instanceof ProjectObservable) {
            if (arg1 instanceof Integer) {
                if (((Integer) arg1) == ProjectObservable.PROJECT_CLOSED) cssFolder = null;
                if (((Integer) arg1) == ProjectObservable.PROJECT_OPENED) {
                    cssFolder = null;
                    checkTemplateFolder();
                }
            }
        }
    }
}
