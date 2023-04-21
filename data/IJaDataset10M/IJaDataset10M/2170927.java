package br.com.caelum.testslicer.registry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import br.com.caelum.testslicer.ant.ChangedFilesSelector;

public class FileTimeStampChangedFilesSelector implements ChangedFilesSelector {

    private final File baseDir;

    private final RegistryData data;

    public FileTimeStampChangedFilesSelector(File baseDir, RegistryData data) {
        this.baseDir = baseDir;
        this.data = data;
    }

    public List<String> discoverChangedFiles() {
        final List<String> list = new ArrayList<String>();
        recursiveSearch(baseDir, "", new Listener() {

            public void found(String resource, File resourceData) {
                list.add(resource);
            }
        });
        return list;
    }

    interface Listener {

        void found(String resource, File resourceData);
    }

    public void updateChangedFiles() {
        recursiveSearch(baseDir, "", new Listener() {

            public void found(String resource, File resourceData) {
                data.updateTimeStamp(resource, resourceData.lastModified());
            }
        });
    }

    private void recursiveSearch(File dir, String path, Listener listener) {
        if (dir.isHidden()) {
            return;
        }
        String pathSeparator = path.equals("") ? "" : ".";
        if (!dir.isDirectory()) {
            analyseFile(dir, path, listener);
            return;
        }
        for (File child : dir.listFiles()) {
            if (child.isFile() && child.getName().endsWith(".jar")) {
                analyseJar(child);
            } else {
                String childPath = path + pathSeparator + child.getName();
                if (child.isDirectory()) {
                    recursiveSearch(child, childPath, listener);
                } else if (child.isFile()) {
                    analyseFile(child, childPath, listener);
                }
            }
        }
    }

    private void analyseFile(File dir, String path, Listener listener) {
        if (changedTimeStampFor(dir, path)) {
            listener.found(path, dir);
        }
    }

    private boolean changedTimeStampFor(File file, String path) {
        return data.getTimestampFor(path) != file.lastModified();
    }

    private void analyseJar(File jar) {
    }
}
