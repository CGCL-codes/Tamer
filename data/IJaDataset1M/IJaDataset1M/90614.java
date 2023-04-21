package ckjm.plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for finding files within some directories.
 */
public class FileFinder {

    public FileFinder() {
    }

    private FilenameFilter filter;

    private static final String FILE_SEP = System.getProperty("file.separator");

    public List<File> findFilesFrom(String dir, FilenameFilter filter, boolean recurse) {
        this.filter = filter;
        List<File> files = new ArrayList<File>();
        scanDirectory(new File(dir), files, recurse);
        return files;
    }

    /**
     * Implements a tail recursive file scanner
     */
    private void scanDirectory(File dir, List<File> list, boolean recurse) {
        FilterDir filterDir = new FilterDir();
        String[] candidates = dir.list(filter);
        String[] notCandidates = dir.list(filterDir);
        if (candidates == null) {
            return;
        } else if (candidates.length == 0) {
            for (int i = 0; i < notCandidates.length; i++) {
                File tmp2 = new File(dir + FILE_SEP + notCandidates[i]);
                if (recurse) {
                    scanDirectory(tmp2, list, true);
                }
            }
        } else if (notCandidates.length != 0) {
            for (int j = 0; j < candidates.length; j++) {
                File tmp = new File(dir + FILE_SEP + candidates[j]);
                if (tmp.isDirectory()) {
                    if (recurse) {
                        scanDirectory(tmp, list, true);
                    }
                } else {
                    list.add(new File(dir + FILE_SEP + candidates[j]));
                }
            }
        }
    }
}
