package org.equivalence.server;

import java.util.ArrayList;
import org.equivalence.common.FileRepository;
import org.equivalence.common.FileVersion;

/**
 * return the file differences between two file repository objects
 * @author gcc
 *
 */
class FileRepositoryDiff {

    private ArrayList<String> replace;

    private ArrayList<String> remove;

    private ArrayList<String> add;

    public FileRepositoryDiff(FileRepository newest, FileRepository oldest) {
        replace = new ArrayList<String>();
        remove = new ArrayList<String>();
        add = new ArrayList<String>();
        for (int i = 0; i < newest.getFileCount(); ++i) {
            FileVersion tempFile = oldest.getFile(newest.getFile(i).getFilePath());
            if (tempFile == null) {
                add.add(newest.getFile(i).getFilePath());
            } else if (tempFile.getVersion() < newest.getFile(i).getVersion()) {
                replace.add(newest.getFile(i).getFilePath());
            }
        }
        for (int i = 0; i < oldest.getFileCount(); ++i) {
            FileVersion tempFile = newest.getFile(oldest.getFile(i).getFilePath());
            if (tempFile == null) {
                remove.add(oldest.getFile(i).getFilePath());
            }
        }
    }

    /**
	 * @return the add
	 */
    public final ArrayList<String> getAdd() {
        return add;
    }

    /**
	 * @return the remove
	 */
    public final ArrayList<String> getRemove() {
        return remove;
    }

    /**
	 * @return the replace
	 */
    public final ArrayList<String> getReplace() {
        return replace;
    }
}
